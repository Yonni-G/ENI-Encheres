package fr.eni.eniencheres.eniencheres.bll;

import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;
import fr.eni.eniencheres.eniencheres.bo.Categorie;
import fr.eni.eniencheres.eniencheres.bo.Enchere;
import fr.eni.eniencheres.eniencheres.dal.EnchereDTO;

import java.util.List;
import java.util.Optional;

public interface EnchereService {
    List<Categorie> findAllCategories();
    List<ArticleVendu> findAllArticleVendu();

    ArticleVendu findArticleById(Integer noArticle);

    List<ArticleVendu> findArticleByCategorieId(Integer noCategorie);

    List<ArticleVendu> findArticleByNom(String nom);

    List<ArticleVendu> findArticleByNomAndCategorieId(Integer noCategorie, String nom);

    EnchereDTO getDetailsVente(int noArticle);

    List<ArticleVendu> findArticleByEncheresOuvertes();

    List<ArticleVendu> findArticleByEncheresEnCours(Integer no_utilisateur);

    List<ArticleVendu> findArticleByMesVentesEnCours(Integer no_utilisateur);

    List<ArticleVendu> findArticleByVenteNonDebutee();

    List<ArticleVendu> findArticleByVenteTerminee();


}
