/**
 * Description: Final Sprint - SD13 - Gym Management Program - Main Class
 * Authors: Asthon Dennis
 *          Joseph Gallant
 *          Justin Greenslade
 * Date(s): August 4, 2025
 */

package MainProgram;

import java.util.ArrayList;
import java.util.Scanner;

import UserManagement.UserDAO;
import Users.*;

public class Main {
    public static void main(String[] args) {
        ArrayList<Role> roles = new ArrayList<>();
        User loggedUser = null;
        Scanner scanner = new Scanner(System.in);

        // Get the roles from the database
        roles = UserDAO.getRoles();

        // TODO - Remove hardcoded users and implement a registration/login system
        Administrator admin = new Administrator(1, "admin", "12345", "Admin", "User", "123, Main", "St. John's", "NL", "A1A1A1", "admin@keyin.com", "(123) 456-7890", roles.get(0));
        Trainer trainer = new Trainer(1, "joseph", "12345", "Joseph", "Gallant", "123, Main", "St. John's", "NL", "A1A1A1", "joseph.gallant@keyin.com", "(123) 456-7890", roles.get(1));
        Member member = new Member(1, "member", "12345", "Member", "User", "123, Main", "St. John's", "NL", "A1A1A1", "member@keyin.com", "(123) 456-7890", roles.get(2));

        loggedUser = member;
        Menu.mainMenu(scanner, roles, loggedUser);
    }
}