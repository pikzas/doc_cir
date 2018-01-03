<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/include/back_authority.jsp"%>
<!DOCTYPE html>
<html lang="en">

<head>
<title>文件流转管理系统</title>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="<c:url value="static/login/bootstrap.min.css"/>" />
<link rel="stylesheet" href="<c:url value="static/login/css/camera.css"/>" />
<link rel="stylesheet" href="<c:url value="static/login/bootstrap-responsive.min.css"/>" />
<link rel="stylesheet" href="<c:url value="static/login/matrix-login.css"/>" />
<link href="<c:url value="static/login/font-awesome.css"/>" rel="stylesheet" />
<script type="text/javascript" src="<c:url value="static/js/jquery-1.5.1.min.js"/>"></script>

<Script Language="JScript">
    try {
        document.writeln("<OBJECT classid=\"clsid:FCAA4851-9E71-4BFE-8E55-212B5373F040\" height=1 id=oSeccom  style=\"HEIGHT: 1px; LEFT: 10px; TOP: 28px; WIDTH: 1px\" width=1 VIEWASTEXT>");
        document.writeln("</OBJECT>");
    }
    catch (e) {
        alert("组件不存在或者未注册！" + e.message());
    }
</Script>
<Script Language="JScript" for=window event=OnLoad>
    function GetUserList() {
        var objListID = document.getElementById("keyname1");
        strTemp_pnp = oSeccom.GetUserList();
        while (1) {
            i = strTemp_pnp.indexOf("&&&");
            if (i <= 0) {
                break;
            }
            strOption = strTemp_pnp.substring(0, i);
            strName = strOption.substring(0, strOption.indexOf("||"));
            strUniqueID = strOption.substring(strOption.indexOf("||") + 2, strOption.length);
            $("#keyid").val(strUniqueID);
            $("#keyname").val(strName);
            
            if(strUniqueID!=""){
            	$("#keyidshow").show();
            	$("#username").hide();
            	$("#loginname").val(strName);
            }
            
            var objItem = new Option(strName, strUniqueID)
            objListID.add(objItem);
            len = strTemp_pnp.length;
            strTemp_pnp = strTemp_pnp.substring(i + 3, len);
        }
    }
    GetUserList();
</Script>
<SCRIPT FOR=oSeccom EVENT=OnUsbkeyChange LANGUAGE=javascript>
    function GetList_pnp() {
        var i;
        var strOption;
        var strName;
        var strUniqueID;
        var objListID = document.getElementById("keyname1");
        var n = objListID.length;

        for (i = 0; i < n; i++)
            objListID.remove(0);

        var strTemp = oSeccom.GetUserListPnp();
       
        while (1) {
            i = strTemp.indexOf("&&&");
            if (i <= 0) {
                break;
            }
            strOption = strTemp.substring(0, i);
            strName = strOption.substring(0, strOption.indexOf("||"));
            strUniqueID = strOption.substring(strOption.indexOf("||") + 2, strOption.length);
            $("#keyid").val(strUniqueID);
            $("#keyname").val(strName);
            var objItem = new Option(strName, strUniqueID)
            objListID.add(objItem);
            len = strTemp.length;
            strTemp = strTemp.substring(i + 3, len);
        }
    }
    function OnUsbkeyChange() {
        GetList_pnp();
    }

    OnUsbkeyChange();
