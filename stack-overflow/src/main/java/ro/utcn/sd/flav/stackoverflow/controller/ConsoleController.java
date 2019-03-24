package ro.utcn.sd.flav.stackoverflow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ro.utcn.sd.flav.stackoverflow.entity.*;
import ro.utcn.sd.flav.stackoverflow.exception.AccountNotFoundException;
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
    private final Scanner scanner = new Scanner(System.in);
    private final AccountManagementService accountManagementService;
    private final QuestionManagementService questionManagementService;
    private final TagManagementService tagManagementService;
    private final AnswerManagementService answerManagementService;
    private ApplicationUser user;


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
        if( accountManagementService.isAccountExistent(username, password, true) == null) {
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


        if( (this.user = accountManagementService.isAccountExistent(username, password, false)) != null )
        {

            if(this.user.getStatus() != UserStatus.BANNED) {
                print("You have successfully logged in!");
                return !handleAccountOperations();
            }
            else
            {
                print("This account is banned");
                return false;
            }

        }

        print("The entered data is invalid. Try again.");
        return false;
    }

    private boolean handleAccountOperations() {

        boolean done = false;

        // To consume the newline
        scanner.nextLine();

        while (!done) {
            print("Possible commands for a user:    'create question'   'list questions'    'search by title'    'search by tag'    " +
                    "'add answer'   'show question'\n                                 'delete answer'    'update answer'    'exit'    " +
                    "'vote question'    'vote answer'   'remove question'   'remove answer'   'update question'  'ban user'   'exit'");

            String command = scanner.nextLine();
            done = handleUserCommand(command);
        }

        return true;
    }

    private boolean handleUserCommand(String command) {

        switch (command) {
            case "create question":
                handleAskQuestion();
                return false;
            case "list questions":
                handleListQuestions();
                return false;
            case "search by tag":
                handleSearchQuestionsByTag();
                return false;
            case "search by title":
                handleSearchQuestionsByTitle();
                return false;
            case "add answer":
                handleAddAnswer();
                return false;
            case "show question":
                handleShowQuestion();
                return false;
            case "delete answer":
                handleDeleteAnswer();
                return false;
            case "update answer":
                handleUpdateAnswerText();
                return false;
            case "vote question":
                handleVoteQuestion();
                return false;
            case "vote answer":
                handleVoteAnswer();
                return false;
            case "remove question":
                handleRemoveQuestion();
                return false;
            case "remove answer":
                handleRemoveAnswer();
                return false;
            case "update question":
                handleUpdateQuestion();
                return false;
            case "ban user":
                banUser();
                return false;
            case "exit":
                return true;
            default:
                print("Unknown command. Try again.");
                return false;
        }
    }



    private void handleAskQuestion() {


        print("Type the question's title");
        String questionTitle = scanner.nextLine();
        print("Type the question's text");
        String questionText = scanner.nextLine();
        Date date = new Date();

        print("Choose one or more tags for the question.\nYou can create new tags or choose an existing tag.\nThe existing tags are: " );
        for (Tag i:tagManagementService.listTags())
        {
            System.out.print(i.getTitle() + "\t\t\t");
        }

        print("");
        ArrayList<Tag> tags = new ArrayList<>();
        boolean insert = true;
        String tagTitle = scanner.next().trim();

        if(!tagTitle.equals("NO"))
        {
            tags.add(tagManagementService.lookForTag(tagTitle));
        }
        else
            insert = false;

        scanner.nextLine();
        while(insert)
        {
            print("Do you want to add another tag? Type NO to cancel, or enter another tag to continue.");
            tagTitle = scanner.next().trim();
            if(tagTitle.equals("NO"))
                insert = false;
            else
            {
                tags.add(tagManagementService.lookForTag(tagTitle));
            }
        }
        if(tags.size() > 0) {
            Question question = questionManagementService.addQuestion(this.user.getUserId(), questionTitle, questionText, new java.sql.Date(date.getTime()), 0, tags);
            print("Created question: " + question.toString() + '.');
        }
        else
            print("No tag entered, the question is cancelled");
        scanner.nextLine();
    }

    private void handleListQuestions() {

        questionManagementService.listQuestions().forEach(q -> print(getUsername(q.getAuthorId()) +
                "'s points: " + getPoints(q.getAuthorId()) + "\n" + q.toString() + "\n\n"));
    }

    private void handleSearchQuestionsByTag() {

        print("Type a tag: ");
        String questionTags = scanner.nextLine();

        Set<String> tags = new HashSet<>(Arrays.asList(questionTags.toLowerCase().split(" ")));

        questionManagementService.filterQuestionByTag(tags).forEach(q -> print(q.toString() + "\n\n"));

    }


    private void handleSearchQuestionsByTitle() {

        print("Type a title: ");
        String questionTitle = scanner.nextLine();
        questionManagementService.filterQuestionByTitle(questionTitle).forEach(q -> print(q.toString() + "\n\n"));

    }



    private void handleAddAnswer() {
        print("Type question id to answer to: ");
        int questionId = scanner.nextInt();

        if(questionManagementService.getQuestionById(questionId) != null) {

            print("The question is: \n");
            print(questionManagementService.getQuestionById(questionId).toString());

            print("\nType answer: ");
            scanner.nextLine();

            String text = scanner.nextLine().trim();

            answerManagementService.addAnswer(this.user.getUserId(), questionId, text);

            int index = answerManagementService.listAnswers(questionId).size() - 1;
            Answer answer = answerManagementService.listAnswers(questionId).get(index);
            print("\n" + answer.toString());
        }
        else
            scanner.nextLine();

    }


    private void handleShowQuestion() {

        print("Type question id you want to see: ");
        int questionId = scanner.nextInt();
        scanner.nextLine();

        Question question = questionManagementService.getQuestionById(questionId);
        if(question != null) {

            int votes = questionManagementService.voteCount(questionId);
            question.setScore(votes);


            int userPoints = getPoints(question.getAuthorId());
            String userName = getUsername(question.getAuthorId());

            print(userName +"'s points: " + userPoints);
            print(question.toString() + "\n");

            List<Answer> answers = answerManagementService.listAnswers(questionId);
            if (answers.isEmpty())
                print("No answers for this question");
            else {

               answers.forEach(a -> a.setScore(answerManagementService.voteCount(a.getAnswerId())));
               answers.forEach(a -> print(getUsername(a.getAuthorIdFk()) + "'s points: " + getPoints(a.getAuthorIdFk()) + "\n" + a.toString() + "\n"));
            }
        }
    }


    private void handleDeleteAnswer() {
        print("Enter answer id to delete: ");
        int answerId = scanner.nextInt();
        scanner.nextLine();

        answerManagementService.removeAnswer(this.user.getUserId(), answerId);
    }

    private void handleUpdateAnswerText() {
        print("Type answer id to edit: ");
        int answerId = scanner.nextInt();
        scanner.nextLine();

        print("Edit answer: ");
        String newText = scanner.nextLine().trim();

        answerManagementService.updateAnswer(this.user.getUserId(), answerId, newText);
    }

    private void handleVoteQuestion() {

        print("Type question id to vote: ");
        int questionId = scanner.nextInt();
        scanner.nextLine();

        print("Type UP or DOWN to vote the question: ");
        String voteText = scanner.next().trim().toUpperCase();

        boolean vote;
        if(voteText.equals("UP") || voteText.equals("DOWN"))
        {
            if(voteText.equals("UP"))
                vote = true;
            else
                vote = false;

            boolean isVoted = questionManagementService.handleVote(this.user.getUserId(), questionId, vote);

            if(!isVoted) {

                questionManagementService.updatePoints(questionId);
                print("Vote successfully saved");
                print("The number of votes for this question is: " + questionManagementService.voteCount(questionId));
            }
            else
                print("Vote already registered, or user tried to vote itself, or question does not exist");

            scanner.nextLine();

        }
        else
            print("Not a vote");
    }



    private void handleVoteAnswer() {

        print("Type answer id to vote: ");
        int answerId = scanner.nextInt();
        scanner.nextLine();

        print("Type UP or DOWN to vote the answer: ");
        String voteText = scanner.next().trim().toUpperCase();

        boolean vote;
        if(voteText.equals("UP") || voteText.equals("DOWN"))
        {
            if(voteText.equals("UP"))
                vote = true;
            else
                vote = false;

            boolean isVoted = answerManagementService.handleVote(this.user.getUserId(), answerId, vote);

            if(!isVoted) {
                answerManagementService.updatePoints(answerId);
                print("Vote successfully saved");
                print("The number of votes for this answer is: " + answerManagementService.voteCount(answerId));
            }
            else
                print("Vote already registered, or user tried to vote itself, or answer does not exist");

            scanner.nextLine();
        }
        else
            print("Not a vote");
    }


    private void handleRemoveAnswer() {

        if(!this.user.getPermission().equals(UserPermission.ADMIN))
            print("Operation can be executed just by an ADMIN");
        else
        {
            print("Select the id of the answer to be removed: ");
            int answerId = scanner.nextInt();
            scanner.nextLine();

            answerManagementService.removeAnswer(this.user.getUserId(), answerId);
        }
    }

    private void handleRemoveQuestion() {

        if(!this.user.getPermission().equals(UserPermission.ADMIN))
            print("Operation can be executed just by an ADMIN");
        else
        {
            print("Select the id of the question to be removed: ");
            int questionId = scanner.nextInt();
            scanner.nextLine();

            questionManagementService.removeQuestion(questionId);
        }
    }


    private void handleUpdateQuestion() {

        if(!this.user.getPermission().equals(UserPermission.ADMIN))
            print("Operation can be executed just by an ADMIN");
        else
        {
            print("Select the id of the question to be updated: ");
            int questionId = scanner.nextInt();
            scanner.nextLine();
            print("Update the title: ");
            String questionTitle = scanner.nextLine();
            print("Update the text: ");
            String questionText = scanner.nextLine();


            questionManagementService.updateQuestion(questionId, questionTitle, questionText);
        }
    }

    private void banUser() {

        if(!this.user.getPermission().equals(UserPermission.ADMIN))
            print("Operation can be executed just by an ADMIN");
        else
        {
            print("Select the id of the user to be banned: ");
            int userId = scanner.nextInt();
            scanner.nextLine();

            accountManagementService.updateAccount(userId, UserStatus.BANNED);

        }

    }


    private int getPoints(Integer authorId) {

        return accountManagementService.findApplicationUserByUserId(authorId).getPoints();
    }

    private String getUsername(Integer authorId) {

        return accountManagementService.findApplicationUserByUserId(authorId).getUsername();
    }


    private void print(String value) {
        System.out.println(value);
    }
}

