package ro.utcn.sd.flav.stackoverflow.repository.memory;

import ro.utcn.sd.flav.stackoverflow.entity.Tag;
import ro.utcn.sd.flav.stackoverflow.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class InMemoryTagRepository implements TagRepository{
    
    private final Map<Integer, Tag> tagsMap = new ConcurrentHashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(0);

    @Override
    public List<Tag> findAll() {
        return new ArrayList<>(tagsMap.values());
    }

    @Override
    public Tag save(Tag tag) {
        if(tag.getTagId() == null)
        {
            tag.setTagId(currentId.incrementAndGet());
        }

        tagsMap.put(tag.getTagId(), tag);

        return tag;
    }

    @Override
    public void remove(Tag tag) {

        tagsMap.remove(tag.getTagId());
    }

    @Override
    public Optional<Tag> findById(int id) {

        return Optional.ofNullable(tagsMap.get(id));
    }
}
