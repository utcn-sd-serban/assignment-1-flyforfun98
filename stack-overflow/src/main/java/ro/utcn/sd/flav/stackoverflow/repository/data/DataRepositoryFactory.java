package ro.utcn.sd.flav.stackoverflow.repository.data;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ro.utcn.sd.flav.stackoverflow.repository.AccountRepository;
import ro.utcn.sd.flav.stackoverflow.repository.QuestionRepository;
import ro.utcn.sd.flav.stackoverflow.repository.RepositoryFactory;
import ro.utcn.sd.flav.stackoverflow.repository.TagRepository;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "stackoverflow.repository-type", havingValue = "DATA")
public class DataRepositoryFactory implements RepositoryFactory{

    private final DataAccountRepository accountRepository;
    private final DataQuestionRepository questionRepository;
    private final DataTagRepository tagRepository;

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
}
