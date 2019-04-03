package ro.utcn.sd.flav.stackoverflow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.flav.stackoverflow.entity.ApplicationUser;
import ro.utcn.sd.flav.stackoverflow.entity.UserPermission;
import ro.utcn.sd.flav.stackoverflow.entity.UserStatus;
import ro.utcn.sd.flav.stackoverflow.exception.AccountExistsException;
import ro.utcn.sd.flav.stackoverflow.exception.AccountNotFoundException;
import ro.utcn.sd.flav.stackoverflow.exception.AdminNotFoundException;
import ro.utcn.sd.flav.stackoverflow.exception.BannedUserException;
import ro.utcn.sd.flav.stackoverflow.repository.AccountRepository;
import ro.utcn.sd.flav.stackoverflow.repository.RepositoryFactory;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AccountManagementService {
    private final RepositoryFactory repositoryFactory;


    @Transactional
    public ApplicationUser login(String username, String password) {

        ApplicationUser user;
        if( (user = isAccountExistent(username, password, false)) != null )
        {

            if(user.getStatus() != UserStatus.BANNED)
                return user;
            else
                throw new BannedUserException();

        }

        throw new AccountNotFoundException();

    }

    @Transactional
    public ApplicationUser register(String username, String password) {

        ApplicationUser user;
        if( isAccountExistent(username, password, true) == null)
            user = addUser(username, password, UserPermission.USER, UserStatus.ALLOWED,0);
        else
            throw new AccountExistsException();

        return user;
    }

    @Transactional
    public void changeUserStatusToBanned(ApplicationUser admin, int userId)
    {
        if(!admin.getPermission().equals(UserPermission.ADMIN))
            throw new AdminNotFoundException();
        else
            updateAccount(userId, UserStatus.BANNED);

    }

    @Transactional
    public void changeUserStatusToUnbanned(ApplicationUser admin, int userId)
    {
        if(!admin.getPermission().equals(UserPermission.ADMIN))
            throw new AdminNotFoundException();
        else
            updateAccount(userId, UserStatus.ALLOWED);

    }

    @Transactional
    private ApplicationUser addUser(String username, String password, UserPermission permission, UserStatus status, int points)
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
    private void updateAccount (int id, UserStatus newStatus)
    {
        AccountRepository accountRepository = repositoryFactory.createAccountRepository();

        try {
            ApplicationUser applicationUser = accountRepository.findById(id).orElseThrow(AccountNotFoundException::new);
            applicationUser.setStatus(newStatus);
            accountRepository.save(applicationUser);
        }
        catch(AccountNotFoundException e)
        {
            throw new AccountNotFoundException();
        }
    }

    @Transactional
    private ApplicationUser isAccountExistent(String username, String password, boolean register)
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
