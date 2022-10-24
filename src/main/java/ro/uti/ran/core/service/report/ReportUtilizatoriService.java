package ro.uti.ran.core.service.report;

import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import net.sf.jasperreports.engine.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.model.portal.Utilizator;
import ro.uti.ran.core.model.registru.DetinatorPj;
import ro.uti.ran.core.reporter.builders.ColumnBuilder;
import ro.uti.ran.core.reporter.builders.DynamicReportBuilder;


import ro.uti.ran.core.repository.base.AbstractRepositoryFilter;
import ro.uti.ran.core.repository.base.RanRepository;
import ro.uti.ran.core.repository.portal.UtilizatorRepository;
import ro.uti.ran.core.service.gospodarii.GospodariiSearchFilter;
import ro.uti.ran.core.service.utilizator.UtilizatoriSearchFilter;
import ro.uti.ran.core.utils.GenericListResult;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SearchFilter;
import ro.uti.ran.core.utils.SortInfo;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by adrian.boldisor on 3/10/2016.
 */


@Service
public class ReportUtilizatoriService {

    @Autowired
    UtilizatorRepository reportDynamicRepository;



    public byte[] getListaUtilizatoriXLS(Object model, String type, UtilizatoriSearchFilter searchFilter) throws ColumnBuilderException, ClassNotFoundException, JRException {

//        DynamicReportBuilder utilizatoriReport = new DynamicReportBuilder(model);
//        List<Utilizator> usersList = reportDynamicRepository.findAll();
//        utilizatoriReport.setReportsValues(usersList);
//
//
//        //utilizatoriReport.setConcreteColumnList(selectColumns);
//        utilizatoriReport.getClomunList();
//        Map<String, ColumnBuilder> columnsPropreties = utilizatoriReport.showReportByColumnName();
//        columnsPropreties.get("numeUtilizator").setName("NUME UTILIZATOR");



        return null ;// utilizatoriReport.buildReport("Utilizatori Raport", utilizatoriReport.XLS);
    }


    public void setColumnList(){

    }



}
