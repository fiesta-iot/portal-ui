package eu.fiestaiot.portal.ui.config;

;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.fiestaiot.portal.ui.domain.Menu;
import eu.fiestaiot.portal.ui.domain.MenuItem;
import eu.fiestaiot.portal.ui.domain.SubMenuItem;
import eu.fiestaiot.portal.ui.web.HomeController;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


@Service
public class MenuConfiguration {

    @Inject
    private Environment env;
    @Inject
    private ResourceLoader resourceLoader;

    private final Logger log = LoggerFactory.getLogger(MenuConfiguration.class);

    public String getMneuEnviroment() {
        if (env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)) {
            return "prod";
        }
        if (env.acceptsProfiles(Constants.SPRING_PROFILE_DEVELOPMENT)) {
            return "dev";
        }
        if(env.acceptsProfiles(Constants.SPRING_PROFILE_TEST)) {
            return "test";
        }
        return "";

    }
    public String getMenuConfigFile() {
        if (env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)) {
            return "classpath:config/menu-prod.json";
        }
        if (env.acceptsProfiles(Constants.SPRING_PROFILE_DEVELOPMENT)) {
            return "classpath:config/menu-dev.json";
        }
        if(env.acceptsProfiles(Constants.SPRING_PROFILE_TEST)) {
            return "classpath:config/menu-test.json";
        }
        return "classpath:config/menu-dev.json";
    }
    public List<Menu> getCurrentMenu(List<String> userRoles) {

        try {

            String fileName  = getMenuConfigFile();
            Resource resource =
                resourceLoader.getResource(fileName);

            InputStream targetStream = resource.getInputStream();
            String result = IOUtils.toString(targetStream, StandardCharsets.UTF_8);

            ObjectMapper objectMapper = new ObjectMapper();
            //List<Menu>  menuList= objectMapper.readValue(result, new TypeReference(){}); 
            List<Menu> menuList = objectMapper.readValue(result, new TypeReference<List<Menu>>() {
            });
            for (String role : userRoles) {

                for (Menu item : menuList) {
                    if (item.getRoles().contains(role)) {
                        item.setShow(true);
                    }
                    List<MenuItem> subItems = item.getSubmenus();
                    if (subItems != null && subItems.size() > 0) {
                        for (MenuItem subItem : subItems) {
                            if (subItem.getRoles().contains(role)) {
                                subItem.setShow(true);
                            }
                            List<SubMenuItem> subofSubItems = subItem.getSubmenus();
                            if (subofSubItems != null && subofSubItems.size() > 0) {
                                for (SubMenuItem subofSubItem : subofSubItems) {
                                    if (subofSubItem.getRoles().contains(role)) {
                                        subofSubItem.setShow(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            log.info("[+] list menu: {}", menuList);
            return menuList;
        } catch ( IOException ex) {
            log.info("[-] Error readfile: {}", ex.toString());
            return null;
        }



    }
}
