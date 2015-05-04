/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SemanticQA.listeners;

import org.semanticweb.owlapi.model.OWLOntology;

/**
 *
 * @author syamsul
 */

public interface OntologyLoaderListener {
    void onOntologyLoaded(OWLOntology ontology);
    void onOntologyLoadFail(String reason);
}
