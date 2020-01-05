<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
input.error, text.error {
	border: 2px solid #FD7D86;
}

label.error {
	color: #FD7D86;
	font-weight: 400;
	font-size: 0.75em;
	margin-top: 7px;
	margin-left: 6px;
	margin-right: 6px;
}
</style>

<title>회사 등록</title>

<%-- jqGrid --%>
<script type="text/ecmascript"
	src="${pageContext.request.contextPath}/assets/js/i18n/grid.locale-kr.js"></script>
<script type="text/ecmascript"
	src="${pageContext.request.contextPath}/assets/js/jquery.jqGrid.min.js"></script>
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

<!-- validate -->

<script type="text/javascript"
	src="${pageContext.request.contextPath}/assets/plugins/jquery.validate.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/assets/plugins/functionValidate.js"></script>

<%--  <script type="text/javascript" src="${pageContext.request.contextPath}/assets/plugins/additional-methods.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/plugins/messages_ko.min.js"></script> --%>





<script>
	var searchWorkplaceCode = "#searchWorkplaceCode"; //사업장코드 조회
	var searchBusiness = "#searchBusiness"; //업태대분류 조회 버튼 	
	var AddWorkplace = "#AddWorkplace"; //사업장 추가

	var parentBusinessGrid = "#parentBusinessGrid" // 업태그리드
	var detailBusinessGrid = "#detailBusinessGrid" //업태소분류그리드

	var workplaceCode = "#workplaceCode"; //사업장조회 인풋 아이디	
	var codeCheck; //코드조회 변수
	var emptyWorkplaceBean;

	var status = "";
	$(document).ready(function() {

		$(AddWorkplace).click(validate); //사업장추가 
		$("#valss input").focus(validate);

		createParentBusinessGrid(); //업태그리드
		createDetailBusinessGrid(); //업태소분류그리드getWorkplaceCode

		//버튼이벤트
		$(searchBusiness).click(function() { //업태 대분류 검색 버튼
			$(detailBusinessGrid).jqGrid("clearGridData");
			showParentBusiness();
		});

		$("#addCheck").click(function() { //사용하기 버튼 이벤트
			$(workplaceCode).val(codeCheck);
			$("#companyCodeModal").modal("hide");
			$("#seccondSearch").val("");
		});

	});

	function getWorkplaceCode(code) {
		if (code.type == "click") { // onclick 이벤트이므로 type == click
			codeCheck = $(workplaceCode).val();
			status = "first";
		}

		if (code.type == "button") {
			codeCheck = $("#seccondSearch").val();
			status = "seccond";
		} 

		$("#seccondModal").attr("hidden", false);
		$("#addCheck").attr("type", "button");

		$.ajax({
					type : "POST",
					url : "${pageContext.request.contextPath}/company/workplace.do",
					data : {
						method : "getWorkplace",
						workplaceCode : codeCheck
					},
					dataType : "json",
					success : function(jsonObj) {
						var workplaceCode = jsonObj.workplaceBean;

						var getChecking = workplaceCode != null ? "사업장 중복 있습니다 , 다시 조회하세요."
								: codeCheck.length != 4 || isNaN(codeCheck) ? "사업장 코드는 숫자로만 구성된 4자리로 입력 부탁드립니다"
										: "사업장 등록이 가능합니다";
						// JSON 결과가 NULL이 아닐경우 중복 / 숫자 4자리가 아닐 경우 경고 / 모두다 아니면 등록 가능
						$("#codeCheck").html(getChecking); // modal 창 하단에 결과메세지 띄움
						
						if (workplaceCode != null || codeCheck.length != 4
								|| isNaN(codeCheck)) {
							$("#addCheck").attr("type", "hidden"); // 조건이 맞지 않을 경우 버튼을 숨긴다
							emptyWorkplaceBean = null;

						} else if (workplaceCode == null
								|| codeCheck.length == 4 || isNaN(codeCheck)) {
							$("#seccondModal").attr("hidden", true);       // 코드 입력 텍스트창 숨김
							emptyWorkplaceBean = jsonObj.emptyWorkplaceBean; 
							if (status == "first")
								$("#codeCheck").html(
										"사업장코드" + codeCheck + "    "
												+ getChecking);

							else if (status == "seccond")
								$("#codeCheck").html(
										"사업장코드" + codeCheck + "    "
												+ getChecking);
						}   // 사업장코드 0000 사업장 등록이 가능합니다.

					}
				});
	}

	//사업장추가
	function workplaceAdd() {
		var workplaceAddItems = emptyWorkplaceBean;

		for ( var index in workplaceAddItems) {
			if (index == "workplaceCode")
				workplaceAddItems[index] = "BRC-" + $("#" + index + "").val(); // code에 BEC- 추가
			else
				workplaceAddItems[index] = $("#" + index + "").val();
		}

		$.ajax({
					type : "POST",
					url : "${pageContext.request.contextPath}/company/workplace.do",
					data : {
						method : "workPlaceAdd",
						workplaceAddItems : JSON.stringify(workplaceAddItems),
					},
					dataType : "json",
					success : function(jsonObj) {
						if (jsonObj.errorCode == 0) {
							alert("회사등록이 성공적으로 완료되었습니다");
							location.href = "${pageContext.request.contextPath}/hello.html"
						}
						if (jsonObj.errorCode < 0)
							alert("회사등록에 실패 되었습니다 사업장조회는 필수입니다.");
					}
				});
	}

	function createParentBusinessGrid() { //업태 그리드

		$(parentBusinessGrid).jqGrid({
			colModel : [ {
				name : "businessName",
				label : "업태대분류"
			}, {
				name : "classificationCode",
				hidden : true,
				label : "업태코드",
				key : true
			} ],
			viewrecords : true,
			width : 200,
			height : 300,
			rowNum : 9999,
			datatype : "local",
			onSelectRow : function(key) {
				showDetailBusinessGrid(key)
			}
		});
	}

	function showParentBusiness() { //업태리스트
		
		// show loading message
		$(parentBusinessGrid)[0].grid.beginReq();
		$.ajax({
			type : "POST",
			url : "${pageContext.request.contextPath}/company/business.do",
			data : {
				"method" : "getBusinessList",
			},
			dataType : "json",
			success : function(jsonObj) {
				// set the new data
				$(parentBusinessGrid).jqGrid("setGridParam", {
					data : jsonObj.businessList
				});
				// hide the show message
				$(parentBusinessGrid)[0].grid.endReq();
				// refresh the grid
				$(parentBusinessGrid).trigger("reloadGrid");

			}
		});

		$(parentBusinessGrid)[0].grid.endReq();
	}

	function createDetailBusinessGrid() { //업태소분류

		$(detailBusinessGrid).jqGrid({
			colModel : [ {
				name : "detailBusinessName",
				label : "업태소분류",
				key : true
			}

			],
			viewrecords : true,
			width : 200,
			height : 300,
			rowNum : 9999,
			datatype : "local",
			onSelectRow : function(key) {
				$("#businessConditions").val(key);
				$("#businessModal").modal("hide");
			}
		});
	}

	function showDetailBusinessGrid(businessCode) { //업태소분류리스트
		// show loading message     
		$(detailBusinessGrid).jqGrid('clearGridData');

		$.ajax({
			type : "GET",
			url : "${pageContext.request.contextPath}/company/business.do",
			data : {
				"method" : "getDetailBusiness",
				"businessCode" : businessCode
			},
			dataType : "json",
			success : function(jsonObj) {
				$(detailBusinessGrid).jqGrid("setGridParam", {
					data : jsonObj.detailBusinessList
				});
				$(detailBusinessGrid).trigger("reloadGrid");
			}
		});
	}
