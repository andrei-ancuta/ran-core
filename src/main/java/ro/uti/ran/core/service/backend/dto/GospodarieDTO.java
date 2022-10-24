package ro.uti.ran.core.service.backend.dto;

import ro.uti.ran.core.model.registru.*;

import java.io.Serializable;
import java.util.List;

/**
 * Trimit listele filtrate dupa an,etc "detasat" de entitate.
 * daca le trimit "atasat" prin entitate sa nu le persite JPA-ul si imi fac de lucru.
 * Created by Dan on 19-Oct-15.
 */
public class GospodarieDTO implements Serializable {

    private Gospodarie gospodarie;

    private List<SuprafataCategorie> suprafataCategories;
    private List<AdresaGospodarie> adresaGospodaries;
    private List<DetinatorPf> detinatorPfs;
    private List<DetinatorPj> detinatorPjs;
    private List<SuprafataUtilizare> suprafataUtilizares;
    private List<Cultura> culturas;
    private List<MembruPf> membruPfs;
    private List<PomRazlet> pomRazlets;
    private List<Plantatie> plantaties;
    private List<TerenIrigat> terenIrigats;
    private List<CategorieAnimal> categorieAnimals;
    private List<SistemTehnic> sistemTehnics;
    private List<AplicareIngrasamant> aplicareIngrasamants;
    private List<SubstantaChimica> substantaChimicas;
    private List<ParcelaTeren> parcelaTerens;
    private List<Cladire> cladires;
    private List<Atestat> atestats;
    private List<MentiuneCerereSuc> mentiuneCerereSucs;
    private List<Preemptiune> preemptiunes;
    private List<MentiuneSpeciala> mentiuneSpecialas;
    private List<Contract> contracts;

    public Gospodarie getGospodarie() {
        return gospodarie;
    }

    public void setGospodarie(Gospodarie gospodarie) {
        this.gospodarie = gospodarie;
    }

    public List<SuprafataCategorie> getSuprafataCategories() {
        return suprafataCategories;
    }

    public void setSuprafataCategories(List<SuprafataCategorie> suprafataCategories) {
        this.suprafataCategories = suprafataCategories;
    }

    public List<AdresaGospodarie> getAdresaGospodaries() {
        return adresaGospodaries;
    }

    public void setAdresaGospodaries(List<AdresaGospodarie> adresaGospodaries) {
        this.adresaGospodaries = adresaGospodaries;
    }

    public List<DetinatorPf> getDetinatorPfs() {
        return detinatorPfs;
    }

    public void setDetinatorPfs(List<DetinatorPf> detinatorPfs) {
        this.detinatorPfs = detinatorPfs;
    }

    public List<DetinatorPj> getDetinatorPjs() {
        return detinatorPjs;
    }

    public void setDetinatorPjs(List<DetinatorPj> detinatorPjs) {
        this.detinatorPjs = detinatorPjs;
    }

    public List<MembruPf> getMembruPfs() {
        return membruPfs;
    }

    public void setMembruPfs(List<MembruPf> membruPfs) {
        this.membruPfs = membruPfs;
    }

    public List<SuprafataUtilizare> getSuprafataUtilizares() {
        return suprafataUtilizares;
    }

    public void setSuprafataUtilizares(List<SuprafataUtilizare> suprafataUtilizares) {
        this.suprafataUtilizares = suprafataUtilizares;
    }

    public List<Cultura> getCulturas() {
        return culturas;
    }

    public void setCulturas(List<Cultura> culturas) {
        this.culturas = culturas;
    }

    public List<PomRazlet> getPomRazlets() {
        return pomRazlets;
    }

    public void setPomRazlets(List<PomRazlet> pomRazlets) {
        this.pomRazlets = pomRazlets;
    }


    public List<TerenIrigat> getTerenIrigats() {
        return terenIrigats;
    }

    public void setTerenIrigats(List<TerenIrigat> terenIrigats) {
        this.terenIrigats = terenIrigats;
    }

    public List<CategorieAnimal> getCategorieAnimals() {
        return categorieAnimals;
    }

    public void setCategorieAnimals(List<CategorieAnimal> categorieAnimals) {
        this.categorieAnimals = categorieAnimals;
    }

    public List<SistemTehnic> getSistemTehnics() {
        return sistemTehnics;
    }

    public void setSistemTehnics(List<SistemTehnic> sistemTehnics) {
        this.sistemTehnics = sistemTehnics;
    }

    public List<SubstantaChimica> getSubstantaChimicas() {
        return substantaChimicas;
    }

    public void setSubstantaChimicas(List<SubstantaChimica> substantaChimicas) {
        this.substantaChimicas = substantaChimicas;
    }

    public List<ParcelaTeren> getParcelaTerens() {
        return parcelaTerens;
    }

    public void setParcelaTerens(List<ParcelaTeren> parcelaTerens) {
        this.parcelaTerens = parcelaTerens;
    }

    public List<Cladire> getCladires() {
        return cladires;
    }

    public void setCladires(List<Cladire> cladires) {
        this.cladires = cladires;
    }

    public List<Atestat> getAtestats() {
        return atestats;
    }

    public void setAtestats(List<Atestat> atestats) {
        this.atestats = atestats;
    }

    public List<MentiuneCerereSuc> getMentiuneCerereSucs() {
        return mentiuneCerereSucs;
    }

    public void setMentiuneCerereSucs(List<MentiuneCerereSuc> mentiuneCerereSucs) {
        this.mentiuneCerereSucs = mentiuneCerereSucs;
    }

    public List<Preemptiune> getPreemptiunes() {
        return preemptiunes;
    }

    public void setPreemptiunes(List<Preemptiune> preemptiunes) {
        this.preemptiunes = preemptiunes;
    }

    public List<MentiuneSpeciala> getMentiuneSpecialas() {
        return mentiuneSpecialas;
    }

    public void setMentiuneSpecialas(List<MentiuneSpeciala> mentiuneSpecialas) {
        this.mentiuneSpecialas = mentiuneSpecialas;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    public List<Plantatie> getPlantaties() {
        return plantaties;
    }

    public void setPlantaties(List<Plantatie> plantaties) {
        this.plantaties = plantaties;
    }

    public List<AplicareIngrasamant> getAplicareIngrasamants() {
        return aplicareIngrasamants;
    }

    public void setAplicareIngrasamants(List<AplicareIngrasamant> aplicareIngrasamants) {
        this.aplicareIngrasamants = aplicareIngrasamants;
    }
}
