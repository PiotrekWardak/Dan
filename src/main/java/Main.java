import javax.mail.Session;
import java.util.Map;

public class Main {

    private static final String FILE_PATH = "kontakty.xlsx";
    private static final String toEmail = "piotrekwardak@gmail.com";

    public static void main(String[] args) {

        Map<String,String> listaNadawcow = ExcelRead.readRecipients(FILE_PATH);


        Session session = Session.getDefaultInstance(ConnectionProperties.getServerProperties(), ConnectionProperties.auth());
        System.out.println("Session created");
        System.out.println(ConnectionProperties.configFileMap.get(ConnectionProperties.cNotListed));

//        EmailUtil.sendEmail(session,ConnectionProperties.configFileMap.get(ConnectionProperties.cLogin), toEmail,"Maigdgdn Testing Subject", "SSLEmfdgdail Testing Body");

    }

}