<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title> 내정보관리</title>
    <script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
    
    <script>
       
        	$(document).ready(function(){
        		$("#UpdateEmployee").click(UpdateEmployee); //저장 버튼 클릭시 UpdateEmployee 함수 호출
        		$("#reset").click(reset); //취소버튼 클릭시 reset함수 호출
 			   $.ajax({
 				   type: "POST",
                    url: "${pageContext.request.contextPath}/hr/hr.do",
                    data: {
                        "method": "findEmployee",
                        "empCode": "${empCode}"
                    },
                    dataType: "json",
                    success: function(obj){
                		var employee=obj.employeeInfo;               		
 						switch(obj.errorCode){
 						case 1: setInfo(employee); break;
 						case -1: alert("서버오류"); break;
 						case -2: alert("DB오류"); break;
 						default : alert(obj.errorMsg);
 						}
                    }
                });
        });
        	function setInfo(employee){ //폼태그에 데이터 셋팅
        				console.log(employee);
						$("#empName").val(employee.empName);
						$("#empCode").val(employee.empCode);
						$("#eMail").val(employee.eMail);
						$("#userPw").val(employee.userPw);
						$("#phoneNumber").val(employee.phoneNumber);
						$("#socialSecurityNumber").val(employee.socialSecurityNumber);
						$("#zipCode").val(employee.zipCode);
						$("#basicAddress").val(employee.basicAddress);
						$("#detailAddress").val(employee.detailAddress);
        	}
        	
        	function UpdateEmployee(){ //저장 버튼클릭시 실행되는 로직(회원정보수정)
        		console.log("버튼함수실행");
        	     var employeeInfo = {};

                 var inputForm = $("#infoForm input[id]");
               
                 console.log(inputForm);
                 console.log(inputForm[0].value);
                 $.each(inputForm, function (index, obj) {
                 	
                     employeeInfo[obj.id] = $("input#" + obj.id).val();
                     console.log(employeeInfo[obj.id]);
                 });


                 var jsonString = JSON.stringify(employeeInfo);
                 console.log(jsonString);

        		 $.ajax({
                     url: "${pageContext.request.contextPath}/hr/hr.do",
                     type: "post",
                     data: {
                         "method": "batchEmpInfo",
                         "employeeInfo": jsonString,
                     },
                     dataType: "json",
                     success: function(){
                    	 alert("수정되었습니다");
                    	 location.reload();
  						}
                 });
        	}
        
        	
            function zipCodeListFunc() { //우편번호검색 함수
        		new daum.Postcode({
        			oncomplete : function(data) {
        				// 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분입니다.
        				// 예제를 참고하여 다양한 활용법을 확인해 보세요.
        				$("#zipCode").val(data.zonecode);
        				$("#basicAddress").val(data.address);
        				$("#detailAddress").val(data.buildingName);
        			}
        		}).open();
        	}
            
             function reset(){ //취소버튼 누르면 페이지가 리로드 된다.
//             	 console.log("취소버튼실행");
            	 location.reload();
             }
             
             function inputPhoneNumber(obj) { //하이픈이 자동으로 생기게 하는 함수, 숫자값만 입력되는 기능 추가해야함 
            	 
            	    var number = obj.value.replace(/[^0-9]/g, "");
            	    var phone = "";


            	    if(number.length < 4) {
            	        return number;
            	    } else if(number.length < 7) {
            	        phone += number.substr(0, 3);
            	        phone += "-";
            	        phone += number.substr(3);
            	    } else if(number.length < 11) {
            	        phone += number.substr(0, 3);
            	        phone += "-";
            	        phone += number.substr(3, 3);
            	        phone += "-";
            	        phone += number.substr(6);
            	    } else {
            	        phone += number.substr(0, 3);
            	        phone += "-";
            	        phone += number.substr(3, 4);
            	        phone += "-";
            	        phone += number.substr(7);
            	    }
            	    obj.value = phone;
             }
     		
 
        
    </script>
</head>
<body>
			
	<div class="col-12 card">
		<div class="card-body">

			<form id="infoForm" style="width: 100%;">
			<div class="row">
				<div class="col-md-3">
  					<div class="form-group">
   					<label for="Name">이름</label>
    				<input type="text" class="form-control" id="empName" readonly>
  					</div>
  				</div>
  					<div class="col-md-3">
  					<div class="form-group">
   					<label for="JuminNum">주민번호</label>
    				<input type="text" class="form-control" id="socialSecurityNumber" readonly>
  					</div>
  				</div>
  			</div><br>
  			<div class="row">	
				<div class="col-md-3">
  					<div class="form-group">
					<label for="Code">코드</label>
    				<input type="text" class="form-control" id="empCode" readonly>
					</div>
				</div>
				<div class="col-md-3">
  					<div class="form-group">
					<label for="Password">비밀번호</label>
    				<input type="password" class="form-control" id="userPw" >
					</div>
				</div>
 			</div><br>
 			<div class="row">
 				<div class="col-md-3">
 					<div class="form-group">
					<label for="phoneNumber">전화번호</label>
					<input type="tel" class="form-control" id="phoneNumber"  onKeyup="inputPhoneNumber(this);" maxlength="13">
					</div>
				</div>
  				<div class="col-md-5">
 					<div class="form-group">
					<label for="Email">이메일</label>
					<input type="email" class="form-control" id="eMail" placeholder="입력된 정보가 없습니다">
					</div>
				</div>
 			</div><br>
 			<div class="row">
 			<div class="form-group col-md-3">
                    <label for="zipCode" class="col-form-label">우편번호</label>
                        <div class="input-group">
                        <input class="form-control" type="text" id="zipCode" readonly>
                        	<div class="input-group-append">
                        	<button type="button" class="form-control btn btn-outline-secondary" onclick="zipCodeListFunc()">
                        	<i style="color:#FD7D86;" class="fa fa-search" aria-hidden="true"></i>
                        	</button>
                        	</div>
                        </div>
            	</div>
            	<div class="col-md-5">
 					<div class="form-group">
					<label for="basicAddress">기본주소</label>
					<input type="text" class="form-control" id="basicAddress" readonly >
					</div>
				</div>
				<div class="col-md-3">
 					<div class="form-group">
					<label for="detailAddress">상세주소</label>
					<input type="text" class="form-control" id="detailAddress" >
					</div>
				</div>
 			</div><br>
 			<div class="buttonTag">
                <button type="button" class="btn btn-warning" id="reset">취소</button>
                <button type="button" class="btn btn-success" id="UpdateEmployee">저장</button>
            </div>
			</form>
			</div>
		</div>

</body>
</html>

