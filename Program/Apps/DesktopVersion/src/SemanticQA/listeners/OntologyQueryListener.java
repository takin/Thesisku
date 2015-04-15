/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SemanticQA.listeners;

/**
 *
 * @author syamsul
 */
public interface OntologyQueryListener {
    public void onQueryExecuted(String result);
    public void onQueryExecutionFail(String reason);
}
