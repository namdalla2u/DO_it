<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>계정과목</title>

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

    <script>
        var accountGrid = "#accountGrid";
        var accountDetailGrid = "#accountDetailGrid";

        $(document).ready(function () {

            createAccount();        
            showAccount();
            createAccountDetail(); 
        });

        function createAccount() {
            $(accountGrid).jqGrid({
                colModel: [
                    {name: "accountInnerCode", label: "계정과목 코드", key: true},
                    {name: "accountName", label: "계정과목"}
                ],
                viewrecords: true,
                autowidth: true, 
                height: 562,
                rowNum: 30,
                datatype: 'local',
                onSelectRow: function (key) {
                    showAccountDetail(key)
                }
            });
        }


        function showAccount() {

            // show loading message
            $(accountGrid)[0].grid.beginReq();
            $.ajax({
                type: "GET",
                url: "${pageContext.request.contextPath}/account/account.do",
                data: {
                    "method": "findParentAccountList",
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

        var lastSelectedAccountDetail;

        function createAccountDetail() {
            $(accountDetailGrid).jqGrid({
                colModel: [
                    {name: "accountInnerCode", label: "계정과목 코드", key: true},
                    {name: "accountName", label: "계정과목", editable: true},
                    {
                        name: "editable", label: "수정가능여부",
                        formatter: function (cellValue, options) {
                            var style = " style='color: green'";
                            var text = "가능";

                            if (cellValue == 0) {
                                style = " style='color: red'";
                                text = "불가";
                            }

                            return "<span" + style + ">" + text + "</span>";
                        }
                    }
                ],
                viewrecords: true,
                autowidth: true,
                height: 562,
                rowNum: 9999,
                datatype: 'local',
                onSelectRow: function (id) {
                    if (lastSelectedAccountDetail && lastSelectedAccountDetail !== id) {
                        $(accountDetailGrid).jqGrid("saveRow", lastSelectedAccountDetail, null, null, null, saveAccountDetail);
                        lastSelectedAccountDetail = null;
                    }
                },
                ondblClickRow: function (id) {
                    if (id && $(accountDetailGrid).getLocalRow(id).editable === "1") {
                        $(accountDetailGrid).jqGrid("restoreRow", lastSelectedAccountDetail);
                        $(accountDetailGrid).jqGrid("editRow", id, true, null, null, null, null, saveAccountDetail);
                        lastSelectedAccountDetail = id;
                    }
                }
            });
        }

        function showAccountDetail(code) {
           $(accountDetailGrid).jqGrid("clearGridData");

            // show loading message
            $(accountDetailGrid)[0].grid.beginReq();
            $.ajax({
                type: "GET",
                url: "${pageContext.request.contextPath}/account/account.do",
                data: {
                    "method": "findDetailAccountList",
                    "code": code
                },
                dataType: "json",
                success: function (jsonObj) {
                    // set the new data
                    $(accountDetailGrid).jqGrid('setGridParam', {data: jsonObj.detailAccountList});
                    // hide the show message
                    $(accountDetailGrid)[0].grid.endReq();
                    // refresh the grid
                    $(accountDetailGrid).trigger('reloadGrid'); // 새로고침
                }
            });

            $(accountGrid)[0].grid.endReq();
        }

        function saveAccountDetail(key) {
            var selectedRow = $(accountDetailGrid).jqGrid("getLocalRow", key);

            // show loading message
            $(accountDetailGrid)[0].grid.beginReq();
            $.ajax({
                type: "POST",
                url: "${pageContext.request.contextPath}/account/account.do",
                data: {
                    method: "editAccount",
                    accountInnerCode: selectedRow.accountInnerCode,
                    accountName: selectedRow.accountName
                },
                dataType: "json",
                success: function (jsonObj) { 
                    // // hide the show message
                    $(accountDetailGrid)[0].grid.endReq();
                    // // refresh the grid
                    $(accountDetailGrid).trigger('reloadGrid');
                }
            });
        }
    </script>
</head>
<body>
    <div class="col-6">
        <div class="card">
            <div class="card-body">
                <table id="accountGrid"></table>
            </div>
        </div>
    </div>
    <div class="col-6">
        <div class="card">
            <div class="card-body">
                <table id="accountDetailGrid"></table>
            </div>
        </div>
    </div>
</body>
</html>