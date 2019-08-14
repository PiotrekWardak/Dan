import com.google.api.client.util.Lists;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class EmailUtil {

    private static Set<String> wrongAttachmentToSendInfoSet = new HashSet<>();
    private static List<String> wrongAttachmentToSendInfoList = new ArrayList<>();

    public static List<String> getWrongAttachmentToSendInfoList() {
        return wrongAttachmentToSendInfoList;
    }

    public static void sendEmail(Session session, String fromEmail, List<String> ListOfPeopleToRespond, String subject, String body) {

        int i = 0;
        for (String toEmail : ListOfPeopleToRespond) {
            try {
                MimeMessage msg = new MimeMessage(session);
                System.out.println("The message will be send to: " + toEmail);
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

    public static void saveEmailOnDrive(List<EmailToSave> listOfTrustedEmails) throws IOException, MessagingException {

        for (EmailToSave emailToSave : listOfTrustedEmails) {
            PrintWriter printWriter = null;
            File dir1EmailAddress = new File("zapisaneWiadomosci/" + emailToSave.getEmail());
            File dir2EmailTime = new File("zapisaneWiadomosci/" + emailToSave.getEmail() + "/" + emailToSave.getTime());
            if (dir1EmailAddress.exists()) {


                File tmp = new File(dir1EmailAddress, emailToSave.getTime() + "_" + emailToSave.getSubject() + ".txt");
                if (tmp.exists()) {
                    System.out.println("That message - " + emailToSave.getSubject() + " - was saved previously");
                } else {

                    zapis(emailToSave, dir1EmailAddress, tmp);
                }

            } else {


                dir1EmailAddress.mkdirs();
                File tmp = new File(dir1EmailAddress, emailToSave.getTime() + "_" + emailToSave.getSubject() + ".txt");
                zapis(emailToSave, dir1EmailAddress, tmp);
            }
        }
    }

    private static void zapis(EmailToSave emailToSave, File dir1EmailAddress, File tmp) throws IOException, MessagingException {
        PrintWriter printWriter;
        tmp.createNewFile();
        printWriter = new PrintWriter(tmp.getAbsoluteFile());
        printWriter.println(emailToSave.getContent());
        printWriter.close();
        EmailUtil emailUtil = new EmailUtil();
        for (MimeBodyPart mimeBodyPart : emailToSave.getmimeBodyPartList()) {

            if (mimeBodyPart != null) {
                String fileName = mimeBodyPart.getFileName();
                String extension = getFileExtension(fileName);
                if (extension.equals("pdf") || extension.equals("docx") || extension.equals("zip") || extension.equals("xades")) {
                    mimeBodyPart.saveFile(dir1EmailAddress + File.separator + fileName);
                } else {

                    wrongAttachmentToSendInfoSet.add(emailToSave.getEmail());

                }
            }
        }
        wrongAttachmentToSendInfoList = Lists.newArrayList(wrongAttachmentToSendInfoSet);
    }

    private static String getFileExtension(String name) {

        if (name.lastIndexOf(".") != -1 && name.lastIndexOf(".") != 0)
            return name.substring(name.lastIndexOf(".") + 1);
        else return "";
    }


}