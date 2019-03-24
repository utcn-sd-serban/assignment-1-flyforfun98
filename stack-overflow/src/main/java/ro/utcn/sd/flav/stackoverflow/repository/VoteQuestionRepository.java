package ro.utcn.sd.flav.stackoverflow.repository;

import ro.utcn.sd.flav.stackoverflow.entity.VoteQuestion;

import java.util.List;
import java.util.Optional;

public interface VoteQuestionRepository {

    List<VoteQuestion> findAll();
    VoteQuestion save(VoteQuestion voteQuestion);
    void remove(VoteQuestion voteQuestion);
    Optional<VoteQuestion> findById(int id);

    Optional<VoteQuestion> findVoteForQuestion(Integer userId, Integer questionId);
    List<VoteQuestion> findAllVotesOfQuestion(Integer questionId);
}
