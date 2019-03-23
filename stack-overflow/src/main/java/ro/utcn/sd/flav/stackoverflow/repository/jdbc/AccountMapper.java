package ro.utcn.sd.flav.stackoverflow.repository.jdbc;


import org.springframework.jdbc.core.RowMapper;
import ro.utcn.sd.flav.stackoverflow.entity.ApplicationUser;
import ro.utcn.sd.flav.stackoverflow.entity.UserPermission;
import ro.utcn.sd.flav.stackoverflow.entity.UserStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountMapper implements RowMapper<ApplicationUser> {
    @Override
    public ApplicationUser mapRow(ResultSet resultSet, int i) throws SQLException {
        return new ApplicationUser(
                resultSet.getInt("user_id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                UserPermission.valueOf(resultSet.getString("permission")),
                UserStatus.valueOf(resultSet.getString("status")),
                resultSet.getInt("points"));
    }
}
