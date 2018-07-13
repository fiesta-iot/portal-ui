package eu.fiestaiot.portal.ui.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.fiestaiot.portal.ui.config.FiestaProperties;
import eu.fiestaiot.portal.ui.service.dto.SensorStatisResponse;
import eu.fiestaiot.portal.ui.web.rest.vm.ReasoningStatistic;
import eu.fiestaiot.portal.ui.web.rest.vm.ReasoningStatisticResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticService {

    private final Logger log = LoggerFactory.getLogger(StatisticService.class);

    @Inject
    private FiestaProperties fiestaTestbedProperties;


    public ReasoningStatisticResponse reasoningStatistic(String token) {
        try {
            log.info("start reasoningStatistic: {}", token);

            String platformBaseURL = fiestaTestbedProperties.getEnpoints().getPlatformBaseURL();

            HttpResponse<JsonNode> jsonResponse = Unirest.get(platformBaseURL + "reasoner-engine/api/reasonings/statistic")
                .header("Content-type", "text/plain")
                .header("Accept", "application/json")
                .header("iPlanetDirectoryPro", token)
                .asJson();

            if (jsonResponse.getStatus() == 200) {

               JSONObject bodyObject = jsonResponse.getBody().getObject();
               log.info("bodyObject: {}", bodyObject);

                com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
                ReasoningStatisticResponse result = objectMapper.readValue(bodyObject.toString(), ReasoningStatisticResponse.class);
               return result;
            } else {
                return  null;
            }
        } catch (Exception ex) {
            log.info("Exception: {}", ex.toString());
                return null;
        }


    }

//    public static void main(String args[]) {
//        new StatisticService().reasoningStatistic("AQIC5wM2LY4SfcySJ58Guh807cgIXmExz0cYu8YgtWwV0r4.*AAJTSQACMDEAAlNLABM4NTYxODAwMzc2NzU3MTQxNzk1AAJTMQAA*");
//
//    }

    public SensorStatisResponse sensorStatistic(String token){
        String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>\n" +
            "PREFIX iot-lite: <http://purl.oclc.org/NET/UNIS/fiware/iot-lite#> \n" +
            "SELECT ?type (COUNT(?type) as ?count)\n" +
            "WHERE { \n" +
            "    ?sensor iot-lite:hasQuantityKind ?qk .\n" +
            "    ?sensor a ?type .\n" +
            "}\n" +
            "GROUP BY ?type";


        SensorStatisResponse response = new SensorStatisResponse();
        try {

            log.info("start getting sensors by token: {}", token);

            String platformBaseURL = fiestaTestbedProperties.getEnpoints().getPlatformBaseURL();

            log.info("platformBaseURL: {}", platformBaseURL);

            Unirest.setTimeouts(0,0);
            HttpResponse<JsonNode> jsonResponse = Unirest.post(platformBaseURL + "iot-registry/api/queries/execute/resources")
                .header("Content-type", "text/plain")
                .header("Accept", "application/json")
                .header("iPlanetDirectoryPro", token)
                .body(query)
                .asJson();

            log.info("reponse status code: {}", jsonResponse.getStatus());
            List<Long> datas = new ArrayList<>();
            List<String> keys = new ArrayList<>();

            if (jsonResponse.getStatus() == 200) {

                log.info("success call service with response status code: {} and body: {}", jsonResponse.getStatus(), jsonResponse.getBody());

                JSONObject bodyObject = null;
                if(jsonResponse.getBody().toString().contains("entity")) {
                    String body = (String) jsonResponse.getBody().getObject().get("entity");

                    bodyObject = new JSONObject(body);

                } else {
                    bodyObject = jsonResponse.getBody().getObject();
                }

                log.info("bodyObject: {}", bodyObject);
                JSONArray items = bodyObject.getJSONArray("items");
                if (items != null && items.length() > 0) {
                    int lengh = items.length();
                    for (int i = 0; i < lengh; i++) {
                        JSONObject object = items.getJSONObject(i);
                        String type = object.getString("type");
                        String data = object.getString("count");
                        String keyItem = type.replace("http://purl.org/iot/vocab/m3-lite#","");
                        keyItem = keyItem.replace("http://purl.oclc.org/NET/ssnx/ssn#","");
                        Long dataItem = Long.parseLong(data.substring(0, data.indexOf("^^")));

                        log.info("[+] keyItem: {}", keyItem);
                        log.info("[+] dataItem: {}", dataItem);
                        datas.add(dataItem);
                        keys.add(keyItem);
                    }
                }
                response.setData(datas);
                response.setKeys(keys);

            } else {
                log.info("response status code: {} and body: {}", jsonResponse.getStatus(), jsonResponse.getBody().toString());
                response = null;
            }

        } catch (UnirestException e) {
            log.error("exception error : {}", e.toString());
            e.printStackTrace();
            response = null;
        }
        log.info("end call service");
        return response;
    }

//    public static void main(String args[]) {
//        new StatisticService().sensorStatistic("AQIC5wM2LY4SfcwgKW4vKzNR8R7VQO24ABgtZN0VAFywf0s.*AAJTSQACMDEAAlNLABM3NDY4MTMwNDQ4MjEyOTc0NDg5AAJTMQAA*");
//    }
}
