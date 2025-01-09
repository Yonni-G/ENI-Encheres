package fr.eni.eniencheres.eniencheres.dal;

import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;
import fr.eni.eniencheres.eniencheres.bo.Categorie;

import java.util.List;

public interface EnchereRepository {
    public List<Categorie> findAllCategories();

    public List<ArticleVendu> findAllArticleVendu();

    // Récupérer les détails d'un article en fonction de l'id
    ArticleVendu findArticleById(Integer noArticle);

    List<ArticleVendu> findArticleByCategorieId(Integer noCategorie);

    // Récupérer la liste des articles contenant le nom "[%nom%]"
    List<ArticleVendu> findArticleByNom(String nom);

    // Récupérer la liste des articles en fonction du nom et de la catégorie
    List<ArticleVendu> findArticleByNomAndCategorieId(Integer noCategorie, String nom);

    // --------------------- Méthodes FILTRES pour tous les utilisateurs ------------------------------------------------------------------------
    // Récupérer la liste des articles en fonction de 'Enchères ouvertes ?'
    List<ArticleVendu> findArticleByEncheresOuvertes();

    // Récupérer la liste des articles en fonction de 'Enchères en cours ?'
    List<ArticleVendu> findArticleByEncheresEnCours(Integer no_utilisateur);

    // TODO: Récupérer la liste des articles en fonction de 'Enchères remportees ?'
    List<ArticleVendu> findArticleByEncheresRemportees(Integer no_utilisateur);

    // Récupérer la liste des articles en fonction de 'Ventes en cours ?'
    List<ArticleVendu> findArticleByMesVentesEnCours(Integer no_utilisateur);

    // Récupérer la liste des articles en fonction de 'Vente non débutées ?'
    List<ArticleVendu> findArticleByVenteNonDebutee();

    // Récupérer la liste des articles en fonction de 'Vente non débutées ?'
    List<ArticleVendu> findArticleByVenteTerminee();

    // Jointure entre les différentes classes pour récupérer les données nécessaires
    EnchereDTO getDetailsVente(int noArticle);

    EnchereDTO getUtilEnchere(int noArticle);

    // Méthode pour récupérer le vainqueur de l'enchère
    EnchereDTO getWinner(int noArticle);
}
