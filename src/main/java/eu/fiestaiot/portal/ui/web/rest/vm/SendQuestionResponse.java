package eu.fiestaiot.portal.ui.web.rest.vm;

import javax.persistence.criteria.CriteriaBuilder;

public class SendQuestionResponse {
    private Integer questionId;
    private boolean status;

    public SendQuestionResponse() {

    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
