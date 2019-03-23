package ro.utcn.sd.flav.stackoverflow.repository.memory;

import ro.utcn.sd.flav.stackoverflow.entity.Answer;
import ro.utcn.sd.flav.stackoverflow.repository.AnswerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryAnswerRepository implements AnswerRepository {

    private final Map<Integer, Answer> answersMap = new ConcurrentHashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(0);

    @Override
    public List<Answer> findAll() {
        return new ArrayList<>(answersMap.values());
    }

    @Override
    public Answer save(Answer answer) {
        if(answer.getAnswerId() == null)
        {
            answer.setAnswerId(currentId.incrementAndGet());
        }

        answersMap.put(answer.getAnswerId(), answer);

        return answer;
    }

    @Override
    public void remove(Answer answer) {

        answersMap.remove(answer.getAnswerId());
    }

    @Override
    public Optional<Answer> findById(int id) {

        return Optional.ofNullable(answersMap.get(id));
    }

    @Override
    public List<Answer> findAllByQuestionIdFk(int questionId) {

        return findAll().stream().filter(answer -> answer.getQuestionIdFk() == questionId).collect(Collectors.toList());
    }
}
