package bg.softuni.kickboxing.service;

import bg.softuni.kickboxing.model.dto.user.UserDTO;
import bg.softuni.kickboxing.model.dto.user.UserRegistrationDTO;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.exception.ObjectNotFoundException;
import bg.softuni.kickboxing.model.exception.UsernameNotFoundException;
import bg.softuni.kickboxing.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, UserRoleService userRoleService, ModelMapper mapper, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, EmailService emailService) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.emailService = emailService;
    }

    public void registerAndLogin(UserRegistrationDTO userRegistrationDTO) {
        UserEntity newUser = this.mapper.map(userRegistrationDTO, UserEntity.class);

        newUser.setPassword(this.passwordEncoder.encode(userRegistrationDTO.getPassword()));

        UserRoleEntity userRole = this.userRoleService.getUserRole();
        newUser.setUserRoles(List.of(userRole));

        this.userRepository.save(newUser);
        this.emailService.sendRegistrationEmail(newUser.getEmail(),
                newUser.getFirstName() + " " + newUser.getLastName());
        login(newUser);
    }

    public void login(UserEntity userEntity) {
        UserDetails userDetails =
                this.userDetailsService.loadUserByUsername(userEntity.getEmail());

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.
                getContext().
                setAuthentication(auth);
    }

    public UserDTO getUserDTOByUsername(String username) {
        UserEntity userEntity = this.userRepository.findByUsername(username).orElseThrow();
        return this.mapper.map(userEntity, UserDTO.class);
    }

    public void makeModerator(String username) {
        UserEntity user = this.userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        user.addRole(this.userRoleService.getModeratorRole());
        this.userRepository.save(user);
    }

    public void removeModerator(String username) {
        UserEntity user = this.userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        user.removeModeratorRole();
        this.userRepository.save(user);
    }
}
