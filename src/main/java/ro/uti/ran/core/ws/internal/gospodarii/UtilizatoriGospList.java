package ro.uti.ran.core.ws.internal.gospodarii;



import ro.uti.ran.core.utils.HasItems;
import ro.uti.ran.core.utils.ListResult;

import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Created by adrian.boldisor on 2/8/2016.
 */
@XmlType
public class UtilizatoriGospList  extends ListResult implements HasItems<InfoUtilizatoriGosp>  {



   private List<InfoUtilizatoriGosp> items ;

   public UtilizatoriGospList(){

    }


    public UtilizatoriGospList(List<InfoUtilizatoriGosp> infoUtilizatoriGosp) {
        if (!infoUtilizatoriGosp.isEmpty()) {
            this.setTotalListGospodariiPjByUser(infoUtilizatoriGosp);
        }

    }


    public List <InfoUtilizatoriGosp> getTotalListGospodariiPjByUser(){
        return items;
    }

    public void setTotalListGospodariiPjByUser(List <InfoUtilizatoriGosp>InfoUtilizatoriGospList){
        this.items = InfoUtilizatoriGospList;
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
