/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package begincbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author Administrator
 */
public class Operations1 {
    
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
            String query1 = "update timeslotdb set Availability=? where CabTimeIndex=?";
            try
            {
                dbconnection();
                ps = con.prepareStatement(query);
                ps.setString(1,cabtimeindex);
                ResultSet r1 = ps.executeQuery();
                r1.next();
                int count = r1.getInt(4);
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
            String query = "insert into historydb values(?,?,?,?,current_timestamp,'No')";
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
        String query3 = "select l.CabNumber,t.Availability,t.CabTimeIndex from locationdb l,timeslotdb t where Location=? and Time=cast(? as time) and not Availability=0 and l.CabTimeIndex = t.CabTimeIndex and not t.Cab_Status=0";
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
                    String query5 = "select t.CabNumber,t.Time,t.CabTimeIndex from locationdb l,timeslotdb t where Location=? and not Availability=0 and l.CabTimeIndex = t.CabTimeIndex and not t.Cab_Status=0";
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
                String query4 = "select t.CabNumber,l.Location,t.Time,t.Availability,t.CabTimeIndex from locationdb l,timeslotdb t where Location=? and l.CabTimeIndex = t.CabTimeIndex and not t.Cab_Status=0 order by(t.Time)";
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
                        String query5 = "select t.CabNumber,t.Time,t.CabTimeIndex from locationdb l,timeslotdb t where Location=? and not Availability=0 and l.CabTimeIndex = t.CabTimeIndex and not t.Cab_Status=0";
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
            System.out.println("The cab is unavailable. Driver not assigned");
        }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    
    //////////////////////////////////////////////////////////////////////////////////////////////
    
