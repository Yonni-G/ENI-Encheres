package fr.eni.eniencheres.eniencheres.dal;

import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;
import fr.eni.eniencheres.eniencheres.bo.Categorie;
import fr.eni.eniencheres.eniencheres.bo.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EnchereRepositoryImpl implements EnchereRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    // Récupérer la liste de toutes les catégories
    @Override
    public List<Categorie> findAllCategories() {
        String sql = "SELECT * FROM categories";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Categorie.class));
    }

    // Récupérer la liste des articles en vente
    @Override
    public List<ArticleVendu> findAllArticleVendu() {
        String sql = "SELECT a.no_article AS noArticle, a.lien_image, a.nom_article AS nomArticle, a.date_fin_encheres AS dateFinEnchere, a.prix_initial AS miseAPrix, U.pseudo AS vendeur FROM articles_vendus a LEFT JOIN UTILISATEURS U on a.no_utilisateur = U.no_utilisateur";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ArticleVendu articleVendu = new ArticleVendu();
            articleVendu.setNoArticle(rs.getInt("noArticle"));
            articleVendu.setLien_image(rs.getString("lien_image"));
            articleVendu.setNomArticle(rs.getString("nomArticle"));
            //articleVendu.setDateFinEncheres(rs.getTimestamp("dateFinEnchere").toLocalDateTime());
            articleVendu.setDateFinEncheres(rs.getTimestamp("dateFinEnchere").toLocalDateTime());
            articleVendu.setMiseAPrix(rs.getInt("miseAPrix"));

            // Création d'un objet Utilisateur et assignation du pseudo
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setPseudo(rs.getString("vendeur"));
            articleVendu.setVendeur(utilisateur);  // On affecte l'objet Utilisateur au vendeur

            return articleVendu;
        });
    }
// --------------------- Méthodes FILTRES pour tous les utilisateurs ------------------------------------------------------------------------
    // Récupérer les détails d'un article en fonction de l'id
    @Override
    public ArticleVendu findArticleById(Integer noArticle) {
        String sql = "SELECT * FROM articles_vendus WHERE no_article = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ArticleVendu.class), noArticle);
    }

    // Récupérer la liste des articles appartenant à la catégorie "[noCategorie]"
    @Override
    public List<ArticleVendu> findArticleByCategorieId(Integer noCategorie){
        String sql = "SELECT a.no_article AS noArticle, a.lien_image, a.nom_article AS nomArticle, a.date_fin_encheres AS dateFinEnchere, a.prix_initial AS miseAPrix, U.pseudo AS vendeur FROM articles_vendus a LEFT JOIN UTILISATEURS U on a.no_utilisateur = U.no_utilisateur WHERE a.no_categorie = ?";
        return jdbcTemplate.query(sql, new Object[]{noCategorie}, (rs, rowNum) -> {
            ArticleVendu articleVendu = new ArticleVendu();
            articleVendu.setNoArticle(rs.getInt("noArticle"));
            articleVendu.setLien_image(rs.getString("lien_image"));
            articleVendu.setNomArticle(rs.getString("nomArticle"));
            articleVendu.setDateFinEncheres(rs.getTimestamp("dateFinEnchere").toLocalDateTime());
            articleVendu.setMiseAPrix(rs.getInt("miseAPrix"));

            // Création d'un objet Utilisateur et assignation du pseudo
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setPseudo(rs.getString("vendeur"));
            articleVendu.setVendeur(utilisateur);  // On affecte l'objet Utilisateur au vendeur

            return articleVendu;
        });
    }
    // Récupérer la liste des articles contenant le nom "[%nom%]"
    @Override
    public List<ArticleVendu> findArticleByNom(String nom) {
        String sql = "SELECT a.no_article AS noArticle, a.lien_image, a.nom_article AS nomArticle, a.date_fin_encheres AS dateFinEnchere, a.prix_initial AS miseAPrix, U.pseudo AS vendeur FROM articles_vendus a LEFT JOIN UTILISATEURS U on a.no_utilisateur = U.no_utilisateur WHERE a.nom_article LIKE ?";
        return jdbcTemplate.query(sql, new Object[]{"%" + nom + "%"}, (rs, rowNum) -> {
            ArticleVendu articleVendu = new ArticleVendu();
            articleVendu.setNoArticle(rs.getInt("noArticle"));
            articleVendu.setLien_image(rs.getString("lien_image"));
            articleVendu.setNomArticle(rs.getString("nomArticle"));
            articleVendu.setDateFinEncheres(rs.getTimestamp("dateFinEnchere").toLocalDateTime());
            articleVendu.setMiseAPrix(rs.getInt("miseAPrix"));

            // Création d'un objet Utilisateur et assignation du pseudo
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setPseudo(rs.getString("vendeur"));
            articleVendu.setVendeur(utilisateur);  // On affecte l'objet Utilisateur au vendeur

            return articleVendu;
        });
    }

    // Récupérer la liste des articles en fonction du nom et de la catégorie
    @Override
    public List<ArticleVendu> findArticleByNomAndCategorieId(Integer noCategorie, String nom) {
        String sql = "SELECT a.no_article AS noArticle, a.lien_image, a.nom_article AS nomArticle, a.date_fin_encheres AS dateFinEnchere, a.prix_initial AS miseAPrix, U.pseudo AS vendeur FROM articles_vendus a LEFT JOIN UTILISATEURS U on a.no_utilisateur = U.no_utilisateur WHERE a.nom_article LIKE ? AND a.no_categorie = ?";
        return jdbcTemplate.query(sql, new Object[]{"%" + nom + "%", noCategorie}, (rs, rowNum) -> {
            ArticleVendu articleVendu = new ArticleVendu();
            articleVendu.setNoArticle(rs.getInt("noArticle"));
            articleVendu.setLien_image(rs.getString("lien_image"));
            articleVendu.setNomArticle(rs.getString("nomArticle"));
            articleVendu.setDateFinEncheres(rs.getTimestamp("dateFinEnchere").toLocalDateTime());
            articleVendu.setMiseAPrix(rs.getInt("miseAPrix"));

            // Création d'un objet Utilisateur et assignation du pseudo
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setPseudo(rs.getString("vendeur"));
            articleVendu.setVendeur(utilisateur);  // On affecte l'objet Utilisateur au vendeur

            return articleVendu;
        });
    }

