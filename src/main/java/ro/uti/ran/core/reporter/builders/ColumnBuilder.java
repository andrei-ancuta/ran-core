package ro.uti.ran.core.reporter.builders;

import ar.com.fdvs.dj.domain.ColumnProperty;

/**
 * Created by adrian.boldisor on 3/15/2016.
 */
public class ColumnBuilder {

    private String name;
    private String title;
    private String type;

    public static ColumnBuilder getNew() {
        return new ColumnBuilder();
    }


    public ColumnBuilder setColumnProperty(String name, String title, String type) {
       this.name = name;
       this.title = title;
       this.type = type;
       return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }


}
