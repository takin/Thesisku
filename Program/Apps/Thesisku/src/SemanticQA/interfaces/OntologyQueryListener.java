/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SemanticQA.interfaces;

/**
 *
 * @author syamsul
 */
public interface OntologyQueryListener {
    void onQueryExecuted(String result);
    void onQueryExecutionFail(String reason);
}
