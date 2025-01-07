package fr.eni.eniencheres.eniencheres.dal;

import fr.eni.eniencheres.eniencheres.bo.Enchere;
import fr.eni.eniencheres.eniencheres.bo.Utilisateur;
import fr.eni.eniencheres.eniencheres.exceptions.UtilisateurExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UtilisateurRepositoryImpl implements UtilisateurRepository {

    private static final Logger logger = LoggerFactory.getLogger(UtilisateurRepositoryImpl.class);
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

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
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Utilisateur.class));
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
    public void encherir(Enchere enchere) {
        String SqlInsertEnchere = "INSERT INTO encheres (no_utilisateur, no_article, date_enchere, montant_enchere)" +
                " VALUES (:noUtilisateur, :noArticle, GETDATE(), :montantEnchere)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("noUtilisateur", enchere.getUtilisateur().getNoUtilisateur());
        params.addValue("noArticle", enchere.getArticleVendu().getNoArticle());
        params.addValue("montantEnchere", enchere.getMontantEnchere());

        // Exécution de la requête avec les paramètres
        namedParameterJdbcTemplate.update(SqlInsertEnchere, params);

    }


}
