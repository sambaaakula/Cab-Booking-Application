/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package begincbs;
import java.util.Scanner;
/**
 *
 * @author Lenovo
 */
public class BeginCBS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner scan = new Scanner(System.in);
        String pass = "iamadmin";
        char ch;
        System.out.println("Welcome to Cab Booking System");
        do
        {
            System.out.println("Select from the options given below:");
            System.out.println("1.Admin");
            System.out.println("2.User");
            int x = scan.nextInt();
            
            switch(x)
            {
                case 1:
                {
                    System.out.print("Enter the admin password:");
                    String password = scan.next();
                    if(password.equals(pass))
                    {
                        AdminClass a = new AdminClass();
                        a.admin();
                        break;
                    }
                    else
                    {
                        System.out.println("Incorrect Password . Try Again.");
                        break;
                    }
                }
                case 2:
                {
                        EmployeeClass e = new EmployeeClass();
                        e.employee();
                        break;
                }
                default:
                    System.out.println("Entered option is incorrect.");
                    break;
                        
            }
            
            System.out.print("Do you want to continue the login process again? y or n:");
            ch = scan.next().charAt(0);
                
        }while(ch == 'y');
                
    }
    
}
