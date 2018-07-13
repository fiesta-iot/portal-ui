package eu.fiestaiot.portal.ui.domain;

import java.util.List;


public class Menu {

    private String name;
    private List<String> roles;
    private String url;
    private List<MenuItem> submenus;
    private Boolean show = false;
    private String target;
    private String cssClass;
    public Menu() {
    }


    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MenuItem> getSubmenus() {
        return submenus;
    }

    public void setSubmenus(List<MenuItem> submenus) {
        this.submenus = submenus;
    }


}


