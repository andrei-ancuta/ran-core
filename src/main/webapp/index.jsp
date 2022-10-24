<%@ page import="org.apache.commons.io.Charsets" %>
<%@ page import="org.apache.commons.io.IOUtils" %>
<%@ page import="org.springframework.core.io.ClassPathResource" %>
<%@ page import="org.springframework.web.util.HtmlUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="org.springframework.core.io.support.PathMatchingResourcePatternResolver" %>
<%@ page import="org.springframework.core.io.Resource" %>
<html>
<head>
    <script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
    <title>RAN</title>
    <style>
        thead th, thead td {
            background-color: #ccc;
        }

        textarea {
            border: 1px solid #999999;
            width: 100%;
            margin: 5px 0;
            padding: 3px;
        }
    </style>
</head>
<body>
<table width="100%">
    <tr>
        <td width="80%">
            &nbsp;
        </td>
        <td width="20%">
            Version: ${project.version}, ${build.timestamp}
        </td>
    </tr>
</table>
<h1>RAN internal services</h1>

<table border="1">
    <thead>
    <tr>
        <td>Categorie</td>
        <td>Serviciu</td>
        <td>wsdl</td>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>Internal</td>
        <td>Sesiune</td>
        <td>
            <a href="<%=request.getContextPath()%>/service/internal/Sesiune?wsdl">
                /service/internal/Sesiune?wsdl
            </a>
        </td>
    </tr>
    <tr>
        <td>Internal</td>
        <td>Parametru</td>
        <td>
            <a href="<%=request.getContextPath()%>/service/internal/Parametru?wsdl">
                /service/internal/Parametru?wsdl
            </a>
        </td>
    </tr>
    <tr>
        <td>Internal</td>
        <td>System</td>
        <td>
            <a href="<%=request.getContextPath()%>/service/internal/Sistem?wsdl">
                /service/internal/Sistem?wsdl
            </a>
        </td>
    </tr>
    <tr>
        <td>Internal</td>
        <td>InfoUtilizator</td>
        <td>
            <a href="<%=request.getContextPath()%>/service/internal/InfoUtilizator?wsdl">
                /service/internal/InfoUtilizator?wsdl
            </a>
        </td>
    </tr>
    <tr>
        <td>Internal</td>
        <td>AdminUtilizator</td>
        <td>
            <a href="<%=request.getContextPath()%>/service/internal/AdminUtilizator?wsdl">
                /service/internal/AdminUtilizator?wsdl
            </a>
        </td>
    </tr>
    <tr>
        <td>Internal</td>
        <td>Nomenclator</td>
        <td>
            <a href="<%=request.getContextPath()%>/service/internal/Nomenclator?wsdl">
                /service/internal/Nomenclator?wsdl
            </a>
        </td>
    </tr>
    <tr>
        <td>Internal</td>
        <td>IncarcareDate</td>
        <td>
            <a href="<%=request.getContextPath()%>/service/internal/IncarcareDate?wsdl">
                /service/internal/IncarcareDate?wsdl
            </a>
        </td>
    </tr>
    <tr>
        <td>Internal</td>
        <td>NotificariSistem</td>
        <td>
            <a href="<%=request.getContextPath()%>/service/internal/NotificariSistem?wsdl">
                /service/internal/NotificariSistem?wsdl
            </a>
        </td>
    </tr>
    <tr>
        <td>Internal</td>
        <td>Transmisii</td>
        <td>
            <a href="<%=request.getContextPath()%>/service/internal/Transmisii?wsdl">
                /service/internal/Transmisii?wsdl
            </a>
        </td>
    </tr>
    <tr>
        <td>Internal</td>
        <td>TransmitereDate</td>
        <td>
            <a href="<%=request.getContextPath()%>/service/internal/TransmitereDate?wsdl">
                /service/internal/TransmitereDate?wsdl
            </a>
        </td>
    </tr>
    <tr>
        <td>Internal</td>
        <td>InterogareDate</td>
        <td>
            <a href="<%=request.getContextPath()%>/service/internal/InterogareDate?wsdl">
                /service/internal/InterogareDate?wsdl
            </a>
        </td>
    </tr>
    <tr>
        <td>Internal</td>
        <td>InventarGospUat</td>
        <td>
            <a href="<%=request.getContextPath()%>/service/internal/InventarGospUat?wsdl">
                /service/internal/InventarGospUat?wsdl
            </a>
        </td>
    </tr>
    <tr>
        <td>Internal</td>
        <td>InterogareDateCentralizatoare</td>
        <td>
            <a href="<%=request.getContextPath()%>/service/internal/InterogareDateCentralizatoare?wsdl">
                /service/internal/InterogareDateCentralizatoare?wsdl
            </a>
        </td>
    </tr>
    </tbody>
</table>


<h1>RAN external services</h1>

