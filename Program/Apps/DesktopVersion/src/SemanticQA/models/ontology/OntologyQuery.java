/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SemanticQA.models.ontology;

import SemanticQA.listeners.OntologyQueryListener;
import de.derivo.sparqldlapi.Query;
import de.derivo.sparqldlapi.QueryEngine;
import de.derivo.sparqldlapi.QueryResult;
import de.derivo.sparqldlapi.exceptions.QueryEngineException;
import de.derivo.sparqldlapi.exceptions.QueryParserException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLDataPropertyImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLNamedIndividualImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

/**
 *
 * @author syamsul
 */
public class OntologyQuery {
    
    private static OWLOntology ontology;
    private static OWLReasoner reasoner;
    private static OntologyQuery ontologyQuery;
    
    public OntologyQuery(OWLOntology ontology, OWLReasoner reasoner){
        OntologyQuery.ontology = ontology;
        OntologyQuery.reasoner = reasoner;
    }
    
    /**
     * Static method sebagai entry point untuk melakukan proses query
     * @param ontology -> ontologi yang akan di query
     * @param reasoner -> reasoner yang digunakan sebagai reasoning engine 
     * pada saat sparqldl api melakukan query
     * @return Object OntologyQuery
     */
    public static OntologyQuery build(OWLOntology ontology, OWLReasoner reasoner){
        return ontologyQuery = new OntologyQuery(ontology, reasoner);
    }
    
    /**
     * Method utama untuk melakukan proses query
     * methhod ini digunakan ketika menggunakan Pub/Sub pattern
     * 
     * @param query
     * @param listener objek listener yang akan menerima setiap hasil proses
     */
    public static void then(String query, OntologyQueryListener listener){
        
        try {
            listener.onQueryExecuted(ontologyQuery.execute(query));
        } catch (QueryParserException | QueryEngineException ex) {
            listener.onQueryExecutionFail(ex.getMessage());
        }
        
    }
    
    private static String buildQuery(String q){
        String query = getPrefix();
        query += "SELECT ?person WHERE { Type(?person, "+ q +") }";
        
        return query;
    }
    
    /**
     * Method untuk melakukan proses pebentukan PREFIX yang nantinya 
     * akan digunakan dalam pembentukan query sqprql dl 
     * 
     * PREFIX diambil dari semua URI dari ontologi, baik itu Class, Datatype
     * Object property maupun instance
     */
    private static String getPrefix(){
        
        String prefix = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"
                + "PREFIX qa: <http://www.ntbprov.go.id/semweb/resource/>\n";
        return prefix;
    }
    
    public String execute(String criteria) throws QueryParserException, QueryEngineException{
        
        String query = buildQuery(criteria);
        
        Query q = Query.create(query);
        QueryEngine qe = QueryEngine.create(ontology.getOWLOntologyManager(), reasoner);
        
        QueryResult res = qe.execute(q);
        
        return (res.isEmpty()) ? "Empty query result" : res.toJSON();
    }
    
}
