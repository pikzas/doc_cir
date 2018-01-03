<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/back_authority.jsp"%>
<%@ include file="/common/include/taglib.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>分审核销</title>
<div class="bjui-pageHeader">
	<form id="pagerForm" data-toggle="ajaxsearch"  action="<c:url value='/back/statistic/toAuditCheck.do'/>" method="post">
		<div class="bjui-searchBar">
			<label>导入日期开始于</label>
				<input  id="auditdate1" name='auditdate1' data-toggle="datepicker" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${auditdate1}"/>"/>
            <label>结束于</label>
				<input id="auditdate2" name='auditdate2' data-toggle="datepicker" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${auditdate2}" />"/>
			<lable>分审员：</lable>
			<select id="auditID" name="auditID" data-toggle="selectpicker">
				<option value="">请选择分审员</option>
				<c:forEach items="${records}" var="opt">
					<option value="${opt.key }" <c:if test="${opt.key eq aduitID}">selected="selected"</c:if>>${opt.value }</option>
				</c:forEach>
			</select>
			<button type="button" onclick="auditCheck()" class="btn-green" data-icon="check">查询</button>&nbsp;
			<a class="btn btn-orange" href="javascript:;"data-toggle="reloadsearch" data-clear-query="true" data-icon="undo">清空查询</a>
		</div>
	</form>
</div>
<div class="bjui-pageContent tableContent">
	<table class="table table-bordered table-hover table-striped table-top" data-selected-multi="true">
		<thead>
			<tr>
				<th>审查员</th>
				<th>分审日期</th>
				<th>分审数</th>
				<th>待制证收案数</th>
				<th>待补正收案数</th>
				<th>未回收数</th>
			</tr>
		</thead>
		<tbody id="mainbody_auditcheck"/>
	</table>
</div>

<script type="text/javascript">
	
	function auditCheck() {
		$("#mainbody_auditcheck").empty();
		var auditdate1 = $("#auditdate1").val();
		var auditdate2 = $("#auditdate2").val();
		var auditID = $("#auditID").val();
		$.ajax({
			url:"<c:url value="/back/statistic/auditCheck.do"/>",
			data:{"auditdate1":auditdate1,"auditdate2":auditdate2,"auditID":auditID},
			type:"get",
			success:function (data) {
				var ret = eval('(' + data + ')');
				var content = "";
				var datetime = "";
				//追加日期到页面
				if(ret.auditdate1 != undefined && ret.auditdate2 != undefined ){
					datetime = ret.auditdate1 +"_"+ret.auditdate2;
				}else{
					datetime = "_";
				}
				//判定审核人员是否存在
				if(ret.auditID != undefined){
					$.each(ret.records,function (i,item) {
						if(ret.auditID == i){
							content+=
									"<tr>"
									+"<td>" + item + "</td>"
									+"<td>" + datetime +"</td>"
									+"<td id="+i+"_3>" + 0	+ "</td>"
									+"<td id="+i+"_4>" + 0 + "</td>"
									+"<td id="+i+"_5>" + 0 + "</td>"
									+"<td id="+i+"_6>" + 0 + "</td>"
									+"</tr>"
						}
					});
					$("#mainbody_auditcheck").append(content);
					$.each(ret.results,function (i,item) {
						$("#"+ item.adminID+"_"+item.type).text(item.total);
					});
					$.each(ret.results,function (i,item) {
						$("#"+ item.adminID+"_"+6).html(parseInt($("#"+ item.adminID+"_"+3).text())-parseInt($("#"+ item.adminID+"_"+4).text())-parseInt($("#"+ item.adminID+"_"+5).text()));
					});
					$.each(ret.results,function (i,item) {
						var adminID = ret.auditID;
						var number = parseInt($("#"+ adminID+"_"+3).text())-parseInt($("#"+ adminID+"_"+4).text())-parseInt($("#"+ adminID+"_"+5).text());
						var htext = "<a href='<c:url value='/back/statistic/checkUnBack.do?id='/>"+adminID+"&time="+datetime+"' data-toggle='dialog' data-width='900' data-height='450' data-id='dialog-mask' data-mask='true'>"+(number)+"</a>"
						$("#"+ adminID+"_"+6).html(htext);
					});
				}else{
					$.each(ret.records,function (i,item) {
						content+=
								"<tr>"
								+"<td>" + item + "</td>"
								+"<td>"+ datetime +"</td>"
								+"<td id="+i+"_3>" + 0+ "</td>"
								+"<td id="+i+"_4>" + 0 + "</td>"
								+"<td id="+i+"_5>" + 0 + "</td>"
								+"<td id="+i+"_6>" + 0 + "</td>"
								+"</tr>"
					});
					$("#mainbody_auditcheck").append(content);
					$.each(ret.results,function (i,item) {
						$("#"+ item.adminID+"_"+item.type).text(item.total);
					});
					$.each(ret.results,function (i,item) {
						var adminID = item.adminID;
						var number = parseInt($("#"+ adminID+"_"+3).text())-parseInt($("#"+ adminID+"_"+4).text())-parseInt($("#"+ adminID+"_"+5).text());
						var htext = "<a href='<c:url value='/back/statistic/checkUnBack.do?id='/>"+adminID+"&time="+datetime+"' data-toggle='dialog' data-width='900' data-height='450' data-id='dialog-mask' data-mask='true'>"+(number)+"</a>"
						$("#"+ adminID+"_"+6).html(htext);
					});
				}
			}
		});
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
</script>