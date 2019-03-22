package ro.utcn.sd.flav.stackoverflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "application_user")
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserPermission permission;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    private int points;

    public ApplicationUser(String username, String password, UserPermission permission, UserStatus status, int points) {
        this.username = username;
        this.password = password;
        this.permission = permission;
        this.status = status;
        this.points = points;
    }
}
