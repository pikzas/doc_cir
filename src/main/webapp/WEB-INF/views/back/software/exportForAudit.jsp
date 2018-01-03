<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/taglib.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>分审出库</title>
<div class="bjui-pageHeader">
	<div style="display: inline-block;margin:5px;">
		<lable><strong class="color_ok">成功量：</strong></lable>
		<span><strong id="audit_count_ok">0</strong></span>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable><strong class="color_fail">异常量：</strong></lable>
		<span><strong id="audit_count_error">0</strong></span>
	</div>
	<br/>
	<lable>审查员：</lable>
	<select id="auditor" name="auditor" data-toggle="selectpicker">
		<option value="">请选择审查员</option>
		<c:forEach items="${records}" var="opt">
			<option value="${opt.key }">${opt.value }</option>
		</c:forEach>
	</select>
	<div style="display:inline-block; ">
		<lable>出证日期开始时间：</lable>
		<input type="text" name="publishDate_start" id="startDate"
			data-toggle="datepicker" size="15" aria-required="true"
			readonly="true" />
		<lable>结束时间：</lable>
		<input type="text" name="publishDate_end" id="endDate"
			data-toggle="datepicker" size="15" aria-required="true"
			readonly="true" />
	</div>
	<lable>请扫描流水号：</lable>
	<input type="text" id="serialnum_auditor" data-toggle="input" size="15" aria-required="true" /> 
	<button id="to_confirm_auditor"class="btn-green" data-icon="check">确认</button>
		
</div>
<div class="bjui-pageContent tableContent">
	<form id="j_custom_form_ruku" class="pageForm">
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
			<tbody id="mainbody_auditor">
			</tbody>
		</table>
	</form>
</div>
<script type="text/javascript">
	$("#to_confirm_auditor").click(function() {
		preCheckIn();
	});

	$("#serialnum_auditor").bind('keydown', function(event) {
		if (event.keyCode == 13) {
			preCheckIn();
		}
	});

	function preCheckIn() {
		var auditor = $("#auditor").val();
		var people = $("#auditor").find("option:selected").text(); 
		if (auditor == "") {
			$(this).alertmsg('info', '请选择一个审查员！');
			return;
		}
		var startDate = $("#startDate").val();
		if (startDate == "") {
			$(this).alertmsg('info', '请选择起始时间！');
			return;
		}
		var endDate = $("#endDate").val();
		if (endDate == "") {
			$(this).alertmsg('info', '请选择结束时间！');
			return;
		}
		var sDate = new Date(startDate);
		var eDate = new Date(endDate);
		if(sDate.getTime()>eDate.getTime()){
			$(this).alertmsg('info', '起始时间不能大于结束时间！');
			return;
		}
		var serialnum_auditor = $("#serialnum_auditor").val();
		if(serialnum_auditor.length!=14){
			$(this).alertmsg('info', '请输入正确的流水号！');
			return;
		}
		$("#serialnum_auditor").val("").focus();
		$.ajax({
			url : "<c:url value='/back/softinfo/exportForAudit.do'/>",
			data : {
				"auditor" : auditor,
				"startDate" : startDate,
				"endDate" : endDate,
				"num" : serialnum_auditor
			},
			type : "POST",
			success : function(data) {
					var ret = eval('(' + data.data + ')');
				if (data.state==false) {
					var content = "<tr><td>" + ret.serialnum + "</td><td>"
							+ ret.softwarename + "</td><td>" + ret.applyperson
							+ "</td><td>" + ret.certificatedate + "</td><td>"
							+ ret.salesman + "</td><td>"
							+ ""
							+"</td><td style='color:red'>" + data.msg
							+ "</td></tr>";
					$("#mainbody_auditor").prepend(content);
					var errorval = $("#audit_count_error").text();
					$("#audit_count_error").html(parseInt(errorval)+1);
				} else {
					var content = "<tr><td>" + ret.serialnum + "</td><td>"
							+ ret.softwarename + "</td><td>" + ret.applyperson
							+ "</td><td>" + ret.certificatedate + "</td><td>"
							+ ret.salesman + "</td><td>"
							+ people
							+"</td><td style='color:green'>" + data.msg
							+ "</td></tr>";
					$("#mainbody_auditor").prepend(content);
					var okval = $("#audit_count_ok").text();
					$("#audit_count_ok").html(parseInt(okval)+1);
				}
			}
		});
	}
</script>
</html>
