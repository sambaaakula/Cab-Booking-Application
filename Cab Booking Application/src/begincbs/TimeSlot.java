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

/**
 *
 * @author Administrator
 */
public class TimeSlot {
    
    String url = "jdbc:mysql://localhost:3307/AshwinAsh";
    String username = "root";
    String password = "root";
    PreparedStatement ps;
    ResultSet rs;
    Connection con;
    String select_query1 = "select * from cabdb where CabNumber=?";
    String select_query2 = "select * from driverdb where DriverId=?";
    String select_query3= "select * from timeslotdb where CabTimeIndex=?";
    String select_query4 ="select * from timeslotdb where CabNumber=? and Time=cast(? as time)";
    String select_query5 = "select * from timeslotdb where CabNumber=?";
    
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
    
    public int add(String cabtimeindex,int cabnumber,String time,int normal_seats,int driverid,int special_seats,int cab_status)
    {
        int r=0;
        String query = "insert into timeslotdb values(?,?,cast(? as time),?,?,?,?,?,?)";
        try
        {
            dbconnection();
            ps = con.prepareStatement(select_query1);
            ps.setInt(1,cabnumber);
            ResultSet rs1 = ps.executeQuery();
            
            ps = con.prepareStatement(select_query2);
            ps.setInt(1,driverid);
            ResultSet rs2 = ps.executeQuery();
            
            ps = con.prepareStatement(select_query3);
            ps.setString(1,cabtimeindex);
            ResultSet rs3 = ps.executeQuery();
            
            ps = con.prepareStatement(select_query4);
            ps.setInt(1,cabnumber);
            ps.setString(2,time);
            ResultSet rs4 = ps.executeQuery();
            
        if(rs1.next() && rs2.next() && !rs3.next() && !rs4.next())
        {
        ps = con.prepareStatement(query);
        ps.setString(1,cabtimeindex);
        ps.setInt(2,cabnumber);
        ps.setString(3,time);
        ps.setInt(4,normal_seats);
        ps.setInt(5,driverid);
        ps.setInt(6,special_seats);
        ps.setInt(7,cab_status);
        ps.setInt(8,normal_seats);
        ps.setInt(9,special_seats);
        r = ps.executeUpdate();
        }
        else
        {
            rs1.previous();
            rs2.previous();
            rs3.previous();
            rs4.previous();
            if(!rs1.next())
                r=2;
            else if(!rs2.next())
                r=3;
            else if(rs3.next())
                r=4;
            else if(rs4.next())
                r=5;
            else
                r=0;
        }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return r;
    }
    
    public int delete(int cabnumber,String time)
    {
        int r=0;
        String query = "delete from timeslotdb where CabNumber=? and Time=cast(? as time)";
        try
        {
        dbconnection();
        
        ps = con.prepareStatement(select_query5);
        ps.setInt(1,cabnumber);
        ResultSet rs5 = ps.executeQuery();
        
        ps = con.prepareStatement(select_query4);
        ps.setInt(1,cabnumber);
        ps.setString(2,time);
        ResultSet rs4 = ps.executeQuery();
        
        if(rs4.next())
        {
        ps = con.prepareStatement(query);
        ps.setInt(1,cabnumber);
        ps.setString(2,time);
        r = ps.executeUpdate();
        }
        else
        {
            rs4.previous();
            if(!rs5.next())
                r=2;
            else if(!rs4.next())
                r=4;
            else
                r=0;
        }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return r;
    }
    
    
    public int modify(int oldcabnumber,String oldtime,int cabnumber,String time,int normal_seats,int cabstatus,int driverid,int special_seats)
    {
        Location l = new Location();
        int r=0,r1=0;
        String query = "select * from timeslotdb where CabNumber=? and Time=cast(? as time)";
        String query1 = "update timeslotdb set CabNumber=?,Time=cast(? as time),Availability=?,DriverId=?,Special_Availability=?,Cab_Status=?,IC=?,ISC=? where CabTimeIndex=?";
        String query2 = "select * from timeslotdb where CabNumber=? and Time=cast(? as time) and CabTimeIndex != ?";
        try
        {
            dbconnection();
            ps = con.prepareStatement(query);
            ps.setInt(1,oldcabnumber);
            ps.setString(2,oldtime);
            rs = ps.executeQuery();
            rs.next();
            String cabtimeindex = rs.getString(1);
            
            ps = con.prepareStatement(select_query1);
            ps.setInt(1,cabnumber);
            ResultSet rs1 = ps.executeQuery();
            
            ps = con.prepareStatement(select_query2);
            ps.setInt(1,driverid);
            ResultSet rs2 = ps.executeQuery();
            
            ps = con.prepareStatement(query2);
            ps.setInt(1,cabnumber);
            ps.setString(2,time);
            ps.setString(3,cabtimeindex);
            ResultSet rs4 = ps.executeQuery();
            
            if(rs1.next() && rs2.next() && !rs4.next())
            {
                ps = con.prepareStatement(query1);
                ps.setInt(1,cabnumber);
                ps.setString(2,time);
                ps.setInt(3,normal_seats);
                ps.setInt(4,driverid);
                ps.setInt(5,special_seats);
                ps.setInt(6,cabstatus);
                ps.setInt(7,normal_seats);
                ps.setInt(8,special_seats);
                ps.setString(9,cabtimeindex);
                r = ps.executeUpdate();
                r1 = l.changelocationdbcabnumber(oldcabnumber,cabnumber,cabtimeindex);
                if(r1 == 0)
                    System.out.println("Error");
            }
            else
            {
                rs1.previous();
                rs2.previous();
                rs4.previous();
                if(!rs1.next())
                r=2;
                else if(!rs2.next())
                r=3;
                else if(rs4.next())
                r=5;
                else if(r1 == 0)
                r=6;
                else
                r=0;
            }
        }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return r;
    }
    
    void timeslotview()
    {
        String query = "select CabNumber,Time,Availability,Special_Availability from timeslotdb order by(Time)";
        try
        {
            dbconnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            if(rs.next())
            {
                rs.previous();
                while(rs.next())
                {
                    System.out.println("CabNumber:"+rs.getInt(1)+"  "+"Time:"+rs.getTime(2)+"  "+"Availability:"+rs.getInt(3)+"  "+"Special_Availability:"+rs.getInt(4));
                }
            }
            else
            {
                System.out.println("The database is empty");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    
    void timeslotview(int cabnumber)
    {
        String query = "select CabNumber,Time,Availability,Special_Availability from timeslotdb where CabNumber =? order by(Time)";
        try
        {
            dbconnection();
            ps = con.prepareStatement(query);
            ps.setInt(1,cabnumber);
            rs = ps.executeQuery();
            if(rs.next())
            {
                rs.previous();
                while(rs.next())
                {
                    System.out.println("CabNumber:"+rs.getInt(1)+"  "+"Time:"+rs.getTime(2)+"  "+"Availability:"+rs.getInt(3)+"  "+"Special_Availability:"+rs.getInt(4));
                }
            }
            else
            {
                System.out.println("There is no cab:"+cabnumber);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    
    int findtimeslot(int cabnumber,String time)
    {
        int r=0;
        try
        {
            dbconnection();
            ps = con.prepareStatement(select_query5);
            ps.setInt(1,cabnumber);
            ResultSet rs5 = ps.executeQuery();
        
            ps = con.prepareStatement(select_query4);
            ps.setInt(1,cabnumber);
            ps.setString(2,time);
            ResultSet rs4 = ps.executeQuery();
        
            if(rs4.next())
            {
                r=1;
            }
            else
            {
                rs4.previous();
                if(!rs5.next())
                    r=2;
                else if(!rs4.next())
                    r=3;
                else
                    r=0;
               
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return r;
    }
}

    
