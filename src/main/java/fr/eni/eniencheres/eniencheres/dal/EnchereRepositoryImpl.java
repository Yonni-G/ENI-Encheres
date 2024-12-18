package fr.eni.eniencheres.eniencheres.dal;

import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;
import fr.eni.eniencheres.eniencheres.bo.Categorie;
import org.springframework.beans.factory.annotation.Autowired;
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
        List<Categorie> categories = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Categorie.class));
        return categories;
    }

    // Récupérer la liste des articles en vente
    @Override
    public List<ArticleVendu> findAllArticleVendu() {
        String sql = "SELECT * FROM articles_vendus";
        List<ArticleVendu> articleVendus = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ArticleVendu.class));
        return articleVendus;
    }

    // Récupérer les détails d'un article en fonction de l'id
    @Override
    public ArticleVendu findArticleById(Integer noArticle) {
        String sql = "SELECT * FROM articles_vendus WHERE no_article = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ArticleVendu.class), noArticle);
    }

    // Récupérer la liste des articles appartenant à la catégorie "[noCategorie]"
    @Override
    public List<ArticleVendu> findArticleByCategorieId(Integer noCategorie){
        String sql = "SELECT * FROM articles_vendus WHERE no_categorie = ?";
        return jdbcTemplate.query(sql, new Object[]{noCategorie}, new BeanPropertyRowMapper<>(ArticleVendu.class));
    }
    // Récupérer la liste des articles contenant le nom "[%nom%]"
    @Override
    public List<ArticleVendu> findArticleByNom(String nom) {
        String sql = "SELECT * FROM articles_vendus WHERE nom_article LIKE ?";
        return jdbcTemplate.query(sql, new Object[]{"%" + nom + "%"}, new BeanPropertyRowMapper<>(ArticleVendu.class));
    }

    // Récupérer la liste des articles en fonction du nom et de la catégorie
    @Override
    public List<ArticleVendu> findArticleByNomAndCategorieId(Integer noCategorie, String nom) {
        String sql = "SELECT * FROM articles_vendus WHERE nom_article LIKE ? AND no_categorie = ?";
        return jdbcTemplate.query(sql, new Object[]{"%" + nom + "%", noCategorie}, new BeanPropertyRowMapper<>(ArticleVendu.class));
    }

    // Jointure entre les différentes classes pour récupérer les données nécessaires
    @Override
    public EnchereDTO getDetailsVente(int noArticle) {
        String sql = "SELECT a.no_article AS noArticle, a.lien_image, a.nom_article AS nomArticle, " +
                "a.description, c.libelle AS categorie, a.prix_initial AS miseAPrix, " +
                "a.date_fin_encheres AS dateFinEnchere," +
                " CONCAT(r.rue, ', ', r.code_postal, ' ', r.ville) AS retrait," +
                " u.pseudo AS vendeur, e.montant_enchere AS montantEnchere, e.date_enchere AS dateEnchere" +
                " FROM articles_vendus a" +
                " JOIN categories c ON c.no_categorie = a.no_categorie" +
                " LEFT JOIN retraits r ON r.no_article = a.no_article" +
                " JOIN utilisateurs u ON u.no_utilisateur = a.no_utilisateur" +
                " JOIN encheres e ON e.no_article = a.no_article" +
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
                rs.getTimestamp("dateEnchere").toLocalDateTime()
        );

        return jdbcTemplate.queryForObject(sql, rowMapper, noArticle);
    }
}