// --------------------- Méthodes FILTRES pour tous les utilisateurs ------------------------------------------------------------------------
    // Récupérer la liste des articles en fonction de 'Enchères ouvertes ?'
    @Override
    public List<ArticleVendu> findArticleByEncheresOuvertes() {
        String sql = "SELECT a.no_article AS noArticle, a.lien_image, a.nom_article AS nomArticle, a.date_fin_encheres AS dateFinEnchere, a.prix_initial AS miseAPrix, U.pseudo AS vendeur FROM articles_vendus a LEFT JOIN UTILISATEURS U on a.no_utilisateur = U.no_utilisateur WHERE date_fin_encheres > GETDATE()";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ArticleVendu articleVendu = new ArticleVendu();
            articleVendu.setNoArticle(rs.getInt("noArticle"));
            articleVendu.setLien_image(rs.getString("lien_image"));
            articleVendu.setNomArticle(rs.getString("nomArticle"));
            articleVendu.setDateFinEncheres(rs.getTimestamp("dateFinEnchere").toLocalDateTime());
            articleVendu.setMiseAPrix(rs.getInt("miseAPrix"));

            // Création d'un objet Utilisateur et assignation du pseudo
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setPseudo(rs.getString("vendeur"));
            articleVendu.setVendeur(utilisateur);  // On affecte l'objet Utilisateur au vendeur

            return articleVendu;
        });
    }

    // Récupérer la liste des articles en fonction de 'Enchères en cours ?'
    @Override
    public List<ArticleVendu> findArticleByEncheresEnCours(Integer no_utilisateur) {
        String sql = "SELECT a.no_article AS noArticle, a.lien_image, a.nom_article AS nomArticle, " +
                "a.date_fin_encheres AS dateFinEnchere, a.prix_initial AS miseAPrix, " +
                "U.pseudo AS vendeur FROM articles_vendus a " +
                "LEFT JOIN UTILISATEURS U on a.no_utilisateur = U.no_utilisateur " +
                "LEFT JOIN encheres e ON e.no_article = a.no_article " +
                "WHERE e.montant_enchere IS NOT NULL AND e.no_utilisateur = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ArticleVendu articleVendu = new ArticleVendu();
            articleVendu.setNoArticle(rs.getInt("noArticle"));
            articleVendu.setLien_image(rs.getString("lien_image"));
            articleVendu.setNomArticle(rs.getString("nomArticle"));
            articleVendu.setDateFinEncheres(rs.getTimestamp("dateFinEnchere").toLocalDateTime());
            articleVendu.setMiseAPrix(rs.getInt("miseAPrix"));

            // Création d'un objet Utilisateur et assignation du pseudo
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setPseudo(rs.getString("vendeur"));
            articleVendu.setVendeur(utilisateur);  // On affecte l'objet Utilisateur au vendeur

            return articleVendu;
        }, no_utilisateur);
    }

    // TODO: Récupérer la liste des articles en fonction de 'Enchères remportees ?'
