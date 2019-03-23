package ro.utcn.sd.flav.stackoverflow.repository.memory;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ro.utcn.sd.flav.stackoverflow.repository.*;

@Component
@ConditionalOnProperty(name = "stackoverflow.repository-type", havingValue = "MEMORY")
public class InMemoryRepositoryFactory implements RepositoryFactory{

    private final InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();
    private final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository();
    private final InMemoryTagRepository tagRepository = new InMemoryTagRepository();
    private final InMemoryAnswerRepository answerRepository = new InMemoryAnswerRepository();

    @Override
    public AccountRepository createAccountRepository() {
        return accountRepository;
    }

    @Override
    public QuestionRepository createQuestionRepository() {
        return questionRepository;
    }

    @Override
    public TagRepository createTagRepository() {
        return tagRepository;
    }

    @Override
    public AnswerRepository createAnswerRepository() {
        return answerRepository;
    }


}
