package fr.eni.eniencheres.eniencheres.bll;

import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;
import fr.eni.eniencheres.eniencheres.bo.Enchere;
import fr.eni.eniencheres.eniencheres.bo.Retrait;
import jakarta.validation.Valid;

public interface ArticleVenduService {
    void ajouter(ArticleVendu articleVendu);

    ArticleVendu getById(Integer noArticle);

    boolean modifier(ArticleVendu articleVendu);
}
