package ro.uti.ran.core.service.backend;

import ro.uti.ran.core.exception.DateRegistruValidationException;

/**
 * Created by Dan on 17-Nov-15.
 */
public interface GeometrieService {

    /**
     * salveaza in baza de date geometria adresei
     *
     * @param idAdresa     id adresa din baza de date
     * @param geometrieGML geometria in format GML
     * @param fkNomJudet   id judet
     */
    void insertAdresaGIS(Long idAdresa, String geometrieGML, Long fkNomJudet);

    /**
     * @param idGeolocatorAdresa id GIS din baza de date
     * @return geometria in format GML a adresei
     */
    String getAdresaGIS(Long idGeolocatorAdresa);

    /**
     * salveaza in baza de date geometria parcelei de teren
     *
     * @param idParcelaTeren id parcela teren din baza de date
     * @param geometrieGML   geometria in format GML
     * @param fkNomJudet     id judet
     */
    void insertParcelaTerenGIS(Long idParcelaTeren, String geometrieGML, Long fkNomJudet,Integer isFolosinta);

    /**
     * @param idGeometrieParcelaTeren id GIS din baza de date
     * @return geometria in format GML a parcelei de teren
     */
    String getParcelaTerenGIS(Long idGeometrieParcelaTeren);


    /**
     * salveaza in baza de date geometria cladirii
     *
     * @param idCladire    id cladire din baza de date
     * @param geometrieGML geometria in format GML
     * @param fkNomJudet   id judet
     */
    void insertCladireGIS(Long idCladire, String geometrieGML, Long fkNomJudet);

    /**
     * @param idGeometrieCladire id GIS din baza de date
     * @return geometria in format GML a cladirii
     */
    String getCladireGIS(Long idGeometrieCladire);

    /**
     * salveaza in baza de date geometria culturii
     *
     * @param idCultura    id cultura din baza de date
     * @param geometrieGML geometria in format GML
     * @param fkNomJudet   id judet
     */
    void insertCulturaGIS(Long idCultura, String geometrieGML, Long fkNomJudet, Integer isPrincipala);

    /**
     * @param idGeometrieCultura id GIS din baza de date
     * @return geometria in format GML a culturii
     */
    String getCulturaGIS(Long idGeometrieCultura);


    /**
     * salveaza in baza de date geometria plantatiei
     *
     * @param idPlantatie  id cultura din baza de date
     * @param geometrieGML geometria in format GML
     * @param fkNomJudet   id judet
     */
    void insertPlantatieGIS(Long idPlantatie, String geometrieGML, Long fkNomJudet);

    /**
     * @param idGeometriePlantatie id GIS din baza de date
     * @return geometria in format GML a plantatiei
     */
    String getPlantatieGIS(Long idGeometriePlantatie);


    /**
     * salveaza in baza de date geometria aplicare ingrasamant
     *
     * @param idAplicareIngrasamant id cultura din baza de date
     * @param geometrieGML          geometria in format GML
     * @param fkNomJudet            id judet
     */
    void insertAplicareIngrasamantGIS(Long idAplicareIngrasamant, String geometrieGML, Long fkNomJudet);

    /**
     * @param idGeometrieAplicareIngras id GIS din baza de date
     * @return geometria in format GML a aplicare ingrasamant
     */
    String getAplicareIngrasamantGIS(Long idGeometrieAplicareIngras);


    /**
     * salveaza in baza de date geometria teren irigat
     *
     * @param idTerenIrigat id cultura din baza de date
     * @param geometrieGML  geometria in format GML
     * @param fkNomJudet    id judet
     */
    void insertTerenIrigatGIS(Long idTerenIrigat, String geometrieGML, Long fkNomJudet);

    /**
     * @param idGeometrieTerenIrigat id GIS din baza de date
     * @return geometria in format GML a teren irigat
     */
    String getTerenIrigatGIS(Long idGeometrieTerenIrigat);


    /**
     * salveaza in baza de date geometria suprafata utilizare
     *
     * @param idSuprafataUtilizare id suprafata utilizare din baza de date
     * @param geometrieGML         geometria in format GML
     * @param fkNomJudet           id judet
     */
    void insertSuprafataUtilizGIS(Long idSuprafataUtilizare, String geometrieGML, Long fkNomJudet);

    /**
     * @param idGeometrieSuprafataUtiliz id GIS din baza de date
     * @return geometria in format GML a suprafata utilizare
     */
    String getSuprafataUtilizGIS(Long idGeometrieSuprafataUtiliz);


    void validateGeometrie(String gml311String) throws DateRegistruValidationException;

    void validateGeometriePunctUatLimit(int sirutaUat, String gml311String) throws DateRegistruValidationException;

    void validateGeometriePoligonUatLimit(int sirutaUat, String gml311String) throws DateRegistruValidationException;

}
