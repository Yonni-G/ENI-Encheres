package fr.eni.eniencheres.eniencheres.bll;

import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;
import fr.eni.eniencheres.eniencheres.bo.Categorie;
import fr.eni.eniencheres.eniencheres.dal.EnchereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnchereServiceImpl implements EnchereService {

    @Autowired
    EnchereRepository repository;

    @Override
    public List<Categorie> findAllCategories() {
        return repository.findAllCategories();
    }

    @Override
    public List<ArticleVendu> findAllArticleVendu() {
        return repository.findAllArticleVendu();
    }
}
