/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Models.nlp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author syamsul
 */
public class NGram {
    
//    private static List<String> TOKEN_TO_CHECK = new ArrayList();
    private static final List<String> URI = new ArrayList();
    private static final Map<String,List<String>> NON_TERMINAL_GRAMMAR = new HashMap();
    
    public NGram(List token){
//        TOKEN_TO_CHECK = token;
        NON_TERMINAL_GRAMMAR.put("NN", Arrays.asList("NN","VP"));
        NON_TERMINAL_GRAMMAR.put("VP", Arrays.asList("VP"));
        this.processNGram(token);
    }
    
    private boolean finalProcess(String ngram){
        return false;
    }
    
    public List<String> getNGram(){
        return URI;
    }
    
    private void processNGram(List token){
        List<String> TOKEN_TO_CHECK = (List) token;
        int numOfToken = TOKEN_TO_CHECK.size();
        for( int i = 0; i< numOfToken; i++ ){
            int n1 = i;
            int n2 = i < numOfToken ? i + 1 : i;
            int n3 = i < numOfToken + 1 ? i+2 : i;
            
            
            
//            StringTokenizer firstToken = new StringTokenizer(TOKEN_TO_CHECK.get(n1));
//            StringTokenizer secondToken = new StringTokenizer(TOKEN_TO_CHECK.get(n2));
//            StringTokenizer thirdToken = new StringTokenizer(TOKEN_TO_CHECK.get(n3));
            
            String[] firstString = TOKEN_TO_CHECK.get(n1).split(" ");
//            String firstStringTag = firstToken.nextToken().toString();
            String[] secondString = TOKEN_TO_CHECK.get(n2).split(" ");
//            String secondStringTag = secondToken.nextToken().toString();
            String[] thirdString = TOKEN_TO_CHECK.get(n3).split(" ");
//            String thirdStringTag = thirdToken.nextToken().toString();
            
            if( !this.finalProcess(firstString[0]) ){
//                if( firstString[0] == null ? secondString[0] != null : !firstString[0].equals(secondString[0]) ){
//                    if(NON_TERMINAL_GRAMMAR.get(firstString[1]).contains(secondString[1])){
                        String bigram = firstString[0] + "_" + secondString[0];
                        if(!this.finalProcess(bigram)){
                            String trigram = firstString[0] + "_" + secondString[0] + "_" + thirdString[0];
                            this.finalProcess(trigram);
                        }
//                    }
//                }
            }
        }
    }
}
