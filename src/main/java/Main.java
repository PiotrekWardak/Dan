import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {

    private static final String FILE_PATH = "kontakty.xlsx";

    public static void main(String[] args) throws IOException {

        Map<String,String> listaNadawcow = ExcelRead.readRecipients(FILE_PATH);

        EmailSearcher emailSearcher = new EmailSearcher();
        try {
            emailSearcher.searchEmail(listaNadawcow);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<EmailToSave> getListOfTrustedMessages =  emailSearcher.getListOfTrustedMessages();
        List<String> getListOfPeopleToRespond = emailSearcher.getListOfPeopleToRespond();
        EmailUtil.saveEmailOnDrive(getListOfTrustedMessages);


//        ConnectionProperties connectionProperties = new ConnectionProperties();
//        Session sessionSend = Session.getInstance(connectionProperties.sendEmails(), connectionProperties.auth());
//        EmailUtil.sendEmail(sessionSend,connectionProperties.getConfigFileMap().get(connectionProperties.getcLogin()), getListOfPeopleToRespond,
//                            connectionProperties.getcNotListed(), connectionProperties.configFileMap.get(connectionProperties.getcNotListed()));



    }

}