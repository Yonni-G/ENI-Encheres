package fr.eni.eniencheres.eniencheres.bll;

import fr.eni.eniencheres.eniencheres.bo.Utilisateur;
import fr.eni.eniencheres.eniencheres.dal.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurServiceImpl implements UtilisateurService{

    private UtilisateurRepository utilisateurRepository;
    //private final PasswordEncoder passwordEncoder;

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

    @Override
    public List<Utilisateur> findAll() {
        return utilisateurRepository.findAll();
    }
}
