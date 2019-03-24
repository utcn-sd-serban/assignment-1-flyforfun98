package ro.utcn.sd.flav.stackoverflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "vote_answer")
@NoArgsConstructor
@AllArgsConstructor
public class VoteAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer voteId;
    private Integer userGivesAnswerId;
    private Integer userGetsAnswerId;
    private Integer answerVotedId;
    private boolean voteType;

    public VoteAnswer(Integer userGivesAnswerId, Integer userGetsAnswerId, Integer answerVotedId, boolean voteType)
    {
        this.userGivesAnswerId = userGivesAnswerId;
        this.userGetsAnswerId = userGetsAnswerId;
        this.answerVotedId = answerVotedId;
        this.voteType = voteType;
    }
}
