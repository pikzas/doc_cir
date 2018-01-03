<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="zh">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>文件流转管理系统</title>
<!-- bootstrap - css -->
<link href="<c:url value="/skin/back/BJUI/themes/css/bootstrap.css"/>" rel="stylesheet">
<!-- core - css -->
<link href='<c:url value="/skin/back/BJUI/themes/css/style.css"/>' rel="stylesheet">
<link href="<c:url value="/skin/back/BJUI/themes/blue/core.css"/>" id="bjui-link-theme" rel="stylesheet">
<!-- plug - css -->
<link href="<c:url value="/skin/back/BJUI/plugins/kindeditor_4.1.10/themes/default/default.css"/>" rel="stylesheet">
<link href="<c:url value="/skin/back/BJUI/plugins/colorpicker/css/bootstrap-colorpicker.min.css"/>" rel="stylesheet">
<link href="<c:url value="/skin/back/BJUI/plugins/niceValidator/jquery.validator.css"/>" rel="stylesheet">
<link href="<c:url value="/skin/back/BJUI/plugins/bootstrapSelect/bootstrap-select.css"/>" rel="stylesheet">
<link href="<c:url value="/skin/back/BJUI/themes/css/FA/css/font-awesome.min.css"/>" rel="stylesheet">

<link href="<c:url value="/skin/back/bootstrap/css/fullcalendar.css"/>" rel="stylesheet">
<link href="<c:url value="/skin/back/fullcalendar/css/fullcalendar.print.css"/>" rel="stylesheet" media='print'>
<link href="<c:url value="/skin/back/fullcalendar/css/theme.css"/>" rel="stylesheet" >

