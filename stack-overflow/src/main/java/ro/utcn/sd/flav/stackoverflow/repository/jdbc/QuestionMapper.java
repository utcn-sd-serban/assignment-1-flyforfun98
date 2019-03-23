package ro.utcn.sd.flav.stackoverflow.repository.jdbc;

import org.springframework.jdbc.core.RowMapper;
import ro.utcn.sd.flav.stackoverflow.entity.Question;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionMapper implements RowMapper<Question> {
    @Override
    public Question mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Question(
                resultSet.getInt("question_id"),
                resultSet.getInt("author_id"),
                resultSet.getString("title"),
                resultSet.getString("text"),
                resultSet.getDate("creation_date"),
                resultSet.getInt("score"));
    }
}
