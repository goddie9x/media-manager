package com.god.MediaManager.service;

import com.god.MediaManager.model.auth.User;
import com.god.MediaManager.model.media.Image;
import com.god.MediaManager.model.media.UserLikedImage;
import com.god.MediaManager.repository.auth.UserRepository;
import com.god.MediaManager.repository.media.ImageRepository;
import com.god.MediaManager.repository.media.UserLikedImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
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
    public static final String IMAGE_PATH = new File("./public/image/readme.txt").getAbsolutePath();
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
    public void likeImage(User user,long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        try{
            UserLikedImage userLikedImage = new UserLikedImage();
            userLikedImage.setImageId(imageId);
            userLikedImage.setUserId(user.getId());
            userLikedImageRepository.save(userLikedImage);
            image.setLikes(image.getLikes() + 1);
        }
        catch (Exception e){
            UserLikedImage userLikedImageExit = userLikedImageRepository
                    .findUserLikedImageByUserIdAndImageId(user.getId(),imageId);
            userLikedImageRepository.delete(userLikedImageExit);
            image.setLikes(image.getLikes() - 1);
        }
        imageRepository.save(image);
    }
    public List<UserLikedImage> getLikedImagesByUser(long userId) {
        return userLikedImageRepository.findByUserId(userId);
    }
    public List<Image> getListImagesLikedByUser(long userId) {
        List<UserLikedImage> likedImageList = getLikedImagesByUser(userId);
        List<Long> listImageId =
                likedImageList.stream().map(x->x.getImageId()).toList();
        return imageRepository.findAllById(listImageId);
    }

    public Resource retrieveImageFile(Image image) {
        String filePath = IMAGE_PATH + image.getFilename();

        File file = new File(filePath);
        return new FileSystemResource(file);
    }
}

