package ro.uti.ran.core.ws.internal.utilizator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.*;
import ro.uti.ran.core.service.exportNomenclatoare.exception.NomenclatorExportCodes;
import ro.uti.ran.core.service.exportNomenclatoare.exception.NomenclatorExportException;
import ro.uti.ran.core.service.utilizator.UtilizatorService;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.utils.ExceptionUtil;

import javax.jws.WebService;

import java.util.LinkedList;
import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 15:40
 */
@WebService(
        serviceName = "InfoUtilizatorService",
        endpointInterface = "ro.uti.ran.core.ws.internal.utilizator.InfoUtilizator",
        targetNamespace = "http://info.internal.ws.core.ran.uti.ro",
        portName = "InfoUtilizatorServicePort")
@Service("infoUtilizatorService")
public class InfoUtilizatorImpl implements InfoUtilizator {

    @Autowired
    UtilizatorService utilizatorService;
    
    @Autowired
    private ExceptionUtil exceptionUtil;

//    @Override
//    public List<RolUtilizator> getRoluriUtilizator(
//            String numeUtilizator,
//            String codContext
//    ) throws RanException, RanRuntimeException {
//        return utilizatorService.getRoluriUtilizator(numeUtilizator, codContext);
//    }

    @Override
    public List<Rol> getRoluriUtilizator(String numeUtilizator, String codContext) throws RanException, RanRuntimeException {
        try {
            List<RolUtilizator> roluri = utilizatorService.getRoluriUtilizator(numeUtilizator, codContext);

            List<Rol> result = new LinkedList<Rol>();

            for (RolUtilizator rolUtilizator : roluri) {

                Rol rol = new Rol();
                rol.setCod(rolUtilizator.getRol().getCod());
                rol.setDenumire(rolUtilizator.getRol().getDenumire());
                rol.setDescriere(rolUtilizator.getRol().getDescriere());

                rol.setInstitutie(rolUtilizator.getInstitutie());
                rol.setJudet(rolUtilizator.getJudet());
                rol.setUat(rolUtilizator.getUat());

                result.add(rol);
            }

            return result;
        }catch(RanBusinessException rbe){
            throw exceptionUtil.buildException(new RanException(rbe));
        }catch(Throwable th){
        	 throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public DetaliiUtilizator getDetaliiUtilizator(String numeUtilizator) throws RanException, RanRuntimeException {

        try {
            Utilizator utilizator = utilizatorService.getUtilizator(numeUtilizator);

            if (utilizator == null) {
                return null;
            }

            return Helper.buildDetaliiUtilizator(utilizator);
        } catch(RanBusinessException rbe){
        	 throw exceptionUtil.buildException(new RanException(rbe));
        } catch (Throwable th) {
        	 throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public List<Context> getContexteUtilizator(String numeUtilizator) throws RanException, RanRuntimeException {
        try {
        	return utilizatorService.getContexteUtilizator(numeUtilizator);
        }catch(RanBusinessException rbe){
        	 throw exceptionUtil.buildException(new RanException(rbe));
        } catch (Throwable th) {
        	 throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }
}
