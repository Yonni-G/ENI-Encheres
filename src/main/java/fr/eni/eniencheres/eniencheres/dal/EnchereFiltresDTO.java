package fr.eni.eniencheres.eniencheres.dal;

public class EnchereFiltresDTO {

    private boolean encheresOuvertes;
    private boolean encheresEnCours;
    private boolean encheresRemportees;

    private boolean ventesEnCours;
    private boolean ventesNonDebutees;
    private boolean ventesTerminees;

    public boolean isEncheresOuvertes() {
        return encheresOuvertes;
    }

    public void setEncheresOuvertes(boolean encheresOuvertes) {
        this.encheresOuvertes = encheresOuvertes;
    }

    public boolean isEncheresEnCours() {
        return encheresEnCours;
    }

    public void setEncheresEnCours(boolean encheresEnCours) {
        this.encheresEnCours = encheresEnCours;
    }

    public boolean isEncheresRemportees() {
        return encheresRemportees;
    }

    public void setEncheresRemportees(boolean encheresRemportees) {
        this.encheresRemportees = encheresRemportees;
    }

    public boolean isVentesEnCours() {
        return ventesEnCours;
    }

    public void setVentesEnCours(boolean ventesEnCours) {
        this.ventesEnCours = ventesEnCours;
    }

    public boolean isVentesNonDebutees() {
        return ventesNonDebutees;
    }

    public void setVentesNonDebutees(boolean ventesNonDebutees) {
        this.ventesNonDebutees = ventesNonDebutees;
    }

    public boolean isVentesTerminees() {
        return ventesTerminees;
    }

    public void setVentesTerminees(boolean ventesTerminees) {
        this.ventesTerminees = ventesTerminees;
    }
}
