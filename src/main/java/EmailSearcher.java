import javax.mail.*;
import javax.mail.search.SearchTerm;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EmailSearcher {


    public void searchEmail(String userName,
                            String password) throws ParseException {
        Calendar c = Calendar.getInstance();
        ConnectionProperties connectionProperties = new ConnectionProperties();
        Session session = Session.getDefaultInstance(connectionProperties.ReceiveEmails());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String day = "06";
        String month = "08";
        String year = "2019";



        try {
            Date date1 = sdf.parse(day+"/"+month+"/"+year);
            System.out.println(date1.toString());
            Store store = session.getStore("imap");
            store.connect(userName, password);
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);
            SearchTerm searchCondition = new SearchTerm() {
                @Override
                public boolean match(Message message) {
                    try {
                        if (message.getSentDate().after(date1)) {
                            return true;
                        }
                    } catch (MessagingException ex) {
                        ex.printStackTrace();
                    }
                    return false;
                }
            };

            // performs search through the folder
            Message[] foundMessages = folderInbox.search(searchCondition);

            for (int i = 0; i < foundMessages.length; i++) {
                Message message = foundMessages[i];
                String subject = message.getSubject();
                String messageDate = message.getReceivedDate().toString();
                System.out.println("Found message #" + i + ": " + subject+ ": " + messageDate);
            }

            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            ex.printStackTrace();
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }


    }

    public static void main(String[] args) {

        String userName = "dankapracadomowa2@wp.pl";
        String password = "123danka";
        EmailSearcher searcher = new EmailSearcher();
        try {
            searcher.searchEmail(userName, password);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }



}
