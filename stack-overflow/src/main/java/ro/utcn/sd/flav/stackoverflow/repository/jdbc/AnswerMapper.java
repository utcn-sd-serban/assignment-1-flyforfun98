package ro.utcn.sd.flav.stackoverflow.repository.jdbc;


import org.springframework.jdbc.core.RowMapper;
import ro.utcn.sd.flav.stackoverflow.entity.Answer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnswerMapper implements RowMapper<Answer> {

    @Override
    public Answer mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Answer(
                resultSet.getInt("answer_id"),
                resultSet.getInt("author_id_fk"),
                resultSet.getInt("question_id_fk"),
                resultSet.getString("text"),
                resultSet.getDate("creation_date"),
                resultSet.getInt("score"));

    }
}
