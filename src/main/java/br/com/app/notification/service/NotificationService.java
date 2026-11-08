package br.com.app.notification.service;

import br.com.app.transaction.entity.Transaction;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationProducer notificationProducer;

    public NotificationService(NotificationProducer notificationProducer) {
        this.notificationProducer = notificationProducer;
    }

    public void notify(Transaction transaction) {
        notificationProducer.sendNotification(transaction);
    }
}