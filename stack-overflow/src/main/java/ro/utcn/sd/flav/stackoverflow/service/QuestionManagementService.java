package ro.utcn.sd.flav.stackoverflow.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.flav.stackoverflow.entity.Question;
import ro.utcn.sd.flav.stackoverflow.entity.Tag;
import ro.utcn.sd.flav.stackoverflow.exception.QuestionNotFoundException;
import ro.utcn.sd.flav.stackoverflow.repository.QuestionRepository;
import ro.utcn.sd.flav.stackoverflow.repository.RepositoryFactory;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QuestionManagementService {

    private final RepositoryFactory repositoryFactory;


    @Transactional
    public Question addQuestion(Integer authorId, String title, String text, Date creationDate, int score, ArrayList<Tag> tags)
    {
        Question question = new Question(authorId,title,text,creationDate,score);
        question.addTags(tags);
        return repositoryFactory.createQuestionRepository().save(question);
    }

    @Transactional
    public void removeQuestion(Integer id)
    {
        QuestionRepository questionRepository = repositoryFactory.createQuestionRepository();
        Question question = questionRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
        questionRepository.remove(question);
    }

    @Transactional
    public List<Question> listQuestions()
    {
        List<Question> questions = repositoryFactory.createQuestionRepository().findAll();

        questions.forEach(q -> q.setTags(repositoryFactory.createQuestionRepository().findTagsByQuestion(q)));

        return questions;
    }

    @Transactional
    public void updateQuestion(int id, Integer authorId, String title, String text, Date creationDate, int score)
    {
        QuestionRepository questionRepository = repositoryFactory.createQuestionRepository();
        Question question = questionRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
        question.setAuthorId(authorId);
        question.setTitle(title);
        question.setText(text);
        question.setCreationDate(creationDate);
        question.setScore(score);
        questionRepository.save(question);
    }

    @Transactional
    public List<Question> filterQuestionByTitle(String questionTitle)
    {
        return listQuestions().stream().filter(q -> q.getTitle().toLowerCase().contains(questionTitle.toLowerCase())).collect(Collectors.toList());
    }


    public List<Question> filterQuestionByTag(Set<String> questionTags) {

        return listQuestions().stream().filter(q -> q.tagsToString().containsAll(questionTags)).collect(Collectors.toList());
    }
}
