package EmailUtils;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

import java.io.IOException;


public class SendMessage {

    // This value is set as an input parameter
    private static String SENDER = "";

    // This value is set as an input parameter
    private static String RECIPIENT = "";

    // This value is set as an input parameter
    private static String SUBJECT = "";

    private static String MSG = "";

    private static Context CONTEXT;

    // The email body for recipients with non-HTML email clients
    private static String BODY_TEXT = "Hello,\r\n" + "Here is a list of almost due bills' links.\n";

    // The HTML body of the email
    private static String BODY_HTML = "";

    public static void sendMessage(String sender, String recipient,
                                   String due_in , String msg, Context context) throws IOException {

        SENDER = sender;
        RECIPIENT = recipient;
        SUBJECT = "These bills will due in " + due_in + "days";
        MSG = msg.replaceAll(",", "\n");
        CONTEXT = context;

        BODY_HTML = "<html>" + "<head></head>" + "<body>" + "<h1>Hello!</h1>"
                + "<p>Here is a list of almost due bills' links.</p>" + "<p>" + MSG+ "</p>" + "</body>" + "</html>";

        BODY_TEXT = "Hello,\r\n" + "Here is a list of almost due bills' links.\n" + MSG;

        CONTEXT.getLogger().log(">>>>>> Start sending Email process.");
        try {

            send();
        } catch (IOException  e) {
            e.getStackTrace();
        }
    }

    public static void send() throws IOException {

        try {
            AmazonSimpleEmailService client =
                    AmazonSimpleEmailServiceClientBuilder.standard()
                            // Replace US_WEST_2 with the AWS Region you're using for
                            // Amazon SES.
                            .withRegion(Regions.US_EAST_1).build();
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(RECIPIENT))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset("UTF-8").withData(BODY_HTML))
                                    .withText(new Content()
                                            .withCharset("UTF-8").withData(BODY_TEXT)))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData(SUBJECT)))
                    .withSource(SENDER);
                    // Comment or remove the next line if you are not using a
                    // configuration set
//                    .withConfigurationSetName(CONFIGSET);
            client.sendEmail(request);
            System.out.println("Email sent!");
        } catch (Exception ex) {
            System.out.println("The email was not sent. Error message: "
                    + ex.getMessage());
        }
    }

//    public static void send() throws MessagingException, IOException {
//
//        Session session = Session.getDefaultInstance(new Properties());
//
//
//        // Create a new MimeMessage object
//        MimeMessage message = new MimeMessage(session);
//        CONTEXT.getLogger().log(">>>>>> MimeMessage : " + message.getContent().toString());
//
//        // Add subject, from and to lines
//        message.setSubject(SUBJECT, "UTF-8");
//        message.setFrom(new InternetAddress(SENDER));
//        CONTEXT.getLogger().log(">>>>>> Sender : " + new InternetAddress(SENDER));
//
//        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(RECIPIENT));
//        CONTEXT.getLogger().log(">>>>>> recipient : " + Arrays.toString(InternetAddress.parse(RECIPIENT)));
//
//        CONTEXT.getLogger().log(">>>>>> MimeMessage : " + message.getContent().toString());
//
//        // Create a multipart/alternative child container
//        MimeMultipart msgBody = new MimeMultipart("alternative");
//
//        // Create a wrapper for the HTML and text parts
//        MimeBodyPart wrap = new MimeBodyPart();
//
//        // Define the text part
//        MimeBodyPart textPart = new MimeBodyPart();
//        textPart.setContent(BODY_TEXT + MSG, "text/plain; charset=UTF-8");
//        CONTEXT.getLogger().log(">>>>>> MimeBodyPart txt: " + textPart);
//
//        // Define the HTML part
//        MimeBodyPart htmlPart = new MimeBodyPart();
//        htmlPart.setContent(BODY_HTML, "text/html; charset=UTF-8");
//        CONTEXT.getLogger().log(">>>>>> MimeBodyPart html: " + htmlPart);
//
//        // Add the text and HTML parts to the child container
//        msgBody.addBodyPart(textPart);
//        msgBody.addBodyPart(htmlPart);
//
//        // Add the child container to the wrapper object
//        wrap.setContent(msgBody);
//
//        // Create a multipart/mixed parent container
//        MimeMultipart msg = new MimeMultipart("mixed");
//
//        // Add the parent container to the message
//        message.setContent(msg);
//
//        // Add the multipart/alternative part to the message
//        msg.addBodyPart(wrap);
//
//
//        try {
//            System.out.println("Attempting to send an email through Amazon SES " + "using the AWS SDK for Java...");
//
//            Region region = Region.US_EAST_1;
//
//            SesClient client = SesClient.builder().region(region).build();
//
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            message.writeTo(outputStream);
//
//            ByteBuffer buf = ByteBuffer.wrap(outputStream.toByteArray());
//
//            byte[] arr = new byte[buf.remaining()];
//            buf.get(arr);
//
//            CONTEXT.getLogger().log(">>>>>> buf.  : " + buf);
//
//
//            SdkBytes data = SdkBytes.fromByteArray(arr);
//
//            RawMessage rawMessage = RawMessage.builder()
//                    .data(data)
//                    .build();
//
//            SendRawEmailRequest rawEmailRequest = SendRawEmailRequest.builder()
//                    .rawMessage(rawMessage)
//                    .build();
//
//            client.sendRawEmail(rawEmailRequest);
//            CONTEXT.getLogger().log(">>>>>> sendRawEmail.");
//
//        } catch (SdkException e) {
//            e.getStackTrace();
//        }
//    }




}