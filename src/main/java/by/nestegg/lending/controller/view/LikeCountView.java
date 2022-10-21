package by.nestegg.lending.controller.view;

import org.springframework.beans.factory.annotation.Value;

public interface LikeCountView {

    @Value("#{target.like_count}")
    int getLikeCount();

    @Value("#{target.dislike_count}")
    int getDislikeCount();

}
