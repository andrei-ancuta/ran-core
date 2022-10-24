package ro.uti.ran.core.ws.internal.gospodarii;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.model.registru.InventarGospUat;
import ro.uti.ran.core.service.gospodarii.InventarGospodariiUATService;
import ro.uti.ran.core.utils.SortInfo;

import javax.jws.WebService;

@WebService(
        serviceName = "TotalGospodariiService",
        endpointInterface = "ro.uti.ran.core.ws.internal.gospodarii.IInventarGospUatService",
        targetNamespace = "http://info.internal.ws.core.ran.uti.ro",
        portName = "TotalGospodariiServicePort")
@Service("InventarGospUatService")
public class InventarGospUatServiceImpl implements IInventarGospUatService {

    @Autowired
    private InventarGospodariiUATService inventarGospodariiUATService;


    @Override
    public InventarGospUatList getInventarGospodariiUat(SortInfo sortInfo, Integer codSiruta) {
        return inventarGospodariiUATService.getInventarGospodariiUat(sortInfo, codSiruta);
    }

    @Override
    public void updateOrCreate(InfoInventarGospUat infoInventarGospUat) {
        inventarGospodariiUATService.updateOrCreate(infoInventarGospUat);
    }

    @Override
    public InventarGospUat getByAnAndSiruta(Integer an, Integer codSiruta) {
        return inventarGospodariiUATService.getByAnAndSiruta(an, codSiruta);
    }
}