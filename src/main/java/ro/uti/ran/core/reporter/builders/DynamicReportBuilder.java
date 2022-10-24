package ro.uti.ran.core.reporter.builders;


import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


import org.apache.poi.UnsupportedFileFormatException;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.core.NamingPolicy;
import org.springframework.cglib.core.Predicate;
import ro.uti.ran.core.exception.FileFormatException;
import ro.uti.ran.core.exception.NotExtendModelClassException;
import ro.uti.ran.core.model.Model;
import ro.uti.ran.core.model.portal.Utilizator;
import ro.uti.ran.core.reporter.utils.Exporter;
import ro.uti.ran.core.repository.base.RanRepository;
import ro.uti.ran.core.service.utilizator.UtilizatorRowItem;
import ro.uti.ran.core.utils.GenericListResult;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by adrian.boldisor on 3/15/2016.
 */
public class DynamicReportBuilder {

    public static final String PDF = "PDF";
    public static final String XLS = "XLS";
    public static final String HTML = "HTMl";
    public static final String JPEG = "JPEG";


    private List<ColumnBuilder> columns = new ArrayList<>();
    Map<String, ColumnBuilder> setupMainColumnsPropretie = new HashMap<>();
    private List<?> values = new ArrayList<>();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    private DynamicReportBuilder() {
    }


    public DynamicReportBuilder(GenericListResult reportValues, Class teamplateBean) {
        setReportsValues(reportValues.getItems());


        Class<?> clazz = null;


        try {
            clazz = Class.forName(teamplateBean.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (!(Model.class.isAssignableFrom(clazz))) {
            throw new NotExtendModelClassException(teamplateBean);
        }
        try {
            for (PropertyDescriptor propertyDescriptor : java.beans.Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
                if ("java.lang.Class".equalsIgnoreCase(propertyDescriptor.getPropertyType().getName())) {
                    continue;
                }
                addColumnForReport(propertyDescriptor.getName(), propertyDescriptor.getName(), propertyDescriptor.getPropertyType().getName());
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        for (ColumnBuilder column : this.columns) {
            setupMainColumnsPropretie.put(column.getName(), column);
        }
    }


    public DynamicReportBuilder(List items, Class teamplateBean) {
        Class<?> clazz = null;
        setReportsValues(items);

        try {
            clazz = Class.forName(teamplateBean.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            for (PropertyDescriptor propertyDescriptor : java.beans.Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
                if ("java.lang.Class".equalsIgnoreCase(propertyDescriptor.getPropertyType().getName())) {
                    continue;
                }
                addColumnForReport(propertyDescriptor.getName(), propertyDescriptor.getName(), propertyDescriptor.getPropertyType().getName());
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        for (ColumnBuilder column : this.columns) {
            setupMainColumnsPropretie.put(column.getName(), column);
        }
    }


    public DynamicReportBuilder addColumnForReport(String name, String title, String type) {
        this.columns.add(ColumnBuilder.getNew().setColumnProperty(name, title, type));
        return this;
    }


    public Map<String, ColumnBuilder> showReportByColumnName() {
        return setupMainColumnsPropretie;
    }





    public void setConcreteColumnList(String... columns) {
        Map<String, ColumnBuilder> setupColumns = new HashMap<>();
        List<ColumnBuilder> columnsSetup = new ArrayList<>();
        for (int i = 0; i < columns.length; i++) {
            ColumnBuilder column = setupMainColumnsPropretie.get(columns[i]);
            columnsSetup.add(column);
            setupColumns.put(columns[i], column);
        }
        this.columns = columnsSetup;
        this.setupMainColumnsPropretie = setupColumns;

    }


    public byte[] buildReport(String title, String format) {

        FastReportBuilder drb = new FastReportBuilder();



        try {
            for (ColumnBuilder column : this.columns) {
                if(column.getType().equalsIgnoreCase(List.class.getName())){
                    drb.addColumn(column.getName(), column.getTitle(), String.class.getName(), 5);
                }
                drb.addColumn(column.getName(), column.getTitle(), column.getType(), 5);

            }

            drb.setPrintColumnNames(true)

                    // Disables pagination
                    .setIgnorePagination(true)

                            // Experiment with this numbers to see the effect
                    .setMargins(1, 1, 1, 1)

                            // Set the title shown inside the Excel file
                    .setTitle(title)

                            // Set the subtitle shown inside the Excel file
                    .setSubtitle("Acest raport a fost generat: " + new Date())

                            // Set to true if you want to constrain your report within the page boundaries
                            // For longer reports, set it to false
                    .setUseFullPageWidth(true);

            // Set the name of the file

            // Build the report layout
            // Note this just the layout. It doesn't have any data yet!

            DynamicReport dr = drb.build();


            //TOTO inainte de a insera lista de verificat daca nu e nul si etc ....
            JRDataSource ds = new JRBeanCollectionDataSource(getReportValues());

            // 3. Compile the report layout
            JasperReport jr = DynamicJasperHelper.generateJasperReport(dr, new ClassicLayoutManager(), null);

            //TOTO la fel zona statica de modificat dinamic
            // 4. Generate the JasperPrint object which also fills the report with data
            JasperPrint jp = JasperFillManager.fillReport(jr, null, ds);


            if (PDF.equalsIgnoreCase(format)) {
                Exporter.exportToPdf(jp, baos);
            } else if (XLS.equalsIgnoreCase(format)) {

                Exporter.exportToXLS(jp, baos,new String[]{title});
            } else if (HTML.equalsIgnoreCase(format)) {
                Exporter.exportToHtml(jp, baos);
            }  else if(JPEG.equalsIgnoreCase(format)){
                Exporter.exportToJpg(jp, baos);
            }


            else {
                throw new FileFormatException("Paraetrul (Format) nu corespunde. Raportul poate fi exportat doar in" +
                        " XLS, PDF, HTML ");
            }


        } catch (ColumnBuilderException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();
        }


        return baos.toByteArray();

    }


    public List<?> getReportValues() {
        return values;
    }

    public void setReportsValues(List<?> values) {
        this.values = values;
    }





}
