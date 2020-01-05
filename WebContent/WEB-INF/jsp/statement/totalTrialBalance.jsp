<%--
  Created by IntelliJ IDEA.
  User: plang
  Date: 2019-01-31
  Time: 오후 2:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>합계잔액시산표</title>
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
        var totalTrialBalanceGrid = "#totalTrialBalanceGrid";
        var rowsToColor = [];
        var currencyFormat = {
            defaultValue: '',
            decimalSeparator: '.',
            decimalPlaces: 0,
            thousandsSeparator: ',',
            prefix: '￦ '
        };
 
        $(document).ready(function () {
        	
            $('[data-toggle="datepicker-to"]').datepicker({
                language: 'ko-KR',
                format: 'yyyy-mm-dd',
                autoPick: true,
                trigger: $('.docs-datepicker-to-trigger')
            });

            createTotalTrialBalanceGrid();
        });

        
        function createTotalTrialBalanceGrid() {
            $(totalTrialBalanceGrid).jqGrid({
                cmTemplate: {
                    sortable: false,
                    resizable: false
                },
                colModel: [
                    {
                        name: "debitsSumBalance",
                        label: "잔액",
                        width: 200,
                        formatter: "currency",
                        formatoptions: currencyFormat
                    },
                    {name: "debitsSum", label: "합계", width: 200, formatter: "currency", formatoptions: currencyFormat},
                    {name: "accountName", label: "과목", width: 200},
                    {name: "creditsSum", label: "합계", width: 200, formatter: "currency", formatoptions: currencyFormat},
                    {
                        name: "creditsSumBalance",
                        label: "잔액",
                        width: 200,
                        formatter: "currency",
                        formatoptions: currencyFormat
                    }
                ],
                shrinkToFit: true,
                viewrecords: true,
                autowidth: true,
                rowNum: 9999,
                height: 562,
                datatype: 'local'
            }).jqGrid("setGroupHeaders", {
                useColSpanStyle: true,
                groupHeaders: [
                    {"numberOfColumns": 2, "titleText": "차변", "startColumnName": "debitsSumBalance"},
                    {"numberOfColumns": 2, "titleText": "대변", "startColumnName": "creditsSum"}]
            });
        }

        function showTotalTrialBalanceGrid() {
            $(totalTrialBalanceGrid).jqGrid("clearGridData");

            // show loading message
            $(totalTrialBalanceGrid)[0].grid.beginReq();
            $.ajax({
                type: "GET",
                url: "${pageContext.request.contextPath}/statement/totalTrialBalance.do",
                data: {
                	
                    toDate: $("#toDate").val()
                },
                dataType: "json",
                success: function (jsonObj) {
                    // set the new data
                    $(totalTrialBalanceGrid).jqGrid('setGridParam', {data: jsonObj.totalTrialBalance});
                    // hide the show message
                    $(totalTrialBalanceGrid)[0].grid.endReq();
                    // refresh the grid
                    
                    $(totalTrialBalanceGrid).trigger('reloadGrid');
					
                   //compareDebtorCredits();
                }
            });
        }
        
        function compareDebtorCredits() {
            var debtorSum = $(journalGrid).jqGrid("getCol", "debitsSum", false, "sum");
            var creditsSum = $(journalGrid).jqGrid("getCol", "creditsSum", false, "sum");

            if(debtorSum != creditsSum){
            	alert("차변 합계와 대변액이 일치하지 않습니다. \n 차변 : "+debtorSum+"원  대변 : "+creditsSum+"원");
            }
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
                            <input id="toDate" type="date" class="form-control form-control-sm docs-date"
                                   data-toggle="datepicker-to" required>
                            <div class="input-group-append">
                                <button type="button"
                                        class="form-control form-control-sm btn btn-outline-secondary docs-datepicker-to-trigger">
                                    <i style="color:#FD7D86;" class="fa fa-calendar" aria-hidden="true"></i>
                                </button>
                            </div>
                        </div>
                    </div>

                    <div>
                        <button type="button" id="search"
                                class="form-control form-control-sm btn btn-flat btn-outline-dark mb-3"
                                onclick="showTotalTrialBalanceGrid();">
                            조회
                        </button>
                    </div>
                </div>
                <table id="totalTrialBalanceGrid"></table>
            </div>
        </div>
    </div>

</body>
</html>
