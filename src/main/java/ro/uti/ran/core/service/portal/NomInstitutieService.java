package ro.uti.ran.core.service.portal;

import ro.uti.ran.core.model.portal.Institutie;

import java.util.List;

/**
 * Created by Dan on 14-Mar-16.
 */
public interface NomInstitutieService {

    Institutie getInstitutieByCod(String codInstitutie);

    List<Institutie> getInstitutieByTipInstitutie(Long fkNomTipInstitutie);
}
