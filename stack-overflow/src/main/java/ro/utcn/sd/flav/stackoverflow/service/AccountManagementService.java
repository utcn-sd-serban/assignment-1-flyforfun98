package ro.utcn.sd.flav.stackoverflow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.flav.stackoverflow.entity.ApplicationUser;
import ro.utcn.sd.flav.stackoverflow.entity.UserPermission;
import ro.utcn.sd.flav.stackoverflow.entity.UserStatus;
import ro.utcn.sd.flav.stackoverflow.exception.AccountNotFoundException;
import ro.utcn.sd.flav.stackoverflow.repository.AccountRepository;
import ro.utcn.sd.flav.stackoverflow.repository.RepositoryFactory;
import ro.utcn.sd.flav.stackoverflow.repository.memory.InMemoryRepositoryFactory;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AccountManagementService {
    private final RepositoryFactory repositoryFactory;

    @Transactional
    public ApplicationUser addUser(String username, String password, UserPermission permission, UserStatus status, int points)
    {
        return repositoryFactory.createAccountRepository().save(new ApplicationUser(username, password, permission, status, points));
    }

    @Transactional
    public void removeUser(Integer id)
    {
        AccountRepository accountRepository = repositoryFactory.createAccountRepository();
        ApplicationUser applicationUser = accountRepository.findById(id).orElseThrow(AccountNotFoundException::new);
        accountRepository.remove(applicationUser);

    }

    @Transactional
    public List<ApplicationUser> listUsers()
    {
        return repositoryFactory.createAccountRepository().findAll();
    }

    @Transactional
    public ApplicationUser findApplicationUserByUserId(int userId)
    {
        return repositoryFactory.createAccountRepository().findById(userId).orElse(null);
    }

    @Transactional
    public void updateAccount (int id, UserStatus newStatus)
    {
        AccountRepository accountRepository = repositoryFactory.createAccountRepository();

        try {
            ApplicationUser applicationUser = accountRepository.findById(id).orElseThrow(AccountNotFoundException::new);
            applicationUser.setStatus(newStatus);
            accountRepository.save(applicationUser);
        }
        catch(AccountNotFoundException e)
        {
            System.out.println("No user with this id");
        }
    }

    @Transactional
    public ApplicationUser isAccountExistent(String username, String password, boolean register)
    {

        if(register)
        {
            for (ApplicationUser user : listUsers()) {
                if (user.getUsername().equals(username) || user.getPassword().equals(password))
                    return user;
            }
        }

        else {
            for (ApplicationUser user : listUsers()) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password))
                    return user;
            }
        }


        return null;
    }

}
