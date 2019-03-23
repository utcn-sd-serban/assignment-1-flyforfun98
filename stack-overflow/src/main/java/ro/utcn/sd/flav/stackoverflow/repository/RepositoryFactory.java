package ro.utcn.sd.flav.stackoverflow.repository;

public interface RepositoryFactory {

    AccountRepository createAccountRepository();

    QuestionRepository createQuestionRepository();

    TagRepository createTagRepository();

    // Here add other repositories;
}
