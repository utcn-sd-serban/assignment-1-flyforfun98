package ro.utcn.sd.flav.stackoverflow.unittests;

import org.junit.BeforeClass;
import org.junit.Test;
import ro.utcn.sd.flav.stackoverflow.entity.*;
import ro.utcn.sd.flav.stackoverflow.repository.*;
import ro.utcn.sd.flav.stackoverflow.repository.memory.InMemoryRepositoryFactory;
import ro.utcn.sd.flav.stackoverflow.service.AnswerManagementService;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;



public class AnswerManagementServiceTest extends TestRuler{


    private static RepositoryFactory createMockedFactory() throws ParseException {

        RepositoryFactory repositoryFactory = new InMemoryRepositoryFactory();

        repositoryFactory.createAnswerRepository().save(new Answer(1,1, 1,
                "Collections have a large use, they process collections of data", createDate("2014-03-21"), 0));
        repositoryFactory.createAnswerRepository().save(new Answer(2, 2, 1,
                "Collections have a large use, they process collections of data", createDate("2014-05-23"), 0));
        repositoryFactory.createAnswerRepository().save(new Answer(3, 3, 1,
                "Collection classes reduce effort for code maintenance", createDate("2014-06-17"), 0 ));
        repositoryFactory.createAnswerRepository().save(new Answer(4, 1, 2,
                "Strings allow the formating of texts", createDate("2015-02-29"), 0));
        repositoryFactory.createAnswerRepository().save(new Answer(5, 2, 3,
                "Integers are a class. Objects in this class have multiple methods, easier to work with than the primitive int", createDate("2018-11-09"), 0));

        return repositoryFactory;
    }



    private static Date createDate(String date) throws ParseException {

        java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        return sqlDate;
    }


    @BeforeClass
    public static void beforeClass() {
        Skip.IF(true);
    }


    @Test
    public void handleVote() throws ParseException {

        RepositoryFactory factory = createMockedFactory();
        AnswerManagementService answerService = new AnswerManagementService(factory);

        Skip.UNLESS(answerService.handleVote(2,1,true));
        Skip.UNLESS(answerService.handleVote(2,1,false));
        Skip.IF(answerService.handleVote(1,1,false));
    }

    @Test
    public void voteCount() throws ParseException {

        RepositoryFactory factory = createMockedFactory();
        AnswerManagementService answerService = new AnswerManagementService(factory);

        answerService.handleVote(2,3,true);
        Skip.IF(answerService.voteCount(3) == 1);
        Skip.UNLESS(answerService.voteCount(2) == 1);
        Skip.IF(answerService.voteCount(4) == 0);

    }

    @Test
    public void listAnswers() throws ParseException {

        RepositoryFactory factory = createMockedFactory();
        AnswerManagementService answerService = new AnswerManagementService(factory);

        ArrayList<Answer> answers1 = new ArrayList<>();
        answers1.addAll(answerService.listAnswers().subList(0,3));
        answers1.get(1).setScore(2);
        answers1.get(2).setScore(1);

        Skip.UNLESS(answerService.listAnswers(1).equals(answers1));

        ArrayList<Answer> answers2 = new ArrayList<>();
        answers2.add(answers1.get(1));
        answers2.add(answers1.get(2));
        answers2.add(answers1.get(0));

        Skip.IF(answerService.listAnswers(1).equals(answers2));

    }


}
