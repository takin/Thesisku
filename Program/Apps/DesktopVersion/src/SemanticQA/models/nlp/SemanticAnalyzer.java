/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SemanticQA.models.nlp;

import SemanticQA.listeners.SemanticAnalyzerListener;
import SemanticQA.listeners.TokenizerListener;
import java.util.List;

/**
 *
 * @author syamsul
 */
public class SemanticAnalyzer {
    
    private static String sentenceToAnalyze;
    
    public SemanticAnalyzer(String sentence){
        sentenceToAnalyze = sentence;
    }
    
    public static SemanticAnalyzer analyze(String sentence){
        return new SemanticAnalyzer(sentence);
    }
    
    public static void then(final SemanticAnalyzerListener listener){
     
        Tokenizer.tokenize(sentenceToAnalyze).then(new TokenizerListener() {

            @Override
            public void onTokenizeSuccess(List<String> taggedToken) {
                analyze(taggedToken);
            }

            @Override
            public void onTokenizeFail(String reason) {
                listener.onAnalyzeFail(reason);
            }
        });
    }
    
    /**
     * Method untuk melalukan validasi terhadap kalimat yang diinputkan
     * @return boolean indicate wheater the sentence is valid or not
     */
    private static void analyze(List<String> taggedWord){
        
        
        
    }
}
