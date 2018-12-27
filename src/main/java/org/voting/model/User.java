package org.voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.SafeHtml;
import org.voting.View;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "users")
public class User extends AbstractNamedEntity {

    @Email
    @NotBlank
    @Column(name = "EMAIL")
    @SafeHtml(groups = {View.Web.class})
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @Column(name = "PASSWORD")
    private String password;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotNull
    @Column(name = "REGISTERED", columnDefinition = "timestamp default now()")
    private Date registered = new Date();

    @Column(name = "ENABLED", nullable = false, columnDefinition = "bool default true")
    private boolean enabled = true;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "ROLES", joinColumns = @JoinColumn(name = "USER_ID"))
    @Column(name = "ROLE")
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @OrderBy("date DESC")
    private List<Vote> votes;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public User(){}

    public User(Integer id, String name, String email, String password, Date registered, Role role) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.registered = registered;
        this.roles = Collections.singleton(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", registered=" + registered +
                ", enabled=" + enabled +
                '}';
    }
}
