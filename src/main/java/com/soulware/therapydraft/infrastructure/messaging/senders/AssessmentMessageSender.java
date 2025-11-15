package com.soulware.therapydraft.infrastructure.messaging.senders;

import com.soulware.therapydraft.domain.model.aggregates.Assessment;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;

@ApplicationScoped
public class AssessmentMessageSender {
    private static final Dotenv dotenv = Dotenv.load();

    private static final String BROKER_URL = dotenv.get("BROKER_URL");
    private static final String QUEUE_NAME = dotenv.get("QUEUE_NAME");

    private final Jsonb jsonb = JsonbBuilder.create();

    public void sendAssessmentDone(Assessment assessment) {
        try {
            // Define connection parameters
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                    dotenv.get("BROKER_USER"),
                    dotenv.get("BROKER_PASSWORD"),
                    BROKER_URL
            );

            // Create connection
            Connection connection = factory.createConnection();
            connection.start();

            // Create session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create queue
            Queue queue = session.createQueue(QUEUE_NAME);

            // Create producer
            MessageProducer producer = session.createProducer(queue);

            // Convert assessment to json
            String json = jsonb.toJson(assessment);

            // Create message
            TextMessage message = session.createTextMessage(json);

            // Send
            producer.send(message);

            System.out.println("Message sent to ActiveMQ: " + json);

            // Close resources
            producer.close();
            session.close();
            connection.close();

        } catch (Exception e) {
            throw new RuntimeException("Error sending message to ActiveMQ: ", e);
        }
    }
}