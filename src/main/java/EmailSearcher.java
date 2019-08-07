import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.SearchTerm;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class EmailSearcher {

    private List<EmailToSave> listOfTrustedMesseages = new ArrayList<>();
    private List<String> listOfPeopleToRespond = new ArrayList<>();

    public List<String> getListOfPeopleToRespond() {
        return listOfPeopleToRespond;
    }

    public void addPeopleToRespond(String PeopleToRespond) {
        this.listOfPeopleToRespond.add(PeopleToRespond);
    }

    public List<EmailToSave> getListOfTrustedMesseages() {
        return listOfTrustedMesseages;
    }

    public void addTrustedMesseages(EmailToSave TrustedMesseages) {
        this.listOfTrustedMesseages.add(TrustedMesseages);
    }

    public void searchEmail(Map<String,String> listOfTrustedEmails) throws Exception {
//        Calendar c = Calendar.getInstance();
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
            store.connect(connectionProperties.getConfigFileMap().get(connectionProperties.getcLogin()), connectionProperties.getConfigFileMap().get(connectionProperties.getcPass()));
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
            List<String> listOfPeopleToRespond = new ArrayList<>();
            List<EmailToSave> emailToSaveList = new ArrayList<>();
            for (int i = 0; i < foundMessages.length; i++) {
                Message message = foundMessages[i];

                if(newDateOfEmail.before(message.getReceivedDate())) {
                    newDateOfEmail =message.getReceivedDate();
                    System.out.println(i+" "+newDateOfEmail);
                }

                findTrustedEmails(message,emailToSaveList,listOfPeopleToRespond,listOfTrustedEmails,sdf);
            }

            folderInbox.close(false);
            store.close();
            PrintWriter printWriter = null;
            try {
                printWriter = new PrintWriter("lastEmailDate.txt");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            printWriter.println(sdf.format(newDateOfEmail));
            printWriter.close();

        } catch (NoSuchProviderException ex) {
            ex.printStackTrace();
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }


public void findTrustedEmails(Message message, List<EmailToSave> listToSave, List<String> listOfPeopleToResponse, Map<String, String> mapOfTrustedEmails, SimpleDateFormat sdf){

    try {
        Address[] fromAddress = message.getFrom();
        if(mapOfTrustedEmails.get(((InternetAddress)fromAddress[0]).getAddress())!=null)
        { EmailToSave emailToSave = new EmailToSave(((InternetAddress)fromAddress[0]).getAddress(),sdf.format(message.getReceivedDate()),message.getSubject(),message.getContentType());
        addTrustedMesseages(emailToSave);

        }
        else
        { addPeopleToRespond(((InternetAddress)fromAddress[0]).getAddress());
        }

    } catch (MessagingException e) {
        e.printStackTrace();
    }


}



//    public static void main(String[] args) {
//        Map<String,String> listaNadawcow = ExcelRead.readRecipients("kontakty.xlsx");
//
//        EmailSearcher emailSearcher = new EmailSearcher();
//        try {
//            emailSearcher.searchEmail(listaNadawcow);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }


}
