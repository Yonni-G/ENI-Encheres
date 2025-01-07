package fr.eni.eniencheres.eniencheres.dal;

import java.time.LocalDateTime;

public class EnchereDTO {

    private int noArticle;
    private int noUtilisateur;
    private String pseudo;
    private String lien_image;
    private String nomArticle;
    private String description;
    private String categorie;
    private int miseAPrix;
    private LocalDateTime dateFinEnchere;
    private String retrait;
    private String vendeur;
    private int montantEnchere;
    private LocalDateTime dateEnchere;

    public EnchereDTO(
            int noArticle,
            String lien_image,
            String nomArticle,
            String description,
            String categorie,
            int miseAPrix,
            LocalDateTime dateFinEnchere,
            String retrait,
            String vendeur,
            int montantEnchere,
            LocalDateTime dateEnchere
    ) {
        this.noArticle = noArticle;
        this.lien_image = lien_image;
        this.nomArticle = nomArticle;
        this.description = description;
        this.categorie = categorie;
        this.miseAPrix = miseAPrix;
        this.dateFinEnchere = dateFinEnchere;
        this.retrait = retrait;
        this.vendeur = vendeur;
        this.montantEnchere = montantEnchere;
        this.dateEnchere = dateEnchere;
    }
    public EnchereDTO(){}

    public EnchereDTO(int noUtilisateur, String pseudo, int montantEnchere) {
        this.noUtilisateur = noUtilisateur;
        this.pseudo = pseudo;
        this.montantEnchere = montantEnchere;
    }

    public EnchereDTO(int noUtilisateur, String pseudo, int montantEnchere, LocalDateTime dateEnchere, LocalDateTime dateFinEncheres) {
        this.noUtilisateur = noUtilisateur;
        this.pseudo = pseudo;
        this.montantEnchere = montantEnchere;
        this.dateEnchere = dateEnchere;
        this.dateFinEnchere = dateFinEncheres;
    }

    public EnchereDTO(int noArticle, String lienImage, String nomArticle, String description, String categorie, int miseAPrix, LocalDateTime dateFinEnchere, String retrait, String vendeur, int noUtilisateur, int montantEnchere, LocalDateTime localDateTime) {
        this.noArticle = noArticle;
        this.lien_image = lienImage;
        this.nomArticle = nomArticle;
        this.description = description;
        this.categorie = categorie;
        this.miseAPrix = miseAPrix;
        this.dateFinEnchere = dateFinEnchere;
        this.retrait = retrait;
        this.vendeur = vendeur;
        this.noUtilisateur = noUtilisateur;
        this.montantEnchere = montantEnchere;
        this.dateEnchere = localDateTime;
    }

    // Getters & Setters

    public int getNoArticle() {
        return noArticle;
    }

    public void setNoArticle(int noArticle) {
        this.noArticle = noArticle;
    }

    public String getLien_image() {
        return lien_image;
    }

    public void setLien_image(String lien_image) {
        this.lien_image = lien_image;
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

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public int getMiseAPrix() {
        return miseAPrix;
    }

    public void setMiseAPrix(int miseAPrix) {
        this.miseAPrix = miseAPrix;
    }

    public LocalDateTime getDateFinEnchere() {
        return dateFinEnchere;
    }

    public void setDateFinEnchere(LocalDateTime dateFinEnchere) {
        this.dateFinEnchere = dateFinEnchere;
    }

    public String getRetrait() {
        return retrait;
    }

    public void setRetrait(String retrait) {
        this.retrait = retrait;
    }

    public String getVendeur() {
        return vendeur;
    }

    public void setVendeur(String vendeur) {
        this.vendeur = vendeur;
    }

    public int getMontantEnchere() {
        return montantEnchere;
    }

    public void setMontantEnchere(int montantEnchere) {
        this.montantEnchere = montantEnchere;
    }

    public LocalDateTime getDateEnchere() {
        return dateEnchere;
    }

    public void setDateEnchere(LocalDateTime dateEnchere) {
        this.dateEnchere = dateEnchere;
    }

    public int getNoUtilisateur() {
        return noUtilisateur;
    }

    public void setNoUtilisateur(int noUtilisateur) {
        this.noUtilisateur = noUtilisateur;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
}
