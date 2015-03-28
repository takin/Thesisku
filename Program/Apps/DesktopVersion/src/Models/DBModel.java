/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Helpers.Broadcaster;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author syamsul
 */
public class DBModel {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "kamuskata";
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";
    protected Connection SQL_CONNECTION;
    protected Broadcaster broadcast;
    
    public DBModel(){
        try{
            // inisialisasi koneksi ke database
            Class.forName(DB_DRIVER).newInstance();
            SQL_CONNECTION = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASS);
        }
        catch( IllegalAccessException | ClassNotFoundException | InstantiationException | SQLException e ){
            
        }
    }
}
