package fr.eni.eniencheres.eniencheres.bo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Objects;

public class Enchere {
    private LocalDateTime dateEnchere;

    @Min(value = 100, message = "Le montant de l'enchère doit être supérieur ou égal à 100.")
    private int montantEnchere;

    private Utilisateur utilisateur;
    private ArticleVendu articleVendu;

    public Enchere() {
    }

    public Enchere(LocalDateTime dateEnchere, int montantEnchere, Utilisateur utilisateur, ArticleVendu articleVendu) {
        this.dateEnchere = dateEnchere;
        this.montantEnchere = montantEnchere;
        this.utilisateur = utilisateur;
        this.articleVendu = articleVendu;
    }

    public LocalDateTime getDateEnchere() {
        return dateEnchere;
    }

    public void setDateEnchere(LocalDateTime dateEnchere) {
        this.dateEnchere = dateEnchere;
    }

    public int getMontantEnchere() {
        return montantEnchere;
    }

    public void setMontantEnchere(int montantEnchere) {
        this.montantEnchere = montantEnchere;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public ArticleVendu getArticleVendu() {
        return articleVendu;
    }

    public void setArticleVendu(ArticleVendu articleVendu) {
        this.articleVendu = articleVendu;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Enchere{");
        sb.append("dateEnchere=").append(dateEnchere);
        sb.append(", montantEnchere=").append(montantEnchere);
        sb.append(", utilisateur=").append(utilisateur);
        sb.append(", articleVendu=").append(articleVendu);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Enchere enchere = (Enchere) o;
        return montantEnchere == enchere.montantEnchere && Objects.equals(dateEnchere, enchere.dateEnchere) && Objects.equals(utilisateur, enchere.utilisateur) && Objects.equals(articleVendu, enchere.articleVendu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateEnchere, montantEnchere, utilisateur, articleVendu);
    }
}
