package ro.utcn.sd.flav.stackoverflow.repository.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ro.utcn.sd.flav.stackoverflow.repository.*;
import ro.utcn.sd.flav.stackoverflow.repository.AnswerRepository;

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

    @Override
    public QuestionRepository createQuestionRepository() {
        return new HibernateQuestionRepository(entityManager);
    }

    @Override
    public TagRepository createTagRepository() {
        return new HibernateTagRepository(entityManager);
    }

    @Override
    public AnswerRepository createAnswerRepository() {
        return new HibernateAnswerRepository(entityManager);
    }

    @Override
    public VoteQuestionRepository createVoteQuestionRepository() {
        return new HibernateVoteQuestionRepository(entityManager);
    }

    @Override
    public VoteAnswerRepository createVoteAnswerRepository() {
        return new HibernateVoteAnswerRepository(entityManager);
    }
}
