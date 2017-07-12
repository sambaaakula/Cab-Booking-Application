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
 * @author Lenovo
 */
public class Driver {
    
    String url = "jdbc:mysql://localhost:3307/AshwinAsh";
    String username = "root";
    String password = "root";
    PreparedStatement ps;
    ResultSet rs;
    Connection con;
    String select_query1 = "select * from driverdb where DriverId=?";
    String select_query2 = "select * from driverdb where DriverName=?";
    String select_query3 = "select * from driverdb where PhoneNumber=?";
    
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
    
    public int add(int driverid,String drivername,String phonenumber)
    {
        int r=0;
        String query = "insert into driverdb values(?,?,?)";
        try
        {
        dbconnection();
        //checking whether driverid already exists
        ps = con.prepareStatement(select_query1);
        ps.setInt(1,driverid);
        ResultSet rs1 = ps.executeQuery();
        
        //Checking whether drivername already exists
        ps = con.prepareStatement(select_query2);
        ps.setString(1,drivername);
        ResultSet rs2 = ps.executeQuery();
        
        //checking whether phonenumber already exists
        ps = con.prepareStatement(select_query3);
        ps.setString(1,phonenumber);
        ResultSet rs3 = ps.executeQuery();
        
        if(!rs1.next() && !rs2.next() && !rs3.next())
        {
            ps = con.prepareStatement(query);
            ps.setInt(1,driverid);
            ps.setString(2,drivername);
            ps.setString(3,phonenumber);
            r = ps.executeUpdate();
        }
        else
        {
            rs1.previous();
            rs2.previous();
            rs3.previous();
            if(rs1.next())
                r=2;
            if(rs2.next())
                r=3;
            if(rs3.next())
                r=4;
        }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return r;
    }
    
    public int modify(int driverid,String drivername,String phonenumber)
    {
        int r=0;
        String query = "update driverdb set DriverName=?,PhoneNumber=? where DriverId=?";
        String query1 ="update timeslotdb set Cab_Status=1 where DriverId=?";
        try
        {
        dbconnection();
        //checking whether driverid already exists
        ps = con.prepareStatement(select_query1);
        ps.setInt(1,driverid);
        ResultSet rs1 = ps.executeQuery();
       
        
        //checking whether phonenumber already exists
        ps = con.prepareStatement(select_query3);
        ps.setString(1,phonenumber);
        ResultSet rs3 = ps.executeQuery();
        
        if(rs1.next()  && !rs3.next())
        {
        ps = con.prepareStatement(query);
        ps.setString(1,drivername);
        ps.setString(2,phonenumber);
        ps.setInt(3,driverid);
        PreparedStatement ps1 = con.prepareStatement(query1);
        ps1.setInt(1,driverid);
        ps1.executeUpdate();
        r = ps.executeUpdate();
        }
        else
        {
            rs1.previous();
            rs3.previous();
            if(!rs1.next())
                r=2;
            if(rs3.next())
                r=4;
            if(!rs1.next() && rs3.next())
                r=5;
        }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return r;
    }
    
    public int delete(int driverid)
    {
        int r=0;
        String query = "delete from driverdb where DriverId=?";
        String query1 = "update timeslotdb set Cab_Status=0 where DriverId=?";
        try
        {
        dbconnection();
        //checking whether driverid already exists
        ps = con.prepareStatement(select_query1);
        ps.setInt(1,driverid);
        ResultSet rs1 = ps.executeQuery();
        if(rs1.next())
        {
        ps = con.prepareStatement(query);
        ps.setInt(1,driverid);
        PreparedStatement ps1 = con.prepareStatement(query1);
        ps1.setInt(1,driverid);
        ps1.executeUpdate();
        r = ps.executeUpdate();
        
        }
        else
        {
            rs1.previous();
          if(!rs1.next())
              r=2;
        }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return r;
    }
    
}
