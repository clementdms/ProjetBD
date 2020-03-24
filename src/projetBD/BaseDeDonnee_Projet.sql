-- Base de donnée Projet BD
-- Auteur : De Lemos Almeida Pierre, Dumas Clément, Meziane Farid, Diallo Mohamed Saliou
DROP TABLE IF EXISTS RESERVE;
DROP TABLE IF EXISTS PLACE;
DROP TABLE IF EXISTS CLASSE;
DROP TABLE IF EXISTS ZONE;
DROP TABLE IF EXISTS AFFECTER;
DROP TABLE IF EXISTS PILOTER;
DROP TABLE IF EXISTS VOL;
DROP TABLE IF EXISTS RESERVATION;
DROP TABLE IF EXISTS CLIENT;
DROP TABLE IF EXISTS AVION;
DROP TABLE IF EXISTS QUALIFIER;
DROP TABLE IF EXISTS MODELE_AVION;
DROP TABLE IF EXISTS PILOTE;
DROP TABLE IF EXISTS PARLER;
DROP TABLE IF EXISTS LANGUE;
DROP TABLE IF EXISTS HOTESSE;
DROP TABLE IF EXISTS VILLE;
DROP TABLE IF EXISTS PAYS;


CREATE TABLE PAYS
(
    id_pays INT,
    nom_pays VARCHAR(100),
    CONSTRAINT pk_pays PRIMARY KEY (id_pays)
);
-- La table VILLE recupère les attributs de l'association ETRE car il y a les cardinalité 1,1 et 0,N donc on ne créée pas de table ETRE
CREATE TABLE VILLE
(
    id_ville INT,
    nom_ville VARCHAR(100),
    id_pays INT NOT NULL,
    CONSTRAINT pk_ville PRIMARY KEY (id_ville),
    CONSTRAINT fk_ville_pays FOREIGN KEY (id_pays) REFERENCES PAYS(id_pays)
);

CREATE TABLE HOTESSE
(
    id_hotesse INT,
    nom_hotesse VARCHAR(100),
    prenom_hotesse VARCHAR(100),
    numeroRue_hotesse INT,
    rue_hotesse VARCHAR(100),
    code_postal_hotesse INT,
    ville_hotesse VARCHAR(100),
    pays_hotesse VARCHAR(100),
    CONSTRAINT pk_hotesse PRIMARY KEY (id_hotesse)    
);

CREATE TABLE LANGUE 
(
    id_langue INT,
    nom_langue VARCHAR(100),
    CONSTRAINT pk_langue PRIMARY KEY (id_langue)
);

CREATE TABLE PARLER
(
    id_langue INT NOT NULL,
    id_hotesse INT NOT NULL,
    CONSTRAINT pk_parler PRIMARY KEY (id_langue,id_hotesse),
    CONSTRAINT fk_parler_langue FOREIGN KEY (id_langue) REFERENCES LANGUE(id_langue),
    CONSTRAINT fk_parler_hotesse FOREIGN KEY (id_hotesse) REFERENCES HOTESSE(id_hotesse)
);

CREATE TABLE PILOTE
(
    id_pilote INT,
    nom_pilote VARCHAR(100),
    prenom_pilote VARCHAR(100),
    numeroRue_pilote INT,
    rue_pilote VARCHAR(100),
    code_postal_pilote INT,
    ville_pilote VARCHAR(100),
    pays_pilote VARCHAR(100),
    CONSTRAINT pk_pilote PRIMARY KEY (id_pilote)
);

CREATE TABLE MODELE_AVION
(
    code_modele_avion VARCHAR(20),
    distancemax_avion DECIMAL(10,2) NOT NULL,
    nbpilote_avion INT NOT NULL,
    CONSTRAINT pk_modele_avion PRIMARY KEY (code_modele_avion)
);

CREATE TABLE QUALIFIER
(
    id_pilote INT,
    code_modele_avion VARCHAR(20) NOT NULL,
    CONSTRAINT pk_qualifier PRIMARY KEY (id_pilote,code_modele_avion),
    CONSTRAINT fk_qualifier_pilote FOREIGN KEY (id_pilote) REFERENCES PILOTE(id_pilote),
    CONSTRAINT fk_qualifier_modele_avion FOREIGN KEY (code_modele_avion) REFERENCES MODELE_AVION(code_modele_avion)
);

CREATE TABLE AVION
(
    numero_avion INT,
    code_modele_avion VARCHAR(20) NOT NULL,
    CONSTRAINT pk_avion PRIMARY KEY (numero_avion),
    CONSTRAINT fk_modele_avion FOREIGN KEY (code_modele_avion) REFERENCES MODELE_AVION(code_modele_avion)
);

