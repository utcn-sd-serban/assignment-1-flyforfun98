package ro.utcn.sd.flav.stackoverflow.repository.memory;

import ro.utcn.sd.flav.stackoverflow.entity.Question;
import ro.utcn.sd.flav.stackoverflow.entity.Tag;
import ro.utcn.sd.flav.stackoverflow.repository.QuestionRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class InMemoryQuestionRepository implements QuestionRepository {
    private final Map<Integer, Question>  questionMap = new ConcurrentHashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(0);

    @Override
    public List<Question> findAll() {

        ArrayList<Question> sortedQuestions = new ArrayList<>(questionMap.values());
        Collections.sort(sortedQuestions);
        return sortedQuestions;
    }

    @Override
    public Question save(Question question) {
        if(question.getQuestionId() == null)
        {
            question.setQuestionId(currentId.incrementAndGet());
        }

        questionMap.put(question.getQuestionId(), question);

        return question;
    }

    @Override
    public void remove(Question question) {

        questionMap.remove(question.getQuestionId());
    }

    @Override
    public Optional<Question> findById(int id) {
        return Optional.ofNullable(questionMap.get(id));
    }

    @Override
    public List<Tag> findTagsByQuestion(Question question) {
        return question.getTags();
    }

}
