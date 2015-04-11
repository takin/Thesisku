/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SemanticQA.models;

import SemanticQA.interfaces.ParserListener;
import SemanticQA.interfaces.ResultListener;
import SemanticQA.interfaces.TokenizerListener;
import SemanticQA.models.nlp.Parser;
import SemanticQA.models.nlp.Tokenizer;
import java.util.List;

/**
 *
 * @author syamsul
 */
public class Process implements TokenizerListener, ParserListener {
    
    private static Process process;
    private static String theQuestion;
    private static ResultListener resultListener;
    
    public static Process theQuestion(String question){
        theQuestion = question;
        
        process = new Process();
        return process;
    }
    
    public static void then(ResultListener listener){
        resultListener = listener;
        
        Tokenizer.tokenize(theQuestion).then(process);
    }

    @Override
    public void onTokenizeSuccess(List<String> taggedToken) {
        Parser.parse(taggedToken).then(process);
    }

    @Override
    public void onTokenizeFail(String reason) {
        resultListener.onFail(reason);
    }

    @Override
    public void onParseSuccess(List parseTree) {
        
    }

    @Override
    public void onParseFail(String reason) {
        resultListener.onFail(reason);
    }
    
}
