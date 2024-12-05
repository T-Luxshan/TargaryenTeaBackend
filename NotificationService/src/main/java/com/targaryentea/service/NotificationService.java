package com.targaryentea.service;

import com.targaryentea.entity.Notification;
import com.targaryentea.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {
    @Autowired
    private final NotificationRepository notificationRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @KafkaListener(topics = "payment-success-topic", groupId = "payment-service-group")
    public void listenPaymentSuccess(String message) {
        try {
            // Create a simple mail message
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject(message+" join with us to adding value to your day!");
            notificationRepository.save(mailMessage);
            sendEmailToCustomer(mailMessage);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }

    private void sendEmailToCustomer(SimpleMailMessage mailMessage) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mailMessage.setSubject("Payment Successful!");
            mailMessage.setText("Thank you for your payment. Details:\n" + messageContent);
            mailMessage.setFrom("your-email@example.com");

            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }


}
