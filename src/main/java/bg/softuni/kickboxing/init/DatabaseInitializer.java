package bg.softuni.kickboxing.init;

import bg.softuni.kickboxing.service.UserRoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRoleService userRoleService;

    public DatabaseInitializer(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @Override
    public void run(String... args) throws Exception {
        this.userRoleService.seedRoles();
    }
}
