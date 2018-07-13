package eu.fiestaiot.portal.ui.web.rest;

import eu.fiestaiot.portal.ui.service.MantisService;
import eu.fiestaiot.portal.ui.service.OpenAMSecurityHelper;
import eu.fiestaiot.portal.ui.service.StatisticService;
import eu.fiestaiot.portal.ui.service.dto.SensorStatisResponse;
import eu.fiestaiot.portal.ui.web.HomeController;
import eu.fiestaiot.portal.ui.web.rest.vm.ReasoningStatisticResponse;
import eu.fiestaiot.portal.ui.web.rest.vm.SendQuestionRequest;
import eu.fiestaiot.portal.ui.web.rest.vm.SendQuestionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class StatisResource {

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private OpenAMSecurityHelper openAMSecurityHelper;


    private final Logger log = LoggerFactory.getLogger(StatisResource.class);

    @RequestMapping("/statistic/reasoning")

    public ResponseEntity<?> sensorStatistic(HttpServletRequest request, Model model){

        String token = openAMSecurityHelper.getToken(request);
        ReasoningStatisticResponse reasoningStatistic = statisticService.reasoningStatistic(token);
        log.info("reasoningStatistic: {}",reasoningStatistic);
        return new ResponseEntity<ReasoningStatisticResponse>(reasoningStatistic, HttpStatus.OK);
    }

    @RequestMapping("/statistic/sensor")

    public ResponseEntity<?>  sensor(HttpServletRequest request, Model model){
        String token = openAMSecurityHelper.getToken(request);
        SensorStatisResponse response =  statisticService.sensorStatistic(token);
        return new ResponseEntity<SensorStatisResponse>(response, HttpStatus.OK);
    }



}
