package ro.utcn.sd.flav.stackoverflow.repository;

import ro.utcn.sd.flav.stackoverflow.entity.ApplicationUser;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    List<ApplicationUser> findAll();
    ApplicationUser save(ApplicationUser applicationUser);
    void remove(ApplicationUser applicationUser);
    Optional<ApplicationUser> findById(int id);
}
