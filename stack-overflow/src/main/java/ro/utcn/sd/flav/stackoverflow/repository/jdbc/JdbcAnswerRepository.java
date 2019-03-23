package ro.utcn.sd.flav.stackoverflow.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ro.utcn.sd.flav.stackoverflow.entity.Answer;
import ro.utcn.sd.flav.stackoverflow.repository.AnswerRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcAnswerRepository implements AnswerRepository {
    
    private final JdbcTemplate template;

    @Override
    public List<Answer> findAll() {
        return template.query("SELECT * FROM answer", new AnswerMapper());
    }

    @Override
    public Answer save(Answer answer) {

        if(answer.getAnswerId() == null)
            answer.setAnswerId(insert(answer));
        else
            update(answer);

        return answer;
    }

    @Override
    public void remove(Answer answer) {

        template.update("DELETE FROM answer WHERE answer_id = ?", answer.getAnswerId());
    }

    @Override
    public Optional<Answer> findById(int id) {
        List<Answer> answers = template.query("SELECT * FROM answer WHERE answer_id = ?", new AnswerMapper(), id);
        if(answers.isEmpty())
            return Optional.empty();
        else
            return Optional.of(answers.get(0));
    }

    @Override
    public List<Answer> findAllByQuestionIdFk(int questionId) {

        return template.query("SELECT * FROM answer WHERE question_id_fk = ?", new AnswerMapper(), questionId);
    }

    private int insert(Answer answer)
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("answer");
        insert.usingGeneratedKeyColumns("answer_id");
        Map<String, Object> map = new HashMap<>();
        map.put("author_id_fk", answer.getAuthorIdFk());
        map.put("question_id_fk", answer.getQuestionIdFk());
        map.put("text", answer.getText());
        map.put("creation_date", answer.getCreationDate());
        map.put("score", answer.getScore());
        return insert.executeAndReturnKey(map).intValue();
    }

    private void update(Answer answer)
    {
        template.update("UPDATE answer SET author_id_fk = ?, question_id_fk = ?, text = ?, creation_date = ?, score = ? WHERE answer_id = ?",
                answer.getAuthorIdFk(), answer.getQuestionIdFk(), answer.getText(), answer.getCreationDate(), answer.getScore(), answer.getAnswerId());
    }
}
