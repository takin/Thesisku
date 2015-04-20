package SemanticQA.models.nlp;

import SemanticQA.helpers.SQLConnector;
import SemanticQA.listeners.StemmingListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Kelas untuk melakukan proses stemming terhadap kata yang memiliki 
 * imbuhan yang tidak dikenali kelas katanya
 * 
 * Algortima yang digunakan di sini adalah algortima stemming Nazief Adriani
 *
 * @author syamsul
 */
public class Stemming extends SQLConnector {
    
    public static final String ORIGINAL_TOKEN = "originalKey";
    public static final String STEMMED_TOKEN = "stemmedKey";
    public static final String TOKEN_TYPE = "type";
    
    private static String wordToStem;
    private static final String SQL_QUERY = "SELECT * FROM tb_katadasar WHERE katadasar='";
    
    public Stemming(String word) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
        super();
        wordToStem = word;
    }
    
    public static Stemming stem(String word){
        Stemming stem = null;
        try {
            stem = new Stemming(word);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(Stemming.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return stem;
    }
    
    public static void then(StemmingListener listener){
        
        try {
            Map<String,String> result = process();
            
            if(result.containsKey("kelasKata")){
                listener.onStemmingMatch(result);
            } else {
                listener.onStemmingNotMatch(wordToStem);
            }
        } catch (SQLException ex) {
            listener.onStemmingFailed(ex.getMessage());
        }
        
        
    }
    
    private static Map<String,String> process() throws SQLException{
        
        /**
         * Stage pertama adalah dengan membuang imbuhan -lah,-kah,-ku,-mu,-nya da -pun
         * Kembalian dari proses ini adalah berupa array string dimana index ke 0
         * adalah kata hasil stemming dan index ke 1 adalah kode katadasarnya ['siapa','PRON']
         */
        Map<String,String> firstStage = infectionSuffix(wordToStem);
        
        /**
         * Jika hash map memiliki key TOKEN_TYPE, artinya proses stemming pada stage 
         * pertama berhasil, sehingga algoritma berhenti di sini
         */
        if(firstStage.containsKey(TOKEN_TYPE)){
            return firstStage;
        }
        
        /**
         * Jika pada stage pertama kode katadasar tidak ditemukan, maka lanjutkan
         * ke stage berikutnya yaitu pemotongan derivation suffix dan lakukan 
         * proses pengecekan dan penggabungan sesuai dengan langkah pertama
         */
        Map<String,String> secondStage = derivationSuffix(firstStage.get(STEMMED_TOKEN));
        
        if(secondStage.containsKey(TOKEN_TYPE)){
            return secondStage;
        }
        
        /**
         * Jika proses stemming hingga langkah ke dua masih belum berhasil,
         * maka lanjutkan dengan langkah ke tiga yaitu proses pemotongan 
         * Prefix dan derivation prefix
         * 
         * Jika pada langkah ini masih belum berhasil, maka kembalikan kata 
         * sesuai dengan aslinya (kelas kata tidak dikenali)
         */
        Map<String,String> thirdStage = derivationPrefix(secondStage.get(STEMMED_TOKEN));
        
        
        return thirdStage;
    }
    
    /**
     * 
     * @param word -> kata masukan yang akan diproses suffixnya
     * @return array String dengan panjang 2 yang terdiri dari ['kata','kode']
     */
    private static Map<String,String> infectionSuffix(String word) throws SQLException{
        
        /** --------------------------------------------------------------------------
         * Array kata dan kode hasil proses stemming pada stage pertama
         * 
         * Note:
         * secara defalt panjang array adalah 1
         * Panjang array ini akan digunakan untuk mengetahui apakah proses stemming
         * pada stage ini berhasil atau tidak, jika berhasil maka panjang array 
         * adalah 2 yang terdiri dari ['kata','kode'] sedangkan jika pada stage ini
         * kelas kata tidak ditemukan, maka panjang array adalah 1 ['kata']
         * ----------------------------------------------------------------------------
         */
        Map<String,String> firstStage = new HashMap<>();
        String stemmedToken = word;
        
        //Pattern dengan pola akhiran -kah, -lah, -ku, -mu, -pun dan -nya
        String infectionSuffixPattern = "([kl]ah|[km]u|pun|nya)$";
        
        // cek apakah kata match dengan pola di atas
        Matcher infectionMatcher = Pattern.compile("^[a-zA-Z]+" + infectionSuffixPattern).matcher(wordToStem);
        
        /**
         * Jika pola kata sesuai dengan pola yang telah ditentukan sebelumnya
         * maka lakukan proses pembuangan akhiran sesuai dengan pola yang ditemukan
         * kemudian cek kata yang telah di potong tersebut ke dalam database
         * untuk mencari kelas katanya
         */
        if(infectionMatcher.matches()){
            
            stemmedToken = word.replaceAll(infectionSuffixPattern, "");
            
            ResultSet res = STATEMENT.executeQuery(SQL_QUERY + stemmedToken + "'");
            
            if(res.isBeforeFirst()){
                /**
                 * oleh karena kelas kata hanya pasti memiliki satu result,
                 * maka arahkan pointer pada posisi row pertama sehingga
                 * tidak perlu dilakukan iterasi untuk mengambil resultset
                 * hasil query
                 */
                res.absolute(1);
                
                //masukkan kelas kata hasil query ke dalam array index ke -1
                firstStage.put(TOKEN_TYPE, res.getString("kode_katadasar"));
            }
            
            
        }
        
        firstStage.put(STEMMED_TOKEN, stemmedToken);
        firstStage.put(ORIGINAL_TOKEN, word);
        
        return firstStage;
    }
    
    private static Map<String,String> derivationSuffix(String word) throws SQLException{
        
        Map<String,String> secondStage = new HashMap<>();
        
        Matcher derivationSuffixesMatcher = Pattern.compile("^[a-zA-Z]+(i|k?an)$").matcher(word);
                
        if(derivationSuffixesMatcher.matches()){
            String newMatch = word.replaceAll("(i|k?an)$", "");
            ResultSet res = STATEMENT.executeQuery(SQL_QUERY + newMatch + "'");

            if(res.isBeforeFirst()){
                res.absolute(1);

                secondStage.put(TOKEN_TYPE, res.getString("kode_katadasar"));
                secondStage.put(STEMMED_TOKEN, newMatch);

            } else {
                
                
                
            }
        }
        
        secondStage.put(ORIGINAL_TOKEN, word);
        
        return secondStage;
    }
    
    private static Map<String,String> derivationPrefix(String word){
        Map<String,String> thirdStage = new HashMap<>();
        
        return thirdStage;
    }
}
