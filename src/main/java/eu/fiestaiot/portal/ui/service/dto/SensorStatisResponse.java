package eu.fiestaiot.portal.ui.service.dto;

import java.util.List;

public class SensorStatisResponse {
    private List<Long> data;
    private List<String> keys;

    public SensorStatisResponse() {
    }

    public List<Long> getData() {
        return data;
    }

    public void setData(List<Long> data) {
        this.data = data;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    @Override
    public String toString() {
        return "SensorStatisResponse{" +
            "data=" + data +
            ", keys=" + keys +
            '}';
    }
}
