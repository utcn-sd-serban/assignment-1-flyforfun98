package ro.utcn.sd.flav.stackoverflow.repository;

import ro.utcn.sd.flav.stackoverflow.entity.VoteAnswer;

import java.util.List;
import java.util.Optional;

public interface VoteAnswerRepository {

    List<VoteAnswer> findAll();
    VoteAnswer save(VoteAnswer voteAnswer);
    void remove(VoteAnswer voteAnswer);
    Optional<VoteAnswer> findById(int id);

    Optional<VoteAnswer> findVoteForAnswer(Integer userId, Integer answerId);

    List<VoteAnswer> findAllVotesOfAnswer(Integer answerId);
}
