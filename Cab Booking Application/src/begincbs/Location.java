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
import java.time.LocalTime;
import java.util.Scanner;

/**
 *
 * @author Administrator
 */
public class Location {
    
    String url = "jdbc:mysql://localhost:3307/AshwinAsh";
    String username = "root";
    String password = "root";
    PreparedStatement ps;
    ResultSet rs;
    Connection con;
    Scanner scan;
    String select_query1="select * from cabdb where CabNumber=?";
    String select_query2="select * from locationdb where CabNumber=? and OrderIndex=?";
    String select_query3="select * from locationdb where CabNumber=? and Location=?";
    
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
    
    public int addlocation(int cabnumber,String location,int order,String cabtimeindex)
    {
        int r=0;
        String query = "insert into locationdb values(?,?,?,?)";
        String query1 = "select * from locationdb where CabNumber=? and OrderIndex=? and CabTimeIndex=?";
        String query2 = "select * from locationdb where CabNumber=? and Location=? and CabTimeIndex=?";
        try
        {
        dbconnection();
        
        ps = con.prepareStatement(query1);
        ps.setInt(1,cabnumber);
        ps.setInt(2,order);
        ps.setString(3,cabtimeindex);
        ResultSet rs1 = ps.executeQuery();
        
        ps = con.prepareStatement(query2);
        ps.setInt(1,cabnumber);
        ps.setString(2,location);
        ps.setString(3,cabtimeindex);
        ResultSet rs2 = ps.executeQuery();
        
        if(!rs1.next() && !rs2.next())
        {
        ps = con.prepareStatement(query);
        ps.setInt(1,cabnumber);
        ps.setInt(2,order);
        ps.setString(3,location);
        ps.setString(4,cabtimeindex);
        r = ps.executeUpdate();
        }
        else
        {
            rs1.previous();
            rs2.previous();
            if(rs1.next())
                r=2;
            else if(rs2.next())
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
    
    public int delete(int cabnumber,String location,String cabtimeindex)
    {
        int r=0;
        String query = "delete from locationdb where CabNumber=? AND Location=? AND CabTimeIndex=?";
        try
        {
        dbconnection();
        ps = con.prepareStatement(query);
        ps.setInt(1,cabnumber);
        ps.setString(2,location);
        ps.setString(3,cabtimeindex);
        r = ps.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return r;
    }
    
    
    public int modify(String locationnew,String location,String cabtimeindex)
    {
        int r=0;
        String query = "update locationdb set Location=? where Location=? and CabTimeIndex=?";
        try
        {
        dbconnection();
        ps = con.prepareStatement(query);
        ps.setString(1,locationnew);
        ps.setString(2,location);
        ps.setString(3,cabtimeindex);
        r = ps.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return r;
    }
    
    
    void locationview()
    {
        String query = "select l.CabNumber,l.OrderIndex,l.Location,t.Time from locationdb l,timeslotdb t where  l.CabTimeIndex = t.CabTimeIndex order by l.Location,t.Time";
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
                    System.out.println("CabNumber:"+rs.getInt(1)+"  "+"Destination:"+rs.getString(3)+"  "+"OrderIndex:"+rs.getInt(2)+"  "+"Time:"+rs.getTime(4));
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
    
    
    
    void locationview(int cabnumber)
    {
        String query = "select l.CabNumber,l.OrderIndex,l.Location,t.Time from locationdb l,timeslotdb t where l.CabNumber = ? and l.CabTimeIndex = t.CabTimeIndex order by t.Time";
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
                    System.out.println("CabNumber:"+rs.getInt(1)+"  "+"Destination:"+rs.getString(3)+"  "+"OrderIndex:"+rs.getInt(2)+" "+"Time:"+rs.getTime(4));
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
    
    
    int changelocationdbcabnumber(int oldcabnumber,int cabnumber,String cabtimeindex)
    {
        int r=0;
        
        String query = "update locationdb set CabNumber=? where CabNumber=? and CabTimeIndex=?";
        try
        {
            dbconnection();
            ps = con.prepareStatement(query);
            ps.setInt(1,cabnumber);
            ps.setInt(2,oldcabnumber);
            ps.setString(3,cabtimeindex);
            r = ps.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return r;
    }
    
    int checklocation(int cabnumber,String cabtimeindex)
    {
        int r=0;
        String query = "select * from timeslotdb where CabNumber=? and CabTimeIndex=?";
        
        try
        {
            dbconnection();
            ps = con.prepareStatement(query);
            ps.setInt(1,cabnumber);
            ps.setString(2,cabtimeindex);
            ResultSet rs1 = ps.executeQuery();
            
            if(rs1.next())
                r=1;
            else
                r=0;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return r;
    }
}
