package by.nestegg.lending.controller;

import by.nestegg.lending.AbstractTests;
import by.nestegg.lending.authencation.jwt.JwtTokenUtil;
import by.nestegg.lending.configuration.HttpConfiguration;
import by.nestegg.lending.entity.User;
import by.nestegg.lending.security.UserDetailsImpl;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@MockBean(JpaMetamodelMappingContext.class)
@Import(HttpConfiguration.class)
abstract class BasicControllerTests extends AbstractTests {

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    void setUp() throws Exception {
        User user = getObjectFromJson("user/user_1.json", User.class);
        given(jwtTokenUtil.extractUsername(anyString())).willReturn("admin");
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(new UserDetailsImpl(user));
        given(jwtTokenUtil.validateToken(anyString(), any(UserDetails.class))).willReturn(true);
    }

    HttpHeaders httpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(UUID.randomUUID().toString());
        return httpHeaders;
    }

    String asJsonString(Object obj) {
        try {
            return getObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
