package cn.org.sprouts.demo.security.spring.user.domain;

import java.util.ArrayList;
import java.util.List;

public class Privilege {
    private String url;
    private String name;
    private String description;
    private List<Role> roles = new ArrayList<>();

    public Privilege() {
    }

    public Privilege(String url, String name, String description) {
        this.url = url;
        this.name = name;
        this.description = description;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
