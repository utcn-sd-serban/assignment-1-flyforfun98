package ro.utcn.sd.flav.stackoverflow.repository.jdbc;

import org.springframework.jdbc.core.RowMapper;
import ro.utcn.sd.flav.stackoverflow.entity.VoteQuestion;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VoteQuestionMapper implements RowMapper<VoteQuestion> {

    @Override
    public VoteQuestion mapRow(ResultSet resultSet, int i) throws SQLException {

        return new VoteQuestion(
                resultSet.getInt("vote_id"),
                resultSet.getInt("user_gives_id"),
                resultSet.getInt("user_gets_id"),
                resultSet.getInt("question_voted_id"),
                resultSet.getBoolean("vote_type"));

    }
}
