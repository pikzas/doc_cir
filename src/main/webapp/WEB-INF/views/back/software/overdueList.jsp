<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/common/include/taglib.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>逾期回库</title>
<div class="bjui-pageHeader">
    <div style="display: inline-block;margin:5px;">
        <lable><strong class="color_ok">成功量：</strong></lable>
        <span/><strong id="odhk_ok">0</strong></span>&nbsp;&nbsp;&nbsp;&nbsp;
        <lable><strong class="color_fail">异常量：</strong></lable>
        <span/><strong id="odhk_error">0</strong></span>
    </div>
    <br/>
    <lable>请选择仓位：</lable>
    <select id="position_odhk_1" name="position_1" data-toggle="selectpicker">
        <option value="">请选择一级仓位</option>
        <c:forEach items="${opts}" var="opt">
            <option value="${opt.key}">${opt.value}</option>
        </c:forEach>
    </select>
    <select id="position_odhk_2" name="position_2" data-toggle="selectpicker" data-width="120" >
    </select>
    <lable>请扫描流水号：</lable>
    <input type="text" id="serialnum_odhk" data-toggle="input" size="15"
           aria-required="true"/>
    <button id="to_confirm_odhk" class="btn-green" data-icon="check">确认</button>
</div>
<div class="bjui-pageContent tableContent">
    <form id="j_custom_form_odhk" class="pageForm">
        <table class="table table-bordered table-hover table-striped table-top" data-toggle="tabledit">
            <thead>
	            <tr>
	                <th title="流水号"></th>
	                <th title="软件全称"></th>
	                <th title="申请人"></th>
	                <th title="制证日期"></th>
	                <th title="业务员"></th>
	                <th title="审查员"></th>
	                <th title="库位号"></th>
	                <th title="匹配状态"></th>
	            </tr>
            </thead>
            <tbody id="mainbody_odhk">
            </tbody>
        </table>
    </form>
</div>
<div class="bjui-pageFooter">
    <ul>
        <li>
            <input id="checkIn_odhk" class="btn-green" value="确认回库" type="button"/>
        </li>
        <li>
            <button id="close_odhk" class="btn btn-close" data-icon="close" type="button">关闭</button>
        </li>
    </ul>
