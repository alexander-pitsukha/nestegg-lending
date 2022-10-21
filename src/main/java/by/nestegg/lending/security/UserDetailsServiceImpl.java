package by.nestegg.lending.security;

import by.nestegg.lending.repository.UserRepository;
import by.nestegg.lending.util.Constants;
import by.nestegg.lending.util.MessageCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserCache userCache;
    private final MessageCodeUtil messageCodeUtil;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String name) {
        var userDetails = userCache.getUserFromCache(name);
        if (userDetails == null) {
            userDetails = Optional.ofNullable(userRepository.findByDeviceToken(name)).map(user ->
                            new UserDetailsImpl(user, user.getRoles()))
                    .orElseThrow(() -> new UsernameNotFoundException(messageCodeUtil
                            .getFullErrorMessageByBundleCode(Constants.ERROR_MSG_AUTH_INVALID_CREDENTIALS)));
            userCache.putUserInCache(userDetails);
        }
        return userDetails;
    }

}
