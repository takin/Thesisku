/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SemanticQA.controllers;

import SemanticQA.listeners.Broadcaster;
import SemanticQA.models.Question;
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
        
        String sentence = scan.nextLine();
        
        Question.process(sentence).then(new Broadcaster(){
            
            @Override
            public void onSuccess(String answer){
                cetak(answer);
            }
            
            @Override
            public void onFail(String reason){
                cetak(reason);
            }
            
        });
    }
    
    public static void cetak(String answer){
        System.out.println(answer);
    }
    
}