CREATE TABLE CLIENT
(
    id_client INT,
    nom_client VARCHAR(100),
    prenom_client VARCHAR(100),
    numeroRue_client INT,
    rue_client VARCHAR(100),
    code_postal_client INT,
    ville_client VARCHAR(100),
    pays_client VARCHAR(100),
    numeropassport_client VARCHAR(100),
    CONSTRAINT pk_client PRIMARY KEY (id_client)
);

CREATE TABLE RESERVATION
(
    id_reservation INT,
    date_reservation DATE,
    CONSTRAINT pk_reservation PRIMARY KEY (id_reservation)
);

CREATE TABLE VOL
(
    numero_vol INT,
    horaire_vol TIME,
    date_vol DATE,
    duree_vol TIME,
    distance_vol DECIMAL(10,2),
    -- etat est soit att/vol/arrive/suppr
    etat_vol VARCHAR(5),
    id_ville_provenir INT NOT NULL,
    id_ville_destiner INT NOT NULL,
    numero_avion INT NOT NULL,
    CONSTRAINT pk_vol PRIMARY KEY (numero_vol),
    CONSTRAINT fk_vol_ville_provenir FOREIGN KEY (id_ville_provenir) REFERENCES VILLE(id_ville),
    CONSTRAINT fk_vol_ville_destiner FOREIGN KEY (id_ville_destiner) REFERENCES VILLE(id_ville),
    CONSTRAINT fk_vol_avion FOREIGN KEY (numero_avion) REFERENCES AVION(numero_avion)
);

CREATE TABLE PILOTER
(
    id_pilote INT,
    numero_vol INT,
    CONSTRAINT pk_piloter PRIMARY KEY (id_pilote, numero_vol),
    CONSTRAINT fk_piloter_pilote FOREIGN KEY (id_pilote) REFERENCES PILOTE(id_pilote),
    CONSTRAINT fk_piloter_vol FOREIGN KEY (numero_vol) REFERENCES VOL(numero_vol)
);

CREATE TABLE AFFECTER
(
    id_hotesse INT,
    numero_vol INT,
    CONSTRAINT pk_affecter PRIMARY KEY (id_hotesse, numero_vol),
    CONSTRAINT fk_affecter_hotesse FOREIGN KEY (id_hotesse) REFERENCES HOTESSE(id_hotesse),
    CONSTRAINT fk_affecter_vol FOREIGN KEY (numero_vol) REFERENCES VOL(numero_vol)
);

CREATE TABLE ZONE 
(
    id_zone INT,
    nom_zone VARCHAR(100),
    CONSTRAINT pk_zone PRIMARY KEY (id_zone)
);

CREATE TABLE CLASSE 
(
    id_classe INT,
    nom_classe VARCHAR(100),
    CONSTRAINT pk_classe PRIMARY KEY (id_classe)
);
-- La table place a comme clé primaire numero_avion et numero_place car la place n° 1 peut etre dans plusieurs avions differentes
CREATE TABLE PLACE 
(
    numero_place INT,
    numero_avion INT,
    id_zone INT NOT NULL,
    id_classe INT NOT NULL,
    CONSTRAINT pk_place PRIMARY KEY (numero_place, numero_avion),
    CONSTRAINT fk_place_avion FOREIGN KEY (numero_avion) REFERENCES AVION(numero_avion),
    CONSTRAINT fk_place_zone FOREIGN KEY (id_zone) REFERENCES ZONE(id_zone),
    CONSTRAINT fk_place_classe FOREIGN KEY (id_classe) REFERENCES CLASSE(id_classe)
);

CREATE TABLE RESERVE
(
    numero_place INT,
    numero_vol INT,
    id_reservation INT,
    prixplace_reserve DECIMAL(10,2),
    CONSTRAINT pk_reserve PRIMARY KEY (numero_place, numero_vol, id_reservation),
    CONSTRAINT fk_reserve_place FOREIGN KEY (numero_place) REFERENCES PLACE(numero_place),
    CONSTRAINT fk_reserve_vol FOREIGN KEY (numero_vol) REFERENCES VOL(numero_vol),
    CONSTRAINT fk_reserve_reservation FOREIGN KEY (id_reservation) REFERENCES RESERVATION(id_reservation)
);


-- On insere les données pour Pays
INSERT INTO PAYS (id_pays, nom_pays) VALUES ('1', 'France');

