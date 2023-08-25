package dev.valentino.whatsapp.user;

import dev.valentino.whatsapp.user.exception.UserNotFoundException;
import dev.valentino.whatsapp.user.update.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public WapUser findUserById(UUID userId) throws UserNotFoundException {
        return userRepository
                .findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    public WapUser findUserByUsername(String username) throws UserNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    public List<WapUser> findUsersByUsernameContaining(String username) {
        return userRepository.findUsersByUsernameContaining(username);
    }

    public void updateUser(UUID userId, UpdateUserRequest request) throws UserNotFoundException {
        WapUser user = findUserById(userId);
        String fullName = request.fullName();
        String profilePicture = request.profilePicture();

        if (fullName != null) {
            user.setFullName(fullName);
        }
        if (profilePicture != null) {
            user.setProfilePicture(profilePicture);
        }
        userRepository.save(user);
    }
}
