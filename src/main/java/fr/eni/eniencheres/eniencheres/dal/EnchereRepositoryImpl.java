package fr.eni.eniencheres.eniencheres.dal;

import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;
import fr.eni.eniencheres.eniencheres.bo.Categorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
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
}
