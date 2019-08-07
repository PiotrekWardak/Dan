public class EmailToSave {

       private String email;
       private String time;
       private String subject;
       private String content;

    public EmailToSave(String email, String time, String subject, String content) {
        this.email = email;
        this.time = time;
        this.subject = subject;
        this.content = content;
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