//    @Override
//    public List<ArticleVendu> findArticleByEncheresRemportees() {
//        String sql = "SELECT a.no_article AS noArticle, a.lien_image, a.nom_article AS nomArticle, a.date_fin_encheres AS dateFinEnchere, a.prix_initial AS miseAPrix, U.pseudo AS vendeur FROM articles_vendus a LEFT JOIN UTILISATEURS U on a.no_utilisateur = U.no_utilisateur WHERE date_fin_encheres > GETDATE() AND date_debut_encheres < GETDATE()";
//        return jdbcTemplate.query(sql, (rs, rowNum) -> {
//            ArticleVendu articleVendu = new ArticleVendu();
//            articleVendu.setNoArticle(rs.getInt("noArticle"));
//            articleVendu.setLien_image(rs.getString("lien_image"));
//            articleVendu.setNomArticle(rs.getString("nomArticle"));
//            articleVendu.setDateFinEncheres(rs.getTimestamp("dateFinEnchere").toLocalDateTime());
//            articleVendu.setMiseAPrix(rs.getInt("miseAPrix"));
//
//            // Création d'un objet Utilisateur et assignation du pseudo
//            Utilisateur utilisateur = new Utilisateur();
//            utilisateur.setPseudo(rs.getString("vendeur"));
//            articleVendu.setVendeur(utilisateur);  // On affecte l'objet Utilisateur au vendeur
//
//            return articleVendu;
//        });
//    }

    // Récupérer la liste des articles en fonction de 'Ventes en cours ?'
    @Override
    public List<ArticleVendu> findArticleByMesVentesEnCours(Integer no_utilisateur) {
        String sql = "SELECT a.no_article AS noArticle, a.lien_image, a.nom_article AS nomArticle, " +
                "a.date_fin_encheres AS dateFinEnchere, a.prix_initial AS miseAPrix, " +
                "U.pseudo AS vendeur FROM articles_vendus a " +
                "LEFT JOIN UTILISATEURS U on a.no_utilisateur = U.no_utilisateur " +
                "LEFT JOIN encheres e ON e.no_article = a.no_article " +
                "WHERE a.no_utilisateur = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ArticleVendu articleVendu = new ArticleVendu();
            articleVendu.setNoArticle(rs.getInt("noArticle"));
            articleVendu.setLien_image(rs.getString("lien_image"));
            articleVendu.setNomArticle(rs.getString("nomArticle"));
            articleVendu.setDateFinEncheres(rs.getTimestamp("dateFinEnchere").toLocalDateTime());
            articleVendu.setMiseAPrix(rs.getInt("miseAPrix"));

            // Création d'un objet Utilisateur et assignation du pseudo
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setPseudo(rs.getString("vendeur"));
            articleVendu.setVendeur(utilisateur);  // On affecte l'objet Utilisateur au vendeur

            return articleVendu;
        }, no_utilisateur);
    }

    // Récupérer la liste des articles en fonction de 'Vente non débutées ?'
    @Override
    public List<ArticleVendu> findArticleByVenteNonDebutee() {
        String sql = "SELECT a.no_article AS noArticle, a.lien_image, a.nom_article AS nomArticle, a.date_fin_encheres AS dateFinEnchere," +
                " a.prix_initial AS miseAPrix, U.pseudo AS vendeur FROM articles_vendus a " +
                "LEFT JOIN UTILISATEURS U on a.no_utilisateur = U.no_utilisateur WHERE date_debut_encheres > GETDATE()";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ArticleVendu articleVendu = new ArticleVendu();
            articleVendu.setNoArticle(rs.getInt("noArticle"));
            articleVendu.setLien_image(rs.getString("lien_image"));
            articleVendu.setNomArticle(rs.getString("nomArticle"));
            articleVendu.setDateFinEncheres(rs.getTimestamp("dateFinEnchere").toLocalDateTime());
            articleVendu.setMiseAPrix(rs.getInt("miseAPrix"));

            // Création d'un objet Utilisateur et assignation du pseudo
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setPseudo(rs.getString("vendeur"));
            articleVendu.setVendeur(utilisateur);  // On affecte l'objet Utilisateur au vendeur

            return articleVendu;
        });
    }

    // Récupérer la liste des articles en fonction de 'Vente non débutées ?'
    @Override
    public List<ArticleVendu> findArticleByVenteTerminee() {
        String sql = "SELECT a.no_article AS noArticle, a.lien_image, a.nom_article AS nomArticle, a.date_fin_encheres AS dateFinEnchere," +
                " a.prix_initial AS miseAPrix, U.pseudo AS vendeur FROM articles_vendus a " +
                "LEFT JOIN UTILISATEURS U on a.no_utilisateur = U.no_utilisateur WHERE date_fin_encheres < GETDATE()";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ArticleVendu articleVendu = new ArticleVendu();
            articleVendu.setNoArticle(rs.getInt("noArticle"));
            articleVendu.setLien_image(rs.getString("lien_image"));
            articleVendu.setNomArticle(rs.getString("nomArticle"));
            articleVendu.setDateFinEncheres(rs.getTimestamp("dateFinEnchere").toLocalDateTime());
            articleVendu.setMiseAPrix(rs.getInt("miseAPrix"));

            // Création d'un objet Utilisateur et assignation du pseudo
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setPseudo(rs.getString("vendeur"));
            articleVendu.setVendeur(utilisateur);  // On affecte l'objet Utilisateur au vendeur

            return articleVendu;
        });
    }

