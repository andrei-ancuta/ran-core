package ro.uti.ran.core.service.sistem;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.audit.Audit;
import ro.uti.ran.core.audit.AuditOpType;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.*;
import ro.uti.ran.core.model.utils.InstitutieEnum;
import ro.uti.ran.core.repository.criteria.AndRepositoryCriteria;
import ro.uti.ran.core.repository.portal.InstitutieRepository;
import ro.uti.ran.core.repository.portal.JudetRepository;
import ro.uti.ran.core.repository.portal.SistemRepository;
import ro.uti.ran.core.repository.portal.UatRepository;
import ro.uti.ran.core.utils.GenericListResult;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;

import java.util.Date;

import static ro.uti.ran.core.audit.AuditOpType.SALVARE_TOKEN_APLICATIE;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-15 16:33
 */
@Component
@Transactional
public class SistemServiceImpl implements SistemService {

    @Autowired
    SistemRepository sistemRepository;

    @Autowired
    TokenGenerator tokenGenerator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UatRepository uatRepository;

    @Autowired
    JudetRepository judetRepository;

    @Autowired
    InstitutieRepository institutieRepository;


    @Audit(opType = SALVARE_TOKEN_APLICATIE)
    @Override
    public Sistem salveazaToken(Long idEntity, ContextSistem context, String token) throws RanBusinessException{

        Sistem sistem;

        //
        // todo: Ce nume utilizator dam ? Ce denumire utilizator dam ?
        //
        switch (context) {
            case UAT: {
                UAT uat = uatRepository.findOne(idEntity);
                if (uat == null) {
                    throw new IllegalArgumentException("Nu exista uat avand id " + idEntity);
                }

                //
                // Verificare daca exista deja
                //
                sistem = sistemRepository.findByUat_Id(uat.getId());
                if (sistem == null) {
                    sistem = new Sistem();
                    sistem.setUat(uat);
                    //sistem.setNumeUtilizator("UAT-" + uat.getDenumire());
                    sistem.setDenumire("UAT");

                    Institutie institutie = institutieRepository.findOne(InstitutieEnum.UAT.getId());
                    if (institutie == null) {
                        throw new IllegalArgumentException("Nu exista institutie cu id " + InstitutieEnum.UAT.getId());
                    }

                    sistem.setInstitutie(institutie);
                }

            }
            break;

            case JUDET: {
                Judet judet = judetRepository.findOne(idEntity);
                if (judet == null) {
                    throw new IllegalArgumentException("Nu exista judet avand id " + idEntity);
                }

                //
                // Verificare daca exista deja
                //
                sistem = sistemRepository.findByJudet_Id(judet.getId());

                if (sistem == null) {
                    sistem = new Sistem();

                    sistem.setJudet(judet);
                    //sistem.setNumeUtilizator("USJ-" + judet.getDenumire());
                    sistem.setDenumire("Judet");

                    Institutie institutie = institutieRepository.findOne(InstitutieEnum.JUDET.getId());
                    if (institutie == null) {
                        throw new IllegalArgumentException("Nu exista institutie cu id " + InstitutieEnum.JUDET.getId());
                    }

                    sistem.setInstitutie(institutie);
                }
            }
            break;

            case INSTITUTIE: {
                Institutie institutie = institutieRepository.findOne(idEntity);
                if (institutie == null) {
                    throw new IllegalArgumentException("Nu exista institutie cu id " + idEntity);
                }

                //
                // Verificare daca exista deja
                //
                sistem = sistemRepository.findByInstitutie_Id(institutie.getId());
                if (sistem == null) {
                    sistem = new Sistem();

                    sistem.setInstitutie(institutie);
                    //sistem.setNumeUtilizator("USI-" + institutie.getDenumire());
                    sistem.setDenumire("Judet");
                }
            }
            break;

            default:
                throw new IllegalArgumentException("ContextSistem necunoscut : " + context);

        }

        sistem.setDataGenerareLicenta(new Date());
        sistem.setLicenta(passwordEncoder.encode(token));
        sistem.setActiv(true);

        return salveaza(sistem);
    }

    @Override
    public String genereazaToken() {
        return tokenGenerator.generateToken();
    }

    @Override
    public Sistem salveaza(Sistem sistem) {
        if (sistem == null) {
            throw new IllegalArgumentException("Parametru sistem nedefinit");
        }

        return sistemRepository.save(sistem);
    }

    @Override
    public Sistem getUtilizatorSistem(Long idEntity, ContextSistem context) {

        switch (context) {
            case UAT:
                return sistemRepository.findByUat_Id(idEntity);
            case JUDET:
                return sistemRepository.findByJudet_Id(idEntity);
            case INSTITUTIE:
                return sistemRepository.findByInstitutie_Id(idEntity);
            default:
                throw new IllegalArgumentException("ContextSistem necunoscut : " + context);

        }
    }

    @Override
    public Sistem getUtilizatorSistem(Long idUtilizatorSistem) throws RanBusinessException {
        return sistemRepository.findOne(idUtilizatorSistem);
    }

    @Override
    public GenericListResult<Sistem> getUtilizatoriSistem(SistemSearchFilter sistemSearchFilter, PagingInfo pagingInfo, SortInfo sortInfo) throws RanBusinessException {
        //todo: search filter
        return sistemRepository.getListResult(new AndRepositoryCriteria(), pagingInfo, sortInfo);
    }
}
