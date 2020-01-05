<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>분개장</title>

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
        var journalGrid = "#journalGrid";

        var currencyFormat = {
            defaultValue: "",
            decimalSeparator: ".",
            decimalPlaces: 0,
            thousandsSeparator: ",",
            prefix: "￦ "
        };

        $(document).ready(function () {
            var date = new Date();
            var year = date.getFullYear().toString();
            var month = (date.getMonth() + 1 > 9 ? date.getMonth() : '0' + (date.getMonth() + 1)).toString();
            var day = date.getDate() > 9 ? date.getDate() : '0' + date.getDate();
            var today = year + "-" + month + "-" + day;

            $('#from').val(today.substring(0, 8) + '01');
            $('#to').val(today.substring(0, 10));

            $('[data-toggle="datepicker-to"]').datepicker({
                language: 'ko-KR',
                format: 'yyyy-mm-dd',
                trigger: $('.docs-datepicker-to-trigger')
            });
            $('[data-toggle="datepicker-from"]').datepicker({
                language: 'ko-KR',
                format: 'yyyy-mm-dd',
                trigger: $('.docs-datepicker-from-trigger')
            });

            createJournal();
            createJournalDetail()
        });

        function createJournal() {
            $(journalGrid).jqGrid({
                colModel: [
                    {name: "slipNo", label: "전표번호", width: 200},
                    {name: "journalNo", label: "분개번호", width: 280, key: true},
                    {name: "balanceDivision", label: "구분", width: 100},
                    {name: "accountName", label: "계정과목", width: 150},
                    {
                        name: "leftDebtorPrice",
                        label: "차변",
                        sorttype: "number",
                        width: 200,
                        formatter: "currency",
                        formatoptions: currencyFormat
                    },
                    {
                        name: "rightCreditsPrice",
                        label: "대변",
                        sorttype: "number",
                        width: 200,
                        formatter: "currency",
                        formatoptions: currencyFormat
                    },
                    {
                        name: "journalDetail", label: "분개상세", width: 150, // 데이터가 아닌 a태그로 분개 상세보기 modal 창으로 띄운다
                        formatter: function (cellValue, options) {
                            var buttonHtml = "<a href=\"javascript:void(0);\" onclick=\"showJournalDetail('" + options.rowId + "')\">상세 보기</a>";
                            return buttonHtml; // javascript:void(0); 는 # 과 비슷한 기능
                        }
                    },
                    {name: "customerName", label: "거래처명", width: 200}
                ],
                shrinkToFit: false,
                viewrecords: true,
                autowidth: true,
                height: 562,
                rowNum: 20,
                datatype: 'local',
                pager: "#journalGridPager"
            });
        }

        function showJournal() {
            $(journalGrid).jqGrid("clearGridData");
            // show loading message
            $(journalGrid)[0].grid.beginReq();
            $.ajax({
                type: "GET",
                url: "${pageContext.request.contextPath}/account/journal.do",
                data: {
                    "method": "findRangedJournalList",
                    "from": $("#from").val(),
                    "to": $("#to").val()
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

        function createJournalDetail() {
            $(journalDetailGrid).jqGrid(
                {
                    colModel: [
                        {name: "accountControlName", label: "항목", width: 150},
                        {name: "journalDescription", label: "항목내용", width: 250}
                    ],
                    shrinkToFit: false,
                    viewrecords: true,
                    rownumbers: true,
                    width: 465,
                    height: 200,
                    datatype: 'local',
                    ondblClickRow: function (id) {
                        if (id) {
                            $(journalDetailGrid).jqGrid("restoreRow", lastSelectedJournalDetail);
                            $(journalDetailGrid).jqGrid("editRow", id, true, null, null
                                , "${pageContext.request.contextPath}/account/journalDetail.do", {
                                    method: "editJournalDetail",
                                    journalDetailNo: id,
                                    journalNo: $(journalGrid).jqGrid("getGridParam", "selrow")
                                });
                            lastSelectedJournalDetail = id;
                        }
                    }
                }
            );
        }

        function showJournalDetail(journalNo) {
            $("div#journalDetailModal").modal();

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
    </script>
</head>
<body>
    <div class="col-12">
        <div class="card">
            <div class="card-body">
                <div class="form-row ml-0">
                    <div class="docs-datepicker mr-1">
                        <div class="input-group">
                            <input id="from" type="date" class="form-control form-control-sm docs-date"
                                   data-toggle="datepicker-from" required>
                            <div class="input-group-append">
                                <button type="button"
                                        class="form-control form-control-sm btn btn-outline-secondary docs-datepicker-from-trigger">
                                    <i style="color:#FD7D86;" class="fa fa-calendar" aria-hidden="true"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="pt-1 pl-1 pr-1">~</div>
                    <div class="docs-datepicker mr-1">
                        <div class="input-group">
                            <input id="to" type="date" class="form-control form-control-sm docs-date"
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
                                onclick="showJournal();">
                            검색
                        </button>
                    </div>
                </div>
                <table id="journalGrid"></table>
                <div id="journalGridPager"></div>
            </div>
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
