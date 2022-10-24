package ro.uti.ran.core.servlet.internal;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.uti.ran.core.model.portal.Judet;
import ro.uti.ran.core.model.portal.UAT;
import ro.uti.ran.core.model.registru.NomUat;
import ro.uti.ran.core.model.registru.view.ViewUatGospodarie;
import ro.uti.ran.core.repository.portal.UatRepository;
import ro.uti.ran.core.repository.registru.NomUatRepository;
import ro.uti.ran.core.service.backend.view.ViewUatGospodarieService;
import ro.uti.ran.core.service.portal.JudetService;
import ro.uti.ran.core.service.portal.UatService;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanciu Neculai on 01.Mar.2016.
 */
@Component
public class GradIncarcareModalConnector extends ExportConnector {
    public static final Logger log = LoggerFactory.getLogger(GradIncarcareModalConnector.class);
    public static final String TOATE_JUDETELE = "total";

    @Autowired
    private ViewUatGospodarieService viewUatGospodarieService;
    @Autowired
    private NomUatRepository nomUatRepository;
    @Autowired
    private JudetService judetService;
    @Autowired
    private UatService uatService;

    @Override
    public JSONArray getData(HttpServletRequest request) throws JSONException {
        List<Jsonable> exportableJsonArray = new ArrayList<>();
        final int year = Integer.valueOf(request.getParameter(_YEAR));
        final String idJudet = request.getParameter(_ID_JUDET);
        if (idJudet == null || idJudet.isEmpty()) {
            throw new JSONException("idJudet is null");
        }
        if (TOATE_JUDETELE.equals(idJudet)) {
            List<NomUat> nomUatList = nomUatRepository.findAll();
            List<Long> uatList = new ArrayList<>();
            List<ViewUatGospodarie> viewUatGospodarieList = viewUatGospodarieService.findByJudet(year);
            for (ViewUatGospodarie viewUatGospodarie : viewUatGospodarieList) {
                uatList.add(viewUatGospodarie.getUat());
                Judet judet = judetService.findOne(viewUatGospodarie.getIdNomJudet());
                UAT uat = uatService.findOne(viewUatGospodarie.getUat());
                exportableJsonArray.add(from(viewUatGospodarie, judet, uat));
            }
            log.info(String.format("Pentru anul %d au fost gasite %d viewUatGospodarie pentru toate judetele si un total de %d UAT-uri", year, viewUatGospodarieList.size(), nomUatList.size()));
            for (NomUat nomUat : nomUatList) {
                if (!uatList.contains(nomUat.getId())) {
                    ViewUatGospodarie viewUatGospodarie = new ViewUatGospodarie();
                    viewUatGospodarie.setUat(nomUat.getId());
                    viewUatGospodarie.setAn(year);
                    viewUatGospodarie.setIdNomJudet(nomUat.getNomJudet().getId());
                    viewUatGospodarie.setTotalUatGospodarie(0L);
                    viewUatGospodarie.setTotalUatDeclaratie(0L);
                    Judet judet = judetService.findOne(viewUatGospodarie.getIdNomJudet());
                    UAT uat = uatService.findOne(viewUatGospodarie.getUat());
                    exportableJsonArray.add(from(viewUatGospodarie, judet, uat));
                }
            }
            log.info(String.format("TOATE_JUDETELE %d:Numarul de viewUatGospodarie este: %d", year, exportableJsonArray.size()));
        } else {
            try {
                List<Long> uatList = new ArrayList<>();
                Long idJudetLong = Long.valueOf(idJudet);
                List<NomUat> nomUatList = nomUatRepository.nomUatDinJudet(idJudetLong);
                List<ViewUatGospodarie> viewUatGospodarieList = viewUatGospodarieService.findByJudetAndAn(idJudetLong, year);
                Judet judet = judetService.findOne(idJudetLong);
                for (ViewUatGospodarie viewUatGospodarie : viewUatGospodarieList) {
                    uatList.add(viewUatGospodarie.getUat());
                    UAT uat = uatService.findOne(viewUatGospodarie.getUat());
                    exportableJsonArray.add(from(viewUatGospodarie, judet, uat));
                }
                for (NomUat nomUat : nomUatList) {
                    if (!uatList.contains(nomUat.getId())) {
                        ViewUatGospodarie viewUatGospodarie = new ViewUatGospodarie();
                        viewUatGospodarie.setUat(nomUat.getId());
                        viewUatGospodarie.setAn(year);
                        viewUatGospodarie.setIdNomJudet(idJudetLong);
                        viewUatGospodarie.setTotalUatGospodarie(0L);
                        viewUatGospodarie.setTotalUatDeclaratie(0L);
                        UAT uat = uatService.findOne(viewUatGospodarie.getUat());
                        exportableJsonArray.add(from(viewUatGospodarie, judet, uat));
                    }
                }
            } catch (NumberFormatException e) {
                log.error(e.getMessage(), e);
            }
        }
        return getJson(exportableJsonArray);
    }

    protected ViewUatGospodarieJson from(ViewUatGospodarie viewUatGospodarie, Judet judet, UAT uat) {
        ViewUatGospodarieJson json = new ViewUatGospodarieJson();
        json.setUat(viewUatGospodarie.getUat());
        json.setAn(viewUatGospodarie.getAn());
        json.setIdNomJudet(viewUatGospodarie.getIdNomJudet());
        json.setTotalUatGospodarie(viewUatGospodarie.getTotalUatGospodarie());
        json.setTotalUatDeclaratie(viewUatGospodarie.getTotalUatDeclaratie());
        json.setDenumireJudet(judet.getDenumire());
        json.setDenumireUat(uat.getDenumire());
        return json;
    }

    protected class ViewUatGospodarieJson implements Serializable, ExportConnector.Jsonable {

        private Long uat;
        private Integer an;
        private Long idNomJudet;
        private Long totalUatGospodarie;
        private Long totalUatDeclaratie;
        private String denumireJudet;
        private String denumireUat;

        public Long getUat() {
            return uat;
        }

        public void setUat(Long uat) {
            this.uat = uat;
        }

        public Integer getAn() {
            return an;
        }

        public void setAn(Integer an) {
            this.an = an;
        }

        public Long getIdNomJudet() {
            return idNomJudet;
        }

        public void setIdNomJudet(Long idNomJudet) {
            this.idNomJudet = idNomJudet;
        }

        public Long getTotalUatGospodarie() {
            return totalUatGospodarie;
        }

        public void setTotalUatGospodarie(Long totalUatGospodarie) {
            this.totalUatGospodarie = totalUatGospodarie;
        }

        public Long getTotalUatDeclaratie() {
            return totalUatDeclaratie;
        }

        public void setTotalUatDeclaratie(Long totalUatDeclaratie) {
            this.totalUatDeclaratie = totalUatDeclaratie;
        }

        public String getDenumireJudet() {
            return denumireJudet;
        }

        public void setDenumireJudet(String denumireJudet) {
            this.denumireJudet = denumireJudet;
        }

        public String getDenumireUat() {
            return denumireUat;
        }

        public void setDenumireUat(String denumireUat) {
            this.denumireUat = denumireUat;
        }

        @Override
        public JSONObject getJson() throws JSONException {
            JSONObject object = new JSONObject();
            object.put("uat", getUat());
            object.put("an", getAn());
            object.put("idNomJudet", getIdNomJudet());
            object.put("totalUatGospodarie", getTotalUatGospodarie());
            object.put("totalUatDeclaratie", getTotalUatDeclaratie());
            object.put("denumireJudet", getDenumireJudet());
            object.put("denumireUat", getDenumireUat());

            return object;
        }
    }
}