</SCRIPT>
</head>
<body>

	<div style="width:100%;text-align: center;margin: 0 auto;position: absolute;">
		<div id="loginbox">
			<form action="<c:url value='/system/login.do'/>" method="post" name="loginForm" id="loginForm">
			<input type="hidden" id="keyid" name="keyid"/>
			<input type="hidden" id="keyname" name="keyname"/>
				<div class="control-group normal_text">
					<h3>
						<img src="<c:url value="static/login/logo.png"/>" alt="Logo" />
					</h3>
				</div>
				<div class="control-group" id="username">
					<div class="controls">
						<div class="main_input_box">
							<span class="add-on bg_lg"><i><img height="37"
									src="<c:url value="static/login/user.png"/>" /></i></span><input type="text"
								name="j_username" id="loginname" value=""
								placeholder="请输入用户名" />
						</div>
					</div>
				</div>
				<div id="keyidshow" class="control-group" style="display: none;">
					<div class="controls">
						<div class="main_input_box">
							<span class="add-on bg_lg"><i>
								<img height="37"src="<c:url value="static/login/user.png"/>" /></i></span>
								<select id="keyname1" name="keyname1" style="width: 306px"></select>
						</div>
					</div>
				</div>
				<div class="control-group">
					<div class="controls">
						<div class="main_input_box">
							<span class="add-on bg_ly"><i><img height="37"
									src="<c:url value="static/login/suo.png"/>" /></i></span><input type="password"
								name="keypassword" id="keypassword" placeholder="请输入证书密码"
								value="" />
						</div>
					</div>
				</div>
				<div style="float:right;padding-right:10%;">
					<div style="float: left;margin-top:3px;margin-right:2px;">
						<font color="white">记住密码</font>
					</div>
					<div style="float: left;">
						<input name="form-field-checkbox" id="saveid" type="checkbox"
							onclick="savePaw();" style="padding-top:0px;" />
					</div>
				</div>
				<div class="form-actions">
					<div style="width:86%;padding-left:8%;">
						<!-- 
						<div style="float: left;">
							<i><img src="<c:url value="static/login/yan.png"/>" /></i>
						</div>
						<div style="float: left;" class="codediv">
							<input type="text" name="verifycode" id="code" class="login_code"
								style="height:16px; padding-top:0px;" />
						</div>
						<div style="float: left;">
							<i><img style="height:22px;" id="codeImg" alt="点击更换"
								title="点击更换" src="" /></i>
						</div>
						 -->
						<span class="pull-right" style="padding-right:3%;"><a
							href="javascript:quxiao();" class="btn btn-success">取消</a></span> <span
							class="pull-right"><a onclick="severCheck();"
							class="flip-link btn btn-info" id="to-recover">登录</a></span>

					</div>
				</div>

			</form>


			<div class="controls">
				<div class="main_input_box">
					<font color="white"><span id="nameerr">Copyright © ECopyright 2015</span></font>
				</div>
			</div>
		</div>
	</div>
	<div id="templatemo_banner_slide" class="container_wapper">
		<div class="camera_wrap camera_emboss" id="camera_slide">
			<div data-src="<c:url value="static/login/images/banner_slide_01.jpg"/>"></div>
			<div data-src="<c:url value="static/login/images/banner_slide_02.jpg"/>"></div>
			<div data-src="<c:url value="static/login/images/banner_slide_03.jpg"/>"></div>
		</div>
		<!-- #camera_wrap_3 -->
	</div>

	<script type="text/javascript">
	<c:if test='${not empty sessionScope.flag}'>
    <c:if test="${flag eq 'passwordError'}">
    	alert('用户名或密码错误');
    </c:if>
     <c:if test="${flag eq 'invalidname'}">
     	alert('该用户名已经锁定');
    </c:if>
    <c:if test="${flag eq 'codeError'}">
	    alert('验证码错误');
    </c:if>
    <c:remove var='rs' scope='session'/>
     <c:remove var='flag' scope='session'/>
