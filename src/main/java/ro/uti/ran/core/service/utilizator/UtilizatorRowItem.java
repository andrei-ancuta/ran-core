package ro.uti.ran.core.service.utilizator;

import java.util.List;

/**
 * Created by adrian.boldisor on 3/18/2016.
 */
public class UtilizatorRowItem {

    private Long id;
    private String email;
    private String nume;
    private String prenume;
    //TEMP
    private String perspective;
    //TEMP
    private String roluri;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getPerspective() {
        return perspective;
    }

    public void setPerspective(String perspective) {
        this.perspective = perspective;
    }

    public String getRoluri() {
        return roluri;
    }

    public void setRoluri(String roluri) {
        this.roluri = roluri;
    }


}
