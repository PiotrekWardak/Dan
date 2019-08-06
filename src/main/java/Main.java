import javax.mail.Session;
import java.util.Map;

public class Main {

    private static final String FILE_PATH = "C:\\Users\\Piotrek\\Desktop\\DankaPracaDomowa\\kontakty.xlsx";
    private static final String toEmail = "piotrekwardak@gmail.com";

    public static void main(String[] args) {

        Map<String,String> listaNadawcow = ExcelRead.readRecipients(FILE_PATH);

        //Properties props = ;
        //Authenticator auth = ;
        Session session = Session.getDefaultInstance(ConnectionProperties.getServerProperties(), ConnectionProperties.auth());
        System.out.println("Session created");

//        EmailUtil.sendEmail(session,ConnectionProperties.configFileMap.get(ConnectionProperties.cLogin), toEmail,"Maigdgdn Testing Subject", "SSLEmfdgdail Testing Body");

    }

}