package ro.utcn.sd.flav.stackoverflow.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ro.utcn.sd.flav.stackoverflow.repository.AccountRepository;
import ro.utcn.sd.flav.stackoverflow.repository.RepositoryFactory;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "stackoverflow.repository-type", havingValue = "JDBC")
public class JdbcRepositoryFactory implements RepositoryFactory{

    private final JdbcTemplate template;

    @Override
    public AccountRepository createAccountRepository() {
        return new JdbcAccountRepository(template);
    }
}
