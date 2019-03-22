package ro.utcn.sd.flav.stackoverflow.repository.data;

import org.springframework.data.repository.Repository;
import ro.utcn.sd.flav.stackoverflow.entity.ApplicationUser;
import ro.utcn.sd.flav.stackoverflow.repository.AccountRepository;

public interface DataAccountRepository extends Repository<ApplicationUser, Integer>, AccountRepository{

    void delete(ApplicationUser applicationUser);

    @Override
    default void remove(ApplicationUser applicationUser){
        delete(applicationUser);
    }
}
