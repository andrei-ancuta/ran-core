package ro.uti.ran.core.servlet.internal;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class ExportConnector {

    protected final static String _YEAR = "year";
    protected final static String _ID_JUDET = "idJudet";

    public abstract JSONArray getData(HttpServletRequest request) throws JSONException;

    public String getName() {
        String[] _class = this.getClass().getCanonicalName().split("\\.");
        return _class[_class.length - 1];
    }

    protected JSONArray getJson(List<Jsonable> list) throws JSONException {

        JSONArray array = new JSONArray();

        for (Jsonable jsonable : list) {
            array.put(jsonable.getJson());
        }

        return array;
    }


    protected interface Jsonable {
        public JSONObject getJson() throws JSONException;
    }
}
