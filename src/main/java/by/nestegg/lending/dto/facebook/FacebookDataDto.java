package by.nestegg.lending.dto.facebook;

import lombok.Getter;
import lombok.Setter;
import org.springframework.social.facebook.api.Page;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.Reference;
import org.springframework.social.facebook.api.Video;

@Getter
@Setter
public class FacebookDataDto extends FacebookDto {

    private String birthday;

    private String gender;

    private Reference hometown;

    private Reference location;

    private PagedList<Reference> friends;

    private PagedList<Page> likes;

    private PagedList<Post> photos;

    private PagedList<Post> posts;

    private PagedList<Video> videos;

}
