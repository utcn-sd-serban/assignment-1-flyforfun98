package ro.utcn.sd.flav.stackoverflow.repository.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import ro.utcn.sd.flav.stackoverflow.entity.Question;
import ro.utcn.sd.flav.stackoverflow.entity.Tag;
import ro.utcn.sd.flav.stackoverflow.repository.QuestionRepository;


import java.util.ArrayList;
import java.util.List;


public interface DataQuestionRepository extends Repository<Question, Integer>, QuestionRepository {


    void delete(Question question);
    List<Question> findAllByOrderByCreationDateAsc();


    @Override
    default List<Question> findAll(){
          return  findAllByOrderByCreationDateAsc();
    }


    @Override
    default void remove(Question question){
        delete(question);
    }

    @Override
    default List<Tag> findTagsByQuestion(Question question)
    {
        return question.getTags();
    }


}
