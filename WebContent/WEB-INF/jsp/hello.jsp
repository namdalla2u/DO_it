<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>HoWooAccount</title>
<%-- jqGrid --%>
<script type="text/ecmascript"
	src="${pageContext.request.contextPath}/assets/js/i18n/grid.locale-kr.js"></script>
<script type="text/ecmascript"
	src="${pageContext.request.contextPath}/assets/js/jquery.jqGrid.min.js"></script>

<script>
	var openAPIGrid = "#openAPIGrid";

	$(document).ready(function() {
		createOpenAPIGrid();
		$("#openAPIButton").click(showOpenAPIGrid);
	});

	function createOpenAPIGrid() {
		$(openAPIGrid).jqGrid({
			cmTemplate : {
				sortable : false,
				resizable : false
			},
			colModel : [ {
				name : "locdate",
				label : "일자",
				width : 200,
				align : "center"
			}, {
				name : "dateName",
				label : "휴일명",
				width : 200,
				align : "center"
			} ],
			shrinkToFit : false,
			viewrecords : true,
			autowidth : true,
			rowNum : 10,
			height : 300,
			datatype : 'local'
			
		});
	}

	function showOpenAPIGrid() {
		$(openAPIGrid).jqGrid("clearGridData");

		// show loading message
		$(openAPIGrid)[0].grid.beginReq();
		
		var month;
		
		if($('#month').val() < 10){
			month = '0' + $('#month').val();
		}
		
		 $.ajax({
			type : "GET",
			url : "${pageContext.request.contextPath}/base/openApi.do",
			data : {
				"year" : $('#year').val(),
				"month" : month
			},
			dataType : "json",
			success : function(jsonObj) {
			//	var dataSet = JSON.parse(jsonObj.OpenAPI);
				console.log(jsonObj.OpenAPI);
				$(openAPIGrid).jqGrid('setGridParam', {
					data : jsonObj.OpenAPI
					//data : dataSet.response.body.items.item
				});

				$(openAPIGrid)[0].grid.endReq();

				$(openAPIGrid).trigger('reloadGrid');
			}
		}); 
	}
</script>


</head>
<style type="text/css">
</style>
<body>
	<div>
		<div>접속자수 : ${userCount} 명</div>
		<br />
		<div>
			<p>공휴일 보기</p>
		</div>
		<br />
		<div>
			<select id="year">
				<option>2019</option>
			</select> 
			
			<select id="month">
			<c:forEach begin="1" end="12" varStatus="status">
						<option>${status.current}</option>
			</c:forEach>

			</select> 
		<input type="button" id="openAPIButton" value="공휴일 보기">
		</div>
		<br />
		<div>
			<table id="openAPIGrid"></table>
		</div>
	</div>
</body>
</html>
