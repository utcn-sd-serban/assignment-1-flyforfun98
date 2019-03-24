package ro.utcn.sd.flav.stackoverflow.unittests;

import org.junit.BeforeClass;
import org.junit.Test;
import ro.utcn.sd.flav.stackoverflow.entity.*;
import ro.utcn.sd.flav.stackoverflow.repository.AccountRepository;
import ro.utcn.sd.flav.stackoverflow.repository.QuestionRepository;
import ro.utcn.sd.flav.stackoverflow.repository.RepositoryFactory;
import ro.utcn.sd.flav.stackoverflow.repository.TagRepository;
import ro.utcn.sd.flav.stackoverflow.service.AnswerManagementService;
import ro.utcn.sd.flav.stackoverflow.service.QuestionManagementService;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AnswerManagementServiceTest extends TestRuler{

    private RepositoryFactory repositoryFactory;
    private QuestionManagementService questionManagementService = new QuestionManagementService(repositoryFactory);
    private AnswerManagementService answerManagementService = new AnswerManagementService(repositoryFactory);
    private Question question1;
    private Question question2;
    private Question question3;
    private Question question4;
    private Set<String> stringTags1 = new HashSet<>();
    private Set<String> stringTags2 = new HashSet<>();
    private Set<String> stringTags3 = new HashSet<>();

    public AnswerManagementServiceTest() throws ParseException {

        AccountRepository accountRepository = repositoryFactory.createAccountRepository();

        accountRepository.save(new ApplicationUser("flyforfun98", "flavius1", UserPermission.ADMIN, UserStatus.ALLOWED, 0));
        accountRepository.save(new ApplicationUser("flavius1", "parola1", UserPermission.USER, UserStatus.ALLOWED, 0));
        accountRepository.save(new ApplicationUser("flavius2", "parola2", UserPermission.USER, UserStatus.ALLOWED, 0));
        accountRepository.save(new ApplicationUser("flavius3", "parola3", UserPermission.USER, UserStatus.ALLOWED, 0));

        TagRepository tagRepository = repositoryFactory.createTagRepository();

        tagRepository.save(new Tag("memory"));
        tagRepository.save(new Tag("sort"));
        tagRepository.save(new Tag("index-out-of-bounds"));
        tagRepository.save(new Tag("overriding"));

        QuestionRepository questionRepository = repositoryFactory.createQuestionRepository();

        ArrayList<ApplicationUser> users = new ArrayList<>();
        users.addAll(accountRepository.findAll());

        ArrayList<Tag> tags = new ArrayList<>();
        tags.addAll(tagRepository.findAll());

        question1 = questionManagementService.addQuestion(users.get(2).getUserId(), "Collections", "Why to use Collections?", createDate("2014-01-28"), 0, new ArrayList<>(tags));
        questionRepository.save(question1);


        Tag newTag = new Tag("dependency-injection");
        tags.add(newTag);
        question2 = questionManagementService.addQuestion(users.get(3).getUserId(), "Strings", "Why to use Strings?", createDate("2000-11-01"), 0, new ArrayList<>(tags.subList(3,5)));
        questionRepository.save(question2);

        question3 = questionManagementService.addQuestion(users.get(3).getUserId(), "Integers", "Why to use Integers?", createDate("2007-09-13"), 0, new ArrayList<>(tags.subList(0,1)));
        questionRepository.save(question3);

        question4 = questionManagementService.addQuestion(users.get(1).getUserId(), "Arrays", "Why to use Arrays?", createDate("2018-03-21"),0, new ArrayList<>(tags.subList(3,4)));
        questionRepository.save(question4);

        stringTags1.add("overriding");
        stringTags1.add("sort");
        stringTags2.add("java-spring");
        stringTags3.add("memory");


        answerManagementService.addAnswer(users.get(1).getUserId(), question3.getQuestionId(),
                "Collections have a large use, they process collections of data");

        answerManagementService.addAnswer(users.get(2).getUserId(), question3.getQuestionId(),
                "Collections allow adding an element, deleting an element and host of other operations");

        answerManagementService.addAnswer(users.get(1).getUserId(), question3.getQuestionId(),
                "Collection classes reduce effort for code maintenance");


        answerManagementService.addAnswer(users.get(2).getUserId(), question1.getQuestionId(),
                "Strings allow the formating of texts");

        answerManagementService.addAnswer(users.get(3).getUserId(), question2.getQuestionId(),
                "Integers are a class. Objects in this class have multiple methods, easier to work with than the primitive int");
    }

    private Date createDate(String date) throws ParseException {

        java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        return sqlDate;
    }


    @BeforeClass
    public static void beforeClass() {
        Skip.IF(true);
    }


    @Test
    public void handleVote() {

        Skip.IF(answerManagementService.handleVote(1,1,true));
        Skip.IF(answerManagementService.handleVote(1,1,false));
        Skip.UNLESS(answerManagementService.handleVote(1,1,false));
    }

    @Test
    public void voteCount()
    {
        answerManagementService.handleVote(1,1,true);
        Skip.IF(answerManagementService.voteCount(1) == 1);
        Skip.UNLESS(answerManagementService.voteCount(2) == 1);
        Skip.IF(answerManagementService.voteCount(3) == 0);
    }


}
