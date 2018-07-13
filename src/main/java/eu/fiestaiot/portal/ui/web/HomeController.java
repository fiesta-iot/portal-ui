package eu.fiestaiot.portal.ui.web;

import eu.fiestaiot.portal.ui.config.FiestaProperties;
import eu.fiestaiot.portal.ui.config.MenuConfiguration;
import eu.fiestaiot.portal.ui.domain.Menu;
import eu.fiestaiot.portal.ui.service.OpenAMSecurityHelper;
import eu.fiestaiot.portal.ui.service.StatisticService;
import eu.fiestaiot.portal.ui.service.dto.FiestaUser;
import eu.fiestaiot.portal.ui.service.dto.SensorStatisResponse;
import eu.fiestaiot.portal.ui.web.rest.vm.ReasoningStatisticResponse;
import org.hibernate.envers.Audited;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {


    private final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private MenuConfiguration menuConfiguration;
    @Autowired
    private OpenAMSecurityHelper openAMSecurityHelper;

    @Autowired
    private StatisticService statisticService;

    @Inject
    private FiestaProperties fiestaProperties;

    @RequestMapping("/")
    public String index (Model model, HttpServletRequest request) {
        String token = openAMSecurityHelper.getToken(request);
        String userID = openAMSecurityHelper.getUserID(token);
        log.info("[-] current user token: {}, userID: {}", token, userID);

        if(token == null || token.isEmpty() || userID == null || userID.isEmpty()) {
            // redirect to portal logiin
            return "redirect:" + fiestaProperties.getEnpoints().getRedirectLoginToPortal();
        }
        FiestaUser user = openAMSecurityHelper.getUserRole(userID, token);
        if(user.getRoles() == null || user.getRoles().size() == 0){
            // no role
            return "redirect:" + fiestaProperties.getEnpoints().getRedirectLoginToPortal();
        }
        log.info("[-] current user:{}", user);

        String profileUrl = fiestaProperties.getEnpoints().getProfileUrl();
        List<Menu> listMenu = menuConfiguration.getCurrentMenu(user.getRoles());
        model.addAttribute("listMenu", listMenu);
        model.addAttribute("fiestaUser", user);
        model.addAttribute("profileUrl", profileUrl);


        return "index";
    }

    @RequestMapping("/statistics")
    public String greeting(HttpServletRequest request, Model model) {
        return "statistics";
    }

    @RequestMapping("/contact")
    public String contact(HttpServletRequest request, Model model) {
        return "contact";
    }
    @RequestMapping("/guide")
    public String guide(HttpServletRequest request, Model model) {
        return "guide";
    }
    @RequestMapping("/welcome")
    public String welcome(HttpServletRequest request, Model model) {
        return "welcome";
    }

    @RequestMapping("/ontologyValidatorDocs")
    public String ontologyValidatorDocs(HttpServletRequest request, Model model) {
        return "ontologyValidatorDocs";
    }
    @RequestMapping("/fatDocs")
    public String fatDocs(HttpServletRequest request, Model model) {
        return "fatDocs";
    }
    @RequestMapping("/resultStoreDocs")
    public String resultStoreDocs(HttpServletRequest request, Model model) {
        return "resultStoreDocs";
    }
    @RequestMapping("/commingSoon")
    public String commingSoon(HttpServletRequest request, Model model) {
        return "commingSoon";
    }


    @RequestMapping("/logout")
    public ModelAndView logout(Model model, HttpServletRequest request) {
        String token = openAMSecurityHelper.getToken(request);
        log.info("[-] current user token: {}", token);
        boolean result = openAMSecurityHelper.logout(token);
        if(result) {
            return new ModelAndView("redirect:" + fiestaProperties.getEnpoints().getRedirectLoginToPortal());
        } else {
            return new ModelAndView("redirect:/" );
        }

    }
}
