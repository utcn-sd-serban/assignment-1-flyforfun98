package ro.utcn.sd.flav.stackoverflow.repository.jdbc;

import org.springframework.jdbc.core.RowMapper;
import ro.utcn.sd.flav.stackoverflow.entity.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagMapper implements RowMapper<Tag>{

    @Override
    public Tag mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Tag(
                resultSet.getInt("tag_id"),
                resultSet.getString("title"));

    }
}
