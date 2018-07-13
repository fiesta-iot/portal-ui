package eu.fiestaiot.portal.ui.service.dto;

import java.util.List;

public class FiestaUser {
    private String firstName;
    private String lastName;
    private String name;
    private List<String> roles;

    public FiestaUser() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "FiestaUser{" +
            "firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", name='" + name + '\'' +
            ", roles=" + roles +
            '}';
    }
}
