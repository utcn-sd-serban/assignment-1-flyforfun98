package ro.utcn.sd.flav.stackoverflow.repository.jdbc;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ro.utcn.sd.flav.stackoverflow.entity.Question;
import ro.utcn.sd.flav.stackoverflow.entity.Tag;
import ro.utcn.sd.flav.stackoverflow.repository.QuestionRepository;

import java.util.*;

@AllArgsConstructor
public class JdbcQuestionRepository implements QuestionRepository{

    private final JdbcTemplate template;

    @Override
    public List<Question> findAll() {
        return template.query("SELECT * FROM question ORDER BY creation_date ASC", new QuestionMapper());
    }

    @Override
    public Question save(Question question) {

        if(question.getQuestionId() == null) {
            question.setQuestionId(insert(question));
            question.getTags().forEach(t -> template.update("INSERT INTO question_tags VALUES (?, ?)", question.getQuestionId(), t.getTagId()));
        }
        else
            update(question);

        return question;
    }

    @Override
    public void remove(Question question) {

        template.update("DELETE FROM question WHERE question_id = ?", question.getQuestionId());
    }

    @Override
    public Optional<Question> findById(int id) {
        List<Question> questions = template.query("SELECT * FROM question WHERE question_id = ?", new QuestionMapper(), id);
        if(questions.isEmpty())
            return Optional.empty();
        else {

            List<Tag> tags = findTagsByQuestion(questions.get(0));
            questions.get(0).setTags(tags);
            return Optional.of(questions.get(0));
        }
    }

    @Override
    public List<Tag> findTagsByQuestion(Question question) {
        List<Tag>  tags = template.query("SELECT tag.* FROM tag JOIN question_tags ON " +
                "tag.tag_id = question_tags.tag_id_fk WHERE question_tags.question_id_fk1 = ?", new TagMapper(), question.getQuestionId());

        return tags;
    }



    private int insert(Question question)
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("question");
        insert.usingGeneratedKeyColumns("question_id");
        Map<String, Object> map = new HashMap<>();
        map.put("author_id", question.getAuthorId());
        map.put("title", question.getTitle());
        map.put("text", question.getText());
        map.put("creation_date", question.getCreationDate());
        map.put("score", question.getScore());

       return insert.executeAndReturnKey(map).intValue();

    }

    private void update(Question question)
    {
        template.update("UPDATE question SET author_id = ?, title = ?, text = ?, creation_date = ?, score = ? WHERE question_id = ?",
               question.getAuthorId(), question.getTitle(), question.getText(),
                     question.getCreationDate(), question.getScore(), question.getQuestionId());
    }
}
