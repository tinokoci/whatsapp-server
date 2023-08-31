package dev.valentino.whatsapp.user;

import dev.valentino.whatsapp.user.update.UpdateUserRequest;
import dev.valentino.whatsapp.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello from protected User route");
    }

    @GetMapping("/search/{username}")
    public ResponseEntity<List<WapUser>> searchUsers(@PathVariable("username") String username) {
        List<WapUser> users = userService.searchUsers(username);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateUser(@RequestBody UpdateUserRequest request) {
        WapUser user = userService.findUserByUsername(UserUtil.getUsernameFromContext());
        userService.updateUser(user.getId(), request);
        return ResponseEntity.ok().build();
    }
}
