package br.com.app.notification.service;

import br.com.app.transaction.entity.Transaction;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    public NotificationProducer(KafkaTemplate<String, Transaction> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(Transaction transaction) {
        kafkaTemplate.send("transaction-notification", transaction);
    }
}