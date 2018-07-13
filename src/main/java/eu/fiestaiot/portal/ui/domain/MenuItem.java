package eu.fiestaiot.portal.ui.domain;

import java.util.List;

public class MenuItem {
    private String name;
    private String url;
    private List<String> roles;
    private List<SubMenuItem> submenus;
    private Boolean show;
    private String cssClass;
    private String target;

    public String getCssClass() {
        return cssClass;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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

    public List<SubMenuItem> getSubmenus() {
        return submenus;
    }

    public void setSubmenus(List<SubMenuItem> submenus) {
        this.submenus = submenus;
    }

    public MenuItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
