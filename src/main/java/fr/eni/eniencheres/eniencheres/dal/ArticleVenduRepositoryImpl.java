package fr.eni.eniencheres.eniencheres.dal;

import fr.eni.eniencheres.eniencheres.bll.UtilisateurService;
import fr.eni.eniencheres.eniencheres.bo.*;
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
        String SqlGetArticle = "SELECT" +
                " a.no_article AS noArticle," +
                " a.nom_article AS nomArticle," +
                " a.description," +
                " a.date_debut_encheres AS dateDebutEncheres," +
                " a.date_fin_encheres AS dateFinEncheres," +
                " a.prix_initial as miseAPrix," +
                " a.prix_vente AS prixVente," +
                " a.no_utilisateur AS noUtilisateur," +
                " a.no_categorie AS noCategorie," +
                " a.etat_vente AS etatVente," +
                " r.rue AS rue," +
                " r.code_postal AS codePostal," +
                " r.ville AS ville" +
                " FROM ARTICLES_VENDUS a" +
                " LEFT JOIN RETRAITS r ON a.no_article = r.no_article" +
                " WHERE a.no_article = :noArticle";

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
            articleVendu.setCategorieArticle(new Categorie(rs.getInt("noCategorie"), ""));
            articleVendu.setEtatVente(rs.getInt("etatVente") == 1);

            // il faut également hydrater notre article avec le le lieu de retrait
            articleVendu.setLieuRetrait(new Retrait(
                    rs.getString("rue"),
                    rs.getString("codePostal"),
                    rs.getString("ville")
            ));

            // Création d'un objet Utilisateur et assignation au vendeur
            Optional<Utilisateur> utilisateur = utilisateurRepository.getUtilisateurById(rs.getInt("noUtilisateur"));
            if(utilisateur.isPresent()) {
                articleVendu.setVendeur(utilisateur.get());  // On affecte l'objet Utilisateur au vendeur
            } else articleVendu.setVendeur(new Utilisateur());  // On affecte l'objet Utilisateur au vendeur



            return articleVendu;
        });
    }

    @Override
    public boolean modifier(ArticleVendu articleVendu) {
        // Déclaration des requêtes SQL avec paramètres nommés

        // Variables pour les paramètres
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("noArticle", articleVendu.getNoArticle());
        updateParams.put("nomArticle", articleVendu.getNomArticle());
        updateParams.put("description", articleVendu.getDescription());
        updateParams.put("dateDebutEncheres", articleVendu.getDateDebutEncheres());
        updateParams.put("dateFinEncheres", articleVendu.getDateFinEncheres());
        updateParams.put("miseAPrix", articleVendu.getMiseAPrix());
        updateParams.put("noCategorie", articleVendu.getCategorieArticle().getNoCategorie());

        try {
            // Exécution de la mise à jour de l'article
            String sqlUpdateArticleWithReturn = "UPDATE ARTICLES_VENDUS SET " +
                    " nom_article = :nomArticle," +
                    " description = :description," +
                    " date_debut_encheres = :dateDebutEncheres," +
                    " date_fin_encheres = :dateFinEncheres," +
                    " prix_initial = :miseAPrix," +
                    " no_categorie = :noCategorie" +
                    " WHERE no_article = :noArticle";

            jdbcTemplate.update(sqlUpdateArticleWithReturn, updateParams);
            // on met également à jour le lieu de retrait
            String sqlUpdateLieuRetrait = "UPDATE RETRAITS SET " +
                    " rue = :rue," +
                    " code_postal = :codePostal," +
                    " ville = :ville";

            jdbcTemplate.update(sqlUpdateLieuRetrait, new BeanPropertySqlParameterSource(articleVendu.getLieuRetrait()));

            return true;


        } catch (Exception e) {
            return false;
        }
    }


}
