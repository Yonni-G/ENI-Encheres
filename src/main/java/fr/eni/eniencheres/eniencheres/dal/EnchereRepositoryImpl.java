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
}
