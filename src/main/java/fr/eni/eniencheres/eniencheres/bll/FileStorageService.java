package fr.eni.eniencheres.eniencheres.bll;

import fr.eni.eniencheres.eniencheres.configuration.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private final FileStorageProperties properties;

    @Autowired
    public FileStorageService(FileStorageProperties properties) {
        this.properties = properties;
    }

    public String saveFile(MultipartFile file) throws IOException {
        // Obtenir le chemin de stockage
        String uploadDir = properties.getUploadDir();

        // Créer le répertoire s'il n'existe pas
        Path directoryPath = Paths.get(uploadDir);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        String originalFileName = file.getOriginalFilename();
        // Vérifier l'extension
        if (originalFileName == null || !originalFileName.contains(".")) {
            throw new IOException("Nom de fichier invalide.");
        }

        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String baseNom = originalFileName.substring(0, originalFileName.lastIndexOf("."));

        // Construire le chemin complet du fichier
        Path destinationPath = Paths.get(uploadDir + originalFileName);

        // Si le fichier existe, générer un nom
        int count = 1;
        while (Files.exists(destinationPath)) {
            String newFileName = baseNom + "_" + count + extension;
            destinationPath = Paths.get(uploadDir + newFileName);
            count++;
        }

        // Sauvegarder le fichier
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationPath);
        }

        return "/images/imagesUtilisateurs/" + destinationPath.getFileName().toString();
    }
}
