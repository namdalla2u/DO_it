<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title> 내정보관리</title>

    <%-- jqGrid --%>
    <script type="text/ecmascript" src="${pageContext.request.contextPath}/assets/js/i18n/grid.locale-kr.js"></script>
    <script type="text/ecmascript" src="${pageContext.request.contextPath}/assets/js/jquery.jqGrid.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/ui.jqgrid-bootstrap4.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/octicons/4.4.0/font/octicons.css">

    <script>
        $.jgrid.defaults.styleUI = 'Bootstrap4';
        $.jgrid.defaults.iconSet = "Octicons";
    </script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js"
            integrity="sha384-FzT3vTVGXqf7wRfy8k4BiyzvbNfeYjK+frTVqZeNDFl8woCbF0CYG6g2fMEFFo/i"
            crossorigin="anonymous"></script>
            
    <script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
	
    <script>
        $(document).ready(function(){
        	  $("#profileGrid").jqGrid({
        		   url:"${pageContext.request.contextPath}/hr/hr.do",
        		   mtype: "post",
        		   postData: {
        			   "method": "findEmployee",
                       "empCode": "${empCode}"
                   },
        		   datatype:"json",
         		   jsonReader:{root:"employeeInfo"},
        		   colModel: [
                       {name: "empCode", label: "사원번호", type: "text", key: true},
                       {name: "empName", label: "성명", type: "text"},
                       {name: "deptCode", label: "부서코드", type: "text"},
                       {name: "gender", label: "성별", type: "text"},
                       {name: "birthDate", label: "생년월일", type: "number"},
                       {name: "positionCode", label: "직급", type: "text"},
                       {name: "status", label: "상태", type: "text"}
                   ]
        });
        });
        
    </script>
</head>
<body>
    <div id="ProfileTag"><table id="profileGrid"></table></div>
</body>
</html>

