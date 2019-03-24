package ro.utcn.sd.flav.stackoverflow.repository.memory;

import ro.utcn.sd.flav.stackoverflow.entity.VoteQuestion;
import ro.utcn.sd.flav.stackoverflow.repository.VoteQuestionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryVoteQuestionRepository implements VoteQuestionRepository{
    private final Map<Integer, VoteQuestion> voteQuestionsMap = new ConcurrentHashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(0);

    @Override
    public List<VoteQuestion> findAll() {
        return new ArrayList<>(voteQuestionsMap.values());
    }

    @Override
    public VoteQuestion save(VoteQuestion voteQuestion) {
        if(voteQuestion.getVoteId() == null)
        {
            voteQuestion.setVoteId(currentId.incrementAndGet());
        }

        voteQuestionsMap.put(voteQuestion.getVoteId(), voteQuestion);

        return voteQuestion;
    }

    @Override
    public void remove(VoteQuestion voteQuestion) {

        voteQuestionsMap.remove(voteQuestion.getVoteId());
    }

    @Override
    public Optional<VoteQuestion> findById(int id) {

        return Optional.ofNullable(voteQuestionsMap.get(id));
    }

    @Override
    public Optional<VoteQuestion> findVoteForQuestion(Integer userId, Integer questionId) {

        return findAll().stream().filter(x -> x.getUserGivesId() == userId && x.getQuestionVotedId() == questionId).findFirst();
    }

    @Override
    public List<VoteQuestion> findAllVotesOfQuestion(Integer questionId) {

        return findAll().stream().filter(x -> x.getQuestionVotedId() == questionId).collect(Collectors.toList());
    }
}
