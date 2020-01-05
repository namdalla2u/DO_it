<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>현금출납장</title>
    <%-- jqGrid --%>
    <script type="text/ecmascript" src="${pageContext.request.contextPath}/assets/js/i18n/grid.locale-kr.js"></script>
    <script type="text/ecmascript" src="${pageContext.request.contextPath}/assets/js/jquery.jqGrid.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/ui.jqgrid-bootstrap4.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/octicons/4.4.0/font/octicons.css">

    <script>
        $.jgrid.defaults.responsive = true;
        $.jgrid.defaults.styleUI = 'Bootstrap4';
        $.jgrid.defaults.iconSet = "Octicons";
    </script>

    <%--DatePicker--%>
    <link href="${pageContext.request.contextPath}/assets/css/datepicker.min.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/assets/js/datepicker.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/datepicker.ko-KR.js"></script>

    <script>
        var cashJournalGrid = "#cashJournalGrid";
        var rowsToColor = [];
        var currencyFormat = {
            defaultValue: '',
            decimalSeparator: '.',
            decimalPlaces: 0,
            thousandsSeparator: ',',
            prefix: '￦ '
        };
        
        var date = new Date();
        var year = date.getFullYear().toString();
        var month = (date.getMonth() + 1 > 9 ? date.getMonth() : '0' + (date.getMonth() + 1)).toString();
        var day = date.getDate() > 9 ? date.getDate() : '0' + date.getDate();
        var today = year + "-" + month + "-" + day;
 
        $(document).ready(function () {
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

            createCashJournalGrid();
        });

        
        function createCashJournalGrid() {
            $(cashJournalGrid).jqGrid({
                cmTemplate: {
                    sortable: false,
                    resizable: false
                },
                colModel: [
                	{      
                        name: "monthReportingDate",
                        label: "",
                        width: 70,
                        formatter: "currency",
                        formatoptions: currencyFormat,
                        hidden : true
                    },
                    {       
                        name: "reportingDate",
                        label: "일자",
                        width: 50
                    },
                    {name: "expenseReport", label: "적요", width: 110},
                    {name: "customerCode", label: "거래처 코드", width: 50},
                    {name: "customerName", label: "거래처명", width: 70},
                    {
                        name: "deposit",
                        label: "입금",
                        width: 70,
                        formatter: "currency",
                        formatoptions: currencyFormat
                    },
                    {
                        name: "withdrawal",
                        label: "출금",
                        width: 70,
                        formatter: "currency",
                        formatoptions: currencyFormat
                    },
                    {
                        name: "balance",
                        label: "잔액",
                        width: 70,
                        formatter: "currency",
                        formatoptions: currencyFormat
                    } 
                ],
                shrinkToFit: true,
                viewrecords: true,
                autowidth: true,
                rowNum: 9999,
                height: 562,
                datatype: 'local',
                gridComplete: function () {
                    var rows = $(cashJournalGrid).getDataIDs(); // 그리드 완성 후 데이터의 ID 들을 받음

                    for (var i = 0; i < rows.length; i++) { 
                        var reportingDate = $(cashJournalGrid).jqGrid("getCell", rows[i], "reportingDate"); // 각 ID의 reportingDate 를 변수에 담음

                        if (reportingDate == '[전 일 이 월]' || reportingDate == '[월 계]') // 배경색상
                            $(cashJournalGrid).jqGrid('setRowData', rows[i], false, {background: '#EEE'});
                        else if (reportingDate == '[전 체 누 계]')
                            $(cashJournalGrid).jqGrid('setRowData', rows[i], false, {background: '#CCC'});
                    }
                }
            });
        }

        function showCashJournalGrid() {
            $(cashJournalGrid).jqGrid("clearGridData");

            // show loading message
            $(cashJournalGrid)[0].grid.beginReq();
            $.ajax({
                type: "GET",
                url: "${pageContext.request.contextPath}/statement/cashJournal.do",
                data: {
                    "fromDate": $('#fromDate').val(),
                    "toDate": $('#toDate').val()
                },
                dataType: "json",
                success: function (jsonObj) {
                    // set the new data
                    $(cashJournalGrid).jqGrid('setGridParam', {data: jsonObj.cashJournalList});
                    // hide the show message
                    $(cashJournalGrid)[0].grid.endReq();
                    // refresh the grid 
                    $(cashJournalGrid).trigger('reloadGrid');
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
							onclick="showCashJournalGrid();">조회</button>
					</div>
				</div>
				<table id="cashJournalGrid"></table>
			</div>
		</div>
	</div>
</body>
</html>
