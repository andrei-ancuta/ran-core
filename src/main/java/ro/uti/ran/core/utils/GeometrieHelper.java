package ro.uti.ran.core.utils;

public class GeometrieHelper {


    public static String buildGmlPoint(String xCordinate, String yCordinate) {
        //<gml:Point srsName="EPSG:3844" xmlns:gml="http://www.opengis.net/gml"><gml:pos srsDimension="2">337751.345830827 476285.760315125 </gml:pos></gml:Point>
        StringBuilder geometrieGML = new StringBuilder();
        geometrieGML.append("<gml:Point srsName=\"EPSG:3844\" xmlns:gml=\"http://www.opengis.net/gml\">");
        geometrieGML.append("<gml:pos srsDimension=\"2\">").append(xCordinate).append(" ").append(yCordinate).append("</gml:pos>");
        geometrieGML.append("</gml:Point>");
        return geometrieGML.toString();
    }
}
