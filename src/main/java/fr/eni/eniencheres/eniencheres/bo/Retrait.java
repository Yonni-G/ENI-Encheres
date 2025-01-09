package fr.eni.eniencheres.eniencheres.bo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

public class Retrait {
    private int noArticle;
    //TODO définir les contraintes exactes de validation

    @NotNull
    @NotEmpty
    private String rue;

    @NotNull
    @NotEmpty
    @Length(min = 5, max = 5, message = "Le code postal doit contenir 5 caractères.")
    private String codePostal;

    @NotNull
    @NotEmpty
    private String ville;

    public Retrait() {
    }

    public Retrait(int noArticle, String rue, String codePostal, String ville) {
        this.noArticle = noArticle;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
    }

    public Retrait(String rue, String codePostal, String ville) {
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
    }

    public int getNoArticle() {
        return noArticle;
    }

    public void setNoArticle(int noArticle) {
        this.noArticle = noArticle;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    @Override
    public String toString() {
        return "Retrait{" +
                "noArticle=" + noArticle +
                ", rue='" + rue + '\'' +
                ", codePostal='" + codePostal + '\'' +
                ", ville='" + ville + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Retrait retrait = (Retrait) o;
        return noArticle == retrait.noArticle && Objects.equals(rue, retrait.rue) && Objects.equals(codePostal, retrait.codePostal) && Objects.equals(ville, retrait.ville);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noArticle, rue, codePostal, ville);
    }
}
