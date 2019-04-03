package ro.utcn.sd.flav.stackoverflow.unittests;

import org.junit.Assert;
import org.junit.Test;
import ro.utcn.sd.flav.stackoverflow.entity.ApplicationUser;
import ro.utcn.sd.flav.stackoverflow.entity.UserPermission;
import ro.utcn.sd.flav.stackoverflow.entity.UserStatus;
import ro.utcn.sd.flav.stackoverflow.exception.AccountNotFoundException;
import ro.utcn.sd.flav.stackoverflow.exception.AdminNotFoundException;
import ro.utcn.sd.flav.stackoverflow.repository.RepositoryFactory;
import ro.utcn.sd.flav.stackoverflow.repository.memory.InMemoryRepositoryFactory;
import ro.utcn.sd.flav.stackoverflow.service.AccountManagementService;
import java.text.ParseException;

public class AccountManagementServiceTest {

    private static RepositoryFactory createMockedFactory() throws ParseException {

        RepositoryFactory repositoryFactory = new InMemoryRepositoryFactory();

        repositoryFactory.createAccountRepository().save(
                new ApplicationUser("flyforfun98", "flavius1", UserPermission.ADMIN, UserStatus.ALLOWED, 0));
        repositoryFactory.createAccountRepository().save(
                new ApplicationUser("flavius1", "parola1", UserPermission.USER, UserStatus.ALLOWED, 0));
        repositoryFactory.createAccountRepository().save(
                new ApplicationUser("flavius2", "parola2", UserPermission.USER, UserStatus.ALLOWED, 0));
        repositoryFactory.createAccountRepository().save(
                new ApplicationUser("flavius3", "parola3", UserPermission.USER, UserStatus.ALLOWED, 0));

        return repositoryFactory;
    }


    @Test(expected = AccountNotFoundException.class)
    public void invalidLogin() throws ParseException {
        RepositoryFactory factory = createMockedFactory();
        AccountManagementService accountService = new AccountManagementService(factory);
        accountService.login("user1234", "user1234");
    }

    @Test
    public void validLogin() throws ParseException {
        RepositoryFactory factory = createMockedFactory();
        AccountManagementService accountService = new AccountManagementService(factory);
        ApplicationUser user = accountService.login("flavius1", "parola1");
        Assert.assertEquals(user, factory.createAccountRepository().findAll().get(1));
    }

    @Test
    public void register() throws ParseException {
        RepositoryFactory factory = createMockedFactory();
        AccountManagementService accountService = new AccountManagementService(factory);
        ApplicationUser user = accountService.register("user1234", "user1234");
        Assert.assertEquals(user, factory.createAccountRepository().findAll().get(4));
    }

    @Test
    public void banUser() throws ParseException {
        RepositoryFactory factory = createMockedFactory();
        AccountManagementService accountService = new AccountManagementService(factory);

        accountService.changeUserStatusToBanned(factory.createAccountRepository().findAll().get(0),
                factory.createAccountRepository().findAll().get(2).getUserId());
        Assert.assertEquals(UserStatus.BANNED, factory.createAccountRepository().findAll().get(2).getStatus());
    }

    @Test
    public void unbanUser() throws ParseException {
        RepositoryFactory factory = createMockedFactory();
        AccountManagementService accountService = new AccountManagementService(factory);

        accountService.changeUserStatusToBanned(factory.createAccountRepository().findAll().get(0),
                factory.createAccountRepository().findAll().get(3).getUserId());
        accountService.changeUserStatusToUnbanned(factory.createAccountRepository().findAll().get(0),
                factory.createAccountRepository().findAll().get(3).getUserId());
        Assert.assertEquals(UserStatus.ALLOWED, factory.createAccountRepository().findAll().get(3).getStatus());
    }

    @Test(expected = AdminNotFoundException.class)
    public void invalidBanUser() throws ParseException {
        RepositoryFactory factory = createMockedFactory();
        AccountManagementService accountService = new AccountManagementService(factory);

        accountService.changeUserStatusToBanned(factory.createAccountRepository().findAll().get(2),
                factory.createAccountRepository().findAll().get(1).getUserId());
    }
}
