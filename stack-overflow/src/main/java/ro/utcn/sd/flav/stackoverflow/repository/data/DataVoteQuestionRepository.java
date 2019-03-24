package ro.utcn.sd.flav.stackoverflow.repository.data;

import org.springframework.data.repository.Repository;
import ro.utcn.sd.flav.stackoverflow.entity.VoteQuestion;
import ro.utcn.sd.flav.stackoverflow.repository.VoteQuestionRepository;

import java.util.List;
import java.util.Optional;

public interface DataVoteQuestionRepository extends Repository<VoteQuestion, Integer>, VoteQuestionRepository {


    void delete(VoteQuestion voteQuestion);

    @Override
    default void remove(VoteQuestion voteQuestion){
        delete(voteQuestion);
    }
    

    @Override
    default Optional<VoteQuestion> findVoteForQuestion(Integer userId, Integer questionId) {

        return findFirstByUserGivesIdAndUserGetsId(userId, questionId);
    }

    Optional<VoteQuestion>  findFirstByUserGivesIdAndUserGetsId(Integer userId, Integer questionId);


    @Override
    default List<VoteQuestion> findAllVotesOfQuestion(Integer questionId) {
        return findAllByQuestionVotedId(questionId);
    }

    List<VoteQuestion> findAllByQuestionVotedId(Integer questionId);
}
