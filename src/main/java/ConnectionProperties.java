import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConnectionProperties {

    protected Map<String,String> configFileMap=new HashMap<>();
    private String cLogin = "LOGIN",cPass="PASS", cSmtpServer="SMTP_SERVER", cSmtpPort="SMTP_PORT", cNotListed="NOTLISTED",
            cNotAllowed="NOTALLOWED", cImapServer="IMAP_SERVER", cImapPort="IMAP_PORT";

    public String getcNotListed() {
        return cNotListed;
    }

    public String getcLogin() {
        return cLogin;
    }

    public String getcPass() {
        return cPass;
    }

    public Map<String, String> getConfigFileMap() {
        return configFileMap;
    }

    public  void readconfig() {


        Properties p=new Properties();
        try {
            FileInputStream input = new FileInputStream(new File("config.properties"));
            p.load(new InputStreamReader(input, StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }


        configFileMap.put(cLogin,p.getProperty(cLogin));
        configFileMap.put(cPass,p.getProperty(cPass));
        configFileMap.put(cSmtpServer,p.getProperty(cSmtpServer));
        configFileMap.put(cSmtpPort,p.getProperty(cSmtpPort));
        configFileMap.put(cImapServer,p.getProperty(cImapServer));
        configFileMap.put(cImapPort,p.getProperty(cImapServer));
        configFileMap.put(cNotListed,p.getProperty(cNotListed));
        configFileMap.put(cNotAllowed,p.getProperty(cNotAllowed));


    }

    protected Properties sendEmails(){

        return getProperties(cSmtpServer, cSmtpPort);
    }

    private Properties getProperties(String cSmtpServer, String cSmtpPort) {
        readconfig();
        String host,port;
        host = configFileMap.get(cSmtpServer);
        port = configFileMap.get(cSmtpPort);
        Properties properties = getServerProperties(host,port);
        return properties;
    }

    protected Properties ReceiveEmails(){

        return getProperties(cImapServer, cImapPort);
    }


    protected  Properties getServerProperties(String host, String port) {

        readconfig();

        String protocol;
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



    protected  Authenticator auth() {

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
