package ro.utcn.sd.flav.stackoverflow.integration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ro.utcn.sd.flav.stackoverflow.entity.*;
import ro.utcn.sd.flav.stackoverflow.exception.AccountNotFoundException;
import ro.utcn.sd.flav.stackoverflow.exception.AdminNotFoundException;
import ro.utcn.sd.flav.stackoverflow.exception.QuestionNotFoundException;
import ro.utcn.sd.flav.stackoverflow.repository.RepositoryFactory;
import ro.utcn.sd.flav.stackoverflow.seed.AnswerSeed;
import ro.utcn.sd.flav.stackoverflow.service.AccountManagementService;
import ro.utcn.sd.flav.stackoverflow.service.AnswerManagementService;
import ro.utcn.sd.flav.stackoverflow.service.QuestionManagementService;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class IntegrationTests {

        @Autowired
        private AnswerManagementService answerManagementService;
        @Autowired
        private QuestionManagementService questionManagementService;
        @Autowired
        private AccountManagementService accountManagementService;
        @Autowired
        private RepositoryFactory factory;


        @Before
        public void addProgramSeed() throws ParseException {

           AnswerSeed answerSeed = new AnswerSeed(factory);
           answerSeed.run();

        }

    @Test
    public void testLogin() {

        AccountManagementService accountService = accountManagementService;
        ApplicationUser user = accountService.login("flavius2","parola2");
        Assert.assertTrue(user.getUserId() == 35);
    }


    @Test(expected = AccountNotFoundException.class)
    public void testLoginInvalid() {

        AccountManagementService accountService = accountManagementService;
        accountService.login("flavius100","parola1");

    }

    @Test
    public void banUser(){

        AccountManagementService accountService = accountManagementService;
        ApplicationUser admin = accountService.login("flyforfun98","flavius1");
        accountService.changeUserStatusToBanned(admin, 36);
        Assert.assertTrue(accountService.listUsers().get(3).getStatus() == UserStatus.BANNED);
    }

    @Test(expected = AdminNotFoundException.class)
    public void simpleUserBans() {

        AccountManagementService accountService = accountManagementService;
        ApplicationUser user = accountService.login("flavius2","parola2");
        accountService.changeUserStatusToBanned(user, 1);
    }

    @Test
    public void listAnswersForQuestion() {

        AnswerManagementService answerService = answerManagementService;
        Assert.assertEquals(3,answerService.listAnswers(12).size());
    }

    @Test
    public void handleVote() {

        AnswerManagementService answerService = answerManagementService;

        Assert.assertTrue(answerService.handleVote(35,8,"UP"));
        Answer answer = answerService.listAnswers(12).stream().filter(a->a.getAnswerId() == 8).findFirst().get();
        Assert.assertEquals(2,answer.getScore());
    }

    @Test
    public void handleVoteSelf() {

        AnswerManagementService service = answerManagementService;
        Assert.assertTrue(service.handleVote(35,7,"UP"));
    }

    @Test
    public void updateAnswer(){

        AnswerManagementService answerService = answerManagementService;
        answerService.updateAnswer(34, 6, "new text");
        Assert.assertTrue(answerService.listAnswers().get(0).getText().equals("new text"));
    }

    @Test(expected = QuestionNotFoundException.class)
    public void removeQuestion(){

        AccountManagementService accountService = accountManagementService;
        ApplicationUser admin = accountService.login("flyforfun98","flavius1");
        QuestionManagementService questionService = questionManagementService;
        questionService.removeQuestion(admin, 1);
        Assert.assertTrue(questionService.listQuestions().get(0) == null);
    }

    private Date createDate(String date) throws ParseException {

        java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        return sqlDate;
    }
}
