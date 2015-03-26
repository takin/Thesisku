<%-- 
    Document   : index
    Created on : Apr 4, 2014, 9:14:54 AM
    Author     : syamsul
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Question Answering data Kabupaten di Nusa Tenggara Barat</title>
        <link href="assets/css/style.css" type="text/css" rel="stylesheet"/>
    </head>
    <body>
        <div class="header">
        <h1>Question Answering Data Kabupaten</h1>
        <h1>Nusa Tenggara Barat</h1>
        </div>
        <form id="qa" class="form-wrapper cf" method="GET" action="search">
            <input type="text" name="q" id="q" placeholder="Masukkan Pertanyaan..." required>
            <button type="submit">Cari</button>
        </form>
        <div class="spinner"></div>
        <div id="answerContainer"></div>
    </body>
    <script src="assets/js/jquery-1.10.2.min.js" type="text/javascript"></script>
    <script src="assets/js/action.js" type="text/javascript"></script>
</html>
