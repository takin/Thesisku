/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SemanticQA.interfaces;

import java.util.Map;

/**
 *
 * @author syamsul
 */
public interface StemmingListener {
    void onStemmingMatch(Map<String,String> result);
    void onStemmingProgress(String message);
    void onStemmingFailed(String reason);
}
