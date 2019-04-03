package ro.utcn.sd.flav.stackoverflow.controller;

import lombok.RequiredArgsConstructor;
import ro.utcn.sd.flav.stackoverflow.entity.Answer;
import ro.utcn.sd.flav.stackoverflow.entity.ApplicationUser;
import ro.utcn.sd.flav.stackoverflow.entity.UserPermission;
import ro.utcn.sd.flav.stackoverflow.exception.CommandNotFoundException;
import ro.utcn.sd.flav.stackoverflow.service.AccountManagementService;
import ro.utcn.sd.flav.stackoverflow.service.AnswerManagementService;
import ro.utcn.sd.flav.stackoverflow.service.QuestionManagementService;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class AnswerCommandHandler implements CommandHandler {

    private static final List<String> commands = Arrays.asList("add answer", "delete answer", "update answer",
            "vote answer", "remove answer");

    private final AccountManagementService accountManagementService;
    private final AnswerManagementService answerManagementService;
    private final QuestionManagementService questionManagementService;

    @Override
    public boolean isCommand(String command) {
        return commands.contains(command);
    }

    @Override
    public boolean handleCommand(ApplicationUser user, Scanner scanner, String command){

        switch (command) {
            case "add answer":
                handleAddAnswer(user, scanner);
                return false;
            case "delete answer":
                handleDeleteAnswer(user, scanner);
                return false;
            case "update answer":
                handleUpdateAnswerText(user, scanner);
                return false;
            case "vote answer":
                handleVoteAnswer(user, scanner);
                return false;
            case "remove answer":
                handleRemoveAnswer(user, scanner);
                return false;
            default:
                throw new CommandNotFoundException();
        }
    }

    private void handleAddAnswer(ApplicationUser user, Scanner scanner) {
        print("Type question id to answer to: ");
        int questionId = scanner.nextInt();

        if(questionManagementService.getQuestionById(questionId) != null) {

            print("The question is: \n");
            print(questionManagementService.getQuestionById(questionId).toString());

            print("\nType answer: ");
            scanner.nextLine();

            String text = scanner.nextLine().trim();

            answerManagementService.addAnswer(user.getUserId(), questionId, text);

            int index = answerManagementService.listAnswers(questionId).size() - 1;
            Answer answer = answerManagementService.listAnswers(questionId).get(index);
            print("\n" + answer.toString());
        }
        else
            scanner.nextLine();

    }


    private void handleDeleteAnswer(ApplicationUser user, Scanner scanner) {

        print("Enter answer id to delete: ");
        int answerId = scanner.nextInt();
        scanner.nextLine();
        answerManagementService.removeAnswer(user.getUserId(), answerId);
    }

    private void handleUpdateAnswerText(ApplicationUser user, Scanner scanner) {
        print("Type answer id to edit: ");
        int answerId = scanner.nextInt();
        scanner.nextLine();

        print("Edit answer: ");
        String newText = scanner.nextLine().trim();

        answerManagementService.updateAnswer(user.getUserId(), answerId, newText);
    }


    private void handleVoteAnswer(ApplicationUser user, Scanner scanner) {

        print("Type answer id to vote: ");
        int answerId = scanner.nextInt();
        scanner.nextLine();

        print("Type UP or DOWN to vote the answer: ");
        String voteText = scanner.next().trim().toUpperCase();
        boolean isVoted = answerManagementService.handleVote(user.getUserId(), answerId, voteText);
        if(!isVoted) {
                answerManagementService.updatePoints(answerId);
                print("Vote successfully saved");
                print("The number of votes for this answer is: " + answerManagementService.voteCount(answerId));
            }
        else
                print("Vote already registered, or user tried to vote itself, or answer does not exist");

        scanner.nextLine();
    }


    private void handleRemoveAnswer(ApplicationUser user, Scanner scanner) {

        print("Select the id of the answer to be removed: ");
        int answerId = scanner.nextInt();
        scanner.nextLine();

        answerManagementService.removeAnswer(user.getUserId(), answerId);
    }

    private void print(String value) {
        System.out.println(value);
    }

}
