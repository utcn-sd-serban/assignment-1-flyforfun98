package ro.utcn.sd.flav.stackoverflow.repository;

import ro.utcn.sd.flav.stackoverflow.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {

    List<Tag> findAll();
    Tag save(Tag tag);
    void remove(Tag tag);
    Optional<Tag> findById(int id);
}
