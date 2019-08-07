import javax.mail.*;
import javax.mail.search.SearchTerm;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class EmailSearcher {


    public void searchEmail(String userName,
                            String password) throws Exception {
        Calendar c = Calendar.getInstance();
        ConnectionProperties connectionProperties = new ConnectionProperties();
        Session session = Session.getDefaultInstance(connectionProperties.ReceiveEmails());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy/HH/mm/ss");
        String [] tab = null;
        File file = new File("lastEmailDate.txt");
        Scanner scanner = new Scanner(file);
        String zdanie;

        do{
            zdanie = scanner.nextLine();
            tab = zdanie.split("/");

        }
        while (scanner.hasNext());
        scanner.close();
        String day = tab[0],month =tab[1], year = tab[2],hour=tab[3],minutes=tab[4],seconds=tab[5];
        Date dateOfTheLastEmail = sdf.parse(day+"/"+month+"/"+year+"/"+hour+"/"+minutes+"/"+seconds);



        try {

//            System.out.println(dateOfTheLastEmail.toString());
            Store store = session.getStore("imap");
            store.connect(userName, password);
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);
            SearchTerm searchCondition = new SearchTerm() {
                @Override
                public boolean match(Message message) {
                    try {
                        if (message.getSentDate().after(dateOfTheLastEmail)) {
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

            Date newDateOfEmail =dateOfTheLastEmail;
            for (int i = 0; i < foundMessages.length; i++) {
                Message message = foundMessages[i];

                if(newDateOfEmail.before(message.getReceivedDate())) {
                    newDateOfEmail =message.getReceivedDate();
                    System.out.println(i+" "+newDateOfEmail);
                }
                String subject = message.getSubject();
                String messageDate = sdf.format(message.getReceivedDate());
                System.out.println("Found message #" + i + ": " + subject + ": " + messageDate);
            }

            folderInbox.close(false);
            store.close();
            System.out.println(sdf.format(newDateOfEmail));
            FileWriter addNewDateToFile = null;
            try {
                addNewDateToFile = new FileWriter("lastEmailDate.txt",true);
                addNewDateToFile.append("aaaa");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

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
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



}
