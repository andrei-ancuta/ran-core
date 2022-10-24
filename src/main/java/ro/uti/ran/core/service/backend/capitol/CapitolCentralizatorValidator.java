package ro.uti.ran.core.service.backend.capitol;

import org.apache.commons.beanutils.PropertyUtils;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.model.registru.CapFructProd;
import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.service.backend.dto.RandCapitolCentralizator;
import ro.uti.ran.core.service.backend.dto.RandCapitol_12dDTO;
import ro.uti.ran.core.service.backend.utils.TipCapitol;

import static ro.uti.ran.core.exception.codes.DateRegistruValidationCodes.*;
import static ro.uti.ran.core.model.utils.RanConstants.NOM_VALOARE_DA;

/**
 * Created by bogdan.ardeleanu on 1/28/2016.
 */
public class CapitolCentralizatorValidator<T extends Nomenclator, R extends RandCapitolCentralizator> {
    private final T nomenclator;

    private final R rand;

    private final TipCapitol tipCapitol;

    public CapitolCentralizatorValidator(T nomenclator, R rand, TipCapitol tipCapitol) {
        this.nomenclator = nomenclator;
        this.rand = rand;
        this.tipCapitol = tipCapitol;
    }

    public void validateField(String fieldNameNomenclator, String fieldNameRand, String label) throws DateRegistruValidationException {
        try {
            if (TipCapitol.CAP12d.equals(tipCapitol)) {
                Integer valueNomenclator = (Integer) PropertyUtils.getProperty(nomenclator, fieldNameNomenclator);
                Object valueRand = PropertyUtils.getProperty(rand, fieldNameRand);
                //camp care nu trebuie completat
                if (!NOM_VALOARE_DA.equals(valueNomenclator) && valueRand != null) {
                    throw new DateRegistruValidationException(GENERIC_FIELD_INVALID, label, rand.getCod(), rand.getCodRand(), tipCapitol.name());
                }
            } else {
                Integer valueNomenclator = (Integer) PropertyUtils.getProperty(nomenclator, fieldNameNomenclator);
                Object valueRand = PropertyUtils.getProperty(rand, fieldNameRand);

                if (!NOM_VALOARE_DA.equals(valueNomenclator) && valueRand != null) {
                    throw new DateRegistruValidationException(GENERIC_FIELD_INVALID, label, rand.getCod(), rand.getCodRand(), tipCapitol.name());
                }

                if (NOM_VALOARE_DA.equals(valueNomenclator) && valueRand == null) {
                    throw new DateRegistruValidationException(GENERIC_FIELD_MANDATORY, rand.getCod(), rand.getCodRand(), tipCapitol.name(), label);
                }
            }
        } catch (DateRegistruValidationException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }


    }


    public void validareRandCAP12d() throws DateRegistruValidationException {
        try {

            //se trimit informatii livada + pom razlet ori livada ori pom razlet
            RandCapitol_12dDTO randCapitol_12dDTO = (RandCapitol_12dDTO) rand;
            if (randCapitol_12dDTO.getProdTotalaLivezi() == null && randCapitol_12dDTO.getSuprafataLivezi() == null
                    && randCapitol_12dDTO.getNrPomiRazleti() == null && randCapitol_12dDTO.getProdTotalaPomiRazleti() == null) {
                throw new DateRegistruValidationException(GENERIC_FIELD_MANDATORY, rand.getCod(), rand.getCodRand(), tipCapitol.name(), "arieLiveziHa si productieTotalaLivezi sau  nrPomiRazleti si productieTotalaPomiRazleti");
            }
            CapFructProd capFructProd = (CapFructProd) nomenclator;
            //livada
            if (NOM_VALOARE_DA.equals(capFructProd.getIsProdTotalLivada())) {
                if (randCapitol_12dDTO.getProdTotalaLivezi() != null && randCapitol_12dDTO.getSuprafataLivezi() == null) {
                    throw new DateRegistruValidationException(GENERIC_FIELD_MANDATORY, rand.getCod(), rand.getCodRand(), tipCapitol.name(), "arieLiveziHa");
                }
                if (randCapitol_12dDTO.getProdTotalaLivezi() == null && randCapitol_12dDTO.getSuprafataLivezi() != null) {
                    throw new DateRegistruValidationException(GENERIC_FIELD_MANDATORY, rand.getCod(), rand.getCodRand(), tipCapitol.name(), "productieTotalaLivezi");
                }
                if (NOM_VALOARE_DA.equals(capFructProd.getIsProdMedieLivada()) && randCapitol_12dDTO.getProdTotalaLivezi() != null
                        && randCapitol_12dDTO.getSuprafataLivezi() != null && randCapitol_12dDTO.getProdMedieLivezi() == null) {
                    throw new DateRegistruValidationException(PRODUCTIE_MEDIE_OBLIGATORIE, rand.getCod(), rand.getCodRand(), tipCapitol.name());
                }
            }
            //pom razlet
            if (NOM_VALOARE_DA.equals(capFructProd.getIsProdTotalPomRazlet())) {
                if (randCapitol_12dDTO.getNrPomiRazleti() != null && randCapitol_12dDTO.getProdTotalaPomiRazleti() == null) {
                    throw new DateRegistruValidationException(GENERIC_FIELD_MANDATORY, rand.getCod(), rand.getCodRand(), tipCapitol.name(), "productieTotalaPomiRazleti");
                }
                if (randCapitol_12dDTO.getNrPomiRazleti() == null && randCapitol_12dDTO.getProdTotalaPomiRazleti() != null) {
                    throw new DateRegistruValidationException(GENERIC_FIELD_MANDATORY, rand.getCod(), rand.getCodRand(), tipCapitol.name(), "nrPomiRazleti");
                }
                if (NOM_VALOARE_DA.equals(capFructProd.getIsProdMediePomRazlet()) && randCapitol_12dDTO.getProdTotalaPomiRazleti() != null
                        && randCapitol_12dDTO.getNrPomiRazleti() != null && randCapitol_12dDTO.getProdMediePomiRazleti() == null) {
                    throw new DateRegistruValidationException(PRODUCTIE_MEDIE_OBLIGATORIE, rand.getCod(), rand.getCodRand(), tipCapitol.name());
                }

            }
        } catch (DateRegistruValidationException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


}

