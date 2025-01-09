-- Supprimer les contraintes dans l'ordre des dépendances si elles existent
IF EXISTS (SELECT *
           FROM sys.foreign_keys
           WHERE name = 'encheres_articles_vendus_fk')
ALTER TABLE ENCHERES
    DROP CONSTRAINT encheres_articles_vendus_fk;

IF EXISTS (SELECT *
           FROM sys.foreign_keys
           WHERE name = 'encheres_utilisateur_fk')
ALTER TABLE ARTICLES_VENDUS
    DROP CONSTRAINT encheres_utilisateur_fk;

IF EXISTS (SELECT *
           FROM sys.foreign_keys
           WHERE name = 'articles_vendus_categories_fk')
ALTER TABLE ARTICLES_VENDUS
    DROP CONSTRAINT articles_vendus_categories_fk;

IF EXISTS (SELECT *
           FROM sys.foreign_keys
           WHERE name = 'ventes_utilisateur_fk')
ALTER TABLE ARTICLES_VENDUS
    DROP CONSTRAINT ventes_utilisateur_fk;

IF EXISTS (SELECT *
           FROM sys.foreign_keys
           WHERE name = 'retraits_articles_vendus_fk')
ALTER TABLE RETRAITS
    DROP CONSTRAINT retraits_articles_vendus_fk;

-- Supprimer les clés primaires si elles existent
IF EXISTS (SELECT *
           FROM sys.key_constraints
           WHERE name = 'categorie_pk')
ALTER TABLE CATEGORIES
    DROP CONSTRAINT categorie_pk;

IF EXISTS (SELECT *
           FROM sys.key_constraints
           WHERE name = 'enchere_pk')
ALTER TABLE ENCHERES
    DROP CONSTRAINT enchere_pk;

IF EXISTS (SELECT *
           FROM sys.key_constraints
           WHERE name = 'retrait_pk')
ALTER TABLE RETRAITS
    DROP CONSTRAINT retrait_pk;

IF EXISTS (SELECT *
           FROM sys.key_constraints
           WHERE name = 'utilisateur_pk')
ALTER TABLE UTILISATEURS
    DROP CONSTRAINT utilisateur_pk;

IF EXISTS (SELECT *
           FROM sys.key_constraints
           WHERE name = 'articles_vendus_pk')
ALTER TABLE ARTICLES_VENDUS
    DROP CONSTRAINT articles_vendus_pk;

-- Supprimer les tables si elles existent
IF EXISTS (SELECT *
           FROM sys.objects
           WHERE type = 'U'
             AND name = 'ENCHERES')
    DROP TABLE ENCHERES;

IF EXISTS (SELECT *
           FROM sys.objects
           WHERE type = 'U'
             AND name = 'RETRAITS')
    DROP TABLE RETRAITS;

IF EXISTS (SELECT *
           FROM sys.objects
           WHERE type = 'U'
             AND name = 'ARTICLES_VENDUS')
    DROP TABLE ARTICLES_VENDUS;

IF EXISTS (SELECT *
           FROM sys.objects
           WHERE type = 'U'
             AND name = 'UTILISATEURS')
    DROP TABLE UTILISATEURS;

IF EXISTS (SELECT *
           FROM sys.objects
           WHERE type = 'U'
             AND name = 'CATEGORIES')
    DROP TABLE CATEGORIES;

-- Créer les tables et ajouter les contraintes PK et FK
CREATE TABLE CATEGORIES
(
    no_categorie INTEGER IDENTITY (1,1) NOT NULL,
    libelle      VARCHAR(30)            NOT NULL
);
ALTER TABLE CATEGORIES
    ADD constraint categorie_pk PRIMARY KEY (no_categorie);

CREATE TABLE ENCHERES
(
    no_enchere      INTEGER IDENTITY (1,1) NOT NULL,
    no_utilisateur  INTEGER                NOT NULL,
    no_article      INTEGER                NOT NULL,
    date_enchere    DATETIME2              NOT NULL,
    montant_enchere INTEGER                NOT NULL
);
ALTER TABLE ENCHERES
    ADD constraint enchere_pk PRIMARY KEY (no_enchere);

/*ALTER TABLE ENCHERES ADD constraint enchere_pk PRIMARY KEY (no_utilisateur, no_article);*/

CREATE TABLE RETRAITS
(
    no_article  INTEGER     NOT NULL,
    rue         VARCHAR(30) NOT NULL,
    code_postal VARCHAR(15) NOT NULL,
    ville       VARCHAR(30) NOT NULL
);
ALTER TABLE RETRAITS
    ADD constraint retrait_pk PRIMARY KEY (no_article);

