package ro.utcn.sd.flav.stackoverflow.repository;


import ro.utcn.sd.flav.stackoverflow.entity.Answer;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository {

    List<Answer> findAll();
    Answer save(Answer answer);
    void remove(Answer answer);
    Optional<Answer> findById(int id);

    List<Answer> findAllByQuestionIdFk(int questionIdFk);

}
