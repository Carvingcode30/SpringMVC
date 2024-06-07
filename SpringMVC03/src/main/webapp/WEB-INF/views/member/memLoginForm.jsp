<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath }"/>     
    
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Bootstrap Example</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
  <script type="text/javascript">
	$(document).ready(function() {
  		if(${!empty msgType}) {
  				$("#messageType").attr("class", "modal-content panel-warning");
  				$("#myMessage").modal("show");
  		}
  	});
  </script>
  
</head>
<body>


<div class="container">
<jsp:include page="../common/header.jsp"></jsp:include>
  <h2>로그인</h2>
  <div class="panel panel-default">
    <div class="panel-heading">로그인 화면</div>
    <div class="panel-body">
    	<form action="${contextPath}/memLogin.do" method="post">
			<table class="table table-bordered" style="text-align: center; border: 1px solid #dddddd;">
				<tr>
					<td style="width: 110px; vertical-align: middle;">아이디</td>
					<td><input class="form-control" id="memID" name="memID" type="text" maxlength="20"></td>
				</tr>
				<tr>
					<td style="width: 110px; vertical-align: middle;">비밀번호</td>						<!-- 비밀번호 눌렀을 때 같은지 확인 -->
					<td colspan="2"><input type="password" class="form-control" id="memPassword" name="memPassword"></td>
				</tr>
				<tr>
					<td colspan="2" style="text-align: left;">
						<input type="submit" class="btn btn-primary btn-sm pull-right" value="로그인"/>
					</td>
				</tr>
			</table>
		</form>
    </div>
    <!-- 실패 메세지 -->
    <!--  실패 메세지를 출력(modal) -->
	<!-- 다이얼로그창(모달)  -->
     <!-- Modal -->
  <div class="modal fade" id="myMessage" role="dialog">
    <div class="modal-dialog">
      <!-- Modal content-->
      <div id="messageType" class="modal-content panel-info">
        <div class="modal-header panel-heading">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">${msgType }</h4>
        </div>
        <div class="modal-body">
          <p>${msg } </p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
</div>
    <div class="panel-footer">스프1탄_인프런</div>
  </div>
</div>

</body>
</html>
    