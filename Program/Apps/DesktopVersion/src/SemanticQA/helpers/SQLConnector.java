/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SemanticQA.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author syamsul
 */
public class SQLConnector {
    
    protected static Connection CONNECTION;
    protected static Statement STATEMENT;
    
    public SQLConnector() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        Class.forName(Constant.DB_DRIVER).newInstance();
        CONNECTION = DriverManager.getConnection(Constant.DB_URL + Constant.DB_NAME, Constant.DB_USER, Constant.DB_PASS);
        STATEMENT = CONNECTION.createStatement();
    }
    
}
