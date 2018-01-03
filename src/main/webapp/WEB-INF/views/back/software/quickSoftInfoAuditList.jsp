<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/taglib.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>快捷入库:雍和</title>
<div class="bjui-pageHeader">
	<div style="display: inline-block;margin:5px;">
	<lable><strong class="color_ok">成功量：</strong></lable>
	<span/><strong id="quickIn_count_ok">0</strong></span>&nbsp;&nbsp;&nbsp;&nbsp;
	<lable><strong class="color_fail">异常量：</strong></lable>
	<span/><strong id="quickIn_count_error">0</strong></span>
	</div>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<div style="display:inline-block;margin:3px;">
		<lable>请选择出证日期：</lable>
		<input type="text" name="publishDate_quickRuku" id="publishDate_quickRuku"
			data-toggle="datepicker" size="15" aria-required="true"
			readonly="true" />
	</div>
	<lable>请选择仓位：</lable>
	<select id="position_quickRuku_1" name="position_1" data-toggle="selectpicker">
		<option value="">请选择一级仓位</option>
		<c:forEach items="${opts}" var="opt">
			<option value="${opt.key}">${opt.value}</option>
		</c:forEach>
	</select>
	<select id="position_quickRuku_2" name="position_2" data-toggle="selectpicker" data-width="120" >
	</select>
	<lable>请扫描流水号：</lable>
	<input type="text" id="serialnum_quickRuku" data-toggle="input" size="15" aria-required="true" /> 
	<button id="to_confirm_quickRuku" class="btn-green" data-icon="check">确认</button>
</div>
<div class="bjui-pageContent tableContent">
	<form id="j_custom_form_quickRuku" class="pageForm">
		<table class="table table-bordered table-hover table-striped table-top" data-toggle="tabledit">
			<thead>
				<tr>
					<th>流水号</th>
					<th>软件全称</th>
					<th>申请人</th>
					<th>制证日期</th>
					<th width="80">业务员</th>
					<th>库位号</th>
					<th>匹配状态</th>
				</tr>
			</thead>
			<tbody id="mainbody_ruku_exception"/>
			<tbody id="mainbody_quickRuku"/>
		</table>
	</form>
</div>
<div class="bjui-pageFooter">
	<ul>
		<li>
			<input id="checkIn_quickRuku" class="btn-green"  value="确认入库" type="button"/>
		</li>
		<li>
			<button id="close_quickRuku" class="btn btn-close" data-icon="close" type="button">关闭</button>
		</li>
	</ul>
