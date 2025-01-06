package fr.eni.eniencheres.eniencheres.dal;

import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleVenduRepositoryImpl implements ArticleVenduRepository {

    @Override
    public void ajouter(ArticleVendu articleVendu) {
        // on insere le nouvel article en BDD
        String sql = "INSERT INTO ARTICLES_VENDUS ()";
    }
}
