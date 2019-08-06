//import javax.mail.*;
//import javax.mail.search.SearchTerm;
//import java.util.Properties;
//
//public class EmailSearcher {
//
//
//    public void searchEmail(String host, String port, String userName,
//                            String password, final String keyword) {
//            Properties properties = new Properties();
//
//        // server setting
//        properties.put("mail.imap.host", host);
//        properties.put("mail.imap.port", port);
//
//        // SSL setting
//        properties.setProperty("mail.imap.socketFactory.class",
//                "javax.net.ssl.SSLSocketFactory");
//        properties.setProperty("mail.imap.socketFactory.fallback", "false");
//        properties.setProperty("mail.imap.socketFactory.port",
//                String.valueOf(port));
//
//        Session session = Session.getDefaultInstance(properties);
//
//        try {
//            // connects to the message store
//            Store store = session.getStore("imap");
//            store.connect(userName, password);
//
//            // opens the inbox folder
//            Folder folderInbox = store.getFolder("INBOX");
//            folderInbox.open(Folder.READ_ONLY);
//
//            // creates a search criterion
//            SearchTerm searchCondition = new SearchTerm() {
//                @Override
//                public boolean match(Message message) {
//                    try {
//                        if (message.getSubject().contains(keyword)) {
//                            return true;
//                        }
//                    } catch (MessagingException ex) {
//                        ex.printStackTrace();
//                    }
//                    return false;
//                }
//            };
//
//            // performs search through the folder
//            Message[] foundMessages = folderInbox.search(searchCondition);
//
//            for (int i = 0; i < foundMessages.length; i++) {
//                Message message = foundMessages[i];
//                String subject = message.getSubject();
//                System.out.println("Found message #" + i + ": " + subject);
//            }
//
//            // disconnect
//            folderInbox.close(false);
//            store.close();
//        } catch (NoSuchProviderException ex) {
//            System.out.println("No provider.");
//            ex.printStackTrace();
//        } catch (MessagingException ex) {
//            System.out.println("Could not connect to the message store.");
//            ex.printStackTrace();
//        }
//
//
//    }
//
//    public static void main(String[] args) {
//        String host = "imap.wp.pl";
//        String port = "993";
//        String userName = "dankapracadomowa2@wp.pl";
//        String password = "123danka";
//        EmailSearcher searcher = new EmailSearcher();
//        String keyword = "JavaMail";
//        searcher.searchEmail(host, port, userName, password, keyword);
//    }
//
//
//
//}
