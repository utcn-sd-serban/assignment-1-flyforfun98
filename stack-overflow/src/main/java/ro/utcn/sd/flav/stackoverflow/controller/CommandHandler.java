package ro.utcn.sd.flav.stackoverflow.controller;

import ro.utcn.sd.flav.stackoverflow.entity.ApplicationUser;

import java.util.List;
import java.util.Scanner;

public interface CommandHandler {

    boolean isCommand(String command);
    boolean handleCommand(ApplicationUser user, Scanner scanner, String command) throws Exception;
}
