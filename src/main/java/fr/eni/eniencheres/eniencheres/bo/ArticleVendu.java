package fr.eni.eniencheres.eniencheres.bo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

public class ArticleVendu {
    private int noArticle;

    @NotEmpty
    @Size(min = 8, max = 30, message = "Le nom de l'article doit être entre 8 et 30 caractères")
    private String nomArticle;

    @NotEmpty
    @Size(min = 10, max = 50, message = "Le nom de l'article doit être entre 8 et 30 caractères")
    private String description;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateDebutEncheres;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateFinEncheres;

    @Min(value = 100)
    @Max(value = 100000)
    private int miseAPrix;

    private Integer prixVente;
    private boolean etatVente;
    private String lien_image;
    private Utilisateur acheteur;
    private Utilisateur vendeur;

    @Valid
    private Retrait lieuRetrait;

    private Categorie categorieArticle;

    public ArticleVendu() {
    }

    public ArticleVendu(int noArticle, String nomArticle, String description, LocalDateTime dateDebutEncheres, LocalDateTime dateFinEncheres, int miseAPrix, int prixVente, boolean etatVente, String lien_image, Utilisateur acheteur, Utilisateur vendeur, Retrait lieuRetrait, Categorie categorieArticle) {
        this.noArticle = noArticle;
        this.nomArticle = nomArticle;
        this.description = description;
        this.dateDebutEncheres = dateDebutEncheres;
        this.dateFinEncheres = dateFinEncheres;
        this.miseAPrix = miseAPrix;
        this.prixVente = prixVente;
        this.etatVente = etatVente;
        this.lien_image = lien_image;
        this.acheteur = acheteur;
        this.vendeur = vendeur;
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

    public Utilisateur getAcheteur() {
        return acheteur;
    }

    public void setAcheteur(Utilisateur acheteur) {
        this.acheteur = acheteur;
    }

    public Utilisateur getVendeur() {
        return vendeur;
    }

    public void setVendeur(Utilisateur vendeur) {
        this.vendeur = vendeur;
    }

    public Retrait getLieuRetrait() {
        return lieuRetrait;
    }

    public void setLieuRetrait(Retrait lieuRetrait) {
        this.lieuRetrait = lieuRetrait;
    }

    public Categorie getCategorieArticle() {
        return categorieArticle;
    }

    public void setCategorieArticle(Categorie categorieArticle) {
        this.categorieArticle = categorieArticle;
    }

    public String getLien_image() {
        return lien_image;
    }

    public void setLien_image(String lien_image) {
        this.lien_image = lien_image;
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
        sb.append(", lien_image='").append(lien_image).append('\'');
        sb.append(", acheteur=").append(acheteur);
        sb.append(", vendeur=").append(vendeur);
        sb.append(", lieuRetrait=").append(lieuRetrait);
        sb.append(", categorieArticle=").append(categorieArticle);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ArticleVendu that = (ArticleVendu) o;
        return noArticle == that.noArticle && miseAPrix == that.miseAPrix && prixVente == that.prixVente && etatVente == that.etatVente && Objects.equals(nomArticle, that.nomArticle) && Objects.equals(description, that.description) && Objects.equals(dateDebutEncheres, that.dateDebutEncheres) && Objects.equals(dateFinEncheres, that.dateFinEncheres) && Objects.equals(lien_image, that.lien_image) && Objects.equals(acheteur, that.acheteur) && Objects.equals(vendeur, that.vendeur) && Objects.equals(lieuRetrait, that.lieuRetrait) && Objects.equals(categorieArticle, that.categorieArticle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noArticle, nomArticle, description, dateDebutEncheres, dateFinEncheres, miseAPrix, prixVente, etatVente, lien_image, acheteur, vendeur, lieuRetrait, categorieArticle);
    }
}

