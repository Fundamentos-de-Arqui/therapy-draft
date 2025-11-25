package com.soulware.therapydraft.infrastructure.messaging.listeners;

import com.soulware.therapydraft.application.queries.GetTherapyPlansQuery;
import com.soulware.therapydraft.application.services.queries.TherapyPlanQueryService;
import com.soulware.therapydraft.interfaces.rest.assemblers.TherapyPlanResourceFromEntityAssembler;
import com.soulware.therapydraft.interfaces.rest.resources.PagedResponseResource;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.enterprise.inject.spi.CDI;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

@WebListener
public class TherapyPlanQueryListener implements ServletContextListener, MessageListener {

    private static final Logger logger = Logger.getLogger(TherapyPlanQueryListener.class.getName());

    private static final String INPUT_QUEUE = "scheduling_getTherapyPlans";
    private static final String OUTPUT_QUEUE = "apigateway_therapyPlansResponse";

    private static Connection connection;
    private static Session session;
    private static MessageConsumer consumer;
    private static MessageProducer producer;
    private static boolean isInitialized = false;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        synchronized (TherapyPlanQueryListener.class) {
            if (isInitialized) {
                logger.info("TherapyPlanQueryListener already initialized, skipping...");
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
                logger.info("SUCCESS: TherapyPlanQueryListener connected to " + INPUT_QUEUE);

            } catch (Exception e) {
                logger.log(Level.SEVERE, "FAILED to initialize TherapyPlanQueryListener: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("=== TherapyPlanQueryListener STOPPING ===");
        closeConnections();
    }

    private static void closeConnections() {
        try {
            if (consumer != null) { consumer.close(); consumer = null; }
            if (producer != null) { producer.close(); producer = null; }
            if (session != null) { session.close(); session = null; }
            if (connection != null) { connection.close(); connection = null; }
            isInitialized = false;
            logger.info("JMS connections closed successfully");
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error closing JMS resources: " + e.getMessage(), e);
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            logger.info("=== MESSAGE RECEIVED BY TherapyPlanQueryListener ===");

            String messageText = null;
            if (message instanceof TextMessage textMessage) {
                messageText = textMessage.getText();
                logger.info("Received TEXT message: " + messageText);
            } else if (message instanceof BytesMessage bytesMessage) {
                long length = bytesMessage.getBodyLength();
                if (length > 0) {
                    byte[] data = new byte[(int) length];
                    bytesMessage.readBytes(data);
                    messageText = new String(data, "UTF-8");
                    logger.info("Converted BYTES to TEXT: " + messageText);
                } else {
                    logger.warning("Empty BytesMessage received");
                    return;
                }
            } else {
                logger.warning("Unsupported JMS message type: " + message.getClass().getSimpleName());
                return;
            }

            Jsonb jsonb = JsonbBuilder.create();
            GetTherapyPlansMessage request = jsonb.fromJson(messageText, GetTherapyPlansMessage.class);

            if (request.page < 0 || request.size <= 0) {
                logger.warning("Invalid pagination parameters: page=" + request.page + ", size=" + request.size);
                return;
            }

            TherapyPlanQueryService queryService = CDI.current().select(TherapyPlanQueryService.class).get();

            GetTherapyPlansQuery query = new GetTherapyPlansQuery(
                    request.assessmentId,
                    request.therapistId,
                    request.patientId,
                    request.legalResponsibleId,
                    request.page,
                    request.size
            );

            var paged = queryService.getTherapyPlans(query);

            List<?> resourceItems = paged.getItems().stream()
                    .map(TherapyPlanResourceFromEntityAssembler::toResourceFromEntity)
                    .toList();

            PagedResponseResource<?> response = new PagedResponseResource<>(
                    resourceItems,
                    paged.getTotalItems(),
                    paged.getTotalPages(),
                    paged.getPage(),
                    paged.getSize()
            );

            String responseJson = jsonb.toJson(response);
            producer.send(session.createTextMessage(responseJson));
            logger.info("Sent therapy plans response to " + OUTPUT_QUEUE + " with " + resourceItems.size() + " items");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing getTherapyPlans message: " + e.getMessage(), e);
        }
    }

    public static class GetTherapyPlansMessage {
        public Long assessmentId;
        public Long therapistId;
        public Long patientId;
        public Long legalResponsibleId;
        public int page;
        public int size;
        public String requestId;
        public String timestamp;
    }
}
