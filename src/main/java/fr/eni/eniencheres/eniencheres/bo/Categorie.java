package fr.eni.eniencheres.eniencheres.bo;

import java.util.Objects;

public class Categorie {
    private int noCategorie;
    private String libelle;

    public Categorie(int noCategorie, String libelle) {
        this.noCategorie = noCategorie;
        this.libelle = libelle;
    }

    public int getNoCategorie() {
        return noCategorie;
    }

    public void setNoCategorie(int noCategorie) {
        this.noCategorie = noCategorie;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Categorie{");
        sb.append("noCategorie=").append(noCategorie);
        sb.append(", libelle='").append(libelle).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Categorie categorie = (Categorie) o;
        return noCategorie == categorie.noCategorie && Objects.equals(libelle, categorie.libelle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noCategorie, libelle);
    }
}
