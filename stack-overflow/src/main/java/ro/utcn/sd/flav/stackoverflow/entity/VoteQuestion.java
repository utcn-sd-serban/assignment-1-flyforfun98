package ro.utcn.sd.flav.stackoverflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "vote_question")
@NoArgsConstructor
@AllArgsConstructor
public class VoteQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer voteId;
    private Integer userGivesId;
    private Integer userGetsId;
    private Integer questionVotedId;
    private boolean voteType;

    public VoteQuestion(Integer userGivesId, Integer userGetsId, Integer questionVotedId, boolean voteType)
    {
        this.userGivesId = userGivesId;
        this.userGetsId = userGetsId;
        this.questionVotedId = questionVotedId;
        this.voteType = voteType;
    }
}
