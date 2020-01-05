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
    <title>손익계산서</title>
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
        var incomeStatementGrid = "#incomeStatementGrid";
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

            createIncomeStatementGrid();
        });

        function createIncomeStatementGrid() {
            $(incomeStatementGrid).jqGrid({
                cmTemplate: {
                    sortable: false,
                    resizable: false
                },
                colModel: [
                    {name: "accountName", label: "과목", width: 200},
                    {name: "income", label: "세부금액", width: 200, formatter: "currency", formatoptions: currencyFormat},
                    {
                        name: "incomeSummary",
                        label: "합계금액",
                        width: 200,
                        formatter: "currency",
                        formatoptions: currencyFormat
                    },
                    {
                        name: "earlyIncome",
                        label: "세부금액",
                        width: 200,
                        formatter: "currency",
                        formatoptions: currencyFormat
                    },
                    {
                        name: "earlyIncomeSummary",
                        label: "합계금액",
                        width: 200,
                        formatter: "currency",
                        formatoptions: currencyFormat
                    }
                ],
                shrinkToFit: false,
                viewrecords: true,
                autowidth: true,
                rowNum: 9999,
                height: 562,
                datatype: 'local'
            }).jqGrid("setGroupHeaders", {
                useColSpanStyle: true,
                groupHeaders: [
                    {numberOfColumns: 2, titleText: "당기", startColumnName: "income"},
                    {numberOfColumns: 2, titleText: "전기", startColumnName: "earlyIncome"}]
            });
        }

        function showIncomeStatementGrid() {
            $(incomeStatementGrid).jqGrid("clearGridData");

            // show loading message
            $(incomeStatementGrid)[0].grid.beginReq();
            $.ajax({
                type: "GET",
                url: "${pageContext.request.contextPath}/statement/incomeStatement.do",
                data: {
                    "toDate": $("#toDate").val()
                },
                dataType: "json",
                success: function (jsonObj) {
                    // set the new data
                    $(incomeStatementGrid).jqGrid('setGridParam', {data: jsonObj.incomeStatement});
                    // hide the show message
                    $(incomeStatementGrid)[0].grid.endReq();
                    // refresh the grid
                    $(incomeStatementGrid).trigger('reloadGrid');
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
                                onclick="showIncomeStatementGrid();">
                            조회
                        </button>
                    </div>
                </div>
                <table id="incomeStatementGrid"></table>
            </div>
        </div>
    </div>

</body>
</html>
