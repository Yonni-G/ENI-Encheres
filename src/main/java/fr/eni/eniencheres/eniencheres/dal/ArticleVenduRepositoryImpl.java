package fr.eni.eniencheres.eniencheres.dal;

import fr.eni.eniencheres.eniencheres.bll.UtilisateurService;
import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;
import fr.eni.eniencheres.eniencheres.bo.Enchere;
import fr.eni.eniencheres.eniencheres.bo.Retrait;
import fr.eni.eniencheres.eniencheres.bo.Utilisateur;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ArticleVenduRepositoryImpl implements ArticleVenduRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UtilisateurRepository utilisateurRepository;

    public ArticleVenduRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate, UtilisateurRepository utilisateurRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.utilisateurRepository = utilisateurRepository;
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
        params.put("lien_image", articleVendu.getLien_image());

        try {
            // Exécution de l'insertion de l'article
            String sqlInsertArticleWithReturn = "INSERT INTO ARTICLES_VENDUS (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, no_utilisateur, no_categorie, lien_image) " +
                    "VALUES (:nomArticle, :description, :dateDebutEncheres, :dateFinEncheres, :miseAPrix, :noUtilisateur, :noCategorie, :lien_image); " +
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

    @Override
    public ArticleVendu getById(int noArticle) {
        String SqlGetArticle = "SELECT no_article AS noArticle," +
                " nom_article AS nomArticle," +
                " description," +
                " date_debut_encheres AS dateDebutEncheres," +
                " date_fin_encheres AS dateFinEncheres," +
                " prix_initial as miseAPrix," +
                " prix_vente AS prixVente," +
                " no_utilisateur AS noUtilisateur," +
                " no_categorie AS noCategorie" +
                " FROM ARTICLES_VENDUS WHERE no_article = :noArticle";
        Map<String, Object> params = new HashMap<>();
        params.put("noArticle", noArticle);
        return jdbcTemplate.queryForObject(SqlGetArticle, params, (rs, rowNum) -> {
            ArticleVendu articleVendu = new ArticleVendu();
            articleVendu.setNoArticle(rs.getInt("noArticle"));
            articleVendu.setNomArticle(rs.getString("nomArticle"));
            articleVendu.setDescription(rs.getString("description"));
            articleVendu.setDateDebutEncheres(rs.getTimestamp("dateDebutEncheres").toLocalDateTime());
            articleVendu.setDateFinEncheres(rs.getTimestamp("dateFinEncheres").toLocalDateTime());
            articleVendu.setMiseAPrix(rs.getInt("miseAPrix"));
            articleVendu.setPrixVente(rs.getInt("prixVente"));

            System.out.println("num:"+rs.getInt("noUtilisateur"));
            // Création d'un objet Utilisateur et assignation au vendeur
            Optional<Utilisateur> utilisateur = utilisateurRepository.getUtilisateurById(rs.getInt("noUtilisateur"));
            if(utilisateur.isPresent()) {
                articleVendu.setVendeur(utilisateur.get());  // On affecte l'objet Utilisateur au vendeur
            } else articleVendu.setVendeur(new Utilisateur());  // On affecte l'objet Utilisateur au vendeur


            return articleVendu;
        });
    }


}
