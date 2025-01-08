package fr.eni.eniencheres.eniencheres.dal;

import fr.eni.eniencheres.eniencheres.bo.Enchere;
import fr.eni.eniencheres.eniencheres.bo.Utilisateur;
import fr.eni.eniencheres.eniencheres.exceptions.UtilisateurExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UtilisateurRepositoryImpl implements UtilisateurRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UtilisateurRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void add(Utilisateur utilisateur) {
        //log.debug(utilisateur.toString());
        utilisateur.setAdministrateur(false);
        utilisateur.setCredit(0);

        String sql = "INSERT INTO utilisateurs (pseudo, nom, prenom, email, telephone, rue, code_Postal, ville, mot_de_passe) VALUES (:pseudo, :nom, :prenom, :email, :telephone, :rue, :codePostal, :ville, :motDePasse)";
        //try {
            namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(utilisateur));
//        } catch (DuplicateKeyException e) {
//            // Vérification du message pour savoir si c'est le pseudo ou l'email
//            if (e.getMessage().contains("unique_pseudo")) {
//                throw new UtilisateurExceptions.PseudoDejaExistant();
//            } else if (e.getMessage().contains("unique_email")) {
//                throw new UtilisateurExceptions.EmailDejaExistant();
//            }
//            // Si l'exception concerne une autre colonne ou une autre contrainte
//            throw e; // Propager l'exception originale
//        }
    }

    @Override
    public Utilisateur getUtilisateur(String pseudo) {

        String sql = "SELECT * FROM utilisateurs WHERE pseudo = :pseudo";
        // Paramètres à passer à la requête
        Map<String, Object> params = new HashMap<>();
        params.put("pseudo", pseudo);

        return namedParameterJdbcTemplate.queryForObject(
                sql,
                params,
                new BeanPropertyRowMapper<>(Utilisateur.class)
        );
    }

    @Override
    public Optional<Utilisateur> getUtilisateurById(int noUtilisateur) {
        String sql = "SELECT * FROM utilisateurs WHERE no_utilisateur = :noUtilisateur";
        // Paramètres à passer à la requête
        Map<String, Object> params = new HashMap<>();
        params.put("noUtilisateur", noUtilisateur);

        return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(
                sql,
                params,
                new BeanPropertyRowMapper<>(Utilisateur.class)
        ));
    }

    @Override
    public List<Utilisateur> findAll() {
        String sql = "SELECT * FROM utilisateurs";
        return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Utilisateur.class));
    }

    @Override
    public void update(Utilisateur utilisateur) {

        String sql = "UPDATE utilisateurs" +
                " SET nom = :nom, prenom = :prenom, email = :email, telephone = :telephone, rue = :rue, code_postal = :codePostal, ville = :ville, mot_de_passe = :motDePasse" +
                " WHERE no_utilisateur = :noUtilisateur";
        namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(utilisateur));
    }

    @Override
    public void delete(String pseudo) {
        // avant de faire la requete de suppression, on s'assure que l'utilisateur existe
        Utilisateur utilisateurExiste;
        utilisateurExiste = getUtilisateur(pseudo);
        if (utilisateurExiste == null) {
            throw new UtilisateurExceptions.UtilisateurNonTrouve();
        }
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("pseudo", pseudo); // Ajouter la valeur pour le paramètre "pseudo"

        String sql = "DELETE FROM utilisateurs WHERE pseudo = :pseudo";
        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    @Transactional
    public void encherir(Enchere enchere) {

        // Déclarer des variables pour stocker les données de la meilleure enchère actuelle
        int noUtilisateur = 0;
        int montantEnchere = 0;

        // Requête SQL pour récupérer le no_utilisateur et la meilleure enchère
        String sqlMontantMeilleureEnchere = "SELECT TOP 1 no_utilisateur, montant_enchere " +
                "FROM encheres WHERE no_article = :noArticle " +
                "ORDER BY montant_enchere DESC";

        // Paramètres pour la requête
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("noArticle", enchere.getArticleVendu().getNoArticle());

        // Exécution de la requête et récupération des résultats
        try {
            // Récupération des données de la meilleure enchère
            Map<String, Object> result = namedParameterJdbcTemplate.queryForMap(sqlMontantMeilleureEnchere, params);

            // Récupération des données du Map
            noUtilisateur = (Integer) result.get("no_utilisateur");
            montantEnchere = (Integer) result.get("montant_enchere");

            //System.out.println("Meilleure enchère: Utilisateur " + noUtilisateur + " avec " + montantEnchere + " points.");

        } catch (EmptyResultDataAccessException e) {
            // Si aucune enchère n'est trouvée, on passe à l'étape suivante (aucun changement de crédit à faire)
            //System.out.println("Aucune enchère trouvée.");
        }

        // Insérer la nouvelle enchère
        String sqlInsertEnchere = "INSERT INTO encheres (no_utilisateur, no_article, date_enchere, montant_enchere) " +
                "VALUES (:noUtilisateur, :noArticle, GETDATE(), :montantEnchere)";

        MapSqlParameterSource params2 = new MapSqlParameterSource();
        params2.addValue("noUtilisateur", enchere.getUtilisateur().getNoUtilisateur());
        params2.addValue("noArticle", enchere.getArticleVendu().getNoArticle());
        params2.addValue("montantEnchere", enchere.getMontantEnchere());

        // Exécution de l'insertion de la nouvelle enchère
        if (namedParameterJdbcTemplate.update(sqlInsertEnchere, params2) == 1) {

            // Débiter directement le crédit du nouvel enchérisseur (sans récupération préalable)
            String sqlDebitUtilisateur = "UPDATE utilisateurs SET credit = credit - :montantEnchere WHERE no_utilisateur = :noUtilisateur";
            MapSqlParameterSource updateParams = new MapSqlParameterSource();
            updateParams.addValue("montantEnchere", enchere.getMontantEnchere());
            updateParams.addValue("noUtilisateur", enchere.getUtilisateur().getNoUtilisateur());

            // Exécution de la mise à jour pour débiter le crédit
            namedParameterJdbcTemplate.update(sqlDebitUtilisateur, updateParams);

            // Si une meilleure enchère existait, on recrédite le crédit du précédent meilleur enchérisseur
            if (noUtilisateur != 0) {  // Vérifie s'il y a eu un précédent meilleur enchérisseur
                String sqlCreditUtilisateur = "UPDATE utilisateurs SET credit = credit + :montantEnchere WHERE no_utilisateur = :noUtilisateur";
                MapSqlParameterSource updateParams2 = new MapSqlParameterSource();
                updateParams2.addValue("montantEnchere", montantEnchere);
                updateParams2.addValue("noUtilisateur", noUtilisateur);

                // Exécution de la mise à jour pour recréditer le précédent enchérisseur
                namedParameterJdbcTemplate.update(sqlCreditUtilisateur, updateParams2);
            }
        }
    }
}
