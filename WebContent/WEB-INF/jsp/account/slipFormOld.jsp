<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>전표입력</title>

    <%-- jqGrid --%>
    <script type="text/ecmascript" src="${pageContext.request.contextPath}/assets/js/i18n/grid.locale-kr.js"></script>
    <script type="text/ecmascript" src="${pageContext.request.contextPath}/assets/js/jquery.jqGrid.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/ui.jqgrid-bootstrap4.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/octicons/4.4.0/font/octicons.css">

    <script>
        $.jgrid.defaults.styleUI = 'Bootstrap4';
        $.jgrid.defaults.iconSet = "Octicons";
    </script>

    <%--DatePicker--%>
    <link href="${pageContext.request.contextPath}/assets/css/datepicker.min.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/assets/js/datepicker.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/datepicker.ko-KR.js"></script>

    <script>
        var NEW_SLIP_NO = "NEW";
        var NEW_JOURNAL_PREFIX = NEW_SLIP_NO + "JOURNAL";
        var REQUIRE_ACCEPT_SLIP = "미승인";

        // 그리드의 선택자
        var slipGrid = "#slipGrid";
        var journalGrid = "#journalGrid";
        var journalDetailGrid = "#journalDetailGrid";
        var accountGrid = "#accountGrid";
        var codeGrid = "#codeGrid";

        // 로그인 정보
        var deptCode = "${sessionScope.deptCode}";
        var accountPeriodNo = "BRC-01_05";          // 회계 기수 구현시 수정해야함
        var empName = "${sessionScope.empName}";
        var empCode = "${sessionScope.empCode}";

        // 그리드의 화폐 단위 설정
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

        var lastSelectedJournalDetail;
        var lastSelectedJournal;
        var lastSelectedSlip;
        var selectedSlipRow;
        var selectedJournalRow;

        function searchSlip() {
            enableElement({
                "button#addSlip": true,
                "button#deleteSlip": false,
                "button#addJournal": false,
                "button#deleteJournal": false,
                "button#createPdf": false,
                "button#sendPdf": false
            });

            showSlipGrid();
        }

        // 전표 추가
        function addSlip() {
            $(this).prop("disabled", true);

            var isSuccess = showSlipGrid(today);

            if (isSuccess) {
                $(slipGrid).jqGrid("addRowData", undefined, {
                    slipNo: NEW_SLIP_NO,
                    accountPeriodNo: accountPeriodNo,
                    slipType: "결산",
                    slipStatus: REQUIRE_ACCEPT_SLIP,
                    deptCode: deptCode,
                    reportingEmpCode: empCode,
                    reportingEmpName: empName,
                    reportingDate: today,
                    status: false
                }, "first");

                $(slipGrid).jqGrid("setSelection", NEW_SLIP_NO);
            }
        }

        // 전표 삭제
        function deleteSlip() {
            $.ajax({
                type: "GET",
                url: "${pageContext.request.contextPath}/account/slip.do",
                data: {
                    "method": "deleteSlip",
                    "slipNo": selectedSlipRow.slipNo
                },
                dataType: "json",
                success: function () {
                    $(slipGrid).jqGrid("delRowData", selectedSlipRow.slipNo);

                    var isNewSlip = selectedSlipRow.slipNo == NEW_SLIP_NO;
                    enableElement({
                        "button#addSlip": isNewSlip,
                        "button#deleteSlip": false,
                        "button#addJournal": false,
                        "button#deleteJournal": false,
                        "button#createPdf": false,
                        "button#sendPdf": false
                    });

                    $(journalGrid).jqGrid("clearGridData");
                }
            });

        }

        // 분개 추가
        function addJournal() {
            var journalNo = $(journalGrid).jqGrid("getGridParam", "records");

            $(journalGrid).jqGrid("addRowData", undefined, {
                journalNo: NEW_JOURNAL_PREFIX + journalNo,
                leftDebtorPrice: 0,
                rightCreditsPrice: 0,
                status: "insert"
            });
        }

        // 분개 삭제
        // 전표번호가 부여된 뒤 삭제 만들어야함
        function deleteJournal() {
            $(journalGrid).jqGrid("delRowData", selectedJournalRow.journalNo);
        }

        // PDF로 보기
        function createPdf() {

        }

        // 메일로 PDF 보내기
        function sendPdf() {

        }

        $(document).ready(function () {
            // Datepicker 연결
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

            // 이번 달 1일부터 오늘까지 조회 설정
            var today = new Date().toISOString();
            $('#from').val(today.substring(0, 8) + '01');
            $('#to').val(today.substring(0, 10));

            // 버튼 이벤트 설정
            $("button#search").click(searchSlip);
            $("button#addSlip").click(addSlip);
            $("button#deleteSlip").click(deleteSlip);
            $("button#addJournal").click(addJournal);
            $("button#deleteJournal").click(deleteJournal);
            $("button#createPdf").click(createPdf);
            $("button#sendPdf").click(sendPdf);

            $("button.add-journal-detail").click(function () {
                journalDetailButtonFunc("add")
            });
            $("button.delete-journal-detail").click(function () {
                journalDetailButtonFunc("delete")
            });

            // 그리드 생성
            createJournalGrid();
            createSlipGrid();
            createJournalDetailGrid();
            createAccountGrid();
            createCodeGrid();
        });

        // 분개상세 추가 삭제
        function journalDetailButtonFunc(method) {
            var journalDetailNo;

            if (method == "add")
                journalDetailNo = $(journalDetailGrid).jqGrid("getGridParam", "records") + 1;
            else if (method == "delete")
                journalDetailNo = $(journalDetailGrid).jqGrid("getGridParam", "selrow");

            $.ajax({
                type: "GET",
                url: "${pageContext.request.contextPath}/account/journalDetail.do",
                data: {
                    "method": method + "JournalDetail",
                    "journalNo": selectedJournalRow.journalNo,
                    "journalDetailNo": journalDetailNo
                },
                dataType: "json",
                success: function () {
                    if (method == "add")
                        $(journalDetailGrid).jqGrid("addRowData", journalDetailNo, {});
                    else if (method == "delete")
                        $(journalDetailGrid).jqGrid("delRowData", journalDetailNo);
                }
            });
        }

        // 분개상세 그리드 생성
        function createJournalDetailGrid() {
            function customElement(value, options) {
                var editRow = $(journalDetailGrid).jqGrid("getLocalRow", options.rowId);
                var ele;

                switch (editRow.accountControlType) {
                    case "CALENDAR":
                        ele = document.createElement("input");
                        ele.type = "date";
                        $(ele).change(function () {
                            if ($(".datepicker-container").hasClass("datepicker-hide")) {
                                $(ele).datepicker("destroy");
                                saveJournalDetailRow();
                            }
                        }).attr({class: "form-control form-control-sm docs-date"}, {style: "width: 94%;"}).datepicker({
                            language: 'ko-KR',
                            format: 'yyyy-mm-dd',
                            autoHide: true,
                            zIndex: "99999"
                        });

                        break;
                    case "TEXT":
                        ele = document.createElement("input");
                        ele.type = "text";
                        $(ele).attr({class: "form-control form-control-sm"});

                        break;
                    case "SEARCH":
                        ele = document.createElement("input");
                        $(ele).attr({
                            class: "form-control form-control-sm",
                            divisionCodeNo: editRow.accountControlDescription
                        })
                            .prop("readonly", true)
                            .click(function () {
                                $("#codeModal").modal();
                            });

                        $("#codeModal").modal();

                        break;
                    default:
                        ele = document.createElement("select");

                        $.ajax({
                            type: "GET",
                            url: "${pageContext.request.contextPath}/base/codeList.do",
                            data: {
                                method: "getDetailCodeList",
                                divisionCodeNo: editRow.accountControlDescription
                            },
                            dataType: "json",
                            async: false,
                            success: function (jsonObj) {
                                $.each(jsonObj.detailCodeList, function (index, obj) {
                                    $("<option></option>").appendTo(ele).val(obj.detailCode).html(obj.detailCodeName);
                                });
                            }
                        });

                        $(ele).change(function () {
                            saveJournalDetailRow();
                        }).attr({class: "form-control form-control-sm"});
                }
                ele.value = value;

                return ele;
            }

            function customValue(element, operation, value) {
                if (operation === 'get') {
                    return $(element).val();
                } else if (operation === 'set') {
                    $('input', element).val(value);
                }
            }

            $(journalDetailGrid).jqGrid(
                {
                    colModel: [
                        {name: "accountControlType", hidden: true},
                        {name: "journalDetailNo", hidden: true, key: true},
                        {name: "status", hidden: true},
                        {name: "accountControlName", label: "항목", width: 100},
                        {
                            name: "journalDescription",
                            label: "항목내용",
                            editable: true,
                            edittype: 'custom',
                            editoptions: {custom_element: customElement, custom_value: customValue}
                        }
                    ],
                    viewrecords: true,
                    rownumbers: true,
                    width: 465,
                    height: 300,
                    datatype: 'local',
                    onSelectRow: function (key) {
                        saveJournalDetailRow(key);
                    },
                    ondblClickRow: function (key) {
                        if (key) {
                            $(journalDetailGrid).jqGrid("restoreRow", lastSelectedJournalDetail);
                            $(journalDetailGrid).jqGrid("editRow", key, true, null, null
                                , "${pageContext.request.contextPath}/account/journalDetail.do", {
                                    method: "editJournalDetail"
                                });
                            lastSelectedJournalDetail = key;
                        }
                    }
                }
            );
        }

        // 분개상세 Save Row
        function saveJournalDetailRow(selectedRow) {
            if (!selectedRow || (lastSelectedJournalDetail !== selectedRow))
                $(journalDetailGrid).jqGrid(
                    "saveRow",
                    lastSelectedJournalDetail,
                    null,
                    "${pageContext.request.contextPath}/account/journalDetail.do",
                    {method: "editJournalDetail"}
                );
        }

        // 분개상세 코드 검색 그리드
        function createCodeGrid() {
            $(codeGrid).jqGrid({
                colModel: [
                    {name: "detailCode", label: "코드", key: true},
                    {name: "detailCodeName", label: "코드이름"}
                ],
                sortname: "accountControlCode",
                viewrecords: true,
                width: 465,
                height: 300,
                rowNum: 9999,
                datatype: 'local',
                onSelectRow: function (key) {
                    $("input#" + lastSelectedJournalDetail + "_journalDescription").val(key);
                    saveJournalDetailRow();
                    $("#codeModal").modal("hide");
                }
            });
        }

        // 분개상세 코드 검색
        function searchCode(obj) {
            // show loading message
            $(codeGrid)[0].grid.beginReq();

            $(codeGrid).jqGrid("clearGridData");

            $.ajax({
                type: "GET",
                url: "${pageContext.request.contextPath}/base/codeList.do",
                data: {
                    method: "getDetailCodeList",
                    divisionCodeNo: $(journalDetailGrid).jqGrid("getLocalRow", lastSelectedJournalDetail).accountControlDescription,
                    detailCodeName: $("input#searchCode").val()
                },
                dataType: "json",
                async: false,
                success: function (jsonObj) {
                    // set the new data
                    $(codeGrid).jqGrid('setGridParam', {data: jsonObj.detailCodeList});
                    // hide the show message
                    $(codeGrid)[0].grid.endReq();
                    // refresh the grid
                    $(codeGrid).trigger('reloadGrid');
                }
            });

            $(codeGrid)[0].grid.endReq();
        }

        // 분개상세 내용 보기
        function showJournalDetailGrid(journalNo) {
            var display = selectedSlipRow.slipStatus === REQUIRE_ACCEPT_SLIP ? "flex" : "none";
            $("#journalDetailModal .modal-footer").css("display", display);

            // show loading message
            $(journalDetailGrid)[0].grid.beginReq();
            $(journalDetailGrid).jqGrid("clearGridData");
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

        // 차변과 대변 텍스트박스 활성/비활성화
        function setBalanceDivision(event) {
            var selectedRowId = typeof event == "object" ? $(event.target).attr("rowid") : event;
            var balanceDivision = $("tr#" + selectedRowId + " select[name='balanceDivision']").val();   // 선택된 차대변 값
            var array = ["leftDebtorPrice", "rightCreditsPrice"];

            // 텍스트박스 활성/비활성화
            $("tr#" + selectedRowId + " input[name='" + array[0] + "']").prop("disabled", balanceDivision !== array[0]);
            $("tr#" + selectedRowId + " input[name='" + array[1] + "']").prop("disabled", balanceDivision === array[0]);

            // 선택되지 않은 쪽을 0으로
            var nameToZero = array[0] === balanceDivision ? array[1] : array[0];
            $("tr#" + selectedRowId + " input[name='" + nameToZero + "']").val(0);
        }

        // 분개 그리드 생성
        function createJournalGrid() {
            function journalOnEditFunc(key) {
                setBalanceDivision(key);

                $("input[name='accountName']").click(function () {
                    $("div#accountModal").modal()
                });

                $("input#" + key + "_accountCode").keyup(function (e) {
                    $("input#" + key + "_accountName").val(getAccountName($(this).val()));
                });
            }

            $(journalGrid).jqGrid({ //밑에 그리드
                colModel: [
                    {name: "journalNo", label: "분개번호", width: 240, key: true},
                    {
                        name: "balanceDivision",
                        label: "구분",
                        width: 80,
                        editable: true,
                        edittype: "select",
                        editoptions: {
                            value: "leftDebtorPrice:차변;rightCreditsPrice:대변",
                            dataEvents: [{type: "change", fn: setBalanceDivision}]
                        },
                        editrules: {required: true}
                    },
                    {name: "accountCode", label: "계정코드", width: 100, editable: true, editrules: {required: true}},
                    {
                        name: "accountName",
                        label: "계정과목",
                        align: "center",
                        editable: true,
                        edittype: "button",
                        editoptions: {class: "form-control form-control-sm btn btn-flat btn-outline-dark"}
                    },
                    {
                        name: "leftDebtorPrice", label: "차변", editable: true, formatter: "currency",
                        formatoptions: currencyFormat,
                        editrules: {
                            number: true,
                            required: true
                        }
                    },
                    {
                        name: "rightCreditsPrice", label: "대변", editable: true, formatter: "currency",
                        formatoptions: currencyFormat,
                        editrules: {
                            number: true,
                            required: true
                        }
                    },
                    {name: "customerCode", label: "거래처코드", width: 100, editable: true},
                    {name: "customerName", label: "거래처", width: 150, editable: true},
                    {
                        name: "journalDetail", label: "분개상세", align: "center",
                        formatter: function (cellValue, options) {
                            var buttonHtml = "<a href=\"javascript:void(0);\" onclick=\"showJournalDetail('" + options.rowId + "')\">상세 보기</a>";
                            return buttonHtml;
                        }
                    },
                    {name: "status", width: 50, hidden: true}
                ],
                sortname: "journalNo",
                rownumbers: true,
                viewrecords: true,
                shrinkToFit: false,
                responsive: true,
                autowidth: true,
                height: 200,
                rowNum: 20,
                footerrow: true,
                userDataOnFooter: true,
                datatype: 'local',
                editurl: 'clientArray',
                onSelectRow: function (key) {
                    if (lastSelectedJournal !== key)
                        $(journalGrid).jqGrid("saveRow", lastSelectedJournal, null, null, null, saveJournalRow);

                    selectedJournalRow = $(journalGrid).jqGrid("getLocalRow", key);

                    if (selectedSlipRow.slipStatus === REQUIRE_ACCEPT_SLIP)
                        enableElement({"button#deleteJournal": true});
                },
                ondblClickRow: function (id) {
                    if (id && selectedSlipRow.slipStatus === REQUIRE_ACCEPT_SLIP) {
                        $(journalGrid).jqGrid("restoreRow", lastSelectedJournal);
                        $(journalGrid).jqGrid("editRow", id, true, journalOnEditFunc, null, null, null, saveJournalRow);
                        lastSelectedJournal = id;
                    }
                },
                gridComplete: compareDebtorCredits
            });


            // 분개 저장
            function saveJournalRow(key) {
                var debtorAndCredits = compareDebtorCredits();
                var savedRow = $(journalGrid).jqGrid("getLocalRow", key);

                // 저장한 분개행이 저장되어 있을 경우 update
                savedRow.status = savedRow.status === "normal" ? "update" : savedRow.status;

                $(journalGrid).jqGrid("setRowData", savedRow);

                // refresh the grid
                $(journalGrid).trigger('reloadGrid');

                if (debtorAndCredits.isOverZero && debtorAndCredits.isEqualSum) {
                    var selectedSlipNo = $(slipGrid).jqGrid("getGridParam", "selrow");

                    if (selectedSlipNo === NEW_SLIP_NO)
                        saveSlip(selectedSlipNo);
                    else
                        saveJournal(selectedSlipNo);
                }
            }

            // 전표 저장
            function saveSlip(selectedSlipNo) {
                // show loading message
                $(journalGrid)[0].grid.beginReq();
                $.ajax({
                    type: "POST",
                    url: "${pageContext.request.contextPath}/account/slip.do",
                    data: {
                        "method": "addSlip",
                        "slipObj": JSON.stringify(selectedSlipRow),
                        "journalObj": JSON.stringify($(journalGrid).jqGrid("getRowData"))
                    },
                    dataType: "json",
                    success: function (jsonObj) {
                        // // hide the show message
                        $(journalGrid)[0].grid.endReq();

                        enableElement({"button#addSlip": true});

                        if (showSlipGrid())
                            $(slipGrid).jqGrid("setSelection", jsonObj.slipNo);
                    }
                });
            }

            // 분개 업데이트
            function saveJournal(selectedSlipNo) {
                // show loading message
                $(journalGrid)[0].grid.beginReq();
                $.ajax({
                    type: "POST",
                    url: "${pageContext.request.contextPath}/account/journal.do",
                    data: {
                        "method": "editJournal",
                        "slipNo": selectedSlipNo,
                        "journalObj": JSON.stringify($(journalGrid).jqGrid("getRowData"))
                    },
                    dataType: "json",
                    success: function (jsonObj) {
                        // // hide the show message
                        $(journalGrid)[0].grid.endReq();

                        if (showSlipGrid())
                            $(slipGrid).jqGrid("setSelection", selectedSlipNo);
                    }
                });
            }

            // 차대변 일치 확인
            function compareDebtorCredits() {
                var debtorSum = $(journalGrid).jqGrid("getCol", "leftDebtorPrice", false, "sum");
                var creditsSum = $(journalGrid).jqGrid("getCol", "rightCreditsPrice", false, "sum");

                $(journalGrid).jqGrid("footerData", "set", {
                    balanceDivision: "합계",
                    leftDebtorPrice: debtorSum,
                    rightCreditsPrice: creditsSum
                });

                var result = {isOverZero: debtorSum > 0, isEqualSum: debtorSum == creditsSum};
                var color = result.isEqualSum ? "" : "red";

                $(".ui-jqgrid-ftable td").css("color", color);

                return result;
            }
        }

        // 계정코드 입력시 계정과목 검색
        function getAccountName(accountCode) {
            var accountName;

            $.ajax({
                type: "GET",
                url: "${pageContext.request.contextPath}/account/account.do",
                data: {
                    method: "getAccount",
                    accountCode: accountCode
                },
                dataType: "json",
                success: function (jsonObj) {
                    accountName = jsonObj.account.accountName;
                },
                async: false
            });
            return accountName;
        }

        // 분개 그리드 내용 보기
        function showJournalGrid(slipNo) {
            // show loading message
            $(journalGrid)[0].grid.beginReq();
            $(journalGrid).jqGrid("clearGridData");

            if (selectedSlipRow.slipNo !== NEW_SLIP_NO) {
                $.ajax({
                    type: "GET",
                    url: "${pageContext.request.contextPath}/account/journal.do",
                    data: {
                        "method": "findSingleJournalList",
                        "slipNo": slipNo
                    },
                    dataType: "json",
                    success: function (jsonObj) {
                        obj = jsonObj.journalList;

                        // set the new data
                        $(journalGrid).jqGrid('setGridParam', {data: obj});
                        // refresh the grid
                        $(journalGrid).trigger('reloadGrid');
                        // hide the show message
                        $(journalGrid)[0].grid.endReq();
                    }
                });
            }

            $(journalGrid)[0].grid.endReq();
        }

        function showJournalDetail(key) {
            $(journalGrid).jqGrid("setSelection", key);

            if (key.substr(0, NEW_SLIP_NO.length) == NEW_SLIP_NO) {
                alert("분개 상세는 전표가 생성된 이후 편집 가능합니다.");
                return;
            }
            $("div#journalDetailModal").modal();
            showJournalDetailGrid(key);
        }

        // 전표 그리드
        function createSlipGrid() {
            $(slipGrid).jqGrid({
                colModel: [
                    {name: "slipNo", label: "전표번호", width: 250, key: true, sorttype: "text"},
                    {name: "accountPeriodNo", label: "기수"},
                    {name: "deptCode", label: "부서코드"},
                    {name: "deptName", label: "부서", hidden: true},
                    {
                        name: "slipType", label: "구분", editable: true,
                        edittype: "select",
                        editoptions: {value: "결산:결산;대체:대체"}
                    },
                    {name: "expenseReport", label: "적요", width: 300, editable: true},
                    {name: "slipStatus", label: "승인상태"},
                    // {name: "status", width: 30, hidden: true},
                    {name: "reportingEmpCode", label: "작성자코드"},
                    {name: "reportingEmpName", label: "작성자", hidden: true},
                    {name: "reportingDate", label: "작성일"},
                    {name: "positionCode", label: "직급", hidden: true}
                ],
                sortname: "slipNo",
                sortorder: "desc",
                viewrecords: true,
                autowidth: true,
                shrinkToFit: false,
                responsive: true,
                height: 370,
                rowNum: 8,
                datatype: 'local',
                pager: "#slipGridPager",
                onSelectRow: function (key) {
                    if (selectedSlipRow && selectedSlipRow.id !== key)
                        $(slipGrid).jqGrid("saveRow", selectedSlipRow.id);

                    selectedSlipRow = $(slipGrid).jqGrid("getLocalRow", key);

                    var isEnable = selectedSlipRow.slipStatus == REQUIRE_ACCEPT_SLIP;
                    enableElement({
                        "button#deleteSlip": isEnable,
                        "button#addJournal": isEnable,
                        "button#deleteJournal": false,
                        "button#createPdf": true,
                        "button#sendPdf": true
                    });

                    showJournalGrid(key);
                },
                ondblClickRow: function (id) {
                    if (id && selectedSlipRow.slipStatus == REQUIRE_ACCEPT_SLIP) {
                        $(slipGrid).jqGrid("restoreRow", lastSelectedSlip);
                        $(slipGrid).jqGrid("editRow", id, true, null, null, "${pageContext.request.contextPath}/account/slip.do", {method: "updateSlip"});
                        lastSelectedSlip = id;
                    }
                }
            });
        }

        // 전표 검색
        function showSlipGrid(date) {
            var isSuccess = false;

            if (date) {
                $("#from").val(date);
                $("#to").val(date);
            }

            $(slipGrid).jqGrid("clearGridData");
            $(journalGrid).jqGrid("clearGridData");

            // show loading message
            $(slipGrid)[0].grid.beginReq();
            $.ajax({
                url: "${pageContext.request.contextPath}/account/slip.do",
                data: {
                    "method": "findRangedSlipList",
                    "from": $("#from").val(),
                    "to": $("#to").val()
                },
                dataType: "json",
                success: function (jsonObj) {
                    // set the new data
                    $(slipGrid).jqGrid('setGridParam', {data: jsonObj.slipList});
                    // hide the show message
                    $(slipGrid)[0].grid.endReq();
                    // refresh the grid
                    $(slipGrid).trigger('reloadGrid');

                    isSuccess = true;
                },
                async: false
            });

            $(slipGrid)[0].grid.endReq();

            return isSuccess;
        }

        // Map 내의 객체들 Disabled/Enabled
        function enableElement(obj) {
            for (var key in obj)
                $(key).prop("disabled", !obj[key]);
        }

        // 계정과목 검색 그리드
        function createAccountGrid() {
            $(accountGrid).jqGrid({
                colModel: [
                    {name: "accountInnerCode", label: "계정과목 코드", key: true},
                    {name: "accountName", label: "계정과목"}
                ],
                sortname: "accountInnerCode",
                viewrecords: true,
                width: 465,
                height: 300,
                rowNum: 9999,
                datatype: 'local',
                onSelectRow: function (key) {
                    var name = $(accountGrid).jqGrid("getCell", key, "accountName");

                    $("input#" + lastSelectedJournal + "_accountCode").val(key);
                    $("input#" + lastSelectedJournal + "_accountName").val(name);

                    $("#accountModal").modal("hide");
                }
            });
        }

        // 계정과목 검색
        function searchAccount() {
            // show loading message
            $(accountGrid)[0].grid.beginReq();

            $(accountGrid).jqGrid("clearGridData");

            $.ajax({
                type: "GET",
                url: "${pageContext.request.contextPath}/account/account.do",
                data: {
                    method: "getAccountListByName",
                    accountName: $("div#accountModal input#searchName").val()
                },
                dataType: "json",
                success: function (jsonObj) {
                    // set the new data
                    $(accountGrid).jqGrid('setGridParam', {data: jsonObj.accountList});
                    // hide the show message
                    $(accountGrid)[0].grid.endReq();
                    // refresh the grid
                    $(accountGrid).trigger('reloadGrid');
                }
            });

            $(accountGrid)[0].grid.endReq();
        }
    </script>
</head>
<body>
    <div class="col-12 card">
        <div class="card-body">
            <div class="row">
                <div class="col-sm-6 form-row ml-0 mr-0">
                    <div class="docs-datepicker mr-1">
                        <div class="input-group">
                            <input id="from" type="date" class="form-control form-control-sm docs-date"
                                   data-toggle="datepicker-to" required>
                            <div class="input-group-append">
                                <button type="button"
                                        class="form-control form-control-sm btn btn-outline-secondary docs-datepicker-to-trigger">
                                    <i style="color:#FD7D86;" class="fa fa-calendar" aria-hidden="true"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="docs-datepicker mr-1">
                        <div class="input-group">
                            <input id="to" type="date" class="form-control form-control-sm docs-date"
                                   data-toggle="datepicker-from" required>
                            <div class="input-group-append">
                                <button type="button"
                                        class="form-control form-control-sm btn btn-outline-secondary docs-datepicker-from-trigger">
                                    <i style="color:#FD7D86;" class="fa fa-calendar" aria-hidden="true"></i>
                                </button>
                            </div>
                        </div>
                    </div>

                    <div>
                        <button type="button" id="search"
                                class="form-control form-control-sm btn btn-flat btn-outline-dark mb-3">
                            검색
                        </button>
                    </div>
                </div>
                <div class="col-sm-6 form-row ml-0 mr-0">
                    <div class="ml-auto">
                        <button type="button" id="addSlip"
                                class="form-control form-control-sm btn btn-flat btn-outline-dark mb-3"
                                style="width:90px">
                            전표 추가
                        </button>
                        <button type="button" id="deleteSlip"
                                class="form-control form-control-sm btn btn-flat btn-outline-dark mb-3"
                                style="width:90px" disabled>
                            전표 삭제
                        </button>
                        <button type="button" id="addJournal"
                                class="form-control form-control-sm btn btn-flat btn-outline-dark mb-3"
                                style="width:90px" disabled>
                            분개 추가
                        </button>
                        <button type="button" id="deleteJournal"
                                class="form-control form-control-sm btn btn-flat btn-outline-dark mb-3"
                                style="width:90px" disabled>
                            분개 삭제
                        </button>
                        <button type="button" id="createPdf"
                                class="form-control form-control-sm btn btn-flat btn-outline-dark mb-3"
                                style="width:90px" disabled>
                            PDF 보기
                        </button>
                        <button type="button" id="sendPdf"
                                class="form-control form-control-sm btn btn-flat btn-outline-dark mb-3"
                                style="width:90px" disabled>
                            PDF 발송
                        </button>
                    </div>
                </div>
            </div>
            <div>
                <table id="slipGrid"></table>
                <div id="slipGridPager"></div>
            </div>
            <div class="mt-4">
                <table id="journalGrid"></table>
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

    <div class="modal fade" id="codeModal" aria-hidden="true" style="display: none;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">코드 검색</h5>
                    <button type="button" class="close" data-dismiss="modal"><span>×</span></button>
                </div>
                <div class="modal-body">
                    <div class="input-group mb-3">
                        <input id="searchCode" type="text" class="form-control form-control-sm">
                        <div class="input-group-append">
                            <button type="button"
                                    class="form-control form-control-sm btn btn-outline-secondary"
                                    style="width: 80px;"
                                    onclick="searchCode(this)">
                                <i style="color:#FD7D86;" class="fa fa-search" aria-hidden="true"></i>
                            </button>
                        </div>
                    </div>
                    <table id="codeGrid"></table>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="accountModal" aria-hidden="true" style="display: none;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">계정 과목</h5>
                    <button type="button" class="close" data-dismiss="modal"><span>×</span></button>
                </div>
                <div class="modal-body">
                    <div class="input-group mb-3">
                        <input id="searchName" type="text" class="form-control form-control-sm">
                        <div class="input-group-append">
                            <button type="button"
                                    class="form-control form-control-sm btn btn-outline-secondary"
                                    style="width: 80px;"
                                    onclick="searchAccount()">
                                <i style="color:#FD7D86;" class="fa fa-search" aria-hidden="true"></i>
                            </button>
                        </div>
                    </div>
                    <table id="accountGrid"></table>
                </div>
            </div>
        </div>
    </div>

</body>
</html>
