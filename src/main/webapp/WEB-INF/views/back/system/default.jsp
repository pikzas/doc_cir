<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/include/back_authority.jsp"%>
<link href="<c:url value="/skin/back/bootstrap/css/fullcalendar.css"/>" rel="stylesheet">
<link href="<c:url value="/skin/back/fullcalendar/css/fullcalendar.print.css"/>" rel="stylesheet" media='print'>
<link href="<c:url value="/skin/back/fullcalendar/css/theme.css"/>" rel="stylesheet" >

<script src="<c:url value="/skin/back/fullcalendar/js/jquery-ui-1.10.2.custom.min.js"/>"></script>
<script src="<c:url value="/static/js/date.js"/>"></script>
<script src="<c:url value="/static/js/fullcalendar.min.js"/>"></script>
<style type="text/css">
	.fc-event{height:30px;}
	span.fc-event-title{text-align:center;}
	div>span{ line-height:20px; }
</style>
<script type="text/javascript">
    $(document).ready(function() {

        //用于日期加减和格式转换 2016/08/01--->2016-01-01


        $('#calendar').fullCalendar({
            theme: true,
            header: {
                left: 'prev,next',
                center: 'title',
                right: 'today'
            },
            monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
            monthNamesShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
            dayNames: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
            dayNamesShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
            firstDay: 1, 
            editable: false,
            buttonText: {
            	 today: '本月',
            	 month: '月',
            	 week: '周',
            	 day: '日',
            	 prev: '上一月',
            	 next: '下一月'
            },
            events: function(start,end,callback) {
                var beginDate = new Date(start).toISOString();
                var endDate = new Date(end).toISOString();
                $.ajax({
                    url: '<c:url value="/system/calendar.do"/>',
                    data : {start:beginDate,end:endDate},
                    dataType:"json",
                    success:function(ret) {
                        var events = [];
                        $.each(ret, function (i,term) {
                            events.push({
                                start: term.category,
                                title: term.total,
                                color: term.color
                            });
                        });
                        callback(events);
                    }
                });
               //逾期背景颜色
                var mm = $("tr td[data-date]");
                var today = new Date(new Date());
                for(var i=0;i<mm.length;i++){
                	var ss = mm[i];
                	var date = new Date($(ss).attr("data-date"));
                	if(date<today){
                		if(date.getFullYear()==today.getFullYear() && date.getMonth()==today.getMonth() &&　date.getDate()==today.getDate()){
                			//$(ss).css("background","#FFC3DB");
                		}else{
	                		$(ss).css("background","#FFC3DB");
                		}
                	}
                }
                
            },
        });
		
        
    });

</script>
<div class="bjui-pageHeader" style="background:#FFF;">
	<div style="padding: 0 15px;">
	</div>
</div>
<div class="bjui-pageContent">
	<div style="margin-top:5px; margin-right:0px; overflow:hidden;">
		<div class="row" style="padding: 0 8px;">
            <div class="col-md-12">
                <div id='calendar'></div>
            </div>
		</div>
	</div>
</div>