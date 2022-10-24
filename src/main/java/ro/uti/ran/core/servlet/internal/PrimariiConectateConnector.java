package ro.uti.ran.core.servlet.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.uti.ran.core.model.portal.Judet;
import ro.uti.ran.core.model.portal.UAT;
import ro.uti.ran.core.model.registru.view.ViewUatGospodarie;
import ro.uti.ran.core.repository.portal.UatRepository;
import ro.uti.ran.core.service.backend.view.ViewUatGospodarieService;
import ro.uti.ran.core.service.portal.JudetService;
import ro.uti.ran.core.service.portal.UatService;

@Component
public class PrimariiConectateConnector extends ExportConnector {
	
	@Autowired
	private ViewUatGospodarieService viewUatGospodarieService;
	@Autowired
	private JudetService judetService;
	@Autowired
	private UatService uatService;
	
	@Override
	public JSONArray getData(HttpServletRequest request) throws JSONException {

		List<Jsonable> primarii = new ArrayList<Jsonable>();
		
		for(final ViewUatGospodarie viewUatGospodarie : viewUatGospodarieService.findAll()) {
			primarii.add(new Primarie() {
			
				private static final long serialVersionUID = 1L;

			{
				Judet judetAsociat = judetService.findOne(viewUatGospodarie.getIdNomJudet());
				UAT uatAsociat = uatService.findOne(viewUatGospodarie.getUat());
				if(judetAsociat != null) {
					setJudet(judetAsociat.getDenumire());
				}
				if(uatAsociat != null) {
					setNume(uatAsociat.getDenumire());
				}
			}});
		}
		
		return getJson(primarii);
	}
	
	private class Primarie implements Serializable, Jsonable {
		
		private static final long serialVersionUID = -7145412077852955264L;
		
		private String nume;
		private String judet;
		
		public String getNume() {
			return nume;
		}
		public void setNume(String nume) {
			this.nume = nume;
		}
		public String getJudet() {
			return judet;
		}
		public void setJudet(String judet) {
			this.judet = judet;
		}
		
		@Override
		public JSONObject getJson() throws JSONException {
			JSONObject object = new JSONObject();
			
			object.put("nume", getNume());
			object.put("judet", getJudet());
			
			return object;
		}
		
		
	}


	

}
