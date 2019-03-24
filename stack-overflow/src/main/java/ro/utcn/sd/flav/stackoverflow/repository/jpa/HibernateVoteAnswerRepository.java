package ro.utcn.sd.flav.stackoverflow.repository.jpa;

import lombok.RequiredArgsConstructor;
import ro.utcn.sd.flav.stackoverflow.entity.VoteAnswer;
import ro.utcn.sd.flav.stackoverflow.repository.VoteAnswerRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HibernateVoteAnswerRepository implements VoteAnswerRepository {

    private final EntityManager entityManager;

    @Override
    public List<VoteAnswer> findAll() {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<VoteAnswer> query = builder.createQuery(VoteAnswer.class);
        query.select(query.from(VoteAnswer.class));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public VoteAnswer save(VoteAnswer votedAnswer) {
        if(votedAnswer.getVoteId() == null)
        {
            entityManager.persist(votedAnswer);
            return votedAnswer;
        }
        else
            return entityManager.merge(votedAnswer);

    }

    @Override
    public void remove(VoteAnswer votedAnswer) {

        entityManager.remove(votedAnswer);
    }

    @Override
    public Optional<VoteAnswer> findById(int id) {

        return Optional.ofNullable(entityManager.find(VoteAnswer.class, id));
    }

    @Override
    public Optional<VoteAnswer> findVoteForAnswer(Integer userId, Integer answerId) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<VoteAnswer> query = builder.createQuery(VoteAnswer.class);
        Root<VoteAnswer> answerRoot = query.from(VoteAnswer.class);
        List<VoteAnswer> voteAnswers = entityManager.createQuery(query.select(answerRoot)
                .where(builder.equal(answerRoot.get("userGivesAnswerId"), userId))
                .where(builder.equal(answerRoot.get("answerVotedId"), answerId)))
                .getResultList();
        if(voteAnswers.isEmpty())
            return Optional.empty();
        else
            return Optional.of(voteAnswers.get(0));
    }

    @Override
    public List<VoteAnswer> findAllVotesOfAnswer(Integer answerId) {
        
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<VoteAnswer> query = builder.createQuery(VoteAnswer.class);
        Root<VoteAnswer> answerRoot = query.from(VoteAnswer.class);
        List<VoteAnswer> voteAnswers = entityManager.createQuery(query.select(answerRoot)
                .where(builder.equal(answerRoot.get("answerVotedId"), answerId)))
                .getResultList();

        return voteAnswers;
    }
}
