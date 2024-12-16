package fr.eni.eniencheres.eniencheres.bo;

import java.time.LocalDateTime;
import java.util.Objects;

public class Enchere {
    private LocalDateTime dateEnchere;
    private int montantEnchere;
    private Utilisateur utilisateur;

    public Enchere(LocalDateTime dateEnchere, int montantEnchere, Utilisateur utilisateur) {
        this.dateEnchere = dateEnchere;
        this.montantEnchere = montantEnchere;
        this.utilisateur = utilisateur;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Enchere{");
        sb.append("dateEnchere=").append(dateEnchere);
        sb.append(", montantEnchere=").append(montantEnchere);
        sb.append(", utilisateur=").append(utilisateur);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Enchere enchere = (Enchere) o;
        return montantEnchere == enchere.montantEnchere && Objects.equals(dateEnchere, enchere.dateEnchere) && Objects.equals(utilisateur, enchere.utilisateur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateEnchere, montantEnchere, utilisateur);
    }
}
