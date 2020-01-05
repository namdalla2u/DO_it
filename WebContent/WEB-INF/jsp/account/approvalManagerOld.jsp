<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>전표승인</title>

    <%-- jqGrid --%>
    <script type="text/ecmascript" src="${pageContext.request.contextPath}/assets/js/i18n/grid.locale-kr.js"></script>
    <script type="text/ecmascript" src="${pageContext.request.contextPath}/assets/js/jquery.jqGrid.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/ui.jqgrid-bootstrap4.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/octicons/4.4.0/font/octicons.css">

    <script>
        $.jgrid.defaults.styleUI = 'Bootstrap4';
        $.jgrid.defaults.iconSet = "Octicons";
    </script>

    <script type="text/javascript">
        var approvalSlipGrid = "table#approvalSlipGrid";
        var journalGrid = "table#journalGrid";

        function createApprovalSlipGrid() {
            $(approvalSlipGrid).jqGrid({
                colModel: [
                    {name: "slipNo", label: "전표번호", width: 250, key: true, sorttype: "text"},
                    {name: "accountPeriodNo", label: "기수"},
                    {name: "deptCode", label: "부서코드"},
                    {name: "deptName", label: "부서", hidden: true},
                    {name: "slipType", label: "구분"},
                    {name: "expenseReport", label: "적요", width: 300},
                    {name: "slipStatus", label: "승인상태"},
                    // {name: "status", width: 30, hidden: true},
                    {name: "reportingEmpCode", label: "작성자코드"},
                    {name: "reportingEmpName", label: "작성자", hidden: true},
                    {name: "reportingDate", label: "작성일"},
                    {name: "positionCode", label: "직급", hidden: true}
                ],
                multiselect: true,
                sortname: "slipNo",
                viewrecords: true,
                autowidth: true,
                shrinkToFit: false,
                responsive: true,
                height: 370,
                rowNum: 8,
                datatype: 'local',
                pager: "#ApprovalSlipGridPager",
                onSelectRow: function (key) {
                    showJournalGrid(key);
                }
            });
        }

        function createJournalGrid() {

            $(journalGrid).jqGrid({
                colModel: [
                    {name: "journalNo", label: "분개번호", width: 220, key: true, hidden: true},
                    {name: "balanceDivision", label: "구분", width: 80},
                    {name: "accountCode", label: "계정코드", width: 100},
                    {name: "accountName", label: "계정과목"},
                    {
                        name: "leftDebtorPrice", label: "차변",
                        formatter: "currency",
                        formatoptions: {
                            defaultValue: '',
                            decimalSeparator: '.',
                            decimalPlaces: 0,
                            thousandsSeparator: ',',
                            prefix: '￦'
                        }
                    },
                    {
                        name: "rightCreditsPrice", label: "대변",
                        formatter: "currency",
                        formatoptions: {
                            defaultValue: '',
                            decimalSeparator: '.',
                            decimalPlaces: 0,
                            thousandsSeparator: ',',
                            prefix: '￦'
                        }
                    },
                    {name: "customerCode", label: "거래처코드", width: 100},
                    {name: "customerName", label: "거래처", width: 150},
                    {
                        name: "journalDetail", label: "분개상세",
                        formatter: function (cellValue, options) {
                            var buttonHtml = "<a href=\"javascript:void(0);\" onclick=\"showJournalDetail('" + options.rowId + "')\">상세 보기</a>";
                            return buttonHtml;
                        }
                    }
                    // {name: "status", width: 50, hidden: true}
                ],
                rownumbers: true,
                viewrecords: true,
                shrinkToFit: false,
                responsive: true,
                autowidth: true,
                height: 200,
                rowNum: 20,
                datatype: 'local'
            });
        }

        function showApprovalSlipGrid() {
            $(approvalSlipGrid).jqGrid("clearGridData");
            $(journalGrid).jqGrid("clearGridData");

            // show loading message
            $(approvalSlipGrid)[0].grid.beginReq();
            $.ajax({
                type: "GET",
                url: "${pageContext.request.contextPath}/account/slip.do",
                data: {
                    "method": "findDisApprovalSlipList"
                },
                dataType: "json",
                success: function (jsonObj) {
                    // set the new data
                    $(approvalSlipGrid).jqGrid('setGridParam', {data: jsonObj.disApprovalSlipList});
                    // hide the show message
                    $(approvalSlipGrid)[0].grid.endReq();
                    // refresh the grid
                    $(approvalSlipGrid).trigger('reloadGrid');
                }
            });

            $(approvalSlipGrid)[0].grid.endReq();
        }

        function showJournalGrid(slipNo) {

            $(journalGrid).jqGrid("clearGridData");

            // show loading message
            $(journalGrid)[0].grid.beginReq();
            $.ajax({
                type: "GET",
                url: "${pageContext.request.contextPath}/account/journal.do",
                data: {
                    "method": "findSingleJournalList",
                    "slipNo": slipNo
                },
                dataType: "json",
                success: function (jsonObj) {
                    // set the new data
                    $(journalGrid).jqGrid('setGridParam', {data: jsonObj.journalList});
                    // hide the show message
                    $(journalGrid)[0].grid.endReq();
                    // refresh the grid
                    $(journalGrid).trigger('reloadGrid');
                }
            });

            $(journalGrid)[0].grid.endReq();
        }


        function createJournalDetailGrid() {
            $(journalDetailGrid).jqGrid(
                {
                    colModel: [
                        {name: "accountControlName", label: "항목", width: 100},
                        {name: "journalDescription", label: "항목내용"}
                    ],
                    shrinkToFit: false,
                    viewrecords: true,
                    rownumbers: true,
                    width: 465,
                    height: 200,
                    datatype: 'local'
                }
            );
        }

        function showJournalDetail(key) {
            $("div#journalDetailModal").modal();
            showJournalDetailGrid(key);
        }

        function showJournalDetailGrid(journalNo) {
            $(journalDetailGrid).jqGrid("clearGridData");

            // show loading message
            $(journalDetailGrid)[0].grid.beginReq();
            $.ajax({
                type: "GET",
                url: "${pageContext.request.contextPath}/account/journalDetail.do",
                data: {
                    "method": "getJournalDetailList",
                    "journalNo": journalNo
                },
                dataType: "json",
                success: function (jsonObj) {
                    // set the new data
                    $(journalDetailGrid).jqGrid('setGridParam', {data: jsonObj.journalDetailList});
                    // hide the show message
                    $(journalDetailGrid)[0].grid.endReq();
                    // refresh the grid
                    $(journalDetailGrid).trigger('reloadGrid');
                }
            });

            $(journalDetailGrid)[0].grid.endReq();
        }

        function approveSelectedSlip(isApprove) {
            var selectedRows = $(approvalSlipGrid).jqGrid("getGridParam", "selarrrow");

            $.ajax({
                type: "GET",
                url: "${pageContext.request.contextPath}/account/slip.do",
                data: {
                    "method": "approveSlip",
                    "approveSlipList": JSON.stringify(selectedRows),
                    "isApprove": isApprove
                },
                dataType: "json",
                success: function (jsonObj) {
                    showApprovalSlipGrid();
                }
            });
        }

        $(document).ready(function () {
            createApprovalSlipGrid();
            showApprovalSlipGrid();
            createJournalGrid();
            createJournalDetailGrid();

            $("button#refresh").click(showApprovalSlipGrid);
            $("button#approval").click(function () {
                approveSelectedSlip(true);
            });
            $("button#disapproval").click(function () {
                approveSelectedSlip(false);
            });
        });
    </script>
