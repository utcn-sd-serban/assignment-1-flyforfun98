package ro.utcn.sd.flav.stackoverflow.repository.memory;

import ro.utcn.sd.flav.stackoverflow.entity.VoteAnswer;
import ro.utcn.sd.flav.stackoverflow.repository.VoteAnswerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryVoteAnswerRepository implements VoteAnswerRepository{
    private final Map<Integer, VoteAnswer> voteAnswersMap = new ConcurrentHashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(0);

    @Override
    public List<VoteAnswer> findAll() {
        return new ArrayList<>(voteAnswersMap.values());
    }

    @Override
    public VoteAnswer save(VoteAnswer voteAnswer) {
        if(voteAnswer.getVoteId() == null)
        {
            voteAnswer.setVoteId(currentId.incrementAndGet());
        }

        voteAnswersMap.put(voteAnswer.getVoteId(), voteAnswer);

        return voteAnswer;
    }

    @Override
    public void remove(VoteAnswer voteAnswer) {

        voteAnswersMap.remove(voteAnswer.getVoteId());
    }

    @Override
    public Optional<VoteAnswer> findById(int id) {

        return Optional.ofNullable(voteAnswersMap.get(id));
    }

    @Override
    public Optional<VoteAnswer> findVoteForAnswer(Integer userId, Integer answerId) {

        return findAll().stream().filter(x -> x.getUserGivesAnswerId() == userId && x.getAnswerVotedId() == answerId).findFirst();
    }

    @Override
    public List<VoteAnswer> findAllVotesOfAnswer(Integer answerId) {

        return findAll().stream().filter(x -> x.getAnswerVotedId() == answerId).collect(Collectors.toList());
    }
}
