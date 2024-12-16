-- Ajout des 4 catégories
INSERT INTO categories(libelle)
VALUES
    ('Informatique'),
    ('Ameublement'),
    ('Vêtements'),
    ('Sports&Loisirs');

-- Jeu d'essai Articles_vendus généré par IA pour test
INSERT INTO articles_vendus
(nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie)
VALUES
    -- Catégorie : Informatique
    ('Ordinateur portable', 'Laptop performant avec SSD 512 Go', '2024-12-10', '2024-12-20', 3000, NULL, 1, 1),
    ('Clavier mécanique', 'Clavier gaming RGB avec switches rouges', '2024-12-12', '2024-12-18', 800, NULL, 2, 1),
    -- Catégorie : Ameublement
    ('Canapé 3 places', 'Canapé confortable en tissu gris', '2024-12-11', '2024-12-22', 1500, NULL, 3, 2),
    ('Table basse', 'Table en bois massif, style scandinave', '2024-12-13', '2024-12-23', 1000, NULL, 4, 2),
    -- Catégorie : Vêtements
    ('Veste en cuir', 'Veste en cuir véritable, taille L', '2024-12-10', '2024-12-15', 1200, NULL, 5, 3),
    ('Sneakers', 'Baskets blanches unisexes, taille 42', '2024-12-09', '2024-12-19', 700, NULL, 6, 3),
    -- Catégorie : Sports & Loisirs
    ('Raquette de tennis', 'Raquette légère pour débutants', '2024-12-08', '2024-12-18', 500, NULL, 7, 4),
    ('Vélo de route', 'Vélo de route en aluminium, idéal pour randonnées', '2024-12-07', '2024-12-17', 5000, NULL, 8, 4);

-- Jeu d'essai Utilisateurs généré par IA pour test
INSERT INTO Utilisateurs
(pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur)
VALUES
    ('jdoe', 'Doe', 'John', 'jdoe@example.com', '0123456789', '10 Rue des Lilas', '75001', 'Paris', 'password123', 100, 0),
    ('mlaurent', 'Laurent', 'Marie', 'mlaurent@example.com', '0678901234', '15 Rue de la Paix', '69002', 'Lyon', 'securepass', 200, 0),
    ('tdupont', 'Dupont', 'Thomas', 'tdupont@example.com', '0654321987', '5 Avenue Foch', '33000', 'Bordeaux', 'thomas2024', 50, 0),
    ('cmartin', 'Martin', 'Claire', 'cmartin@example.com', '0612345678', '20 Rue Lafayette', '59000', 'Lille', 'claire@123', 150, 0),
    ('fbernard', 'Bernard', 'François', 'fbernard@example.com', '0698765432', '18 Boulevard Haussmann', '67000', 'Strasbourg', 'bernardpwd', 300, 0),
    ('aline', 'Durand', 'Aline', 'aline@example.com', '0609876543', '3 Rue des Fleurs', '76000', 'Rouen', 'alinepass', 80, 0),
    ('chris', 'Leclerc', 'Christophe', 'chris@example.com', '0623456789', '25 Allée des Chênes', '34000', 'Montpellier', 'chrissecure', 500, 0),
    ('lucieB', 'Blanc', 'Lucie', 'lucieB@example.com', '0634567890', '8 Rue Pasteur', '80000', 'Amiens', 'lucie!2024', 120, 0);
