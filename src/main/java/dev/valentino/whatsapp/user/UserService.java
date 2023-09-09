package dev.valentino.whatsapp.user;

import dev.valentino.whatsapp.user.exception.UserNotFoundException;
import dev.valentino.whatsapp.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public WapUser getSelfUser() throws UserNotFoundException {
        return userRepository
                .findById(UserUtil.getIdFromContext())
                .orElseThrow(UserNotFoundException::new);
    }

    public WapUser getUserById(@NonNull UUID userId) throws UserNotFoundException {
        return userRepository
                .findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    public WapUser getUserByUsername(String username) throws UserNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    public List<WapUser> getUsersByUsername(String username) {
        WapUser excludedUser = UserUtil.getUserFromContext(); // don't show user searching themselves
        return userRepository.searchUsersByUsername(username, excludedUser);
    }

    public List<UserDTO> searchUsersByUsername(String username) {
        WapUser excludedUser = UserUtil.getUserFromContext(); // don't show user searching themselves
        return userRepository.searchUsersByUsername(username, excludedUser)
                .stream()
                .map(WapUser::toDTO)
                .toList();
    }

    @Transactional
    public UserDTO updateUser(UserUpdateRequest request) {
        WapUser user = getUserById(UserUtil.getIdFromContext());
        String username = request.username();
        String fullName = request.fullName();

        if (username != null && userRepository.findByUsername(username).isEmpty()) {
            user.setUsername(username);
        }
        if (fullName != null) {
            user.setFullName(fullName);
        }
        return userRepository.save(user).toDTO();
    }

    @Transactional
    public UserDTO updateUserAvatar(MultipartFile file) throws IOException {
        WapUser user = getUserById(UserUtil.getIdFromContext());
        user.setAvatar(file.getBytes());
        return userRepository.save(user).toDTO();
    }
}
