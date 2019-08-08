import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

public class EmailUtil {


    public static void sendEmail(Session session, String fromEmail, List<String> ListOfPeopleToRespond, String subject, String body) {

        int i = 0;
        for (String toEmail : ListOfPeopleToRespond) {
            try {
                MimeMessage msg = new MimeMessage(session);
                System.out.println("The message will be send to: "+toEmail);
                msg.setFrom(new InternetAddress(fromEmail, "NoReply-JD"));
                msg.setSubject(subject, "UTF-8");
                msg.setText(body, "UTF-8");
                msg.setSentDate(new Date());

                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
                System.out.println("Message is prepared");
                Transport.send(msg);

                System.out.println("Email " + i + " sent successfully!!");
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveEmailOnDrive(List<EmailToSave> listOfTrustedEmails) throws IOException {

        for (EmailToSave emailToSave : listOfTrustedEmails) {
            PrintWriter printWriter = null;
            File dir = new File("zapisaneWiadomosci/" + emailToSave.getEmail());
                if (dir.exists()) {

                File tmp = new File(dir, emailToSave.getTime() + "_" + emailToSave.getSubject() + ".txt");
                if (tmp.exists()) {
                    System.out.println("That message - "+emailToSave.getSubject() +" - was saved previously");
                } else {

                tmp.createNewFile();
                printWriter = new PrintWriter(tmp.getAbsoluteFile());
                printWriter.println(emailToSave.getContent());
                printWriter.close();
                }

            } else {


                dir.mkdirs();
                File tmp = new File(dir, emailToSave.getTime() + "_" + emailToSave.getSubject() + ".txt");
                tmp.createNewFile();
                printWriter = new PrintWriter(tmp.getAbsoluteFile());
                printWriter.println(emailToSave.getContent());
                printWriter.close();

            }

        }


    }


}