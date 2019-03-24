package ro.utcn.sd.flav.stackoverflow.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.flav.stackoverflow.entity.ApplicationUser;
import ro.utcn.sd.flav.stackoverflow.entity.Question;
import ro.utcn.sd.flav.stackoverflow.entity.Tag;
import ro.utcn.sd.flav.stackoverflow.entity.VoteQuestion;
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

        try{

            Question question = questionRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
            questionRepository.remove(question);
            repositoryFactory.createQuestionRepository().remove(question);
        }
        catch (QuestionNotFoundException e){
            System.out.println("Not a valid question id");
        }
    }

    @Transactional
    public List<Question> listQuestions()
    {
        List<Question> questions = repositoryFactory.createQuestionRepository().findAll();

        questions.forEach(q -> q.setTags(repositoryFactory.createQuestionRepository().findTagsByQuestion(q)));

        return questions;
    }

    @Transactional
    public void updateQuestion(int id, String title, String text)
    {
        QuestionRepository questionRepository = repositoryFactory.createQuestionRepository();

        try {
            Question question = questionRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
            question.setTitle(title);
            question.setText(text);
            questionRepository.save(question);
        }
        catch (QuestionNotFoundException e){
            System.out.println("No question with this id was found");
        }
    }

    @Transactional
    public List<Question> filterQuestionByTitle(String questionTitle)
    {
        return listQuestions().stream().filter(q -> q.getTitle().toLowerCase().contains(questionTitle.toLowerCase())).collect(Collectors.toList());
    }


    @Transactional
    public List<Question> filterQuestionByTag(Set<String> questionTags) {

        return listQuestions().stream().filter(q -> q.tagsToString().containsAll(questionTags)).collect(Collectors.toList());
    }

    @Transactional
    public Question getQuestionById(int questionId) {

        try {
            return repositoryFactory.createQuestionRepository().findById(questionId).orElseThrow(QuestionNotFoundException::new);
        }
        catch (QuestionNotFoundException e)
        {
            System.out.println("No question with this id was found.");
            return null;
        }
    }

    @Transactional
    public boolean handleVote(Integer userId, int questionId, boolean vote) {

        VoteQuestion voteQuestion = repositoryFactory.createVoteQuestionRepository().findVoteForQuestion(userId, questionId).orElse(null);
        Question question = repositoryFactory.createQuestionRepository().findById(questionId).orElse(null);

        if((voteQuestion != null && voteQuestion.isVoteType() == vote) || question == null || userId == question.getAuthorId())
            return true;
        else
        {

                if (voteQuestion == null) {

                    voteQuestion = new VoteQuestion(userId, question.getAuthorId(), questionId, vote);
                }
                voteQuestion.setVoteType(vote);

                if (vote) {
                    question.setScore(question.getScore() + 1);

                } else {

                    question.setScore(question.getScore() - 1);

                }

                repositoryFactory.createQuestionRepository().save(question);
                repositoryFactory.createVoteQuestionRepository().save(voteQuestion);

                return false;

        }
    }

    @Transactional
    public int voteCount(int questionId)
    {
        List<VoteQuestion> voteQuestions = repositoryFactory.createVoteQuestionRepository().findAllVotesOfQuestion(questionId);

        int downVotes = (int)voteQuestions.stream().filter(v -> !v.isVoteType()).count();
        int upVotes = (int)voteQuestions.stream().filter(v -> v.isVoteType()).count();

        return  upVotes - downVotes;
    }

    @Transactional
    public void updatePoints(int questionId)
    {
        List<VoteQuestion> voteQuestions = repositoryFactory.createVoteQuestionRepository().findAllVotesOfQuestion(questionId);
        Question question = repositoryFactory.createQuestionRepository().findById(questionId).orElse(null);

        int downVotes = (int)voteQuestions.stream().filter(v -> !v.isVoteType()).count();
        int upVotes = (int)voteQuestions.stream().filter(v -> v.isVoteType()).count();

        if(question != null) {

            ApplicationUser userQuestion = repositoryFactory.createAccountRepository().findById(question.getAuthorId()).orElse(null);

            if(userQuestion != null) {
                int downVotesScore = downVotes * 2;
                int upVotesScore = upVotes * 5;
                userQuestion.setPoints(userQuestion.getPoints() + upVotesScore - downVotesScore);

                repositoryFactory.createAccountRepository().save(userQuestion);
            }
        }
    }
}
