package eu.fiestaiot.portal.ui.web.rest;

import eu.fiestaiot.portal.ui.config.DefaultProfileUtil;
import eu.fiestaiot.portal.ui.config.FiestaProperties;
import eu.fiestaiot.portal.ui.config.MenuConfiguration;
import eu.fiestaiot.portal.ui.domain.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Resource to return information about the currently running Spring profiles.
 */
@RestController
@RequestMapping("/api")
public class ProfileInfoResource {

    @Inject
    private Environment env;

    @Inject
    private FiestaProperties fiestaProperties;

    @Autowired
    private MenuConfiguration menuConfiguration;

    @GetMapping("/menus/fiestaAdmin")
    public List<Menu> fiestaAdmin() {

            List<String> roles = new ArrayList<>();
            roles.add("fiestaAdmin");
            return menuConfiguration.getCurrentMenu(roles);

    }
    @GetMapping("/menus/experimenterAdmin")
    public List<Menu> experimenterAdmin() {

            List<String> roles = new ArrayList<>();
            roles.add("experimenterAdmin");
            return menuConfiguration.getCurrentMenu(roles);

    }
    @GetMapping("/menus/testbedAdmin")
    public List<Menu> testbedAdmin() {

            List<String> roles = new ArrayList<>();
            roles.add("testbedAdmin");
            return menuConfiguration.getCurrentMenu(roles);

    }

    @GetMapping("/menus/authenticated")
    public List<Menu> authenticated() {

            List<String> roles = new ArrayList<>();
            roles.add("authenticated");
            return menuConfiguration.getCurrentMenu(roles);

    }

    @GetMapping("/menus/getAll")
    public List<Menu> getAll() {

            List<String> roles = new ArrayList<>();
            roles.add("authenticated");
            roles.add("testbedAdmin");
            roles.add("experimenterAdmin");
            roles.add("fiestaAdmin");
            return menuConfiguration.getCurrentMenu(roles);

    }



    @GetMapping("/profile-info")
    public ProfileInfoResponse getActiveProfiles() {
        String[] activeProfiles = DefaultProfileUtil.getActiveProfiles(env);
        return new ProfileInfoResponse(activeProfiles, getRibbonEnv(activeProfiles));
    }

    private String getRibbonEnv(String[] activeProfiles) {
        String[] displayOnActiveProfiles = fiestaProperties.getRibbon().getDisplayOnActiveProfiles();

        if (displayOnActiveProfiles == null) {
            return null;
        }

        List<String> ribbonProfiles = new ArrayList<>(Arrays.asList(displayOnActiveProfiles));
        List<String> springBootProfiles = Arrays.asList(activeProfiles);
        ribbonProfiles.retainAll(springBootProfiles);

        if (ribbonProfiles.size() > 0) {
            return ribbonProfiles.get(0);
        }
        return null;
    }

    class ProfileInfoResponse {

        public String[] activeProfiles;
        public String ribbonEnv;

        ProfileInfoResponse(String[] activeProfiles, String ribbonEnv) {
            this.activeProfiles = activeProfiles;
            this.ribbonEnv = ribbonEnv;
        }
    }
}
