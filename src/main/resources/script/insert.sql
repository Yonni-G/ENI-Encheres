-- Script de création de la base de données ENCHERES
--   type : SQL Server 2012

-- Supprimer les contraintes dans l'ordre des dépendances
ALTER TABLE ENCHERES DROP CONSTRAINT encheres_articles_vendus_fk;
ALTER TABLE ARTICLES_VENDUS DROP CONSTRAINT encheres_utilisateur_fk;
ALTER TABLE ARTICLES_VENDUS DROP CONSTRAINT articles_vendus_categories_fk;
ALTER TABLE ARTICLES_VENDUS DROP CONSTRAINT ventes_utilisateur_fk;
ALTER TABLE RETRAITS DROP CONSTRAINT retraits_articles_vendus_fk;

-- Supprimer les clés primaires
ALTER TABLE CATEGORIES DROP CONSTRAINT categorie_pk;
ALTER TABLE ENCHERES DROP CONSTRAINT enchere_pk;
ALTER TABLE RETRAITS DROP CONSTRAINT retrait_pk;
ALTER TABLE UTILISATEURS DROP CONSTRAINT utilisateur_pk;
ALTER TABLE ARTICLES_VENDUS DROP CONSTRAINT articles_vendus_pk;

-- Supprimer les tables
DROP TABLE ENCHERES;
DROP TABLE RETRAITS;
DROP TABLE ARTICLES_VENDUS;
DROP TABLE UTILISATEURS;
DROP TABLE CATEGORIES;

-- Créer les tables et ajouter les contraintes PK et FK
CREATE TABLE CATEGORIES (
                            no_categorie   INTEGER IDENTITY(1,1) NOT NULL,
                            libelle        VARCHAR(30) NOT NULL
)
ALTER TABLE CATEGORIES ADD constraint categorie_pk PRIMARY KEY (no_categorie)

CREATE TABLE ENCHERES (
                          no_utilisateur   INTEGER NOT NULL,
                          no_article       INTEGER NOT NULL,
                          date_enchere     datetime NOT NULL,
                          montant_enchere  INTEGER NOT NULL
)
ALTER TABLE ENCHERES ADD constraint enchere_pk PRIMARY KEY (no_utilisateur, no_article)

CREATE TABLE RETRAITS (
                          no_article         INTEGER NOT NULL,
                          rue              VARCHAR(30) NOT NULL,
                          code_postal      VARCHAR(15) NOT NULL,
                          ville            VARCHAR(30) NOT NULL
)
ALTER TABLE RETRAITS ADD constraint retrait_pk PRIMARY KEY  (no_article)

CREATE TABLE UTILISATEURS (
                              no_utilisateur   INTEGER IDENTITY(1,1) NOT NULL,
                              pseudo           VARCHAR(30) NOT NULL,
                              nom              VARCHAR(30) NOT NULL,
                              prenom           VARCHAR(30) NOT NULL,
                              email            VARCHAR(20) NOT NULL,
                              telephone        VARCHAR(15),
                              rue              VARCHAR(30) NOT NULL,
                              code_postal      VARCHAR(10) NOT NULL,
                              ville            VARCHAR(30) NOT NULL,
                              mot_de_passe     VARCHAR(80) NOT NULL,
                              credit           INTEGER NOT NULL DEFAULT 0, -- Valeur par défaut pour credit
                              administrateur   BIT NOT NULL DEFAULT 0, -- Valeur par défaut pour administrateur (false)
                              CONSTRAINT utilisateur_pk PRIMARY KEY (no_utilisateur),
                              CONSTRAINT unique_pseudo UNIQUE (pseudo),   -- Contrôle de l'unicité du pseudo
                              CONSTRAINT unique_email UNIQUE (email)      -- Contrôle de l'unicité de l'email
);
CREATE TABLE ARTICLES_VENDUS (
                                 no_article                    INTEGER IDENTITY(1,1) NOT NULL,
                                 nom_article                   VARCHAR(30) NOT NULL,
                                 description                   VARCHAR(300) NOT NULL,
                                 date_debut_encheres           DATE NOT NULL,
                                 date_fin_encheres             DATE NOT NULL,
                                 prix_initial                  INTEGER,
                                 prix_vente                    INTEGER,
                                 no_utilisateur                INTEGER NOT NULL,
                                 no_categorie                  INTEGER NOT NULL,
                                 lien_image                    VARCHAR
)
ALTER TABLE ARTICLES_VENDUS ADD constraint articles_vendus_pk PRIMARY KEY (no_article)

ALTER TABLE ARTICLES_VENDUS
    ADD CONSTRAINT encheres_utilisateur_fk FOREIGN KEY ( no_utilisateur ) REFERENCES UTILISATEURS ( no_utilisateur )
        ON DELETE NO ACTION
        ON UPDATE no action

