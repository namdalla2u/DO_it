<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>거래처 등록/등록해제</title>

<%-- jqGrid --%>
<script type="text/ecmascript"
	src="${pageContext.request.contextPath}/assets/js/i18n/grid.locale-kr.js"></script>
<script type="text/ecmascript"
	src="${pageContext.request.contextPath}/assets/js/jquery.jqGrid.min.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/ui.jqgrid-bootstrap4.css" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/octicons/4.4.0/font/octicons.css">

<script>
	$.jgrid.defaults.styleUI = 'Bootstrap4';
	$.jgrid.defaults.iconSet = "Octicons";
</script>

<%--DatePicker--%>
<link
	href="${pageContext.request.contextPath}/assets/css/datepicker.min.css"
	rel="stylesheet">
<script
	src="${pageContext.request.contextPath}/assets/js/datepicker.min.js"></script>
<script
	src="${pageContext.request.contextPath}/assets/js/datepicker.ko-KR.js"></script>

<script>
	var accountUnregisteredGrid = "#accountUnregisteredGrid"; //미등록 회사 그리드
	var accountRegisteredGrid = "#accountRegisteredGrid"; //등록 회사 그리드
	var status = null;
	var approval = "등록";
	var unApproval = "미등록";

	$(document).ready(
			function() {
				createAccountUnregisteredGrid(); //미등록 회사 그리드 생성
				createAccountRegisteredGrid();// 등록회사 그리드 생성
				
				showApprovalStatusList();

				$("#refresh").click(function() { // 새로고침
					showApprovalStatusList();
				});
				
				$("#AddWorkplace").click(function(){ // 회사 추가
					location.href="${pageContext.request.contextPath}/company/WorkplaceInserForm.html";
				});
									
				$("#deleteWorkplace").click(function(){ //회사삭제
						var getSelectedSell = [];
						getSelectedSell = $(accountUnregisteredGrid).jqGrid(
								'getGridParam', 'selarrrow');
						var isCheck=confirm("정말로 삭제하시겠습니까? \n(거래처로 등록되어있는 회사는 해제하고 삭제가능)");
						if(isCheck){
							eliminationWorkplace(getSelectedSell); //회사삭제	
						}
					});
				
				$("#Release").click( //해제
						function() {
							var getSelectedSell = [];
							getSelectedSell = $(accountRegisteredGrid).jqGrid(
									'getGridParam', 'selarrrow');
							status = unApproval
							updateApprovalStatus(getSelectedSell);
						})

				$("#Approval").click( //승인
						function() {
							var getSelectedSell = [];
							getSelectedSell = $(accountUnregisteredGrid)
									.jqGrid('getGridParam', 'selarrrow');
							status = approval
							updateApprovalStatus(getSelectedSell);
						});
			});

	function createAccountUnregisteredGrid() { //미등록 회사 그리드 생성
		$(accountUnregisteredGrid).jqGrid({
			colModel : [ {
				name : "workplaceCode",
				label : "사업장코드",
				width : 100,
				key : true,
				align : "center"
			}, {
				name : "companyCode",
				label : "회사코드",
				width : 100,
				align : "center"
			}, {
				name : "workplaceName",
				label : "사업장명",
				width : 100,
				align : "center"
			}, {
				name : "workplaceCeoName",
				label : "대표자명",
				width : 100,
				align : "center"
			}, {
				name : "businessConditions",
				label : "업태",
				width : 130,
				align : "center"
			},

			{
				name : "businessLicense",
				label : "사업자등록번호",
				width : 130,
				align : "center"
			}, {
				name : "corporationLicence",
				label : "법인등록번호",
				width : 130,
				align : "center"
			}, {
				name : "workplaceTelNumber",
				label : "사업장전화번호",
				width : 130,
				align : "center"
			}, {
				name : "approvalStatus",
				label : "승인상태",
				width : 100,
				align : "center"
			} ],
			multiselect : true,
			viewrecords : true,

			width : 1150,
			height : 268,
			rowNum : 6,
			pager : "#UnregisteredGridPager",
			datatype : "local",
			onSelectRow : function(key) {
				
			}
		}).jqGrid("setGroupHeaders", {		
			useColSpanStyle : false,
			groupHeaders :  [{
				"numberOfColumns" : 9,
				"startColumnName" : "workplaceCode",
				"titleText" : "미등록회사"
			}]
		}); 
	}
	

	
	function createAccountRegisteredGrid() { //등록 회사 그리드 생성
		$(accountRegisteredGrid).jqGrid({
			colModel : [ {
				name : "workplaceCode",
				label : "사업장코드",
				width : 100,
				key : true,
				align : "center"
			}, {
				name : "companyCode",
				label : "회사코드",
				width : 100,
				align : "center"
			}, {
				name : "workplaceName",
				label : "사업장명",
				width : 100,
				align : "center"
			}, {
				name : "workplaceCeoName",
				label : "대표자명",
				width : 100,
				align : "center"
			}, {
				name : "businessConditions",
				label : "업태",
				width : 130,
				align : "center"
			},

			{
				name : "businessLicense",
				label : "사업자등록번호",
				width : 130,
				align : "center"
			}, {
				name : "corporationLicence",
				label : "법인등록번호",
				width : 130,
				align : "center"
			}, {
				name : "workplaceTelNumber",
				label : "사업장전화번호",
				width : 130,
				align : "center"
			}, {
				name : "approvalStatus",
				label : "승인상태",
				width : 130,
				align : "center"
			} ],
			multiselect : true,
			viewrecords : true,

			width : 1150,
			height : 268,
			rowNum : 6,
			pager : "#registeredGridPager",
			datatype : "local",
			onSelectRow : function(key) {
				
			}
		}).jqGrid("setGroupHeaders", {		
			useColSpanStyle : false,
			groupHeaders :  [{
				"numberOfColumns" : 9,
				"startColumnName" : "workplaceCode",
				"titleText" : "등록회사"
			}]
		});

	}
	
	function showApprovalStatusList() { // 회사리스트 불러오기
		$(accountUnregisteredGrid)[0].grid.beginReq();
	
		$(accountUnregisteredGrid).jqGrid('clearGridData');
		$(accountRegisteredGrid).jqGrid('clearGridData');
		$.ajax({
					type : "POST",
					url : "${pageContext.request.contextPath}/company/AccountRegisteredList.do",
					data : {
						"method" : "getAllWorkplaceList"
					},
					dataType : "json",
					success : function(jsonObj) {
						var  approvalList= []; // 승인 리스트
						var  unApprovalList = []; // 미승인 리스트
						for(index in jsonObj.allWorkplaceList){
							var approvalStatusList=jsonObj.allWorkplaceList[index];
							
							if(approvalStatusList.approvalStatus==unApproval) // 미승인
								unApprovalList.push(jsonObj.allWorkplaceList[index]);

							else if(approvalStatusList.approvalStatus==approval) // 승인
								approvalList.push(jsonObj.allWorkplaceList[index]);
							
							 	$(accountUnregisteredGrid).jqGrid("setGridParam", {
									data : unApprovalList
								});
									$(accountUnregisteredGrid).trigger("reloadGrid"); 
												
								$(accountRegisteredGrid).jqGrid("setGridParam", {
									data : approvalList
								});
									$(accountRegisteredGrid).trigger("reloadGrid");
						}
					}
				});
		$(accountUnregisteredGrid)[0].grid.endReq();
	}

	function updateApprovalStatus(getSelectedSell) {
		$.ajax({
					type : "POST",
					url : "${pageContext.request.contextPath}/company/updateApprovalStatus.do",
					data : {
						"method" : "updateApprovalStatus",
						"codes" : JSON.stringify(getSelectedSell),
						"status" : status
					},
					dataType : "json",
					success : function(jsonObj) {
						showApprovalStatusList();
					}
				});
	}
	
	// 회사삭제
	function eliminationWorkplace(getSelectedSell) {
		$.ajax({
					type : "POST",
					url : "${pageContext.request.contextPath}/company/updateApprovalStatus.do",
					data : {
						"method" : "eliminationWorkplace",
						"codes" : JSON.stringify(getSelectedSell)
					},
					dataType : "json",
					success : function(jsonObj) {
						showApprovalStatusList();
					}
				});
		}
