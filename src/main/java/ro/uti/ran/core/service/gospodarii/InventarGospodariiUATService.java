package ro.uti.ran.core.service.gospodarii;

import ro.uti.ran.core.model.registru.InventarGospUat;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.internal.gospodarii.InfoInventarGospUat;
import ro.uti.ran.core.ws.internal.gospodarii.InventarGospUatList;

/**
 * Created by Anastasia cea micuta on 1/19/2016.
 */
public interface InventarGospodariiUATService {
    InventarGospUatList getInventarGospodariiUat(SortInfo sortInfo, Integer codSiruta);

    void updateOrCreate(InfoInventarGospUat infoInventarGospUat);

    InventarGospUat getByAnAndSiruta(Integer an, Integer codSiruta);

}
