<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>일(월)계표</title>
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
	$.jgrid.defaults.responsive = true;
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
	var detailTrialBalanceGrid = "#detailTrialBalanceGrid";
	var currencyFormat = {
		defaultValue : '',
		decimalSeparator : '.',
		decimalPlaces : 0,
		thousandsSeparator : ',',
		prefix : '￦ '
	};
	
    var date = new Date();
    var year = date.getFullYear().toString();
    var month = (date.getMonth() + 1 > 9 ? date.getMonth() : '0' + (date.getMonth() + 1)).toString();
    var day = date.getDate() > 9 ? date.getDate() : '0' + date.getDate();
    var today = year + "-" + month + "-" + day;

	$(document).ready(function() {
		$('[data-toggle="datepicker-from"]').datepicker({
			format : 'yyyy-mm-dd',
			autoPick : true,
			trigger : $('.docs-datepicker-from-trigger'),

		});
		$('[data-toggle="datepicker-to"]').datepicker({
			format : 'yyyy-mm-dd',
			autoPick : true,
			trigger : $('.docs-datepicker-to-trigger'),

		});
		
		 $('#fromDate').val(today.substring(0, 8) + '01');
         $('#toDate').val(today);

		
		createDetailTrialBalanceGrid();
	});

	function createDetailTrialBalanceGrid() {
		$(detailTrialBalanceGrid).jqGrid({
			colModel : [ 
				{name: "lev", label: "계층", hidden: true
			}, {
				name : "debitsSum",
				label : "계",
				width : 155,
				formatter : "currency",
				formatoptions : currencyFormat
			}, {
				name : "exceptCashDebits",
				label : "대체",
				width : 155,
				formatter : "currency",
				formatoptions : currencyFormat
			}, {
				name : "cashDebits",
				label : "현금",
				width : 155,
				formatter : "currency",
				formatoptions : currencyFormat
			}, {
				name : "accountName",
				label : "과목",
				width : 155
			}, {
				name : "cashCredits",
				label : "현금",
				width : 155,
				formatter : "currency",
				formatoptions : currencyFormat
			}, {
				name : "exceptCashCredits",
				label : "대체",
				width : 155,
				formatter : "currency",
				formatoptions : currencyFormat
			}, {
				name : "creditsSum",
				label : "계",
				width : 155,
				formatter : "currency",
				formatoptions : currencyFormat
			} ],
			shrinkToFit : true,
			viewrecords : true,
			autowidth : true,
			rowNum : 9999,
			height : 562,
			datatype : 'local'
		}).jqGrid("setGroupHeaders", {
			useColSpanStyle : true,
			groupHeaders : [ {
				"numberOfColumns" : 3,
				"startColumnName" : "debitsSum",
				"titleText" : "차변"
			}, {
				"numberOfColumns" : 3,
				"startColumnName" : "cashCredits",
				"titleText" : "대변"
			} ]
		});
	}
	
    function showDetailTrialBalanceGrid() {
            $(detailTrialBalanceGrid).jqGrid("clearGridData");

            // show loading message
            $(detailTrialBalanceGrid)[0].grid.beginReq();
            $.ajax({
                type: "GET",
                url: "${pageContext.request.contextPath}/statement/detailTrialBalance.do",
                data: {
                	
                	fromDate:$("#fromDate").val(),
                    toDate:$("#toDate").val()
                    
                },
                dataType: "json",
                success: function (jsonObj) {
                    // set the new data
                    $(detailTrialBalanceGrid).jqGrid('setGridParam', {data: jsonObj.detailTrialBalanceList});
                    // hide the show message
                    $(detailTrialBalanceGrid)[0].grid.endReq();
                    // refresh the grid
                    $(detailTrialBalanceGrid).trigger('reloadGrid');
                }
            });
        }
	
</script>

</head>
<body>
	<div class="col-12">
		<div class="card">
			<div class="card-body">
				<div class="form-row ml-0">
					<div class="docs-datepicker mr-1">
						<div class="input-group">
							<input id="fromDate" type="date"
								class="form-control form-control-sm docs-date"
								data-toggle="datepicker-from" required>

							<div class="input-group-append">
							
								<button type="button"
									class="form-control form-control-sm btn btn-outline-secondary docs-datepicker-from-trigger">
									<i style="color: #FD7D86;" class="fa fa-calendar"
										aria-hidden="true"></i>
								</button>

							</div>
							<input id="toDate" type="date"
								class="form-control form-control-sm docs-date ml-1"
								data-toggle="datepicker-to" required>
							<div class="input-group-append">
							
								<button type="button"
									class="form-control form-control-sm btn btn-outline-secondary docs-datepicker-to-trigger">
									<i style="color: #FD7D86;" class="fa fa-calendar"
										aria-hidden="true"></i>
								</button>
								
							</div>
						</div>
					</div>
					<div>
						<button type="button" id="detailTrialBalance"
							class="form-control form-control-sm btn btn-flat btn-outline-dark mb-3"
							onclick="showDetailTrialBalanceGrid();">조회</button>
					</div>
				</div>
				<table id="detailTrialBalanceGrid"></table>
			</div>
		</div>
	</div>
</body>
</html>