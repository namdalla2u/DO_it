<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title><sitemesh:write property='title'/> - TISSystem ERP</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" type="image/png" href="${pageContext.request.contextPath}/assets/images/icon/favicon.ico">
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

    <%-- 현재 접근한 메뉴에 active 클래스 추가 및 현재 페이지 타이틀 표시 --%>
    <script>
        $(document).ready(function () {
        	createOpenApiGrid();
        	showOpenApiGrid();
        	
            var menuLists = $("ul#menu > li");

            $.each(menuLists, function (index, category) {
                var ulTag = $(category).find("ul");
                var menuItems = $(category).find("li > a");
                var br = true;

                $.each(menuItems, function (index, item) {
                    // console.log($(location).attr("href"));
                    if ($(location).attr("href") == item.href) {
                        $(item).parent().addClass("active");
                        $(category).addClass("active");
                        $(ulTag).addClass("in").attr("style", "");

                        return br = false;
                    }
                });
                if (!br) return false;
            })
        });
    </script>

    <sitemesh:write property='head'/>
</head>

<body>
    <%--[if lt IE 8]>
    <p class="browserupgrade">You are using an <strong>outdated</strong> browser. Please <a
            href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
    <![endif]--%>
    <%-- preloader area start --%>
<!--     <div id="preloader">
        <div class="loader"></div>
    </div> -->
    <%-- preloader area end --%>
    <%-- page container area start --%>
    <div class="page-container">
        <%-- sidebar menu area start --%>
        <div class="sidebar-menu">
            <div class="sidebar-header">
                <div class="logo">
                    <%--<img src="/assets/images/icon/logo.png" alt="logo">--%>
                    <h3><a href="${pageContext.request.contextPath}/hello.html">HoWoo Account</a></h3>
                    
                </div>
            </div>
            <div class="main-menu">
                <div class="menu-inner">
                    <nav>
                        <ul class="metismenu" id="menu">
                            <li>
                                <a href="javascript:void(0)" aria-expanded="true">
                                    <i class="ti-dashboard"></i>
                                    <span>전표 입력</span>
                                </a>
                                <ul class="collapse">
                                    <li>
                                        <a href="${pageContext.request.contextPath}/account/slipForm.html">일반전표</a>
                                    </li>
                                    <li>
                                        <a href="${pageContext.request.contextPath}/account/approvalManager.html">전표승인</a>
                                    </li>
                                </ul>
                            </li>

                            <li>
                                <a href="javascript:void(0)" aria-expanded="true">
                                    <i class="ti-layout-sidebar-left"></i>
                                    <span>기본 설정</span>
                                </a>
                                <ul class="collapse">
                                <li><a href="${pageContext.request.contextPath}/hr/MyprofileForm.html">내 프로필</a></li>
                                <li><a href="${pageContext.request.contextPath}/hr/employeeForm.html">사원 관리</a></li>
                                    <li><a href="${pageContext.request.contextPath}/company/WorkplaceInserForm.html">회사 등록</a></li>
                                    <li><a href="${pageContext.request.contextPath}/company/WorkplaceManagement.html">거래처 관리</a></li>
                                    <li><a href="${pageContext.request.contextPath}/account/accountForm.html">계정과목 및
                                                                                                              적요</a>
                                    </li>
                                </ul>
                            </li>
                            <li>
                                <a href="javascript:void(0)" aria-expanded="true">
                                    <i class="ti-pie-chart"></i>
                                    <span>장부 관리</span>
                                </a>
                                <ul class="collapse">
                                    <li><a href="/ledgerManage/clientLedger">거래처원장</a></li>
                                    <li><a href="/ledgerManage/accountLedger">계정별원장</a></li>
                                    <li><a href="${pageContext.request.contextPath}/statement/cashJournal.html">현금출납장</a></li>
                                    <li><a href="/ledgerManage/vatLedger">매입매출장</a></li>
                                    <li><a href="${pageContext.request.contextPath}/account/journalForm.html">분개장</a>
                                    <li><a href="${pageContext.request.contextPath}/statement/detailTrialBalance.html">일(월)계표</a></li>
                                    </li>
                                </ul>
                            </li>
                            <li>
                                <a href="javascript:void(0)" aria-expanded="true">
                                    <i class="ti-palette"></i>
                                    <span>결산 및 재무제표</span>
                                </a>
                                <ul class="collapse">
                                
                                    <li><a href="${pageContext.request.contextPath}/statement/totalTrialBalance.html">합계잔액시산표</a>
                                    </li>
                                    <li><a href="${pageContext.request.contextPath}/statement/incomeStatement.html">손익계산서</a>
                                    </li>
                                    <li><a href="${pageContext.request.contextPath}/statement/financialPosition.html">재무상태표</a>
                                    </li>
                                </ul>
                            </li>
                            <li>
                                <a href="javascript:void(0)" aria-expanded="true">
                                    <i class="ti-slice"></i>
                                    <span>전기분재무제표</span>
                                </a>
                                <ul class="collapse">
                                    <li><a href="themify.html">거래처별초기이월</a></li>
                                    <li><a href="themify.html">전기분손익계산서</a></li>
                                    <li><a href="fontawesome.html">전기분재무상태표</a></li>
                                </ul>
                            </li>
                            <li>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>
        </div>     
         <div  style="margin-left:70%;">사원코드 : ${empCode}&nbsp 사원명 : <a href="${pageContext.request.contextPath}/hr/MyprofileForm.html">${empName}</a>님&nbsp  
        <a href="${pageContext.request.contextPath}/logout.do" aria-expanded="true">
           <button class="ti-hand-stop">로그아웃</button>
        </a><br>
        </div>  
        <div> 
<%-- <div  style="margin-left:70%;">사원코드 : ${empCode}&nbsp 사원명 : ${empName}님&nbsp  
        <a href="${pageContext.request.contextPath}/logout.do" aria-expanded="true">
           <button class="ti-hand-stop"></button>
        </a>
        </div>    --%>

</div>        
        <%-- sidebar menu area end --%>
        <%-- main content area start --%>
        <div class="main-content">
            <%-- header area start --%>
            <div class="header-area">
                <div class="row align-items-center">
                    <%-- nav and search button --%>
                    <%--<div class="col-12 clearfix">--%>
                    <%--<div class="nav-btn pull-left">--%>
                    <%--<span></span>--%>
                    <%--<span></span>--%>
                    <%--<span></span>--%>
                    <%--</div>--%>
                    <%--</div>--%>
                </div>
            </div>
            <!-- page title area start -->
            <div class="page-title-area">
                <div class="row align-items-center">
                    <div class="col-sm-6 mt-lg-3 mb-lg-3">
                        <h4 class="page-title pull-left"><sitemesh:write property='title'/></h4>
                    </div>
                </div>
            </div>
            <!-- page title area end -->
            <div class="main-content-inner pt-5 pb-5">
                <!-- market value area start -->
                <div class="row">
                    <%--Decorator Body Area Start--%>
                    <sitemesh:write property='body'/>
                    <%--Decorator Body Area Area End--%>
                </div>
                <!-- market value area end -->
            </div>
        </div>
        <%-- main content area end --%>
        <%-- footer area start--%>
        <footer>
            <div class="footer-area">
                <p>© Copyright 2019. HoWooAccount All right reserved.</a>.
                </p>
            </div>
        </footer>
        <%-- footer area end--%>
    </div>
    <%-- page container area end --%>
</body>

</html>
