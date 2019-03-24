package ro.utcn.sd.flav.stackoverflow.repository.data;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ro.utcn.sd.flav.stackoverflow.entity.VoteQuestion;
import ro.utcn.sd.flav.stackoverflow.repository.*;
import ro.utcn.sd.flav.stackoverflow.repository.AnswerRepository;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "stackoverflow.repository-type", havingValue = "DATA")
public class DataRepositoryFactory implements RepositoryFactory{

    private final DataAccountRepository accountRepository;
    private final DataQuestionRepository questionRepository;
    private final DataTagRepository tagRepository;
    private final DataAnswerRepository answerRepository;
    private final DataVoteQuestionRepository voteQuestionRepository;
    private final DataVoteAnswerRepository voteAnswerRepository;

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

    @Override
    public VoteQuestionRepository createVoteQuestionRepository() {
        return voteQuestionRepository;
    }

    @Override
    public VoteAnswerRepository createVoteAnswerRepository() {
        return voteAnswerRepository;
    }
}
