/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

/**
 *
 * @author syamsul
 */
public interface Broadcaster {
    
    public void onSuccess(Object model, String message);
    public void onFail(String message);
}
