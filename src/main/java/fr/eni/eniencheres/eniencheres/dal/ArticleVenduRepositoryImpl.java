package fr.eni.eniencheres.eniencheres.dal;

import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;
import fr.eni.eniencheres.eniencheres.bo.Retrait;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ArticleVenduRepositoryImpl implements ArticleVenduRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ArticleVenduRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void ajouter(ArticleVendu articleVendu) {
        // Déclaration des requêtes SQL avec paramètres nommés

        // Variables pour les paramètres
        Map<String, Object> params = new HashMap<>();
        params.put("nomArticle", articleVendu.getNomArticle());
        params.put("description", articleVendu.getDescription());
        params.put("dateDebutEncheres", articleVendu.getDateDebutEncheres());
        params.put("dateFinEncheres", articleVendu.getDateFinEncheres());
        params.put("miseAPrix", articleVendu.getMiseAPrix());
        params.put("noUtilisateur", articleVendu.getVendeur().getNoUtilisateur());
        params.put("noCategorie", articleVendu.getCategorieArticle().getNoCategorie());

        try {
            // Exécution de l'insertion de l'article
            String sqlInsertArticleWithReturn = "INSERT INTO ARTICLES_VENDUS (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, no_utilisateur, no_categorie) " +
                    "VALUES (:nomArticle, :description, :dateDebutEncheres, :dateFinEncheres, :miseAPrix, :noUtilisateur, :noCategorie); " +
                    "SELECT SCOPE_IDENTITY();";

            // Récupération de l'ID généré
            Number generatedKey = jdbcTemplate.queryForObject(sqlInsertArticleWithReturn, params, Number.class);

            // Vérification de la clé générée
            if (generatedKey != null) {
                int noArticle = generatedKey.intValue();
                System.out.println("L'ID généré pour l'article est : " + noArticle);

                // Assigner la valeur générée à l'objet articleVendu
                articleVendu.setNoArticle(noArticle);

                // Mettre à jour le retrait avec le noArticle
                articleVendu.getLieuRetrait().setNoArticle(noArticle);

                // Insertion de l'adresse de retrait avec la clé générée
                String sqlInsertRetrait = "INSERT INTO RETRAITS (no_article, rue, code_postal, ville) " +
                        "VALUES (:noArticle, :rue, :codePostal, :ville)";

                // Paramètres pour l'insertion du retrait
                Map<String, Object> retraitParams = new HashMap<>();
                retraitParams.put("noArticle", articleVendu.getNoArticle());
                retraitParams.put("rue", articleVendu.getLieuRetrait().getRue());
                retraitParams.put("codePostal", articleVendu.getLieuRetrait().getCodePostal());
                retraitParams.put("ville", articleVendu.getLieuRetrait().getVille());

                // Exécution de l'insertion du retrait
                jdbcTemplate.update(sqlInsertRetrait, retraitParams);
            } else {
                throw new RuntimeException("Échec de la récupération de l'ID généré pour l'article.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'insertion de l'article et du retrait", e);
        }
    }
}
