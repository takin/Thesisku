package SemanticQA.controllers;

import SemanticQA.helpers.Constant;
import SemanticQA.interfaces.OntologyLoaderListener;
import SemanticQA.interfaces.OntologyQueryListener;
import SemanticQA.interfaces.SemanticAnalyzerListener;
import SemanticQA.models.nlp.Parser;
import SemanticQA.models.ontology.OntologyLoader;
import SemanticQA.models.ontology.OntologyQuery;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;

import java.util.List;
import java.util.Scanner;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.BufferingMode;

/**
 *
 * @author syamsul
 */
public class ThesisDesktopVersion {

    private static Scanner scan;

	/**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.print("Masukkan pertanyaan: ");
        scan = new Scanner(System.in);
        
        String sentence = scan.nextLine();
        
        Parser.analyze(sentence);
        Parser.then(new SemanticAnalyzerListener() {
			
			@Override
			public void onAnalyzeSuccess(List<Object> parseTree) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnalyzeFail(String reason) {
				// TODO Auto-generated method stub
				
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