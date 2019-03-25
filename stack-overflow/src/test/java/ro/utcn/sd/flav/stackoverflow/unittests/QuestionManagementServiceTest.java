package ro.utcn.sd.flav.stackoverflow.unittests;

import org.junit.BeforeClass;
import org.junit.Test;
import ro.utcn.sd.flav.stackoverflow.entity.*;
import ro.utcn.sd.flav.stackoverflow.repository.*;
import ro.utcn.sd.flav.stackoverflow.repository.memory.InMemoryRepositoryFactory;
import ro.utcn.sd.flav.stackoverflow.service.QuestionManagementService;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;



public class QuestionManagementServiceTest extends TestRuler {



        private static RepositoryFactory createMockedFactory() throws ParseException {

            RepositoryFactory repositoryFactory = new InMemoryRepositoryFactory();

            repositoryFactory.createQuestionRepository().save(new Question(1,1, "Collections",
                    "Why are collections useful?", createDate("2018-03-21"), 0));
            repositoryFactory.createQuestionRepository().save(new Question(2, 2, "Strings",
                    "What are Strings?", createDate("2014-05-23"), 0));
            repositoryFactory.createQuestionRepository().save(new Question(3, 2, "Integers",
                    "What are the methods for Integer objects?", createDate("2014-03-17"), 0 ));
            repositoryFactory.createQuestionRepository().save(new Question(4, 3, "Arrays",
                    "How to convert List to ArrayList?", createDate("2015-02-29"), 0));


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
            QuestionManagementService questionService = new QuestionManagementService(factory);

            Skip.UNLESS(questionService.handleVote(2,1,true));
            Skip.UNLESS(questionService.handleVote(2,1,false));
            Skip.IF(questionService.handleVote(1,1,false));
        }

        @Test
        public void voteCount() throws ParseException {

            RepositoryFactory factory = createMockedFactory();
            QuestionManagementService questionService = new QuestionManagementService(factory);

            questionService.handleVote(2,3,true);
            Skip.IF(questionService.voteCount(3) == 0);
            Skip.UNLESS(questionService.voteCount(2) == 1);
            Skip.IF(questionService.voteCount(4) == 0);

        }

        @Test
        public void listQuestions() throws ParseException {

            RepositoryFactory factory = createMockedFactory();
            QuestionManagementService questionService = new QuestionManagementService(factory);

            ArrayList<Question> questions1 = new ArrayList<>();
            questions1.add(new Question(3, 2, "Integers",
                    "What are the methods for Integer objects?", createDate("2014-03-17"), 0 ));
            questions1.add(new Question(2, 2, "Strings",
                    "What are Strings?", createDate("2014-05-23"), 0));
            questions1.add(new Question(4, 3, "Arrays",
                    "How to convert List to ArrayList?", createDate("2015-02-29"), 0));
            questions1.add(new Question(1,1, "Collections",
                    "Why are collections useful?", createDate("2018-03-21"), 0));


            Skip.IF(questionService.listQuestions().equals(questions1));


            ArrayList<Question> questions2 = new ArrayList<>();
            questions2.add(questions1.get(1));
            questions2.add(questions1.get(2));
            questions2.add(questions1.get(0));
            questions2.add(questions1.get(3));

            Skip.UNLESS(questionService.listQuestions().equals(questions2));

        }


    @Test
    public void filterQuestionByTitle() throws ParseException {

        RepositoryFactory factory = createMockedFactory();
        QuestionManagementService questionService = new QuestionManagementService(factory);

        ArrayList<Question> questions1 = new ArrayList<>();
        questions1.add(new Question(3, 2, "Integers",
                "What are the methods for Integer objects?", createDate("2014-03-17"), 0 ));
        questions1.add(new Question(2, 2, "Strings",
                "What are Strings?", createDate("2014-05-23"), 0));
        questions1.add(new Question(4, 3, "Arrays",
                "How to convert List to ArrayList?", createDate("2015-02-29"), 0));
        questions1.add(new Question(1,1, "Collections",
                "Why are collections useful?", createDate("2018-03-21"), 0));

        ArrayList<Question> questions2 = new ArrayList<>();
        questions2.add(questions1.get(0));
        questions2.add(questions1.get(1));
        questions2.add(questions1.get(3));

        Skip.IF(questionService.filterQuestionByTitle("i").equals(questions2));

        questions2.remove(1);
        questions2.remove(1);

        Skip.IF(questionService.filterQuestionByTitle("integers").equals(questions2));

        questions2.add(questions1.get(2));
        questions2.add(questions1.get(1));

        Skip.UNLESS(questionService.filterQuestionByTitle("i").equals(questions2));
    }


    @Test
    public void filterQuestionByTag() throws ParseException {

        RepositoryFactory factory = createMockedFactory();
        QuestionManagementService questionService = new QuestionManagementService(factory);

        ArrayList<Tag> tags = new ArrayList<>();

        tags.add(new Tag(1, "memory"));
        tags.add(new Tag(2, "null-pointer-exception"));
        factory.createQuestionRepository().findById(1).orElse(null).addTags(tags);

        tags.remove(1);
        tags.add(new Tag(3,"index-out-of-bounds"));
        factory.createQuestionRepository().findById(2).orElse(null).addTags(tags);


        tags.remove(1);
        tags.remove(0);
        tags.add(new Tag(4, "arrays"));
        tags.add(new Tag(5, "java-spring"));
        factory.createQuestionRepository().findById(3).orElse(null).addTags(tags);

        tags.remove(1);
        tags.add(new Tag(6, "dependency-injection"));
        factory.createQuestionRepository().findById(4).orElse(null).addTags(tags);

        Set<String> stringTags = new HashSet<>();
        stringTags.add("memory");
        stringTags.add("index-out-of-bounds");

        ArrayList<Question> questions1 = new ArrayList<>();
        questions1.add(new Question(3, 2, "Integers",
                "What are the methods for Integer objects?", createDate("2014-03-17"), 0 ));
        questions1.add(new Question(2, 2, "Strings",
                "What are Strings?", createDate("2014-05-23"), 0));
        questions1.add(new Question(4, 3, "Arrays",
                "How to convert List to ArrayList?", createDate("2015-02-29"), 0));
        questions1.add(new Question(1,1, "Collections",
                "Why are collections useful?", createDate("2018-03-21"), 0));


        Skip.IF(questionService.filterQuestionByTag(stringTags).size() == 1);


        stringTags.remove("index-out-of-bounds");
        Skip.IF(questionService.filterQuestionByTag(stringTags).size() == 2);

        stringTags.add("dependency-injection");
        stringTags.add("arrays");

        Skip.UNLESS(questionService.filterQuestionByTag(stringTags).size() >= 1);

        stringTags.remove("memory");
        Skip.UNLESS(questionService.filterQuestionByTag(stringTags).size() != 1);
    }


}
