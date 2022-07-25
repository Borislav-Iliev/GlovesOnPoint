package bg.softuni.kickboxing.service;


import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.user.GlovesOnPointUserDetails;
import bg.softuni.kickboxing.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class GlovesOnPointUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public GlovesOnPointUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository
                .findByUsername(username)
                .map(this::mapToGlovesOnPointUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + "not found!"));
    }

    private UserDetails mapToGlovesOnPointUserDetails(UserEntity userEntity) {
        return new GlovesOnPointUserDetails(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getImageUrl(),
                userEntity.getPosts(),
                userEntity.getComments(),
                userEntity
                        .getUserRoles()
                        .stream()
                        .map(this::mapToGrantedAuthority)
                        .toList()
        );
    }

    private GrantedAuthority mapToGrantedAuthority(UserRoleEntity userRole) {
        return new SimpleGrantedAuthority("ROLE_" + userRole.getUserRole().name());
    }
}
