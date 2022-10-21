package by.nestegg.lending.controller.view;

import org.springframework.beans.factory.annotation.Value;

public interface UserLikeResultView extends LikeCountView {

    @Value("#{target.is_like}")
    Boolean getIsLike();

}
