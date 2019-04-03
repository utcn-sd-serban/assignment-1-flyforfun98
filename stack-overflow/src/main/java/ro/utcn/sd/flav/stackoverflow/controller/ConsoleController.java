package ro.utcn.sd.flav.stackoverflow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Component;
import ro.utcn.sd.flav.stackoverflow.entity.*;
import ro.utcn.sd.flav.stackoverflow.exception.*;
import ro.utcn.sd.flav.stackoverflow.service.AccountManagementService;
import ro.utcn.sd.flav.stackoverflow.service.AnswerManagementService;
import ro.utcn.sd.flav.stackoverflow.service.QuestionManagementService;
import ro.utcn.sd.flav.stackoverflow.service.TagManagementService;

import java.security.Permission;
import java.util.*;

@Component
@RequiredArgsConstructor
// Command line runners are executed by Spring after the initialization of the app has been done
// https://www.baeldung.com/spring-boot-console-app
public class ConsoleController implements CommandLineRunner {

    @Transient
    private final List<CommandHandler> commandHandlers = new ArrayList<>();

    private final Scanner scanner = new Scanner(System.in);
    private final AccountManagementService accountManagementService;
    private final QuestionManagementService questionManagementService;
    private final TagManagementService tagManagementService;
    private final AnswerManagementService answerManagementService;
    private ApplicationUser user;


    @Override
    public void run(String... args) {

        commandHandlers.add(new QuestionCommandHandler(questionManagementService, tagManagementService,
                accountManagementService, answerManagementService));
        commandHandlers.add(new AnswerCommandHandler(accountManagementService, answerManagementService, questionManagementService));
        commandHandlers.add(new UserCommandHandler(accountManagementService));

        boolean done = false;
        while (!done) {
            print("Possible commands:   login   register    exit");
            print("Enter a command: ");
            String command = scanner.next().trim();
            //String command = "exit";
            try {
                done = handleCommand(command);
            }
            catch (AccountNotFoundException accountNotFoundException) {
                print("The entered data is invalid. Try again.");
            }
            catch (BannedUserException bannedUserException){
                print("This account is banned.");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private boolean handleCommand(String command) throws Exception {


        switch (command) {
            case "login":
                handleLogIn();
                break;
            case "register":
                handleRegister();
                return false;
            case "exit":
                return true;
            default:
                print("Unknown command. Try again.");
                scanner.nextLine();
                return false;
        }

        scanner.nextLine();

        boolean done = false;
        while(!done) {
            command = scanner.nextLine();
            for (CommandHandler handler : commandHandlers) {
                if (command.equals("exit") && user != null) {
                    user = null;
                    print("You have logged out");
                    done = true;
                } else {
                    if (handler.isCommand(command)) {
                        try {
                            done = handler.handleCommand(user, scanner, command);
                        }
                        catch (AdminNotFoundException adminNotFoundException){
                            print("Operation can be executed just by an ADMIN");
                        }
                        catch (CommandNotFoundException commandNotFoundException){
                            print("Unknown command. Try again.");
                        }
                        catch (AccountExistsException accountExistsException){
                            print("The username or password already exists! Choose another username and password");
                        }
                        catch (QuestionNotFoundException questionExistsException) {
                            print("Not a valid question id");
                        }
                        catch (VoteExistingException voteExistingException) {
                            print("Vote already registered, or user tried to vote itself, or question does not exist");
                        }
                        catch (NotAVoteException notAVoteException) {
                            print("Not a vote");
                        }
                        catch (AnswerNotFoundException answerNotFoundException) {
                            print("Answer id was not found");
                        }
                        catch (AnswerRemovalException answerRemovalException) {
                            print("Operation can be executed just by the ADMIN or the owner of the answer");
                        }
                    }
                }
            }
        }


        return false;
    }

    private void possibleCommands() {

        print("Possible commands for a user:\n" +
                "create question    list questions    search by title    search by tag    add answer\n" +
                "show question    vote question    delete answer    update answer    exit\n" +
                "vote answer    remove question    remove answer    update question    ban user    unban user");
    }

    private void handleRegister() {

        print("Username:");
        String username = scanner.next().trim();
        print("Password:");
        String password = scanner.next().trim();
        user = accountManagementService.register(username, password);
        print("Created user: " + user + ".");
        user = null;
    }

    private void handleLogIn() {

        print("Please introduce the username: ");
        String username = scanner.next().trim();
        print("Please introduce the password: ");
        String password = scanner.next().trim();
        user = accountManagementService.login(username, password);
        print("You have successfully logged in!");
        possibleCommands();

    }




    private void print(String value) {
        System.out.println(value);
    }
}

