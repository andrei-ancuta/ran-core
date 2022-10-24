package ro.uti.ran.core.ws.internal.utilizator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.exception.UnsupportedOperationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.Rol;
import ro.uti.ran.core.model.portal.RolUtilizator;
import ro.uti.ran.core.model.portal.Utilizator;
import ro.uti.ran.core.service.report.ReportUtilizatoriService;
import ro.uti.ran.core.service.sistem.SistemService;
import ro.uti.ran.core.service.utilizator.RoluriSearchFilter;
import ro.uti.ran.core.service.utilizator.UtilizatorService;
import ro.uti.ran.core.service.utilizator.UtilizatoriSearchFilter;
import ro.uti.ran.core.utils.GenericListResult;
import ro.uti.ran.core.utils.ListResultHelper;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.utils.ExceptionUtil;

import javax.jws.WebService;
import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-16 11:13
 */
@WebService(
        serviceName = "AdminUtilizatorService",
        endpointInterface = "ro.uti.ran.core.ws.internal.utilizator.AdminUtilizator",
        targetNamespace = "http://admin.internal.ws.core.ran.uti.ro",
        portName = "AdminUtilizatorServicePort")
@Service("adminUtilizatorService")
public class AdminUtilizatorImpl implements AdminUtilizator {

    @Autowired
    UtilizatorService utilizatorService;

    @Autowired
    SistemService sistemService;

    @Autowired
    private ExceptionUtil exceptionUtil;

    @Autowired
    private ReportUtilizatoriService reportUtilizatoriService;

    @Override
    public Utilizator getUtilizator(Long idUtilizator) throws RanException, RanRuntimeException {
        try {
            return utilizatorService.getUtilizator(idUtilizator);
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public RezultatVerificareUtilizator verificaUtilizator(String numeUtilizator) throws RanException, RanRuntimeException {
        try {
            return utilizatorService.verificaUtilizator(numeUtilizator);
        } catch (RanBusinessException rbe) {
            throw exceptionUtil.buildException(new RanException(rbe));
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public List<RolUtilizator> getRoluriUtilizatorByIdUtilizator(Long idUtilizator) throws RanException, RanRuntimeException {
        try {
            return utilizatorService.getRoluriUtilizator(idUtilizator);
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public UtilizatoriListResult getListaUtilizatori(
            UtilizatoriSearchFilter searchFilter,
            PagingInfo pagingInfo,
            SortInfo sortInfo) throws RanException, RanRuntimeException {

        try {
            return ListResultHelper.build(
                    UtilizatoriListResult.class,
                    utilizatorService.getListaUtilizatori(searchFilter, pagingInfo, sortInfo)
            );
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public Utilizator salveazaUtilizator(Utilizator utilizator) throws RanException, RanRuntimeException {
        try {
            return utilizatorService.saveUtilizator(utilizator);
        } catch (RanBusinessException rbe) {
            throw exceptionUtil.buildException(new RanException(rbe));
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public Utilizator salveazaGospodar(Utilizator utilizator) throws RanException, RanRuntimeException {
        try {
            return utilizatorService.saveGospodar(utilizator);
        } catch (RanBusinessException rbe) {
            throw exceptionUtil.buildException(new RanException(rbe));
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public void stergeUtilizator(Long idUtilizator) throws RanException, RanRuntimeException {
        try {
            utilizatorService.deleteUtilizator(idUtilizator);
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public void schimbaStareUtilizator(Long idUtilizator) throws RanException, RanRuntimeException {
        //todo:
        throw exceptionUtil.buildException(new RanRuntimeException("Not implemented"));
    }

    @Override
    public void schimbaParolaUtilizator(
            String numeUtilizator,
            String parolaNoua
    ) throws RanException, RanRuntimeException {

        try {
            utilizatorService.schimbaParolaUtilizator(numeUtilizator, parolaNoua);
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public void reseteazaParolaUtilizator(String numeUtilizator) throws RanException, RanRuntimeException {
        try {
            utilizatorService.reseteazaParolaUtilizator(numeUtilizator);
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public Rol getRol(Long idRol) throws RanException, RanRuntimeException {
        return utilizatorService.getRol(idRol);
    }

    @Override
    public Rol getRolByCode(String codRol) throws RanException, RanRuntimeException {
        return utilizatorService.getRol(codRol);
    }

    @Override
    public RoluriListResult getListaRoluri(
            RoluriSearchFilter searchFilter,
            PagingInfo pagingInfo,
            SortInfo sortInfo) throws RanException, RanRuntimeException {

        try {
            GenericListResult<Rol> roluri = utilizatorService.getListaRoluri(
                    searchFilter,
                    pagingInfo,
                    sortInfo);

            return ListResultHelper.build(RoluriListResult.class, roluri);
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public Rol salveazaRol(Rol rol) throws RanException, RanRuntimeException {
        throw exceptionUtil.buildException(new UnsupportedOperationException("Operatia salveazaRol este nesuportata"));
    }

    @Override
    public void stergeRol(Long idRol) throws RanException, RanRuntimeException {
        throw exceptionUtil.buildException(new UnsupportedOperationException("Operatia stergeRol este nesuportata"));
    }

    @Override
    public void asigneazaRolUtilizator(
            Long idUtilizator,
            RolUtilizator rolUtilizator
    ) throws RanException, RanRuntimeException {
        try {
            utilizatorService.asigneazaRolUtilizator(idUtilizator, rolUtilizator);
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public void revocaRoluriUtilizator(
            Long idUtilizator,
            List<Long> idRoluriUtilizator
    ) throws RanException, RanRuntimeException {
        try {
            utilizatorService.revocaRoluriUtilizator(idUtilizator, idRoluriUtilizator);
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public void adaugaRolGospodar(Long idUtilizator, Long idUat, String cnp, String nif) throws RanException, RanRuntimeException {
        try {
            utilizatorService.adaugaRolGospodar(idUtilizator, idUat, cnp, nif);
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public void updateRoluriUtilizatorTipUATLocalJudetean(Long idUtilizator, boolean stergRoluriVechiTipUat, List<RolUtilizator> listaRoluri) throws RanException, RanRuntimeException {
        try {
            utilizatorService.updateRoluriUtilizatorTipUATLocalJudetean(idUtilizator, stergRoluriVechiTipUat, listaRoluri);
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }


    @Override
    public void updateRoluriUtilizatorTipInstitutieExt(Long idUtilizator, boolean stergRoluriVechiTipInstitutie, List<RolUtilizator> listaRoluri) throws RanException, RanRuntimeException {
        try {
            utilizatorService.updateRoluriUtilizatorTipInstitutieExt(idUtilizator, stergRoluriVechiTipInstitutie, listaRoluri);
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }


    @Override
    public void adaugaRoluriUtilizator(Long idUtilizator, List<RolUtilizator> listaRoluri) throws RanException, RanRuntimeException {
        try {
            utilizatorService.adaugaRoluriUtilizator(idUtilizator, listaRoluri);
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public byte[] getExportedUserList( UtilizatoriSearchFilter searchFilter,
                                  SortInfo sortInfo,String format) throws RanException, RanRuntimeException {
        try {
           return utilizatorService.getListaUtilizatoriExport(searchFilter,sortInfo,format);
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }


}
