package ro.utcn.sd.flav.stackoverflow.repository.data;

import org.springframework.data.repository.Repository;
import ro.utcn.sd.flav.stackoverflow.entity.Tag;
import ro.utcn.sd.flav.stackoverflow.repository.TagRepository;

public interface DataTagRepository extends Repository<Tag, Integer>, TagRepository {

    void delete(Tag tag);

    @Override
    default void remove(Tag tag){
        delete(tag);
    }
}
