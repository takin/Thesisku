package SemanticQA.controllers;

import SemanticQA.models.Process;
import SemanticQA.helpers.Constant;
import SemanticQA.listeners.OntologyLoaderListener;
import SemanticQA.listeners.OntologyQueryListener;
import SemanticQA.listeners.ResultListener;
import SemanticQA.listeners.SemanticAnalyzerListener;
import SemanticQA.listeners.StemmingListener;
import SemanticQA.listeners.TokenizerListener;
import SemanticQA.models.nlp.SemanticAnalyzer;
import SemanticQA.models.nlp.Stemming;
import SemanticQA.models.nlp.Tokenizer;
import SemanticQA.models.ontology.OntologyLoader;
import SemanticQA.models.ontology.OntologyQuery;
import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.BufferingMode;

/**
 *
 * @author syamsul
 */
public class ThesisDesktopVersion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        System.out.print("Masukkan pertanyaan: ");
        Scanner scan = new Scanner(System.in);
        
        String sentence = scan.nextLine();
        
        Stemming.stem(sentence).then(new StemmingListener() {

            @Override
            public void onStemmingMatch(Map<String,Object> finalWord) {
                System.out.println(finalWord);
//                cetak(finalWord.get(Stemming.ORIGINAL_WORD).toString() + " -> " + finalWord.get(Stemming.STEMMED_WORD).toString() + " -> " + finalWord.get(Stemming.WORD_TYPE).toString());
            }

            @Override
            public void onStemmingNotMatch(String originalWord) {
                System.out.println("word did not match");
                cetak(originalWord);
            }

            @Override
            public void onStemmingFailed(String reason) {
                System.out.println("stemming failed");
                cetak(reason);
            }
        });

    }
    
    public static void ontology(){
        OntologyLoader.load(Constant.ONTOGOV_URL);
        OntologyLoader.then(new OntologyLoaderListener() {

            @Override
            public void onOntologyLoaded(OWLOntology ontology) {
                
                OntologyQuery.build(ontology, new PelletReasoner(ontology, BufferingMode.BUFFERING));
                OntologyQuery.then("<http://www.ntbprov.go.id/semweb/resource/lombok_timur>",new OntologyQueryListener() {

                    @Override
                    public void onQueryExecuted(String result) {
                        System.out.println(result);
                    }

                    @Override
                    public void onQueryExecutionFail(String reason) {
                        cetak(reason);
                    }
                });
            }

            @Override
            public void onOntologyLoadFail(String reason) {
            }
        });
    }
    
    public static void cetak(String answer){
        System.out.println(answer);
    }
    
}
