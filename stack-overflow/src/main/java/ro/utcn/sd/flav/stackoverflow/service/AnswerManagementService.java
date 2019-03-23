package ro.utcn.sd.flav.stackoverflow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.flav.stackoverflow.entity.Answer;
import ro.utcn.sd.flav.stackoverflow.entity.ApplicationUser;
import ro.utcn.sd.flav.stackoverflow.entity.UserPermission;
import ro.utcn.sd.flav.stackoverflow.exception.AccountNotFoundException;
import ro.utcn.sd.flav.stackoverflow.exception.AnswerNotFoundException;
import ro.utcn.sd.flav.stackoverflow.repository.AnswerRepository;
import ro.utcn.sd.flav.stackoverflow.repository.RepositoryFactory;

import java.sql.Date;
import java.util.List;

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

        return repositoryFactory.createAnswerRepository().findAllByQuestionIdFk(questionIdFk);//.stream().sorted(Comparator.comparing(Answer::getVoteCount).reversed()).collect(Collectors.toList());
    }
}