-- On insere les données pour Ville
INSERT INTO VILLE (id_ville, nom_ville, id_pays) VALUES ('1', 'Grenoble', '1');
INSERT INTO VILLE (id_ville, nom_ville, id_pays) VALUES ('2', 'Lyon', '1');
INSERT INTO VILLE (id_ville, nom_ville, id_pays) VALUES ('3', 'Marseille', '1');
INSERT INTO VILLE (id_ville, nom_ville, id_pays) VALUES ('4', 'Paris', '1');
INSERT INTO VILLE (id_ville, nom_ville, id_pays) VALUES ('5', 'Nantes', '1');
INSERT INTO VILLE (id_ville, nom_ville, id_pays) VALUES ('6', 'Nimes', '1');
INSERT INTO VILLE (id_ville, nom_ville, id_pays) VALUES ('7', 'Strasbourg', '1');
INSERT INTO VILLE (id_ville, nom_ville, id_pays) VALUES ('8', 'Bordeaux', '1');
INSERT INTO VILLE (id_ville, nom_ville, id_pays) VALUES ('9', 'Perpignan', '1');
INSERT INTO VILLE (id_ville, nom_ville, id_pays) VALUES ('10', 'Rennes', '1');

-- On insere les données pour Hotesse
INSERT INTO HOTESSE (id_hotesse, nom_hotesse, prenom_hotesse, numeroRue_hotesse, rue_hotesse, code_postal_hotesse, ville_hotesse, pays_hotesse) VALUES ('1', 'Kardashian', 'Kim', '15', 'Avenue des champs Elysées', '75000', 'Paris','France');
INSERT INTO HOTESSE (id_hotesse, nom_hotesse, prenom_hotesse, numeroRue_hotesse, rue_hotesse, code_postal_hotesse, ville_hotesse, pays_hotesse) VALUES ('2', 'Collomb', 'Estelle', '24', 'Place de Gordes', '38000', 'Grenoble','France');

-- On insere les données pour Langue
INSERT INTO LANGUE (id_langue, nom_langue) VALUES ('1', 'Francais');
INSERT INTO LANGUE (id_langue, nom_langue) VALUES ('2', 'Espagnol');
INSERT INTO LANGUE (id_langue, nom_langue) VALUES ('3', 'Anglais');

-- On insere les données pour Parler
INSERT INTO PARLER (id_langue, id_hotesse) VALUES ('1', '1');
INSERT INTO PARLER (id_langue, id_hotesse) VALUES ('3','1');
INSERT INTO PARLER (id_langue, id_hotesse) VALUES ('1','2');
INSERT INTO PARLER (id_langue, id_hotesse) VALUES ('2','2');
INSERT INTO PARLER (id_langue, id_hotesse) VALUES ('3','2');

-- On insere les données pour Pilote
INSERT INTO PILOTE (id_pilote, nom_pilote, prenom_pilote, numeroRue_pilote, rue_pilote, code_postal_pilote, ville_pilote, pays_pilote) VALUES ('1', 'Dumas', 'Clément', '16', 'Rue Hector Berlioz','38420','Domène','France');
INSERT INTO PILOTE (id_pilote, nom_pilote, prenom_pilote, numeroRue_pilote, rue_pilote, code_postal_pilote, ville_pilote, pays_pilote) VALUES ('2', 'Meziane', 'Farid', '110', 'Rue de la victoire de l Algérie','38000','Fontaine','France');
INSERT INTO PILOTE (id_pilote, nom_pilote, prenom_pilote, numeroRue_pilote, rue_pilote, code_postal_pilote, ville_pilote, pays_pilote) VALUES ('3', 'De Lemos', 'Pierre', '15', 'Route de l eglise','38000','Grenble','France');
INSERT INTO PILOTE (id_pilote, nom_pilote, prenom_pilote, numeroRue_pilote, rue_pilote, code_postal_pilote, ville_pilote, pays_pilote) VALUES ('4', 'Diallo','Mohammed','35', 'Chemin de la Guinée','38400','Echirolles','France');

-- On insere les données pour Modèle Avion
INSERT INTO MODELE_AVION (code_modele_avion, distancemax_avion, nbpilote_avion) VALUES ('A380', '25.5', '2');
INSERT INTO MODELE_AVION (code_modele_avion, distancemax_avion, nbpilote_avion) VALUES ('A320', '35', '3');
INSERT INTO MODELE_AVION (code_modele_avion, distancemax_avion, nbpilote_avion) VALUES ('MIAGE2020', '500', '1');

