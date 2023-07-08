package com.god.MediaManager.DTO;

import com.god.MediaManager.model.media.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ImageResponse {
    private Long id;
    private String filename;
    private int likes;
    public static ImageResponse fromImage(Image image){
        return new ImageResponse(image.getId(), image.getFilename(), image.getLikes());
    }
}
