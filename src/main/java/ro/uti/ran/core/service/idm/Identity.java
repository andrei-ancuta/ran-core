package ro.uti.ran.core.service.idm;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-14 09:03
 */
public class Identity {
    private String username;
    private String userpassword;
    private String firstName;
    private String lastName;
    private String mail;
    private String cnp;
    private String nif;

    private boolean active;

    public Identity() {
    }

    public Identity(String username, String userpassword, String mail) {
        this.username = username;
        this.userpassword = userpassword;
        this.mail = mail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    @Override
    public String toString() {
        return "Identity{" +
                "username='" + username + '\'' +
                ", userpassword='" + userpassword + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }
}
