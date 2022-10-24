/*
Q: Care este rolul tabelelor APP_CONTEXT si APP_COMPONENTA ? Consideram un Context un tip de utilizator ? Utilizator RAN, Utilizator UAT, Gospodar ?
A: APP_CONTEXT contine contextele (perspectivele in care poate lucra un utilizator). Vezi mai sus. Nu este chiar un tip de utilizator.
   De exemplu un utilizator de la UAT poate sa fie atat administrator cat si gospodar. Aici ar fi practic 2 perspective: administrator si gospodar.
   APP_COMPONENTE contine aplicatiile componente ale sistemului. Exemplu: Portal, App Introducere Date, Corelare Cadastrala.
*/

INSERT INTO APP_PARAMETRU(ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) VALUES(-1, '-1', 'debug.buildNumber', 'Show build number', 'true', 'false');

INSERT INTO APP_PARAMETRU(ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) VALUES(1, '1', 'url.login', 'Url login', '/portal/web/public/login', '/portal/web/public/login');
INSERT INTO APP_PARAMETRU(ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) VALUES(2, '2', 'url.logout', 'Url logout', '/portal/web/public/logout', '/portal/web/public/logout');
INSERT INTO APP_PARAMETRU(ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) VALUES(3, '3', 'url.context.GOSPODAR', 'Link aplicatie gospodar', '/portal/web/gospodar', '/portal/web/gospodar');
INSERT INTO APP_PARAMETRU(ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) VALUES(4, '4', 'url.context.UAT', 'Link aplicatie UAT', '/portal/web/uat', '/portal/web/uat');
INSERT INTO APP_PARAMETRU(ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) VALUES(5, '5', 'url.context.ANCPI', 'Link aplicatie ANCPI', '/portal/web/admin', '/portal/web/admin');
INSERT INTO APP_PARAMETRU(ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) VALUES(6, '6', 'url.context.INSTITUTIE', 'Link aplicatie ANCPI', '/portal/web/institutie', '/portal/web/institutie');

INSERT INTO APP_PARAMETRU (ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) values ('11','11','nomenclators.export.cluster.dir.path','Calea directorului radacina unde se va exporta nomenclatoarele.','D:/tmp/','/opt/');
INSERT INTO APP_PARAMETRU (ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) values ('12','12','nomenclators.store.local','Daca nomenclatoarele se stocheaza local sau nu. Boolean true = se stocheaza local','true','true');
INSERT INTO APP_PARAMETRU (ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) values ('13','13','nomenclators.export.path','Directorul in care se salveaza fisierele xml cu inregistrarile din nomenclatoare.','nomenclators','nomenclators');
INSERT INTO APP_PARAMETRU (ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) values ('14','14','nomenclators.summary.export.path','Directorul cu fisierele xml de sumarizare pentru nomenclatoare. Este localizat in cadrul directorului: nomenclators.export.path/','summary','summary');
INSERT INTO APP_PARAMETRU (ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) values ('15','15','nomenclators.zip.export.path','Directorul cu fisierele zip pentru nomenclatoare. Este localizat in cadrul directorului: nomenclators.export.path/','archive','archive');
INSERT INTO APP_PARAMETRU (ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) values ('10','10','ws.transmitereDate.async','tipul de transmisie bool true= async, false = sync','true','true');

INSERT INTO APP_PARAMETRU(ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) VALUES(101, '101', 'ws.parametru.wsdl', 'Parametru WS', 'http://ran-wasnd.uti.ro:9080/ran-core/service/internal/Parametru?wsdl', 'n/a');
INSERT INTO APP_PARAMETRU(ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) VALUES(102, '102', 'ws.sesiune.wsdl', 'Sesiune WS', 'http://ran-wasnd.uti.ro:9080/ran-core/service/internal/Sesiune?wsdl', 'n/a');
INSERT INTO APP_PARAMETRU(ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) VALUES(103, '103', 'ws.info-utilizator.wsdl', 'Info utilizator WS', 'http://ran-wasnd.uti.ro:9080/ran-core/service/internal/InfoUtilizator?wsdl', 'n/a');
INSERT INTO APP_PARAMETRU(ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) VALUES(104, '104', 'ws.admin-utilizator.wsdl', 'Admin utilizator WS', 'http://ran-wasnd.uti.ro:9080/ran-core/service/internal/AdminUtilizator?wsdl', 'n/a');
INSERT INTO APP_PARAMETRU(ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) VALUES(105, '105', 'ws.nomenclator.wsdl', 'Nomenclator WS', 'http://ran-wasnd.uti.ro:9080/ran-core/service/internal/Nomenclator?wsdl', 'n/a');
INSERT INTO APP_PARAMETRU(ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) VALUES(106, '106', 'ws.incarcare-date.wsdl', 'Incarcare Date WS', 'http://ran-wasnd.uti.ro:9080/ran-core/service/internal/IncarcareDate?wsdl', 'n/a');
INSERT INTO APP_PARAMETRU(ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) VALUES(107, '107', 'ws.sistem.wsdl', 'Sistem WS', 'http://ran-wasnd.uti.ro:9080/ran-core/service/internal/Sistem?wsdl', 'n/a');
INSERT INTO APP_PARAMETRU(ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) VALUES(108, '108', 'ws.notificari-sistem.wsdl', 'Notificari Sistem WS', 'http://ran-wasnd.uti.ro:9080/ran-core/service/internal/NotificariSistem?wsdl', 'n/a');
INSERT INTO APP_PARAMETRU(ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) VALUES(109, '109', 'ws.transmisii.wsdl', 'Transmisii WS', 'http://ran-wasnd.uti.ro:9080/ran-core/service/internal/Transmisii?wsdl', 'n/a');
INSERT INTO APP_PARAMETRU(ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) VALUES(110, '110', 'ws.intern.transmitere-date.wsdl', 'TransmitereDate Internal WS', 'http://ran-wasnd.uti.ro:9080/ran-core/service/internal/TransmitereDate?wsdl', 'n/a');
INSERT INTO APP_PARAMETRU(ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) VALUES(111, '111', 'ws.intern.interogare-date.wsdl', 'InterogareDate Internal WS', 'http://ran-wasnd.uti.ro:9080/ran-core/service/internal/InterogareDate?wsdl', 'n/a');
INSERT INTO APP_PARAMETRU(ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) VALUES(112, '112', 'ws.extern.transmitere-date.wsdl', 'TransmitereDate External WS', 'http://ran-wasnd.uti.ro:9080/ran-core/service/external/TransmitereDate?wsdl', 'n/a');
INSERT INTO APP_PARAMETRU(ID_APP_PARAMETRU,COD,DENUMIRE,DESCRIERE,VALOARE,VALOARE_IMPLICITA) VALUES(113, '113', 'ws.extern.interogare-date.wsdl', 'InterogareDate External WS', 'http://ran-wasnd.uti.ro:9080/ran-core/service/external/InterogareDate?wsdl', 'n/a');


