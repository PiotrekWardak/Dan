package KeysValidation;

public enum Answers {

    NOTLISTED("Adres, z którego wysłano wiadomość nie znajduje się na liście. Proszę o kontakt z biurem."),
    NOTALLOWED("Przesłany załącznik nie znajduje się na liście akceptowanych formatów plików. Proszę o kontakt z biurem.");

    public String description;


    Answers(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