//---------------------------------------------------------------------------------------------------------------------------------------

    // Jointure entre les différentes classes pour récupérer les données nécessaires (!! detailVente.html !!)
    @Override
    public EnchereDTO getDetailsVente(int noArticle) {
        String sql = "SELECT a.no_article AS noArticle, a.lien_image, a.nom_article AS nomArticle, " +
                "a.description, c.libelle AS categorie, a.prix_initial AS miseAPrix, " +
                "a.date_fin_encheres AS dateFinEnchere," +
                " CONCAT(r.rue, ', ', r.code_postal, ' ', r.ville) AS retrait," +
                " u.pseudo AS vendeur, e.montant_enchere AS montantEnchere, e.date_enchere AS dateEnchere" +
                " FROM articles_vendus a" +
                " LEFT JOIN categories c ON c.no_categorie = a.no_categorie" +
                " LEFT JOIN retraits r ON r.no_article = a.no_article" +
                " LEFT JOIN utilisateurs u ON u.no_utilisateur = a.no_utilisateur" +
                " LEFT JOIN encheres e ON e.no_article = a.no_article" +
                " WHERE a.no_article = ?;";
        // Mappage des données récupérées dans l'objet DTO
        RowMapper<EnchereDTO> rowMapper = (rs, rowNum) -> new EnchereDTO(
                rs.getInt("noArticle"),
                rs.getString("lien_image"),
                rs.getString("nomArticle"),
                rs.getString("description"),
                rs.getString("categorie"),
                rs.getInt("miseAPrix"),
                rs.getTimestamp("dateFinEnchere").toLocalDateTime(),
                rs.getString("retrait"),
                rs.getString("vendeur"),
                rs.getInt("montantEnchere"),
                rs.getTimestamp("dateEnchere") != null ? rs.getTimestamp("dateEnchere").toLocalDateTime() : null
        );

        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, noArticle);

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Cette méthode permet de récupérer le pseudo de l'enchérisseur
    @Override
    public EnchereDTO getUtilEnchere(int noArticle){
        String sql = "SELECT u.pseudo, e.no_utilisateur AS noUtilisateur, e.montant_enchere AS montantEnchere " +
                "FROM utilisateurs u " +
                "LEFT JOIN encheres e ON u.no_utilisateur = e.no_utilisateur " +
                "WHERE e.no_article = ? ";
        RowMapper<EnchereDTO> rowMapper = (rs, rowNum) -> new EnchereDTO(
                rs.getInt("noUtilisateur"),
                rs.getString("pseudo"),
                rs.getInt("montantEnchere")
        );
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, noArticle);

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Méthode pour récupérer le vainqueur de l'enchère
    @Override
    public EnchereDTO getWinner(int noArticle){
        String sql = "SELECT u.pseudo, e.no_utilisateur AS noUtilisateur, e.montant_enchere AS montantEnchere, " +
                "e.date_enchere AS dateEnchere, a.date_fin_encheres AS dateFinEncheres " +
                "FROM utilisateurs u " +
                "LEFT JOIN encheres e ON u.no_utilisateur = e.no_utilisateur " +
                "LEFT JOIN articles_vendus a ON a.no_article = e.no_article " +
                "WHERE e.no_article = ? AND date_fin_encheres < GETDATE()";
        RowMapper<EnchereDTO> rowMapper = (rs, rowNum) -> new EnchereDTO(
                rs.getInt("noUtilisateur"),
                rs.getString("pseudo"),
                rs.getInt("montantEnchere"),
                rs.getTimestamp("dateEnchere").toLocalDateTime(),
                rs.getTimestamp("dateFinEncheres").toLocalDateTime()
        );
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, noArticle);

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
