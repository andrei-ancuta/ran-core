package ro.uti.ran.core.ws.internal.utilizator;

import ro.uti.ran.core.model.portal.Rol;
import ro.uti.ran.core.model.portal.RolUtilizator;
import ro.uti.ran.core.model.portal.Utilizator;
import ro.uti.ran.core.service.utilizator.RoluriSearchFilter;
import ro.uti.ran.core.service.utilizator.UtilizatoriSearchFilter;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

/**
 * Serviciu administrare utilizatori, roluri, autorizari
 * <p/>
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-16 09:33
 */
@WebService
public interface AdminUtilizator {

    /**
     * Preluare utilizator dupa id;
     *
     * @param idUtilizator
     * @return null daca utilizatorul nu exista
     * @throws Exception
     */
    @WebMethod
    Utilizator getUtilizator(
            @WebParam(name = "idUtilizator") Long idUtilizator
    ) throws RanException, RanRuntimeException;


    @WebMethod
    RezultatVerificareUtilizator verificaUtilizator(
            @WebParam(name = "numeUtilizator") String numeUtilizator
    ) throws RanException, RanRuntimeException;

    /**
     * Preluare roluri utilizator
     *
     * @param idUtilizator
     * @return
     * @throws Exception
     */
    @WebMethod
    List<RolUtilizator> getRoluriUtilizatorByIdUtilizator(
            @WebParam(name = "idUtilizator") Long idUtilizator
    ) throws RanException, RanRuntimeException;

    /**
     * Preluare lista utilizatori
     *
     * @param searchFilter
     * @param pagingInfo
     * @param SortInfo
     * @return lista utilizatori
     * @throws Exception
     */
    @WebMethod
    UtilizatoriListResult getListaUtilizatori(
            @WebParam(name = "searchFilter") UtilizatoriSearchFilter searchFilter,
            @WebParam(name = "pagingInfo") PagingInfo pagingInfo,
            @WebParam(name = "sortInfo") SortInfo SortInfo
    ) throws RanException, RanRuntimeException;


    /**
     * Salvare utilizator
     *
     * @param utilizator
     * @return utilizatorul salvat
     * @throws Exception
     */
    @WebMethod
    Utilizator salveazaUtilizator(
            @WebParam(name = "utilizator") Utilizator utilizator
    ) throws RanException, RanRuntimeException;


    /**
     * Logica de creare gospodar.
     *
     * @param utilizator
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    Utilizator salveazaGospodar(
            @WebParam(name = "utilizator") Utilizator utilizator
    ) throws RanException, RanRuntimeException;


    /**
     * Stergere utilizator. Utilizatorul este marcat inactiv.
     *
     * @param idUtilizator
     * @throws Exception
     */
    @WebMethod
    void stergeUtilizator(
            @WebParam(name = "idUtilizator") Long idUtilizator
    ) throws RanException, RanRuntimeException;

    /**
     * @param idUtilizator
     * @throws Exception
     */
    @WebMethod
    void schimbaStareUtilizator(
            @WebParam(name = "idUtilizator") Long idUtilizator
    ) throws RanException, RanRuntimeException;

    /**
     * Schimbare parola utilizator
     *
     * @param numeUtilizator
     * @param parolaNoua
     * @throws Exception
     */
    @WebMethod
    void schimbaParolaUtilizator(
            @WebParam(name = "numeUtilizator") String numeUtilizator,
            @WebParam(name = "parolaNoua") String parolaNoua
    ) throws RanException, RanRuntimeException;

    /**
     * Resetare parola utilizator.
     *
     * @param numeUtilizator
     * @throws Exception
     */
    @WebMethod
    void reseteazaParolaUtilizator(
            @WebParam(name = "numeUtilizator") String numeUtilizator
    ) throws RanException, RanRuntimeException;


