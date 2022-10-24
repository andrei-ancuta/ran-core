package ro.uti.ran.core.service.backend.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dan on 19-Oct-15.
 */
public class SuprafataHelper {

    public final static int HA_TO_MP = 10000;
    public final static int ARI_TO_MP = 100;
    public final static String KEY_HA = "HA";
    public final static String KEY_ARI = "ARI";


    public static Integer transformToMP(BigDecimal valueARI, Integer valueHA) {
        BigDecimal valueMP = BigDecimal.ZERO;
        if (valueHA != null) {
            valueMP = valueMP.add(BigDecimal.valueOf(valueHA * HA_TO_MP));
        }
        if (valueARI != null) {
            valueMP = valueMP.add(valueARI.multiply(BigDecimal.valueOf(ARI_TO_MP)));
        }
        return valueMP.intValue();
    }

    /**
     * BigInteger ha si BigDecimal arii
     *
     * @param valueMP valoarea suprafetei in MP
     * @return valoarea suprafetei defalcata in HA si ARI
     */
    public static Map<String, Object> extractHAAndARIFromMP(BigInteger valueMP) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        BigInteger nrIntregHA = valueMP.divide(BigInteger.valueOf(HA_TO_MP));
        if (nrIntregHA.compareTo(BigInteger.ZERO) != -1) {
            returnMap.put(KEY_HA, nrIntregHA);
        }
        BigInteger restMPDupaExtragereNrIntregHA = valueMP.mod(BigInteger.valueOf(HA_TO_MP));
        if (restMPDupaExtragereNrIntregHA.compareTo(BigInteger.ZERO) == 1) {
            BigDecimal rest = BigDecimal.valueOf(restMPDupaExtragereNrIntregHA.longValue());
            returnMap.put(KEY_ARI, rest.divide(BigDecimal.valueOf(ARI_TO_MP), 2, BigDecimal.ROUND_DOWN));
        }
        return returnMap;
    }

}
