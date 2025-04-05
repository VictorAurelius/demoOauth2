package exo.study.demo.service.inter;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface UserOAuth2Service extends OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Override
    OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException;

    void processOAuth2PostLogin(String username);
}