</c:if>
		
		//服务器校验
		function severCheck(){
			if(check()){
			$("#loginForm").submit();
			/* 	var loginname = $("#loginname").val();
				var password = $("#password").val();
				var code = "qq313596790fh"+loginname+",fh,"+password+"QQ978336446fh"+",fh,"+$("#code").val();
				$.ajax({
					type: "POST",
					url: 'login_login',
			    	data: {KEYDATA:code,tm:new Date().getTime()},
					dataType:'json',
					cache: false,
					success: function(data){
						if("success" == data.result){
							saveCookie();
							window.location.href="main/index";
						}else if("usererror" == data.result){
							$("#loginname").tips({
								side : 1,
								msg : "用户名或密码有误",
								bg : '#FF5080',
								time : 15
							});
							$("#loginname").focus();
						}else if("codeerror" == data.result){
							$("#code").tips({
								side : 1,
								msg : "验证码输入有误",
								bg : '#FF5080',
								time : 15
							});
							$("#code").focus();
						}else{
							$("#loginname").tips({
								side : 1,
								msg : "缺少参数",
								bg : '#FF5080',
								time : 15
							});
							$("#loginname").focus();
						}
					}
				}); */
			}
		}
	
		$(document).ready(function() {
			changeCode();
			$("#codeImg").bind("click", changeCode);
		});

		$(document).keyup(function(event) {
			if (event.keyCode == 13) {
				$("#to-recover").trigger("click");
			}
		});

		function genTimestamp() {
			var time = new Date();
			return time.getTime();
		}

		function changeCode() {
			$("#codeImg").attr("src", "<c:url value='/code'/>?t=" + genTimestamp());
		}
		
		//客户端校验
		function check() {
			//证书登录校验   便于测试开放用户名密码登录 正式只有key登录
			

			var keyid = document.getElementById("keyid").value;
			if (keyid != "") {
				if (!checkCert()) {
					return false;
				}
			}
			if ($("#loginname").val() == "") {

				$("#loginname").tips({
					side : 2,
					msg : '用户名不得为空',
					bg : '#AE81FF',
					time : 3
				});

				$("#loginname").focus();
				return false;
			} else {
				$("#loginname").val(jQuery.trim($('#loginname').val()));
			}

			if ($("#password").val() == "") {

				$("#password").tips({
					side : 2,
					msg : '密码不得为空',
					bg : '#AE81FF',
					time : 3
				});

				$("#password").focus();
				return false;
			}
			if ($("#code").val() == "") {

				$("#code").tips({
					side : 1,
					msg : '验证码不得为空',
					bg : '#AE81FF',
					time : 3
				});

				$("#code").focus();
				return false;
			}

			$("#loginbox").tips({
				side : 1,
				msg : '正在登录 , 请稍后 ...',
				bg : '#68B500',
				time : 10
			});

			return true;
		}

		function checkCert() {
			var keyname = document.getElementById("keyname1").value;
			var keypassword = document.getElementById("keypassword").value;
			//var keyid = document.getElementById("keyid").value;
			var ret = oSeccom.userLogin(keyname, keypassword);
			//var cert =oSeccom.ExportUserCert(strCertID);
			//var cert2 =oSeccom.GetUserListPnp();
			//var cert3 =oSeccom.GetUserList();
			//alert(keyname);
			if (!ret) {
				alert("证书密码错误！");
				return false;
			}
			return true;
		}

		function savePaw() {
			if (!$("#saveid").attr("checked")) {
				$.cookie('loginname', '', {
					expires : -1
				});
				$.cookie('password', '', {
					expires : -1
				});
				$("#loginname").val('');
				$("#password").val('');
			}
		}

		function saveCookie() {
			if ($("#saveid").attr("checked")) {
				$.cookie('loginname', $("#loginname").val(), {
					expires : 7
				});
				$.cookie('password', $("#password").val(), {
					expires : 7
				});
			}
		}
		function quxiao() {
			$("#loginname").val('');
			$("#password").val('');
		}

		jQuery(function() {
			var loginname = $.cookie('loginname');
			var password = $.cookie('password');
			if (typeof (loginname) != "undefined" && typeof (password) != "undefined") {
				$("#loginname").val(loginname);
				$("#password").val(password);
				$("#saveid").attr("checked", true);
				$("#code").focus();
			}
		});
	</script>
	<script>
		//TOCMAT重启之后 点击左侧列表跳转登录首页 
		if (window != top) {
			top.location.href = location.href;
		}
		$.cookie('menusf', 'ok');
		$("#sidebar").attr("class","");
	</script>

	<script src="<c:url value="static/js/bootstrap.min.js"/>"></script>
	<script src="<c:url value="static/js/jquery-1.7.2.js"/>"></script>
	<script src="<c:url value="static/login/js/jquery.easing.1.3.js"/>"></script>
	<script src="<c:url value="static/login/js/jquery.mobile.customized.min.js"/>"></script>
	<script src="<c:url value="static/login/js/camera.min.js"/>"></script>
	<script src="<c:url value="static/login/js/templatemo_script.js"/>"></script>
	<script type="text/javascript" src="<c:url value="static/js/jquery.tips.js"/>"></script>
	<script type="text/javascript" src="<c:url value="static/js/jquery.cookie.js"/>"></script>
</body>

</html>