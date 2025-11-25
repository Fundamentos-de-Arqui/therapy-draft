package com.soulware.therapydraft.infrastructure.messaging.listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import jakarta.enterprise.inject.spi.CDI;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.soulware.therapydraft.application.commands.ChangeAssessmentStatusCommand;
import com.soulware.therapydraft.application.services.commands.AssessmentCommandService;
import com.soulware.therapydraft.interfaces.rest.assemblers.AssessmentResourceFromEntityAssembler;

@WebListener
public class AssessmentStatusUpdateListener implements ServletContextListener, MessageListener {

    private static final Logger logger = Logger.getLogger(AssessmentStatusUpdateListener.class.getName());

    private static final String INPUT_QUEUE = "scheduling_updateAssessmentStatus";
    private static final String OUTPUT_QUEUE = "apigateway_assessmentStatusUpdated";

    private static Connection connection;
    private static Session session;
    private static MessageConsumer consumer;
    private static MessageProducer producer;
    private static boolean initialized = false;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        synchronized (AssessmentStatusUpdateListener.class) {
            if (initialized) {
                logger.info("AssessmentStatusUpdateListener already initialized.");
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

                initialized = true;

                logger.info("SUCCESS: AssessmentStatusUpdateListener listening on " + INPUT_QUEUE);

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Failed to initialize AssessmentStatusUpdateListener", e);
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("AssessmentStatusUpdateListener shutting down.");
        closeConnections();
    }

    private void closeConnections() {
        try {
            if (consumer != null) { consumer.close(); consumer = null; }
            if (producer != null) { producer.close(); producer = null; }
            if (session != null) { session.close(); session = null; }
            if (connection != null) { connection.close(); connection = null; }

            initialized = false;
            logger.info("All JMS resources closed.");

        } catch (Exception e) {
            logger.log(Level.WARNING, "Error closing JMS resources", e);
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            String json = extractMessage(message);
            if (json == null) return;

            logger.info("Received status update request: " + json);

            Jsonb jsonb = JsonbBuilder.create();
            UpdateStatusMessage payload = jsonb.fromJson(json, UpdateStatusMessage.class);

            ChangeAssessmentStatusCommand command =
                    new ChangeAssessmentStatusCommand(payload.assessmentId, payload.status);

            AssessmentCommandService service =
                    CDI.current().select(AssessmentCommandService.class).get();

            var updated = service.updateStatus(command);

            var resource = AssessmentResourceFromEntityAssembler.toResourceFromEntity(updated);
            String responseJson = jsonb.toJson(resource);

            TextMessage responseMessage = session.createTextMessage(responseJson);
            producer.send(responseMessage);

            logger.info("Status updated and response sent to " + OUTPUT_QUEUE);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing assessment status update", e);
        }
    }

    private String extractMessage(Message message) throws Exception {
        if (message instanceof TextMessage textMsg) {
            return textMsg.getText();
        }

        if (message instanceof BytesMessage bytes) {
            long len = bytes.getBodyLength();
            byte[] data = new byte[(int) len];
            bytes.readBytes(data);
            return new String(data, "UTF-8");
        }

        logger.warning("Unsupported JMS message type received.");
        return null;
    }

    // DTO FROM GATEWAY
    public static class UpdateStatusMessage {
        public Long assessmentId;
        public String status;
        public String timestamp;
        public String requestId;
    }
}
