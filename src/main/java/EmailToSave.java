public class EmailToSave {

       private String email;
       private String receipient;
       private String subject;
       private String content;

    public EmailToSave(String email, String receipient, String subject, String content) {
        this.email = email;
        this.receipient = receipient;
        this.subject = subject;
        this.content = content;
    }

    public String getEmail() {
        return email;
    }

    public String getReceipient() {
        return receipient;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }
}