</script>

</head>
<body>
	<div class="col-12 card">
		<div class="card-body">
			<form id="valss">
				<div id="workplaceAddItems">
					<label for="example-text-input" class="col-form-label">사업장코드</label>
					<span class="ti-pencil"></span><br />
					<div class="input-group">
						<input class="form-control-sm" type="text" id="workplaceCode"
							name="workplaceCode" placeholder="workplaceCode">

						<!-- 사업장코드확인 -->
						<div class="input-group-append">
							<button type="button"
								class="form-control form-control-sm mt-0 btn btn-outline-secondary"
								style="width: 40px;" data-toggle="modal"
								data-target="#companyCodeModal" id="searchWorkplaceCode">
								<i style="color: #FD7D86;" class="fa fa-search"></i>
							</button>
						</div>
					</div>

					<div class="mt-2">
						<label for="example-text-input" class="col-form-label">회사코드</label><span
							class="ti-pencil"></span><br />
						<div class="input-group">
							<input class="form-control-sm" type="text" id="companyCode"
								name="companyCode" placeholder="companyCode">
						</div>
					</div>
					<div class="mt-2">
					<label for="example-text-input" class="col-form-label">사업장명</label><span
						class="ti-pencil"></span><br />
					<div class="input-group ">
						<input class="form-control-sm" type="text" id="workplaceName"
							name="workplaceName" placeholder="workplaceName">
					</div>
					</div>
					<div class="mt-2">
						<label for="example-text-input" class="col-form-label">사업자등록번호</label><span
							class="ti-pencil"></span><br />
						<div class="input-group ">
							<input class="form-control-sm" type="text" id="businessLicense"
								name="businessLicense" placeholder="businessLicense">
						</div>
					</div>

					<div class="mt-2">
						<label for="example-text-input" class="col-form-label">법인등록번호</label><span
							class="ti-pencil"></span><br />
						<div class="input-group ">
							<input class="form-control-sm" type="text"
								id="corporationLicence" name="corporationLicence"
								placeholder="corporationLicence">
						</div>
					</div>

					<div class="mt-2">
						<label for="example-text-input" class="col-form-label">대표자명</label><span
							class="ti-pencil"></span><br />
						<div class="input-group ">
							<input class="form-control-sm" type="text" id="workplaceCeoName"
								name="workplaceCeoName" placeholder="ceoName">
						</div>
					</div>

					<div class="mt-2">
						<label for="example-text-input" class="col-form-label">업태</label><span
							class="ti-pencil"></span><br />
						<div class="input-group">
							<input class="form-control-sm" type="text"
								id="businessConditions" name="businessConditions"
								placeholder="businessConditions" disabled="disabled">


							<div class="input-group-append">
								<button type="button"
									class="form-control form-control-sm mt-0 btn btn-outline-secondary"
									style="width: 40px;" data-toggle="modal"
									data-target="#businessModal" id="searchBusiness">
									<i style="color: #FD7D86;" class="fa fa-search"></i>
								</button>
							</div>
						</div>
					</div>

					<div class="mt-2">
						<label for="example-text-input" class="col-form-label">종목</label><span
							class="ti-pencil"></span><br />
						<div class="input-group ">
							<input class="form-control-sm" type="text" id="businessItems"
								name="businessItems" placeholder="businessItems">
						</div>
					</div>

					<div class="mt-2">
						<label for="example-text-input" class="col-form-label">사업장전화번호</label><span
							class="ti-pencil"></span><br />
						<div class="input-group ">
							<input class="form-control-sm" type="text"
								id="workplaceTelNumber" name="workplaceTelNumber"
								placeholder="workplaceTelNumber">
						</div>
					</div>
					<div class="mt-2">
						<label for="example-text-input" class="col-form-label">사업장팩스번호</label><span
							class="ti-pencil"></span><br />
						<div class="input-group "></div>
						<input class="form-control-sm" type="text" id="workplaceFaxNumber"
							name="workplaceFaxNumber" placeholder="workplaceFaxNumber">
					</div>
					<div class="mt-2">
						<label for="example-text-input" class="col-form-label">사업장주소</label><span
							class="ti-pencil"></span><br />
						<div class="input-group ">
							<input class="form-control-sm" type="text"
								id="workplaceBasicAddress" name="workplaceBasicAddress"
								placeholder="workplaceAdress">
						</div>
					</div>

					<div class="input-group" hidden="true">
						<label for="example-text-input" class="col-form-label">거래처등록유무</label><br />
						<input class="form-control-sm" type="text" id="approvalStatus"
							value="미등록" placeholder="approvalStatus" disabled="disabled">
					</div>
				</div>

				<div class="input-group mt-4">
					<input class="btn btn-secondary mr-1" type="submit"
						id="AddWorkplace" value="등록"> <input
						class="btn btn-secondary" type="reset" value="취소">
				</div>
			</form>

		</div>
	</div>

	<!-- 모달시작 -->
	<div class="modal fade" id="businessModal" aria-hidden="true"
		style="display: none;">
		<div class="modal-dialog">
			<!-- 모달 다이얼로그 -->
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">업태</h5>
					<button type="button" class="close" data-dismiss="modal">
						<span>x</span>
					</button>
				</div>
				<div class="modal-body">
					<div class="input-group mb-3"></div>

					<div class="row">
						<div class="col-6">
							<table id="parentBusinessGrid"></table>
						</div>
						<div class="col-6">
							<table id="detailBusinessGrid"></table>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 모달 다이얼로그 끝 -->
	</div>


	<div class="modal fade" id="companyCodeModal" aria-hidden="true"
		style="display: none;">
		<div class="modal-dialog">
			<!-- 모달 다이얼로그 -->
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">사업장코드확인</h5>
					<button type="button" class="close" data-dismiss="modal">
						<span>x</span>
					</button>
				</div>
				<div class="modal-body">
					<div class="input-group mb-3" id="seccondModal">
						<!-- 검색입력란 -->
						<input type="text" id="seccondSearch"
							class="form-control form-control-sm col-md-6" maxlength="4">
						<div class="input-group-append">
							<button type="button"
								class="form-control form-control-sm btn btn-outline-secondary"
								style="width: 80px;" onclick="getWorkplaceCode(this)">
								<i style="color: #FD7D86;" class="fa fa-search"
									aria-hidden="true"></i>
							</button>
						</div>
					</div>
					<div id="codeCheck">
						<small class="form-text text-muted"></small>
					</div>
					<input class="btn btn-secondary mt-3" type="button" id="addCheck"
						value="사용하기">
					<table id="companyCodeGrid"></table>
				</div>

			</div>
		</div>
		<!-- 모달 다이얼로그 끝 -->
	</div>
</body>
</html>
