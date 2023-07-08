package com.god.MediaManager.repository.media;

import com.god.MediaManager.model.media.Image;
import com.god.MediaManager.model.media.UserLikedImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLikedImageRepository extends JpaRepository<UserLikedImage, Long> {
    List<UserLikedImage> findByUserId(Long userId);

    void deleteAllByImageId(Long id);
    UserLikedImage findUserLikedImageByUserIdAndImageId(long userId,long imageId);
}
