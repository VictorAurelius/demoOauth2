package exo.study.demo.service.implement;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import exo.study.demo.domain.User;
import exo.study.demo.repository.UserRepository;
import exo.study.demo.service.inter.UserOAuth2Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserOAuth2ServiceImpl implements UserOAuth2Service {

    private final UserRepository userRepository;
    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        String email = getEmail(oAuth2User);
        if (!StringUtils.hasText(email)) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> registerNewUser(oAuth2UserRequest, oAuth2User));

        updateExistingUser(user, oAuth2User);
        return oAuth2User;
    }

    private String getEmail(OAuth2User oAuth2User) {
        return oAuth2User.getAttribute("email");
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        User user = User.builder()
                .name(oAuth2User.getAttribute("name"))
                .email(getEmail(oAuth2User))
                .imageUrl(oAuth2User.getAttribute("picture"))
                .provider(oAuth2UserRequest.getClientRegistration().getRegistrationId())
                .providerId(oAuth2User.getName())
                .emailVerified(true)
                .build();
        return userRepository.save(user);
    }

    private void updateExistingUser(User user, OAuth2User oAuth2User) {
        user.setName(oAuth2User.getAttribute("name"));
        user.setImageUrl(oAuth2User.getAttribute("picture"));
        userRepository.save(user);
    }

    @Override
    public void processOAuth2PostLogin(String email) {
        User existUser = userRepository.findByEmail(email).orElse(null);
        if (existUser != null) {
            existUser.setEmailVerified(true);
            userRepository.save(existUser);
        }
    }
}