package fr.eni.eniencheres.eniencheres.exceptions;

public class UtilisateurExceptions {

    // Exception pour l'email déjà existant
    public static class EmailDejaExistant extends RuntimeException {
        public EmailDejaExistant() {
            super("L'email est déjà utilisé.");
        }
    }

    // Exception pour le pseudo déjà existant
    public static class PseudoDejaExistant extends RuntimeException {
        public PseudoDejaExistant() {
            super("Le pseudo est déjà utilisé.");
        }
    }


    // Exception pour un utilisateur inexistant
    public static class UtilisateurNonTrouve extends RuntimeException {
        public UtilisateurNonTrouve() {
            super("Mot de passe et/ou pseudo incorrects !!");
        }
    }

}

