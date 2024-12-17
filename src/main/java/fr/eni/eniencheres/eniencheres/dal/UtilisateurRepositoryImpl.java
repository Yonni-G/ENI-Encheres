package fr.eni.eniencheres.eniencheres.dal;

import fr.eni.eniencheres.eniencheres.bo.Utilisateur;
import fr.eni.eniencheres.eniencheres.exceptions.UtilisateurExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class UtilisateurRepositoryImpl implements UtilisateurRepository {

    private static final Logger log = LoggerFactory.getLogger(UtilisateurRepositoryImpl.class);
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UtilisateurRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void add(Utilisateur utilisateur) {
        log.debug(utilisateur.toString());
        utilisateur.setAdministrateur(false);
        utilisateur.setCredit(0);

        String sql = "INSERT INTO utilisateurs (pseudo, nom, prenom, email, telephone, rue, code_Postal, ville, mot_de_passe) VALUES (:pseudo, :nom, :prenom, :email, :telephone, :rue, :codePostal, :ville, :motDePasse)";
        try {
            namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(utilisateur));
        }catch (DuplicateKeyException e) {
            // Vérification du message pour savoir si c'est le pseudo ou l'email
            if (e.getMessage().contains("unique_pseudo")) {
                throw new UtilisateurExceptions.PseudoDejaExistant();
            } else if (e.getMessage().contains("unique_email")) {
                throw new UtilisateurExceptions.EmailDejaExistant();
            }
            // Si l'exception concerne une autre colonne ou une autre contrainte
            throw e; // Propager l'exception originale
        }
    }
}
