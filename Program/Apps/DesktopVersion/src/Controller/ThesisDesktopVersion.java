/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Models.POSTagger;
import Models.ProcessQuestion;
import java.util.Scanner;

/**
 *
 * @author syamsul
 */
public class ThesisDesktopVersion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        System.out.print("Masukkan pertanyaan: ");
        Scanner scan = new Scanner(System.in);
        
        String q = scan.nextLine();
        
        ProcessQuestion p = new ProcessQuestion(q);
        
//        System.out.println(p.getAnswer());
    }
    
}
