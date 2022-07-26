package bg.softuni.kickboxing.service;

import bg.softuni.kickboxing.model.dto.user.EditUserDTO;
import bg.softuni.kickboxing.model.dto.user.UserDTO;
import bg.softuni.kickboxing.model.dto.user.UserRegistrationDTO;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;
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
                this.userDetailsService.loadUserByUsername(userEntity.getUsername());

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
        return this.userRepository
                .findByUsername(username)
                .map(u -> this.mapper.map(u, UserDTO.class))
                .orElseThrow();
    }

    public UserDTO getUserDTOById(Long id) {
        return this.userRepository
                .findById(id)
                .map(u -> this.mapper.map(u, UserDTO.class))
                .orElseThrow();
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

    public void editProfile(EditUserDTO editUserModel, Long id) {
        UserEntity userEntity = this.userRepository.findById(id).orElseThrow();

        if (validateField(editUserModel.getUsername(), 2, 20)) {
            userEntity.setUsername(editUserModel.getUsername());
        }

        if (validateField(editUserModel.getFirstName(), 2, 20)) {
            userEntity.setFirstName(editUserModel.getFirstName());
        }

        if (validateField(editUserModel.getLastName(), 2, 20)) {
            userEntity.setLastName(editUserModel.getLastName());
        }

        if (validateField(editUserModel.getPassword(), 3, 20)) {
            userEntity.setPassword(this.passwordEncoder.encode(editUserModel.getPassword()));
        }

        if (!editUserModel.getImageUrl().isBlank()) {
            userEntity.setImageUrl(editUserModel.getImageUrl());
        }

        this.userRepository.save(userEntity);
    }

    private boolean validateField(String field, int minLength, int maxLength) {
        if (field.length() >= minLength && field.length() <= maxLength && !field.isBlank()) {
            return true;
        }
        return false;
    }
}
