package com.alpha.marketplace.models;

import com.alpha.marketplace.models.dtos.ExtensionDTO;
import com.fasterxml.jackson.annotation.*;
import com.google.cloud.storage.BlobId;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users" )
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Column(name = "expired")
    private boolean isAccountNonExpired;

    @Column(name = "locked")
    private boolean isAccountNonLocked;

    @Column(name = "credentials_expired")
    private boolean isCredentialsNonExpired;

    @Column(name = "enabled")
    private boolean isEnabled;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false, length = 1000)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    private String email;

    @Column(name = "pic_blob_id")
    private BlobId picBlobId;

    @Column(name = "pic", nullable = false)
    private String picURI;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ManyToMany( fetch = FetchType.EAGER,
            cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
    })
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> authorities;

    //asdasd
    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "publisher")
    private List<Extension> extensions;

    public User(){
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
        this.isEnabled = true;
        this.picURI = "https://vignette.wikia.nocookie.net/teamfourstar/images/7/7c/UnknownPerson.jpg/revision/latest?cb=20160521184455";
        extensions = new ArrayList<>();
        authorities = new HashSet<>();
    }

    public User(
            boolean isAccountNonExpired,
            boolean isAccountNonLocked,
            boolean isCredentialsNonExpired,
            boolean isEnabled, String username,
            String password,
            String email,
            BlobId picBlobId,
            String picURI,
            String firstName,
            String lastName,
            Set<Role> authorities,
            List<Extension> extensions
    ) {
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
        this.username = username;
        this.password = password;
        this.email = email;
        this.picBlobId = picBlobId;
        this.picURI = picURI;
        this.firstName = firstName;
        this.lastName = lastName;
        this.authorities = authorities;
        this.extensions = extensions;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BlobId getPicBlobId() {
        return picBlobId;
    }

    public void setPicBlobId(BlobId picBlobId) {
        this.picBlobId = picBlobId;
    }

    public String getPicURI() {
        return picURI;
    }

    public void setPicURI(String picURI) {
        this.picURI = picURI;
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

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    @JsonIgnore
    public List<Extension> getExtensions() {
        return extensions;
    }

    @JsonIdentityReference
    public void setExtensions(List<Extension> extensions) {
        this.extensions = extensions;
    }

    @JsonProperty("extensions")
    public Set<ExtensionDTO> getExtensionsDTO(){
            return getExtensions().stream()
                    .map(e -> new ExtensionDTO(
                            e.getId(), e.getName(),
                            e.getDescription(),
                            e.getDownloads(),
                            e.getDlURI(),
                            e.getTags(),
                            e.getAddedOn(),
                            e.getGitHubInfo()))
                    .collect(Collectors.toSet());
    }

    @Override
    public Set<Role> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public void ban(){
        isEnabled = false;
        extensions.forEach(Extension::forbid);
    }

    public void unban(){
        //TODO might have to rethink this in case there were pending extensions before the ban
        isEnabled = true;
        extensions.forEach(Extension::approve);
    }

    @Transient
    public boolean isAdmin(){
        return this.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    @Transient
    public boolean isOwner(){
        return this.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_OWNER"));
    }

    @Transient
    public boolean isPublisher(Extension extension){
        return (extension.getPublisher().getId() == this.getId());
    }

    @Transient
    public boolean isBanned(){
        return !isEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId() == user.getId();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }
}
