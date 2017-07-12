/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package begincbs;

import java.sql.DriverManager;
import java.sql.*;
/**
 *
 * @author Lenovo
 */
public class Cab {
    String url = "jdbc:mysql://localhost:3307/AshwinAsh";
    String username = "root";
    String password = "root";
    PreparedStatement ps;
    ResultSet rs;
    Connection con;
    String select_query = "select * from cabdb where CabNumber=?";
    String select_query2 = "select * from cabdb where VehicleNumber=?";
    
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
    
    public int add(int cabnumber,String vehiclenumber,String ch1)
    {
        int r=0;
        String query = "insert into cabdb values(?,?,?)";
        
        try
        {
        dbconnection();
        //Checking whether cab already exists.
        ps = con.prepareStatement(select_query);
        ps.setInt(1,cabnumber);
        ResultSet rs1 = ps.executeQuery();
        
        //Checking whether vehicle number already exists
        ps = con.prepareStatement(select_query2);
        ps.setString(1,vehiclenumber);
        ResultSet rs2 = ps.executeQuery();
            
            if(!rs1.next() && !rs2.next())
            {
                ps = con.prepareStatement(query);
                ps.setInt(1,cabnumber);
                ps.setString(2,vehiclenumber);
                ps.setString(3,ch1);
                r = ps.executeUpdate();
            }
            
            else
            {
                rs1.previous();
                rs2.previous();
                if(rs1.next())
                    r=2;
                if(rs2.next())
                    r=3;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return r;
    }
    
    public int modify(int cabnumber,String vehiclenumber,String ch1)
    {
        int r=0;
        String query = "update cabdb set VehicleNumber=?,Handicap_Seats=? where CabNumber=?";
        try
        {
        dbconnection();
        //Checking whether cab already exists.
        ps = con.prepareStatement(select_query);
        ps.setInt(1,cabnumber);
        ResultSet rs1 = ps.executeQuery();
        
        //Checking whether vehicle number already exists
        ps = con.prepareStatement(select_query2);
        ps.setString(1,vehiclenumber);
        ResultSet rs2 = ps.executeQuery();
        
        if(rs1.next() && !rs2.next())
        {
            ps = con.prepareStatement(query);
            ps.setString(1,vehiclenumber);
            ps.setString(2,ch1);
            ps.setInt(3,cabnumber);
            r = ps.executeUpdate();
        }
        else
        {
            rs1.previous();
            rs2.previous();
            if(!rs1.next())
                r=2;
            if(rs2.next())
                r=3;
        }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return r;
    }
    
    public int delete(int cabnumber)
    {
        int r=0;
        String query = "delete from cabdb where CabNumber=?";
        try
        {
        dbconnection();
        //Checking whether cab already exists.
        ps = con.prepareStatement(select_query);
        ps.setInt(1,cabnumber);
        ResultSet rs1 = ps.executeQuery();
        if(rs1.next())
        {
        ps = con.prepareStatement(query);
        ps.setInt(1,cabnumber);
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
