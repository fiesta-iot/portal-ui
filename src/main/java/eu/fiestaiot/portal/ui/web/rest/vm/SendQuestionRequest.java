package eu.fiestaiot.portal.ui.web.rest.vm;

public class SendQuestionRequest {
    private String name;
    private String email;
    private String body;

    public SendQuestionRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "SendQuestionRequest{" +
            "name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", body='" + body + '\'' +
            '}';
    }
}
