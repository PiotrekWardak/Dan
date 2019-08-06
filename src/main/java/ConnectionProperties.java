import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConnectionProperties {

    protected static Map<String,String> configFileMap=new HashMap<>();
     static String cLogin = "LOGIN",cPass="PASS", cSmtpServer="SMTP_SERVER", cSmtpPort="SMTP_PORT", cNotListed="NOTLISTED", cNotAllowed="NOTALLOWED";

    public static void readconfig() {


        Properties p=new Properties();
        try {
            FileInputStream input = new FileInputStream(new File("config.properties"));
            p.load(new InputStreamReader(input, Charset.forName("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }


        configFileMap.put(cLogin,p.getProperty(cLogin));
        configFileMap.put(cPass,p.getProperty(cPass));
        configFileMap.put(cSmtpServer,p.getProperty(cSmtpServer));
        configFileMap.put(cSmtpPort,p.getProperty(cSmtpPort));
        configFileMap.put(cSmtpPort,p.getProperty(cSmtpPort));
        configFileMap.put(cNotListed,p.getProperty(cNotListed));
        configFileMap.put(cNotAllowed,p.getProperty(cNotAllowed));


    }



    protected static Properties getServerProperties() {

        readconfig();

        String host,port,protocol;
        host = configFileMap.get(cSmtpServer);
        port = configFileMap.get(cSmtpPort);
        protocol = host.substring(0,4);

        java.util.Properties properties = new java.util.Properties();
        // server setting
        properties.put(String.format("mail.%s.host", protocol), host);
        properties.put(String.format("mail.%s.port", protocol), port);

        // SSL setting
        properties.setProperty(
                String.format("mail.%s.socketFactory.class", protocol),
                "javax.net.ssl.SSLSocketFactory");
        properties.setProperty(
                String.format("mail.%s.auth", protocol), "true");
        properties.setProperty(
                String.format("mail.%s.socketFactory.port", protocol),
                String.valueOf(port));

        return properties;
    }



    protected static Authenticator auth() {

        String from_email,password;

        from_email = configFileMap.get(cLogin);
        password = configFileMap.get(cPass);


        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from_email, password);
            }
        };

        return auth;

    }

}
