package ro.uti.ran.core.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.model.portal.ContextSistem;
import ro.uti.ran.core.model.portal.Institutie;
import ro.uti.ran.core.model.portal.Judet;
import ro.uti.ran.core.model.portal.UAT;
import ro.uti.ran.core.repository.portal.InstitutieRepository;
import ro.uti.ran.core.repository.portal.JudetRepository;
import ro.uti.ran.core.repository.portal.UatRepository;
import ro.uti.ran.core.ws.external.RanAuthentication;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.internal.RanAuthorization;

import java.util.List;

/**
 * Created by Stanciu Neculai on 26.Jan.2016.
 */
@Service
public class WsUtilsService {
    @Autowired
    private UatRepository uatRepository;

    @Autowired
    private JudetRepository judetRepository;

    @Autowired
    private InstitutieRepository institutieRepository;

    public boolean checkIfRanAuthorizationValid(RanAuthorization ranAuthorization) throws RanRuntimeException {
        WsUtils.checkRanAuthorization(ranAuthorization);
        List<Institutie> listaInstitutii = institutieRepository.findAll();
        for(Institutie institutie : listaInstitutii){
            if(institutie.getCod().equals(ranAuthorization.getContext())){
                return checkInstitutie(institutie,ranAuthorization);
            }
        }
        return false;
    }


    private boolean checkInstitutie(Institutie institutie, RanAuthorization ranAuthorization) {
        if(institutie.getCod().equals(ContextSistem.UAT.getCod())){
            return checkInstitutieUat(ranAuthorization);
        } else if(institutie.getCod().equals(ContextSistem.JUDET.getCod())){
            return checkInstitutieJudet(ranAuthorization);
        } else {
            //?????
           if(institutie.getId().equals(ranAuthorization.getIdEntity())){
               return true;
           } else {
               return false;
           }
        }
    }

    private boolean checkInstitutieJudet(RanAuthorization ranAuthorization) {
        Judet judet = judetRepository.findOne(ranAuthorization.getIdEntity());
        if(judet != null){
            return true;
        } else {
            return false;
        }
    }

    private boolean checkInstitutieUat(RanAuthorization ranAuthorization) {
        UAT uat = uatRepository.findOne(ranAuthorization.getIdEntity());
        if(uat != null){
            return true;
        } else {
            return false;
        }
    }


    public boolean isUat(RanAuthorization ranAuthorization) {
        return ranAuthorization.getContext().equals(ContextSistem.UAT.getCod());
    }
    public UAT getUatFrom(RanAuthorization ranAuthorization){
        if(isUat(ranAuthorization)){
            return uatRepository.findOne(ranAuthorization.getIdEntity());
        } else {
            return null;
        }
    }
}
