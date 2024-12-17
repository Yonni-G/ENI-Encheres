package fr.eni.eniencheres.eniencheres.dal;

import fr.eni.eniencheres.eniencheres.bo.Utilisateur;
import fr.eni.eniencheres.eniencheres.exceptions.UtilisateurExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UtilisateurRepositoryImpl implements UtilisateurRepository {

    private static final Logger log = LoggerFactory.getLogger(UtilisateurRepositoryImpl.class);
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
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

        Utilisateur utilisateur = null;
        //try {
            return namedParameterJdbcTemplate.queryForObject(
                    sql,
                    params,
                    new BeanPropertyRowMapper<>(Utilisateur.class)
            );
//        } catch (EmptyResultDataAccessException e) {
//            throw new UtilisateurExceptions.UtilisateurNonTrouve();
//        }
    }

    @Override
    public List<Utilisateur> findAll() {
        String sql = "SELECT * FROM utilisateurs";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Utilisateur.class));
    }
}
