package begincbs;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class Operations2 {
    
    String url = "jdbc:mysql://localhost:3307/AshwinAsh";
    String username = "root";
    String password = "root";
    PreparedStatement ps;
    ResultSet rs;
    Connection con;
    Scanner scan = new Scanner(System.in);
    
    void dbconnection()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url,username,password);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    void requestadmin(int empid,String dest)
        {
            String query = "insert into requestdb values(?,?,current_timestamp)";
            try
            {
                dbconnection();
                ps = con.prepareStatement(query);
                ps.setInt(1,empid);
                ps.setString(2,dest);
                ps.executeUpdate();
                System.out.println("Request sent.");
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    
       int findtimedifference(String cabtime,String yourtime)
       {
           int diff = 0;
        try
        {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date yourdate = format.parse(yourtime);
        Date cabdate = format.parse(cabtime);
        long difference = cabdate.getTime() - yourdate.getTime(); 
        diff = (int)difference/60000;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
           return diff;
       }
       
       void othertimebooking(int empid,ResultSet rs,String time)
       {
          
           int temp = 0;
           try
           {
                while(rs.next())
               {
                    String time1 = rs.getString(3);
                    LocalTime cabtime = LocalTime.parse(time1);                  //time1=cabtime
                    LocalTime yourtime = LocalTime.parse(time);
                    Boolean isTrue = yourtime.isBefore(cabtime);
                        if(isTrue)
                        {
                            System.out.println("Cab:"+rs.getInt(1)+"  "+"Time:"+rs.getTime(3));
                            temp = 1;
                        }
               }
                rs.first();
                if(temp == 0)
                    System.out.println("No further timings for destination "+rs.getString(2));
                else
                {
                    System.out.print("Do you want to book any of these timings? y or n");
                    char ch = scan.next().charAt(0);
                    if(ch == 'y')
                    {
                        System.out.print("Enter the time:");
                        String t = scan.next();
                        bookcab(empid,rs.getString(2),t);
                    }
                    else
                    {
                        System.out.println();
                    }
                }
             }
           catch(Exception e)
           {
               e.printStackTrace();
           }
       }
        
        
        void decrementcount(String cabtimeindex)
        {
            String query = "select * from timeslotdb where CabTimeIndex=?";
            String query1 = "update timeslotdb set Special_Availability=? where CabTimeIndex=?";
            try
            {
                dbconnection();
                ps = con.prepareStatement(query);
                ps.setString(1,cabtimeindex);
                ResultSet r1 = ps.executeQuery();
                r1.next();
                int count = r1.getInt(6);
                ps = con.prepareStatement(query1);
                ps.setInt(1,count-1);
                ps.setString(2,cabtimeindex);
                ps.executeUpdate();
            }
              
            catch(Exception e)
            {
               e.printStackTrace();
            }
        }
        
        void updatehistory(int empid,int cabnumber,String destination,String time,String cabtimeindex)
        {
            String query = "insert into historydb values(?,?,?,?,current_timestamp,'Yes')";
            try
            {
                dbconnection();
                ps = con.prepareStatement("select * from historydb where EmployeeId=?");
                ps.setInt(1,empid);
                ResultSet r = ps.executeQuery();
                if(!r.next())
                {
                    ps = con.prepareStatement(query);
                    ps.setInt(1,empid);
                    ps.setInt(2,cabnumber);
                    ps.setString(3,destination);
                    ps.setString(4,time);
                    ps.executeUpdate();
                    decrementcount(cabtimeindex);
                    System.out.println("Booking confirmed for "+empid+" in Cab:"+cabnumber);
                }
                else
                {
                    System.out.println("Your booking is done for the day.");
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    
    
    public void bookcab(int empid,String dest,String time)
    {
        String query1 = "select * from locationdb l,timeslotdb t where Location=? and l.CabTimeIndex = t.CabTimeIndex and not t.Cab_Status=0";
        String query2 = "select * from locationdb l,timeslotdb t where Location=? and Time=cast(? as time) and l.CabTimeIndex = t.CabTimeIndex and not t.Cab_Status=0";
        String query3 = "select l.CabNumber,t.Special_Availability,t.CabTimeIndex from locationdb l,timeslotdb t where Location=? and Time=cast(? as time) and not Special_Availability=0 and l.CabTimeIndex = t.CabTimeIndex and not t.Cab_Status=0";
        String query_1 = "select DriverId from locationdb l,timeslotdb t where l.Location=? and t.Time=cast(? as time) and l.CabTimeIndex=t.CabTimeIndex and not t.Cab_Status=0";
        try
        {
        dbconnection();
        ps = con.prepareStatement(query1);
        ps.setString(1,dest);
        ResultSet rs1 = ps.executeQuery();
        
        ps = con.prepareStatement(query2);
        ps.setString(1,dest);
        ps.setString(2,time);
        ResultSet rs2 = ps.executeQuery();
        
        ps = con.prepareStatement(query3);
        ps.setString(1,dest);
        ps.setString(2,time);
        ResultSet rs3 = ps.executeQuery();
        
        ps = con.prepareStatement(query_1);
        ps.setString(1,dest);
        ps.setString(2,time);
        ResultSet rs_1 = ps.executeQuery();
        String val=" ";
        if(rs_1.next())
            val = rs_1.getString(1);
        if(val!=null)
        {
        
        if(rs1.next()) //location satisfied
        {
            if(rs2.next()) //location and time satisfied
            {
                if(rs3.next())       //all satisfied
                {
                    System.out.println("Cab "+rs3.getInt(1)+" is available at time: "+time);
                    System.out.println("Do you want to book ? y or n:");
                    char ch = scan.next().charAt(0);
                    if(ch == 'y')
                    {
                    String cabtimeindex = rs3.getString(3);
                    updatehistory(empid,rs3.getInt(1),dest,time,cabtimeindex);
                    }
                    else
                    {
                        System.out.println();
                    }
                }
                else   //count not satisfied
                {
                    System.out.println("Cab is available at time: "+time+" but it is full.");
                    String query5 = "select t.CabNumber,t.Time,t.CabTimeIndex from locationdb l,timeslotdb t,cabdb c where Location=? and not Special_Availability=0 and l.CabTimeIndex = t.CabTimeIndex and c.Handicap_Seats='Yes' and c.CabNumber=t.CabNumber and not t.Cab_Status=0";
                        ps = con.prepareStatement(query5);
                        ps.setString(1,dest);
                        ResultSet rs5 = ps.executeQuery();
                        if(rs5.next())   //other count satisfied
                        {
                            rs5.previous();
                            while(rs5.next())
                            {
                            System.out.println("Next trip to "+dest+" is at time "+rs5.getTime(2)+" in cab:"+rs5.getInt(1));
                            System.out.println("Do you want to book ? y or n");
                            char ch = scan.next().charAt(0);
                            if(ch == 'y')
                            {
                                String cabtimeindex = rs5.getString(3);
                                updatehistory(empid,rs5.getInt(1),dest,time,cabtimeindex);
                                break;
                            }
                            else
                            {
                                System.out.println();
                                break;
                            }
                            }
                        }
                        else                    //other count delayed
                        {
                            System.out.println("All cabs to destination "+dest+" is full.");
                        }
                }
            }
            else   //time not satisfied
            {
                String query4 = "select t.CabNumber,l.Location,t.Time,t.Special_Availability,t.CabTimeIndex from locationdb l,timeslotdb t where Location=? and l.CabTimeIndex = t.CabTimeIndex and not t.Cab_Status=0 order by(t.Time)";
                ps = con.prepareStatement(query4);
                ps.setString(1,dest);
                ResultSet rs4 = ps.executeQuery();
                while(rs4.next())
                {//rs4.next();
                    if(rs4.getInt(4)!=0)   //count satisfied
                    {
                        int temp = 0;
                        String time1 = rs4.getString(3);
                        int diff = findtimedifference(time1,time);
                        if( diff<=-1 && diff>=-15 )  //further time present
                        {
                            temp = 1;
                            System.out.println("Cab is unavailable for time:"+time);
                            System.out.println("Next trip to location "+dest+" is available at "+time1);
                            System.out.println("Do you want to book or do you want to view other times? y or n");
                            char ch = scan.next().charAt(0);
                            if(ch == 'y')
                            {
                                String cabtimeindex = rs4.getString(5);
                                updatehistory(empid,rs4.getInt(1),dest,time1,cabtimeindex);
                                break;
                            }
                            else
                            {
                                othertimebooking(empid,rs4,time);
                                break;
                            }
                        }
                        else if(diff>=1)
                        {
                            temp = 1;
                            System.out.println("Cab is unavailable for time:"+time);
                            System.out.println("Next trip to location "+dest+" is available at "+time1);
                            System.out.println("Do you want to book or do you want to view other times? y or n");
                            char ch = scan.next().charAt(0);
                            if(ch == 'y')
                            {
                                String cabtimeindex = rs4.getString(5);
                                updatehistory(empid,rs4.getInt(1),dest,time1,cabtimeindex);
                                break;
                            }
                            else
                            {
                                othertimebooking(empid,rs4,time);
                                break;
                            }
                        }
                        else if(diff<=-60)
                        {
                            if(!rs4.next())
                            System.out.println("There are no more cabs to "+dest+" destination.");
                            else
                                rs4.previous();
                        }
                        else       // further time also delayed
                        {
                            if(temp == 1)
                            System.out.println("There are no more cabs to destination:"+dest);
                        }
                    }
                    else   //count not satisfied
                    {
                        String query5 = "select t.CabNumber,t.Time,t.CabTimeIndex from locationdb l,timeslotdb t where Location=? and not Special_Availability=0 and l.CabTimeIndex = t.CabTimeIndex and not t.Cab_Status=0";
                        ps = con.prepareStatement(query5);
                        ps.setString(1,dest);
                        ResultSet rs5 = ps.executeQuery();
                        if(rs5.next())   //other count satisfied
                        {
                            System.out.println("Next trip to "+dest+" is at time "+rs5.getInt(2)+" in cab:"+rs5.getInt(1));
                            System.out.println("Do you want to book ? y or n");
                            char ch = scan.next().charAt(0);
                            if(ch == 'y')
                            {
                                String cabtimeindex = rs5.getString(3);
                                updatehistory(empid,rs5.getInt(1),dest,time,cabtimeindex);
                                break;
                            }
                            else
                            {
                                System.out.println();
                            }
                        }
                        else                    //other count delayed
                        {
                            System.out.println("All cabs to destination "+dest+" is full.");
                        }
                    }
                }
            }
        }
        else     //location not satisfied
        {
            System.out.println("There are no cabs to location:"+dest);
            System.out.print("Do you want to send a request to admin? y or n");
            char ch = scan.next().charAt(0);
            if(ch == 'y')
                requestadmin(empid,dest);
            else
                System.out.println();
        }
        }
        else
        {
            System.out.println("There are no cabs to location:"+dest);
            System.out.print("Do you want to send a request to admin? y or n");
            char ch = scan.next().charAt(0);
            if(ch == 'y')
                requestadmin(empid,dest);
            else
                System.out.println();
        }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
     void incrementcount(String cabtimeindex)
        {
            String query = "select Special_Availability from timeslotdb where CabTimeIndex=?";
            String query1 = "update timeslotdb set Special_Availability=? where CabTimeIndex=?";
            try
            {
                dbconnection();
                ps = con.prepareStatement(query);
                ps.setString(1,cabtimeindex);
                ResultSet rs3 = ps.executeQuery();
                rs3.next();
                int count = rs3.getInt(1);
                ps = con.prepareStatement(query1);
                ps.setInt(1,count+1);
                ps.setString(2,cabtimeindex);
                ps.executeUpdate();
                
            }
            catch(Exception e)
            {
               e.printStackTrace();
            }
        }
    
    
    
     public void cancelcab(int empid,int cabnumber,String time)
    {
        
        
        String query = "delete from historydb where EmployeeId=?";
        String query1 = "select CabTimeIndex from timeslotdb where CabNumber=? and Time=cast(? as time)";      
        
        try
        {
            dbconnection();
            ps = con.prepareStatement(query);
            ps.setInt(1,empid);
            int x = ps.executeUpdate();
            ps = con.prepareStatement(query1);
            ps.setInt(1,cabnumber);
            ps.setString(2,time);
            rs = ps.executeQuery();
            if(x!=0 && rs.next())
            {
            incrementcount(rs.getString(1));
            System.out.println("EmployeeId:"+empid+" booking cancelled.");
            }
            else
            {
                if(x==0)
                    System.out.print("Employee:"+empid+" has not done any booking");
                if(!rs.next())
                    System.out.print("No cab available at given cabnumber or time.");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    
    }
     
     
     ///////////////////////////////////////////////////////////////////////////////////////////////
     
     
     

    void viewspecialcabs()
    {
        String query = "select * from cabdb where Handicap_Seats='Yes'";
       
        try
        {
            dbconnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            
            while(rs.next())
            {
                System.out.println("Cab :"+rs.getInt(1));
            }
        }
        catch(Exception e)
        {
             e.printStackTrace();
        }
    }
 
    void specialcabsbooking()
    {
            String query = "select distinct(l.Location) from locationdb l,cabdb c where c.CabNumber in (select CabNumber from cabdb where Handicap_Seats='Yes') and l.CabNumber = c.CabNumber";
            String query1 = "select l.Location,t.Time,t.Special_Availability,t.CabNumber from locationdb l ,timeslotdb t where l.CabTimeIndex = t.CabTimeIndex and not Special_Availability=0 and l.Location=? order by(t.Time) ";
            try
            {
                dbconnection();
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();
                while(rs.next())
                {
                    String x = rs.getString(1);
                    
                    ps = con.prepareStatement(query1);
                    ps.setString(1,x);
                    ResultSet rs1 = ps.executeQuery();
                    while(rs1.next())
                    {
                        System.out.println("CabNumber:"+rs1.getInt(4)+"  "+"Destination:"+rs1.getString(1)+"  "+"Time:"+rs1.getTime(2)+"  "+"Special Availability:"+rs1.getInt(3));
                    }
                }    
        
                    System.out.println("Do you want to book any of these timings? y or n");
                    char ch = scan.next().charAt(0);
                    if(ch == 'y')
                    {
                        System.out.print("Enter employeeid:");
                        int empid = scan.nextInt();
                        System.out.print("Enter the destination:");
                        String dest = scan.next();
                        System.out.print("Enter time:");
                        String time = scan.next();
                        bookcab(empid,dest,time);
                    }
                    else
                    {
                        System.out.println();
                    }
                    
                    
                }
               
            catch(Exception e)
            {
                e.printStackTrace();
            }
    }
    
    //Reserved Seats Allocation
    int checkspecialfacility(int cabnumber,int capacity)
    {
        String query = "select * from cabdb where CabNumber=? and Handicap_Seats='Yes'";
        int special_seats=0;
        try
        {
            dbconnection();
            ps = con.prepareStatement(query);
            ps.setInt(1,cabnumber);
            rs = ps.executeQuery();
            
            if(rs.next())
            {
                System.out.println("How many seats do you want to reserve for handicap?:");
                special_seats = scan.nextInt();
            }
            else
            {
                special_seats = 0;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return special_seats;
    }
}

    

