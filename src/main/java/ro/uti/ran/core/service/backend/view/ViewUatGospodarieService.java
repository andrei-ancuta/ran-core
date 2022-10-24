package ro.uti.ran.core.service.backend.view;

import java.util.List;

import ro.uti.ran.core.model.registru.view.ViewUatGospodarie;

public interface ViewUatGospodarieService {

	public List<ViewUatGospodarie> findAll();
	
	public List<ViewUatGospodarie> findByJudet(int year);

	public List<ViewUatGospodarie> findTotalByJudet(int year);

	public List<ViewUatGospodarie> findByJudetAndAn(Long idJudet,int year);

	ViewUatGospodarie findByUatAndAn(Long idNomUat,Integer an);

	long totalUatCareADeclaratInventarAnual(int year,Long idJudet);

	long totalUatDinJudet(int year,Long idJudet);
}
