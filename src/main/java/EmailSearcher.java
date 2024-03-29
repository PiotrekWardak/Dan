import org.jsoup.Jsoup;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SearchTerm;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class EmailSearcher {

    private List<EmailToSave> listOfTrustedMessages = new ArrayList<>();
    private List<String> listOfPeopleToRespond = new ArrayList<>();

    public List<String> getListOfPeopleToRespond() {
        return listOfPeopleToRespond;
    }

    public void addPeopleToRespond(String PeopleToRespond) {
        this.listOfPeopleToRespond.add(PeopleToRespond);
    }

    public List<EmailToSave> getListOfTrustedMessages() {
        return listOfTrustedMessages;
    }

    public void addTrustedMesseages(EmailToSave TrustedMesseages) {
        this.listOfTrustedMessages.add(TrustedMesseages);
    }

    public void searchEmail(Map<String, String> listOfTrustedEmails) throws Exception {
        ConnectionProperties connectionProperties = new ConnectionProperties();
        Session session = Session.getDefaultInstance(connectionProperties.ReceiveEmails());
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        String[] tab = null;
        File file = new File("lastEmailDate.txt");
        Scanner scanner = new Scanner(file);
        String zdanie;

        do {
            zdanie = scanner.nextLine();
            tab = zdanie.split("_");

        }
        while (scanner.hasNext());
        scanner.close();
        String day = tab[0], month = tab[1], year = tab[2], hour = tab[3], minutes = tab[4], seconds = tab[5];
        Date dateOfTheLastEmail = sdf.parse(day + "_" + month + "_" + year + "_" + hour + "_" + minutes + "_" + seconds);


        try {

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

            Date newDateOfEmail = dateOfTheLastEmail;
            System.out.println("Number of received emails: " + foundMessages.length);
            for (int i = 1; i <= foundMessages.length; i++) {
                Message message = foundMessages[i-1];

                if (newDateOfEmail.before(message.getReceivedDate())) {
                    newDateOfEmail = message.getReceivedDate();
                    System.out.println((i+1) + " " + newDateOfEmail);
                }

                findTrustedEmails(message, listOfTrustedEmails, sdf);

            }

            System.out.println("Wielkosc tablicy Trusted to: " + getListOfTrustedMessages().size());
            System.out.println("Wielkosc tablicy To Respong to: " + getListOfPeopleToRespond().size());

//
//            folderInbox.close(false);
//            store.close();
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


    public void findTrustedEmails(Message message, Map<String, String> mapOfTrustedEmails, SimpleDateFormat sdf) {

        try {
            Address[] fromAddress = message.getFrom();
            if (mapOfTrustedEmails.get(((InternetAddress) fromAddress[0]).getAddress()) != null) {
                EmailToSave emailToSave = null;

                String contentType = message.getContentType();

                try {


                    if (contentType.contains("multipart"))
                    {
                        // content may contain attachments
                        Object content = message.getContent();
                        MimeMultipart multipartToRetrieveText = (MimeMultipart) content;
                        Multipart multiPart = (Multipart) message.getContent();



                        int numberOfParts = multiPart.getCount();
                        List<MimeBodyPart> listParts = new ArrayList<>();
                        for (int partCount = 0; partCount < numberOfParts; partCount++)
                        {
                            MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()))
                            {
                                listParts.add(part);

                            }

                        }
                        emailToSave = new EmailToSave(((InternetAddress) fromAddress[0]).getAddress(), sdf.format(message.getReceivedDate()),
                                message.getSubject(), getTextFromMimeMultipart(multipartToRetrieveText), listParts);

                    }



                    else{
                        emailToSave = new EmailToSave(((InternetAddress) fromAddress[0]).getAddress(), sdf.format(message.getReceivedDate()),
                                message.getSubject(), getTextFromMessage(message), null);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                addTrustedMesseages(emailToSave);

            } else {
                addPeopleToRespond(((InternetAddress) fromAddress[0]).getAddress());
            }


        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }


    private String getTextFromMessage(Message message) throws Exception {
        if (message.isMimeType("text/plain")) {
            return message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            String result = "";
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            int count = mimeMultipart.getCount();
            for (int i = 0; i < count; i++) {
                BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                if (bodyPart.isMimeType("text/plain")) {
                    result = result + "\n" + bodyPart.getContent();
                    break;
                } else if (bodyPart.isMimeType("text/html")) {
                    String html = (String) bodyPart.getContent();
                    result = result + "\n" + Jsoup.parse(html).text();

                }
            }
            return result;
        }
        return "";
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
        String result = "";
        int partCount = mimeMultipart.getCount();
        for (int i = 0; i < partCount; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break;
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = html;
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }
}
