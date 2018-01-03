<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/taglib.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>制证出库</title>
<div class="bjui-pageHeader" style="padding:5px;">
	<div style="display: inline-block;margin:5px;">
		<lable><strong class="color_ok">成功量：</strong></lable>
		<span><strong id="export_ok">0</strong></span>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable><strong class="color_fail">异常量：</strong></lable>
		<span><strong id="export_error">0</strong></span>
	</div>
	<lable>请选择Excel：</lable>
	<div
		style="display:inline-block; vertical-align:middle; position: absolute; z-index: 100;">
		<div id="doc_excel_up" data-toggle="upload"
			data-uploader="<c:url value='/back/checkout/importExcel.do'/>"
			data-file-size-limit="102400" data-file-type-exts="*.xls;"
			data-on-upload-success="doc_upload_success" data-icon="cloud-upload"></div>
	</div>
	<select id="export_auditor" name="auditor" data-toggle="selectpicker"
		style="display:none">
		<c:forEach items="${records}" var="opt">
			<option value="${opt.key }">${opt.value }</option>
		</c:forEach>
	</select>
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
			<tbody id="mainBody_checkout">
		</table>
	</form>
</div>
<script type="text/javascript">
	function doc_upload_success(file, data) {
		var ret = eval('(' + data + ')');
		if (ret.state) {
			$(this).alertmsg('info', '出库成功！');
			var json = eval('(' + ret.data + ')');
			var content;
			$.each(json, function(n, value) {
				var people;
				var msg;
				try	{
					if(value.trialAdmin.relName != null || value.trialAdmin.relName != "" || value.trialAdmin.relName != undefined ){
						people = value.trialAdmin.relName;
					}
				}catch (e){
					people = "";
				}
				if(value!=null&&value.temphouseorder==undefined){
					msg = "<td style='color:green'>出库成功</td>";
					var okval = $("#export_ok").text();
					$("#export_ok").html(parseInt(okval)+1);
					content +=
							"<tr>"
							+"<td>" + value.serialnum + "</td>"
							+"<td>"	+ value.softwarename + "</td>"
							+"<td>" + value.applyperson	+ "</td>"
							+"<td>" + value.certificatedate + "</td>"
							+"<td>"	+ value.salesman + "</td>"
							+"<td>" + people + "</td>" + msg
							+ "</tr>";
				}else{
					msg = "<td style='color:red'>" + value.temphouseorder + "</td>";
					var errorval = $("#export_error").text();
					$("#export_error").html(parseInt(errorval)+1);
					content +=
							"<tr>"
							+"<td>" + value.serialnum + "</td>"
							+"<td>"	+ "" + "</td>"
							+"<td>" + "" + "</td>"
							+"<td>" + "" + "</td>"
							+"<td>"	+ "" + "</td>"
							+"<td>" + people + "</td>" + msg
							+ "</tr>";
				}
//				if(value==null||value.temphouseorder!=""||value.temphouseorder==null){
//					msg = "<td style='color:red'>" + value.temphouseorder + "</td>";
//					console.log("2222"+msg);
//					var errorval = $("#export_error").text();
//					$("#export_error").html(parseInt(errorval)+1);
//				}else{
//					msg = "<td style='color:green'>出库成功</td>";
//					var okval = $("#export_ok").text();
//					$("#export_ok").html(parseInt(okval)+1);
//				}

			});
			$("#mainBody_checkout").prepend(content);
		} else {
			$(this).alertmsg('error', 'Excel 导入失败！');
		}
	}
</script>
</html>