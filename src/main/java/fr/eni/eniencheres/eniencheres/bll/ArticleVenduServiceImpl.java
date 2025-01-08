package fr.eni.eniencheres.eniencheres.bll;

import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;
import fr.eni.eniencheres.eniencheres.bo.Enchere;
import fr.eni.eniencheres.eniencheres.bo.Retrait;
import fr.eni.eniencheres.eniencheres.dal.ArticleVenduRepository;
import org.springframework.stereotype.Service;

@Service
public class ArticleVenduServiceImpl implements ArticleVenduService {
    private ArticleVenduRepository articleVenduRepository;

    public ArticleVenduServiceImpl(ArticleVenduRepository articleVenduRepository) {
        this.articleVenduRepository = articleVenduRepository;
    }

    @Override
    public void ajouter(ArticleVendu articleVendu) {
        articleVenduRepository.ajouter(articleVendu);
    }

    @Override
    public ArticleVendu getById(Integer noArticle) {
        return articleVenduRepository.getById(noArticle);
    }


}
