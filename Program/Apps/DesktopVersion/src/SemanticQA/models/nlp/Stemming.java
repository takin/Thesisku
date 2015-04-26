package SemanticQA.models.nlp;

import SemanticQA.helpers.SQLConnector;
import SemanticQA.listeners.StemmingListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    
    // pointer key untuk menunjukkan kata asli sebelum dilakukan proses stemming
    public static final String ORIGINAL_WORD = "original_word";
    /** ------------------------------------------------------------------------
     * Key pointer untuk menjunjukkan kata hasil stemming
     * kata hasil stemming berupa arraylist yang menunjukkan sequence perubahan 
     * kata hasil stemming pada setiap tingkatan
     * ------------------------------------------------------------------------*/
    public static final String STEMMED_WORD = "stemmed_word";
    
    // pointer key untuk menunjukkan tipe kata yang ditemukan di dalam database
    public static final String WORD_TYPE = "word_type";
    
    // string asli yang akan dilakukan proses stemming
    private static String wordToStem;
    
    private static List<String> STEMMING_SEQUENCE;
    
    private static Map<String,Object> RESULT;
    
    // template query database untuk melakukan pengecekan tipe kata
    private static final String SQL_QUERY = "SELECT * FROM tb_katadasar WHERE katadasar='";
    
    public Stemming(String word) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
        super();
        
        STEMMING_SEQUENCE = new ArrayList<>();
        RESULT = new HashMap<>();
        
        wordToStem = word;
    }
    
    /** ------------------------------------------------------------------------
     * Method entry point untuk melakukan proses stemming dengan menggunakan 
     * pub/sub pattern Method ini harus mengembalikan objek dirinya sendiri 
     * (Stemming) agar bisa dilakukan method chaining di kelas observer
     * 
     * @param word -> kata yang akan di steming
     * @return Objek kelas Stemming
     * ------------------------------------------------------------------------*/
    public static Stemming stem(String word){
        Stemming stem = null;
        try {
            stem = new Stemming(word);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(Stemming.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return stem;
    }
    
    /** ----------------------------------------------------------------
     * Method untuk mendengarkan hasil stemming di dalam kelas observer
     * @param listener -> objek interface Stemming listener
     * ----------------------------------------------------------------*/
    public static void then(StemmingListener listener){
        
        try {
            if(process()){
                listener.onStemmingMatch(RESULT);
            } else {
                listener.onStemmingNotMatch(wordToStem);
            }
        } catch (SQLException ex) {
            listener.onStemmingFailed(ex.getMessage());
        }
    }
    
    private static boolean process() throws SQLException{
        
        RESULT.put(ORIGINAL_WORD, wordToStem);
        
        /** ----------------------------------------------------------------------------------
         * Stage pertama adalah dengan membuang imbuhan -lah,-kah,-ku,-mu,-nya da -pun
         * Kembalian dari proses ini adalah berupa array string dimana index ke 0
         * adalah kata hasil stemming dan index ke 1 adalah kode katadasarnya ['siapa','PRON']
         * 
         * Jika infectionSuffix() true, artinya hash map memiliki key TOKEN_TYPE
         * maka proses berhenti sampai di sini
         * --------------------------------------------------------------------------*/
        if(infectionSuffix()){
            RESULT.put(STEMMED_WORD, STEMMING_SEQUENCE);
            return true;
        }
        
        /** ------------------------------------------------------------------------
         * Jika pada stage pertama kode katadasar tidak ditemukan, maka lanjutkan
         * ke stage berikutnya yaitu pemotongan derivation suffix dan lakukan 
         * proses pengecekan dan penggabungan sesuai dengan langkah pertama
         *-------------------------------------------------------------------------*/
        if(derivationSuffix()){ 
            RESULT.put(STEMMED_WORD, STEMMING_SEQUENCE);
            return true;
        }
        
        /** ------------------------------------------------------------------
         * Jika proses stemming hingga langkah ke dua masih belum berhasil,
         * maka lanjutkan dengan langkah ke tiga yaitu proses pemotongan 
         * Prefix dan derivation prefix
         * 
         * Jika pada langkah ini masih belum berhasil, maka kembalikan kata 
         * sesuai dengan aslinya (kelas kata tidak dikenali)
         * ------------------------------------------------------------------*/    
        return derivationPrefix();
    }
    
    /** ------------------------------------------------------------------------
     * @param word -> kata masukan yang akan diproses suffixnya
     * @return array String dengan panjang 2 yang terdiri dari ['kata','kode']
     * ------------------------------------------------------------------------*/
    private static boolean infectionSuffix() throws SQLException{
        
        //Pattern dengan pola akhiran -kah, -lah, -ku, -mu, -pun dan -nya
        String infectionSuffixPattern = "([kl]ah|[km]u|pun|nya)$";
        
        // cek apakah kata match dengan pola di atas
        Matcher infectionMatcher = Pattern.compile("^[a-zA-Z]+" + infectionSuffixPattern).matcher(wordToStem);
        
        /** ------------------------------------------------------------------------------------
         * selalu lakukan pengisian stemming sequence pertama dengan kata yang belum di proses
         * item ini diperlukan oleh stage ke dua yaitu derivationSuffix() sebagai kata yang 
         * akan di proses
         * 
         * jika kata asli (wordToStem) match dengan pola yang sudah ditentukan di atas
         * maka isi dari stemming sequence pertama diganti dengan kata hasil stemming
         * ------------------------------------------------------------------------------------*/
        STEMMING_SEQUENCE.add(wordToStem);
        
        /** -------------------------------------------------------------------------
         * Jika pola kata sesuai dengan pola yang telah ditentukan sebelumnya
         * maka lakukan proses pembuangan akhiran sesuai dengan pola yang ditemukan
         * kemudian cek kata yang telah di potong tersebut ke dalam database
         * untuk mencari kelas katanya
         * --------------------------------------------------------------------------*/
        if(infectionMatcher.matches()){
            
            String stemmedWord = wordToStem.replaceAll(infectionSuffixPattern, "");
            STEMMING_SEQUENCE.set(0,stemmedWord);
                   
            ResultSet res = STATEMENT.executeQuery(SQL_QUERY + stemmedWord + "'");
            
            // jika kelas kata ditemukan
            if(res.isBeforeFirst()){
                /** --------------------------------------------------------
                 * oleh karena kelas kata hanya pasti memiliki satu result,
                 * maka arahkan pointer pada posisi row pertama sehingga
                 * tidak perlu dilakukan iterasi untuk mengambil resultset
                 * hasil query
                 ---------------------------------------------------------- */
                res.absolute(1);
                
                //masukkan kelas kata hasil query ke dalam array index ke -1
                RESULT.put(WORD_TYPE, res.getString("kode_katadasar"));
                
                return true;
            }
            return false;
        }
        
        return false;
    }
    
    private static boolean derivationSuffix() throws SQLException{
        
        String word = STEMMING_SEQUENCE.get(0);
        
        /** ----------------------------------------------------------------------------
         * Sama halnya dengan langkah infectionSuffix(), isikan nilai stemming sequence
         * ke 2 dengan data asli yang akan di stem (isi sequence pertama)
         * jika kata berhasil di stem, maka ganti isinya dengan kata yang telah di stem
         * -----------------------------------------------------------------------------*/
        STEMMING_SEQUENCE.add(word);
        
        String derivationSuffixPattern = "(i|an)$";
        
        Matcher derivationSuffixesMatcher = Pattern.compile("^[a-zA-Z]+"+ derivationSuffixPattern).matcher(word);
                
        if(derivationSuffixesMatcher.matches()){
            
            /** --------------------------------------------------------------------------------
             * simpan akhiran yang sudah di buang, karena ini akan digunakan untuk melakukan
             * pengcekean apabila akhirannya adalah -an dan setelah di cek ternyata 
             * kata tidak dikenali maka dicek kembali apakah huruf terakhir adalah -k 
             * maka buang huruf tersebut dan lakukan pengecekan kembali ke dalam database
             * --------------------------------------------------------------------------------*/
            int idxOfsuffix = derivationSuffixesMatcher.groupCount();
            String removedTail = derivationSuffixesMatcher.group(idxOfsuffix);
            
            // buang akhiran -i atau -an
            String newMatch = word.replaceAll(derivationSuffixPattern,"");
            
            // lakukan pengecekan ke dalam database
            ResultSet res = STATEMENT.executeQuery(SQL_QUERY + newMatch + "'");

            if(res.isBeforeFirst()){
                res.absolute(1);

                RESULT.put(WORD_TYPE, res.getString("kode_katadasar"));
                STEMMING_SEQUENCE.set(1,newMatch);
                
                return true;

            } 
            
            // ambil karakter terakhir dari kata yang sudah di stem
            char lastCharacter = newMatch.charAt(newMatch.length() - 1);
            /** ----------------------------------------------------------------
             * jika akhiran yang dibuang adalah -an dan akhiran setelah dibuang 
             * adalah -k maka buang -k dan cek kembali ke dalam database
             * ----------------------------------------------------------------*/
            if(removedTail.equals("an") && lastCharacter == 'k'){
                
                // buang akhiran k
                newMatch = newMatch.replaceAll("k$", "");
                
                //Lakukan pencarian ke dalam database
                res = STATEMENT.executeQuery(SQL_QUERY + newMatch + "'");

                if(res.isBeforeFirst()){
                    res.absolute(1);
                    RESULT.put(WORD_TYPE, res.getString("kode_katadasar"));
                    STEMMING_SEQUENCE.set(1,newMatch);
                    
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }
    
    private static boolean derivationPrefix(){
        
        /** --------------------------------------------------------------------
         * Pada stage ini, langkah yang harus ditempuh adalah:
         * - Cek apakah kata asli (wordToStem) sudah mengalami pemotongan suffix
         *   pada stage sebelumnya (derivationSuffix)
         * - Jika ada, maka lakukan proses pengecekan apakah kombinasi awalan 
         *   dan akhiran diizinkan atau tidak
         * - Jika tidak ada, maka lakukan proses pemotongan prefix sesuai dengan
         *   kombinasi yang telah ditentukan
         * --------------------------------------------------------------------*/
        
        String word = STEMMING_SEQUENCE.get(1);
        
        /** --------------------------------------------------------------------
         * Note: proses pembandingan wordToStem dengan word harus dilakukan 
         * terlebih dahulu karena ini menandakan apakah word sudah mengalami
         * pemotongan suffix atau belum
         * jika bernilai false, artinya word telah mengalami pemotongan, maka 
         * proses selanjtnya adalah pengecekan kombinasi awalan akhiran yang 
         * diizinkan
         * --------------------------------------------------------------------*/
        if(word.equals(wordToStem) || isProcessableWord(word)){
            
            /**
             * Pada stage ini proses dilakukan maksimal hanya tiga kali yaitu 
             * penghapusan prefix 1 + prefix 2 + prefix 3
             * 
             * masing-masing dengan memperhatikan urutan kemunculan kata prefix
             */
            
            // cek apakah prefix merupakah [refix yang diizinkan atau tidak 
            if(isValidPrefix(word)){
                
            }
            return false;
        }
        
        return false;
    }
    
    private static boolean isValidPrefix(String word){
        
        if(word.length() < 2) return false;
        
        String[] tempArray = {"di","ke","se","be","me","te","pe"};
        List<String> validPrefix = new ArrayList<>(Arrays.asList(tempArray));
        
        String prefix = word.substring(0, 1);
        
        return (validPrefix.contains(prefix));
    }
    
    private static boolean isProcessableWord(String word){
        
        String[] pattern = {
            "^([bks]e)[a-zA-Z]+(i)$",
            "^(di|me)[a-zA-Z]+(an)$",
            "^(ke|se)[a-zA-Z]+(kan)$"
        };
        
        
        for(String p:pattern){
            Matcher m = Pattern.compile(p).matcher(word);
            if(m.matches()){
                return false;
            }
        }
        
        return true;
    }
}
