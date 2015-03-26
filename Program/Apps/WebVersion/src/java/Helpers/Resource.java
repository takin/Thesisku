/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Helpers;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author syamsul
 */
public class Resource {
    
    private static final Map<String, Object> RESOURCE = new HashMap();
    
    public static void setResource(String key, Object value){
        
        RESOURCE.put(key, value);
        
    }
    
}
