package ro.utcn.sd.flav.stackoverflow.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ro.utcn.sd.flav.stackoverflow.entity.ApplicationUser;
import ro.utcn.sd.flav.stackoverflow.repository.AccountRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcAccountRepository implements AccountRepository{

    private final JdbcTemplate template;

    @Override
    public List<ApplicationUser> findAll() {
        return template.query("SELECT * FROM application_user", new AccountMapper());
    }

    @Override
    public ApplicationUser save(ApplicationUser applicationUser) {

        if(applicationUser.getUserId() == null)
            applicationUser.setUserId(insert(applicationUser));
        else
            update(applicationUser);

        return applicationUser;
    }

    @Override
    public void remove(ApplicationUser applicationUser) {

        template.update("DELETE FROM application_user WHERE id = ?", applicationUser.getUserId());
    }

    @Override
    public Optional<ApplicationUser> findById(int id) {
        List<ApplicationUser> users = template.query("SELECT * FROM application_user WHERE user_id = ?", new AccountMapper(), id);
        if(users.isEmpty())
            return Optional.empty();
        else
            return Optional.of(users.get(0));
    }

    private int insert(ApplicationUser applicationUser)
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("application_user");
        insert.usingGeneratedKeyColumns("user_id");
        Map<String, Object> map = new HashMap<>();
        map.put("username", applicationUser.getUsername());
        map.put("password", applicationUser.getPassword());
        map.put("permission", applicationUser.getPermission());
        map.put("status", applicationUser.getStatus());
        map.put("points", applicationUser.getPoints());
        return insert.executeAndReturnKey(map).intValue();
    }

    private void update(ApplicationUser applicationUser)
    {
        template.update("UPDATE application_user SET username = ?, password = ?, permission = ?, status = ?, points = ? WHERE user_id = ?",
                applicationUser.getUsername(), applicationUser.getPassword(), applicationUser.getPermission(), applicationUser.getStatus(), applicationUser.getUserId());
    }
}
