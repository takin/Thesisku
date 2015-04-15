/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SemanticQA.models.nlp;

import SemanticQA.listeners.SemanticAnalyzerListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author syamsul
 */
public class SemanticAnalyzer {
    
    /**
     * Array list ini untuk menyimpan token asli dari hasil pos tagging dari 
     * kelas POSTagger
     */
    private List<String> TAGGED_WORD;
    /**
     * Array list ini digunakan untuk menampung struktur baru yang dibentuk oleh 
     * proses parsing berdasarkan aturan-aturan tata bahasa indonesia yang sudah
     * disiapkan
     */
    private List<String> TAGGED_PHRASE;
    /**
     * Interface untuk melakukan proses broadcasting hasil selama proses parsing
     * Hasil broadcast akan diterima oleh kelas ProcessQuestion untuk 
     * ditindak lanjuti
     */
    private static SemanticAnalyzerListener parserListener;
    private static SemanticAnalyzer PARSER;
    
    public static SemanticAnalyzer analyze(List<String> token){
        
        PARSER = new SemanticAnalyzer();
        PARSER.TAGGED_WORD = token;
        PARSER.TAGGED_PHRASE = new ArrayList<>();
        
        return PARSER;
    }
    
    public static void then(SemanticAnalyzerListener listener){
        parserListener = listener;
        PARSER.generatePhrase();
    }
    
    /**
     * Method untuk melalukan validasi terhadap kalimat yang diinputkan
     * @return boolean indicate wheater the sentence is valid or not
     */
    private void generatePhrase(){
        parserListener.onAnalyzeSuccess(TAGGED_WORD);
        
        int nextToken = 1;
        int tokenLength = TAGGED_WORD.size();
        
        for(int i = 0; i < tokenLength; i++){
            
            String[] currentWord = TAGGED_WORD.get(i).split(";");
            String word = currentWord[0];
            String wordType = (currentWord.length > 1) ? currentWord[1] : "UN";
            
            switch(wordType){
                case "N" :
                    
                    
                    break;
            }
            
            if(nextToken < (TAGGED_WORD.size() - 1)){
                nextToken += 1;
            }
        }
        
    }
}
