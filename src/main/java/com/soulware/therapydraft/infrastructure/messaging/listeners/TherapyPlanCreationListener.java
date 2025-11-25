package com.soulware.therapydraft.infrastructure.messaging.listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.enterprise.inject.spi.CDI;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.time.ZonedDateTime;
import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.soulware.therapydraft.application.commands.CreateTherapyPlanCommand;
import com.soulware.therapydraft.application.commands.ScheduleEntryCommand;
import com.soulware.therapydraft.application.services.commands.TherapyPlanCommandService;
import com.soulware.therapydraft.interfaces.rest.assemblers.TherapyPlanResourceFromEntityAssembler;
import com.soulware.therapydraft.domain.model.aggregates.TherapyPlan;

@WebListener
public class TherapyPlanCreationListener implements ServletContextListener, MessageListener {

    private static final Logger logger = Logger.getLogger(TherapyPlanCreationListener.class.getName());

    private static final String INPUT_QUEUE = "scheduling_createTherapyPlan";
    private static final String OUTPUT_QUEUE = "apigateway_therapyPlanCreated";

    private static Connection connection;
    private static Session session;
    private static MessageConsumer consumer;
    private static MessageProducer producer;
    private static boolean isInitialized = false;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        synchronized (TherapyPlanCreationListener.class) {
            if (isInitialized) {
                logger.info("TherapyPlanCreationListener already initialized, skipping...");
                return;
            }

            try {
                closeConnections();

                ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
                connection = factory.createConnection();
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                Queue inputQueue = session.createQueue(INPUT_QUEUE);
                Queue outputQueue = session.createQueue(OUTPUT_QUEUE);

                consumer = session.createConsumer(inputQueue);
                producer = session.createProducer(outputQueue);

                consumer.setMessageListener(this);
                connection.start();

                isInitialized = true;
                logger.info("SUCCESS: TherapyPlanCreationListener listening on: " + INPUT_QUEUE);

            } catch (Exception e) {
                logger.log(Level.SEVERE, "FAILED to initialize TherapyPlanCreationListener", e);
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("=== TherapyPlanCreationListener STOPPING ===");
        closeConnections();
    }

    private static void closeConnections() {
        try {
            if (consumer != null) { consumer.close(); consumer = null; }
            if (producer != null) { producer.close(); producer = null; }
            if (session != null) { session.close(); session = null; }
            if (connection != null) { connection.close(); connection = null; }
            isInitialized = false;
            logger.info("All JMS resources closed correctly");
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error closing JMS resources", e);
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            logger.info("=== MESSAGE RECEIVED BY TherapyPlanCreationListener ===");

            String jsonPayload = extractMessage(message);
            if (jsonPayload == null) return;

            Jsonb jsonb = JsonbBuilder.create();
            CreateTherapyPlanMessage request = jsonb.fromJson(jsonPayload, CreateTherapyPlanMessage.class);

            // Convert schedule to ScheduleEntryCommand
            List<ScheduleEntryCommand> scheduleCommands = request.schedule == null ? List.of() :
                    request.schedule.stream()
                            .map(e -> new ScheduleEntryCommand(
                                    e.dayOfWeek,
                                    ZonedDateTime.parse(e.startTime),
                                    ZonedDateTime.parse(e.endTime)
                            ))
                            .collect(Collectors.toList());

            // Create command
            CreateTherapyPlanCommand command = new CreateTherapyPlanCommand(
                    request.assessmentId,
                    request.description,
                    request.goals,
                    request.assignedTherapistId,
                    request.legalResponsibleId,
                    scheduleCommands
            );

            // Obtain service via CDI
            TherapyPlanCommandService service = CDI.current().select(TherapyPlanCommandService.class).get();

            // Execute creation
            TherapyPlan created = service.create(command);

            // Convert to resource
            var resource = TherapyPlanResourceFromEntityAssembler.toResourceFromEntity(created);
            String responseJson = jsonb.toJson(resource);

            // Send response
            TextMessage responseMessage = session.createTextMessage(responseJson);
            producer.send(responseMessage);

            logger.info("TherapyPlan created successfully. Response delivered to: " + OUTPUT_QUEUE);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing therapy plan creation message", e);
        }
    }

    private String extractMessage(Message message) throws Exception {
        if (message instanceof TextMessage textMessage) return textMessage.getText();

        if (message instanceof BytesMessage bytesMessage) {
            long len = bytesMessage.getBodyLength();
            if (len <= 0) return null;
            byte[] data = new byte[(int) len];
            bytesMessage.readBytes(data);
            return new String(data, "UTF-8");
        }
        return null;
    }

    // DTOs para recibir mensajes del Gateway
    public static class CreateTherapyPlanMessage {
        public Long assessmentId;
        public String description;
        public String goals;
        public Long assignedTherapistId;
        public Long legalResponsibleId;
        public List<ScheduleEntryMessage> schedule;
        public String requestId;
        public String timestamp;
    }

    public static class ScheduleEntryMessage {
        public String dayOfWeek;
        public String startTime; // ISO-8601
        public String endTime;   // ISO-8601
    }
}
