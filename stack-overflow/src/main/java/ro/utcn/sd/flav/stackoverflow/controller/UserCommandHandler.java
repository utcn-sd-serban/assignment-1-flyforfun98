package ro.utcn.sd.flav.stackoverflow.controller;

import lombok.RequiredArgsConstructor;
import ro.utcn.sd.flav.stackoverflow.entity.ApplicationUser;
import ro.utcn.sd.flav.stackoverflow.exception.CommandNotFoundException;
import ro.utcn.sd.flav.stackoverflow.service.AccountManagementService;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class UserCommandHandler implements CommandHandler {

    private static final List<String> commands = Arrays.asList("ban user", "unban user", "list users");
    private final AccountManagementService accountManagementService;

    @Override
    public boolean isCommand(String command) {
        return commands.contains(command);
    }

    @Override
    public boolean handleCommand(ApplicationUser user, Scanner scanner, String command) {

        switch (command) {
            case "ban user":
                banUser(user, scanner);
                return false;
            case "unban user":
                unbanUser(user, scanner);
                return false;
            case "list users":
                handleListUsers();
                return false;
            default:
                throw new CommandNotFoundException();
        }
    }


    private void handleListUsers() {

        accountManagementService.listUsers().forEach(user -> print(user.toString()));
    }

    private void banUser(ApplicationUser user, Scanner scanner) {

        print("Select the id of the user to be banned: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        accountManagementService.changeUserStatusToBanned(user, userId);

    }

    private void unbanUser(ApplicationUser user, Scanner scanner) {

        print("Select the id of the user to be unbanned: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        accountManagementService.changeUserStatusToUnbanned(user, userId);
    }


    private void print(String value) {
        System.out.println(value);
    }
}
