package ro.utcn.sd.flav.stackoverflow.repository.jpa;


import lombok.RequiredArgsConstructor;
import ro.utcn.sd.flav.stackoverflow.entity.ApplicationUser;
import ro.utcn.sd.flav.stackoverflow.repository.AccountRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HibernateAccountRepository implements AccountRepository{
    private final EntityManager entityManager;

    @Override
    public List<ApplicationUser> findAll() {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ApplicationUser> query = builder.createQuery(ApplicationUser.class);
        query.select(query.from(ApplicationUser.class));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public ApplicationUser save(ApplicationUser applicationUser) {
        if(applicationUser.getUserId() == null)
        {
            entityManager.persist(applicationUser);
            return applicationUser;
        }
        else
            return entityManager.merge(applicationUser);

    }

    @Override
    public void remove(ApplicationUser applicationUser) {

        entityManager.remove(applicationUser);
    }

    @Override
    public Optional<ApplicationUser> findById(int id) {

        return Optional.ofNullable(entityManager.find(ApplicationUser.class, id));
    }
}
