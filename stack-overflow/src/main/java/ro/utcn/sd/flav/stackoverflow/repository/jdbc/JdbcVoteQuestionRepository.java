package ro.utcn.sd.flav.stackoverflow.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ro.utcn.sd.flav.stackoverflow.entity.VoteQuestion;
import ro.utcn.sd.flav.stackoverflow.repository.VoteQuestionRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcVoteQuestionRepository implements VoteQuestionRepository {

    private final JdbcTemplate template;

    @Override
    public List<VoteQuestion> findAll() {
        return template.query("SELECT * FROM vote_question", new VoteQuestionMapper());
    }

    @Override
    public VoteQuestion save(VoteQuestion voteQuestion) {

        if(voteQuestion.getVoteId() == null)
            voteQuestion.setVoteId(insert(voteQuestion));
        else
            update(voteQuestion);

        return voteQuestion;
    }

    @Override
    public void remove(VoteQuestion voteQuestion) {

        template.update("DELETE FROM vote_question WHERE vote_id = ?", voteQuestion.getVoteId());
    }

    @Override
    public Optional<VoteQuestion> findById(int id) {
        List<VoteQuestion> voteQuestions = template.query("SELECT * FROM vote_question WHERE vote_id = ?", new VoteQuestionMapper(), id);
        if(voteQuestions.isEmpty())
            return Optional.empty();
        else
            return Optional.of(voteQuestions.get(0));
    }

    private int insert(VoteQuestion voteQuestion)
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("vote_question");
        insert.usingGeneratedKeyColumns("vote_id");
        Map<String, Object> map = new HashMap<>();
        map.put("user_gives_id", voteQuestion.getUserGivesId());
        map.put("user_gets_id", voteQuestion.getUserGetsId());
        map.put("question_voted_id", voteQuestion.getQuestionVotedId());
        map.put("vote_type", voteQuestion.isVoteType());
        return insert.executeAndReturnKey(map).intValue();
    }

    private void update(VoteQuestion voteQuestion)
    {
        template.update("UPDATE vote_question SET vote_type = ? WHERE vote_id = ?",
                voteQuestion.isVoteType(), voteQuestion.getVoteId());
    }

    @Override
    public Optional<VoteQuestion> findVoteForQuestion(Integer userId, Integer questionId) {

        List<VoteQuestion> votes = template.query("SELECT * FROM vote_question WHERE user_gives_id = ? AND question_voted_id = ?", new VoteQuestionMapper(), userId, questionId);
        if(votes.isEmpty())
            return Optional.empty();
        else
            return Optional.of(votes.get(0));
    }

    @Override
    public List<VoteQuestion> findAllVotesOfQuestion(Integer questionId) {

        return template.query("SELECT * FROM vote_question WHERE question_voted_id = ?", new VoteQuestionMapper(), questionId);
    }
}
