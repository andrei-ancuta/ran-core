package ro.uti.ran.core.service.backend.nomenclator;

import ro.uti.ran.core.model.registru.TipNomenclator;

/**
 * Created by Stanciu Neculai on 20.Nov.2015.
 */
public enum ExportableParamsEnum {
    CAPITOL(TipNomenclator.CapCultura),
    CAPITOL_PROD(TipNomenclator.CapCultura);

    private final TipNomenclator nomType;

    ExportableParamsEnum(TipNomenclator nomType) {
        this.nomType = nomType;
    }

    public TipNomenclator getNomType() {
        return nomType;
    }
}
