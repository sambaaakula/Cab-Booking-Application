/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package begincbs;
import static java.sql.JDBCType.NULL;
import java.util.Scanner;

/**
 *
 * @author Administrator
 */
public class EmployeeClass {
    
    
    public void employee()
    {
        char ch;
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome Employee");
        do
        {
            System.out.println("Select any option given below.");
            System.out.println("1.View Time Slots for the Cab");
            System.out.println("2.View Locations traveled by the Cab");
            System.out.println("3.Book Cab");
            System.out.println("4.Cancel Cab.");
            System.out.println("5.Special Booking");
            System.out.println("6.Special Booking Cancellation");
            System.out.println("7.Cancelled cab status");
            System.out.println("8.Guest Booking.");
            System.out.println("9.Preferences Booking");
            
            int x = scan.nextInt();
            switch(x)
            {
                case 1:
                {
                 TimeSlot ts = new TimeSlot();
                    do
                    {
                     System.out.println("1.Display all cabs its time and its availability.");
                     System.out.println("2.Display particular cab with its time and availability");
                     
                     int x1 = scan.nextInt();
                        switch(x1)
                        {
                            case 1:
                            {
                             ts.timeslotview();
                             break;
                            }
                            case 2:
                            {
                             System.out.print("Enter the cab number:");
                             int cabnumber = scan.nextInt();
                             ts.timeslotview(cabnumber);
                             break;
                            }
                            default:
                                System.out.println("Incorrect option");
                                 break;
                        }
                        System.out.print("Do you want to repeat? y or n:");
                        ch = scan.next().charAt(0);
                    }while(ch == 'y');
                    break;
                }
                case 2:
                {
                    Location l = new Location();
                    do
                    {
                     System.out.println("1.Display all cabs its destinations and order. ");
                     System.out.println("2.Display particular cab its destinations and order.");
                     
                     int x1 = scan.nextInt();
                     switch(x1)
                     {
                         case 1:
                         {
                             l.locationview();
                             break;
                         }
                         case 2:
                         {
                             System.out.print("Enter the cab number:");
                             int cabnumber = scan.nextInt();
                             l.locationview(cabnumber);
                             break;
                         }
                         default:
                            System.out.println("Incorrect option");
                            break;
                     }
                     System.out.print("Do you want to repeat? y or n:");
                     ch = scan.next().charAt(0);
                    }while(ch == 'y');
                    break;
                }
                case 3:
                {
                    Operations1 o = new Operations1();
                    System.out.println("Cab Booking Process.");
                    System.out.print("Enter the employee id:");
                    int empid = scan.nextInt();
                    System.out.print("Enter the location:");
                    String dest = scan.next();
                    System.out.print("Enter the time for travel:");
                    String time = scan.next();
                    o.bookcab(empid,dest,time);
                    break;
                }
                case 4:
                {
                    Operations1 o = new Operations1();
                    System.out.println("Cab Cancellation Process");
                    System.out.print("Enter the Employee Id:");
                    int empid = scan.nextInt();
                    System.out.print("Enter the Cab Number you were booked for :");
                    int cabnumber = scan.nextInt();
                    System.out.print("Enter the Cab Timing:");
                    String time = scan.next();
                    o.cancelcab(empid,cabnumber,time);
                    break;
                }
                case 5:
                {
                    Operations2 o1 = new Operations2();
                    System.out.println("Special Bookings");
                    do
                    {
                        System.out.println("1.View Handicap Facility Cab");
                        System.out.println("2.Book cab");
                        
                        int x5 = scan.nextInt();
                        switch(x5)
                        {
                            case 1:
                            {
                                o1.viewspecialcabs();
                                break;
                            }
                            case 2:
                            {
                                o1.specialcabsbooking();
                                break;
                                
                            }
                            default:
                                System.out.println("Incorrect Option");
                        }
                        System.out.print("Do you want to repeat? y or n:");
                        ch = scan.next().charAt(0);    
                        
                    }while(ch == 'y');
                    break;
                }
                case 6:
                {
                    Operations2 o1 = new Operations2();
                    System.out.println("Cab Cancellation Process");
                    System.out.print("Enter the Employee Id:");
                    int empid = scan.nextInt();
                    System.out.print("Enter the Cab Number you were booked for :");
                    int cabnumber = scan.nextInt();
                    System.out.print("Enter the Cab Timing:");
                    String time = scan.next();
                    o1.cancelcab(empid,cabnumber,time);
                    break;
                }
                case 7:
                {
                    Operations1 o = new Operations1();
                    /*char ch1;
                    do
                    {
                    System.out.println("1.View all cancelled allocations:");
                    System.out.println("2.View allocation by employee id:");
                    int y = scan.nextInt();
                    switch(y)
                    {
                        case 1:
                        {
                            System.out.print("Enter cab number:");
                            int cabnumber = scan.nextInt();
                            System.out.print("Enter time");
                            String time = scan.next();
                            o.checkhistory(cabnumber,time);
                            break;
                        }
                        case 2:
                        {*/
                            System.out.print("Enter the employee id:");
                            int empid = scan.nextInt();
                            o.checkhistory(empid);
                            break;
                        /*}
                        default:
                            System.out.println("Incorrect Option.");
                    }
                    //System.out.print("Do you want to continue? y or n:");
                    ch1 = scan.next().charAt(0);
                    }while(ch1 == 'y');*/
                }
                case 8:
                {
                    Operations1 o = new Operations1();
                    System.out.println("Welcome to guest Booking.");
                    System.out.print("Enter the destination:");
                    String dest = scan.next();
                    System.out.println("Enter the time:");
                    String time = scan.next();
                    o.bookcab(0,dest,time);
                    break;
                }
                case 9:
                {
                    Operations1 o = new Operations1();
                    System.out.println("1.Add Preference Destination");
                    System.out.println("2.Book by preferences");
                    int x3 = scan.nextInt();
                    switch(x3)
                    {
                        case 1:
                        {
                            System.out.print("Enter employee id:");
                            int empid = scan.nextInt();
                            System.out.print("Enter Destination Preference:");
                            String dest = scan.next();
                            System.out.print("Any Disability? Yes or No: ");
                            String disab = scan.next();
                            o.addpreferences(empid,dest,disab);
                            break;
                        }
                        case 2:
                        {
                            System.out.print("Enter the employee id:");
                            int empid = scan.nextInt();
                            o.preferencebooking(empid);
                            break;
                        }
                        default:
                            System.out.println("Incorrect option");
                            break;
                    }
                   break; 
                }
                default:
                    System.out.println("Incorrect option");
                    break;
            }
            System.out.print("Do you want to go to Main Menu? y or n:");
            ch = scan.next().charAt(0);
            
        }while(ch == 'y');
    }
    
}