<!--[if lte IE 7]>
<link href="<c:url value="/skin/back/BJUI/themes/css/ie7.css"/>" rel="stylesheet">
<![endif]-->
<!--[if lte IE 9]>
<script src="<c:url value="/skin/back/BJUI/other/html5shiv.min.js"/>"></script>
<script src="<c:url value="/skin/back/BJUI/other/respond.min.js"/>"></script>
<![endif]-->
<!-- jquery -->
<script src="<c:url value="/skin/back/BJUI/js/jquery-1.7.2.min.js"/>"></script>
<script src="<c:url value="/skin/back/BJUI/js/jquery.cookie.js"/>"></script>
<%-- fullcalendar --%>
<script src="<c:url value="/skin/back/fullcalendar/js/jquery-ui-1.10.2.custom.min.js"/>"></script>
<script src="<c:url value="/skin/back/fullcalendar/js/moment.min.js"/>"></script>
<script src="<c:url value="/static/js/fullcalendar.min.js"/>"></script>
<!--[if lte IE 9]>
<script src="<c:url value="/skin/back/BJUI/other/jquery.iframe-transport.js"/>"></script>    
<![endif]-->
<!-- BJUI.all 分模块压缩版 -->
<script src='<c:url value="/skin/back/BJUI/js/bjui-all.js"/>'></script>
<!-- 以下是B-JUI的分模块未压缩版，建议开发调试阶段使用下面的版本 -->
<!--
<script src="BJUI/js/bjui-core.js"></script>
<script src="BJUI/js/bjui-regional.zh-CN.js"></script>
<script src="BJUI/js/bjui-frag.js"></script>
<script src="BJUI/js/bjui-extends.js"></script>
<script src="BJUI/js/bjui-basedrag.js"></script>
<script src="BJUI/js/bjui-slidebar.js"></script>
<script src="BJUI/js/bjui-contextmenu.js"></script>
<script src="BJUI/js/bjui-navtab.js"></script>
<script src="BJUI/js/bjui-dialog.js"></script>
<script src="BJUI/js/bjui-taskbar.js"></script>
<script src="BJUI/js/bjui-ajax.js"></script>
<script src="BJUI/js/bjui-alertmsg.js"></script>
<script src="BJUI/js/bjui-pagination.js"></script>
<script src="BJUI/js/bjui-util.date.js"></script>
<script src="BJUI/js/bjui-datepicker.js"></script>
<script src="BJUI/js/bjui-ajaxtab.js"></script>
<script src="BJUI/js/bjui-datagrid.js"></script>
<script src="BJUI/js/bjui-tablefixed.js"></script>
<script src="BJUI/js/bjui-tabledit.js"></script>
<script src="BJUI/js/bjui-spinner.js"></script>
<script src="BJUI/js/bjui-lookup.js"></script>
<script src="BJUI/js/bjui-tags.js"></script>
<script src="BJUI/js/bjui-upload.js"></script>
<script src="BJUI/js/bjui-theme.js"></script>
<script src="BJUI/js/bjui-initui.js"></script>
<script src="BJUI/js/bjui-plugins.js"></script>
-->
<!-- plugins -->
<!-- swfupload for uploadify && kindeditor -->
<script src='<c:url value="/skin/back/BJUI/plugins/swfupload/swfupload.js"/>'></script>
<!-- kindeditor -->
<script src="<c:url value="/skin/back/BJUI/plugins/kindeditor_4.1.10/kindeditor-all.min.js"/>"></script>
<script src="<c:url value="/skin/back/BJUI/plugins/kindeditor_4.1.10/lang/zh_CN.js"/>"></script>
<!-- colorpicker -->
<script src="<c:url value="/skin/back/BJUI/plugins/colorpicker/js/bootstrap-colorpicker.min.js"/>"></script>
<!-- ztree -->
<script src="<c:url value="/skin/back/BJUI/plugins/ztree/jquery.ztree.all-3.5.js"/>"></script>
<!-- nice validate -->
<script src="<c:url value="/skin/back/BJUI/plugins/niceValidator/jquery.validator.js"/>"></script>
<script src="<c:url value="/skin/back/BJUI/plugins/niceValidator/jquery.validator.themes.js"/>"></script>
<!-- bootstrap plugins -->
<script src="<c:url value="/skin/back/BJUI/plugins/bootstrap.min.js"/>"></script>
<script src="<c:url value="/skin/back/BJUI/plugins/bootstrapSelect/bootstrap-select.min.js"/>"></script>
<script src="<c:url value="/skin/back/BJUI/plugins/bootstrapSelect/defaults-zh_CN.min.js"/>"></script>
<!-- icheck -->
<script src="<c:url value="/skin/back/BJUI/plugins/icheck/icheck.min.js"/>"></script>
<!-- dragsort -->
<script src="<c:url value="/skin/back/BJUI/plugins/dragsort/jquery.dragsort-0.5.1.min.js"/>"></script>
<!-- HighCharts -->
<script src="<c:url value="/skin/back/BJUI/plugins/highcharts/highcharts.js"/>"></script>
<script src="<c:url value="/skin/back/BJUI/plugins/highcharts/highcharts-3d.js"/>"></script>
<script src="<c:url value="/skin/back/BJUI/plugins/highcharts/themes/gray.js"/>"></script>
<!-- ECharts -->
<script src="<c:url value="/skin/back/BJUI/plugins/echarts/echarts.js"/>"></script>
<!-- other plugins -->
<script src="<c:url value="/skin/back/BJUI/plugins/other/jquery.autosize.js"/>"></script>
<link href="<c:url value="/skin/back/BJUI/plugins/uploadify/css/uploadify.css"/>" rel="stylesheet">
<script src="<c:url value="/skin/back/BJUI/plugins/uploadify/scripts/jquery.uploadify.min.js"/>"></script>
<script src="<c:url value="/skin/back/BJUI/plugins/download/jquery.fileDownload.js"/>"></script>
<!-- init -->
<script type="text/javascript">
$(function() {
    BJUI.init({
      
        loginInfo    : {url:'<c:url value="/login_timeout.jsp"/>', title:'登录', width:400, height:200}, // 会话超时后弹出登录对话框
        statusCode   : {ok:200, error:300, timeout:301}, //[可选]
        ajaxTimeout  : 50000, //[可选]全局Ajax请求超时时间(毫秒)
        pageInfo     : {total:'total', pageCurrent:'pageCurrent', pageSize:'pageSize', orderField:'orderField', orderDirection:'orderDirection'}, //[可选]分页参数
        alertMsg     : {displayPosition:'topcenter', displayMode:'slide', alertTimeout:3000}, //[可选]信息提示的显示位置，显隐方式，及[info/correct]方式时自动关闭延时(毫秒)
        keys         : {statusCode:'statusCode', message:'message'}, //[可选]
        ui           : {
                         windowWidth      : 0, //框架显示宽度，0=100%宽，> 600为则居中显示
                         showSlidebar     : true, //[可选]左侧导航栏锁定/隐藏
                         clientPaging     : true, //[可选]是否在客户端响应分页及排序参数
                         overwriteHomeTab : false //[可选]当打开一个未定义id的navtab时，是否可以覆盖主navtab(我的主页)
                       },
        debug        : true,    // [可选]调试模式 [true|false，默认false]
        theme        : 'sky' // 若有Cookie['bjui_theme'],优先选择Cookie['bjui_theme']。皮肤[五种皮肤:default, orange, purple, blue, red, green]
    })


    // main - menu
    $('#bjui-accordionmenu')
        .collapse()
        .on('hidden.bs.collapse', function(e) {
            $(this).find('> .panel > .panel-heading').each(function() {
                var $heading = $(this), $a = $heading.find('> h4 > a')
                
                if ($a.hasClass('collapsed')) $heading.removeClass('active')
            })
        })
        .on('shown.bs.collapse', function (e) {
            $(this).find('> .panel > .panel-heading').each(function() {
                var $heading = $(this), $a = $heading.find('> h4 > a')
                
                if (!$a.hasClass('collapsed')) $heading.addClass('active')
            })
        })
        
    $(document).on('click', 'ul.menu-items li > a', function(e) {
        var $a = $(this), $li = $a.parent(), options = $a.data('options').toObj(), $children = $li.find('> .menu-items-children')
        var onClose = function() {
            $li.removeClass('active')
        }
        var onSwitch = function() {
            $('#bjui-accordionmenu').find('ul.menu-items li').removeClass('switch')
            $li.addClass('switch')
        }
        
        $li.addClass('active')
        if (options) {
            options.url      = $a.attr('href')
            options.onClose  = onClose
            options.onSwitch = onSwitch
            if (!options.title) options.title = $a.text()
            
            if (!options.target)
                $a.navtab(options)
            else
                $a.dialog(options)
        }
        if ($children.length) {
            $li.toggleClass('open')
        }
        
        e.preventDefault()
    })
    
    //时钟
    var today = new Date(), time = today.getTime()
    $('#bjui-date').html(today.formatDate('yyyy/MM/dd'));
    //humin add time
    var td = new Date();
    var tdat = new Date();
    var tdat1 = new Date();
    var tdat2 = new Date();
    var tomorrow = new Date(td.setTime(td.getTime()+1*24*60*60*1000));
    var the_day_after_tomorrow = new Date(tdat.setTime(tdat.getTime()+2*24*60*60*1000));
    var next_the_day_after_tomorrow = new Date(tdat1.setTime(tdat1.getTime()+3*24*60*60*1000));
    var double_next_the_day_after_tomorrow = new Date(tdat2.setTime(tdat2.getTime()+4*24*60*60*1000));
    $('#hm-today').html(today.formatDate('yyyy/MM/dd'));
    $('#hm-tomorrow').html(tomorrow.formatDate('yyyy/MM/dd'));
    $('#hm-the-day-after-tomorrow').html(the_day_after_tomorrow.formatDate('yyyy/MM/dd'));
    $('#hm-next-the-day-after-tomorrow').html(next_the_day_after_tomorrow.formatDate('yyyy/MM/dd'));
    $('#hm-double-next-the-day-after-tomorrow').html(double_next_the_day_after_tomorrow.formatDate('yyyy/MM/dd'));
    setInterval(function() {
        today = new Date(today.setSeconds(today.getSeconds() + 1))
        $('#bjui-clock').html(today.formatDate('HH:mm:ss'))
    }, 1000)
})

