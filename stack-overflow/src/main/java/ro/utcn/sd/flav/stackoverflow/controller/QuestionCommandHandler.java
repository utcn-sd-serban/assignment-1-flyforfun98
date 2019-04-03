package ro.utcn.sd.flav.stackoverflow.controller;

import lombok.RequiredArgsConstructor;
import ro.utcn.sd.flav.stackoverflow.entity.*;
import ro.utcn.sd.flav.stackoverflow.exception.CommandNotFoundException;
import ro.utcn.sd.flav.stackoverflow.exception.VoteExistingException;
import ro.utcn.sd.flav.stackoverflow.service.AccountManagementService;
import ro.utcn.sd.flav.stackoverflow.service.AnswerManagementService;
import ro.utcn.sd.flav.stackoverflow.service.QuestionManagementService;
import ro.utcn.sd.flav.stackoverflow.service.TagManagementService;

import java.util.*;

@RequiredArgsConstructor
public class QuestionCommandHandler implements CommandHandler {
    private static final List<String> commands = Arrays.asList("create question", "list questions", "search by tag",
            "search by title", "show question", "vote question", "remove question", "update question");

    private final QuestionManagementService questionManagementService;
    private final TagManagementService tagManagementService;
    private final AccountManagementService accountManagementService;
    private final AnswerManagementService answerManagementService;

    @Override
    public boolean isCommand(String command) {
        return commands.contains(command);
    }

    @Override
    public boolean handleCommand(ApplicationUser user, Scanner scanner, String command) {


        switch (command) {
            case "create question":
                handleAskQuestion(user, scanner);
                return false;
            case "list questions":
                handleListQuestions();
                return false;
            case "search by tag":
                handleSearchQuestionsByTag(scanner);
                return false;
            case "search by title":
                handleSearchQuestionsByTitle(scanner);
                return false;
            case "show question":
                handleShowQuestion(scanner);
                return false;
            case "vote question":
                handleVoteQuestion(user, scanner);
                return false;
            case "remove question":
                handleRemoveQuestion(user, scanner);
                return false;
            case "update question":
                handleUpdateQuestion(user, scanner);
                return false;
            default:
                throw new CommandNotFoundException();

        }
    }

    private void handleAskQuestion(ApplicationUser user, Scanner scanner) {

        print("Type the question's title");
        String questionTitle = scanner.nextLine();
        print("Type the question's text");
        String questionText = scanner.nextLine();

        print("Choose one or more tags for the question.\nYou can create new tags or choose an existing tag.\nThe existing tags are: " );
        for (Tag i:tagManagementService.listTags())
            System.out.print(i.getTitle() + "\t\t\t");

        print("");
        ArrayList<Tag> tags = new ArrayList<>();
        boolean insert = true;
        String tagTitle = scanner.next().trim();

        if(!tagTitle.equals("NO"))
            tags.add(tagManagementService.lookForTag(tagTitle));
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
                tags.add(tagManagementService.lookForTag(tagTitle));
        }
        if(tags.size() > 0) {
            Question question = questionManagementService.addQuestion(user.getUserId(), questionTitle, questionText, tags);
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

    private void handleSearchQuestionsByTag(Scanner scanner) {

        print("Type a tag: ");
        String questionTags = scanner.nextLine();
        Set<String> tags = new HashSet<>(Arrays.asList(questionTags.toLowerCase().split(" ")));
        questionManagementService.filterQuestionByTag(tags).forEach(q -> print(q.toString() + "\n\n"));
    }

    private void handleSearchQuestionsByTitle(Scanner scanner) {

        print("Type a title: ");
        String questionTitle = scanner.nextLine();
        questionManagementService.filterQuestionByTitle(questionTitle).forEach(q -> print(q.toString() + "\n\n"));
    }

    private void handleRemoveQuestion(ApplicationUser user, Scanner scanner) {

        print("Select the id of the question to be removed: ");
        int questionId = scanner.nextInt();
        scanner.nextLine();
        questionManagementService.removeQuestion(user, questionId);
    }

    private void handleUpdateQuestion(ApplicationUser user, Scanner scanner) {

        print("Select the id of the question to be updated: ");
        int questionId = scanner.nextInt();
        scanner.nextLine();
        print("Update the title: ");
        String questionTitle = scanner.nextLine();
        print("Update the text: ");
        String questionText = scanner.nextLine();

        questionManagementService.updateQuestion(questionId, questionTitle, questionText, user);
    }

    private void handleVoteQuestion(ApplicationUser user, Scanner scanner) {

        print("Type question id to vote: ");
        int questionId = scanner.nextInt();
        scanner.nextLine();

        print("Type UP or DOWN to vote the question: ");
        String voteText = scanner.next().trim().toUpperCase();

        boolean isVoted = questionManagementService.handleVote(user.getUserId(), questionId, voteText);
        if(!isVoted) {

            questionManagementService.updatePoints(questionId);
            print("Vote successfully saved");
            print("The number of votes for this question is: " + questionManagementService.voteCount(questionId));
        }
        else
            throw new VoteExistingException();

        scanner.nextLine();
    }

    private void handleShowQuestion(Scanner scanner) {

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
