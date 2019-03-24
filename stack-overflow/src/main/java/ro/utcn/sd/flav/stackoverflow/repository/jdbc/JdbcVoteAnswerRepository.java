package ro.utcn.sd.flav.stackoverflow.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ro.utcn.sd.flav.stackoverflow.entity.VoteAnswer;
import ro.utcn.sd.flav.stackoverflow.repository.VoteAnswerRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcVoteAnswerRepository implements VoteAnswerRepository{

    private final JdbcTemplate template;

    @Override
    public List<VoteAnswer> findAll() {
        return template.query("SELECT * FROM vote_answer", new VoteAnswerMapper());
    }

    @Override
    public VoteAnswer save(VoteAnswer voteAnswer) {

        if(voteAnswer.getVoteId() == null)
            voteAnswer.setVoteId(insert(voteAnswer));
        else
            update(voteAnswer);

        return voteAnswer;
    }

    @Override
    public void remove(VoteAnswer voteAnswer) {

        template.update("DELETE FROM vote_answer WHERE vote_id = ?", voteAnswer.getVoteId());
    }

    @Override
    public Optional<VoteAnswer> findById(int id) {
        List<VoteAnswer> voteAnswers = template.query("SELECT * FROM vote_answer WHERE vote_id = ?", new VoteAnswerMapper(), id);
        if(voteAnswers.isEmpty())
            return Optional.empty();
        else
            return Optional.of(voteAnswers.get(0));
    }

    private int insert(VoteAnswer voteAnswer)
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("vote_answer");
        insert.usingGeneratedKeyColumns("vote_id");
        Map<String, Object> map = new HashMap<>();
        map.put("user_gives_answer_id", voteAnswer.getUserGivesAnswerId());
        map.put("user_gets_answer_id", voteAnswer.getUserGetsAnswerId());
        map.put("answer_voted_id", voteAnswer.getAnswerVotedId());
        map.put("vote_type", voteAnswer.isVoteType());
        return insert.executeAndReturnKey(map).intValue();
    }

    private void update(VoteAnswer voteAnswer)
    {
        template.update("UPDATE vote_answer SET vote_type = ? WHERE vote_id = ?",
                voteAnswer.isVoteType(), voteAnswer.getVoteId());
    }

    @Override
    public Optional<VoteAnswer> findVoteForAnswer(Integer userId, Integer answerId) {

        List<VoteAnswer> votes = template.query("SELECT * FROM vote_answer WHERE user_gives_answer_id = ? AND answer_voted_id = ?", new VoteAnswerMapper(), userId, answerId);
        if(votes.isEmpty())
            return Optional.empty();
        else
            return Optional.of(votes.get(0));
    }

    @Override
    public List<VoteAnswer> findAllVotesOfAnswer(Integer answerId) {

        return template.query("SELECT * FROM vote_answer WHERE answer_voted_id = ?", new VoteAnswerMapper(), answerId);
    }
}