CREATE TABLE UTILISATEURS
(
    no_utilisateur INTEGER IDENTITY (1,1) NOT NULL,
    pseudo         VARCHAR(30)            NOT NULL,
    nom            VARCHAR(30)            NOT NULL,
    prenom         VARCHAR(30)            NOT NULL,
    email          VARCHAR(20)            NOT NULL,
    telephone      VARCHAR(15),
    rue            VARCHAR(30)            NOT NULL,
    code_postal    VARCHAR(10)            NOT NULL,
    ville          VARCHAR(30)            NOT NULL,
    mot_de_passe   VARCHAR(80)            NOT NULL,
    credit         INTEGER                NOT NULL DEFAULT 0, -- Valeur par défaut pour credit
    administrateur BIT                    NOT NULL DEFAULT 0, -- Valeur par défaut pour administrateur (false)
    CONSTRAINT utilisateur_pk PRIMARY KEY (no_utilisateur),
    CONSTRAINT unique_pseudo UNIQUE (pseudo),                 -- Contrôle de l'unicité du pseudo
    CONSTRAINT unique_email UNIQUE (email)                    -- Contrôle de l'unicité de l'email
);

CREATE TABLE ARTICLES_VENDUS
(
    no_article          INTEGER IDENTITY (1,1) NOT NULL,
    nom_article         VARCHAR(30)            NOT NULL,
    description         VARCHAR(300)           NOT NULL,
    date_debut_encheres DATETIME2              NOT NULL,
    date_fin_encheres   DATETIME2              NOT NULL,
    prix_initial        INTEGER,
    prix_vente          INTEGER,
    no_utilisateur      INTEGER                NOT NULL,
    no_categorie        INTEGER                NOT NULL,
    lien_image          VARCHAR(100),
    etat_vente          BIT                    NOT NULL DEFAULT 0, -- Valeur par défaut pour etat_vente (false)
);
ALTER TABLE ARTICLES_VENDUS
    ADD constraint articles_vendus_pk PRIMARY KEY (no_article);

-- Ajouter les contraintes FK
ALTER TABLE ARTICLES_VENDUS
    ADD CONSTRAINT encheres_utilisateur_fk FOREIGN KEY (no_utilisateur) REFERENCES UTILISATEURS (no_utilisateur)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE ENCHERES
    ADD CONSTRAINT encheres_articles_vendus_fk FOREIGN KEY (no_article)
        REFERENCES ARTICLES_VENDUS (no_article)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE RETRAITS
    ADD CONSTRAINT retraits_articles_vendus_fk FOREIGN KEY (no_article)
        REFERENCES ARTICLES_VENDUS (no_article)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE ARTICLES_VENDUS
    ADD CONSTRAINT articles_vendus_categories_fk FOREIGN KEY (no_categorie)
        REFERENCES CATEGORIES (no_categorie)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE ARTICLES_VENDUS
    ADD CONSTRAINT ventes_utilisateur_fk FOREIGN KEY (no_utilisateur)
        REFERENCES UTILISATEURS (no_utilisateur)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION;


