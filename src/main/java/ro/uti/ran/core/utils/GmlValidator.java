package ro.uti.ran.core.utils;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.xml.XmlMapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by miroslav.rusnac on 21/07/2016.
 */

public class GmlValidator {

    public static String ValidateGeometry(String uatLayerUrl, int sirutaUAT, String xmlGeo) {


        boolean isIn = false;
        String USER_AGENT = "Mozilla/5.0";
        String retValue = "";

        try {

            XmlMapper mapper1 = new XmlMapper();

            Map<String, Object> fromGmlXml = mapper1.readValue(xmlGeo, Map.class);

            String[] points;

            HashMap pos = (HashMap) fromGmlXml.get("pos");
            if (null == pos) {
                // cazul unui poligon
                HashMap exterior = (HashMap) fromGmlXml.get("exterior");
                if (null == exterior) {
                    throw new IllegalArgumentException("TAG <exterior> lipseste. Geometria trebuie sa fie un poligon.");
                }
                HashMap linearRing = (HashMap) exterior.get("LinearRing");
                if (null == linearRing) {
                    throw new IllegalArgumentException("TAG <LinearRing> lipseste. Geometria trebuie sa fie un poligon.");
                }
                HashMap posList = (HashMap) linearRing.get("posList");
                if (null == posList) {
                    throw new IllegalArgumentException("TAG <posList> lipseste. Geometria trebuie sa fie un poligon.");
                }
                points = ((String) posList.get("")).trim().split(" ");
            } else {
                points = ((String) pos.get("")).trim().split(" ");
            }

            Double[][] xyPoints = new Double[points.length / 2][2];

            int index = 0;
            for (int i = 0; i < points.length; i++) {
                xyPoints[index][0] = Double.parseDouble(points[i]);
                i++;
                xyPoints[index][1] = Double.parseDouble(points[i]);
                index++;
            }


            String url;
            if (null == uatLayerUrl) {
                Properties prop = new Properties();
                InputStream inputStream =
                        GmlValidator.class.getClassLoader().getResourceAsStream("gisserver.properties");
                prop.load(inputStream);

                url = prop.getProperty("url.uat").replace("{sirutaUAT}", Integer.toString(sirutaUAT));
            } else {
                url = uatLayerUrl.replace("{sirutaUAT}", Integer.toString(sirutaUAT));
                ;
            }


            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            ObjectMapper mapper = new ObjectMapper();

            // read JSON from a file
            Map<String, Object> mapUat = mapper.readValue(
                    response.toString(),
                    new TypeReference<Map<String, Object>>() {
                    });

            ArrayList features = (ArrayList) mapUat.get("features");

            HashMap features0 = (HashMap) features.get(0);

            HashMap geometry = (HashMap) features0.get("geometry");

            ArrayList rings = (ArrayList) geometry.get("rings");

            ArrayList coordUat = (ArrayList) rings.get(0);


            if (coordUat.size() <= 1) {
                isIn = false;
            } else {
                for (int checkP = 0; checkP < (xyPoints.length == 1 ? 1 : xyPoints.length - 1); checkP++) {
                    int intersections = 0;
                    int prev = coordUat.size() - 2;
                    boolean prevUnder = ((Number) ((ArrayList) coordUat.get(prev)).get(1)).doubleValue() < xyPoints[checkP][1];

                    for (int repeatUat = 0; repeatUat < coordUat.size() - 1; repeatUat++) {
                        boolean curUnder = (((Number) ((ArrayList) coordUat.get(repeatUat)).get(1)).doubleValue() <= xyPoints[checkP][1]);
                        boolean notCorner = !(((Number) ((ArrayList) coordUat.get(repeatUat)).get(1)).doubleValue() == xyPoints[checkP][1]);
                        Double[][] a = new Double[1][2];
                        Double[][] b = new Double[1][2];
                        a[0][0] = ((Number) ((ArrayList) coordUat.get(prev)).get(0)).doubleValue() - xyPoints[checkP][0];
                        a[0][1] = ((Number) ((ArrayList) coordUat.get(prev)).get(1)).doubleValue() - xyPoints[checkP][1];

                        b[0][0] = ((Number) ((ArrayList) coordUat.get(repeatUat)).get(0)).doubleValue() - xyPoints[checkP][0];
                        b[0][1] = ((Number) ((ArrayList) coordUat.get(repeatUat)).get(1)).doubleValue() - xyPoints[checkP][1];

                        Double t = (a[0][0] * (b[0][1] - a[0][1]) - a[0][1] * (b[0][0] - a[0][0]));
                        if (curUnder && !prevUnder) {
                            if (t >= 0)
                                intersections += 1;
                        }
                        if (!curUnder && prevUnder) {
                            if (t < 0)
                                intersections += 1;
                        }

                        prev = repeatUat;
                        prevUnder = (curUnder && notCorner);
                    }

                    isIn = (intersections & 1) != 0;
                    if (!isIn) {
                        break;
                    }


                }

            }

            retValue = isIn ? "Ok" : "Geometria aplicata nu se incadreaza in limita UAT cod siruta " + Integer.toString(sirutaUAT);

        } catch (Throwable e) {
            e.printStackTrace();
            retValue = e.getMessage();
        }


        return retValue;
    }
}
