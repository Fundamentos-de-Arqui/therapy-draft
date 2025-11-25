package com.soulware.therapydraft.infrastructure.messaging.listeners;

import com.soulware.therapydraft.application.queries.GetAssessmentsQuery;
import com.soulware.therapydraft.application.services.queries.AssessmentQueryService;
import com.soulware.therapydraft.interfaces.rest.assemblers.AssessmentResourceFromEntityAssembler;
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
public class AssessmentQueryListener implements ServletContextListener, MessageListener {

    private static final Logger logger = Logger.getLogger(AssessmentQueryListener.class.getName());

    private static final String INPUT_QUEUE = "scheduling_getAssessments";
    private static final String OUTPUT_QUEUE = "apigateway_assessmentsResponse";

    private static Connection connection;
    private static Session session;
    private static MessageConsumer consumer;
    private static MessageProducer producer;
    private static boolean isInitialized = false;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        synchronized (AssessmentQueryListener.class) {
            if (isInitialized) {
                logger.info("AssessmentQueryListener already initialized, skipping...");
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
                logger.info("SUCCESS: AssessmentQueryListener connected to " + INPUT_QUEUE);

            } catch (Exception e) {
                logger.log(Level.SEVERE, "FAILED to initialize AssessmentQueryListener: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("=== AssessmentQueryListener STOPPING ===");
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
            logger.info("=== MESSAGE RECEIVED BY AssessmentQueryListener ===");

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
            GetAssessmentsMessage request = jsonb.fromJson(messageText, GetAssessmentsMessage.class);

            if (request.page < 0 || request.size <= 0) {
                logger.warning("Invalid pagination parameters: page=" + request.page + ", size=" + request.size);
                return;
            }

            // Obtener servicio
            AssessmentQueryService queryService = CDI.current().select(AssessmentQueryService.class).get();

            // Construir query
            GetAssessmentsQuery query = new GetAssessmentsQuery(
                    request.patientId,
                    request.therapistId,
                    request.status,
                    request.scheduledAt,
                    request.page,
                    request.size
            );

            var paged = queryService.getAssessments(query);

            List<?> resourceItems = paged.getItems().stream()
                    .map(AssessmentResourceFromEntityAssembler::toResourceFromEntity)
                    .toList();

            PagedResponseResource<?> response = new PagedResponseResource<>(
                    resourceItems,
                    paged.getTotalItems(),
                    paged.getTotalPages(),
                    paged.getPage(),
                    paged.getSize()
            );

            String responseJson = jsonb.toJson(response);
            TextMessage responseMessage = session.createTextMessage(responseJson);
            producer.send(responseMessage);
            logger.info("Sent assessments response to " + OUTPUT_QUEUE + " with " + resourceItems.size() + " items");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing getAssessments message: " + e.getMessage(), e);
        }
    }

    // DTO para deserializar la solicitud del gateway
    public static class GetAssessmentsMessage {
        public Long patientId;
        public Long therapistId;
        public String status;
        public String scheduledAt;
        public int page;
        public int size;
        public String requestId;
        public String timestamp;
    }
}
