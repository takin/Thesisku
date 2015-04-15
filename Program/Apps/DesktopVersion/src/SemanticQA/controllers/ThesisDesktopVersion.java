/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SemanticQA.controllers;

import SemanticQA.helpers.Constant;
import SemanticQA.listeners.OntologyLoaderListener;
import SemanticQA.listeners.OntologyQueryListener;
import SemanticQA.models.ontology.OntologyLoader;
import SemanticQA.models.ontology.OntologyQuery;
import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import de.derivo.sparqldlapi.QueryResult;
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
        
        
//        System.out.print("Masukkan pertanyaan: ");
//        Scanner scan = new Scanner(System.in);
        
//        String sentence = scan.nextLine();
        
//        Process.theQuestion(sentence).then(new ResultListener(){
//            
//            @Override
//            public void onSuccess(String answer){
//                cetak(answer);
//            }
//            
//            @Override
//            public void onFail(String reason){
//                cetak(reason);
//            }
//            
//        });
        
        OntologyLoader.load(Constant.ONTOLOGIES, Constant.ONTO_MERGED_URI);
        OntologyLoader.then(new OntologyLoaderListener() {

            @Override
            public void onOntologyLoaded(OWLOntology ontology) {
                OntologyQuery.build(ontology, new PelletReasoner(ontology, BufferingMode.BUFFERING));
                OntologyQuery.then(new OntologyQueryListener() {

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
