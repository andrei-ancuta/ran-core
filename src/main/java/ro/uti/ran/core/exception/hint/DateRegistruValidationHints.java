package ro.uti.ran.core.exception.hint;

/**
 * Created by Dan on 16-Nov-15.
 */
public interface DateRegistruValidationHints {
    String GOSPODARIE_NOT_FOUND_HINT = "Trimiteti mai intai CAP0_12 sau CAP0_34.";
    String GOSPODARIA_ARE_CAPITOLE_HINT = "Anulati mai intai toate capitolele diferite de CAP0_12 sau CAP0_34 transmise anterior pentru aceasta gospodarie";
    String RESTRICTIE_ADRESA_CUA_AND_GEOMETRIE_HINT = "Completati doar 'cua' sau 'referintaGeoXml'.";
}
