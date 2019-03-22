package ro.utcn.sd.flav.stackoverflow.repository.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ro.utcn.sd.flav.stackoverflow.repository.AccountRepository;
import ro.utcn.sd.flav.stackoverflow.repository.RepositoryFactory;

import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "stackoverflow.repository-type", havingValue = "JPA")
public class HibernateRepositoryFactory implements RepositoryFactory{
    private final EntityManager entityManager;

    @Override
    public AccountRepository createAccountRepository() {
        return new HibernateAccountRepository(entityManager);
    }
}
