import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EmailUtil {


    public static void sendEmail(Session session, String fromEmail, List<String> ListOfPeopleToRespond, String subject, String body) {

        int i=0;
        for (String toEmail: ListOfPeopleToRespond) {
            try {
                MimeMessage msg = new MimeMessage(session);

                msg.setFrom(new InternetAddress(fromEmail, "NoReply-JD"));
                msg.setSubject(subject, "UTF-8");
                msg.setText(body, "UTF-8");
                msg.setSentDate(new Date());

                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
                System.out.println("Message is ready");
                Transport.send(msg);

                System.out.println("E-mail "+i+" Sent Successfully!!");
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveTrustedEmails(Map<String,String> listOfTrustedEmails){



    }



}