package ro.uti.ran.core.ws.internal.gospodarii;

import ro.uti.ran.core.model.registru.InventarGospUat;

import java.util.Comparator;

public class InventarGospUatComparator implements Comparator<InventarGospUat> {

    @Override
    public int compare(InventarGospUat o1, InventarGospUat o2) {
        return o1.getAn().compareTo(o2.getAn());
    }
}