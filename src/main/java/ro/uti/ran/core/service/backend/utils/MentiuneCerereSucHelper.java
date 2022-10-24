package ro.uti.ran.core.service.backend.utils;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import ro.uti.ran.core.model.registru.MentiuneCerereSuc;

/**
 * Created by Dan on 10-Nov-15.
 */
public class MentiuneCerereSucHelper {
    private long dataInregistrare;
    private String nrInregistrare;

    public MentiuneCerereSucHelper(MentiuneCerereSuc mentiuneCerereSuc) {
        this.dataInregistrare = DataRaportareHelper.getDataOra_00_00_00_000(mentiuneCerereSuc.getDataInregistrare()).getTime();
        this.nrInregistrare = mentiuneCerereSuc.getNrInregistrare() != null ? mentiuneCerereSuc.getNrInregistrare().toLowerCase() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MentiuneCerereSucHelper that = (MentiuneCerereSucHelper) o;

        return new EqualsBuilder()
                .append(dataInregistrare, that.dataInregistrare)
                .append(nrInregistrare, that.nrInregistrare)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(dataInregistrare)
                .append(nrInregistrare)
                .toHashCode();
    }

    public static boolean isEquals(MentiuneCerereSuc a1, MentiuneCerereSuc a2) {
        if (a1 == null) {
            throw new IllegalArgumentException("MentiuneCerereSuc a1 nedefinit!");
        }
        if (a2 == null) {
            throw new IllegalArgumentException("MentiuneCerereSuc a2 nedefinit!");
        }
        return (new MentiuneCerereSucHelper(a1)).equals(new MentiuneCerereSucHelper(a2));
    }
}
