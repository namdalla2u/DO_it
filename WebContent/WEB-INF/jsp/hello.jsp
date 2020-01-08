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
	var openAPIGrid = "#openAPIGrid";  // #openAPIGrid 문자열을 변수  openAPIGrid에 담는다. 

	$(document).ready(function() {
		createOpenAPIGrid();  //함수 createOpenAPIGrid() 호출
		$("#openAPIButton").click(showOpenAPIGrid);  //이벤트등록 openAPIButton을 클릭하면 showOpenAPIGrid 호출
	});

	function createOpenAPIGrid() {
		$(openAPIGrid).jqGrid({
			cmTemplate : { //colModel의 기본값을 override할 properties를 정의한다.모든 컬럼을 지정하는 대신 여기서 하나의 property로 가능하다.	
				sortable : false, // true 설정 시 마우스로 컬럼을 드래그앤드롭으로 순서를 변경할 수 있다. (defalt는 false)
				resizable : false // 컬럼의 resize 여부 정의(defalt는 true)
			},
			colModel : [ { //그리드 Body에 보여질 데이터의 속성(매개변수) 지정
				name : "locdate", //그리드에서 컬럼의 고유한 name을 설정하며, 이 속성은 필수로 설정해야 한다.
				label : "일자", //칼럼의 제목, 만약 값이 없으면 name속성에서 가져온다. 
				width : 200, //칼럼의 가로길이
				align : "center" //데이터정렬 . defalt는 left
			}, {
				name : "dateName",
				label : "휴일명",
				width : 200,
				align : "center"
			} ],
			shrinkToFit : false, // true로 해둘시, 컬럼의 width사이즈에 맞는 비율로 자동 조정된다고 함.
			viewrecords : true, // true설정시 그리드는 쿼리의 총 갯수 중에서 시작/끝 레코드 번호를 표시한다.  Pager bar 항목 오른쪽 하단에 'View X to Y out of Z' 이와같이 보여진다.
			autowidth : true,  // true 설정시 그리드의 상위 요소의 width로 자동 계산되어 표현된다.
			rowNum : 10,  // 한 페이지에 보여질 데이터(레코드)갯수.
			height : 300,  //그리드의 높이를 설정.
			datatype : 'local'  //그리드를 채우는 데이터 형식을 정의한다. local (array data)
			
		});
	}

	function showOpenAPIGrid() {
		$(openAPIGrid).jqGrid("clearGridData"); //clearGridData를 호출. 그리드에서 현재 로드된 데이터를 지우는 기능을 한다.  

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
