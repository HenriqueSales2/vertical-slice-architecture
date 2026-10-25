package br.com.app.notification.service;

import br.com.app.notification.exception.NotificationException;
import br.com.app.notification.entity.Notification;
import br.com.app.transaction.entity.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NotificationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);

    private final RestClient restClient;

    public NotificationConsumer() {
        this.restClient = RestClient.builder()
                .baseUrl("https://quickmock.dev/m/eicsQHwp2fCL")
                .build();
    }

    @KafkaListener(topics = "transaction-notification", groupId = "picpay-desafio-backend")
    public void receiveNotification(Transaction transaction) {

        logger.info("Notifying transaction {}...", transaction);

        var response = restClient.get()
                .retrieve()
                .toEntity(Notification.class);

        if (response.getStatusCode().isError() || !response.getBody().message())
            throw new NotificationException("Error sending notification!");

        logger.info("Notification has been sent {}...", transaction);
    }
}