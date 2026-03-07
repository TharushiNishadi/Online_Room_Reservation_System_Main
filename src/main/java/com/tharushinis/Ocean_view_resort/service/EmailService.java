package com.tharushinis.Ocean_view_resort.service;

import com.tharushinis.Ocean_view_resort.model.Bill;
import com.tharushinis.Ocean_view_resort.model.Customer;
import com.tharushinis.Ocean_view_resort.model.Item;


import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.util.Properties;
import java.util.List;

public class EmailService {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_FROM = "pahanaedubilling@gmail.com"; // Change this
    private static final String EMAIL_PASSWORD = "mjtd dlnr beuj phvh"; // Use App Password

    private Session session;

    public EmailService() {
        // Setup mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        // Create session with authentication
        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD);
            }
        });
    }

    /**
     * Send low stock alert email
     * @param items List of low stock items
     * @param recipientEmail Admin email
     */
    public void sendLowStockAlert(List<Item> items, String recipientEmail) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EMAIL_FROM));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject("Low Room Availability Alert - Ocean View Resort");

        // Build email content
        StringBuilder content = new StringBuilder();
        content.append("<html><body>");
        content.append("<h2>Low Stock Alert</h2>");
        content.append("<p>The following items are running low on stock:</p>");
        content.append("<table border='1' cellpadding='5'>");
        content.append("<tr><th>Item Name</th><th>Current Stock</th><th>Action</th></tr>");

        for (Item item : items) {
            content.append("<tr>");
            content.append("<td>").append(item.getClass()).append("</td>");
            content.append("<td style='color: red;'>").append(item.getStockQuantity()).append("</td>");
            content.append("<td>Reorder Required</td>");
            content.append("</tr>");
        }

        content.append("</table>");
        content.append("<p>Please take necessary action to update room availability.</p>");
        content.append("<p>Best regards,<br>Ocean View Resort Reservation System</p>");
        content.append("</body></html>");

        message.setContent(content.toString(), "text/html");

        Transport.send(message);
    }

    /**
     * Send bill receipt email to customer
     * @param bill Bill object
     * @param pdfBytes PDF bill as byte array
     */
    public void sendBillReceipt(Bill bill, byte[] pdfBytes) throws MessagingException {
        Customer customer = bill.getCustomer();

        if (customer.getEmail() == null || customer.getEmail().isEmpty()) {
            return; // No email to send to
        }

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EMAIL_FROM));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(customer.getEmail()));
        message.setSubject("Reservation Receipt - " + bill.getBillNumber() + " - Ocean View Resort");

        // Create multipart message
        Multipart multipart = new MimeMultipart();

        // Text part
        MimeBodyPart textPart = new MimeBodyPart();
        String emailContent = buildBillEmailContent(bill);
        textPart.setContent(emailContent, "text/html");
        multipart.addBodyPart(textPart);

        // PDF attachment
        if (pdfBytes != null) {
            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource dataSource = new ByteArrayDataSource(pdfBytes, "application/pdf");
            attachmentPart.setDataHandler(new DataHandler(dataSource));
            attachmentPart.setFileName("Bill_" + bill.getBillNumber() + ".pdf");
            multipart.addBodyPart(attachmentPart);
        }

        message.setContent(multipart);
        Transport.send(message);
    }

    private String buildBillEmailContent(Bill bill) {
        StringBuilder content = new StringBuilder();
        content.append("<html><body>");
        content.append("<h2>Thank you for your purchase!</h2>");
        content.append("<p>Dear ").append(bill.getCustomer().getName()).append(",</p>");
        content.append("<p>Your bill has been generated successfully. Please find the details below:</p>");
        content.append("<ul>");
        content.append("<li><strong>Bill Number:</strong> ").append(bill.getBillNumber()).append("</li>");
        content.append("<li><strong>Date:</strong> ").append(bill.getBillDate()).append("</li>");
        content.append("<li><strong>Total Amount:</strong> Rs. ").append(String.format("%.2f", bill.getTotalAmount())).append("</li>");
        content.append("</ul>");
        content.append("<p>The detailed reservation confirmation is attached as a PDF document.</p>");
        content.append("<p>Thank you for choosing Ocean View Resort!</p>");
        content.append("<p>Best regards,<br>Ocean View Resort Team</p>");
        content.append("</body></html>");

        return content.toString();
    }

    /**
     * Send welcome email to new customer
     * @param customer New customer
     */
    public void sendWelcomeEmail(Customer customer) throws MessagingException {
        if (customer.getEmail() == null || customer.getEmail().isEmpty()) {
            return;
        }

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EMAIL_FROM));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(customer.getEmail()));
        message.setSubject("Welcome to Ocean View Resort!");

        String content = "<html><body>" +
                "<h2>Welcome to Ocean View Resort!</h2>" +
                "<p>Dear " + customer.getName() + ",</p>" +
                "<p>Thank you for registering with us. We're excited to have you as our customer.</p>" +
                "<p>You can now manage your room reservations with us. If you have any questions, please don't hesitate to contact us.</p>" +
                "<p>Best regards,<br>Ocean View Resort Team</p>" +
                "</body></html>";

        message.setContent(content, "text/html");
        Transport.send(message);
    }
}
