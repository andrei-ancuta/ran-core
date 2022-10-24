package ro.uti.ran.core.ws.internal.utilizator;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.ContextSistem;
import ro.uti.ran.core.model.portal.Sistem;
import ro.uti.ran.core.repository.registru.RegistruRepository;
import ro.uti.ran.core.service.registru.NumarSecventaUatService;
import ro.uti.ran.core.service.sistem.SistemSearchFilter;
import ro.uti.ran.core.service.sistem.SistemService;
import ro.uti.ran.core.service.utilizator.UatConfigService;
import ro.uti.ran.core.utils.GenericListResult;
import ro.uti.ran.core.utils.ListResultHelper;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.internal.RanAuthorization;
import ro.uti.ran.core.ws.internal.UatRanAuthorization;
import ro.uti.ran.core.ws.utils.ExceptionUtil;

import javax.jws.WebService;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-15 17:07
 */
@WebService(
        serviceName = "UtilizatorSistemService",
        endpointInterface = "ro.uti.ran.core.ws.internal.utilizator.UtilizatorSistemService",
        targetNamespace = "http://sistem.internal.ws.core.ran.uti.ro",
        portName = "UtilizatorSistemServicePort")
@Service("sistemService")
public class UtilizatorSistemServiceImpl implements UtilizatorSistemService {

    @Autowired
    SistemService sistemService;

    @Autowired
    private ExceptionUtil exceptionUtil;

    @Autowired
    private UatConfigService uatConfigService;

    @Autowired
    private RegistruRepository registruRepository;
    
    @Autowired
    private NumarSecventaUatService numarSecventaUatService;

    @Override
    public Sistem getUtilizatorSistem(UtilizatorSistemSearchFilter searchFilter) throws RanException, RanRuntimeException {
        try {
            if (searchFilter == null) {
                throw new IllegalArgumentException("Parametru searchFilter nedefinit");
            }

            if (searchFilter.getIdEntity() == null) {
                throw new IllegalArgumentException("Parametru idEntity nespecificat");
            }

            if (searchFilter.getContext() == null) {
                throw new IllegalArgumentException("Parametru context nespecificat");
            }

            return sistemService.getUtilizatorSistem(
                    searchFilter.getIdEntity(),
                    searchFilter.getContext()
            );
        } catch (RanBusinessException rbe) {
            throw exceptionUtil.buildException(new RanException(rbe));
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public Sistem getUtilizatorSistemById(Long idUtilizatorSistem) throws RanException, RanRuntimeException {
        try {
            return sistemService.getUtilizatorSistem(idUtilizatorSistem);
        } catch (RanBusinessException e) {
            throw exceptionUtil.buildException(new RanException(e));
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public UtilizatoriSistemListResult getUtilizatoriSistem(UtilizatorSistemSearchFilter searchFilter, PagingInfo pagingInfo, SortInfo sortInfo) throws RanException, RanRuntimeException {
        try {

            SistemSearchFilter _sf = new SistemSearchFilter();
            //todo: convert from searchFilter to _sf

            GenericListResult<Sistem> listResult = sistemService.getUtilizatoriSistem(_sf, pagingInfo, sortInfo);

            return ListResultHelper.build(UtilizatoriSistemListResult.class, listResult);
        } catch (RanBusinessException e) {
            throw exceptionUtil.buildException(new RanException(e));
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public String genereazaToken() throws RanException, RanRuntimeException {
        return sistemService.genereazaToken();
    }

    @Override
    public Sistem salveazaTokenUtilizatorSistem(
            Long idEntity,
            ContextSistem context,
            String token
    ) throws RanException, RanRuntimeException {
        try {
            if (idEntity == null) {
                throw new IllegalArgumentException("Parametru idEntity nespecificat");
            }

            if (context == null) {
                throw new IllegalArgumentException("Parametru context nespecificat");
            }

            if (token == null) {
                throw new IllegalArgumentException("Parametru token nespecificat");
            }

            return sistemService.salveazaToken(idEntity, context, token);

        } catch (RanBusinessException rbe) {
            throw exceptionUtil.buildException(new RanException(rbe));
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public UatConfig getUatConfig(Long idUat) throws RanException, RanRuntimeException {
        try {
            return uatConfigService.getUatConfig(idUat);
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public void saveUatConfig(Long idUat, UatConfig uatConfig) throws RanException, RanRuntimeException {
        try {
            uatConfigService.saveUatConfig(idUat, uatConfig);
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }
    @Override
    public boolean canSwitchToManualTransmision(Long idUat) throws RanException, RanRuntimeException {
        try {
            long nrTransmisii = registruRepository.getNbTransmisionByUat(idUat);
            if (nrTransmisii == 0) {
                return true;
            } else {
                return false;
            }
        }catch (Throwable th){
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

	@Override 
	public Long getNumarCerere(UatRanAuthorization ranAuthorization) throws RanException,
			RanRuntimeException {
		try {
          return numarSecventaUatService.getNumarCerere(ranAuthorization);
        } catch (RanBusinessException rbe) {
            throw exceptionUtil.buildException(new RanException(rbe));
        }catch (Throwable th){
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
	}
	
	@Override 
	public Long vizualizeazaNumarCerere(UatRanAuthorization ranAuthorization) throws RanException,
			RanRuntimeException {
		try {
          return numarSecventaUatService.vizualizeazaNumarCerere(ranAuthorization);
        } catch (RanBusinessException rbe) {
            throw exceptionUtil.buildException(new RanException(rbe));
        }catch (Throwable th){
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
	}
 
	@Override
	public void seteazaSecventa(UatRanAuthorization ranAuthorization, BigDecimal value) 
			throws RanException, RanRuntimeException {
		try {
	          numarSecventaUatService.setSecventa(ranAuthorization, value);
        } catch (RanBusinessException rbe) {
            throw exceptionUtil.buildException(new RanException(rbe));
        }catch (Throwable th){
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
		
	}
	
	@Override
	public void reseteazaSecventa(UatRanAuthorization ranAuthorization)
			throws RanException, RanRuntimeException {
		try {
	          numarSecventaUatService.resetSecventa(ranAuthorization);
        } catch (RanBusinessException rbe) {
            throw exceptionUtil.buildException(new RanException(rbe));
        }catch (Throwable th){
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
		
	}
}
