<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/back_authority.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>批量核费</title>
<div class="bjui-pageHeader" style="padding:5px;">
	<div style="display: inline-block;margin:5px;">
		<lable><strong class="color_ok">核费成功：</strong></lable>
		<span><strong id="accounting_ok">0</strong></span>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable><strong class="color_fail">核费异常：</strong></lable>
		<span><strong id="accounting_error">0</strong></span>
	</div>
	<lable>请选择核费Excel：</lable>
	<div style="display:inline-block; vertical-align:middle; position: absolute; z-index:100;">
		<div id="doc_accountind_excel_up" data-toggle="upload" data-button-text="请选择正确的核费文件"
			data-uploader="<c:url value='/back/checkout/accountingExcel.do'/>"
			data-file-size-limit="102400" data-file-type-exts="*.xls;"
			data-on-upload-success="doc_upload_success" data-icon="cloud-upload">
		</div>
	</div>
	<div style="float:right;">
		<button type="button" onclick="getExcel()" class="btn-green" data-icon="cloud-download">标准EXCEL文件下载</button>
	</div>
	<form id="pagerForm" data-toggle="ajaxsearch" action="<c:url value=''/>" method="post" style="padding:0 0 0 5px;">
		<input type="hidden" name="pageSize" value="${model.pageSize}" /> 
		<input type="hidden" name="pageCurrent" value="${model.pageCurrent}" /> 
		<input type="hidden" name="orderField" value="${param.orderField}" /> 
		<input type="hidden" name="orderDirection" value="${param.orderDirection}" />
		<div class="bjui-searchBar">
			<label>流水号:</label>&nbsp;<input id="serachSerialnum" name="serachSerialnum" type="text" value="${serialnum}" class="form-control" size="15"/>&nbsp; 
			<button type="submit" class="btn-default" data-icon="fa-hand-o-right" onclick="ToAccountingSoftware()">确认</button>
		</div>
	</form>
</div>
<div class="bjui-pageContent tableContent">
	<table class="table table-bordered table-hover table-striped table-top" data-selected-multi="true">
		<thead>
			<tr>
				<th>流水号</th>
				<th>软件全称</th>
				<th>申请人</th>
				<th>制证日期</th>
				<th>业务员</th>
				<th>库位号</th>
				<th>匹配状态</th>
			</tr>
		</thead>
		<tbody id="mainBody_accounting">
	</table>
</div>
<script type="text/javascript">
	function doc_upload_success(file, data) {
		var ret = $.parseJSON(data);
//		var ret = eval('(' + data + ')');
		if (ret.state) {
			$(this).alertmsg('ok', '批量核费成功！');
			var json = eval('(' + ret.data + ')');
			var content;
			$.each(json, function(n, value) {
				if(value==null || value.temphouseorder!=""){
					var msg = "<td style='color:red'>" + value.temphouseorder + "</td>";
					var errorval = $("#accounting_error").text();
					$("#accounting_error").html(parseInt(errorval)+1);
				}else{
					var msg = "<td style='color:green'>√ 核费成功!</td>";
					var okval = $("#accounting_ok").text();
					$("#accounting_ok").html(parseInt(okval)+1);
				}
				if(value.certificatedate == null){
					var cdate = "";
				}else{
					var cdate = value.certificatedate;
				}
				content +=
						"<tr>"
							+"<td>" + value.serialnum + "</td>"
							+"<td>"	+ value.softwarename + "</td>"
							+"<td>" + value.applyperson	+ "</td>"
							+"<td>" + cdate + "</td>"
							+"<td>"	+ value.salesman + "</td>"
							+"<td>" + value.description + "</td>" + msg
						+ "</tr>";
			});
			$("#mainBody_accounting").prepend(content);
		} else {
			$(this).alertmsg('error', 'Excel 导入失败！');
		}
	}
	
	function getExcel() {
		document.location.href="<c:url value="/back/softinfo/getStandardExcel.do"/>";
	}
	
	function ToAccountingSoftware(){
		var serachSerialnum = $("#serachSerialnum").val();
		if(serachSerialnum==''){
			$("#serachSerialnum").alertmsg('error', '您还未输入流水号！',{okCall:function(){$("#serachSerialnum").focus();}});
			$("#serachSerialnum").val("");
			$("#serachSerialnum").focus();
			return;
		}
		if(serachSerialnum.length!=14){
			$("#serachSerialnum").alertmsg('error', '请输入正确的流水号！',{okCall:function(){$("#serachSerialnum").focus();}});
			$("#serachSerialnum").val("");
			$("#serachSerialnum").focus();
			return;
		}
		$.ajax({
			type : 'POST',
			url : '<c:url value="/back/softinfo/doAccountingSoftware.do"/>',
			data : {
				serialnum:serachSerialnum
			},
			success : function(data){
				var ret = eval('(' + data.data + ')');
				if (data.state == true) {
					var afterTohead = "<tr><td>"+ret.serialnum+"</td><td>"+ret.softwarename+"</td><td>"+ret.applyperson+"</td><td>"+ret.certificatedate+"</td><td>"+ret.salesman+"</td><td>"+ret.temphouseorder+"</td><td style='color:green'>"+data.msg+"</td></tr>";
					$("#mainBody_accounting").prepend(afterTohead);
					var okval = $("#accounting_ok").text();
					$("#accounting_ok").html(parseInt(okval)+1);
					$("#serachSerialnum").val("");
					$("#serachSerialnum").focus();
				} else {
					$(this).alertmsg('info', data.msg , {alertTimeout:1000});
					var afterTohead = "<tr><td>"+ret.serialnum+"</td><td>"+ret.softwarename+"</td><td>"+ret.applyperson+"</td><td>"+ret.certificatedate+"</td><td>"+ret.salesman+"</td><td>"+ret.temphouseorder+"</td><td style='color:red'>"+data.msg+"</td></tr>";
					$("#mainBody_accounting").prepend(afterTohead);
					var errorval = $("#accounting_error").text();
					$("#accounting_error").html(parseInt(errorval)+1);
					$("#serachSerialnum").val("");
					$("#serachSerialnum").focus();
				}
			}
		});
	}
</script>