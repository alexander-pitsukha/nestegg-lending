package by.nestegg.lending.security;

import by.nestegg.lending.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {

    private final User user;
    private final Collection<? extends GrantedAuthority> grantedAuthorities;

    public UserDetailsImpl(User user) {
        this(user, Collections.emptyList());
    }

    public UserDetailsImpl(User user, Collection<? extends GrantedAuthority> grantedAuthorities) {
        this.user = user;
        this.grantedAuthorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return user.getDeviceToken();
    }

    @Override
    public String getUsername() {
        return user.getDeviceToken();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return user;
    }

}
