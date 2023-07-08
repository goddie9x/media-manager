package com.god.MediaManager.controller.image;

import com.god.MediaManager.DTO.ImageResponse;
import com.god.MediaManager.model.auth.User;
import com.god.MediaManager.model.media.Image;
import com.god.MediaManager.model.media.UserLikedImage;
import com.god.MediaManager.service.AuthService;
import com.god.MediaManager.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;
    private final AuthService authService;

    @PostMapping("/upload")
    public ResponseEntity uploadImage( @RequestParam("file") MultipartFile file) {
        try {
            User crrUser = authService.getCurrentUser();
            Image image = imageService.uploadImage(crrUser.getId(),file);
            return ResponseEntity.ok(image);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload image failed");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserImages(@PathVariable Long userId) {
        List<Image> images = imageService.getImagesByUser(userId);
        List<ImageResponse> lisImage = images.stream().map(
                x->ImageResponse.fromImage(x)).toList();

        return ResponseEntity.ok(lisImage);
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity deleteImage(@PathVariable Long imageId) {
        try{
            imageService.deleteImage(imageId);
            return ResponseEntity.ok("Delete image success");
        }
        catch (Exception e){
            return ResponseEntity.status(500).body("Delete image failed");
        }
    }

    @GetMapping("/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) {
        Image image = imageService.getImageById(imageId);

        Resource file = imageService.retrieveImageFile(image);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFilename() + "\"")
                .body(file);
    }

    @PostMapping("/like/{imageId}")
    public ResponseEntity likeImage(@PathVariable Long imageId) {
        try{
            User crrUser = authService.getCurrentUser();
            imageService.likeImage(crrUser,imageId);
            return ResponseEntity.ok("Like image success");
        }
        catch (Exception e){
            return ResponseEntity.status(500).body("Like image failed");
        }
    }

    @GetMapping("/user/liked/{userId}")
    public ResponseEntity<?> getUserLikedImages(@PathVariable Long userId) {
        List<Image> likedImages = imageService.getListImagesLikedByUser(userId);
        List<ImageResponse> lisImage = likedImages.stream().map(
                x->ImageResponse.fromImage(x)).toList();

        return ResponseEntity.ok(lisImage);
    }
}
