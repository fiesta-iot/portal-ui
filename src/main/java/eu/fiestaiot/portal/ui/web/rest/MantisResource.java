package eu.fiestaiot.portal.ui.web.rest;

import eu.fiestaiot.portal.ui.domain.Menu;
import eu.fiestaiot.portal.ui.service.MantisService;
import eu.fiestaiot.portal.ui.web.rest.vm.SendQuestionRequest;
import eu.fiestaiot.portal.ui.web.rest.vm.SendQuestionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MantisResource {

    @Autowired
    private MantisService mantisService;

    @PostMapping("/sendQuestion")

    public ResponseEntity<?> sendQuestion(@Valid @RequestBody SendQuestionRequest requestBody){

        SendQuestionResponse response =  mantisService.postQuestion(requestBody);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


}