</div>
<script type="text/javascript">
    $('#position_odhk_2').selectpicker('hide');
    $("#close_odhk").click(function () {
        var pos = $("#position_odhk_2").val();
        $.ajax({
            url: "<c:url value='/back/softinfo/shutdown.do'/>",
            data: {
                "pos": pos
            },
            type: "post",
            dataType: "json"
        });
    });

    $("#to_confirm_odhk").click(function () {
        preCheckIn();
    });

    $("#serialnum_odhk").bind('keydown', function (event) {
        if (event.keyCode == 13) {
            preCheckIn();
        }
    });

    $("#checkIn_odhk").click(function () {
        var pos = $("#position_odhk_2").val();
        $("#checkIn_odhk").prop('disabled', true);
        $.ajax({
            url: "<c:url value='/back/softinfo/odhkCheckIn.do'/>",
            data: {
                "pos": pos
            },
            type: "post",
            dataType: "json",
            success: function (data) {
                var ret = eval('(' + data.data + ')');
                if (data.state) {
                    $(this).alertmsg('ok', data.msg);
                    $.CurrentNavtab.navtab("refresh");
                } else {
                    $(this).alertmsg('info', data.msg);
                    $("#mainbody_odhk").empty();
                    var content = "";
                    $.each(ret,function (i,item) {
                        var people;
                        try
                        {
                            if(item.trialAdmin.relName != null || item.trialAdmin.relName != "" || item.trialAdmin.relName != undefined ){
                                people = item.trialAdmin.relName;
                            }
                        }
                        catch (e){
                            people = "";
                        }
                        content+=
                                "<tr>"
                                +"<td>" + item.serialnum + "</td>"
                                +"<td>"	+ item.softwarename + "</td>"
                                +"<td>" + item.applyperson	+ "</td>"
                                +"<td>" + item.certificatedate + "</td>"
                                +"<td>"	+ item.salesman + "</td>"
                                +"<td>"	+ people + "</td>"
                                +"<td style='color:red'>" + "该条记录已入库,请查证后在操作!" + "</td>"
                                +"<td style='color:red'>" + data.msg + "</td>"
                                +"</tr>"
                    });
                    $("#mainbody_odhk").append(content);
                }
                $("#checkIn_odhk").prop('disabled', false);
            }
        });
    });

    //选择一级仓库下拉选项
    $("#position_odhk_1").change(function() {
        var pos = $("#position_odhk_1").val();
        $("#position_odhk_2").empty();
        $.ajax({
            url : "<c:url value='/back/softinfo/getOverdueSecStorage.do'/>",
            data : {
                "pos" : pos
            },
            type : "GET",
            dataType : "json",
            success : function(ret) {
                $("#position_odhk_2").append(new Option("请选择二级仓位",""));
                $.each(ret,function(index,item){
                    $("#position_odhk_2").append(new Option(item,index));
                });
                $('#position_odhk_2').selectpicker('show');
                $('#position_odhk_2').selectpicker('refresh');
            }
        });
    });

    //选择二级仓库下拉选项
    $("#position_odhk_2").change(function() {
        $(this).alertmsg('confirm', '确认锁定该仓库？', {
            okCall : function() {
                //锁定数据库
                var pos = $("#position_odhk_2").val();
                $('#position_odhk_1').prop('disabled', true);
                $('#position_odhk_2').prop('disabled', true);
                $.ajax({
                    url : "<c:url value='/back/softinfo/lockStorage.do'/>",
                    data : {
                        "pos" : pos
                    },
                    type : "GET",
                    success:function (ret) {
                    	if(ret == 'alreadyLock'){
							$('#position_odhk_2').alertmsg('error', '该仓库以被锁定，请选择其他仓库！',{displayMode:"none",alertTimeout:1500});
							$('#position_odhk_2').prop('disabled', false);
							$('#position_odhk_2').selectpicker('val', '');
						}else if(ret == 'success'){
							$('#position_odhk_2').alertmsg('ok', '该仓库锁定成功！',{displayMode:"none",alertTimeout:1000});
						}else{
							$('#position_odhk_2').alertmsg('error', '该仓库锁定失败！',{displayMode:"none",alertTimeout:500});
							$('#position_odhk_2').prop('disabled', false);
							$('#position_odhk_2').selectpicker('val', '');
						}
                    }
                });
            },
            cancelCall : function() {
                $('#position_odhk_2').selectpicker('val', '');
            }
        });
    });



    function preCheckIn() {
    	var pos1 = $("#position_odhk_1").val();
        if (pos1 == "") {
            $(this).alertmsg('info', '请选择一级仓位！',{displayMode:"none",alertTimeout:800});
            return;
        }
        var pos = $("#position_odhk_2").val();
        if (pos == "") {
            $(this).alertmsg('info', '请选择二级仓位！',{displayMode:"none",alertTimeout:800});
            return;
        }
        var serialnum = $("#serialnum_odhk").val();
        if (serialnum == "") {
			$(this).alertmsg('info', '请扫描文档流水号！',{displayMode:"none",alertTimeout:800});
            $("#serialnum_odhk").val("").focus();
			return;
		}
        if(serialnum.length != 14){
        	$(this).alertmsg('info', '流水号不符合要求！',{displayMode:"none",alertTimeout:800});
            $("#serialnum_odhk").val("").focus();
			return;
        }
        $("#serialnum_odhk").val("").focus();
        $.ajax({
            url: "<c:url value='/back/softinfo/odhk.do'/>",
            data: {
                "num": serialnum,
                "pos": pos
            },
            type: "POST",
            success: function (data) {
                var ret = eval('(' + data.data + ')');
                var people;
                var certificatedate;
                try
                {
                    if(ret.trialAdmin.relName != null || ret.trialAdmin.relName != "" || ret.trialAdmin.relName != undefined ){
                        people = ret.trialAdmin.relName;
                    }
                }
                catch (e){
                    people = "";
                }
                try
                {
                    if(ret.certificatedate != null || ret.certificatedate  != "" || ret.certificatedate  != undefined ){
                    	certificatedate = ret.certificatedate ;
                    }
                }
                catch (e){
                	certificatedate = "";
                }
                if (data.state == true) {
                    var content =
                            "<tr>"
                            +"<td>" + ret.serialnum + "</td>"
                            +"<td>" + ret.softwarename + "</td>"
                            +"<td>" + ret.applyperson  + "</td>"
                            +"<td>" + certificatedate + "</td>"
                            +"<td>" + ret.salesman + "</td>"
                            +"<td>" + people + "</td>"
                            +"<td style='color:green'>" + ret.temphouseorder + "</td>"
                            +"<td style='color:green'>" + data.msg + "</td>"
                            +"</tr>";
                    $("#mainbody_odhk").prepend(content);
                    var okval = $("#odhk_ok").text();
                    $("#odhk_ok").html(parseInt(okval)+1);
                } else {
                    var content =
                            "<tr>"
                                +"<td>"+ ret.serialnum +"</td>"
                                +"<td>"+ ret.softwarename +"</td>"
                                +"<td>"+ ret.applyperson +"</td>"
                                +"<td>"+ certificatedate +"</td>"
                                +"<td>"+ ret.salesman +"</td>"
                                +"<td>"+ people +"</td>"
                                +"<td style='color:red'>"+ ret.temphouseorder +"</td>"
                                +"<td style='color:red'>"+ data.msg +"</td>"
                            +"</tr>"
                    $("#mainbody_odhk").prepend(content);
                    var errorval = $("#odhk_error").text();
                    $("#odhk_error").html(parseInt(errorval)+1);
                }
            }
        });
    }
</script>
</html>
