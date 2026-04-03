package com.example.courseworkitfu.consoleImplementation;

import com.example.courseworkitfu.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StartConsole {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        //I will read from file later
        List<User> users = new ArrayList<>();
        String cmd = "";

        while (!cmd.equals("exit")) {
            System.out.println("""
            Choose action:
            u - work with users
            o - work with orders
            m - work with messages
            r - work with reviews
            q - exit
            """);
            cmd = scanner.nextLine();
           switch (cmd) {
            case "u":
                Menus.printUserMenu(scanner, users);
                break;
            case "o":
                
                break;
            case "m":
                
                break;
            case "r":
                
                break;
            case "q":
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Read again, please");
                break;
           }
        }

    }
}
