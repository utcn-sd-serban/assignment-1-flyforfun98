package ro.utcn.sd.flav.stackoverflow.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;


@Data
@Entity
@Table(name = "answer")
@NoArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer answerId;
    private Integer authorIdFk;
    private Integer questionIdFk;
    private String text;
    private Date creationDate;
    int score;

    public Answer(Integer authorIdFk, Integer questionIdFk, String text, Date creationDate, int score)
    {
        this.authorIdFk = authorIdFk;
        this.questionIdFk = questionIdFk;
        this.text = text;
        this.creationDate = creationDate;
        this.score = score;
    }

    public Answer(Integer answerId, Integer authorIdFk, Integer questionIdFk, String text, Date creationDate, int score)
    {
        this.answerId = answerId;
        this.authorIdFk = authorIdFk;
        this.questionIdFk = questionIdFk;
        this.text = text;
        this.creationDate = creationDate;
        this.score = score;
    }

    @Override
    public String toString()
    {
        return  "Answer id: " + answerId +
                "\nAnswer author id: " + authorIdFk +
                "\nAnswer's question id: " + questionIdFk +
                "\nAnswer text: " + text +
                "\nAnswer creation date: " + creationDate.toString() +
                "\nAnswer score: " + score;
    }

}
