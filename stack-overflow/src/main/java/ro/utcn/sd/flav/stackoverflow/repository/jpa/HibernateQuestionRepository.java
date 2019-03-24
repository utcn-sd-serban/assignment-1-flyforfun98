package ro.utcn.sd.flav.stackoverflow.repository.jpa;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import ro.utcn.sd.flav.stackoverflow.entity.Question;
import ro.utcn.sd.flav.stackoverflow.entity.Tag;
import ro.utcn.sd.flav.stackoverflow.repository.QuestionRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HibernateQuestionRepository implements QuestionRepository{

    private final EntityManager entityManager;

    @Override
    public List<Question> findAll() {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Question> query = builder.createQuery(Question.class);
        query.select(query.from(Question.class));

        List<Question> sortedQuestions = entityManager.createQuery(query).getResultList();
        Collections.sort(sortedQuestions);
        return sortedQuestions;

    }

    @Override
    public Question save(Question question) {
        if(question.getQuestionId() == null)
        {
            entityManager.persist(question);
            return question;
        }
        else
            return entityManager.merge(question);

    }

    @Override
    public void remove(Question question) {

        entityManager.remove(question);
    }

    @Override
    public Optional<Question> findById(int id) {

        return Optional.ofNullable(entityManager.find(Question.class, id));
    }

    @Override
    public List<Tag> findTagsByQuestion(Question question) {

        return question.getTags();
    }



}
