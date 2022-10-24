package ro.uti.ran.core.reporter.utils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.export.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by adrian.boldisor on 3/12/2016.
 */
public class Exporter {



    /**
     * Exports a report to XLS (Excel) format. You can declare another export here, i.e PDF or CSV.
     * You don't really need to create a separate class or method for the exporter. You can call it
     * directly within your Service or Controller.
     *
     * @param jp the JasperPrint object
     * @param baos the ByteArrayOutputStream where the report will be written
     */
    public static void exportToXLS(JasperPrint jp, ByteArrayOutputStream baos) throws JRException {
        exportToXLS(jp,baos,new String[]{});
    }
    public static void exportToPdf(JasperPrint jp, ByteArrayOutputStream baos) throws JRException {
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
        exporter.exportReport();
    }
    public static void exportToHtml(JasperPrint jp, ByteArrayOutputStream baos) throws JRException {
        JRHtmlExporter exporter = new JRHtmlExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
        exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, false);
        exporter.exportReport();
    }


    public static void exportToXLS(JasperPrint jp, ByteArrayOutputStream baos, String[] sheetsName ) throws JRException {

        // Create a JRXlsExporter instance
        JRXlsExporter exporter = new JRXlsExporter();

        // Here we assign the parameters jp and baos to the exporter
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

        // Excel specific parameters
        // Check the Jasper (not DynamicJasper) docs for a description of these settings. Most are
        // self-documenting
        exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
        exporter.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);

       if(sheetsName.length!=0){
           exporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, sheetsName);
       }



        // Retrieve the exported report in XLS format
        exporter.exportReport();

    }


    public static void exportToJpg(JasperPrint jp, ByteArrayOutputStream baos) throws JRException {

        BufferedImage rendered_image = null;
        rendered_image = (BufferedImage)JasperPrintManager.printPageToImage(jp,0,1.6f);
        try {
            ImageIO.write(rendered_image, "jpeg", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