ALTER TABLE ENCHERES
    ADD CONSTRAINT encheres_articles_vendus_fk FOREIGN KEY ( no_article )
        REFERENCES ARTICLES_VENDUS ( no_article )
        ON DELETE NO ACTION
        ON UPDATE no action

ALTER TABLE RETRAITS
    ADD CONSTRAINT retraits_articles_vendus_fk FOREIGN KEY ( no_article )
        REFERENCES ARTICLES_VENDUS ( no_article )
        ON DELETE NO ACTION
        ON UPDATE no action

ALTER TABLE ARTICLES_VENDUS
    ADD CONSTRAINT articles_vendus_categories_fk FOREIGN KEY ( no_categorie )
        REFERENCES categories ( no_categorie )
        ON DELETE NO ACTION
        ON UPDATE no action

ALTER TABLE ARTICLES_VENDUS
    ADD CONSTRAINT ventes_utilisateur_fk FOREIGN KEY ( no_utilisateur )
        REFERENCES utilisateurs ( no_utilisateur )
        ON DELETE NO ACTION
        ON UPDATE no action


-- ------------------------- Insérer les données (jeu d'essai) ------------------------------

-- Ajout des 4 catégories
INSERT INTO categories(libelle)
VALUES
    ('Informatique'),
    ('Ameublement'),
    ('Vêtements'),
    ('Sports&Loisirs');

-- Jeu d'essai Articles_vendus généré par IA pour test
INSERT INTO articles_vendus
(nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie, lien_image)
VALUES
    -- Catégorie : Informatique
    ('Ordinateur portable', 'Laptop performant avec SSD 512 Go', '2024-12-10', '2024-12-20', 3000, 3000, 1, 1, '/images/ordinateur_port.jpg'),
    ('Clavier mécanique', 'Clavier gaming RGB avec switches rouges', '2024-12-12', '2024-12-18', 800, 800, 2, 1, '/images/clavier_mec.jpg'),
    -- Catégorie : Ameublement
    ('Canapé 3 places', 'Canapé confortable en tissu gris', '2024-12-11', '2024-12-22', 1500, 1500, 3, 2, '/images/canape.webp'),
    ('Table basse', 'Table en bois massif, style scandinave', '2024-12-13', '2024-12-23', 1000, 1000, 4, 2, '/images/table_basse.avif'),
    -- Catégorie : Vêtements
    ('Veste en cuir', 'Veste en cuir véritable, taille L', '2024-12-10', '2024-12-15', 1200, 1200, 5, 3, '/images/veste_cuir.jpg'),
    ('Sneakers', 'Baskets blanches unisexes, taille 42', '2024-12-09', '2024-12-19', 700, 700, 6, 3, '/images/sneakers.webp'),
    -- Catégorie : Sports & Loisirs
    ('Raquette de tennis', 'Raquette légère pour débutants', '2024-12-08', '2024-12-18', 500, 500, 7, 4, '/images/raquette_de_tennis.avif'),
    ('Vélo de route', 'Vélo de route en aluminium, idéal pour randonnées', '2024-12-07', '2024-12-17', 5000, 5000, 8, 4, '/images/velo_route.jpg');

-- Jeu d'essai Utilisateurs généré par IA pour test
INSERT INTO Utilisateurs
(pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur)
VALUES
    ('jdoe', 'Doe', 'John', 'jdoe@example.com', '0123456789', '10 Rue des Lilas', '75001', 'Paris', 'password123', 100, 1),
    ('mlaurent', 'Laurent', 'Marie', 'mlaurent@example.com', '0678901234', '15 Rue de la Paix', '69002', 'Lyon', 'securepass', 200, 0),
    ('tdupont', 'Dupont', 'Thomas', 'tdupont@example.com', '0654321987', '5 Avenue Foch', '33000', 'Bordeaux', 'thomas2024', 50, 0),
    ('cmartin', 'Martin', 'Claire', 'cmartin@example.com', '0612345678', '20 Rue Lafayette', '59000', 'Lille', 'claire@123', 150, 0),
    ('fbernard', 'Bernard', 'François', 'fbernard@example.com', '0698765432', '18 Boulevard Haussmann', '67000', 'Strasbourg', 'bernardpwd', 300, 0),
    ('aline', 'Durand', 'Aline', 'aline@example.com', '0609876543', '3 Rue des Fleurs', '76000', 'Rouen', 'alinepass', 80, 0),
    ('chris', 'Leclerc', 'Christophe', 'chris@example.com', '0623456789', '25 Allée des Chênes', '34000', 'Montpellier', 'chrissecure', 500, 0),
    ('lucieB', 'Blanc', 'Lucie', 'lucieB@example.com', '0634567890', '8 Rue Pasteur', '80000', 'Amiens', 'lucie!2024', 120, 0);
