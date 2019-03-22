package ro.utcn.sd.flav.stackoverflow.repository;

public interface RepositoryFactory {

    AccountRepository createAccountRepository();

    // Here add other repositories;
}
