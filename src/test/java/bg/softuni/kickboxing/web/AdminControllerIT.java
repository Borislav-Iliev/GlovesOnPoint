package bg.softuni.kickboxing.web;

import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.exception.UsernameNotFoundException;
import bg.softuni.kickboxing.repository.UserRepository;
import bg.softuni.kickboxing.repository.UserRoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    private UserEntity adminEntity;

    private UserEntity moderatorEntity;

    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        this.adminEntity = initAdmin(this.userRoleRepository.findAll().get(2));
        this.moderatorEntity = initModerator(this.userRoleRepository.findAll().get(0), this.userRoleRepository.findAll().get(1));
        this.userEntity = initUser(this.userRoleRepository.findAll().get(0));
    }

    @AfterEach
    public void tearDown() {
        this.userRepository.deleteAll();
    }

    public UserEntity initAdmin(UserRoleEntity adminRole) {
        UserEntity testUserEntity = new UserEntity()
                .setUsername("TestUsername")
                .setFirstName("Test")
                .setLastName("Test")
                .setEmail("test@example.com")
                .setPassword("testPassword")
                .setAge(20)
                .setImageUrl("image:/url")
                .setUserRoles(List.of(adminRole));
        testUserEntity.setId(1L);

        this.userRepository.save(testUserEntity);
        return testUserEntity;
    }

    public UserEntity initModerator(UserRoleEntity userRole, UserRoleEntity moderatorRole) {
        UserEntity testUserEntity = new UserEntity()
                .setUsername("TestUsername")
                .setFirstName("Test")
                .setLastName("Test")
                .setEmail("test@example.com")
                .setPassword("testPassword")
                .setAge(20)
                .setImageUrl("image:/url")
                .setUserRoles(List.of(userRole, moderatorRole));
        testUserEntity.setId(1L);

        this.userRepository.save(testUserEntity);
        return testUserEntity;
    }

    public UserEntity initUser(UserRoleEntity userRole) {
        UserEntity testUserEntity = new UserEntity()
                .setUsername("User")
                .setFirstName("Test")
                .setLastName("Test")
                .setEmail("test2@example.com")
                .setPassword("testPassword")
                .setAge(20)
                .setImageUrl("image:/url")
                .setUserRoles(List.of(userRole));
        testUserEntity.setId(2L);

        this.userRepository.save(testUserEntity);
        return testUserEntity;
    }

    @Test
    @WithMockUser(username = "TestUsername", roles = "ADMIN")
    public void testAdminPageShownForAdminUser() throws Exception {
        this.mockMvc.perform(get("/admin/moderators"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-moderators"));
    }

    @Test
    @WithMockUser(username = "TestUsername", roles = "ADMIN")
    public void testAdminPageSearchingForUser_ValidUsername() throws Exception {
        this.mockMvc.perform(post("/admin/moderators")
                        .param("query", "User")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/moderators/User"));
    }

    @Test
    @WithMockUser(username = "TestUsername", roles = "ADMIN")
    public void testAdminPageSearchingForUser_InvalidUsername() throws Exception {
        this.mockMvc.perform(post("/admin/moderators")
                        .param("query", "WrongUsername")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("searchUserModel", "org.springframework.validation.BindingResult.searchUserModel"))
                .andExpect(redirectedUrl("/admin/moderators"));
    }

    @Test
    @WithMockUser(username = "TestUsername", roles = "ADMIN")
    public void testFoundUserPageShownForAdminUser() throws Exception {
        this.mockMvc.perform(get("/admin/moderators/{query}", this.userEntity.getUsername()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("admin-moderators"));
    }

    @Test
    @WithMockUser(username = "TestUsername", roles = "ADMIN")
    public void testMakeModeratorShouldRedirectToAdminPageAndAddModeratorRole() throws Exception {
        this.mockMvc.perform(get("/admin/moderators/make/{username}", this.userEntity.getUsername()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/moderators"));
    }

    @Test
    @WithMockUser(username = "TestUsername", roles = "ADMIN")
    public void testRemoveModeratorShouldRedirectToAdminPageAndRemoveModeratorRole() throws Exception {
        this.mockMvc.perform(get("/admin/moderators/remove/{username}", this.moderatorEntity.getUsername()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/moderators"));
    }

    @Test
    @WithMockUser(username = "TestUsername", roles = "ADMIN")
    public void testSearchForUserShouldThrowExceptionForInvalidUsername() throws Exception {
        this.mockMvc.perform(get("/admin/moderators/{query}", "WrongUsername"))
                .andExpect(status().isOk())
                .andExpect(view().name("username-not-found"));
    }
}
