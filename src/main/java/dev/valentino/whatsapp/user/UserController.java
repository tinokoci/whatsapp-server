package dev.valentino.whatsapp.user;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID userId) {
        UserDTO user = userService.getUserById(userId).toDTO();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/self")
    public ResponseEntity<UserDTO> getSelfUser() {
        UserDTO user = userService.getSelfUser().toDTO();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<List<WapUser>> searchUsersByQuery(@PathVariable("query") String query) {
        List<WapUser> users = userService.searchUsersByUsername(query);
        return ResponseEntity.ok(users);
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserUpdateRequest request) {
        UserDTO user = userService.updateUser(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO> uploadFile(@RequestParam MultipartFile file) throws IOException {
        UserDTO user = userService.updateAvatar(file);
        return ResponseEntity.ok(user);
    }
}
