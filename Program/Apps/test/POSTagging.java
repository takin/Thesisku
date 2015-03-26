/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.regex.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author syamsul
 */
public class POSTagging {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "lexicon";
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";
    private static Connection SQLConnection;
    
    private static String PATTERN;
    
    public POSTagging(){
        try{
            Class.forName(DB_DRIVER).newInstance();
            SQLConnection = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASS);
        }
        catch( IllegalAccessException e ){
            
        }
        catch( ClassNotFoundException e ){
            
        }
        catch( InstantiationException e ){
            
        }
        catch( SQLException e ){
            
        }
    }
    
    protected List<Map<String,String>> queryLexicon(final String word){
        
        List<Map<String,String>> returned = new ArrayList();
        returned.clear();
        
        try{
            Statement stmt = SQLConnection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM tb_katadasar WHERE katadasar='"+word+"' LIMIT 1");

            // jika lexicon terdapat dalam database
            if( res.isBeforeFirst() ){
                
                while( res.next() ){ 
                    final String kata = res.getString("katadasar");
                    final String kode = res.getString("kode_katadasar");
                    returned.add(new HashMap<String, String>(){{put(kata,kode);}});
                }
            } else { // jika lexicon tidak ditemukan
                returned.add(new HashMap<String, String>(){{put(word,null);}});
            }
        } catch( SQLException e ){
            returned.add(new HashMap<String, String>(){{put(null,null);}});
        }
        return  returned;
    }
    
    protected String affixProcess(int stage, String word){
        
        switch(stage){
            case 2 :
                    break;
            case 3 :
                    break;
            default :
                PATTERN = "^ber";
                    break;
        }
        
        Pattern REGEX = Pattern.compile(PATTERN);
        Matcher matcher = REGEX.matcher(word);
        String replacer = "";
        if( matcher.find() ){
            StringBuffer sb = new StringBuffer();
            matcher.appendReplacement(sb, replacer).appendTail(sb);
            return sb.toString();
        }
        else {
            return null;
        }
        
    }
}
