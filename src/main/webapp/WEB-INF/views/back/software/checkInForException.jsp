<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/common/include/taglib.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>异常回库</title>
<div class="bjui-pageHeader">
    <div style="display: inline-block;margin:5px;">
        <lable><strong class="color_ok">成功量：</strong></lable>
        <span/><strong id="count_ok">0</strong></span>&nbsp;&nbsp;&nbsp;&nbsp;
        <lable><strong class="color_fail">异常量：</strong></lable>
        <span/><strong id="count_error">0</strong></span>
    </div>
    <br/>
    <div style="display:inline-block; margin-left:120px\0;">
        <lable>请选择出证日期：</lable>
        <input type="text" name="publishDate_exception" id="publishDate_exception"
               data-toggle="datepicker" size="15" aria-required="true"
               readonly="true"/>
    </div>
    <lable>请选择仓位：</lable>
    <select id="position_exception_1" name="position_1" data-toggle="selectpicker">
        <option value="">请选择一级仓位</option>
        <c:forEach items="${opts}" var="opt">
            <option value="${opt.key }">${opt.value }</option>
        </c:forEach>
    </select>
    <select id="position_exception_2" name="position_2" data-toggle="selectpicker" data-width="120" >
    </select>
    <lable>请扫描流水号：</lable>
    <input type="text" id="serialnum_exception" data-toggle="input" size="15"
           aria-required="true"/>
    <button id="to_confirm_exception" class="btn-green" data-icon="check">确认</button>

</div>
<div class="bjui-pageContent tableContent">
    <form id="j_custom_form_exception" class="pageForm">
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
                <th title="库位号"></th>
                <th title="匹配状态"></th>
            </tr>
            </thead>
            <tbody id="mainbody_exception">
            </tbody>
        </table>
    </form>
</div>
<div class="bjui-pageFooter">
    <ul>
        <li>
            <input id="checkIn_exception" class="btn-green" type="button"  value="确认回库"/>
        </li>
        <li>
            <button id="close_exception" class="btn btn-close" data-icon="close" type="button">关闭</button>
        </li>
    </ul>
