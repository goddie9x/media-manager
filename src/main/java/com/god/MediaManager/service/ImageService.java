package com.god.MediaManager.service;

import com.god.MediaManager.model.auth.User;
import com.god.MediaManager.model.media.Image;
import com.god.MediaManager.repository.auth.UserRepository;
import com.god.MediaManager.repository.media.ImageRepository;
import com.god.MediaManager.repository.media.UserLikedImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserLikedImageRepository userLikedImageRepository;
    private final String IMAGE_PATH = new File("./public/image/readme.txt").getAbsolutePath();
    public Image uploadImage(long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

        String filePath = IMAGE_PATH + filename;
        file.transferTo(new File(filePath));

        Image image = new Image();
        image.setUser(user);
        image.setFilename(filename);
        imageRepository.save(image);
        return image;
    }

    public List<Image> getImagesByUser(long userId) {
        return imageRepository.findByUserId(userId);
    }

    public void deleteImage(long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        userLikedImageRepository.deleteAllByImageId(image.getId());
        String filePath = IMAGE_PATH + image.getFilename();
        try {
            Path fileToDelete = Paths.get(filePath);
            Files.deleteIfExists(fileToDelete);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image file");
        }
        imageRepository.delete(image);
    }

    public Image getImageById(long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }
    public void likeImage(long imageId,long userId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        image.setLikes(image.getLikes() + 1);
        imageRepository.save(image);
    }
    public List<Image> getLikedImagesByUser(long userId) {
        return userLikedImageRepository.findByUserId(userId);
    }

    public Resource retrieveImageFile(Image image) {
        String filePath = IMAGE_PATH + image.getFilename();

        File file = new File(filePath);
        return new FileSystemResource(file);
    }
}
