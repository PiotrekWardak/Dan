import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.util.Properties;

public class ConnectionProperties {


    protected static Properties getServerProperties(String protocol, String host,
                                                    String port) {
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

    protected static PasswordAuthentication getPasswordAuthentication(String from_email, String password) {
        return new PasswordAuthentication(from_email, password);
    }

    protected static Authenticator auth(String from_email, String password) {


        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from_email, password);
            }
        };

        return auth;

    }

}
