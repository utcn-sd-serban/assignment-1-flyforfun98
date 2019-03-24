package ro.utcn.sd.flav.stackoverflow.repository.data;

import org.springframework.data.repository.Repository;
import ro.utcn.sd.flav.stackoverflow.entity.Answer;
import ro.utcn.sd.flav.stackoverflow.repository.AnswerRepository;

public interface DataAnswerRepository extends Repository<Answer, Integer>, AnswerRepository {

    void delete(Answer answer);

    @Override
    default void remove(Answer answer){
        delete(answer);
    }
}
