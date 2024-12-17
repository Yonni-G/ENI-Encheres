//package fr.eni.eniencheres.eniencheres.security;
//
//
//import fr.eni.eniencheres.eniencheres.bll.UtilisateurService;
//import fr.eni.eniencheres.eniencheres.bo.Utilisateur;
//import fr.eni.eniencheres.eniencheres.exceptions.UtilisateurExceptions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserDetailServiceImpl implements UserDetailsService {
//
//    private UtilisateurService utilisateurService;
//    private PasswordEncoder passwordEncoder;
//
//    public UserDetailServiceImpl(UtilisateurService utilisateurService, PasswordEncoder passwordEncoder) {
//        this.utilisateurService = utilisateurService;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override
//    /**
//     * est appelée a chaque connexion utilisateur
//     * username : login saisi par l'utilisateur
//     */
//    public UserDetails loadUserByUsername(String username) {
//
//        // on recupere l'utilisateur s'il existe
//        Utilisateur utilisateur =  UtilisateurService.getUtilisateur(username);
//        String encodedPassword = passwordEncoder.encode(utilisateur.getPassword());
//
//        if(utilisateur != null) {
//            UserDetails user = User.builder()
//                    .username(utilisateur.getUsername())
//                    .password(encodedPassword)
//                    .roles(utilisateur.getRole())
//                    .build();
//
//            return user;
//        } else throw new UtilisateurExceptions.UtilisateurNonTrouve();
//    }
//
////			User.builder()
////			.username(username)
////			.password("popo") // a aller cherche en bdd
////			.roles("USER", "ADMIN") // a aller chercher en bdd
////			.build();
//
//    //return user;
//
//}
//
