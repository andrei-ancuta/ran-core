package ro.uti.ran.core.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

/**
 * Created by adrian.boldisor on 3/11/2016.
 */

//@Configuration
public class JasperConfig {




    @Bean(name = "userReport")
    public JasperReportsPdfView userReport() {

        JasperReportsPdfView pdfView = new JasperReportsPdfView();
        pdfView.setUrl("classpath:reports-template/UtilizatoriList.jrxml");
        pdfView.setReportDataKey("dataSource");

        return pdfView;
    }

}
