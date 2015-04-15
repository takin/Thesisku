/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SemanticQA.listeners;

import java.util.List;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 *
 * @author syamsul
 */

public interface OntologyLoaderListener {
    public void onOntologyLoaded(OWLOntology ontology);
    public void onOntologyLoadFail(String reason);
}
