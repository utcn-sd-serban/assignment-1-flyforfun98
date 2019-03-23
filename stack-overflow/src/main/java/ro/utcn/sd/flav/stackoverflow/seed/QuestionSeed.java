package ro.utcn.sd.flav.stackoverflow.seed;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.flav.stackoverflow.entity.Question;
import ro.utcn.sd.flav.stackoverflow.entity.Tag;
import ro.utcn.sd.flav.stackoverflow.repository.QuestionRepository;
import ro.utcn.sd.flav.stackoverflow.repository.RepositoryFactory;
import ro.utcn.sd.flav.stackoverflow.repository.TagRepository;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class QuestionSeed implements CommandLineRunner {

    private final RepositoryFactory repositoryFactory;

    @Override
    @Transactional
    public void run(String... args) throws ParseException {

        QuestionRepository questionRepository = repositoryFactory.createQuestionRepository();
        TagSeed tagSeed = new TagSeed(repositoryFactory);
        tagSeed.run();
        TagRepository tagRepository = repositoryFactory.createTagRepository();


        if(questionRepository.findAll().isEmpty())
        {
            ArrayList<Tag> tags = new ArrayList<>();
            tags.addAll(tagRepository.findAll());

            Question question = new Question(2, "Collections", "Why to use Collections?", createDate("2014-01-28"), 0);
            question.setTags(tags);
            questionRepository.save(question);


            Tag newTag = new Tag("dependency-injection");
            tags.add(newTag);
            question = new Question(3, "Strings", "Why to use Strings?", createDate("2000-11-01"), 0);
            question.setTags(tags.subList(3,5));
            questionRepository.save(question);

            question = new Question(4, "Integers", "Why to use Integers?", createDate("2007-09-13"), 0);
            question.setTags(tags.subList(0,1));
            questionRepository.save(question);

            question = new Question(1, "Arrays", "Why to use Arrays?", createDate("2018-03-21"),0);
            question.setTags(tags.subList(3,4));
            questionRepository.save(question);
        }

    }

    private Date createDate(String date) throws ParseException {

        java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        return sqlDate;
    }
}
