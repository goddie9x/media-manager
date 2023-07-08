package com.god.MediaManager.model.media;

import com.god.MediaManager.model.auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLikedImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imageId")
    private Image image;
}
