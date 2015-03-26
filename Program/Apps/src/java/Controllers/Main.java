/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controllers;

import Models.nlp.NGram;
import Models.nlp.Tokenizer;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author syamsul
 */
public class Main extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       

            String q = request.getParameter("q");
            
            Tokenizer tokenizer = new Tokenizer(q);
            
            List token = tokenizer.getToken();
            NGram ng = new NGram(token);
            
            request.getRequestDispatcher("Views/templates/test.html").forward(request, response);
    }

}
