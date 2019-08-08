import javax.mail.Session;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final String FILE_PATH = "kontakty.xlsx";


    public static void main(String[] args) throws IOException, InterruptedException {

        long time = numberValidation() * 60000;

        while (true) {
            action();
            Thread.sleep(time);
        }
    }


    private static int numberValidation() {

        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Enter how many minutes the break lasts between server check");
            System.out.println("The number is treated as valid when the number is between 1 and 10. ");
            boolean hasNextInt = scanner.hasNextInt();
            if (hasNextInt) {
                int number = scanner.nextInt();

                if (number >= 1 && number <= 10) {
                    return number;
                }

            } else {
                System.out.println("Wrong character, try again");
            }

            scanner.nextLine();


        }
        while (true);

    }

    private static void action() throws IOException {

        Map<String, String> listaNadawcow = ExcelRead.readRecipients(FILE_PATH);

        EmailSearcher emailSearcher = new EmailSearcher();
        try {
            emailSearcher.searchEmail(listaNadawcow);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<EmailToSave> getListOfTrustedMessages = emailSearcher.getListOfTrustedMessages();
        List<String> getListOfPeopleToRespond = emailSearcher.getListOfPeopleToRespond();
        EmailUtil.saveEmailOnDrive(getListOfTrustedMessages);


        ConnectionProperties connectionProperties = new ConnectionProperties();
        Session sessionSend = Session.getInstance(connectionProperties.sendEmails(), connectionProperties.auth());
        EmailUtil.sendEmail(sessionSend, connectionProperties.getConfigFileMap().get(connectionProperties.getcLogin()), getListOfPeopleToRespond,
                connectionProperties.getcNotListed(), connectionProperties.configFileMap.get(connectionProperties.getcNotListed()));
    }

}