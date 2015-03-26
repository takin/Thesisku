/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Models;

import Helpers.Broadcaster;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * POSTagging merupakan bagian dari pre-process NLP
 fungsi kelas ini adalah untuk membentuk token-token dari kata yang diinputkan
 
 Token yang telah dibentuk selanjutnya akan di cek kelas katanya ke dalam 
 database SQL untuk selanjutnya digunakan untuk memberikan TAG (POS TAG)
 sehingga nantinya token tersebut dapat di proses lebih lanjut oleh parser
 
 Apabila sebuah token tidak diketahui kelas katanya, maka kata/token tersebut 
 akan diberikan tag UN (unknwon)
 * @author syamsul
 */
public class POSTagging {
    
    /**
     * Database setup (MySQL) property
     */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "kamuskata";
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";
    private static Connection SQL_CONNECTION;
    Broadcaster broadcast;
    
    private List<Map<String,String>> TAGGED_TOKEN = new ArrayList();
    
    public POSTagging(String question){
        try{
            // inisialisasi koneksi ke database
            Class.forName(DB_DRIVER).newInstance();
            SQL_CONNECTION = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASS);
            
            // jika koneksi dengan database berhasil
            // maka lanjutkan ke proses utama
            doTagging(question);
            
            broadcast = (Broadcaster) this;
        }
        catch( IllegalAccessException | ClassNotFoundException | InstantiationException | SQLException e ){
            broadcast.onFail();
        }
    }
    
    /**
     * 
     * @param word -> token yang akan dicek kelasnya ke dalam database
     * @return Arraylist Map<Kata, Kelas> 
     */
    private void doTagging(String sentence){
        
        try{
            /**
             * Bangun query statement.
             * di sini yang akan diambil dari database hanyalah kolom kode_katadasar saja
             * dimana kolom ini hanya berisi kode dari kata yang akan dicari 
             * yaitu (N, V, ADJ dll)
             */
            
            StringTokenizer token = new StringTokenizer(sentence);
            Statement stmt = SQL_CONNECTION.createStatement();
            
            while (token.hasMoreTokens()) {
                String word = token.nextToken();
                Map<String, String> item = new HashMap<>();
             
                ResultSet res = stmt.executeQuery("SELECT kode_katadasar FROM tb_katadasar WHERE katadasar='"+word+"' LIMIT 1");
            
                /**
                 * jika kata yang diinputkan ditmukan (kelas katanya diketahui) 
                 * maka masukkan kata tersebut ke dalam array list untuk dikebalikan
                 */
                if( res.isBeforeFirst() ){  

                    /**
                     * oleh karena hasil dari query sudah pasti hanya 1 baris
                     * maka posisi kursor langsung dipindah ke baris pertama 
                     * dan langsung ambil data tanpa harus menggunakan looping
                     */
                    res.absolute(1);
                    String kode = res.getString("kode_katadasar");
                    item.put(word, kode);
                }
                else {
                    /**
                     * Jika kata yang diinputkan tidak dikenali, maka tag kata tersebut
                     * dengan kode UN
                     */
                    item.put(word, "UN");
                }
                TAGGED_TOKEN.add(item);
            }
        } catch( SQLException e ){
            
        }
    }
    
    public List<Map<String, String>> getTaggegSentence(){
        return TAGGED_TOKEN;
    }
}
