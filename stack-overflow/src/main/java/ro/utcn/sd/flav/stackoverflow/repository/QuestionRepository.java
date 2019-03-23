package ro.utcn.sd.flav.stackoverflow.repository;

import ro.utcn.sd.flav.stackoverflow.entity.Question;
import ro.utcn.sd.flav.stackoverflow.entity.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface QuestionRepository {

    List<Question> findAll();
    Question save(Question question);
    void remove(Question question);
    Optional<Question> findById(int id);
    List<Tag> findTagsByQuestion(Question question);

}