</head>
<body>
    <div class="col-12 card">
        <div class="card-body">
            <div class="row">
                <div class="col-sm-6 ml-0">
                    <button type="button" id="approval"
                            class="form-control form-control-sm btn btn-flat btn-outline-dark mb-3"
                            style="width:90px;">
                        승인
                    </button>
                    <button type="button" id="disapproval"
                            class="form-control form-control-sm btn btn-flat btn-outline-dark mb-3"
                            style="width:90px;">
                        반려
                    </button>
                </div>
                <div class="col-sm-6 mr-0 text-right">
                    <button type="button" id="refresh"
                            class="form-control form-control-sm btn btn-flat btn-outline-dark mb-3"
                            style="width:90px">
                        새로고침
                    </button>
                </div>
            </div>
            <table id="approvalSlipGrid"></table>
            <div id="approvalSlipGridPager"></div>
        </div>
    </div>

    <div class="mt-3 col-12 card">
        <div class="card-body">
            <table id="journalGrid"></table>
        </div>
    </div>

    <div class="modal fade" id="journalDetailModal" aria-hidden="true" style="display: none;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">분개 상세</h5>
                    <button type="button" class="close" data-dismiss="modal"><span>×</span></button>
                </div>
                <div class="modal-body">
                    <table id="journalDetailGrid"></table>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
