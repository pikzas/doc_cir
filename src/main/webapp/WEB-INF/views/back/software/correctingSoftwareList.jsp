<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/back_authority.jsp"%>
<title>批量补正</title>
<div class="bjui-pageHeader">
	<form id="pagerForm" data-toggle="ajaxsearch" action="<c:url value=''/>" method="post">
		<input type="hidden" name="pageSize" value="${model.pageSize}" /> 
		<input type="hidden" name="pageCurrent" value="${model.pageCurrent}" /> 
		<input type="hidden" name="orderField" value="${param.orderField}" /> 
		<input type="hidden" name="orderDirection" value="${param.orderDirection}" />
		<div class="bjui-searchBar">
			<label>流水号:</label>&nbsp;<input id="correctSerialnum" name="correctSerialnum" type="text" value="${serialnum}" class="form-control" size="15"/>&nbsp; 
			<button type="submit" class="btn-default" data-icon="fa-hand-o-right" onclick="ToCorrectingSoftware()">确认</button>
			<button type="button" class="btn-green" data-confirm-msg="确定要导出信息吗？" data-idname="expids" data-group="ids"  data-icon="fa-download" onclick="checkout2()">导出补正表</button>
		</div>
	</form>
</div>
<div class="bjui-pageContent tableContent">
	<table class="table table-bordered table-hover table-striped table-top" data-selected-multi="true">
		<thead>
			<tr>
				<th width="26"><input id="checkall" type="checkbox" class="checkboxCtrl" data-group="ids" data-toggle="icheck"></th>
				<th>流水号</th>
				<th>申请人</th>
				<th>软件全称</th>
				<th>制证日期</th>
				<th>业务员</th>
				<th>审查员</th>
				<th>匹配状态</th>
			</tr>
		</thead>
		<tbody id="softwareMain">
		</tbody>
	</table>
</div>
<div class="bjui-pageFooter">
	<div class="pages">
		<span>每页&nbsp;</span>
		<div class="selectPagesize">
			<select data-toggle="selectpicker"
				data-toggle-change="changepagesize">
				<option value="20">20</option>
				<option value="30">30</option>
				<option value="60">60</option>
				<option value="120">120</option>
				<option value="150">150</option>
			</select>
		</div>
		<span>&nbsp;条，共 ${po.total} 条</span>
	</div>
	<div class="pagination-box" data-toggle="pagination" data-total="${po.total}" data-page-size="${po.neverypage}" data-page-current="1"></div>
</div>
<script type="text/javascript">
	function checkout2() {
		var ids = '';
		var idsObj = document.getElementsByName('ids');//获取所有列表数据
		
		for (i = 0; i < idsObj.length; i++) {
			//获取所有选中数据
			if (idsObj[i].checked == true) {
				ids = idsObj[i].value + ',' + ids;
			}
		}
		
		if(ids==""){
			$(this).alertmsg('info', '请先选择导出数据！');
			$("#correctSerialnum").focus();
			return;
		}
		window.location.href="<c:url value='/back/softinfo/exportExcelAll.do?ids='/>"+ids;
		
		/*ids = ids.substring(0, ids.length - 1);
		
		var form = $("<form>");   //定义一个form表单
        form.attr('style', 'display:none');   //在form表单中添加查询参数
        form.attr('target', '');
        form.attr('method', 'post');
        form.attr('action', "<c:url value="/back/softinfo/exportExcelAllhm.do"/>");

        var input1 = $('<input>');
        input1.attr('type', 'hidden');
        input1.attr('name', 'ids');
        input1.attr('value', ids);
        $('body').append(form);  //将表单放置在web中 
        form.append(input1);   //将查询参数控件提交到表单上
        form.submit();*/
        
        
	}
	
	
	//加载前
	function DonLoad() {
		$.CurrentNavtab.navtab("refresh");
		return true;
	}
	//页面关闭刷新当前页面
	function beforeClose() {
		$.CurrentNavtab.navtab("refresh");
		return true;
	}
	function ToCorrectingSoftware(){
		var correctSerialnum = $("#correctSerialnum").val();
		if(correctSerialnum==''){
			$("#correctSerialnum").alertmsg('error', '您还未输入流水号！',{okCall:function(){$("#correctSerialnum").focus();}});
			$("#correctSerialnum").val("");
			$("#correctSerialnum").focus();
			return;
		}
		if(correctSerialnum.length!=14){
			$("#correctSerialnum").alertmsg('error', '请输入正确的流水号！',{okCall:function(){$("#correctSerialnum").focus();}});
			$("#correctSerialnum").val("");
			$("#correctSerialnum").focus();
			return;
		}
		$.ajax({
			type : 'POST',
			url : '<c:url value="/back/softinfo/doCorrectingSoftware.do"/>',
			data : {
				serialnum:correctSerialnum
			},
			success : function(data){
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
				if (data.state == true) {
					var afterTohead =
							"<tr>"
								+"<td><input type='checkbox' name='ids' data-toggle='icheck' id='ids' value='"+ret.id+"'></td>"
								+"<td>"+ret.serialnum+"</td>"
								+"<td>"+ret.applyperson+"</td>"
								+"<td>"+ret.softwarename+"</td>"
								+"<td>"+ret.certificatedate+"</td>"
								+"<td>"+ret.salesman+"</td>"
								+"<td>"+ret.trialAdmin.relName+"</td>"
								+"<td style='color:green'>"+data.msg+"</td>"
							+"</tr>";
					$("#softwareMain").prepend(afterTohead);
					$("#correctSerialnum").val("");
					$("#correctSerialnum").focus();
				}else{
					var afterTohead =
							"<tr>"
								+"<td><input type='checkbox' name='ids' data-toggle='icheck' id='ids' value='"+ret.id+"'></td>"
								+"<td>"+ret.serialnum+"</td>"
								+"<td>"+ret.applyperson+"</td>"
								+"<td>"+ret.softwarename+"</td>"
								+"<td>"+ret.certificatedate+"</td>"
								+"<td>"+ret.salesman+"</td>"
								+"<td>"+ret.trialAdmin.relName+"</td>"
								+"<td style='color:red'>"+data.msg+"</td>"
							+"</tr>";
					$(this).alertmsg('error', data.msg);
					$("#correctSerialnum").focus();
				}
			}
		});
	}
</script>