package ro.uti.ran.core.servlet.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.uti.ran.core.model.portal.Judet;
import ro.uti.ran.core.model.registru.view.ViewUatGospodarie;
import ro.uti.ran.core.repository.registru.NomUatRepository;
import ro.uti.ran.core.service.backend.view.ViewUatGospodarieService;
import ro.uti.ran.core.service.portal.JudetService;

@Component
public class GradIncarcareConnector extends ExportConnector {

    private final static String _TOTAL = "Total";

    public static final Logger log = LoggerFactory.getLogger(GradIncarcareConnector.class);


    @Autowired
    private ViewUatGospodarieService viewUatGospodarieService;
    @Autowired
    private JudetService judetRepository;
    @Autowired
    private NomUatRepository nomUatRepository;

    @Override
    public JSONArray getData(HttpServletRequest request) throws JSONException {

        List<Jsonable> grade = new ArrayList<Jsonable>();

        final double[] sumGrade = new double[]{0.0};

        final int year = Integer.valueOf(request.getParameter(_YEAR));
        for (final ViewUatGospodarie viewUatGospodarie : viewUatGospodarieService.findTotalByJudet(year)) {

            grade.add(new Grad() {

                private static final long serialVersionUID = 1L;

                {
                    Judet judetAsociat = judetRepository.findOne(viewUatGospodarie.getIdNomJudet());
                    if (judetAsociat != null) {
                        setJudet(judetAsociat.getDenumire());
                        setIdJudet(judetAsociat.getId());
                    }
                    double sumaGrade = 0;
                    List<ViewUatGospodarie> viewUatGospodarieUatList = viewUatGospodarieService.findByJudetAndAn(viewUatGospodarie.getIdNomJudet(), year);

                    if (viewUatGospodarieUatList == null || viewUatGospodarieUatList.isEmpty()) {
                        setGrad(0.0);
                    } else {
                        for (ViewUatGospodarie viewUatGospodarieUat : viewUatGospodarieService.findByJudetAndAn(viewUatGospodarie.getIdNomJudet(), year)) {
                            double gradUat = getProcent(viewUatGospodarieUat.getTotalUatDeclaratie(), viewUatGospodarieUat.getTotalUatGospodarie());
                            if(gradUat > 100.0) {
                                gradUat = 100.0;
                            }
                            sumaGrade += gradUat;
                        }
                        setGrad((sumaGrade / nomUatRepository.totalUatDinJudet(viewUatGospodarie.getIdNomJudet())));
                    }
                    sumGrade[0] += getGrad();
                }
            });
        }
        double nrJudete = 42.0;
        final Double grad = sumGrade[0] / nrJudete;
        grade.add(new Grad() {

            private static final long serialVersionUID = 31485961771167693L;

            {
                setJudet(_TOTAL);
                setGrad(grad);
            }
        });

        Collections.reverse(grade);
        return getJson(grade);
    }


    private Double getProcent(Long a, Long b) {
        if (a == null || a == 0) {
            return 0D;
        } else if (b == null || b == 0) {
            return 100D;
        } else {
            return 100 * a / Double.valueOf(b);
        }
    }

    private class Grad implements Serializable, Jsonable {

        private static final long serialVersionUID = -7145412077852955264L;

        private String judet;
        private Long idJudet;
        private Double grad;

        public String getJudet() {
            return judet;
        }

        public void setJudet(String judet) {
            this.judet = judet;
        }

        public Double getGrad() {
            return grad;
        }

        public void setGrad(Double grad) {
            this.grad = grad;
        }

        public Long getIdJudet() {
            return idJudet;
        }

        public void setIdJudet(Long idJudet) {
            this.idJudet = idJudet;
        }

        @Override
        public JSONObject getJson() throws JSONException {
            JSONObject object = new JSONObject();

            object.put("judet", getJudet());
            object.put("grad", getGrad().toString());
            object.put("idJudet", getIdJudet());

            return object;
        }


    }


}
