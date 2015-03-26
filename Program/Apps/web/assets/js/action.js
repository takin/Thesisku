/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var loader = '<div class="rect1"></div><div class="rect2"></div><div class="rect3"></div><div class="rect4"></div><div class="rect5"></div><span class="loadingtext">Loading...</span></div>';
var spinner = $(".spinner");
var answerContainer = $("#answerContainer");
var queries = null;

$(function (){
    $("#q").focus();
});

$("#qa").submit(function (e){
    // prevent default form subit action
    e.preventDefault();
    
    // prepare the data to be sent to server
    queries = $(this).serializeArray().pop();
    
    // append the AJAX loader
    answerContainer.fadeOut(function(){
       spinner.html(loader).fadeIn(function(){
        getAjax();
    }); 
    });

});

function getAjax(){
    // do the ajax call 
    $.ajax({
       url: $("#qa").attr("action"),
       method: "GET",
       data: {q: queries.value},
       success: function(res){
           spinner.fadeOut(function(){
              answerContainer.html(res).show(); 
           });
       }
    });
}