    /**
     * Preluare rol dupa id.
     *
     * @param idRol
     * @return
     * @throws Exception
     */
    @WebMethod
    Rol getRol(
            @WebParam(name = "idRol") Long idRol
    ) throws RanException, RanRuntimeException;


    /**
     * Preluare rol dupa cod
     *
     * @param codRol
     * @return rol identificat dupa cod sau null daca nu exista
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    Rol getRolByCode(
            @WebParam(name = "codRol") String codRol
    ) throws RanException, RanRuntimeException;

    /**
     * Preluare lista roluri.
     *
     * @param searchFilter
     * @param pagingInfo
     * @param sortInfo
     * @return
     * @throws Exception
     */
    @WebMethod
    RoluriListResult getListaRoluri(
            @WebParam(name = "searchFilter") RoluriSearchFilter searchFilter,
            @WebParam(name = "pagingInfo") PagingInfo pagingInfo,
            @WebParam(name = "sortInfo") SortInfo sortInfo
    ) throws RanException, RanRuntimeException;

    /**
     * @param rol
     * @return
     */
    @WebMethod
    Rol salveazaRol(
            @WebParam(name = "rol") Rol rol
    ) throws RanException, RanRuntimeException;

    /**
     * Stergere rol, marcare inactiv.
     *
     * @param idRol
     */
    @WebMethod
    void stergeRol(
            @WebParam(name = "idRol") Long idRol
    ) throws RanException, RanRuntimeException;


    /**
     * Asignare roluri utilizator
     *
     * @param idUtilizator
     * @param rolUtilizator
     */
    @WebMethod
    void asigneazaRolUtilizator(@WebParam(name = "idUtilizator") Long idUtilizator,
                                @WebParam(name = "rolUtilizator") RolUtilizator rolUtilizator
    ) throws RanException, RanRuntimeException;


    /**
     * Revoca roluri utilizator
     *
     * @param idUtilizator
     * @param idRoluriUtilizator
     * @throws Exception
     */
    @WebMethod
    void revocaRoluriUtilizator(@WebParam(name = "idUtilizator") Long idUtilizator,
                                @WebParam(name = "idRolUtilizator") List<Long> idRoluriUtilizator
    ) throws RanException, RanRuntimeException;

    @WebMethod
    void adaugaRolGospodar(@WebParam(name = "idUtilizator") Long idUtilizator,@WebParam(name = "idUat") Long idUat,@WebParam(name = "cnp") String cnp,@WebParam(name = "nif") String nif) throws RanException, RanRuntimeException;

    /**
     * @param idUtilizator           id utilizator la care modific rolurile
     * @param listaRoluri            lista roluri care se adauga
     * @param stergRoluriVechiTipUat true sterg toate rolurile anterioare de tip UAT
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    void updateRoluriUtilizatorTipUATLocalJudetean(@WebParam(name = "idUtilizator") Long idUtilizator, @WebParam(name = "stergRoluriVechiTipUat") boolean stergRoluriVechiTipUat, @WebParam(name = "listaRoluri") List<RolUtilizator> listaRoluri) throws RanException, RanRuntimeException;

    @WebMethod
    void updateRoluriUtilizatorTipInstitutieExt(@WebParam(name = "idUtilizator") Long idUtilizator, @WebParam(name = "stergRoluriVechiTipInstitutie") boolean stergRoluriVechiTipInstitutie, @WebParam(name = "listaRoluri") List<RolUtilizator> listaRoluri) throws RanException, RanRuntimeException;


    /**
     * @param idUtilizator id utilizator la care modific rolurile
     * @param listaRoluri  lista roluri care se adauga
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    void adaugaRoluriUtilizator(@WebParam(name = "idUtilizator") Long idUtilizator, @WebParam(name = "listaRoluri") List<RolUtilizator> listaRoluri) throws RanException, RanRuntimeException;

    @WebMethod
    byte[] getExportedUserList(UtilizatoriSearchFilter searchFilter,SortInfo sortInfo,String format) throws RanException, RanRuntimeException;

}
