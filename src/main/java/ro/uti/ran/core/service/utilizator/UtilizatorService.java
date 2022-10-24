package ro.uti.ran.core.service.utilizator;

import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.*;
import ro.uti.ran.core.utils.GenericListResult;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.internal.utilizator.RezultatVerificareUtilizator;

import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 16:28
 */
public interface UtilizatorService {
    // ------------------------------
    // USERS
    // ------------------------------

    RezultatVerificareUtilizator verificaUtilizator(String numeUtilizator) throws RanBusinessException;

    Utilizator getUtilizator(Long idUtilizator);

    Utilizator getUtilizator(String numeUtilizator) throws RanBusinessException;

    Utilizator getUtilizatorFromIdm(String numeUtilizator) throws RanBusinessException;

    List<RolUtilizator> getRoluriUtilizator(Long idUtilizator);

    List<RolUtilizator> getRoluriUtilizator(String numeUtilizator, String codContext) throws RanBusinessException;

    List<Context> getContexteUtilizator(String numeUtilizator) throws RanBusinessException;

    Utilizator saveUtilizator(Utilizator utilizator) throws RanBusinessException;

    Utilizator saveGospodar(Utilizator gospodar) throws RanBusinessException;

    void deleteUtilizator(Long idUtilizator);

    GenericListResult<Utilizator> getListaUtilizatori(UtilizatoriSearchFilter rolSearchFilter, PagingInfo pagingInfo, SortInfo sortInfo);

    byte[] getListaUtilizatoriExport(UtilizatoriSearchFilter utilizatoriSearchFilter, SortInfo sortInfo,String format);

    void schimbaParolaUtilizator(String username, String newPassword) throws RanBusinessException;


    void reseteazaParolaUtilizator(String username) throws RanBusinessException;

    List<Utilizator> getUtilizatoriActiviByRolAndUatCodSiruta(String codRol, Boolean rolActiv, Integer codSiruta, Boolean utilizatorActiv);

    List<RolUtilizator> getUtilizatorByCNPAndRol(String codRol, String CNP);

    List<RolUtilizator> getUtilizatorByNIFAndRol(String codRol, String NIF);


    // ------------------------------
    // ROLES
    // ------------------------------

    Rol getRol(Long idRol);

    Rol getRol(String codRol);

    Rol saveRol(Rol rol);

    void deleteRol(Long idRol);

    GenericListResult<Rol> getListaRoluri(RoluriSearchFilter roluriSearchFilter, PagingInfo pagingInfo, SortInfo sortInfo);

    void asigneazaRolUtilizator(Long idUtilizator, RolUtilizator rolUtilizator);

    void revocaRoluriUtilizator(Long idUtilizator, List<Long> idRoluriUtilizator);

    void updateRoluriUtilizatorTipUATLocalJudetean(Long idUtilizator,boolean stergRoluriVechiTipUat,List<RolUtilizator> listaRoluri) throws RanBusinessException;

    void updateRoluriUtilizatorTipInstitutieExt(Long idUtilizator, boolean stergRoluriVechiTipInstitutie, List<RolUtilizator> listaRoluri) throws RanBusinessException;

    void adaugaRoluriUtilizator(Long idUtilizator, List<RolUtilizator> listaRoluri) throws RanBusinessException;

    void adaugaRolGospodar(Long idUtilizator, Long idUat, String cnp, String nif) throws RanBusinessException;
    // ------------------------------
    // CONFIG
    // ------------------------------

}