    void incrementcount(String cabtimeindex)
        {
            String query = "select Availability from timeslotdb where CabTimeIndex=?";
            String query1 = "update timeslotdb set Availability=? where CabTimeIndex=?";
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
    
    
    
     public void cancelcab(int empid,int cabnumber,String time)                        //cancel cab by user
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
            ps = con.prepareStatement("delete from historydb where EmployeeId=?");
            ps.setInt(1,empid);
            ps.executeUpdate();
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

     
     
     //////////////////////////////////////////////////////////////////////////////////////////////////////////////
     
     //Cancelling and booking other cabs by admin
     
     
     public void bookcab1(int empid,String dest,String newtime)
     {
         String query1 = "select t.CabNumber,t.Availability,t.CabTimeIndex,t.Time from locationdb l,timeslotdb t where l.Location=? and t.Time=cast(? as Time) and t.CabTimeIndex = l.CabTimeIndex";
         String query2 = "select t.CabNumber,t.Availability,t.CabTimeIndex from locationdb l,timeslotdb t where l.Location=? and t.CabTimeIndex = l.CabTimeIndex and not t.Cab_Status=0 and not t.Availability=0";
         try
         {
             dbconnection();
         ps = con.prepareStatement(query1);
         ps.setString(1,dest);
         ps.setString(2,newtime);
         ResultSet rs1 = ps.executeQuery();
         rs1.next();
         int  availability = rs1.getInt(2);
         
         if(availability != 0)
         {
             int cabnumber = rs1.getInt(1);
             String cabtimeindex = rs1.getString(3);
             updatehistory(empid,cabnumber,dest,newtime,cabtimeindex);
         }
         else
         {
             ps = con.prepareStatement(query2);
             ps.setString(1,dest);
             ResultSet rs2 = ps.executeQuery();
             if(rs2.next())
             {
                int cabnumber = rs2.getInt(1);
                String cabtimeindex = rs2.getString(3);
                updatehistory(empid,cabnumber,dest,newtime,cabtimeindex);
             }
             else
             {
                 System.out.println("No cab further for employee "+empid+"  to destination:"+dest+" since all cabs are full.");
             }
         }
         }
         catch(Exception e)
         {
             e.printStackTrace();
         }
     }
     
     public void bookspecialcab1(int empid,String dest,String newtime)
     {
         String query1 = "select t.CabNumber,t.Special_Availability,t.CabTimeIndex,t.Time from locationdb l,timeslotdb t where l.Location=? and t.Time=cast(? as Time) and t.CabTimeIndex = l.CabTimeIndex";
         String query2 = "select t.CabNumber,t.Special_Availability,t.CabTimeIndex from locationdb l,timeslotdb t where l.Location=? and t.CabTimeIndex = l.CabTimeIndex and not t.Cab_Status=0 and not t.Special_Availability=0";
         try
         {
             Operations2 o1 = new Operations2();
             dbconnection();
         ps = con.prepareStatement(query1);
         ps.setString(1,dest);
         ps.setString(2,newtime);
         ResultSet rs1 = ps.executeQuery();
         rs1.next();
         int  availability = rs1.getInt(2);
         
         if(availability != 0)
         {
             int cabnumber = rs1.getInt(1);
             String cabtimeindex = rs1.getString(3);
             o1.updatehistory(empid,cabnumber,dest,newtime,cabtimeindex);
         }
         else
         {
             ps = con.prepareStatement(query2);
             ps.setString(1,dest);
             ResultSet rs2 = ps.executeQuery();
             if(rs2.next())
             {
                int cabnumber = rs2.getInt(1);
                String cabtimeindex = rs2.getString(3);
                o1.updatehistory(empid,cabnumber,dest,newtime,cabtimeindex);
             }
             else
             {
                 System.out.println("No cab further for employee"+empid+" to destination:"+dest+" since all cabs are full.");
             }
         }
         }
         catch(Exception e)
         {
             e.printStackTrace();
         }
     }





     
     public void cancelcab1(int cabnumber,String time)
     {
         int i=0,j=0;
         //done after cancel(reseting)
         String query_avail = "select IC from timeslotdb where CabNumber=? and Time=cast(? as time)";
         String query_availh= "select ISC from timeslotdb where CabNumber=? and Time=cast(? as time)";
         String query_default = "update timeslotdb set Cab_Status=0,Availability=?,Special_Availability=? where CabNumber=? and Time=cast(? as Time)";
         
         String query_count = "select count(*) from historydb where CabNumber=? and CabTime=cast(? as time) and IsHandicap='No'";
         String queryh_count = "select count(*) from historydb where CabNumber=? and CabTime=cast(? as time) and IsHandicap='Yes'";
         String query = "select EmployeeId,Destination from historydb where CabNumber=? and CabTime=cast(? as time) and IsHandicap='No' order by(TimeOfBooking)";
         String queryh  = "select EmployeeId,Destination from historydb where CabNumber=? and CabTime=cast(? as time) and IsHandicap='Yes' order by(TimeOfBooking)";
         
         String query1 = "select l.Location,t.Time,t.CabTimeIndex,t.CabNumber from timeslotdb t,locationdb l,cabdb c where l.Location=? and not Time=cast(? as time) and not t.Cab_Status=0 and l.CabTimeIndex=t.CabTimeIndex and Time>? and c.CabNumber=t.CabNumber order by(t.Time)";
         String query1h = "select l.Location,t.Time,t.CabTimeIndex,t.CabNumber from timeslotdb t,locationdb l,cabdb c where l.Location=? and not Time=cast(? as time) and not t.Cab_Status=0 and l.CabTimeIndex=t.CabTimeIndex and Time>? and c.Handicap_Seats='Yes' and c.CabNumber=t.CabNumber order by(t.Time)";
         
         String query2 = "delete from historydb where CabNumber=? and CabTime=cast(? as time)";
         try
         {
         dbconnection();
         
         ps = con.prepareStatement(query_avail);
         ps.setInt(1,cabnumber);
         ps.setString(2,time);
         ResultSet rs_iavail = ps.executeQuery();
         rs_iavail.next();
         int init_avail = rs_iavail.getInt(1);
         
         ps = con.prepareStatement(query_availh);
         ps.setInt(1,cabnumber);
         ps.setString(2,time);
         ResultSet rs_iavailh = ps.executeQuery();
         rs_iavailh.next();
         int init_availh = rs_iavailh.getInt(1);
         
         ps = con.prepareStatement(query_default);
         ps.setInt(1,init_avail);
         ps.setInt(2,init_availh);
         ps.setInt(3,cabnumber);
         ps.setString(4,time);
         ps.executeUpdate();
         
         ps=con.prepareStatement(query_count);
         ps.setInt(1,cabnumber);
         ps.setString(2,time);
         ResultSet rs_count = ps.executeQuery();
         rs_count.next();
         int n = rs_count.getInt(1);
         
         ps = con.prepareStatement(queryh_count);
         ps.setInt(1,cabnumber);
         ps.setString(2,time);
         ResultSet rs_hcount = ps.executeQuery();
         rs_hcount.next();
         int nh = rs_hcount.getInt(1);
              
         
         ps = con.prepareStatement(query);
         ps.setInt(1,cabnumber);
         ps.setString(2,time);
         ResultSet r = ps.executeQuery();
         int empid[] = new int[n];
         String dest[] = new String[n];
         
         ps = con.prepareStatement(queryh);
         ps.setInt(1,cabnumber);
         ps.setString(2,time);
         ResultSet rh = ps.executeQuery();
         int empidh[] = new int[nh];
         String desth[] = new String[nh];

         
         while(r.next())
         {
             empid[i]=r.getInt(1);
             dest[i]=r.getString(2);
             i++;
         }
         
         while(rh.next())
         {
             empidh[j]=rh.getInt(1);
             desth[j]=rh.getString(2);
             j++;
         }
         
         ps = con.prepareStatement(query2);
         ps.setInt(1,cabnumber);
         ps.setString(2,time);
         ps.executeUpdate();
         
            for(i=0;i<dest.length;i++)
            {
                ps=con.prepareStatement(query1);
                ps.setString(1,dest[i]);
                ps.setString(2,time);
                ps.setString(3,time);
                ResultSet rs1 = ps.executeQuery();
                if(rs1.next())
                {
                    String newtime = rs1.getString(2);
                    bookcab1(empid[i],dest[i],newtime);
                    
                }
                else
                {
                    System.out.println("No cab further for employee "+empid[i]+"  to destination:"+dest[i]);
                }
            }
            
            for(i=0;i<desth.length;i++)
 	    {
                
                ps=con.prepareStatement(query1h);
                ps.setString(1,desth[i]);
                ps.setString(2,time);
                ps.setString(3,time);
                ResultSet rsh1 = ps.executeQuery();
                if(rsh1.next())
                {
                    String newtime = rsh1.getString(2);
                    bookspecialcab1(empidh[i],desth[i],newtime);
                    
                }
                else
                {
                    System.out.println("No handicap facility cab further for employee "+empidh[i]+"  to destination:"+desth[i]);
                }
            }

        }
        catch(Exception e)
         {
             e.printStackTrace();
         }
     }
    
     
     void checkhistory(int cabnumber,String time)
     {
         String query = "select * from historydb where CabNumber=? and CabTime=cast(? as time)";
         
         try
         {
             dbconnection();
             ps = con.prepareStatement(query);
             ps.setInt(1,cabnumber);
             ps.setString(2,time);
             rs = ps.executeQuery();
             if(rs.next())
             {
               rs.previous();
             while(rs.next())
             {
                 System.out.println("EmployeeId:"+rs.getInt(1)+"  "+"Cab Allotted:"+rs.getInt(2)+"  "+"Cab Time:"+rs.getTime(4));
             }
             }
             else
             {
                 System.out.println("Cab at that time was not cancelled or does not exist");
             }
         }
         catch(Exception e)
         {
             e.printStackTrace();
         }
     }


     void checkhistory(int empid)
     {
        String query = "select * from historydb where EmployeeId=?";
        try
        {
            char ch;
            dbconnection();
            ps = con.prepareStatement(query);
            ps.setInt(1,empid);
            rs = ps.executeQuery();
            if(rs.next())
            {
                String handicap = rs.getString(6);
                System.out.println("EmployeeId:"+empid);
                System.out.println("Cab Assigned after cancellation:"+rs.getInt(2));
                System.out.println("Cab Time:"+rs.getTime(4));
                System.out.print("Do you want to cancel the reallocated cab? y or n:");
                ch = scan.next().charAt(0);
                if(ch == 'y')
                {
                    if(handicap.equals("No"))
                    {
                        cancelcab(empid,rs.getInt(2),rs.getString(4));
                    }
                    if(handicap.equals("Yes"))
                    {
                        Operations2 o1 = new Operations2();
                        o1.cancelcab(empid,rs.getInt(2),rs.getString(4));
                    }
                    
                }
                    
                else
                    System.out.println();
            }
            else
            {
                System.out.println("Sorry. No cab assigned for the given empid.");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
     }



     public void resetavailability()
     {
         String count_query = "select count(*) from timeslotdb";
         String update_query =" update timeslotdb set Availability=? where IC=? and Time<current_timestamp";
         String update_queryh = "update timeslotdb set Special_Availability=? where ISC=? and Time<current_timestamp";
         String select_query = "select IC from timeslotdb";
         String select_queryh ="select ISC from timeslotdb";
         
         try
         {
             int x=0;
             dbconnection();
             ps = con.prepareStatement(count_query);
             ResultSet rs_count = ps.executeQuery();
             rs_count.next();
             int c = rs_count.getInt(1);
             int navail[] = new int[c];
             int savail[] = new int[c]; 
             int i =0;
             ps = con.prepareStatement(select_query);
             ResultSet rs_select = ps.executeQuery();
             ps = con.prepareStatement(select_queryh);
             ResultSet rs_selecth =ps.executeQuery();
             
             while(rs_select.next() && rs_selecth.next())
             {
                 navail[i] = rs_select.getInt(1);
                 savail[i] = rs_selecth.getInt(1);
                 i++;
             }
             
             for(i=0;i<navail.length;i++)
             {
                 ps = con.prepareStatement(update_query);
                 ps.setInt(1,navail[i]);
                 ps.setInt(2,navail[i]);
                 x=ps.executeUpdate();
             }
             
             for(i=0;i<savail.length;i++)
             {
                 ps = con.prepareStatement(update_queryh);
                 ps.setInt(1,savail[i]);
                 ps.setInt(2,savail[i]);
                 x=ps.executeUpdate();
             }
             if(x!=0)
             System.out.println("Values Reseted");
             else
             System.out.println("Nothing to reset");
         }
         catch(Exception e)
         {
             e.printStackTrace();
         }
     }

     
     public void resetstatus()
     {
         String query = "update timeslotdb set Cab_Status=1";
         
         try
         {
             dbconnection();
             ps = con.prepareStatement(query);
             ps.executeUpdate();
             System.out.println("Status reseted.");
         }
         catch(Exception e)
         {
             e.printStackTrace();
         }
     }



    void clearbookinghistory()
    {
        String query = "delete from historydb";
        
        try
        {
            dbconnection();
            ps = con.prepareStatement(query);
            int success = ps.executeUpdate();
            
            if(success!=0)
                System.out.println("Booking History Database Cleared.");
            else
                System.out.println("Some problem occured.");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
    
    //Preferences functions
    
    void addpreferences(int empid,String dest,String disab)
    {
	String query = "insert into employeepref values(?,?,?)";

	try
	{
		dbconnection();
		ps = con.prepareStatement(query);
		ps.setInt(1,empid);	
		ps.setString(2,dest);
		ps.setString(3,disab);
		int x = ps.executeUpdate();

		if(x!=0)
			System.out.println("Preferences added");
		else
			System.out.println("Some error occured");
	}
	catch(Exception e)
	{
		e.printStackTrace();	
	}
    }
    
    void preferencebooking(int empid)
    {
        String query = "select * from employeepref where EmployeeId=?";
        String query_normal = "select t.CabNumber,t.Time from timeslotdb t,locationdb l where l.Location=? and t.CabTimeIndex = l.CabTimeIndex order by(t.Time)";
        String query_handicap = "select t.CabNumber,t.Time from timeslotdb t,locationdb l,cabdb c where l.Location=? and t.CabTimeIndex = l.CabTimeIndex and c.Handicap_Seats='Yes' and c.CabNumber = t.CabNumber order by(t.Time)";

        try
        {
            dbconnection();
	
            ps = con.prepareStatement(query);
            ps.setInt(1,empid);
            ResultSet rs1 = ps.executeQuery();
            
			
            
            char ch;
            if(rs1.next())
            {
                
                String pref_dest = rs1.getString(2);
                String handicap = rs1.getString(3);
	
                ResultSet rs2;
                if(handicap.equals("Yes"))
                {
                        ps = con.prepareStatement(query_handicap);
                        ps.setString(1,pref_dest);
                        rs2 = ps.executeQuery();
                }
                else
                {
                        ps = con.prepareStatement(query_normal);
                        ps.setString(1,pref_dest);
                        rs2 = ps.executeQuery();
                }
                
                if(rs2.next())
                {
                        rs2.previous();
                        System.out.println("Location Preferred:"+pref_dest);
                        System.out.println("Cabs of preferred location and their timings:");
                        while(rs2.next())
                        {
                                int cabnumber = rs2.getInt(1);
                                String time = rs2.getString(2);
                                System.out.println("Cab:"+cabnumber+"   "+"Time:"+time);
                        }

                                System.out.println("Do you want to book any of these timings ? y or n:");
                                ch = scan.next().charAt(0);

                                if(ch == 'y')
                                {
                                        System.out.print("Enter the time:");
                                        String time = scan.next();

                                        if(handicap.equals("No"))
                                        {
                                                bookcab(empid,pref_dest,time);
                                        }
                                        if(handicap.equals("Yes"))
                                        {
                                                Operations2 o1 = new Operations2();
                                                o1.bookcab(empid,pref_dest,time);
                                        }
                                }
                                else
                                {
                                        System.out.println();
                                }
                }
                else
                {
                        System.out.println("No cabs to location:"+pref_dest);
                }
            }
            else
            {
                System.out.println("No preferred destination found for employeeid:"+empid);  
            }
      
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    
    



}