<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/common/include/taglib.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>撤回</title>
<div class="bjui-pageHeader">
    <div style="display: inline-block;margin:5px;">
        <lable><strong class="color_ok">撤回成功数：</strong></lable>
        <span/><strong id="revoke_ok">0</strong></span>&nbsp;&nbsp;&nbsp;&nbsp;
        <lable><strong class="color_fail">撤回异常数：</strong></lable>
        <span/><strong id="revoke_error">0</strong></span>
    </div>
    <br/>
    <lable>请扫描流水号：</lable>
    <input type="text" id="serialnum_revoke" data-toggle="input" size="15" aria-required="true"/>
    <button id="to_confirm_revoke" class="btn-green" data-icon="check">确认</button>
</div>
<div class="bjui-pageContent tableContent">
    <form id="j_custom_form_revoke" class="pageForm">
        <table class="table table-bordered table-hover table-striped table-top" data-toggle="tabledit">
            <thead>
	            <tr>
	                <th title="流水号"></th>
	                <th title="软件全称"></th>
	                <th title="申请人"></th>
	                <th title="制证日期"></th>
	                <th title="业务员"></th>
	                <th title="审查员"></th>
	                <%--
	                <th title="库位号"></th>
	                --%>
	                <th title="匹配状态"></th>
	            </tr>
            </thead>
            <tbody id="mainbody_revoke">
            </tbody>
        </table>
    </form>
</div>
<div class="bjui-pageFooter">
    <ul>
        <li>
            <button class="btn btn-close"  data-icon="close" type="button" >关闭</button>
        </li>
    </ul>
</div>
<script type="text/javascript">
    $("#to_confirm_revoke").click(function () {
        preCheckIn();
    });

    $("#serialnum_revoke").bind('keydown', function (event) {
        if (event.keyCode == 13) {
            preCheckIn();
        }
    });
	

    function preCheckIn() {
        var serialnum = $("#serialnum_revoke").val();
        if (serialnum == "") {
            $(this).alertmsg('error', '请扫描文档流水号！',{okCall:function(){$("#serialnum_revoke").focus();}});
            $("#serialnum_revoke").val("").focus();
            return;
        }
        if(serialnum.length!=14){
			$("#serialnum_revoke").alertmsg('error', '请输入正确的流水号！',{okCall:function(){$("#serialnum_revoke").focus();}});
			$("#serialnum_revoke").val("").focus();
			return;
		}
        $("#serialnum_revoke").val("").focus();
        $.ajax({
            url: "<c:url value='/back/softinfo/toRevoke.do'/>",
            data: {
                "num": serialnum
            },
            type: "POST",
            success: function (data) {
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
                    var content =
                            "<tr><td>" + ret.serialnum + "</td><td>"
                            + ret.softwarename + "</td><td>" + ret.applyperson
                            + "</td><td>" + ret.certificatedate + "</td><td>"
                            + ret.salesman + "</td><td>"
                            + people
                            + "</td><td style='color:green'>" + data.msg
                            + "</td></tr>";

                    $("#mainbody_revoke").prepend(content);
                    var okval = $("#revoke_ok").text();
                    $("#revoke_ok").html(parseInt(okval)+1);
                } else {
                    var content =
                            "<tr>"
                                +"<td>"+ ret.serialnum  +"</td>"
                                +"<td>"+ ret.softwarename  +"</td>"
                                +"<td>"+ ret.applyperson  +"</td>"
                                +"<td>"+ ret.certificatedate  +"</td>"
                                +"<td>"+ ret.salesman  +"</td>"
                                +"<td>"+people+"</td>"
                                +"<td style='color:red'>"+ data.msg  +"</td>"
                            +"</tr>"
                    $("#mainbody_revoke").prepend(content);
                    var errorval = $("#revoke_error").text();
                    $("#revoke_error").html(parseInt(errorval)+1);
                }
            }
        });
    }
</script>
</html>