-- On insere les données pour Qualifier
INSERT INTO QUALIFIER (id_pilote, code_modele_avion) VALUES ('1', 'A380');
INSERT INTO QUALIFIER (id_pilote, code_modele_avion) VALUES ('1', 'A320');
INSERT INTO QUALIFIER (id_pilote, code_modele_avion) VALUES ('1', 'MIAGE2020');
INSERT INTO QUALIFIER (id_pilote, code_modele_avion) VALUES ('2', 'A380');
INSERT INTO QUALIFIER (id_pilote, code_modele_avion) VALUES ('2', 'A320');
INSERT INTO QUALIFIER (id_pilote, code_modele_avion) VALUES ('2', 'MIAGE2020');
INSERT INTO QUALIFIER (id_pilote, code_modele_avion) VALUES ('3', 'A320');
INSERT INTO QUALIFIER (id_pilote, code_modele_avion) VALUES ('3', 'MIAGE2020');
INSERT INTO QUALIFIER (id_pilote, code_modele_avion) VALUES ('4', 'MIAGE2020');

-- On insere les données pour Avion
INSERT INTO AVION (numero_avion, code_modele_avion) VALUES ('1', 'A380');
INSERT INTO AVION (numero_avion, code_modele_avion) VALUES ('2', 'A380');
INSERT INTO AVION (numero_avion, code_modele_avion) VALUES ('3', 'A380');
INSERT INTO AVION (numero_avion, code_modele_avion) VALUES ('4', 'MIAGE2020');
INSERT INTO AVION (numero_avion, code_modele_avion) VALUES ('5', 'MIAGE2020');
INSERT INTO AVION (numero_avion, code_modele_avion) VALUES ('6', 'MIAGE2020');
INSERT INTO AVION (numero_avion, code_modele_avion) VALUES ('7', 'MIAGE2020');
INSERT INTO AVION (numero_avion, code_modele_avion) VALUES ('8', 'A320');


-- On insere les données pour Client
INSERT INTO CLIENT (id_client, nom_client, prenom_client, numeroRue_client, rue_client, code_postal_client, ville_client, pays_client, numeropassport_client) VALUES ('1', 'Jouannot' , 'Fabrice', '16', 'Rue Im2ag', '38400', 'Saint Martin d Hères', 'France', '1215415121');

-- On insere les données pour Reservation
INSERT INTO RESERVATION (id_reservation, date_reservation) VALUES ('1', '2020-03-04');
INSERT INTO RESERVATION (id_reservation, date_reservation) VALUES ('2', '2020-12-24');
INSERT INTO RESERVATION (id_reservation, date_reservation) VALUES ('3', '2020-06-04');

-- On insere les données pour Vol
INSERT INTO VOL (numero_vol, horaire_vol, date_vol,duree_vol, distance_vol, etat_vol, id_ville_provenir, id_ville_destiner, numero_avion) VALUES ('1', '12:59:35', '2020-05-15', '01:45:00' , '50', 'att', '1', '4', '2');
INSERT INTO VOL (numero_vol, horaire_vol, date_vol,duree_vol, distance_vol, etat_vol, id_ville_provenir, id_ville_destiner, numero_avion) VALUES ('2', '12:59:35', '2020-05-20', '01:45:00' , '50', 'att', '1', '5', '2');

-- On insere les données pour Piloter
INSERT INTO PILOTER (id_pilote, numero_vol) VALUES ('1', '1');
INSERT INTO PILOTER (id_pilote, numero_vol) VALUES ('1', '2');
INSERT INTO PILOTER (id_pilote, numero_vol) VALUES ('2', '1');
INSERT INTO PILOTER (id_pilote, numero_vol) VALUES ('3', '2');

-- On insere les données pour Affecter
INSERT INTO AFFECTER (id_hotesse, numero_vol) VALUES ('1', '1');
INSERT INTO AFFECTER (id_hotesse, numero_vol) VALUES ('2', '2');
INSERT INTO AFFECTER (id_hotesse, numero_vol) VALUES ('1', '2');

-- On insere les données pour Zone
INSERT INTO ZONE (id_zone, nom_zone) VALUES ('1', 'Avant');
INSERT INTO ZONE (id_zone, nom_zone) VALUES ('2', 'Centre');
INSERT INTO ZONE (id_zone, nom_zone) VALUES ('3', 'Arriere');

-- On insere les données pour Classe
INSERT INTO CLASSE (id_classe, nom_classe) VALUES ('1', 'Affaire');
INSERT INTO CLASSE (id_classe, nom_classe) VALUES ('2', 'Economique');
INSERT INTO CLASSE (id_classe, nom_classe) VALUES ('3', '1e Classe');

-- On insere les données pour Place
INSERT INTO PLACE (numero_place, numero_avion, id_zone, id_classe) VALUES ('45', '1', '2', '3');

-- On insere les données pour Reserve
INSERT INTO RESERVE (numero_place, numero_vol, id_reservation, prixplace_reserve) VALUES ('45', '2', '1', '12.5');
