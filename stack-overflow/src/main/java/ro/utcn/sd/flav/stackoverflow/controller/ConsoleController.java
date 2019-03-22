package ro.utcn.sd.flav.stackoverflow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ro.utcn.sd.flav.stackoverflow.entity.ApplicationUser;
import ro.utcn.sd.flav.stackoverflow.entity.UserPermission;
import ro.utcn.sd.flav.stackoverflow.entity.UserStatus;
import ro.utcn.sd.flav.stackoverflow.exception.AccountNotFoundException;
import ro.utcn.sd.flav.stackoverflow.service.AccountManagementService;


import java.util.Scanner;

@Component
@RequiredArgsConstructor
// Command line runners are executed by Spring after the initialization of the app has been done
// https://www.baeldung.com/spring-boot-console-app
public class ConsoleController implements CommandLineRunner {
    private final Scanner scanner = new Scanner(System.in);
    private final AccountManagementService accountManagementService;


    @Override
    public void run(String... args) {
        boolean done = false;
        while (!done) {
            print("Enter a command: ");
            String command = scanner.next().trim();
            try {
                done = handleCommand(command);
            }
            catch (AccountNotFoundException accountNotFoundException) {
                print("The entered data is invalid. Try again.");
            }
        }
    }

    private boolean handleCommand(String command) {

        switch (command) {
            case "login":
                return handleLogIn();
            case "register":
                handleRegister();
                return false;
            case "list":
                handleListUsers();
                return false;
            case "exit":
                return true;
            default:
                print("Unknown command. Try again.");
                return false;
        }
    }

    private void handleListUsers() {

        accountManagementService.listUsers().forEach(user -> print(user.toString()));
    }

    private void handleRegister() {

        print("Username:");
        String username = scanner.next().trim();
        print("Password:");
        String password = scanner.next().trim();
        if(!accountManagementService.isAccountExistent(username, password, true)) {
            ApplicationUser user = accountManagementService.addUser(username, password, UserPermission.USER, UserStatus.ALLOWED,0);
            print("Created user: " + user + ".");
        }
        else
            print("The username or password already exists! Choose another username and password");
    }

    private boolean handleLogIn() {

        print("Please introduce the username: ");
        String username = scanner.next().trim();
        print("Please introduce the password: ");
        String password = scanner.next().trim();


        if( accountManagementService.isAccountExistent(username, password, false) )
        {
            print("You have successfully logged in!");
            return !handleAccountOperations();

        }

        print("The entered data is invalid. Try again.");
        return false;
    }

    private boolean handleAccountOperations() {

        boolean done = false;
        while (!done) {
            print("Possible commands for a user:    'ask'   'search'    'list'    'exit'");
            String command = scanner.next().trim();
            done = handleUserCommand(command);
        }

        return true;
    }

    private boolean handleUserCommand(String command) {

        switch (command) {
            case "ask":
                handleAskQuestion();
                return false;
            case "list":
                handleListQuestions();
                return false;
            case "search":
                handleSearchQuestionsByTag();
                return false;
            case "exit":
                return true;
            default:
                print("Unknown command. Try again.");
                return false;
        }
    }


    private void handleAskQuestion() {
    }

    private void handleListQuestions() {
    }

    private void handleSearchQuestionsByTag() {

    }


    private void print(String value) {
        System.out.println(value);
    }
}

