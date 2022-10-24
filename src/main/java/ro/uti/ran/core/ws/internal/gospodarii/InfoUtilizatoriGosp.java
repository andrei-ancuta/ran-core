package ro.uti.ran.core.ws.internal.gospodarii;

import ro.uti.ran.core.model.registru.DetinatorPj;

/**
 * Created by adrian.boldisor on 2/8/2016.
 */
public class InfoUtilizatoriGosp {

    private Long idGospodarie;
    private String indentificator;
    private String uat;
    private String cui;
    private String denumireFirma;
    private Long codJudet;
    private long codSiruta;
    //nomUat

    InfoUtilizatoriGosp(){

    }




    public InfoUtilizatoriGosp(DetinatorPj source){
        this.idGospodarie = source.getGospodarie().getIdGospodarie();
        this.indentificator = source.getGospodarie().getIdentificator();
        this.uat = source.getGospodarie().getNomUat().getDenumire();
        this.cui = source.getPersoanaRc().getCui();
        this.denumireFirma = source.getPersoanaRc().getDenumire();
        this.codJudet = source.getGospodarie().getNomJudet().getId();
        this.codSiruta = source.getGospodarie().getNomUat().getCodSiruta();

    }


    public Long getIdGospodarie() {
        return idGospodarie;
    }

    public void setIdGospodarie(Long idGospodarie) {
        this.idGospodarie = idGospodarie;
    }

    public String getIndentificator() {
        return indentificator;
    }

    public void setIndentificator(String indentificator) {
        this.indentificator = indentificator;
    }


    public String getCui() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }

    public String getUat() {
        return uat;
    }

    public void setUat(String uat) {
        this.uat = uat;
    }


    public Long getCodJudet() {
        return codJudet;
    }

    public void setCodJudet(Long codJudet) {
        this.codJudet = codJudet;
    }

    public String getDenumireFirma() {
        return denumireFirma;
    }

    public void setDenumireFirma(String denumireFirma) {
        this.denumireFirma = denumireFirma;
    }


    public long getCodSiruta() {
        return codSiruta;
    }

    public void setCodSiruta(long codSiruta) {
        this.codSiruta = codSiruta;
    }

    @Override
    public String toString() {
        return "InfoUtilizatoriGosp{" +
                "idGospodarie=" + idGospodarie +
                ", indentificator='" + indentificator + '\'' +
                ", uat='" + uat + '\'' +
                ", cui='" + cui + '\'' +
                ", denumireFirma='" + denumireFirma + '\'' +
                ", codJudet=" + codJudet +
                '}';
    }
}
