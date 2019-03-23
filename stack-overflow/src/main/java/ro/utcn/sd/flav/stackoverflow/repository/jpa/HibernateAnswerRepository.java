package ro.utcn.sd.flav.stackoverflow.repository.jpa;

import lombok.RequiredArgsConstructor;
import ro.utcn.sd.flav.stackoverflow.entity.Answer;
import ro.utcn.sd.flav.stackoverflow.repository.AnswerRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HibernateAnswerRepository implements AnswerRepository {
    private final EntityManager entityManager;

    @Override
    public List<Answer> findAll() {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Answer> query = builder.createQuery(Answer.class);
        query.select(query.from(Answer.class));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Answer save(Answer answer) {
        if(answer.getAnswerId() == null)
        {
            entityManager.persist(answer);
            return answer;
        }
        else
            return entityManager.merge(answer);

    }

    @Override
    public void remove(Answer answer) {

        entityManager.remove(answer);
    }

    @Override
    public Optional<Answer> findById(int id) {

        return Optional.ofNullable(entityManager.find(Answer.class, id));
    }

    @Override
    public List<Answer> findAllByQuestionIdFk(int questionIdFk) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Answer> query = builder.createQuery(Answer.class);

        // Define a range variable in FROM clause
        Root<Answer> root = query.from(Answer.class);

        // query.select(root) -> Select * FROM answer
        // SELECT * FROM answer WHERE questionIdFk = questionId -> questionIdFk attribute in Answer Class
        return entityManager.createQuery(query.select(root).where(builder.equal(root.get("questionIdFk"), questionIdFk))).getResultList();
    }
}
