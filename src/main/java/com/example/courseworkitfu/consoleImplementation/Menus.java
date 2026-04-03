package com.example.courseworkitfu.consoleImplementation;

import com.example.courseworkitfu.model.User;

import java.util.List;
import java.util.Scanner;

public class Menus {

    public static void printUserMenu(Scanner scanner, List<User> users) {
        var cmd = 0;
        while (cmd != 6) {
            System.out.println("""
                    1 - create user
                    2 - edit user
                    3 - delete user
                    4 - view user
                    5 - view all users
                    6 - return to main menu
                    """);
            cmd = scanner.nextInt();
            //When using nextInt, nextDouble, etc. do not forget to add nextLine
            scanner.nextLine();
            switch (cmd) {
                case 1:
                    System.out.println("What type of user do you want to create? A/D/R/C");
                    var userType = scanner.nextLine();
                    switch (userType) {
                        case "A":
                            System.out.println("Enter admin data: username; password; phone number; email");
                            var adminData = scanner.nextLine();
                            var adminDataArray = adminData.split(";");
                            var admin = new User(adminDataArray[0], adminDataArray[1], adminDataArray[2], adminDataArray[3]);
                            users.add(admin);
                            break;
                    }
                    break;
                case 2:
                    System.out.println("Editing user...");
                    break;
                case 5:
                    for (User u : users) {
                        System.out.println(u);
                    }
                    break;
                default:
                    System.out.println("WRONG!");
            }
        }
    }
}
