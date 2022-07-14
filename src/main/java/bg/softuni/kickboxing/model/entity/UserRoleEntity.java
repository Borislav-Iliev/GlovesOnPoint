package bg.softuni.kickboxing.model.entity;

import bg.softuni.kickboxing.model.enums.UserRoleEnum;

import javax.persistence.*;

@Entity
@Table(name = "role")
public class UserRoleEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private UserRoleEnum userRole;

    public UserRoleEntity() {
    }

    public UserRoleEntity(UserRoleEnum userRole) {
        this.userRole = userRole;
    }

    public UserRoleEnum getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRoleEnum userRole) {
        this.userRole = userRole;
    }
}
