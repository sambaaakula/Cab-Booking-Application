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
public class AdminClass {
    
    public void admin()
    {
        Scanner scan = new Scanner(System.in);
        char ch;
        System.out.println("Welcome Admin");
        do
        {
            System.out.println("Enter the operation:");
            System.out.println("1.Cab Details Operation");
            System.out.println("2.Driver Details Operation");
            System.out.println("3.Time slot listing Operation");
            System.out.println("4.Locaton Listing Operation");
            System.out.println("5.Cancel Cab");
            System.out.println("6.Reset Availability or CabStatus.");
            System.out.println("7.Clear Booking History.");
            int x = scan.nextInt();
            switch(x)
            {
                case 1:    //CAB                                                                    
                {
                    Cab c = new Cab();
                    do
                    {
                        System.out.println("Select the operation on cab:");
                        System.out.println("1.Add Cab");
                        System.out.println("2.Modify Cab");
                        System.out.println("3.Delete Cab");
                        
                        int x1 = scan.nextInt();
                        switch(x1)
                        {
                            case 1:
                            {
                                System.out.println("Cab Addition process");
                                System.out.print("Enter the cab number:");
                                int cabnumber = scan.nextInt();
                                System.out.print("Enter the Vehicle Number:");
                                String vehiclenumber = scan.next();
                                System.out.println("Does it have handicap seats?");
                                String ch1 = scan.next();
                                int val = c.add(cabnumber,vehiclenumber,ch1);
                                if(val==1)
                                    System.out.println("Cab Added");
                                else
                                {
                                    if(val == 2)
                                        System.out.println("CabNumber already exists.");
                                    if(val == 3)
                                        System.out.println("Vehicle Number already exists.");
                                    if(val == 0)
                                        System.out.println("Some problem occured.");
                                }
                                break;
                            }
                            case 2:
                            {
                                System.out.println("Cab modification process");
                                System.out.print("Enter the cab number to be modified:");
                                int cabnumber = scan.nextInt();
                                System.out.print("Enter the modified vehicle number:");
                                String vehiclenumber = scan.next();
                                System.out.println("Does it have handicap seats?");
                                String ch1 = scan.next();
                                int val = c.modify(cabnumber,vehiclenumber,ch1);
                                if(val == 1)
                                    System.out.println("Cab Modified");
                                else
                                {
                                    if(val == 2)
                                        System.out.println("CabNumber do not exist.");
                                    if(val == 3)
                                        System.out.println("Vehicle Number already exists.");
                                    if(val == 0)
                                        System.out.println("Some problem occured.");
                                }
                                break;
                            }
                            case 3:
                            {
                                System.out.println("Cab Deletion Process:");
                                System.out.print("Enter the cab number to be deleted:");
                                int cabnumber = scan.nextInt();
                                int val = c.delete(cabnumber);
                                if(val == 1)
                                    System.out.println("Cab Deleted");
                                else
                                {
                                    if(val == 2)
                                        System.out.println("Cab Number do not exist.");
                                    if(val == 0)
                                        System.out.println("Some problem occured.");
                                }
                                break;
                            }
                            default:
                                System.out.println("Incorrect option");
                                break;
                        }
                        System.out.println("Do you want to continue the cab process? y or n");
                        ch = scan.next().charAt(0);
                    }while(ch == 'y');
                    break;
                }
                case 2: //DRIVER
                {                    
                    Driver d = new Driver();
                    do
                    {
                        System.out.println("Select the operation on Driver:");
                        System.out.println("1.Add Driver");
                        System.out.println("2.Modify Driver");
                        System.out.println("3.Delete Driver");
                        int x1 = scan.nextInt();
                        switch(x1)
                        {
                            case 1:
                            {
                                System.out.println("Driver Addition process");
                                System.out.print("Enter the Driver id:");
                                int driverid = scan.nextInt();
                                System.out.print("Enter the Driver Name:");
                                String drivername = scan.next();
                                System.out.print("Enter the driver phone number:");
                                String phonenumber = scan.next();
                                int val = d.add(driverid,drivername,phonenumber);
                                if(val == 1)
                                    System.out.println("Driver Added.");
                                else
                                {
                                    if(val == 2)
                                        System.out.println("DriverId already exists.");
                                    if(val == 3)
                                        System.out.println("DriverName already exists.");
                                    if(val == 4)
                                        System.out.println("Phone Number already exists.");
                                    if(val == 0)
                                        System.out.println("Some problem occured.");
                                }
                                break;
                            }
                            case 2:
                            {
                                System.out.println("Driver Modification process");
                                System.out.print("Enter the Driver id to be modified:");
                                int driverid = scan.nextInt();
                                System.out.print("Enter the new Driver Name:");
                                String drivername = scan.next();
                                System.out.print("Enter the new driver phone number:");
                                String phonenumber = scan.next();
                                int val = d.modify(driverid,drivername,phonenumber);
                                if(val == 1)
                                    System.out.println("Driver Modified.");
                                else
                                {
                                    if(val == 2)
                                        System.out.println("DriverId do not exists.");
                                    if(val == 4)
                                        System.out.println("Phone Number already exists.");
                                    if(val == 0)
                                        System.out.println("Some problem occured.");
                                }
                                break;
                            }
                            case 3:
                            {
                                System.out.println("Driver Deletion Process:");
                                System.out.print("Enter the driver id to be deleted:");
                                int driverid = scan.nextInt();
                                int val = d.delete(driverid);
                                if(val == 1)
                                    System.out.println("Driver Deleted.");
                                else
                                {
                                    if(val == 2)
                                    System.out.println("DriverId do not exist.");
                                    if(val == 0)
                                        System.out.println("Some problem occured.");
                                }
                                break;
                            }
                            default:
                                System.out.println("Incorrect option");
                                 break;
                        }
                        System.out.print("Do you want to continue the driver process? y or n:");
                        ch = scan.next().charAt(0);
                    }while(ch == 'y');
                    break;
                }
                case 3:  //TIMESLOT
                {
                    TimeSlot ts = new TimeSlot();
                    Operations2 o1 = new Operations2();
                    Location l = new Location();
                    int val;
                    do
                    {
                        System.out.println("Select the operation on Time Slot Listing:");
                        System.out.println("1.Add details");
                        System.out.println("2.Modify Details");
                        System.out.println("3.Delete Details");
                        int x1 = scan.nextInt();
                        switch(x1)
                        {
                            case 1:
                            {
                                System.out.println("Time slot addition process");
                                System.out.print("Enter CabTimeIndex:");
                                String cabtimeindex = scan.next();
                                System.out.print("Enter Cab Number:");
                                int cabnumber = scan.nextInt();
                                System.out.print("Enter time:");
                                String time = scan.next();
                                System.out.print("Enter cab capacity:");
                                int capacity = scan.nextInt();
                                int special_seats = o1.checkspecialfacility(cabnumber,capacity);
                                System.out.print("Enter driver id:");
                                int driverid = scan.nextInt();
                                System.out.println("Enter the status of the cab(1:Active,0:Inactive) :");
                                int cabstatus = scan.nextInt();
                                int normal_seats = capacity - special_seats;
                                val = ts.add(cabtimeindex,cabnumber,time,normal_seats,driverid,special_seats,cabstatus);
                                if(val == 1)
                                {
                                    System.out.println("TimeSlot Added.");
                                    System.out.println("Location Entering process.");
                                    System.out.print("Enter the number of locations:");
                                    int n = scan.nextInt();
                                    for(int i=0;i<n;i++)
                                    {
                                        System.out.print("Enter the Location:");
                                        String destination = scan.next();
                                        System.out.print("Enter the orderindex:");
                                        int order = scan.nextInt();
                                        int val2=l.addlocation(cabnumber,destination,order,cabtimeindex);
                                    
                                        if(val2 == 1)
                                            System.out.println("Location and order added.");
                                        else
                                        {
                                            if(val2 == 2)
                                            System.out.println("Order Index already present.");
                                            if(val2 == 3)
                                            System.out.println("Location already present.");
                                            if(val2 == 0)
                                            System.out.println("Some problem occured.");
                                        }
                                    }
                                }   
                                else
                                {
                                    if(val == 2)
                                    System.out.println("Cab Number does not exist.");
                                    if(val == 3)
                                    System.out.println("Driver Id does not exist.");
                                    if(val == 4)
                                    System.out.println("CabTimeIndex is already present.");
                                    if(val == 5)
                                    System.out.println("Cab and that Time is already present");
                                    if(val == 0)
                                    System.out.println("Some problem occured.");
                                }
                                
                                
                                break;
                                
                            }
                            case 2:
                            {
         
                                System.out.println("Time slot modification process");
                                System.out.println("Enter the cabnumber and time whose details is to be modified.");
                                int oldcabnumber = scan.nextInt();
                                String oldtime = scan.next();
                                int val1 = ts.findtimeslot(oldcabnumber,oldtime);
                                if(val1 == 1)
                                {
                                    System.out.println("Enter the details to be modified.");
                                    System.out.print("Enter new Cab Number:");
                                    int cabnumber = scan.nextInt();
                                    System.out.print("Enter new time:");
                                    String time = scan.next();
                                    System.out.print("Enter new cab capacity:");
                                    int capacity = scan.nextInt();
                                    int special_seats = o1.checkspecialfacility(cabnumber,capacity);
                                    System.out.print("Enter new driver id:");
                                    int driverid = scan.nextInt();
                                    System.out.print("Enter cab status:");
                                    int cabstatus = scan.nextInt();
                                    val = ts.modify(oldcabnumber,oldtime,cabnumber,time,capacity-special_seats,cabstatus,driverid,special_seats);
                                    if(val == 1)
                                    {
                                        System.out.println("TimeSlot Modified.");
                                        System.out.println("Location Database also updated.");
                                    }
                                    else
                                    {
                                        if(val == 2)
                                        System.out.println("Cab Number does not exist.");
                                        if(val == 3)
                                        System.out.println("Driver Id does not exist.");
                                        if(val == 5)
                                        System.out.println("Cab and that Time is already present");
                                        if(val == 6)
                                        System.out.println("Updation problem in locationdb");
                                        if(val == 0)
                                        System.out.println("Some problem occured.");
                                        
                                    }
                                }
                                else
                                {
                                    if(val1 == 2)
                                        System.out.println("Cab Number not found.");
                                    if(val1 == 3)
                                        System.out.println("Cab number at the time is not found.");
                                }
                                break;
                            }
                            case 3:
                            {
                                System.out.println("Time Slot Deletion process");
                                System.out.print("Enter the CabNumber:");
                                int cabnumber = scan.nextInt();
                                System.out.println("Enter the Time:");
                                String time = scan.next();
                                val = ts.delete(cabnumber,time);
                                if(val == 1)
                                    System.out.println("TimeSlot deleted.");
                                else
                                {
                                    if(val == 2)
                                        System.out.println("Cab Number do not exist.");
                                    if(val == 3)
                                        System.out.println("Cab do not exist at the given time.");
                                    if(val == 0)
                                        System.out.println("Some problem occured.");
                                }
                                break;
                                
                            }
                            default:
                                System.out.println("Incorrect option");
                                break;
                        }
                        System.out.print("Do you want to continue the time slot process? y or n:");
                        ch = scan.next().charAt(0);
                    }while(ch == 'y');
                    break;
                }
                case 4:  //LOCATION
                {
                    do
                    {
                        Location l = new Location();
                        System.out.println("Select the operation on Location Listing:");
                        System.out.println("1.Add details");
                        System.out.println("2.Modify Details");
                        System.out.println("3.Delete Details");
                        int x1 = scan.nextInt();
                        switch(x1)
                        {
                            case 1:
                            {
                                int val= 0;
                                System.out.println("Location Addition Process:");
                                System.out.print("Enter the cab number:");
                                int cabnumber = scan.nextInt();
                                System.out.print("Enter the CabTimeIndex:");
                                String cabtimeindex = scan.next();
                                int val1= l.checklocation(cabnumber,cabtimeindex);
                                if(val1 == 1)
                                {
                                System.out.print("Enter the OrderIndex:");
                                int orderindex = scan.nextInt();
                                System.out.print("Enter the location:");
                                String location = scan.next();
                                val = l.addlocation(cabnumber, location, orderindex, cabtimeindex);
                                }
                                else
                                {
                                    System.out.println("CabNumber and corresponding cabtimeindex does not exist.");
                                }
                                if(val==1)
                                    System.out.println("Location Details Added.");
                                else
                                    System.out.println("Operation not performed due to some problem.");
                                break;
                                
                            }
                            case 2:
                            {
                                System.out.println("Location Modification Process:");
                                /*System.out.print("Enter the cab number:");
                                int cabnumber = scan.nextInt();
                                System.out.print("Enter the OrderIndex:");
                                int orderindex = scan.nextInt();*/
                                System.out.print("Enter the new location:");
                                String locationnew = scan.next();
                                System.out.print("Enter old location:");
                                String location = scan.next();
                                System.out.print("Enter the CabTimeIndex:");
                                String cabtimeindex = scan.next();
                                int val = l.modify(locationnew,location,cabtimeindex);
                                if(val!=0)
                                    System.out.println("Location Details modified.");
                                else
                                    System.out.println("Operation not performed due to some problem.");
                                break;
                            }
                            case 3:
                            {
                               System.out.println("Location Deletion Process:");
                               System.out.print("Enter the cab number:");
                               int cabnumber = scan.nextInt();
                               System.out.print("Enter the location:");
                               String location = scan.next();
                               System.out.print("Enter the cabtimeindex:");
                               String cabtimeindex = scan.next();
                               int val = l.delete(cabnumber,location,cabtimeindex);
                               if(val!=0)
                                    System.out.println("Location Details Deleted.");
                                else
                                    System.out.println("Operation not performed due to some problem.");
                                break;
                            }
                        }
                        System.out.print("Do you want to continue the location process? y or n:");
                        ch = scan.next().charAt(0);
                    }while(ch == 'y');
                    break;
                }
                case 5:
                {
                    Operations1 o = new Operations1();
                    System.out.println("Enter the cab number to cancel:");
                    int cabnumber = scan.nextInt();
                    System.out.println("Enter the cab time:");
                    String time = scan.next();
                    o.cancelcab1(cabnumber,time);
                    break;
                }
                case 6:
                {
                    Operations1 o = new Operations1();
                    System.out.println("1.Reset Availability.");
                    System.out.println("2.Reset Cab Status");
                    int y1 = scan.nextInt();
                    switch(y1)
                    {
                        case 1:
                        {
                            o.resetavailability();
                            break;
                        }
                        case 2:
                        {
                           o.resetstatus();
                            break;
                        }
                        default:
                            System.out.println("Incorrect option");
                    }
                    break;
                }
                case 7:
                {
                    Operations1 o = new Operations1();
                    o.clearbookinghistory();
                    break;
                }
                default:
                    System.out.println("Incorrect option");
                    break;
            }
            
            System.out.println("Do you want to continue the process? y or n");
            ch = scan.next().charAt(0);
        }while(ch =='y');
    }
    
}
