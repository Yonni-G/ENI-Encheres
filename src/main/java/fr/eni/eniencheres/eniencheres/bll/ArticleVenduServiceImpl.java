package fr.eni.eniencheres.eniencheres.bll;

import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;
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

    @Override
    public boolean modifier(ArticleVendu articleVendu) {
        return articleVenduRepository.modifier(articleVendu);
    }

    @Override
    public void desactiver(ArticleVendu articleVendu) {
        articleVenduRepository.desactiver(articleVendu);
    }


}
