<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/taglib.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>异常出库</title>
<div class="bjui-pageHeader">
	<div style="display: inline-block;margin:5px;">
		<lable><strong class="color_ok">成功量：</strong></lable>
		<span><strong id="exc_count_ok">0</strong></span>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable><strong class="color_fail">异常量：</strong></lable>
		<span><strong id="exc_count_error">0</strong></span>
	</div>
	<br/>
	<lable>请扫描流水号：</lable>
	<input type="text" id="serialnum_exc_checkout" data-toggle="input" size="15" aria-required="true" /> 
	<button id="to_confirm_exc_checkout"class="btn-green" data-icon="check">确认</button>
		
</div>
<div class="bjui-pageContent tableContent">
	<form id="j_custom_form_exc_checkout" class="pageForm">
		<table
			class="table table-bordered table-hover table-striped table-top"
			data-toggle="tabledit">
			<thead>
				<tr>
					<th title="流水号"></th>
					<th title="软件全称"></th>
					<th title="申请人"></th>
					<th title="制证日期"></th>
					<th title="业务员"></th>
					<th title="审查员"></th>
					<th title="匹配状态"></th>
				</tr>
			</thead>
			<tbody id="mainbody_exc_checkout">
			</tbody>
		</table>
	</form>
</div>
<script type="text/javascript">
	$("#to_confirm_exc_checkout").click(function() {
		preCheckIn();
	});

	$("#serialnum_exc_checkout").bind('keydown', function(event) {
		if (event.keyCode == 13) {
			preCheckIn();
		}
	});

	function preCheckIn() {
		var serialnum_exc_checkout = $("#serialnum_exc_checkout").val();
		if(serialnum_exc_checkout.length!=14){
			$(this).alertmsg('info', '请输入正确的流水号！',{alertTimeout:800});
			$("#serialnum_exc_checkout").val("").focus();
			return;
		}
		$.ajax({
			url : "<c:url value='/back/checkout/toExceptionCheckout.do'/>",
			data : {
				"serialNum" : serialnum_exc_checkout
			},
			type : "POST",
			success : function(data) {
				var ret = eval('(' + data.data + ')');
				var people;
                try
                {
                    if(ret.trialAdmin.relName != null || ret.trialAdmin.relName != "" || ret.trialAdmin.relName != undefined ){
                        people = ret.trialAdmin.relName;
                    }
                }
                catch (e){
                    people = "";
                }
				if (data.state==false) {
					var content = "<tr><td>" + ret.serialnum + "</td><td>"
							+ ret.softwarename + "</td><td>" + ret.applyperson
							+ "</td><td>" + ret.certificatedate + "</td><td>"
							+ ret.salesman + "</td><td>"
							+ people
							+"</td><td style='color:red'>" + data.msg
							+ "</td></tr>";
					$("#mainbody_exc_checkout").prepend(content);
					var errorval = $("#exc_count_error").text();
					$("#exc_count_error").html(parseInt(errorval)+1);
					$("#serialnum_exc_checkout").val("").focus();
				} else {
					var content = "<tr><td>" + ret.serialnum + "</td><td>"
							+ ret.softwarename + "</td><td>" + ret.applyperson
							+ "</td><td>" + ret.certificatedate + "</td><td>"
							+ ret.salesman + "</td><td>"
							+ people
							+"</td><td style='color:green'>" + data.msg
							+ "</td></tr>";
					$("#mainbody_exc_checkout").prepend(content);
					var okval = $("#exc_count_ok").text();
					$("#exc_count_ok").html(parseInt(okval)+1);
					$("#serialnum_exc_checkout").val("").focus();
				}
			}
		});
	}
</script>
</html>
