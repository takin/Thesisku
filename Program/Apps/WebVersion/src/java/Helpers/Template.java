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
public class Template {
    
    private static String TPL;
    
    public static String person(){
        
        TPL = "<table>";
        TPL += "<tr><td>Nama</td><td>:</td><td>.....</td></tr>";
        TPL += "<tr><td>TTL</td><td>:</td><td>.....</td></tr>";
        TPL += "<tr><td>Alamat</td><td>:</td><td>.....</td></tr>";
        TPL += "<tr><td>Jabatan</td><td>:</td><td>.....</td></tr>";
        TPL += "</table>";
        return TPL;
    }
    
    public static String location(){
        return "..... Terletak di Kabupaten ....... dan berada pada koordinat .....";
    }
}
