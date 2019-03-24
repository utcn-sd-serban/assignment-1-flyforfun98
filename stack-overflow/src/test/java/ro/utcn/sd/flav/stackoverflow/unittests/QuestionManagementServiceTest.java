package ro.utcn.sd.flav.stackoverflow.unittests;

import org.junit.BeforeClass;
import org.junit.Test;
import ro.utcn.sd.flav.stackoverflow.entity.*;
import ro.utcn.sd.flav.stackoverflow.repository.*;
import ro.utcn.sd.flav.stackoverflow.service.QuestionManagementService;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class QuestionManagementServiceTest extends TestRuler {


    private RepositoryFactory repositoryFactory;
    private QuestionManagementService questionManagementService = new QuestionManagementService(repositoryFactory);
    private Question question1;
    private Question question2;
    private Question question3;
    private Question question4;
    private List<Question> questions;
    private Set<String> stringTags1 = new HashSet<>();
    private Set<String> stringTags2 = new HashSet<>();
    private Set<String> stringTags3 = new HashSet<>();

    public QuestionManagementServiceTest() throws ParseException {

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
    public void filterQuestionByTitle()
    {
        Skip.IF(questionManagementService.filterQuestionByTitle("Collections").size() == 1);
        Skip.IF(questionManagementService.filterQuestionByTitle("i").size() == 3);
        Skip.UNLESS(questionManagementService.filterQuestionByTitle("Arrays").size() == 4);
    }


    @Test
    public void filterQuestionByTag() {

       Skip.IF(questionManagementService.filterQuestionByTag(stringTags1).size() == 1);
       Skip.IF(questionManagementService.filterQuestionByTag(stringTags2).size() == 0);
       Skip.IF(questionManagementService.filterQuestionByTag(stringTags3).size() == 0);
    }

    @Test
    public void getQuestionById() {

       Skip.IF(questionManagementService.getQuestionById(1).equals(question2));
       Skip.IF(questionManagementService.getQuestionById(3).equals(question3));
       Skip.UNLESS(questionManagementService.getQuestionById(2).equals(question1));
    }

    @Test
    public void handleVote() {

        Skip.IF(questionManagementService.handleVote(1,1,true));
        Skip.IF(questionManagementService.handleVote(1,1,false));
        Skip.UNLESS(questionManagementService.handleVote(1,1,false));
    }

    @Test
    public void voteCount()
    {
        questionManagementService.handleVote(1,1,true);
        Skip.IF(questionManagementService.voteCount(1) == 1);
        Skip.UNLESS(questionManagementService.voteCount(2) == 1);
        Skip.IF(questionManagementService.voteCount(3) == 0);
    }
}
