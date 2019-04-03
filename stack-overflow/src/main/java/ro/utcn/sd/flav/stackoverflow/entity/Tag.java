package ro.utcn.sd.flav.stackoverflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "tag")
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tagId;
    private String title;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.EAGER)
    private List<Question> questions;

    public Tag(String title)
    {
        this.title = title;
    }

    public Tag(Integer tagId, String title)
    {
        this.tagId = tagId;
        this.title = title;
    }

}
