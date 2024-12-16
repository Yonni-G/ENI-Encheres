package fr.eni.eniencheres.eniencheres.bo;

import java.time.LocalDateTime;
import java.util.Objects;

public class ArticleVendu {
    private int noArticle;
    private String nomArticle;
    private String description;
    private LocalDateTime dateDebutEncheres;
    private LocalDateTime dateFinEncheres;
    private int miseAPrix;
    private int prixVente;
    private boolean etatVente;
    private Utilisateur utilisateur;
    private Categorie categorie;
    private Retrait lieuRetrait;
    private Categorie categorieArticle;

    public ArticleVendu(int noArticle, String nomArticle, String description, LocalDateTime dateDebutEncheres, LocalDateTime dateFinEncheres, int miseAPrix, int prixVente, boolean etatVente, Utilisateur utilisateur, Categorie categorie, Retrait lieuRetrait, Categorie categorieArticle) {
        this.noArticle = noArticle;
        this.nomArticle = nomArticle;
        this.description = description;
        this.dateDebutEncheres = dateDebutEncheres;
        this.dateFinEncheres = dateFinEncheres;
        this.miseAPrix = miseAPrix;
        this.prixVente = prixVente;
        this.etatVente = etatVente;
        this.utilisateur = utilisateur;
        this.categorie = categorie;
        this.lieuRetrait = lieuRetrait;
        this.categorieArticle = categorieArticle;
    }

    public int getNoArticle() {
        return noArticle;
    }

    public void setNoArticle(int noArticle) {
        this.noArticle = noArticle;
    }

    public String getNomArticle() {
        return nomArticle;
    }

    public void setNomArticle(String nomArticle) {
        this.nomArticle = nomArticle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateDebutEncheres() {
        return dateDebutEncheres;
    }

    public void setDateDebutEncheres(LocalDateTime dateDebutEncheres) {
        this.dateDebutEncheres = dateDebutEncheres;
    }

    public LocalDateTime getDateFinEncheres() {
        return dateFinEncheres;
    }

    public void setDateFinEncheres(LocalDateTime dateFinEncheres) {
        this.dateFinEncheres = dateFinEncheres;
    }

    public int getMiseAPrix() {
        return miseAPrix;
    }

    public void setMiseAPrix(int miseAPrix) {
        this.miseAPrix = miseAPrix;
    }

    public int getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(int prixVente) {
        this.prixVente = prixVente;
    }

    public boolean isEtatVente() {
        return etatVente;
    }

    public void setEtatVente(boolean etatVente) {
        this.etatVente = etatVente;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Retrait getLieuRetrait() {
        return lieuRetrait;
    }

    public void setLieuRetrait(Retrait lieuRetrait) {
        this.lieuRetrait = lieuRetrait;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ArticleVendu{");
        sb.append("noArticle=").append(noArticle);
        sb.append(", nomArticle='").append(nomArticle).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", dateDebutEncheres=").append(dateDebutEncheres);
        sb.append(", dateFinEncheres=").append(dateFinEncheres);
        sb.append(", miseAPrix=").append(miseAPrix);
        sb.append(", prixVente=").append(prixVente);
        sb.append(", etatVente=").append(etatVente);
        sb.append(", utilisateur=").append(utilisateur);
        sb.append(", categorie=").append(categorie);
        sb.append(", lieuRetrait=").append(lieuRetrait);
        sb.append(", categorieArticle=").append(categorieArticle);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ArticleVendu that = (ArticleVendu) o;
        return noArticle == that.noArticle && miseAPrix == that.miseAPrix && prixVente == that.prixVente && etatVente == that.etatVente && Objects.equals(nomArticle, that.nomArticle) && Objects.equals(description, that.description) && Objects.equals(dateDebutEncheres, that.dateDebutEncheres) && Objects.equals(dateFinEncheres, that.dateFinEncheres) && Objects.equals(utilisateur, that.utilisateur) && Objects.equals(categorie, that.categorie) && Objects.equals(lieuRetrait, that.lieuRetrait) && Objects.equals(categorieArticle, that.categorieArticle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noArticle, nomArticle, description, dateDebutEncheres, dateFinEncheres, miseAPrix, prixVente, etatVente, utilisateur, categorie, lieuRetrait, categorieArticle);
    }
}

