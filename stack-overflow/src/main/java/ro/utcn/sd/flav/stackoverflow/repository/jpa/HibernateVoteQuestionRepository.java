package ro.utcn.sd.flav.stackoverflow.repository.jpa;

import lombok.RequiredArgsConstructor;
import ro.utcn.sd.flav.stackoverflow.entity.VoteQuestion;
import ro.utcn.sd.flav.stackoverflow.repository.VoteQuestionRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HibernateVoteQuestionRepository implements VoteQuestionRepository {

    private final EntityManager entityManager;

    @Override
    public List<VoteQuestion> findAll() {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<VoteQuestion> query = builder.createQuery(VoteQuestion.class);
        query.select(query.from(VoteQuestion.class));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public VoteQuestion save(VoteQuestion voteQuestion) {
        if(voteQuestion.getVoteId() == null)
        {
            entityManager.persist(voteQuestion);
            return voteQuestion;
        }
        else
            return entityManager.merge(voteQuestion);

    }

    @Override
    public void remove(VoteQuestion voteQuestion) {

        entityManager.remove(voteQuestion);
    }

    @Override
    public Optional<VoteQuestion> findById(int id) {

        return Optional.ofNullable(entityManager.find(VoteQuestion.class, id));
    }

    @Override
    public Optional<VoteQuestion> findVoteForQuestion(Integer userId, Integer questionId) {
        
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<VoteQuestion> query = builder.createQuery(VoteQuestion.class);
        Root<VoteQuestion> questionRoot = query.from(VoteQuestion.class);
        List<VoteQuestion> voteQuestions = entityManager.createQuery(query.select(questionRoot)
                .where(builder.equal(questionRoot.get("userGivesId"), userId))
                .where(builder.equal(questionRoot.get("questionVotedId"), questionId)))
                .getResultList();
        if(voteQuestions.isEmpty())
            return Optional.empty();
        else
            return Optional.of(voteQuestions.get(0));
    }

    @Override
    public List<VoteQuestion> findAllVotesOfQuestion(Integer questionId) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<VoteQuestion> query = builder.createQuery(VoteQuestion.class);
        Root<VoteQuestion> questionRoot = query.from(VoteQuestion.class);
        List<VoteQuestion> voteQuestions = entityManager.createQuery(query.select(questionRoot)
                .where(builder.equal(questionRoot.get("questionVotedId"), questionId)))
                .getResultList();

        return voteQuestions;
    }
}
