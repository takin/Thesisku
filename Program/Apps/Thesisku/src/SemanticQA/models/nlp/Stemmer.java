package SemanticQA.models.nlp;

import SemanticQA.helpers.SQLConnector;
import SemanticQA.interfaces.StemmingListener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class Stemmer extends SQLConnector {
	
	public static final boolean DIRECT_STEMMING = true;
	
    /* ------------------------------------------------------------------------
     * Key pointer untuk menjunjukkan kata hasil stemming
     * kata hasil stemming berupa arraylist yang menunjukkan sequence perubahan 
     * kata hasil stemming pada setiap tingkatan
     * ------------------------------------------------------------------------*/
    public static final String TOKEN_WORD = "word";
    
    // pointer key untuk menunjukkan tipe kata yang ditemukan di dalam database
    public static final String TOKEN_TYPE = "type";
    
    // string asli yang akan dilakukan proses stemming
    private static String wordToStem;
    
//    private static List<String> STEMMING_SEQUENCE;
    
    private static Map<String,String> RESULT;
    
    // template query database untuk melakukan pengecekan tipe kata
    private static final String SQL_QUERY = "SELECT * FROM tb_katadasar WHERE katadasar='";
    
    private static StemmingListener broadcast;
    
    private static boolean isDirectStemming = false;
    
    
    public Stemmer(String word) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
        super();
        
//        STEMMING_SEQUENCE = new ArrayList<>();
        RESULT = new HashMap<>();
        
        wordToStem = word;
    }
    
    /* ------------------------------------------------------------------------
     * Method entry point untuk melakukan proses stemming dengan menggunakan 
     * pub/sub pattern Method ini harus mengembalikan objek dirinya sendiri 
     * (Stemming) agar bisa dilakukan method chaining di kelas observer
     * 
     * @param word -> kata yang akan di steming
     * @return Objek kelas Stemming
     * ------------------------------------------------------------------------*/
    public static Stemmer stem(String word){
        Stemmer stem = null;
        
        try {
			stem = new Stemmer(word);
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | SQLException e) {
			e.getMessage();
		}
        
        return stem;
    }
    
    public static Stemmer stem(String word, boolean isDirectStemming){
    	Stemmer stem = null;
        Stemmer.isDirectStemming = isDirectStemming;
        try {
			stem = new Stemmer(word);
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | SQLException e) {
			e.getMessage();
		}
        
        return stem;
    }
    
    /* ----------------------------------------------------------------
     * Method untuk mendengarkan hasil stemming di dalam kelas observer
     * @param listener -> objek interface Stemming listener
     * ----------------------------------------------------------------*/
    public static void then(StemmingListener listener){
        
    	Stemmer.broadcast = listener;
    	start(wordToStem);
    }
    
    private static boolean start(String word){
    	
    	if(!isDirectStemming){
    		if(typeIsFound(word)){
    			Stemmer.broadcast.onStemmingMatch(RESULT);
    			return true;
    		}
    	}
		
    	particleRemoval(word);
    	return false;
    }
    
    private static boolean particleRemoval(String word){
    	
    	if(word.length() < 3){
    		Stemmer.broadcast.onStemmingFailed("Karakter terlallu sedikit");
    		return false;
    	}
    	
    	String particlePattern = "^[a-z-A-Z]+([lkt]ah|pun)$";
    	
    	Stemmer.broadcast.onStemmingProgress("Memeriksa partikel suffix");
    	if(word.matches(particlePattern)){
    		Stemmer.broadcast.onStemmingProgress("Partikel ditemukan!!");
    		
    		word = word.replaceAll("([lkt]ah|pun)$", "");
    		
			if(typeIsFound(word)){
				Stemmer.broadcast.onStemmingProgress("Tipe kata ditemukan setelah proses partikel removal");
				Stemmer.broadcast.onStemmingMatch(RESULT);
				return true;
			}
    		
    		Stemmer.broadcast.onStemmingProgress("Tipe kata tidak ditemukan setelah proses particle removal");
    		possesivePronounceRemoval(word);
    		return false;
    	}
    	
    	Stemmer.broadcast.onStemmingProgress("Kata tidak mengandung suffix");
    	possesivePronounceRemoval(word);
    	return false;
    }
    
    private static boolean possesivePronounceRemoval(String word){
    	
    	if(word.length() < 3){
    		Stemmer.broadcast.onStemmingFailed("karakter terlalu sedikit");
    		return false;
    	}
    	
    	String possesivePronouncePattern = "^[a-zA-Z]+([km]u|nya)$";
    	
    	Stemmer.broadcast.onStemmingProgress("Memeriksa possesive pronounce suffix...");
    	if(word.matches(possesivePronouncePattern)){
    		
    		Stemmer.broadcast.onStemmingProgress("Possesive pronounce ditemukan!");
    		
    		word = word.replaceAll("([km]u|nya)$", "");
    		
			if(typeIsFound(word)){
				Stemmer.broadcast.onStemmingProgress("Tipe kata ditemukan setelah proses penghapusan possesive pronounce");
				Stemmer.broadcast.onStemmingMatch(RESULT);
				return true;
			}
    		
    		Stemmer.broadcast.onStemmingProgress("Tipe kata tidak ditemukan seteleah proses penghapusan possesive pronounce");
    		derivationSuffixRemoval(word);
    		
    		return false;
    	}
    	
    	Stemmer.broadcast.onStemmingProgress("Kata tidak mengandung possesive pronounce!");
    	derivationSuffixRemoval(word);
    	return false;
    }
    
    private static boolean derivationSuffixRemoval(String word){
    	
    	if(word.length() < 3){
    		Stemmer.broadcast.onStemmingFailed("kata terlalu pendek");
    		return false;
    	}
    	
    	String derivationSuffixPattern = "^[a-zA-Z]+([k]an|i)$";
    	
    	Stemmer.broadcast.onStemmingProgress("Memeriksa derivation suffix");
    	if(word.matches(derivationSuffixPattern)){
    		Stemmer.broadcast.onStemmingProgress("Derivation suffix ditemukan!");
    		
    		word = word.replaceAll("([k]an|i)$", "");
    		
			if(typeIsFound(word)){
				Stemmer.broadcast.onStemmingProgress("Tipe kata ditemukan setelah proses penghapusan derivation suffix!");
				Stemmer.broadcast.onStemmingMatch(RESULT);
				return true;
			}
    		
			
    		Stemmer.broadcast.onStemmingProgress("Tipe kata tidak ditemukan setelah proses penghapusan derivation suffix");
    	} else {
    		Stemmer.broadcast.onStemmingProgress("Kata tidak mengandung derivation suffix");	
    	}
    	derivationPrefixRemoval(word, "", 1);
    	return false;
    }
    
    private static boolean derivationPrefixRemoval(String word, String previousPrefix, int iteration){
    	
    	if(isInvalidPrefix(word)){
    		Stemmer.broadcast.onStemmingFailed("Kata " + word + " tidak mengandung awalan yang valid");
    		return false;
    	}
    	
    	// jika kata telah mengalamai proses pemotongan di tahapan derivation removal (ada suffix yang dibuang)
    	// maka cek apakah kata yang baru mengandung kombinasi awalan dan akhiran yang diizinkan atau tidak
    	// Pengecekan dilakukan dengan membandingkan kata yang dikirimkan dengan variabel wordToStem
    	if(!word.equals(wordToStem) && isProhibitedCombination(word)){
    		Stemmer.broadcast.onStemmingFailed("Kombinasi awalan dan akhiran tidak diizinkan");
    		return false;
    	}
    	
    	if(iteration > 3){
    		Stemmer.broadcast.onStemmingFailed("Proses penghapusan prefix sudah 3x");
    		return false;
    	}
    	
    	if(word.length() < 5){
    		Stemmer.broadcast.onStemmingFailed("Kata terlalu pendek");
    		return false;
    	}
    	
    	String currentPrefix = word.substring(0, 2);
    	int wordLength = word.length();
    	
    	if(currentPrefix.equals(previousPrefix)){
    		Stemmer.broadcast.onStemmingFailed("Prefix kata sama dengan prefix yang telah dihapus sebelumnya!");
    		return false;
    	}
    	
    	
    	/*--------------------------------------------------------------
    	 * Jika kata mengandung awalan standar yaitu di- ke- se- maka
    	 * maka awalan tersebut dapat langsung dihilangkan 
    	 *  ------------------------------------------------------------
    	 */
    	if(word.matches("^(di|[ks]e)[a-zA-Z]+$")){
    		
    		Stemmer.broadcast.onStemmingProgress("Kata memiliki standar prefix...");
    		
    		word = word.substring(2, wordLength);
    		
			if(typeIsFound(word)){
				Stemmer.broadcast.onStemmingMatch(RESULT);
				Stemmer.broadcast.onStemmingProgress("Tipe kata ditemukan setelah proses ");
				return true;
			}	
    	}
    	
    	if(word.matches("^([mbpt]e)[a-zA-Z]+$")){
    		Stemmer.broadcast.onStemmingProgress("Kata memiliki kompleks prefix...");
    		
    		// awalan ber- dan bel-
    		if(word.matches("^([bt]er|bel)([bcdfghjkmnpqstvwxyz]{0,1}[a-zA-Z]+)$")){
    			
    			// buang awalan ber- --> aturan 2,3,5,7,8,9
    			if(typeIsFound(word.substring(3, wordLength))){
    				Stemmer.broadcast.onStemmingMatch(RESULT);
    				return true;
    			}
    			
    			// jika belum ketemu maka buang awalan be- --> aturan 1,4,6
    			if(typeIsFound(word.substring(2, wordLength))){
    				Stemmer.broadcast.onStemmingMatch(RESULT);
    				return true;
    			}
    			
    			// jika masih tidak ketemu, maka ubah kata asli dengan membuang ber- atau bel-
    			// selanjutnya kata ini akan digunakan pada iterasi selanjutnya
    			word = word.substring(3, wordLength);
    		}
    		
    		// awalan me-{lrwy}V --> aturan 10
    		if(word.matches("^(me)[lrwy][aiueo]+[a-zA-Z]*$")){
    			
    			word = word.substring(2, wordLength);
    			
    			if(typeIsFound(word)){
    				Stemmer.broadcast.onStemmingMatch(RESULT);
    				return true;
    			}
    		}
    		
    		// awalan mem-{bfv} dan mempe-{rl} dipotong menjadi mem- --> aturan 11 dan 12
    		if(word.matches("^(mem)([bvf]|pe[rl])[a-zA-Z]*$")){
    			
    			word = word.substring(3, wordLength);
    			
    			if(typeIsFound(word.substring(3, word.length()))){
    				Stemmer.broadcast.onStemmingMatch(RESULT);
    				return true;
    			}
    		}
    		
    		// awalan mem{rV|V} menjadi me-m{rV|V} atau me-p{rV|V} --> autran 13
    		if(word.matches("^(mem)r?[aiueo]+[a-zA-Z]*$")){
    			
    			// buang awalan me- dan ganti m dengan p
    			if(typeIsFound("p" + word.substring(3, wordLength))){
    				Stemmer.broadcast.onStemmingMatch(RESULT);
    				return true;
    			}
    			
    			// jika belum ketemu, buang me-
    			if(typeIsFound(word.substring(2, wordLength))){
    				Stemmer.broadcast.onStemmingMatch(RESULT);
    				return true;
    			}
    			
    			word = word.substring(2, wordLength);
    		}
    		
    		// awalan men- diikuti dengan {cdjz} dibuang men- --> aturan 14
    		if(word.matches("^(men)[cdjz][a-zA-Z]*$")){
    			word = word.substring(3, wordLength);
    			
    		
    			if(typeIsFound(word)){
    				Stemmer.broadcast.onStemmingMatch(RESULT);
    				return true;
    			}
    		}
    		
    		// awalan menV menjadi me-nV atau me-tV (n diganti t) --> aturan 15
    		if(word.matches("^(men)[aiueo][a-zA-Z]*$")){
    			
    			word = word.substring(2, wordLength);
    			// buang me
    			if(typeIsFound(word)){
    				Stemmer.broadcast.onStemmingMatch(RESULT);
    				return true;
    			}
    			
    			
    			// buang me- dan n diganti t
    			if(typeIsFound("t" + word.substring(1, word.length()))){
    				Stemmer.broadcast.onStemmingMatch(RESULT);
    				return true;
    			}
    			
    		}
    		
    		// awalan meng-{ghq} --> auturan 16
    		if(word.matches("^(meng)[ghq][a-zA-Z]*$")){
    			
    			word = word.substring(4, wordLength);
    			
    			if(typeIsFound(word)){
    				Stemmer.broadcast.onStemmingMatch(RESULT);
    				return true;
    			}
    		}
    		
    		// awalan meng-V atau meny-V buang meng/meny atau buang meng dan tambahkan -k 
    		// atau buang meny dan tambahkan -s --> aturan 17,18,30,31
    		if(word.matches("^([mp]en[gy])[aiueo][a-zA-Z]*$")){
    			
    			String prefix = word.substring(0, 4);
    			word = word.substring(4, word.length());
    			
    			if(prefix.matches("^[pm](eng)$")){
    				
    				if(typeIsFound(word)){
        				Stemmer.broadcast.onStemmingMatch(RESULT);
        				return true;
        			}
    				
    				if(typeIsFound("k" + word)){
    					Stemmer.broadcast.onStemmingMatch(RESULT);
        				return true;
    				}
    			}
    			
    			if(prefix.matches("^[pm](eny)")){
    				
    				// tambhakan -s
        			if(typeIsFound("s" + word)){
        				Stemmer.broadcast.onStemmingMatch(RESULT);
        				return true;
        			}
    			}
    		}
    		
    		// awalan mempV dimana V != 'e' --> aturan 19
    		if(word.matches("^(memp)[aiuo][a-zA-Z]")){
    			word = word.substring(3, wordLength);
    			
    			if(typeIsFound(word)){
    				Stemmer.broadcast.onStemmingMatch(RESULT);
    				return true;
    			}
    		}
    		
    		// awalan pe-{wy}V --> aturan 20
    		if(word.matches("^(pe)[wy][aiueo][a-zA-Z]*$")){
    			word = word.substring(2, wordLength);
    			if(typeIsFound(word)){
    				Stemmer.broadcast.onStemmingMatch(RESULT);
    				return true;
    			}
    		}
    		
    		// awalan per-V --> aturan 21
    		if(word.matches("^(per)[aiueo][a-zA-Z]*$")){
    			
    			if(typeIsFound(word.substring(2, wordLength))){
    				Stemmer.broadcast.onStemmingMatch(RESULT);
    				return true;
    			}
    			
    			word = word.substring(3, wordLength);
    			
    			if(typeIsFound(word)){
    				Stemmer.broadcast.onStemmingMatch(RESULT);
    				return true;
    			}
    			
    		}
    		
    		// awalan per-CAP dimana C != 'r' dan P != 'er', per-CAerV dimana C != 'r', pem-{b|f|V} atau pen-{c|d|j|z} 
    		// --> aturan 23,24,25,27
    		if(word.matches("^(per)[bcdfghjklmnpqstvwxyz][a-zA-Z]([^(er)]|(er)[aiueo])[a-z-A-Z]*$") /*aturan 23 dan 24*/ 
    				|| word.matches("^(pe)(m[bfaiueo]|n[cdjz])[a-zA-Z]*$") /* aturan 25,26,27 */){
    			
    			word = word.substring(3, word.length());
    			if(typeIsFound(word)){
    				Stemmer.broadcast.onStemmingMatch(RESULT);
    				return true;
    			}
    			
    		}
    		
    		// awalan pen-V menjadi pe-nV atau pe-tV --> aturan 28
    		if(word.matches("^(pen)[aiueo][a-zA-Z]*$")){
    			if(typeIsFound(word.substring(2, wordLength))){
    				Stemmer.broadcast.onStemmingMatch(RESULT);
    				return true;
    			}
    			
    			if(typeIsFound("t" + word.substring(3, wordLength))){
    				Stemmer.broadcast.onStemmingMatch(RESULT);
    				return true;
    			}
    			
    			word = word.substring(2, wordLength);
    		}
    		
    		// awalan peng-{g|h|q} --> aturan 29
    		if(word.matches("^(peng)[ghq][a-zA-Z]*$")){
    			word = word.substring(4, wordLength);
    			Stemmer.broadcast.onStemmingMatch(RESULT);
				return true;
    		}
    		
    		// awalan peng-V dan peny-V aturan 30,31
    		if(word.matches("^(pen[gy])[aiueo][a-zA-Z]*$")){
    			
    		}
    		
    		// awalan pel-V kecuali 'pelajar' --> aturan 32
    		if(word.matches("^(pel)[aiueo][a-zA-Z]*$")){
    			if(!word.equals("pelajar")){
    				word = word.substring(2, wordLength);
    				if(typeIsFound(word)){
    					Stemmer.broadcast.onStemmingMatch(RESULT);
        				return true;
    				}
    			}
    		}
    		
    		// awalan peCerV dengan C != {rwylmn} --> aturan 33
    		if(word.matches("^(pe)[^aiueorwylmn](er)[aiueo][a-zA-Z]*$")){
    			word = word.substring(3, wordLength);
    			if(typeIsFound(word)){
    				Stemmer.broadcast.onStemmingMatch(RESULT);
    				return true;
    			}
    		}
    		
    		// awalan peCP dengan C != rwylmn dan P != er
    		if(word.matches("^(pe)[aiueorwylmn][^er][a-zA-Z]*$")){
    			word = word.substring(2, wordLength);
    			if(typeIsFound(word)){
    				Stemmer.broadcast.onStemmingMatch(RESULT);
    				return true;
    			}
    		}
    		
    	}
    	
    	iteration++;
		Stemmer.broadcast.onStemmingProgress("Tipe kata tidak ditemukan, proses prefix removal dilanjutkan untuk iterasi ke " + iteration);;
		
		derivationPrefixRemoval(word, currentPrefix, iteration);
    	
    	return false;
    }
    
    private static boolean isInvalidPrefix(String word){
        
        if(word.length() < 2) return false;
        
        String[] tempArray = {"di","ke","se","be","me","te","pe"};
        List<String> validPrefix = new ArrayList<>(Arrays.asList(tempArray));
        
        String prefix = word.substring(0, 2);
        
        return !(validPrefix.contains(prefix));
    }
    
    private static boolean isProhibitedCombination(String word){
        
        String[] pattern = {
            "^([bks]e)[a-zA-Z]+(i)$",
            "^(di|me|te)[a-zA-Z]+(an)$",
            "^(ke|se)[a-zA-Z]+(kan)$"
        };
        
        
        for(String p:pattern){
            Matcher m = Pattern.compile(p).matcher(word);
            if(m.matches()){
                return true;
            }
        }
        
        return false;
    }
    
    private static boolean typeIsFound(String word){
       
    	try{
	        ResultSet res = STATEMENT.executeQuery(SQL_QUERY + word + "'");
	        
	        if(res.isBeforeFirst()){
	            res.absolute(1);
	
	            String wordType = res.getString("kode_katadasar");
	
//	            STEMMING_SEQUENCE.add(word);
	            RESULT.put(TOKEN_WORD, word);
	            RESULT.put(TOKEN_TYPE, wordType);
	
	            return true;
	        }
    	} catch (SQLException e){
    		Stemmer.broadcast.onStemmingFailed(e.getMessage());
    	}
        return false;
    }
    
}
