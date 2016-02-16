package org.kantega.restmakeover.rest.users;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 */
@XmlRootElement
public class User {
    @XmlElement
    private final String username;
    @XmlElement
    private final String fullName;

    public User() {
        username = null;
        fullName = null;
    }

    public User(String username, String fullName) {
        this.username = username;

        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }
}
