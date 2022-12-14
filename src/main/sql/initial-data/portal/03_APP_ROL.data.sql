-- ------------------------- --
-- Roluri context PORTAL     --
-- ------------------------- --

--
-- Roluri context GOSPODAR
--
INSERT INTO APP_ROL(
ID_APP_ROL, IS_ACTIV, COD, DENUMIRE, DESCRIERE, FK_APP_COMPONENTA, FK_APP_CONTEXT
) VALUES(
    1, 1, 'GOSPODAR', 'Gospodar', '',
    (SELECT ID_APP_COMPONENTA FROM APP_COMPONENTA WHERE COD = 'PORTAL'),
    (SELECT ID_APP_CONTEXT FROM APP_CONTEXT WHERE COD = 'GOSPODAR')
);

--
-- Roluri context UAT
--
INSERT INTO APP_ROL(
ID_APP_ROL, IS_ACTIV, COD, DENUMIRE, DESCRIERE, FK_APP_COMPONENTA, FK_APP_CONTEXT
) VALUES(
    2, 1, 'ADM-GOSP', 'Gestionar conturi gospodari', '',
    (SELECT ID_APP_COMPONENTA FROM APP_COMPONENTA WHERE COD = 'PORTAL'),
    (SELECT ID_APP_CONTEXT FROM APP_CONTEXT WHERE COD = 'UAT')
);

INSERT INTO APP_ROL(
ID_APP_ROL, IS_ACTIV, COD, DENUMIRE, DESCRIERE, FK_APP_COMPONENTA, FK_APP_CONTEXT
) VALUES(
    3, 1, 'ADM-UAT', 'Administrator UAT', '',
    (SELECT ID_APP_COMPONENTA FROM APP_COMPONENTA WHERE COD = 'PORTAL'),
    (SELECT ID_APP_CONTEXT FROM APP_CONTEXT WHERE COD = 'UAT')
);

INSERT INTO APP_ROL(
ID_APP_ROL, IS_ACTIV, COD, DENUMIRE, DESCRIERE, FK_APP_COMPONENTA, FK_APP_CONTEXT
) VALUES(
    4, 1, 'ADM-UAT-J', 'Administrator UAT Judetean', '',
    (SELECT ID_APP_COMPONENTA FROM APP_COMPONENTA WHERE COD = 'PORTAL'),
    (SELECT ID_APP_CONTEXT FROM APP_CONTEXT WHERE COD = 'UAT')
);


--
-- Roluri context ANCPI
--
INSERT INTO APP_ROL(
ID_APP_ROL, IS_ACTIV, COD, DENUMIRE, DESCRIERE, FK_APP_COMPONENTA, FK_APP_CONTEXT
) VALUES(
    5, 1, 'ADM-USR', 'Gestionar utilizatori RAN', '',
    (SELECT ID_APP_COMPONENTA FROM APP_COMPONENTA WHERE COD = 'PORTAL'),
    (SELECT ID_APP_CONTEXT FROM APP_CONTEXT WHERE COD = 'ANCPI')
);

INSERT INTO APP_ROL(
ID_APP_ROL, IS_ACTIV, COD, DENUMIRE, DESCRIERE, FK_APP_COMPONENTA, FK_APP_CONTEXT
) VALUES(
    6, 1, 'OP-INST', 'Operator Institutie', '',
    (SELECT ID_APP_COMPONENTA FROM APP_COMPONENTA WHERE COD = 'PORTAL'),
    (SELECT ID_APP_CONTEXT FROM APP_CONTEXT WHERE COD = 'INSTITUTIE')
);


INSERT INTO APP_ROL(
ID_APP_ROL, IS_ACTIV, COD, DENUMIRE, DESCRIERE, FK_APP_COMPONENTA, FK_APP_CONTEXT
) VALUES(
    7, 1, 'OP-UAT', 'Operator registru agricol primarie', '',
    (SELECT ID_APP_COMPONENTA FROM APP_COMPONENTA WHERE COD = 'PORTAL'),
    (SELECT ID_APP_CONTEXT FROM APP_CONTEXT WHERE COD = 'UAT')
);

INSERT INTO APP_ROL(
ID_APP_ROL, IS_ACTIV, COD, DENUMIRE, DESCRIERE, FK_APP_COMPONENTA, FK_APP_CONTEXT
) VALUES(
    8, 1, 'OP-RAN', 'Operator RAN', '',
    (SELECT ID_APP_COMPONENTA FROM APP_COMPONENTA WHERE COD = 'PORTAL'),
    (SELECT ID_APP_CONTEXT FROM APP_CONTEXT WHERE COD = 'ANCPI')
);

INSERT INTO APP_ROL(
ID_APP_ROL, IS_ACTIV, COD, DENUMIRE, DESCRIERE, FK_APP_COMPONENTA, FK_APP_CONTEXT
) VALUES(
    9, 1, 'ADM-NOM', 'Gestionar Nomenclatoare', '',
    (SELECT ID_APP_COMPONENTA FROM APP_COMPONENTA WHERE COD = 'PORTAL'),
    (SELECT ID_APP_CONTEXT FROM APP_CONTEXT WHERE COD = 'ANCPI')
);
