package fr.eni.eniencheres.eniencheres.bll;

import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;
import fr.eni.eniencheres.eniencheres.bo.Categorie;

import java.util.List;

public interface EnchereService {
    List<Categorie> findAllCategories();
    List<ArticleVendu> findAllArticleVendu();

    ArticleVendu findArticleById(Integer noArticle);

    List<ArticleVendu> findArticleByCategorieId(Integer noCategorie);

    List<ArticleVendu> findArticleByNom(String nom);

    List<ArticleVendu> findArticleByNomAndCategorieId(Integer noCategorie, String nom);
}
