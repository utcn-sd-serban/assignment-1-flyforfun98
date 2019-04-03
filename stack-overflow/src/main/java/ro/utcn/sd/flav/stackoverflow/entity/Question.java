package ro.utcn.sd.flav.stackoverflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Data
@Entity
@Table(name = "question")
@AllArgsConstructor
@NoArgsConstructor
public class Question implements Comparable<Question>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer questionId;
    private Integer authorId;
    private String title;
    private String text;
    private Date creationDate;
    private int score;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "question_tags",
            joinColumns = @JoinColumn(name = "question_id_fk1"),
            inverseJoinColumns = @JoinColumn(name = "tag_id_fk"))
    private List<Tag> tags;



    public Question(Integer authorId, String title, String text, Date creationDate, int score)
    {
        this.authorId = authorId;
        this.title = title;
        this.text = text;
        this.creationDate = creationDate;
        this.score = score;
    }

    public Question(Integer questionId, Integer authorId, String title, String text, Date creationDate, int score)
    {
        this.questionId = questionId;
        this.authorId = authorId;
        this.title = title;
        this.text = text;
        this.creationDate = creationDate;
        this.score = score;
    }

    @Override
    public int compareTo(Question o) {
        if(creationDate.compareTo(o.getCreationDate()) == 0)
            return 0;
        else if(creationDate.compareTo(o.getCreationDate()) > 0)
            return 1;
        else
            return -1;
    }


    public void addTags(ArrayList<Tag> tags)
    {

            this.tags = new ArrayList<>(tags);
    }

    @Override
    public String toString()
    {
        String question =
                "Question id: " + questionId +
                "\nQuestion author id: " + authorId +
                "\nQuestion title: " + title +
                "\nQuestion text: " + text +
                "\nQuestion creation date: " + creationDate.toString() +
                "\nQuestion score: " + score +
                "\nQuestion tags: ";
        for(Tag i : tags) {
            question += i.getTitle() + " ";
        }
        return question;
    }

    public Set<String> tagsToString()
    {
        return tags.stream().map(t -> t.getTitle()).collect(Collectors.toSet());
    }
}
