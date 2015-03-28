/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Helpers.Broadcaster;
import java.util.List;
import java.util.Map;

/**
 *
 * @author syamsul
 */
public class ProcessQuestion {
    
    private final String Question;
    private String Answer;
    
    public ProcessQuestion(String question){
        Question = question;
        
        processQuestion();
    }
    
    private void processQuestion(){
        POSTagger tagging = new POSTagger(Question);
        
        
    }
    
    public String getAnswer(){
        return Answer;
    }
}
