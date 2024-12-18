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

    @Override
    public ArticleVendu findArticleById(Integer noArticle) {
        return repository.findArticleById(noArticle);
    }

    @Override
    public List<ArticleVendu> findArticleByCategorieId(Integer noCategorie) {
        return repository.findArticleByCategorieId(noCategorie);
    }

    @Override
    public List<ArticleVendu> findArticleByNom(String nom) {
        return repository.findArticleByNom(nom);
    }

    @Override
    public List<ArticleVendu> findArticleByNomAndCategorieId(Integer noCategorie, String nom){
        return repository.findArticleByNomAndCategorieId(noCategorie, nom);
    }
}
