package com.soulware.therapydraft.infrastructure.messaging.listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import jakarta.enterprise.inject.spi.CDI;

import java.time.ZonedDateTime;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.soulware.therapydraft.application.commands.CreateAssessmentCommand;
import com.soulware.therapydraft.application.services.commands.AssessmentCommandService;
import com.soulware.therapydraft.interfaces.rest.assemblers.AssessmentResourceFromEntityAssembler;

@WebListener
public class AssessmentCreationListener implements ServletContextListener, MessageListener {

    private static final Logger logger = Logger.getLogger(AssessmentCreationListener.class.getName());

    private static final String INPUT_QUEUE = "scheduling_createReassessmentSession";
    private static final String OUTPUT_QUEUE = "apigateway_reassessmentSessionCreated";

    private static Connection connection;
    private static Session session;
    private static MessageConsumer consumer;
    private static MessageProducer producer;
    private static boolean isInitialized = false;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        synchronized (AssessmentCreationListener.class) {
            if (isInitialized) {
                logger.info("AssessmentCreationListener already initialized, skipping...");
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

                logger.info("SUCCESS: AssessmentCreationListener now listening on: " + INPUT_QUEUE);

            } catch (Exception e) {
                logger.log(Level.SEVERE, "FAILED to initialize AssessmentCreationListener", e);
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("=== AssessmentCreationListener STOPPING ===");
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
            logger.log(Level.WARNING, "Error closing ActiveMQ resources", e);
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            logger.info("=== MESSAGE RECEIVED BY AssessmentCreationListener ===");

            String jsonPayload = extractMessage(message);
            if (jsonPayload == null) return;

            logger.info("Received Assessment Create Request: " + jsonPayload);

            Jsonb jsonb = JsonbBuilder.create();
            CreateAssessmentMessage request = jsonb.fromJson(jsonPayload, CreateAssessmentMessage.class);

            // Convert incoming message to domain command
            CreateAssessmentCommand command =
                    new CreateAssessmentCommand(request.patientId, request.therapistId, request.scheduledTo);

            // Obtain service via CDI
            AssessmentCommandService service =
                    CDI.current().select(AssessmentCommandService.class).get();

            var createdAssessment = service.create(command);

            var resource = AssessmentResourceFromEntityAssembler.toResourceFromEntity(createdAssessment);
            String responseJson = jsonb.toJson(resource);

            // Send response back
            TextMessage responseMessage = session.createTextMessage(responseJson);
            producer.send(responseMessage);

            logger.info("Assessment created successfully. Response delivered to: " + OUTPUT_QUEUE);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing assessment creation JMS message", e);
        }
    }

    private String extractMessage(Message message) throws Exception {
        if (message instanceof TextMessage textMessage) {
            return textMessage.getText();
        }

        if (message instanceof BytesMessage bytesMessage) {
            long len = bytesMessage.getBodyLength();
            if (len <= 0) {
                logger.warning("Received empty BytesMessage");
                return null;
            }
            byte[] data = new byte[(int) len];
            bytesMessage.readBytes(data);
            String text = new String(data, "UTF-8");
            logger.info("Converted BYTES message to TEXT: " + text);
            return text;
        }

        logger.warning("Unsupported JMS message type: " + message.getClass().getSimpleName());
        return null;
    }

    // DTO expected from the gateway
    public static class CreateAssessmentMessage {
        public Long patientId;
        public Long therapistId;
        public ZonedDateTime scheduledTo;
        public String timestamp;
        public String requestId;
    }
}