</script>
</head>
<body>
	<div class="col-12 card">
		<div class="d-flex justify-content-between align-items-center">
			<h4 class="col-sm-6 mt-5">
				<font style="vertical-align: inherit;"></font>
			</h4>
			<select class="custome-select border-0 pr-3">
				<option selected=""><font style="vertical-align: inherit;"><font
						style="vertical-align: inherit;">정렬</font></font></option>
				<option value="0"><font style="vertical-align: inherit;"><font
						style="vertical-align: inherit;">오름차순</font></font></option>
				<option value="0"><font style="vertical-align: inherit;"><font
						style="vertical-align: inherit;">내림차순</font></font></option>
			</select>
		</div>
		<div class="card-body">
			<div class="row">
				<div class="col-sm-6 ml-0">

					<button type="button" id="Approval"
						class="form-control form-control-sm btn btn-flat btn-outline-dark mb-3"
						style="width: 90px;">승인</button>
				</div>
				<div class="col-sm-6 ml-6" align="right">
					<button type="button" id="refresh"
						class="form-control form-control-sm btn btn-flat btn-outline-dark mb-3"
						style="width: 90px;">새로고침</button>
					<button type="button" id="AddWorkplace"
						class="form-control form-control-sm btn btn-flat btn-outline-dark mb-3"
						style="width: 90px;">회사등록</button>
					<button type="button" id="deleteWorkplace"
						class="form-control form-control-sm btn btn-flat btn-outline-dark mb-3"
						style="width: 90px;">회사삭제</button>
				</div>
			</div>
			<table id="accountUnregisteredGrid"></table>
			<div id="UnregisteredGridPager"></div>

		</div>
	</div>

	<div class="col-12 card">
		<div class="d-flex justify-content-between align-items-center">
			<h4 class="col-sm-6 ml-3">
				<font style="vertical-align: inherit;"></font>
			</h4>
			<select class="custome-select border-0 pr-3">
				<option selected=""><font style="vertical-align: inherit;"><font
						style="vertical-align: inherit;">정렬</font></font></option>
				<option value="0"><font style="vertical-align: inherit;"><font
						style="vertical-align: inherit;">오름차순</font></font></option>
				<option value="0"><font style="vertical-align: inherit;"><font
						style="vertical-align: inherit;">내림차순</font></font></option>
			</select>
		</div>

		<div class="card-body">
			<div class="row">
				<div class="col-sm-6 ml-0">
					<button type="button" id="Release"
						class="form-control form-control-sm btn btn-flat btn-outline-dark mb-3"
						style="width: 90px;">해제</button>
				</div>
			</div>
			<table id="accountRegisteredGrid"></table>
			<div id="registeredGridPager"></div>

		</div>
	</div>


</body>
</html>