1. SDO_GTYPE
The SDO_GTYPE value is 4 digits in the format DLTT, where:
- D identifies the number of dimensions (2, 3, or 4)
- DL01 POINT Geometry contains one point.
- DL03 POLYGON or SURFACE Geometry contains one polygon with or without holes,Foot 1  or one surface consisting of one or more polygons.
In a three-dimensional polygon, all points must be on the same plane.
- For example, an SDO_GTYPE value of 2003 indicates a two-dimensional polygon












SELECT * FROM USER_SDO_GEOM_METADATA;

SELECT META.TABLE_NAME, META.COLUMN_NAME, META.SRID, DIM.*
FROM USER_SDO_GEOM_METADATA META, TABLE(META.DIMINFO) DIM;


SELECT SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT(tbl.GEOMETRIE,(SELECT META.DIMINFO FROM USER_SDO_GEOM_METADATA META WHERE META.TABLE_NAME = 'GEOMETRIE_PARCELA_TEREN'))
FROM GEOLOCATOR_ADRESA tbl WHERE tbl.ID_GEOLOCATOR_ADRESA = 5006;


SELECT tbl.GEOMETRIE.Get_Dims(),tbl.GEOMETRIE.Get_GType(),SDO_UTIL.TO_GML311GEOMETRY(tbl.GEOMETRIE)
FROM GEOLOCATOR_ADRESA tbl;

SELECT tbl.GEOMETRIE.Get_Dims(),tbl.GEOMETRIE.Get_GType(),tbl.GEOMETRIE.SDO_SRID ,SDO_UTIL.TO_GML311GEOMETRY(tbl.GEOMETRIE)
FROM GEOLOCATOR_ADRESA tbl;

SELECT PKG_GEOMETRY_VALIDARE.f_validare_limita_uat(
 '<gml:Polygon srsName="EPSG:3844" xmlns:gml="http://www.opengis.net/gml"><gml:exterior><gml:LinearRing><gml:posList srsDimension="2">119.593002319336 -31.6695003509522 119.595306396484 31.6650276184082 119.600944519043 -31.6658897399902 119.603385925293 -31.669527053833 119.60050201416 -31.6739158630371 119.595664978027 -31.6728610992432 119.593002319336 31.6695003509522 </gml:posList></gml:LinearRing></gml:exterior></gml:Polygon>',
 89561,
 3,
 3844)
FROM DUAL;



<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:core="core.ran.uti.ro" xmlns:tran="http://transmitere.internal.ws.core.ran.uti.ro/">
   <soapenv:Header>
      <core:ranAuthorization>
         <!--Optional:-->
         <context>UAT</context>
         <!--Optional:-->
         <idEntity>1511</idEntity>
      </core:ranAuthorization>
   </soapenv:Header>
   <soapenv:Body>
      <tran:transmitere>
         <modalitateTransmitere>MANUAL</modalitateTransmitere>
         <xmlCDATA><![CDATA[<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<DOCUMENT_RAN>
    <HEADER>
         <codXml value="${=java.util.UUID.randomUUID()}"/>
        <dataExport>2016-12-20T16:36:54.143+02:00</dataExport>
        <indicativ>ADAUGA_SI_INLOCUIESTE</indicativ>
        <sirutaUAT>89561</sirutaUAT>
    </HEADER>
    <BODY>
        <gospodarie identificator="670000B">
            <capitol_0_12 codCapitol="CAP0_12" denumire="Date de identificare a gospodăriei deținute de persoană fizică">
                <date_identificare_gospodarie_PF>
                    <adresaGospodarie>
                        <numar>16-18</numar>
                        <sirutaJudet>207</sirutaJudet>
                        <sirutaLocalitate>89570</sirutaLocalitate>
                        <sirutaUAT>89561</sirutaUAT>
                        <strada>principala</strada>
                        <referintaGeoXml><![CDATA[<gml:Point srsName="EPSG:3844" xmlns:gml="http://www.opengis.net/gml"><gml:pos srsDimension="2">361444.0 495489.0 </gml:pos></gml:Point>]]]]>><![CDATA[</referintaGeoXml>
                    </adresaGospodarie>
                    <domiciliuFiscalRo>
                        <numar>16-18</numar>
                        <sirutaJudet>207</sirutaJudet>
                        <sirutaLocalitate>89570</sirutaLocalitate>
                        <sirutaUAT>89561</sirutaUAT>
                        <strada>principala</strada>
                    </domiciliuFiscalRo>
                    <nrUnicIdentificare>657000001</nrUnicIdentificare>
                    <pozitieGospodarie>
                        <pozitiaAnterioara>3</pozitiaAnterioara>
                        <pozitieCurenta>3</pozitieCurenta>
                        <volumul>62</volumul>
                        <rolNominalUnic>32123</rolNominalUnic>
                    </pozitieGospodarie>
                    <tipDetinator>1</tipDetinator>
                    <tipExploatatie>2</tipExploatatie>
                    <gospodar>
                        <nume>GARABAJIU</nume>
                        <prenume>GRIGORE</prenume>
                        <initialaTata>M</initialaTata>
                        <cnp value="1650202110137"/>
                    </gospodar>
                    <elementeJuridice>
                        <cui value="6859662"/>
                        <cif value="RO6859662"/>
                        <denumire>EXEMPLU COM SRL</denumire>
                        <formaOrganizareRC>IF</formaOrganizareRC>
                    </elementeJuridice>
                </date_identificare_gospodarie_PF>
            </capitol_0_12>
        </gospodarie>
    </BODY>
</DOCUMENT_RAN>]]></xmlCDATA>
      </tran:transmitere>
   </soapenv:Body>
</soapenv:Envelope>
