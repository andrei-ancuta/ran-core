package ro.uti.ran.core.service.backend.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.model.registru.view.ViewUatGospodarie;
import ro.uti.ran.core.repository.registru.view.ViewUatGospodarieRepository;

import java.util.Calendar;
import java.util.List;

@Service
public class ViewUatGospodarieServiceImpl implements ViewUatGospodarieService {

    @Autowired
    private ViewUatGospodarieRepository viewUatGospodarieRepository;

    public List<ViewUatGospodarie> findAll() {
        return viewUatGospodarieRepository.findAll();
    }

    @Override
    public List<ViewUatGospodarie> findByJudet(int year) {
        return viewUatGospodarieRepository.findByJudet(year);
    }

    public List<ViewUatGospodarie> findTotalByJudet(int year){
        return viewUatGospodarieRepository.findTotalByJudet(year);
    }

    @Override
    public List<ViewUatGospodarie> findByJudetAndAn(Long idJudet, int year) {
        return viewUatGospodarieRepository.findByJudetAndAn(idJudet,year);
    }

    @Override
    public ViewUatGospodarie findByUatAndAn(Long idNomUat, Integer an) {
        return viewUatGospodarieRepository.findByUatAndAn(idNomUat, an);
    }

    @Override
    public long totalUatCareADeclaratInventarAnual(int year,Long idJudet) {
        final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if(idJudet == null){
            throw new IllegalArgumentException("IdJudet should not be null");
        }
        int reqYear = year == 0 ? currentYear : year;
        if(reqYear == currentYear){

        }
        return viewUatGospodarieRepository.totalUatCareADeclaratInventarAnual(reqYear,idJudet);
    }

    @Override
    public long totalUatDinJudet(int year,Long idJudet) {
        final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if(idJudet == null){
            throw new IllegalArgumentException("IdJudet should not be null");
        }
        String dataStart;
        String dataStop;
        if(year == 0){
              dataStart = "01-01-"+currentYear;
              dataStop = "31-12-"+currentYear;
        } else {
             dataStart = "01-01-" + year;
             dataStop = "31-12-" + year;
        }
        return viewUatGospodarieRepository.totalUatDinJudet(dataStop,idJudet);
    }
}
