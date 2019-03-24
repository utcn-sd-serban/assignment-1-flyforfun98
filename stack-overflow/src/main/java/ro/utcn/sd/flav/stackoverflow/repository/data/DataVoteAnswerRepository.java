package ro.utcn.sd.flav.stackoverflow.repository.data;

import org.springframework.data.repository.Repository;
import ro.utcn.sd.flav.stackoverflow.entity.VoteAnswer;
import ro.utcn.sd.flav.stackoverflow.repository.VoteAnswerRepository;

import java.util.List;
import java.util.Optional;

public interface DataVoteAnswerRepository extends Repository<VoteAnswer, Integer>, VoteAnswerRepository {

    void delete(VoteAnswer applicationUser);

    @Override
    default void remove(VoteAnswer voteAnswer){
        delete(voteAnswer);
    }

    @Override
    default Optional<VoteAnswer> findVoteForAnswer(Integer userId, Integer answerId) {

        return findFirstByUserGivesAnswerIdAndUserGetsAnswerId(userId, answerId);
    }

    Optional<VoteAnswer>  findFirstByUserGivesAnswerIdAndUserGetsAnswerId(Integer userId, Integer answerId);


    @Override
    default List<VoteAnswer> findAllVotesOfAnswer(Integer answerId) {
         return findAllByAnswerVotedId(answerId);
    }

    List<VoteAnswer> findAllByAnswerVotedId(Integer answerId);
}
