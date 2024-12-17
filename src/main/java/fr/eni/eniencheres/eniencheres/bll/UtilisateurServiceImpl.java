package fr.eni.eniencheres.eniencheres.bll;

import fr.eni.eniencheres.eniencheres.bo.Utilisateur;
import fr.eni.eniencheres.eniencheres.dal.UtilisateurRepository;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurServiceImpl implements UtilisateurService{

    private UtilisateurRepository utilisateurRepository;

    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public void add(Utilisateur utilisateur) {
        try {
            utilisateurRepository.add(utilisateur);
        } catch(IllegalArgumentException e) {

        }
    }
}
