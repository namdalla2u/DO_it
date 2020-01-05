<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%--<!DOCTYPE html>--%>
<%--<html>--%>
<%--<head>--%>
<%--<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">--%>
<%--<title>heo account</title>--%>
<%--<!-- 제이쿼리,제이쿼리 UI , jqGrid 사용위해 링크검 -->--%>
<%--<link rel="stylesheet" type="text/css"--%>
<%--href="${pageContext.request.contextPath}/scripts/jqueryUI/jquery-ui.min.css"/>--%>
<%--<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/jqGrid/css/ui.jqgrid.css"/>--%>
<%--<link rel="stylesheet" type="text/css"--%>
<%--href="${pageContext.request.contextPath}/scripts/jqGrid/plugins/ui.multiselect.css"/>--%>
<%--<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/jqGrid/css/ui.jqgrid.css"/>--%>
<%--<link rel="stylesheet" type="text/css"--%>
<%--href="${pageContext.request.contextPath}/scripts/jqGrid/plugins/ui.multiselect.css"/>--%>

<%--&lt;%&ndash;<script src="${pageContext.request.contextPath}/scripts/jquery/jquery-3.3.1.min.js"></script>&ndash;%&gt;--%>
<%--<script src="${pageContext.request.contextPath}/scripts/jqueryUI/jquery-ui.min.js"></script>--%>
<%--<script src="${pageContext.request.contextPath}/scripts/jqGrid/js/jquery.jqGrid.min.js"></script>--%>
<%--<script src="${pageContext.request.contextPath}/scripts/jqGrid/js/i18n/grid.locale-kr.js"></script>--%>


<%--<script>--%>
<%--var code;--%>
<%--var codeType;--%>
<%--var codeName;--%>
<%--var employeeBean;--%>

<%--$(document).ready(function () {--%>
<%--$("input[type=button], input[type=submit] , input[type=reset] ,input[type=text] ,input[type=password]").button();--%>


<%--$("#deptCode").click({--%>
<%--code: "CO-03",--%>
<%--codeName: "부서선택",--%>
<%--type: "deptCode"--%>
<%--},--%>
<%--showDialog--%>
<%--);--%>


<%--$("#confirmtable").attr("style", "display:none");--%>

<%--var errorCode = <%=request.getAttribute("errorCode")%>;--%>
<%--if (errorCode < 0)--%>
<%--$("#confirmtable").attr("style", "display:block");--%>
<%--else--%>
<%--$("#confirmtable").attr("style", "display:none");--%>

<%--<%session.invalidate();%>   //세션 완전히 삭제--%>
<%--});--%>

<%--function showDialog(event) {--%>
<%--/* click이벤트가 발생했을 때 넘어오는 parameter값을 변수에 저장한다 */--%>
<%--code = event.data.code;--%>
<%--codeName = event.data.codeName;--%>
<%--codeType = event.data.type;--%>

<%--/* dialog를 설정하는 부분 */--%>
<%--$("<div>", {--%>
<%--"id": "codeDialog",--%>
<%--"title": codeName--%>
<%--}).appendTo("body");--%>

<%--$("#codeDialog").dialog({--%>
<%--"width": "600",--%>
<%--"height": "400"--%>
<%--});--%>

<%--/* dialog의 내용으로 띄울 grid 설정하는 부분 */--%>
<%--$("<table>", {--%>
<%--"id": "codeList"--%>
<%--}).appendTo("#codeDialog");--%>

<%--$.jgrid.gridUnload("#codeList");--%>
<%--/* 해당하는 code를 넘겨줘서 상세코드값을 받아온다 */--%>
<%--$("#codeList").jqGrid({--%>
<%--url: "${pageContext.request.contextPath}/base/codeList.do",--%>
<%--postData: {--%>
<%--"method": "getDetailCodeList",--%>
<%--"code": code--%>
<%--},--%>
<%--datatype: 'json',--%>
<%--jsonReader: {--%>
<%--root: 'detailCodeList'--%>
<%--},--%>
<%--colNames: ['부서번호', '부서명'],--%>
<%--colModel: [{--%>
<%--name: 'detailCode',--%>
<%--width: 200,--%>
<%--editable: false--%>
<%--}, {--%>
<%--name: 'detailCodeName',--%>
<%--width: 200,--%>
<%--editable: false--%>
<%--},],--%>
<%--width: 500,--%>
<%--height: 400,--%>
<%--viewrecords: true,--%>
<%--rownumbers: true,--%>
<%--onSelectRow: function (rowid) {--%>
<%--/* codeList의 row를 선택할때마다 선택된 row의 상세코드값이 변수에 저장된다. */--%>
<%--var codeNo = $("#codeList").jqGrid("getRowData", rowid).detailCode;--%>
<%--/* 동적으로 부서dialog인지 권한 dialog인지 따라 상세코드가 부서입력란 또는 권한 입력란에 들어간다. */--%>
<%--$("#" + codeType).val(codeNo);--%>
<%--$("#codeDialog").remove();--%>
<%--$("#codeDialog").dialog("close");--%>
<%--}--%>
<%--});--%>
<%--}--%>

<%--</script>--%>

<%--&lt;%&ndash;<style type="text/css">--%>

<%--input[type=text], input[type=password] {--%>
<%--display: inline;--%>
<%--width: 50%;--%>
<%--padding-left: 1%;--%>
<%--margin-bottom: 10px;--%>
<%--transition: 0.6s;--%>
<%--outline: lime;--%>
<%--height: 30px;--%>
<%--background-color: #FAF4C0;--%>
<%--}--%>

