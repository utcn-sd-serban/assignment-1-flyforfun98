package ro.utcn.sd.flav.stackoverflow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.flav.stackoverflow.entity.Answer;
import ro.utcn.sd.flav.stackoverflow.entity.ApplicationUser;
import ro.utcn.sd.flav.stackoverflow.entity.UserPermission;
import ro.utcn.sd.flav.stackoverflow.entity.VoteAnswer;
import ro.utcn.sd.flav.stackoverflow.exception.AccountNotFoundException;
import ro.utcn.sd.flav.stackoverflow.exception.AnswerNotFoundException;
import ro.utcn.sd.flav.stackoverflow.repository.AnswerRepository;
import ro.utcn.sd.flav.stackoverflow.repository.RepositoryFactory;

import java.sql.Date;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerManagementService {

    private final RepositoryFactory repositoryFactory;


    @Transactional
    public Answer addAnswer(Integer authorId, Integer questionId, String text)
    {
        java.util.Date date = new java.util.Date();
        return repositoryFactory.createAnswerRepository().save(new Answer(authorId, questionId, text, new Date(date.getTime()), 0));
    }

    @Transactional
    public void removeAnswer(Integer userId, Integer answerId)
    {
        AnswerRepository answerRepository = repositoryFactory.createAnswerRepository();

        try {

            ApplicationUser user = repositoryFactory.createAccountRepository().findById(userId).orElseThrow(AccountNotFoundException::new);
            Answer answer = answerRepository.findById(answerId).orElseThrow(AnswerNotFoundException::new);

            if (!answer.getAuthorIdFk().equals(user.getUserId()) && !user.getPermission().equals(UserPermission.ADMIN)) {
                System.out.println("No permission to remove the answer");
            }
            else {
                answerRepository.remove(answer);
                repositoryFactory.createAnswerRepository().remove(answer);
            }
        }
        catch (AnswerNotFoundException e)
        {
            System.out.println("No answer id or user was found");
        }
    }

    @Transactional
    public List<Answer> listAnswers()
    {
        return repositoryFactory.createAnswerRepository().findAll();
    }

    @Transactional
    public void updateAnswer(Integer userId, Integer answerId, String text)
    {
        AnswerRepository answerRepository = repositoryFactory.createAnswerRepository();

        try {

            ApplicationUser user = repositoryFactory.createAccountRepository().findById(userId).orElseThrow(AccountNotFoundException::new);
            Answer answer = answerRepository.findById(answerId).orElseThrow(AnswerNotFoundException::new);

            if (!answer.getAuthorIdFk().equals(user.getUserId()) && !user.getPermission().equals(UserPermission.ADMIN)) {
                System.out.println("No permission to edit the answer");
            } else {
                answer.setText(text);
                answerRepository.save(answer);
            }
        }
        catch (AnswerNotFoundException e)
        {
            System.out.println("No answer id or user was found");
        }
    }

    @Transactional
    public List<Answer> listAnswers (int questionIdFk) {

        return repositoryFactory.createAnswerRepository().findAllByQuestionIdFk(questionIdFk).stream()
                .sorted(Comparator.comparing(Answer::getScore).reversed()).collect(Collectors.toList());
    }

    @Transactional
    public boolean handleVote(Integer userId, int answerId, boolean vote) {

        VoteAnswer voteAnswer = repositoryFactory.createVoteAnswerRepository().findVoteForAnswer(userId, answerId).orElse(null);
        Answer answer = repositoryFactory.createAnswerRepository().findById(answerId).orElse(null);
        ApplicationUser userVote = repositoryFactory.createAccountRepository().findById(userId).orElse(null);

        if((voteAnswer != null && voteAnswer.isVoteType() == vote) || answer == null || userId == answer.getAuthorIdFk())
            return true;
        else
        {
                if (voteAnswer == null)
                    voteAnswer = new VoteAnswer(userId, answer.getAuthorIdFk(), answerId, vote);

                voteAnswer.setVoteType(vote);

                if (vote) {
                    answer.setScore(answer.getScore() + 1);
                }
                else {
                    answer.setScore(answer.getScore() - 1);
                    if(userVote != null) {

                        userVote.setPoints(userVote.getPoints() - 1);
                        repositoryFactory.createAccountRepository().save(userVote);
                    }
                }

                repositoryFactory.createAnswerRepository().save(answer);
                repositoryFactory.createVoteAnswerRepository().save(voteAnswer);

                return false;

        }
    }

    @Transactional
    public int voteCount(int answerId)
    {
        List<VoteAnswer> voteAnswer = repositoryFactory.createVoteAnswerRepository().findAllVotesOfAnswer(answerId);

        int downVotes = (int)voteAnswer.stream().filter(v -> !v.isVoteType()).count();
        int upVotes = (int)voteAnswer.stream().filter(v -> v.isVoteType()).count();

        return  upVotes - downVotes;
    }


    @Transactional
    public void updatePoints(int answerId)
    {
        List<VoteAnswer> voteAnswer = repositoryFactory.createVoteAnswerRepository().findAllVotesOfAnswer(answerId);
        Answer answer = repositoryFactory.createAnswerRepository().findById(answerId).orElse(null);

        int downVotes = (int)voteAnswer.stream().filter(v -> !v.isVoteType()).count();
        int upVotes = (int)voteAnswer.stream().filter(v -> v.isVoteType()).count();

        if(answer != null) {

            ApplicationUser userAnswer = repositoryFactory.createAccountRepository().findById(answer.getAuthorIdFk()).orElse(null);

            if(userAnswer != null) {

                int downVotesScore = downVotes * 2;
                int upVotesScore = upVotes * 10;
                userAnswer.setPoints(userAnswer.getPoints() + upVotesScore - downVotesScore);

                repositoryFactory.createAccountRepository().save(userAnswer);
            }
        }
    }
}
