/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * POSTagger merupakan bagian dari pre-process NLP
 fungsi kelas ini adalah untuk membentuk token-token dari kata yang diinputkan
 
 Token yang telah dibentuk selanjutnya akan di cek kelas katanya ke dalam 
 database SQL untuk selanjutnya digunakan untuk memberikan TAG (POS TAG)
 sehingga nantinya token tersebut dapat di proses lebih lanjut oleh parser
 
 Apabila sebuah token tidak diketahui kelas katanya, maka kata/token tersebut 
 akan diberikan tag UN (unknwon)
 * @author syamsul
 */
public class POSTagger extends DBModel {
    
    /**
     * Arraylist yang berisi hashmap masing-masing token yang sudah diberi tag
     * sesuai dengan jenis katanya yang terdapat dalam database
     */
    private final List<Map<String,String>> TAGGED_TOKEN = new ArrayList<>();
    
    private final List<String> TOKEN = new ArrayList<>();
    
    public POSTagger(String question){
        super();
        doTagging(question);
    }
    
    /**
     * 
     * @param word -> token yang akan dicek kelasnya ke dalam database
     * @return Arraylist Map<Kata, Kelas> 
     */
    private void doTagging(String sentence){
        
        try{
            /**
             * lakukan tokenisasi terhadap kalimat yang diinputkan
             * token ini nantinya akan digunakan sebagai clausa dalam SQL 
             * untuk mencari tipe kata dari masing-masing token
             */
            
            String[] token = sentence.split(" ");
            
            /**
             * buat query sql untuk mencari tipe kata di dalam database
             * clause IN dipilih dengan alasan agar proes query ke dalam database 
             * tidak dilakukan berulang-ulang, sehingga dapat meningkatkan 
             * performa, terutama pada query dengan jumlah token yang banyak
             */
            String SQL_QUERY = "SELECT katadasar,kode_katadasar FROM tb_katadasar WHERE katadasar in (";
            
            /**
             * lakukan iterasi untuk memasukkan masing-masing kata yang akan
             * dijadikan sebagai kriteria di dalam kalusa IN
             */
            for(int i=0; i < token.length; i++){
                
                /**
                 * Siapkan kata yang akan dicek
                 * kata dalam database menggunakan lowercase, untuk itu pastikan 
                 * kata yang akan dimasukkan juga dalam format lowe case
                 * serta jangan lupa pula untuk melakukan trimming untuk membuang
                 * tanda baca lain seperti koma dan titik yang menjadi satu 
                 * dengan kata
                 */
                String word = token[i].toLowerCase().trim();
                
                TOKEN.add(word);
               
                /**
                 * Lakukan concatinate terhadap SQL_QUERY sehingga membentuk
                 * array string yang akan di cek di dalam database
                 * 
                 * Oleh karen kriteria yang akan dicek berupa string, 
                 * maka jangan lupa untuk menambahkan tanda petik (')
                 * pada setiap iterasi
                 * 
                 * Cek juga apakah posisi pointer sudah berada di akhir array 
                 * atau belum, jika belum maka tambahkan tanda koma pada setiap 
                 * akhir kata yang akan digabungkan
                 */
                SQL_QUERY += (i == (token.length - 1)) ? "'" + word + "'" : "'" + word + "',";
            }
            
            /**
             * setelah semua string kriteria dimasukkan, tambahkan tanda 
             * kurung tutup pada akhir query sehingga membentuk statement 
             * SQL yang utuk: Select katadasar, kode_katadasar from 
             * tb_katadasar where katadasar in ('a','b',c')
             */
            SQL_QUERY += ")";
            
            // buat statement SQL
            Statement stmt = SQL_CONNECTION.createStatement();
            
            // lakukan query ke database
            ResultSet queryResult = stmt.executeQuery(SQL_QUERY);
            
           
           // cek apakah query menghasilkan result atau tidak
           if(queryResult.isBeforeFirst()){
               
               // pastikan row mulai dari awal
               queryResult.first();
               
               /**
                * Buat objek HashMap yang nantinya berfungsi untuk menyimpan 
                * informasi kata dengan tipenya dimana kata menjadi key sedangkan 
                * kode kata tersebut menjadi value dari hashmap tersebut
               */
               Map<String, String> items = new HashMap<>();
               
               
               while(queryResult.next()){
                   String kata = queryResult.getString("katadasar");
                   String kode = queryResult.getString("kode_katadasar");
                   
                   items.put(kata, kode);
               }
               
               
               /**
                * Lakukan itersi untuk melakukan pengecekan terhadap TOKEN asli
                * (sebelum dilakukan tagging) apakah token tersebut memiliki
                * sudah dikenali kelas katanya atau tidak
                */
               for(String t: TOKEN){
                   
                   Map<String,String> newMap = new HashMap<>();
                   
                   if(items.containsKey(t)){
                       // jika token terdapat di dalam database, maka buat hash
                       // dengan token sebagai key dan kode sebagai value
                       newMap.put(t, items.get(t));
                   } else {
                       // jika token tidak dikenali maka masukkan kata sebagai 
                       // key dan UN sebagai value
                       newMap.put(t, "UN");
                   }
                   
                   // masukkan hashmap yang sudah dibuat sebelumnya ke dalam 
                   // arraylist
                   TAGGED_TOKEN.add(newMap);
               }
               
           } else {
               /**
                * Jika hasil query dari database kosong, artinya semua token
                * tidak memiliki kelas kata di dalam database maka lakukan
                * tagging manual dengan memberi tag "UN"
                */
               for(String word : token){
                   Map<String,String> map = new HashMap<>();
                   
                   // masukkan token ke dalam map dan beri tag UN
                   map.put(word, "UN");
                   
                   TAGGED_TOKEN.add(map);
               }
           }
            
        } catch( SQLException e ){
            System.out.println(e.getMessage());
        }
    }
    
    public List<Map<String,String>> getTaggegSentence(){
        return TAGGED_TOKEN;
    }
}
