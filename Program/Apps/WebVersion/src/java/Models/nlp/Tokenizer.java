/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Models.nlp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author syamsul
 */
public class Tokenizer {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "kamuskata";
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";
    private static Connection SQL_CONNECTION;
    private static final Map<String, String> QUESTION_TYPE_LIST = new HashMap<String, String>(){{
                                                                                put("Apa","Benda"); 
                                                                                put("Apakah","Benda"); 
                                                                                put("Siapakah","Orang"); 
                                                                                put("Siapa","Orang"); 
                                                                                put("Dimana","Lokasi"); 
                                                                                put("Dimanakah","Lokasi");
                                                                                put("Berapa","Jumlah");
                                                                                put("Berapakah","Jumlah");
                                                                                put("Kapan","Waktu");
                                                                                put("Kapankah","Waktu");}};
    private static final List<String> STEMMING_PATTERN = new ArrayList<String>(Arrays.asList("^ber","^ter","^me","^di","^ber","^pe","^se","^memper","an$","kan$","i$","pun$","lah$","kah$","nya$","ku$","mu$"));
    private static StringTokenizer TOKEN; // raw token dari hasil tokenisasi java StringTokenizer
    private static final List<String> TAGGED_TOKEN = new ArrayList(); //map postagging <katadasar,kode_katadasar>
    private static String QUESTION_TYPE = null; //string yang diambil dari token pertama. token ini berfungsi untuk melakukan validasi terhadap format pertanyaan
    
    public Tokenizer(String question){
        try{
            Class.forName(DB_DRIVER).newInstance();
            SQL_CONNECTION = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASS);
            TOKEN = new StringTokenizer(question); // lakukan tokenisasi string
            String q  = TOKEN.nextToken(); // ambil token pertama untuk dicek apakah pertanyaan valid
            QUESTION_TYPE = q.substring(0,1).toUpperCase() + q.substring(1); // potong token pertama
            this.processToken(); // proses token
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
    
    private List<Map<String,String>> getLexicon(final String word){
        
        List<Map<String,String>> returned = new ArrayList();
        returned.clear();
        
        try{
            Statement stmt = SQL_CONNECTION.createStatement();
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
    
    private Matcher regexProcess(String word, String pattern){
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(word);
        return m;
    }
    
    private String stemming(String word){
        
        String returnedWord = null;
        for( String strpattern: STEMMING_PATTERN ){
            Matcher suffixToken = this.regexProcess(word, strpattern);
            if( suffixToken.find() ){
                returnedWord = suffixToken.replaceAll("");
                break;
            }
        }
        return returnedWord; 
       
    }
    
    private void processToken(){
        TAGGED_TOKEN.clear();// lakukan pegosongan terhadap hasil pencarian sebelumnya
        while( TOKEN.hasMoreTokens() ){
            String tokenToCheck = TOKEN.nextToken();
            // generate regular expression dengan pola non word charackter \\W untuk melakukan validasi kata pad token
            // jika token bukan word (token adalah meta character) maka buang token tersebut
            Matcher symbolicToken = this.regexProcess(tokenToCheck, "\\W");
            if( !symbolicToken.matches() ){
                // jika token bukan merupakan satu atau sekumpulan metacharacter maka
                // cek apakah token yang bersangkutan mengandung metacharacter
                // jika mengandung metacharacter, maka buang meta character tersebut
                Matcher containMeta = this.regexProcess(tokenToCheck, "\\W*");
                if( containMeta.find() ){
                    tokenToCheck = containMeta.replaceAll("");
                }
                this.processLexicon(tokenToCheck);// cek token ke dalam database lexicon
            }
        }
    }
    
    private void processLexicon(final String tokenToCheck){
        Map lexicon = (Map) this.getLexicon(tokenToCheck).get(0);
        if( lexicon.containsKey(tokenToCheck) && lexicon.get(tokenToCheck) != null ){ // jika token ada dalam database lexicon
            TAGGED_TOKEN.add(tokenToCheck + " " + lexicon.get(tokenToCheck));
        }
        /**
         * jika token tidak dietmukan dalam database lexicon
         * lakukan proses stemming dengan urutan
         * stage1: pengecekan dan pembuangan prefix
         * stage2: pengecekan dan pembuangan suffix
         * stage3: pengecekan dan pembuangan konfix
         * settelah melewati masing-masing stage, lakukan pengecekan ulang ke dalam database.
         * apabila semua stage sudah dilewati dan token tetap tidak ditemukan dalam database.
         * maka lakukan manual tagging dengan menandai token sebagai UN
         */
        if( lexicon.containsKey(tokenToCheck) && lexicon.get(tokenToCheck) == null ){
            String stemmedWord = this.stemming(tokenToCheck);
            if( stemmedWord != null ){
                this.processLexicon(stemmedWord);
            }
        }
    }
    
    public List<String> getToken(){
        return TAGGED_TOKEN;
    }
    
    public String getQuestion(){
        if( QUESTION_TYPE_LIST.containsKey(QUESTION_TYPE) ){
            return QUESTION_TYPE_LIST.get(QUESTION_TYPE);
        }
        else {
            return "UNKNOWN";
        }
    }
    
}
