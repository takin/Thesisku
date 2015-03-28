/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author syamsul
 */
public class BahasaParser {
    
    private final List<Map<String,String>> TOKENS;
    private final List<Map<String,String>> RULES = new ArrayList<>();

    public BahasaParser(List<Map<String,String>> POSTTagged) {
        TOKENS = POSTTagged;
    }
    
    /**
     * Method untuk melalukan validasi terhadap kalimat yang diinputkan
     * @return boolean indicate wheater the sentence is valid or not
     */
    public boolean validateSentence(){
        
        for(Map token: TOKENS){
            
        }
        
        return false;
    }
}