<%--div {--%>
<%--text-align: center;--%>
<%--overflow: hidden;--%>
<%--height: auto;--%>
<%--border: 2px outset navy;--%>
<%--border-radius: 10px;--%>
<%--}--%>

<%--#outdiv {--%>
<%--/* margin: 10px; */--%>
<%--padding: 10px;--%>
<%--position: absolute;--%>
<%--top: 20%;--%>
<%--left: 45%;--%>

<%--width: 350px;--%>
<%--height: 500px;--%>
<%--border: 3px outset navy;--%>
<%--background-color: #FAED7D--%>

<%--}--%>

<%--#backimg {--%>

<%--position: absolute;--%>
<%--align: center;--%>
<%--border: 0px solid gray;--%>
<%--width: 100%;--%>
<%--height: 100%;--%>
<%--}--%>

<%--body {--%>
<%--background-image: url('${pageContext.request.contextPath}/resources/image/back.jpg');--%>
<%--background-size: cover;--%>

<%--}--%>

<%--h1 {--%>
<%--color: navy;--%>
<%--text-shadow: 2px 3px 5px #FFB2F5;--%>
<%--}--%>

<%--h3 {--%>
<%--color: blue;--%>
<%--text-shadow: 2px 3px 5px gray;--%>
<%--}--%>

<%--label {--%>
<%--color: #474747;--%>
<%--}--%>

<%--#confirmtable {--%>
<%--color: #FF00DD;--%>
<%--border: 0px;--%>
<%--text-shadow: 1px 1px 1px #F361A6;--%>
<%--}--%>


<%--</style>&ndash;%&gt;--%>


<%--</head>--%>
<%--<body>--%>

<%--<div class="col-12">--%>
<%--<div class="card">--%>
<%--<div class="card-body">--%>
<%--<form method="post" action="${pageContext.request.contextPath}/login.do">--%>
<%--<input type="text" placeholder="부서코드" name="deptCode" id="deptCode"--%>
<%--style="background-color:#FAF4C0"/><br/><br/>--%>
<%--<input type="text" placeholder="사원코드" title="사원코드를 입력해주세요" name="empCode"/><br/><br/>--%>
<%--<input type="password" placeholder="비밀번호" title="비밀번호를 입력해주세요" name="empPassword"/><br/><br/>--%>
<%--<input type="reset" value="취소"/>--%>
<%--<input type="submit" value="로그인"/>--%>
<%--</form>--%>
<%--<div id="confirmtable">--%>
<%--<p>로그인 실패 했습니다</p>--%>
<%--${errorMsg}--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--</body>--%>
<%--</html>--%>

<!doctype html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>Login - srtdash</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/font-awesome.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/themify-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/metisMenu.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/owl.carousel.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/slicknav.min.css">
    <%-- amchart css --%>
    <link rel="stylesheet" href="https://www.amcharts.com/lib/3/plugins/export/export.css" type="text/css" media="all"/>
    <%-- others css --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/typography.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/default-css.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/responsive.css">
    <%-- modernizr css --%>
    <script src="${pageContext.request.contextPath}/assets/js/vendor/modernizr-2.8.3.min.js"></script>
    <%-- jquery latest version --%>
    <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
    <%-- bootstrap 4 js --%>
    <script src="${pageContext.request.contextPath}/assets/js/popper.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/owl.carousel.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/metisMenu.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/jquery.slimscroll.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/jquery.slicknav.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/plugins.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/scripts.js"></script>
    
</head>

<body>
    <!--[if lt IE 8]>
    <p class="browserupgrade">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
    <![endif]-->
    <!-- preloader area start -->
    <div id="preloader">
        <div class="loader"></div>
    </div>
    <!-- preloader area end -->
    <!-- login area start -->
    <div class="login-area login-bg">
        <div class="container">
            <div class="login-box ptb--100">
                <form method="post" action="${pageContext.request.contextPath}/login.do">
                
                    <div class="login-form-head">
                        <h4>로그인</h4>
                    </div>
                    <div class="login-form-body">
                        <div class="form-gp">
                            <label for="empCode">사원코드</label>
                            <input type="text" id="empCode" name="empCode">
                            <i class="ti-user"></i>
                        </div>
                        <div class="form-gp">
                            <label for="userPw">비밀번호</label>
                            <input type="password" id="userPw" name="userPw">
                            <i class="ti-lock"></i>
                        </div>
                        <div class="row mb-4 rmber-area">
                            <div class="col-6">
                                <div class="custom-control custom-checkbox mr-sm-2">
                                    <input type="checkbox" class="custom-control-input" id="customControlAutosizing">
                                    <label class="custom-control-label" for="customControlAutosizing">Remember
                                                                                                      Me</label>
                                </div>
                            </div>
                            <div class="col-6 text-right">
                                <a href="#">Forgot Password?</a>
                            </div>
                        </div>
                        <div class="submit-btn-area">
                            <button id="form_submit" type="submit">Submit <i class="ti-arrow-right"></i></button>
                        </div>
                        <div class="form-footer text-center mt-5">
                            <p class="text-muted">Don't have an account? <a href="register.html">Sign up</a></p>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!-- login area end -->
    
    
    
    
    
    
</body>

</html>
