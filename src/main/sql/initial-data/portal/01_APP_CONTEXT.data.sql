/*
Q: Care este rolul tabelelor APP_CONTEXT si APP_COMPONENTA ? Consideram un Context un tip de utilizator ? Utilizator RAN, Utilizator UAT, Gospodar ?
A: APP_CONTEXT contine contextele (perspectivele in care poate lucra un utilizator). Vezi mai sus. Nu este chiar un tip de utilizator.
   De exemplu un utilizator de la UAT poate sa fie atat administrator cat si gospodar. Aici ar fi practic 2 perspective: administrator si gospodar.
   APP_COMPONENTE contine aplicatiile componente ale sistemului. Exemplu: Portal, App Introducere Date, Corelare Cadastrala.
*/
INSERT INTO APP_CONTEXT(ID_APP_CONTEXT,COD,DENUMIRE,DESCRIERE) VALUES(1, 'GOSPODAR', 'Gospodar', 'Context Gospodar');
INSERT INTO APP_CONTEXT(ID_APP_CONTEXT,COD,DENUMIRE,DESCRIERE) VALUES(2, 'UAT', 'UAT', 'Context UAT');
INSERT INTO APP_CONTEXT(ID_APP_CONTEXT,COD,DENUMIRE,DESCRIERE) VALUES(3, 'ANCPI', 'ANCPI', 'Context ANCPI');
INSERT INTO APP_CONTEXT(ID_APP_CONTEXT,COD,DENUMIRE,DESCRIERE) VALUES(4, 'INSTITUTIE', 'INSTITUTIE', 'Context INSTITUTIE');