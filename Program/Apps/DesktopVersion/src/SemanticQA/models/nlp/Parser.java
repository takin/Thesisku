/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SemanticQA.models.nlp;

import SemanticQA.listeners.Broadcaster;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author syamsul
 */
public class Parser {
    
    /**
     * Array list ini untuk menyimpan token asli dari hasil pos tagging dari 
     * kelas POSTagger
     */
    private List<Map<String,String>> TAGGED_WORD;
    /**
     * Array list ini digunakan untuk menampung struktur baru yang dibentuk oleh 
     * proses parsing berdasarkan aturan-aturan tata bahasa indonesia yang sudah
     * disiapkan
     */
    private List<Map<String,String>> TAGGED_PHRASE;
    /**
     * Interface untuk melakukan proses broadcasting hasil selama proses parsing
     * Hasil broadcast akan diterima oleh kelas ProcessQuestion untuk 
     * ditindak lanjuti
     */
    private static Broadcaster BROADCASTER;
    private static Parser PARSER;
    
    public static Parser parse(List token){
        
        PARSER = new Parser();
        
        PARSER.TAGGED_WORD = new ArrayList<>();
        PARSER.TAGGED_PHRASE = new ArrayList<>();
        
        return PARSER;
    }
    
    public static void then(Broadcaster broadcaster){
        BROADCASTER = broadcaster;
        PARSER.generatePhrase();
    }
    
    /**
     * Method untuk melalukan validasi terhadap kalimat yang diinputkan
     * @return boolean indicate wheater the sentence is valid or not
     */
    private void generatePhrase(){
        
        BROADCASTER.onParseSuccess(TAGGED_WORD);
        
        int nextToken = 1;
        int tokenLength = TAGGED_WORD.size();
        
        for(int i = 0; i < tokenLength; i++){
            
            Map currentWord = TAGGED_WORD.get(i);
            Map nextWord = TAGGED_WORD.get(nextToken);
            
            String currentWordType = currentWord.get("kode").toString();
            String nextWordType = nextWord.get("kode").toString();
            
            switch(currentWordType){
                case "N" :
                    
                    switch(nextWordType){
                        case "N":
                            
                            if(tokenLength < nextToken){
                                Map thirdToken = TAGGED_WORD.get((nextToken + 1));
                                String thirdTokenType = thirdToken.get("kode").toString();
                                
                                if(thirdTokenType.equals("N")){
                                    
                                }
                            }
                            
                            break;
                    }
                    
                    break;
            }
            
            if(nextToken < (TAGGED_WORD.size() - 1)){
                nextToken += 1;
            }
        }
        
    }
}