<table border="1">
    <thead>
    <tr>
        <td>Categorie</td>
        <td>Serviciu</td>
        <td>wsdl</td>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>External</td>
        <td>TransmitereDate</td>
        <td>
            <a href="<%=request.getContextPath()%>/service/external/TransmitereDate?wsdl">
                /service/external/TransmitereDate?wsdl
            </a>
        </td>
    </tr>
    <tr>
        <td>External</td>
        <td>InterogareDate</td>
        <td>
            <a href="<%=request.getContextPath()%>/service/external/InterogareDate?wsdl">
                /service/external/InterogareDate?wsdl
            </a>
        </td>
    </tr>
    <tr>
        <td>External</td>
        <td>InterogareDateCentralizatoare</td>
        <td>
            <a href="<%=request.getContextPath()%>/service/external/InterogareDateCentralizatoare?wsdl">
                /service/external/InterogareDateCentralizatoare?wsdl
            </a>
        </td>
    </tr>
    <tr>
        <td>External</td>
        <td>Nomenclatoare</td>
        <td>
            <a href="<%=request.getContextPath()%>/service/external/Nomenclatoare?wsdl">
                /service/external/Nomenclatoare?wsdl
            </a>
        </td>
    </tr>
    </tbody>

</table>

<h1>FTP (descarcare rapoarte centralizatoare (codSiruta/codLicenta): 1234/111111111111111111111)</h1>
<div>
    Status ftp:<span id="ftpStatus"></span>
    <form action="<%=request.getContextPath()%>/ftpInfo" method="post">
        <input type="submit" value="Restart server">
    </form>
</div>
<script>
    document.addEventListener("DOMContentLoaded", function (event) {
        var loadDoc = function () {
            var xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function () {
                if (xhttp.readyState == 4 && xhttp.status == 200) {
                    var jsonResp = JSON.parse(xhttp.responseText);
                    document.getElementById("ftpStatus").innerHTML = jsonResp.status;
                }
            };
            var currentUrl = location.href;
            var reqUrl = currentUrl;
            if (currentUrl[currentUrl.length - 1] === "/") {
                reqUrl += "ftpInfo";
            } else {
                reqUrl += "/ftpInfo";
            }
            xhttp.open("GET", reqUrl, true);
            xhttp.send();
        };
        setInterval(loadDoc, 20 * 1000);//20 de secunde
    });
</script>
<a href="ftp://172.17.10.58:2221/">ftp://172.20.11.58:2221/</a>


<h1>HTTP (descarcare rapoarte centralizatoare)</h1>

<table border="1">
    <thead>
    <tr>
        <td>Categorie</td>
        <td>Link</td>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>Pagina de descarcare rapoarte</td>
        <td>
            <a href="<%=request.getContextPath()%>/rapoarte">
                Acceseaza
            </a>
        </td>
    </tr>
    <tr>
        <td>xsd Schema</td>
        <td>
            <a href="<%=request.getContextPath()%>/rapoarte?xsd">
                Acceseaza
            </a>
        </td>
    </tr>
    <tr>
        <td>xml Continut rapoarte</td>
        <td>
            <a href="<%=request.getContextPath()%>/rapoarte?xml">
                Acceseaza
            </a>
        </td>
    </tr>
    </tbody>
</table>


<h1>Samples: Capitole gospodarie</h1>
<table border="1">
    <%
        PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourceResolver.getResources("classpath:samples/v3/gospodarie/*.xml");

        for (Resource resource : resources) {
            String data = HtmlUtils.htmlEscape(IOUtils.toString(resource.getInputStream()), "UTF-8");
    %>
    <tr>
        <td>
            <%=resource.getFilename()%>
        </td>
        <td>
            <pre class="prettyprint">
                <%=data%>
            </pre>
        </td>
    </tr>
    <%
        }
    %>

</table>

<h1>Samples: Capitole centralizatoare UAT</h1>
<table border="1">
    <%
        resources = resourceResolver.getResources("classpath:samples/v3/centralizatoare_uat/*.xml");

        for (Resource resource : resources) {
            String data = HtmlUtils.htmlEscape(IOUtils.toString(resource.getInputStream()), "UTF-8");
    %>
    <tr>
        <td>
            <%=resource.getFilename()%>
        </td>
        <td>
            <pre class="prettyprint">
                <%=data%>
            </pre>
        </td>
    </tr>
    <%
        }
    %>

</table>

<h1>RAN: XSD Schema</h1>

<div style="display: block;" id="rulesformitem" class="formitem">
    <p>Copy&Paste to the following URL: <a target="_blank" href="http://view.xmlgrid.net/">http://view.xmlgrid.net/</a>
        to see graphical representation</p>
    <textarea cols="2" rows="30">
        <%=new String(IOUtils.toByteArray(new ClassPathResource("schema1.xsd").getInputStream()), "UTF-8")%>
    </textarea>
</div>


</body>
</html>
