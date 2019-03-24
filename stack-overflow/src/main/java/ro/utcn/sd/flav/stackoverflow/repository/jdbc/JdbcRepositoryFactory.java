package ro.utcn.sd.flav.stackoverflow.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ro.utcn.sd.flav.stackoverflow.entity.VoteQuestion;
import ro.utcn.sd.flav.stackoverflow.repository.*;
import ro.utcn.sd.flav.stackoverflow.repository.AnswerRepository;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "stackoverflow.repository-type", havingValue = "JDBC")
public class JdbcRepositoryFactory implements RepositoryFactory{

    private final JdbcTemplate template;

    @Override
    public AccountRepository createAccountRepository() {
        return new JdbcAccountRepository(template);
    }

    @Override
    public QuestionRepository createQuestionRepository() {
        return new JdbcQuestionRepository(template);
    }

    @Override
    public TagRepository createTagRepository() {
        return new JdbcTagRepository(template);
    }

    @Override
    public AnswerRepository createAnswerRepository() {
        return new JdbcAnswerRepository(template);
    }

    @Override
    public VoteQuestionRepository createVoteQuestionRepository() {
        return new JdbcVoteQuestionRepository(template);
    }

    @Override
    public VoteAnswerRepository createVoteAnswerRepository() {
        return new JdbcVoteAnswerRepository(template);
    }


}
