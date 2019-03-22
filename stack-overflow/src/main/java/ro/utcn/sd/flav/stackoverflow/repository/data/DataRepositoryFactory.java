package ro.utcn.sd.flav.stackoverflow.repository.data;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ro.utcn.sd.flav.stackoverflow.repository.AccountRepository;
import ro.utcn.sd.flav.stackoverflow.repository.RepositoryFactory;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "stackoverflow.repository-type", havingValue = "DATA")
public class DataRepositoryFactory implements RepositoryFactory{

    private final DataAccountRepository accountRepository;

    @Override
    public AccountRepository createAccountRepository() {
        return accountRepository;
    }
}
