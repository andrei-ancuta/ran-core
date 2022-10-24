package ro.uti.ran.core.service.backend.utils;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;

/**
 * Created by Dan on 19-Oct-15.
 */
public enum TipCapitol {
    CAP0_12, CAP0_34,
    CAP1, CAP1_cent(false),
    CAP2a, CAP2a_cent(false), CAP2b, CAP2b_cent(false),
    CAP3,
    CAP3_cent,
    CAP4a, CAP4a_cent(false), CAP4a1, CAP4a1_cent(false), CAP4b1, CAP4b1_cent(false), CAP4b2, CAP4b2_cent(false), CAP4c, CAP4c_cent(false),
    CAP5a, CAP5a_cent(false), CAP5b, CAP5b_cent(false), CAP5c, CAP5c_cent(false), CAP5d, CAP5d_cent(false),
    CAP6, CAP6_cent(false),
    CAP7, CAP7_cent(false),
    CAP8, CAP8_cent(false),
    CAP9, CAP9_cent(false),
    CAP10a, CAP10a_cent(false), CAP10b, CAP10b_cent(false),
    CAP11, CAP11_cent(false),
    CAP12,
    CAP13,
    CAP14,
    CAP15a, CAP15b,
    CAP16,
    CAP12a,
    CAP12a1,
    CAP12b1,
    CAP12b2,
    CAP12c,
    CAP12d,
    CAP12e,
    CAP12f,
    CAP13cent;

    private boolean used = true;

    TipCapitol() {
    }

    TipCapitol(boolean used) {
        this.used = used;
    }

    public boolean isUsed() {
        return used;
    }

    public static TipCapitol checkTipCapitol(String tipCapitolName) throws RequestValidationException {
        for (TipCapitol tipCapitol : values()) {
            if (tipCapitol.name().equals(tipCapitolName)) {
                return tipCapitol;
            }
        }

        throw new RequestValidationException(RequestCodes.INVALID_ELEMENT, "tipCapitol");
    }

    public String getCapitolClassName() {
        try {
            return name().replace("CAP", "Capitol_");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
