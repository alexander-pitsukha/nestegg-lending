package by.nestegg.lending.entity;

import by.nestegg.lending.entity.data.FacebookData;
import by.nestegg.lending.entity.data.InstagramData;
import by.nestegg.lending.entity.data.LinkedInData;
import by.nestegg.lending.entity.data.VkontakteData;
import by.nestegg.lending.entity.domain.AbstractTimestamp;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Entity
@Table(name = "user_social_data")
public class UserSocialData extends AbstractTimestamp {

    @Column(name = "facebook_id", unique = true)
    private String facebookId;

    @Column(name = "linkedin_id", unique = true)
    private String linkedinId;

    @Column(name = "instagram_id", unique = true)
    private String instagramId;

    @Column(name = "vkontakte_id", unique = true)
    private String vkontakteId;

    @Column(name = "facebook_token", length = 600)
    private String facebookToken;

    @Column(name = "linkedin_token", length = 600)
    private String linkedinToken;

    @Column(name = "instagram_token", length = 600)
    private String instagramToken;

    @Column(name = "vkontakte_token", length = 600)
    private String vkontakteToken;

    @Type(type = "jsonb")
    @Column(name = "facebook_data", columnDefinition = "jsonb")
    private FacebookData facebookData;

    @Type(type = "jsonb")
    @Column(name = "linked_data", columnDefinition = "jsonb")
    private LinkedInData linkedinData;

    @Type(type = "jsonb")
    @Column(name = "instagram_data", columnDefinition = "jsonb")
    private InstagramData instagramData;

    @Type(type = "jsonb")
    @Column(name = "vkontakte_data", columnDefinition = "jsonb")
    private VkontakteData vkontakteData;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false, updatable = false)
    private User user;

}
