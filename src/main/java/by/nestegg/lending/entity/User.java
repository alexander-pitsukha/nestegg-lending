package by.nestegg.lending.entity;

import by.nestegg.lending.entity.converter.RoleConverter;
import by.nestegg.lending.entity.domain.AbstractTimestamp;
import by.nestegg.lending.entity.enums.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
public class User extends AbstractTimestamp {

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Column(name = "device_token", unique = true, length = 500)
    private String deviceToken;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "verified_phone_code")
    private String verifiedPhoneCode;

    @Column(name = "disabled", nullable = false)
    private Boolean disabled;

    @Column(name = "push_active", nullable = false)
    private Boolean pushActive;

    @Column(name = "automatic_debiting")
    private Boolean automaticDebiting;

    @Column(name = "avatar_id")
    private String avatarId;

    @Convert(converter = RoleConverter.class)
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"))
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BankCard> bankCards = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private UserSocialData userSocialData;

}
