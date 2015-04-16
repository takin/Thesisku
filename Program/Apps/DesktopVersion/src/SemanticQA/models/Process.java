/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SemanticQA.models;

import SemanticQA.helpers.Constant;
import SemanticQA.listeners.OntologyLoaderListener;
import SemanticQA.listeners.OntologyQueryListener;
import SemanticQA.listeners.SemanticAnalyzerListener;
import SemanticQA.listeners.ResultListener;
import SemanticQA.models.nlp.SemanticAnalyzer;
import SemanticQA.models.ontology.OntologyLoader;
import SemanticQA.models.ontology.OntologyQuery;
import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import java.util.List;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.BufferingMode;

/**
 *
 * @author syamsul
 */
public class Process implements SemanticAnalyzerListener, OntologyQueryListener, OntologyLoaderListener {
    
    private static String theQuestion;
    private static ResultListener resultListener;
    private static Process process;
    
    public Process(String question){
       theQuestion = question;
    }
    
    public static Process theQuestion(String question){
        return process = new Process(question);
    }
    
    public static void then(ResultListener listener){
        resultListener = listener;
        SemanticAnalyzer.analyze(theQuestion);
        SemanticAnalyzer.then(process);
    }

    @Override
    public void onAnalyzeSuccess(List parseTree) {
        OntologyLoader.load(Constant.ONTOLOGIES,Constant.ONTO_MERGED_URI).then(this);
    }

    @Override
    public void onAnalyzeFail(String reason) {
        resultListener.onFail(reason);
    }

    @Override
    public void onQueryExecuted(String result) {
        System.out.println("execution result: " + result);
    }

    @Override
    public void onQueryExecutionFail(String reason) {
        resultListener.onFail(reason);
    }

    @Override
    public void onOntologyLoaded(OWLOntology ontology) {
        
        OntologyQuery.build(ontology, new PelletReasoner(ontology, BufferingMode.BUFFERING));
        OntologyQuery.then("Bupati",this);
    }

    @Override
    public void onOntologyLoadFail(String reason) {
        System.out.println(reason);
    }
    
}