</div>
<script type="text/javascript">
	function aa() {
		var flag = false;
		$(this).alertmsg('confirm', '关闭窗口前请确认录入的数据已入库？', {
			okCall : function() {
				//关闭窗口
				$("#close_quickRuku").trigger("click");
			},
			cancelCall : function() {
				flag = false;
			},
		});
		return flag;
	}


	$('#position_quickRuku_2').selectpicker('hide');





	$("#close_quickRuku").click(function() {
		var pos = $("#position_quickRuku_2").val();
		$.ajax({
			url : "<c:url value='/back/softinfo/shutdown.do'/>",
			data : {
				"pos" : pos
			},
			type : "post",
			dataType : "json"
		});
	});

	$("#to_confirm_quickRuku").click(function() {
		preCheckIn();
	});

	$("#serialnum_quickRuku").bind('keydown', function(event) {
		if (event.keyCode == 13) {
			preCheckIn();
		}
	});

	$("#checkIn_quickRuku").click(function() {
		var pos = $("#position_quickRuku_2").val();
		$("#checkIn_quickRuku").prop('disabled',true);
		$.ajax({
			url : "<c:url value='/back/softinfo/quickRukuCheckIn.do'/>",
			data : {
				"pos" : pos
			},
			type : "post",
			dataType : "json",
			success : function(data) {
				var ret = eval('(' + data.data + ')');
				if (data.state) {
					$(this).alertmsg('ok', data.msg);
					$.CurrentNavtab.navtab("refresh");
				} else {
					$(this).alertmsg('info', data.msg);
					$("#mainbody_quickRuku").empty();
					var content = "";
						$.each(ret,function (i,item) {
							content+=
									"<tr>"
									+"<td>" + item.serialnum + "</td>"
									+"<td>"	+ item.softwarename + "</td>"
									+"<td>" + item.applyperson	+ "</td>"
									+"<td>" + item.certificatedate + "</td>"
									+"<td>"	+ item.salesman + "</td>"
									+"<td style='color:red'>" + "该条记录已入库,请查证后在操作!" + "</td>"
									+"<td style='color:red'>" + data.msg + "</td>"
									+"</tr>"
						});
					$("#mainbody_quickRuku").append(content);
				}
				$("#checkIn_quickRuku").prop('disabled',false);
			}
		});
	});

	//选择二级仓库下拉选项
	$("#position_quickRuku_2").change(function() {
		$(this).alertmsg('confirm', '确认锁定该仓库？', {
			okCall : function() {
				//锁定数据库
				var pos = $("#position_quickRuku_2").val();
				$('#position_quickRuku_1').prop('disabled', true);
				$('#position_quickRuku_2').prop('disabled', true);
				$.ajax({
					url : "<c:url value='/back/softinfo/lockStorage.do'/>",
					data : {
						"pos" : pos
					},
					type : "GET",
					success:function (ret) {
						if(ret == 'alreadyLock'){
							$('#position_quickRuku_2').alertmsg('error', '该仓库以被锁定，请选择其他仓库！',{displayMode:"none",alertTimeout:1500});
							$('#position_quickRuku_2').prop('disabled', false);
							$('#position_quickRuku_2').selectpicker('val', '');
						}else if(ret == 'success'){
							$('#position_quickRuku_2').alertmsg('ok', '该仓库锁定成功！',{displayMode:"none",alertTimeout:1000});
						}else{
							$('#position_quickRuku_2').alertmsg('error', '该仓库锁定失败！',{displayMode:"none",alertTimeout:500});
							$('#position_quickRuku_2').prop('disabled', false);
							$('#position_quickRuku_2').selectpicker('val', '');
						}
					}
				});
			},
			cancelCall : function() {
				$('#position_quickRuku_2').selectpicker('val', '');
			}
		});
	});

	//选择一级仓库下拉选项
	$("#position_quickRuku_1").change(function() {
		var pos = $("#position_quickRuku_1").val();
		$("#position_quickRuku_2").empty();
		$.ajax({
			url : "<c:url value='/back/softinfo/getSecStorage.do'/>",
			data : {
				"pos" : pos
			},
			type : "GET",
			dataType : "json",
			success : function(ret) {
				$("#position_quickRuku_2").append(new Option("请选择二级仓位",""));
				$.each(ret,function(index,item){
					$("#position_quickRuku_2").append(new Option(item,index));
				});
				$('#position_quickRuku_2').selectpicker('show');
				$('#position_quickRuku_2').selectpicker('refresh');
			}
		});
	});

	function preCheckIn() {
		var pos = $("#position_quickRuku_2").val();
		if (pos == ""||pos == null||typeof(pos)==undefined) {
			$(this).alertmsg('info', '请选择存放仓位！',{displayMode:"none",alertTimeout:500});
			return;
		}
		var publishDate = $("#publishDate_quickRuku").val();
		if (publishDate == "") {
			$(this).alertmsg('info', '请选择发证日期！',{displayMode:"none",alertTimeout:500});
			return;
		}
		var serialnum = $("#serialnum_quickRuku").val();
		if (serialnum == "") {
			$(this).alertmsg('info', '请扫描文档流水号！',{displayMode:"none",alertTimeout:500});
			return;
		}

		var tar = $('#position_quickRuku_2 option:selected').text();
		//仓库里有软件 有出库日期的
		if(tar.split("_")[1]!=undefined){
			var rightTime = (tar.split("_")[1]).split(":")[1];
			if(rightTime!=publishDate){
				$(this).alertmsg('confirm', '选中的仓库存放的软件出证日期与选定日期不符！');
				return;
			}
		}

		$("#serialnum_quickRuku").val("").focus();
		$.ajax({
			url : "<c:url value='/back/softinfo/quickRuku.do'/>",
			data : {
				"date" : publishDate,
				"num" : serialnum,
				"pos" : pos
			},
			type : "POST",
			success : function(data) {
				var ret = eval('(' + data.data + ')');
				if (data.state == true) {
					var content =
						"<tr>"
							+"<td>" + ret.serialnum + "</td>"
							+"<td>"	+ ret.softwarename + "</td>"
							+"<td>" + ret.applyperson + "</td>"
							+"<td>" + ret.certificatedate + "</td>"
							+"<td>"	+ ret.salesman + "</td>"
							+"<td style='color:green'>"	+ ret.temphouseorder + "</td>"
							+"<td style='color:green'>" + data.msg + "</td>"
						+"</tr>";
					$("#mainbody_quickRuku").prepend(content);
					var okval = $("#quickIn_count_ok").text();
					$("#quickIn_count_ok").html(parseInt(okval)+1);
				} else {
					$(this).alertmsg('info', ret.temphouseorder,{displayMode:"none",alertTimeout:500});
					var cdate ;
					if(ret.certificatedate==null){
						cdate = "";
					}else{
						cdate = ret.certificatedate;
					}
					var content =
							"<tr>"
								+"<td>" + ret.serialnum + "</td>"
								+"<td>" + ret.softwarename + "</td>"
								+"<td>" + ret.applyperson + "</td>"
								+"<td>" + cdate + "</td>"
								+"<td>"	+ ret.salesman + "</td>"
								+"<td style='color:red'>"	+ ret.temphouseorder + "</td>"
								+"<td style='color:red'>" + data.msg + "</td>"
							+"</tr>";
					$("#mainbody_quickRuku").prepend(content);
					var errorval = $("#quickIn_count_error").text();
					$("#quickIn_count_error").html(parseInt(errorval)+1);
				}
			}
		});
	}
</script>
</html>