//菜单-事件
function MainMenuClick(event, treeId, treeNode) {
    event.preventDefault()
    
    if (treeNode.isParent) {
        var zTree = $.fn.zTree.getZTreeObj(treeId)
        
        zTree.expandNode(treeNode, !treeNode.open, false, true, true)
        return
    }
    
    if (treeNode.target && treeNode.target == 'dialog')
        $(event.target).dialog({id:treeNode.tabid, url:treeNode.url, title:treeNode.name})
    else
        $(event.target).navtab({id:treeNode.tabid, url:treeNode.url, title:treeNode.name, fresh:treeNode.fresh, external:treeNode.external})
}
</script>

<!-- for doc begin -->
<%-- <link type="text/css" rel="stylesheet" href="<c:url value="/skin/back/BJUI/js/syntaxhighlighter-2.1.382/styles/shCore.css"/>"/>
<link type="text/css" rel="stylesheet" href='<c:url value="/skin/back/BJUI/js/syntaxhighlighter-2.1.382/styles/shThemeEclipse.css"/>'/>
<script type="text/javascript" src="<c:url value="/skin/back/BJUI/js/syntaxhighlighter-2.1.382/scripts/brush.js"/>"></script>
<script type="text/javascript">
$(function(){
    SyntaxHighlighter.config.clipboardSwf = '<c:url value="/skin/back/BJUI/js/syntaxhighlighter-2.1.382/scripts/clipboard.swf"/>'
    $(document).on(BJUI.eventType.initUI, function(e) {
        SyntaxHighlighter.highlight();
    })
})
</script> --%>
<!-- for doc end -->
</head>
<body>
    <!--[if lte IE 7]>
        <div id="errorie"><div>您还在使用老掉牙的IE，正常使用系统前请升级您的浏览器到 IE8以上版本 <a target="_blank" href="http://windows.microsoft.com/zh-cn/internet-explorer/ie-8-worldwide-languages">点击升级</a>&nbsp;&nbsp;强烈建议您更改换浏览器：<a href="http://down.tech.sina.com.cn/content/40975.html" target="_blank">谷歌 Chrome</a></div></div>
    <![endif]-->
    <div id="bjui-window" >
    <header id="bjui-header">
        <div class="bjui-navbar-header">
            <button type="button" class="bjui-navbar-toggle btn-default" data-toggle="collapse" data-target="#bjui-navbar-collapse">
                <i class="fa fa-bars"></i>
            </button>
            <%--<a class="bjui-navbar-logo" href="#"><img src="/images/logo.png"></a>
        --%></div>
        <nav id="bjui-navbar-collapse">
            <ul class="bjui-navbar-right">
                <li class="datetime"><div><span id="bjui-date"></span> <span id="bjui-clock"></span></div></li>
                <li><a href="#"><font color="red">Hi</font> ${admin.relName}<span class="badge"></span></a></li>
                <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">我的账户 <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="<c:url value="/back/system/saveAdminAuditForm.do"/>" data-toggle="dialog" data-width="800" data-height="400">&nbsp;<span class="glyphicon glyphicon-user"></span> 我的资料</a></li>
                        <li class="divider"></li>
                        <li><a href="<c:url value="/system/logout.do"/>" class="red">&nbsp;<span class="glyphicon glyphicon-off"></span> 注销登陆</a></li>
                    </ul>
                </li>
                <li class="dropdown"><a href="#" class="dropdown-toggle theme blue" data-toggle="dropdown" title="切换皮肤"><i class="fa fa-tree"></i></a>
                    <ul class="dropdown-menu" role="menu" id="bjui-themes">
                        <li><a href="javascript:;" class="theme_default" data-toggle="theme" data-theme="default">&nbsp;<i class="fa fa-tree"></i> 黑白分明&nbsp;&nbsp;</a></li>
                        <li><a href="javascript:;" class="theme_orange" data-toggle="theme" data-theme="orange">&nbsp;<i class="fa fa-tree"></i> 橘子红了</a></li>
                        <li class="active"><a href="javascript:;" class="theme_purple" data-toggle="theme" data-theme="purple">&nbsp;<i class="fa fa-tree"></i> 紫罗兰</a></li>
                        <li><a href="javascript:;" class="theme_blue" data-toggle="theme" data-theme="blue">&nbsp;<i class="fa fa-tree"></i> 天空蓝</a></li>
                        <li><a href="javascript:;" class="theme_green" data-toggle="theme" data-theme="green">&nbsp;<i class="fa fa-tree"></i> 绿草如茵</a></li>
                    </ul>
                </li>
            </ul>
            <br/>
            <ul class="bjui-navbar-right" style=" position: absolute; right: 30px;  z-index: 100; margin-top: 12px; ">
            	<li><div><span id="hm-today"></span>预期制证:${todayNum}</div></li>
            	<li><div><span id="hm-tomorrow"></span>预期制证:${tomorrowNum}</div></li>
            	<li><div><span id="hm-the-day-after-tomorrow"></span>预期制证:${afterTomorrowNum}</div></li>
            	<li><div><span id="hm-next-the-day-after-tomorrow"></span>预期制证:${nextAfterTomorrowNum}</div></li>
            	<li><div><span id="hm-double-next-the-day-after-tomorrow"></span>预期制证:${doubleNextAfterTomorrowNum}</div></li>
            </ul>
        </nav>
        <div id="bjui-hnav">
            <button type="button" class="btn-default bjui-hnav-more-left" title="导航菜单左移"><i class="fa fa-angle-double-left"></i></button>
            <div id="bjui-hnav-navbar-box">
                <ul id="bjui-hnav-navbar">
                    <li class="active"><a href="javascript:;" data-toggle="slidebar"><i class="fa fa-cog"></i> 系统管理</a>
                        <div class="items hide" data-noinit="true">
                            <ul class="menu-items" data-tit="系统管理" data-faicon="list">
                                <li><a href="<c:url value="/back/system/queryAllAdmin.do"/>" data-options="{id:'form-demo', faicon:'th-large',fresh:true}">系统账号</a></li>
                                <li><a href="<c:url value="/back/softinfo/queryAllSoftwareToStatusList.do"/>" data-options="{id:'software-status', faicon:'th-large',fresh:true}">状态赋权</a></li>
                                <li><a href="<c:url value="/back/softinfo/toRevokeList.do"/>" data-options="{id:'do-revoke', faicon:'th-large',fresh:true}">撤回</a></li>
                                <li><a href="<c:url value="/back/menu/menuList.do"/>" data-options="{id:'menu', faicon:'th-large'}">功能菜单</a></li>
                                <li><a href="<c:url value="/back/role/roleList.do"/>" data-options="{id:'menu', faicon:'th-large'}">角色管理</a></li>
                                <li><a href="<c:url value="/back/role/powerList.do"/>" data-options="{id:'menu', faicon:'th-large'}">权限管理</a></li>
                            </ul>
                        </div>
                    </li>
                    <li><a href="javascript:;" data-toggle="slidebar"><i class="fa fa-check-square-o"></i>入库管理</a>
                     <div class="items hide" data-noinit="true">
                         <ul class="menu-items" data-tit="入库管理" data-faicon="list">
                             <li><a href="<c:url value="/back/softinfo/softInfoAuditList.do"/>" data-options="{id:'import-new', faicon:'th-large', fresh:true,beforeClose: function(){return aa()}}">新入库</a></li>
                             <li><a href="<c:url value="/back/softinfo/backSoftInfoAuditList.do"/>" data-options="{id:'import-backForCertificated', faicon:'th-large', fresh:true,beforeClose: function(){return bb()}}">制证回库</a></li>
                             <li><a href="<c:url value="/back/softinfo/backForException.do"/>" data-options="{id:'import-backForException', faicon:'th-large', fresh:true,beforeClose: function(){return cc()}}">异常回库</a></li>
                          <%-- <li><a href="<c:url value="/back/softinfo/softInfoAuditListHistory.do?history=1"/>" data-options="{id:'history-audit-list', faicon:'th-large', fresh:true}">入库异常记录</a></li>
                             <li><a href="<c:url value="/back/softinfo/softInfoList.do"/>" data-options="{id:'form-demo', faicon:'th-large', fresh:true}">Excel导入导出</a></li> --%>
                         </ul>
                     </div>
                    </li>
                  
                    <li><a href="javascript:;" data-toggle="slidebar"><i class="fa fa-database"></i>在库管理</a>
                     <div class="items hide" data-noinit="true">
                         <ul class="menu-items" data-tit="在库管理" data-faicon="list">
                             <li><a href="<c:url value="/back/warehouse/warehouseIndex.do"/>" data-options="{id:'warehouse-index', faicon:'th-large', fresh:true}">仓库管理</a></li>
                             <li><a href="<c:url value="/back/softinfo/accountingSoftwareList.do"/>" data-options="{id:'accounting-swl', faicon:'th-large', fresh:true}">批量核费</a></li>
                             <li><a href="<c:url value="/back/softinfo/queryAllSoftwareCertificatedateList.do"/>" data-options="{id:'form-demo', faicon:'th-large', fresh:true}">修改出证日期</a></li>
                             <li><a href="<c:url value="/back/softinfo/queryAllSoftware.do"/>" data-options="{id:'form-demo', faicon:'th-large', fresh:true}">案件查询</a></li>
                             <li><a href="<c:url value="/back/warehouse/findWarehouse.do"/>" data-options="{id:'form-demo', faicon:'th-large', fresh:true}">仓位查询</a></li>
                             <li><a href="<c:url value="/back/softinfo/correctingSoftwareList.do"/>" data-options="{id:'accounting-swl', faicon:'th-large', fresh:true}">批量补正</a></li>
                             <li><a href="<c:url value="/back/warehouse/unlockStorageList.do"/>" data-options="{id:'form-demo', faicon:'th-large', fresh:true}">仓库解锁</a></li>
                             <%--
                             <li><a href="<c:url value="/back/softinfo/updateCertificatedateForm.do"/>" data-options="{id:'form-demo', faicon:'th-large', fresh:true}">修改出证日期</a></li>
                             --%>
                         </ul>
                     </div>
                    </li>
                    
                    <li><a href="javascript:;" data-toggle="slidebar"><i class="fa fa-plane"></i>出库管理</a>
                        <div class="items hide" data-noinit="true">
                            <ul class="menu-items" data-tit="出库管理" data-faicon="table">
                             <li><a href="<c:url value="/back/checkout/certificatedSoftwareCheckout.do"/>" data-options="{id:'export-certificate', faicon:'th-large', fresh:true}">制证出库</a></li>
                             <li><a href="<c:url value="/back/softinfo/fenshen.do"/>" data-options="{id:'export-audit', faicon:'th-large', fresh:true}">分审出库</a></li>
                             <li><a href="<c:url value="/back/checkout/exceptionCheckout.do"/>" data-options="{id:'exception-checkout', faicon:'th-large', fresh:true}">异常出库</a></li>
                            </ul>
                        </div>
                    </li>

                    <li><a href="javascript:;" data-toggle="slidebar"><i class="fa fa-table"></i>数据统计</a>
                        <div class="items hide" data-noinit="true">
                            <ul class="menu-items" data-tit="出库管理" data-faicon="list">
                                <li><a href="<c:url value="/back/statistic/hexiao.do"/>" data-options="{id:'statistic-checkin', faicon:'th-large', fresh:true}">入库核销</a></li>
                                <li><a href="<c:url value="/back/statistic/audit.do"/>" data-options="{id:'statistic-audit', faicon:'th-large', fresh:true}">分审统计</a></li>
                            </ul>
                        </div>
                    </li>
                </ul>
            </div>
            <button type="button" class="btn-default bjui-hnav-more-right" title="导航菜单右移"><i class="fa fa-angle-double-right"></i></button>
        </div>
    </header>
    <div id="bjui-container" class="clearfix">
        <div id="bjui-leftside">
            <div id="bjui-sidebar-s">
                <div class="collapse"></div>
            </div>
            <div id="bjui-sidebar">
                <div class="toggleCollapse"><h2><i class="fa fa-bars"></i> 导航栏 <i class="fa fa-bars"></i></h2><a href="javascript:;" class="lock"><i class="fa fa-lock"></i></a></div>
                <div class="panel-group panel-main" data-toggle="accordion" id="bjui-accordionmenu">
                </div>
            </div>
        </div>
        <div id="bjui-navtab" class="tabsPage">
            <div class="tabsPageHeader">
                <div class="tabsPageHeaderContent">
                    <ul class="navtab-tab nav nav-tabs">
                        <li data-url="<c:url value="/system/default.do"/>" data-faicon="home"><a href="javascript:;"><span><i class="fa fa-home"></i> #maintab#</span></a></li>
                    </ul>
                </div>
                <div class="tabsLeft"><i class="fa fa-angle-double-left"></i></div>
                <div class="tabsRight"><i class="fa fa-angle-double-right"></i></div>
                <div class="tabsMore"><i class="fa fa-angle-double-down"></i></div>
            </div>
            <ul class="tabsMoreList">
                <li><a href="javascript:;">#maintab#</a></li>
            </ul>
            <div class="navtab-panel tabsPageContent">
                <div class="navtabPage unitBox">
                    <div class="bjui-pageContent" style="background:#FFF;">
                        Loading...
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
