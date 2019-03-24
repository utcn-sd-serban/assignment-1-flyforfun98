package ro.utcn.sd.flav.stackoverflow.repository.jdbc;

import org.springframework.jdbc.core.RowMapper;
import ro.utcn.sd.flav.stackoverflow.entity.VoteAnswer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VoteAnswerMapper implements RowMapper<VoteAnswer> {

    @Override
    public VoteAnswer mapRow(ResultSet resultSet, int i) throws SQLException {

        return new VoteAnswer(
                resultSet.getInt("vote_id"),
                resultSet.getInt("user_gives_answer_id"),
                resultSet.getInt("user_gets_answer_id"),
                resultSet.getInt("answer_voted_id"),
                resultSet.getBoolean("vote_type"));

    }
}
