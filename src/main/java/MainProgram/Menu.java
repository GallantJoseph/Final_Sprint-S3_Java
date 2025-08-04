/**
 * Description: Final Sprint - SD13 - Gym Management Program - Menu Class
 * Authors: Asthon Dennis
 *          Joseph Gallant
 *          Justin Greenslade
 * Date(s): August 4, 2025
 */

package MainProgram;

import java.util.ArrayList;
import java.util.Scanner;

import UserManagement.UserService;
import Users.Role;
import Users.User;

public class Menu {
    public static void MainMenu(Scanner scanner, ArrayList<Role> roles, User loggedUser){
        // TODO:
        // Registration and Login
        // Membership Management

        // If no user is logged in. Show Login/Registration
        if (loggedUser == null) {
            //loggedUser = UserService.login(username, password)
        } else{
            String roleName = null;

            for (Role role: roles) {
                if (loggedUser.getRole().getId() == role.getId()){
                    roleName = role.getName();
                    break;
                }
            }
            
            switch (roleName) {
                case "admin":
                    break;
                case "trainer":
                    break;
                case "member":
                    break;
                default:
                    break;
            }
        }



    }
}
