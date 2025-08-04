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

import Users.Role;

import Users.User;

public class Main {
    public static void main(String[] args) {
        ArrayList<Role> roles = new ArrayList<>();
        User loggedUser = null;
        Scanner scanner = new Scanner(System.in);

        loadRoles(roles);
        Menu.MainMenu(scanner, roles, loggedUser);
    }

    private static void loadRoles(ArrayList<Role> roles) {
        roles.add(new Role(1, "admin"));
        roles.add(new Role(2, "member"));
        roles.add(new Role(3, "trainer"));
    }
}