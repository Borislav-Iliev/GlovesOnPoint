package bg.softuni.kickboxing.service;


import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.user.KickboxingUserDetails;
import bg.softuni.kickboxing.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class KickboxingUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public KickboxingUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository
                .findByUsername(username)
                .map(this::mapToKickboxingUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + "not found!"));
    }

    private UserDetails mapToKickboxingUserDetails(UserEntity userEntity) {
        return new KickboxingUserDetails(
                userEntity.getUsername(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getEmail(),
                userEntity.getPassword(),
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