</div>
<script type="text/javascript">

    function cc() {
        var flag = false;
        $(this).alertmsg('confirm', '关闭窗口前请确认录入的数据已入库？', {
            okCall : function() {
                //关闭窗口
                $("#close_exception").trigger("click");
            },
            cancelCall : function() {
                flag = false;
            },
        });
        return flag;
    }

    $('#position_exception_2').selectpicker('hide');

    $("#to_confirm_exception").click(function () {
        preCheckIn();
    });

    $("#serialnum_exception").bind('keydown', function (event) {
        if (event.keyCode == 13) {
            preCheckIn();
        }
    });

    $("#checkIn_exception").click(function () {
        var pos = $("#position_exception_2").val();
        $("#checkIn_exception").prop('disabled',true);
        $.ajax({
            url: "<c:url value='/back/softinfo/exceptionCheckIn.do'/>",
            data: {
                "pos": pos
            },
            type: "post",
            dataType: "json",
            success: function (data) {
                var ret = eval('(' + data.data + ')');
                if (data.state) {
                    $(this).alertmsg('ok', data.msg);
                    $("#mainbody_exception").empty()
                    $.CurrentNavtab.navtab("refresh");
                } else {
                    $(this).alertmsg('info', data.msg);
                    $("#mainbody_exception").empty();
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
                    $("#mainbody_exception").append(content);
                }
                $("#checkIn_exception").prop('disabled',false);
            }
        });
    });


    //选择一级仓库下拉选项
    $("#position_exception_1").change(function() {
        var pos = $("#position_exception_1").val();
        $("#position_exception_2").empty();
        $.ajax({
            url : "<c:url value='/back/softinfo/getSecStorage.do'/>",
            data : {
                "pos" : pos
            },
            type : "GET",
            dataType : "json",
            success : function(ret) {
                $("#position_exception_2").append(new Option("请选择二级仓位",""));
                $.each(ret,function(index,item){
                    $("#position_exception_2").append(new Option(item,index));
                });
                $('#position_exception_2').selectpicker('show');
                $('#position_exception_2').selectpicker('refresh');
            }
        });
    });

    //选择二级仓库下拉选项
    $("#position_exception_2").change(function() {
        $(this).alertmsg('confirm', '确认锁定该仓库？', {
            okCall : function() {
                //锁定数据库
                var pos = $("#position_exception_2").val();
                $('#position_exception_1').prop('disabled', true);
                $('#position_exception_2').prop('disabled', true);
                $.ajax({
                    url : "<c:url value='/back/softinfo/lockStorage.do'/>",
                    data : {
                        "pos" : pos
                    },
                    type : "GET",
                    success:function (ret) {
                    	if(ret == 'alreadyLock'){
							$('#position_exception_2').alertmsg('error', '该仓库以被锁定，请选择其他仓库！',{displayMode:"none",alertTimeout:1500});
							$('#position_exception_2').prop('disabled', false);
							$('#position_exception_2').selectpicker('val', '');
						}else if(ret == 'success'){
							$('#position_exception_2').alertmsg('ok', '该仓库锁定成功！',{displayMode:"none",alertTimeout:1000});
						}else{
							$('#position_exception_2').alertmsg('error', '该仓库锁定失败！',{displayMode:"none",alertTimeout:500});
							$('#position_exception_2').prop('disabled', false);
							$('#position_exception_2').selectpicker('val', '');
						}
                    }
                });
            },
            cancelCall : function() {
                $('#position_exception_2').selectpicker('val', '');
            }
        });
    });

    $("#close_exception").click(function () {
        var pos = $("#position_exception_2").val();
        $.ajax({
            url: "<c:url value='/back/softinfo/shutdown.do'/>",
            data: {
                "pos": pos
            },
            type: "post",
            dataType: "json"
        });
    });

    function preCheckIn() {
        var pos = $("#position_exception_2").val();
        if (pos == ""||pos == null||typeof(pos)==undefined ) {
            $(this).alertmsg('info', '请选择存放仓位！');
            return;
        }
        var publishDate = $("#publishDate_exception").val();
        if (publishDate == "") {
            $(this).alertmsg('info', '请选择发证日期！');
            return;
        }
        var serialnum = $("#serialnum_exception").val();
        if (serialnum == "") {
            $(this).alertmsg('info', '请扫描文档流水号！');
            return;
        }
        $("#serialnum_exception").val("").focus();
        $.ajax({
            url: "<c:url value='/back/softinfo/rukuForException.do'/>",
            data: {
                "date": publishDate,
                "num": serialnum,
                "pos": pos
            },
            type: "POST",
            success: function (data) {
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
                    var ret = eval('(' + data.data + ')');
                    var content =
                        "<tr>"
                            +"<td>" + ret.serialnum + "</td>"
                            +"<td>" + ret.softwarename + "</td>"
                            +"<td>" + ret.applyperson + "</td>"
                            +"<td>" + ret.certificatedate + "</td>"
                            +"<td>" + ret.salesman + "</td>"
                            +"<td>" + people + "</td>"
                            +"<td style='color:green'>" + ret.temphouseorder + "</td>"
                            +"<td style='color:green'>" + data.msg + "</td>"
                        +"</tr>";
                    $("#mainbody_exception").prepend(content);
                    var okval = $("#count_ok").text();
                    $("#count_ok").html(parseInt(okval)+1);
                } else {
                    var ret = eval('(' + data.data + ')');
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
                            +"<td>" + ret.applyperson  + "</td>"
                            +"<td>" + cdate + "</td>"
                            +"<td>" + ret.salesman + "</td>"
                            +"<td>" + people + "</td>"
                            +"<td style='color:red'>" + ret.temphouseorder + "</td>"
                            +"<td style='color:red'>" + data.msg + "</td>"
                        +"</tr>";
                    $("#mainbody_exception").prepend(content);
                    var errorval = $("#count_error").text();
                    $("#count_error").html(parseInt(errorval)+1);
                }
            }
        });
    }
</script>
</html>
