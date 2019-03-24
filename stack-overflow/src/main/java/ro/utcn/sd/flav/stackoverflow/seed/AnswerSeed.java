package ro.utcn.sd.flav.stackoverflow.seed;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.flav.stackoverflow.entity.Answer;
import ro.utcn.sd.flav.stackoverflow.entity.ApplicationUser;
import ro.utcn.sd.flav.stackoverflow.entity.Question;
import ro.utcn.sd.flav.stackoverflow.repository.AccountRepository;
import ro.utcn.sd.flav.stackoverflow.repository.AnswerRepository;
import ro.utcn.sd.flav.stackoverflow.repository.QuestionRepository;
import ro.utcn.sd.flav.stackoverflow.repository.RepositoryFactory;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AnswerSeed implements CommandLineRunner{

    private final RepositoryFactory repositoryFactory;


    @Override
    @Transactional
    public void run(String... args){

        AnswerRepository answerRepository = repositoryFactory.createAnswerRepository();
        AccountSeed accountSeed = new AccountSeed(repositoryFactory);
        QuestionSeed questionSeed = new QuestionSeed(repositoryFactory);


        try {
            accountSeed.run();
            questionSeed.run();

            AccountRepository accountRepository = repositoryFactory.createAccountRepository();
            QuestionRepository questionRepository = repositoryFactory.createQuestionRepository();


            if(answerRepository.findAll().isEmpty())
            {

                ArrayList<ApplicationUser> users = new ArrayList<>();
                users.addAll(accountRepository.findAll());

                ArrayList<Question> questions = new ArrayList<>();
                questions.addAll(questionRepository.findAll());


                answerRepository.save(new Answer(users.get(1).getUserId(), questions.get(2).getQuestionId(),
                        "Collections have a large use, they process collections of data", createDate("2014-03-11"),0 ));

                answerRepository.save(new Answer(users.get(2).getUserId(), questions.get(2).getQuestionId(),
                        "Collections allow adding an element, deleting an element and host of other operations", createDate("2014-04-02"),0));

                answerRepository.save(new Answer(users.get(1).getUserId(), questions.get(2).getQuestionId(),
                        "Collection classes reduce effort for code maintenance", createDate("2014-04-03"),0));


                answerRepository.save(new Answer(users.get(2).getUserId(), questions.get(0).getQuestionId(),
                        "Strings allow the formating of texts", createDate("2001-05-17"),0 ));

                answerRepository.save(new Answer(users.get(3).getUserId(), questions.get(1).getQuestionId(),
                        "Integers are a class. Objects in this class have multiple methods, easier to work with than the primitive int", createDate("2008-01-07"),0 ));
            }
        }
        catch (ParseException e)
        {
            e.getStackTrace();
        }


    }

    private Date createDate(String date) throws ParseException {

        java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        return sqlDate;
    }
}