-- ------------------------- Insérer les données (jeu d'essai) ------------------------------

-- Ajout des 4 catégories
INSERT INTO categories(libelle)
VALUES ('Informatique'),
       ('Ameublement'),
       ('Vêtements'),
       ('Sports&Loisirs');

-- Jeu d'essai Utilisateurs généré par IA pour test
INSERT INTO Utilisateurs
(pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur)
VALUES ('admin', 'Doe', 'John', 'jdoe@example.com', '0123456789', '10 Rue des Lilas', '75001', 'Paris',
        '{bcrypt}$2a$10$RUP813cgJ0sLL2W5VqGBvuXyySMACT.DpKNGsVF41DEiS6MB9g8RW', 100000, 1),
       ('mlaurent', 'Laurent', 'Marie', 'mlaurent@example.com', '0678901234', '15 Rue de la Paix', '69002', 'Lyon',
        '{bcrypt}$2a$10$RUP813cgJ0sLL2W5VqGBvuXyySMACT.DpKNGsVF41DEiS6MB9g8RW', 200, 0),
       ('tdupont', 'Dupont', 'Thomas', 'tdupont@example.com', '0654321987', '5 Avenue Foch', '33000', 'Bordeaux',
        '{bcrypt}$2a$10$RUP813cgJ0sLL2W5VqGBvuXyySMACT.DpKNGsVF41DEiS6MB9g8RW', 50, 0),
       ('cmartin', 'Martin', 'Claire', 'cmartin@example.com', '0612345678', '20 Rue Lafayette', '59000', 'Lille',
        '{bcrypt}$2a$10$RUP813cgJ0sLL2W5VqGBvuXyySMACT.DpKNGsVF41DEiS6MB9g8RW@123', 150, 0),
       ('fbernard', 'Bernard', 'François', 'fbernard@example.com', '0698765432', '18 Boulevard Haussmann', '67000',
        'Strasbourg', '{bcrypt}$2a$10$RUP813cgJ0sLL2W5VqGBvuXyySMACT.DpKNGsVF41DEiS6MB9g8RW', 300, 0),
       ('aline', 'Durand', 'Aline', 'aline@example.com', '0609876543', '3 Rue des Fleurs', '76000', 'Rouen',
        '{bcrypt}$2a$10$RUP813cgJ0sLL2W5VqGBvuXyySMACT.DpKNGsVF41DEiS6MB9g8RW', 80, 0),
       ('chris', 'Leclerc', 'Christophe', 'chris@example.com', '0623456789', '25 Allée des Chênes', '34000',
        'Montpellier', '{bcrypt}$2a$10$RUP813cgJ0sLL2W5VqGBvuXyySMACT.DpKNGsVF41DEiS6MB9g8RW', 500, 0),
       ('lucieB', 'Blanc', 'Lucie', 'lucieB@example.com', '0634567890', '8 Rue Pasteur', '80000', 'Amiens',
        '{bcrypt}$2a$10$RUP813cgJ0sLL2W5VqGBvuXyySMACT.DpKNGsVF41DEiS6MB9g8RW4', 120, 0);


-- Jeu d'essai Articles_vendus généré par IA pour test
INSERT INTO articles_vendus
(nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur,
 no_categorie, lien_image)
VALUES
    -- Catégorie : Informatique
    ('Ordinateur portable', 'Laptop performant avec SSD 512 Go', '2024-12-10 00:00:00', '2024-12-20 00:00:00', 3000,
     3000, 1, 1, '/images/ordinateur_port.jpg'),
    ('Clavier mécanique', 'Clavier gaming RGB avec switches rouges', '2024-12-12 00:00:00', '2024-12-18 00:00:00', 800,
     800, 2, 1, '/images/clavier_mec.jpg'),
    -- Catégorie : Ameublement
    ('Canapé 3 places', 'Canapé confortable en tissu gris', '2024-12-11 00:00:00', '2025-12-22 00:00:00', 1500, 1500, 3,
     2, '/images/canape.webp'),
    ('Table basse', 'Table en bois massif, style scandinave', '2024-12-13 00:00:00', '2025-12-23 00:00:00', 1000, 1000,
     4, 2, '/images/table_basse.avif'),
    -- Catégorie : Vêtements
    ('Veste en cuir', 'Veste en cuir véritable, taille L', '2025-02-10 00:00:00', '2025-12-15 00:00:00', 1200, 1200, 5,
     3, '/images/veste_cuir.jpg'),
    ('Sneakers', 'Baskets blanches unisexes, taille 42', '2025-02-09 00:00:00', '2025-12-19 00:00:00', 700, 700, 6, 3,
     '/images/sneakers.webp'),
    -- Catégorie : Sports & Loisirs
    ('Raquette de tennis', 'Raquette légère pour débutants', '2024-12-08 00:00:00', '2024-12-18 00:00:00', 500, 500, 7,
     4, '/images/raquette_de_tennis.avif'),
    ('Vélo de route', 'Vélo de route en aluminium, idéal pour randonnées', '2024-12-07 00:00:00', '2024-12-17 00:00:00',
     5000, 5000, 8, 4, '/images/velo_route.jpg');


INSERT INTO ENCHERES (no_utilisateur, no_article, date_enchere, montant_enchere)
VALUES (1, 4, '2024-01-02 12:00:00', 1000), -- John Doe enchère sur Ordinateur Portable
       (2, 5, '2024-01-03 14:30:00', 1100), -- Jane Smith enchère sur Ordinateur Portable
       (3, 6, '2024-02-02 09:00:00', 1200), -- Bob Lee enchère sur T-shirt vintage
       (1, 3, '2024-03-01 10:00:00', 1300), -- John Doe enchère sur Canapé 3 places
       (1, 2, '2024-12-18 10:00:00', 900); -- clavier remporté par admin

INSERT INTO RETRAITS (no_article, rue, code_postal, ville)
VALUES (3, '123 rue de Paris', '75001', 'Paris'),      -- Retrait pour Ordinateur Portable
       (4, '456 avenue des Champs', '75002', 'Paris'), -- Retrait pour T-shirt vintage
       (5, '789 boulevard Saint-Germain', '75003', 'Paris'); -- Retrait pour Canapé 3 places
