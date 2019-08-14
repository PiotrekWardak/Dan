import javax.mail.internet.MimeBodyPart;
import java.util.List;

public class EmailToSave {

       private String email;
       private String time;
       private String subject;
       private String content;
       private List<MimeBodyPart> mimeBodyPartList;

    public EmailToSave(String email, String time, String subject, String content, List<MimeBodyPart> mimeBodyPartList) {
        this.email = email;
        this.time = time;
        this.subject = subject;
        this.content = content;
        this.mimeBodyPartList = mimeBodyPartList;
    }

    public String getEmail() {
        return email;
    }

    public String getTime() {
        return time;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public List<MimeBodyPart> getmimeBodyPartList() {
        return mimeBodyPartList;
    }

    @Override
    public String toString() {
        return "EmailToSave{" +
                "email='" + email + '\'' +
                ", time='" + time + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
