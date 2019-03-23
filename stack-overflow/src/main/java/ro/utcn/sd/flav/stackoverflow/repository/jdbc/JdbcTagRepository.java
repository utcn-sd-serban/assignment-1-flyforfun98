package ro.utcn.sd.flav.stackoverflow.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ro.utcn.sd.flav.stackoverflow.entity.Tag;
import ro.utcn.sd.flav.stackoverflow.repository.TagRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcTagRepository implements TagRepository{

    private final JdbcTemplate template;

    @Override
    public List<Tag> findAll() {
        return template.query("SELECT * FROM tag", new TagMapper());
    }

    @Override
    public Tag save(Tag tag) {

        if(tag.getTagId() == null)
            tag.setTagId(insert(tag));
        else
            update(tag);

        return tag;
    }

    @Override
    public void remove(Tag tag) {

        template.update("DELETE FROM tag WHERE tag_id = ?", tag.getTagId());
    }

    @Override
    public Optional<Tag> findById(int id) {
        List<Tag> tags = template.query("SELECT * FROM tag WHERE tag_id = ?", new TagMapper(), id);
        if(tags.isEmpty())
            return Optional.empty();
        else
            return Optional.of(tags.get(0));
    }

    private int insert(Tag tag)
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("tag");
        insert.usingGeneratedKeyColumns("tag_id");
        Map<String, Object> map = new HashMap<>();
        map.put("title", tag.getTitle());
        return insert.executeAndReturnKey(map).intValue();
    }

    private void update(Tag tag)
    {
        template.update("UPDATE tag SET title = ? WHERE tag_id = ?",
                tag.getTitle(), tag.getTagId());
    }
    
}
