package ro.utcn.sd.flav.stackoverflow.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.flav.stackoverflow.entity.Tag;
import ro.utcn.sd.flav.stackoverflow.exception.TagNotFoundException;
import ro.utcn.sd.flav.stackoverflow.repository.TagRepository;
import ro.utcn.sd.flav.stackoverflow.repository.RepositoryFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagManagementService {

    private final RepositoryFactory repositoryFactory;


    @Transactional
    public Tag addTag(String title)
    {
        return repositoryFactory.createTagRepository().save(new Tag(title));
    }

    @Transactional
    public void removeTag(Integer id)
    {
        TagRepository tagRepository = repositoryFactory.createTagRepository();
        Tag tag = tagRepository.findById(id).orElseThrow(TagNotFoundException::new);
        tagRepository.remove(tag);
    }

    @Transactional
    public List<Tag> listTags()
    {
        return repositoryFactory.createTagRepository().findAll();
    }

    @Transactional
    public void updateTag(int id, String title)
    {
        TagRepository tagRepository = repositoryFactory.createTagRepository();
        Tag tag = tagRepository.findById(id).orElseThrow(TagNotFoundException::new);
        tag.setTitle(title);
        tagRepository.save(tag);
    }

    @Transactional
    public Tag lookForTag(String tag)
    {
        for (Tag i: listTags())
        {
            if(tag.equals(i.getTitle().toLowerCase()))
                return i;
        }

        return addTag(tag);
    }
}
