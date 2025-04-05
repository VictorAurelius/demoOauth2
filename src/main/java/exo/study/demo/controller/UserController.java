package exo.study.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import exo.study.demo.dto.response.UserDTOResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<UserDTOResponse> getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.ok(null);
        }

        UserDTOResponse userDTO = UserDTOResponse.builder()
                .name(principal.getAttribute("name"))
                .email(principal.getAttribute("email"))
                .imageUrl(principal.getAttribute("picture"))
                .build();

        return ResponseEntity.ok(userDTO);
    }
}