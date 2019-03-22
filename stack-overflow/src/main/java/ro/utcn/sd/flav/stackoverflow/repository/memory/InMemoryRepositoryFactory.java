package ro.utcn.sd.flav.stackoverflow.repository.memory;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ro.utcn.sd.flav.stackoverflow.repository.AccountRepository;
import ro.utcn.sd.flav.stackoverflow.repository.RepositoryFactory;

@Component
@ConditionalOnProperty(name = "stackoverflow.repository-type", havingValue = "MEMORY")
public class InMemoryRepositoryFactory implements RepositoryFactory{

    private final InMemoryAccountRepository repository = new InMemoryAccountRepository();

    @Override
    public AccountRepository createAccountRepository() {
        return repository;
    }



}
