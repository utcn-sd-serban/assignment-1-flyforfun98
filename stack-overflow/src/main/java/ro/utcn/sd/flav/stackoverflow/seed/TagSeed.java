package ro.utcn.sd.flav.stackoverflow.seed;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.flav.stackoverflow.entity.Tag;
import ro.utcn.sd.flav.stackoverflow.repository.TagRepository;
import ro.utcn.sd.flav.stackoverflow.repository.RepositoryFactory;

@Component
@RequiredArgsConstructor
public class TagSeed{


    private final RepositoryFactory repositoryFactory;


    @Transactional
    public void run(String... args) {

        TagRepository tagRepository = repositoryFactory.createTagRepository();
        if(tagRepository.findAll().isEmpty())
        {
            tagRepository.save(new Tag("memory"));
            tagRepository.save(new Tag("sort"));
            tagRepository.save(new Tag("index-out-of-bounds"));
            tagRepository.save(new Tag("overriding"));
        }

    }
}
