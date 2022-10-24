package ro.uti.ran.core.ws.internal.gospodarii;

import ro.uti.ran.core.utils.HasItems;
import ro.uti.ran.core.utils.ListResult;

import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlType
public class InventarGospUatList extends ListResult implements HasItems {

    private List<InfoInventarGospUat> items;

    public InventarGospUatList() {
    }

    ;

    public InventarGospUatList(List<InfoInventarGospUat> infoInventarGospUatList) {
        if (!infoInventarGospUatList.isEmpty()) {
            this.setInfoTotalGospodarieList(infoInventarGospUatList);
        }

    }

    public List<InfoInventarGospUat> getInfoTotalGospodarieList() {
        return items;
    }

    public void setInfoTotalGospodarieList(List<InfoInventarGospUat> infoInventarGospUatList) {
        this.items = infoInventarGospUatList;
    }

    @Override
    public void setItems(List items) {
        this.items = items;
    }

    @Override
    public List getItems() {
        return items;
    }
}
