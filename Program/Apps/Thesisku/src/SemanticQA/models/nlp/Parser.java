/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SemanticQA.models.nlp;

import SemanticQA.interfaces.SemanticAnalyzerListener;
import SemanticQA.interfaces.TokenizerListener;

import java.util.List;
import java.util.Map;

/**	
 *
 * @author syamsul
 */
public class Parser implements TokenizerListener {
    
    private static String sentenceToAnalyze;
    private static Parser analyzer;
    private static SemanticAnalyzerListener broadcaster;
    
    public Parser(String sentence){
        sentenceToAnalyze = sentence;
    }
    
    public static Parser analyze(String sentence){
        return analyzer = new Parser(sentence);
    }
    
    public static void then(final SemanticAnalyzerListener listener){
     
        Tokenizer.tokenize(sentenceToAnalyze);
		Tokenizer.then(analyzer);
    }

	@Override
	public void onTokenizeSuccess(List<Map<String, String>> taggedToken) {
		
		buildParseTree(taggedToken);
	}

	@Override
	public void onTokenizeFail(String reason) {
		System.out.println(reason);
	}
	
	private static void buildParseTree(List<Map<String, String>> tokens){
		
		System.out.println(tokens);
		
		if(!tokens.isEmpty()){
			
			Map<String, String> currentTokenBuffer = tokens.remove(0);
			String currentTokenType = currentTokenBuffer.get(Stemmer.TOKEN_TYPE);
			
			Map<String, String> nextTokenBuffer = (tokens.isEmpty()) ? null : tokens.remove(0);
			
			switch(currentTokenType){
			case "N":
				
				// build frasa nominal
				
				
				
				break;
			case "PRON":
				
				// build frasa preposisional
				
				break;
			}
		}
	
	}
}
