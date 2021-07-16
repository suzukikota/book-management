<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page import="java.util.*" %>
<%@ page import="bean.*" %>
<%@ page import="java.net.URLEncoder" %>
 <jsp:useBean id="obj" class="bean.BookBean"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>返却申請ページ</title>
<style>
	body{
		font-size: 20px;
		text-align: center;
		background-color:#ADD8E6;
		}
	.return{
		padding: 0.5em 1em;
		margin:2em;
		font-weight:bold;
		background:#FFF;
		border:solid 3px #6091d3;
		border-radius:10px;
		position:fixed;
		left:50%;
		top:50%;
		transform:translateX(-50%)
				  translateY(-50%);

	}
	.btn-square{
		display:inline-block;
		padding:0.5em 1em;
		text-decoration:none;
		background:#668ad8;
		color:#FFF;
		border-bottom:solid 4px #627295;
		border-radius:3px;
		}
	.btn-square2{
		display:inline-block;
		padding:0.5em 1em;
		text-decoration:none;
		background:#FFA07A;
		color:#FFF;
		border-bottom:solid 4px #627295;
		border-radius:3px;
		}

</style>
</head>
<body>

<div class="return">
<h2>書籍返却申請画面</h2>

<form action="#" method="post">
<!-- 		フォームの送り先は一旦#で保留 -->
<p>書籍番号:<input type="text" name="isbn" placeholder="例1234567891234" pattern="\d{13}" title="13桁の数字で入力してください">

	<!-- 	入力しされたisbn(書籍番号)を取得し、そのisbnを基に書籍名の取得 -->
	<%String isbn=request.getParameter("isbn");%>
	<button name="auto" onclick="location.href='ReturnForm.jsp?isbn=<%=isbn %>'" class="btn-square2">書籍名の取得</button></p>

		<%List<BookBean> list = obj.Rental(isbn);
			for(int i=0;i<list.size();i++){
				obj=list.get(i);%>
				<p>書籍番号:<%=obj.getIsbn() %></p>
				<p>書籍名:	<%=obj.getTitle() %></p>

				<p>返却予定日</p>
				<input required id="input1" type="text" name="return" placeholder="例2021/01/01" pattern="\d{4}/\d{2}/\d{2}" title="例2021/01/01" ><br><br>
				<input type="submit" value="申請ボタン" class="btn-square">
				</form>
				<%} %>


</div>
</body>
</html>