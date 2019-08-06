import KeysValidation.Answers;

import javax.mail.Authenticator;
import javax.mail.Session;
import java.util.Map;
import java.util.Properties;

public class Main {

    private static final String HOST = "smtp.wp.pl";
    private static final String Port = "465";
    private static final String PROTOCOL = "smtp";
    private static final String FROM_EMAIL = "dankapracadomowa@wp.pl";
    private static final String PASSWORD = "123danka";
    private static final String FILE_PATH = "C:\\Users\\Piotrek\\Desktop\\kontakty.xlsx";
    private static final String toEmail = "piotrekwardak@gmail.com";





    public static void main(String[] args) {


        System.out.println(Answers.NOTALLOWED.getDescription());


        Map<String,String> listaNadawcow = ExcelRead.readRecipients(FILE_PATH);



        Properties props = ConnectionProperties.getServerProperties(PROTOCOL,HOST,Port);
        Authenticator auth = ConnectionProperties.auth(FROM_EMAIL,PASSWORD);
        Session session = Session.getDefaultInstance(props, auth);
        System.out.println("Session created");



        EmailUtil.sendEmail(session,FROM_EMAIL, toEmail,"Main Testing Subject", "SSLEmail Testing Body");

    }

}