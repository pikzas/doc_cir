// JavaScript Document
$(function(){
	//搜索
	$("div.top div.floatright a:last").css("background","none");
	var defaulttext = "输入版权DCI编码或APP名搜索版权认证信息";
	$("div.search input.keywords").val(defaulttext);
	$("div.search input.keywords").live("focus",function(){
		if($(this).val() == defaulttext)
		{
			$(this).val("");
		}
	});
	$("div.search input.keywords").live("blur",function(){
		if($(this).val() == "")
		{
			$(this).val(defaulttext);
		}
	});
	$("div.foot div.main div.link a:last").css("background","none");
	$("div.foot div.main div.footnav ul.footnav li:last").css("margin","0");
	$("div.lc ul.lcbody li:last").css("margin","0");
	$("ul.lctext li:first").css("width","130px").css("text-align","left");
	$("ul.lctext li:last").css("width","115px").css("text-align","right");
	$("div.iden_lc ul.lcbody li:last").css("margin","0");
	$("ul.iden_lctext li:last").css("width","93px").css("text-align","right");
	//站内消息
	$("div.mainbody div.main:last").css("margin-bottom","36px");
	$("ul.msglist").hide();
	$("div.msg").live("mouseover",function(){
		$(this).addClass("current");
		$("ul.msglist").show();
	});
	$("div.msg").live("mouseout",function(){
		$(this).removeClass("current");
		$("ul.msglist").hide();
	});

});

function placeholder(css) {
	/*
    if (navigator.userAgent.toLowerCase().indexOf("chrome") == -1) {
        $("input").each(function () {
            if (typeof ($(this).attr("placeholder")) != "undefined") {
                var pcolor = "#999999"; //颜色
                placeholder = $(this).attr("placeholder");	
                var temp = "<div name='temp' style='margin:0; padding:0; border:0;color:" + pcolor + ";cursor:text;width:"+($(this).width())+"px;" + css + "'>" + placeholder + "</div>";
                if($(this).hasClass("code"))
				{
					var temp = "<div name='temp' style='margin:0; padding:0; border:0;color:" + pcolor + ";cursor:text;width:"+($(this).width())+"px;" + css + "'>" + placeholder + "</div>";
				}
				$(this).hide().after(temp);

                $("div[name=temp]").live("click", function () {
                    $(this).hide().prev().show().focus();
                });

                $(this).live("blur", function () {
                    if ($(this).val() == "") {
                        $(this).hide().next().show();
                    }
                });
            }
        });
		
    }*/
}

function showerr(errTitle, errinfo) {
    var jBoxConfig = {};
    jBoxConfig.defaults = {
        id: null, /* 在页面中的唯一id，如果为null则自动生成随机id,一个id只会显示一个jBox */
        top: '40%' /* 窗口离顶部的距离,可以是百分比或像素(如 '100px') */
    };
    $.jBox.setDefaults(jBoxConfig);
    $.jBox.prompt(errinfo, errTitle, 'error');
}

function showtip(url, tiptitle, tipwidth, tipheight, tiptop) {
    // 用iframe显示http://www.baidu.com的内容，并设置了标题、宽与高、按钮
    $.jBox("iframe:" + url + "", {
        title: tiptitle,
        width: tipwidth,
        height: tipheight,
        top: tiptop
    });
}

function addFavorite2() {
        var url = window.location;
        var title = document.title;
        var ua = navigator.userAgent.toLowerCase();
        if (ua.indexOf("360se") > -1) {
            alert("由于360浏览器功能限制，请按 Ctrl+D 手动收藏！");
        } else if (ua.indexOf("msie 8") > -1) {
            window.external.AddToFavoritesBar(url, title); //IE8
        } else if (document.all) {
            try {
                window.external.addFavorite(url, title);
            } catch (e) {
                alert('您的浏览器不支持,请按 Ctrl+D 手动收藏!');
            }
        } else if (window.sidebar) {
            window.sidebar.addPanel(title, url, "");
        } else {
            alert('您的浏览器不支持,请按 Ctrl+D 手动收藏!');
        }
    }