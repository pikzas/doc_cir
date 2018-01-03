Pn = {
	version : '1.0'
};
/**
 * Pn.ns('Company', 'Company.data');
 */
Pn.ns = function() {
	var a = arguments, o = null, i, j, d, rt;
	for (i = 0;i < a.length; ++i) {
		d = a[i].split(".");
		rt = d[0];
		eval('if (typeof ' + rt + ' == "undefined"){' + rt + ' = {};} o = '
				+ rt + ';');
		for (j = 1;j < d.length; ++j) {
			o[d[j]] = o[d[j]] || {};
			o = o[d[j]];
		}
	}
};
/**
 * 创建html对象
 */
Pn.create = function(a) {
	return document.createElement(a);
};
/**
 * html模板
 * 使用#()作为占位符
 */
Pn.Template = function(s) {
	this.tpl = s;
}
Pn.Template.prototype.create = function(params) {
	var s = this.tpl;
	for(var p in params) {
		s = s.replace(new RegExp("#\\("+p+"\\)", "g") ,params[p]);
	}
	return s;
}
/**
 * 获得本页面上唯一的ID
 */
Pn.UID = 0;
Pn.getUID = function() {
	Pn.UID++;
	return Pn.UID;
}
// 设置全局 AJAX 默认选项。
$.ajaxSetup( {
	dataType : "json",
	type : "POST",
	beforeSend : function(xhr) {
		xhr.setRequestHeader('isAjax', 'true');
	}
});
jQuery.extend( {
	postJson : function(url, data, success, error) {
		$.ajax( {
			url : url,
			data : data,
			success : success,
			error : error
		});
	}
});
/**
 * 获得url参数。doc：文本对象document；key：参数。返回：value。
 */
Pn.getParam = function(key) {
	var aParams = location.search.substr(1).split('&');
	var aKv;
	for (var i = 0;i < aParams.length; i++) {
		aKv = aParams[i].split('=');
		if (aKv[0] == key) {
			return aKv[1];
		}
	}
};
/**
 * 复选框全选或取消
 * 
 * @param name
 *            复选框名称
 * @param checked
 *            of boolean 全选或取消
 */
Pn.checkBox = function(name, checked) {
	$("input[name='" + name + "']").attr("checked", checked);
}
//@ TODO 是否需要
Pn.checkLen = function(o, len) {
	len = len || 1;
	if (o.val() == undefined || $.trim(o.val()) == ''
			|| $.trim(o.val()).length < len) {
		return false;
	} else {
		return true;
	}
}
Pn.Cookie = function Cookie() {
	this.cookies = [];
	/**
	 * @param name
	 */
	this.get = function(name) {
		this._reset();
		return this.cookies[name];
	}

	/**
	 * name & value must be Required
	 * 
	 * @param {string}
	 *            name (Required)
	 * @param {string}
	 *            value (Required)
	 * @param {integer}
	 *            expires (optional, [Units:seconde] , exp (one day):
	 *            1*24*60*60)
	 * @param {string}
	 *            path (optional, exp: '/')
	 * @param {string}
	 *            domain (optional, exp: '.a.com')
	 * @param {boolean}
	 *            secure (optional, exp: false)
	 */
	this.set = function() {
		var args = this.set.arguments;
		var _num = args.length;
		if (_num < 2) {
			return;
		}
		var _cookie = args[0] + '=' + this._encode(args[1]);
		if (_num >= 3) {
			var now = new Date();
			var _expires = new Date(now.getTime() + args[2]);
			_cookie += ';expires=' + _expires.toUTCString();
		}
		if (_num >= 4) {
			_cookie += ';path=' + args[3];
		}
		if (_num >= 5) {
			_cookie += ';domain=' + args[4];
		}
		if (_num >= 6) {
			_cookie += ';secure';
		}
		document.cookie = _cookie;
	}

	this.print_r = function() {
		this._reset();
		for (var i in this.cookies) {
			alert(i + ' -> ' + this.cookies[i]);
		}
	}
	this._reset = function() {
		var cookie = document.cookie.split(';');
		var _num = cookie.length;
		for (var i = 0;i < _num; i++) {
			var _arr = cookie[i].split('=');
			var _name = this._trim(_arr[0]);
			var _value = '';
			if (typeof _arr[1] != 'undefined') {
				_value = this._decode(this._trim(_arr[1]));
			}
			this.cookies[_name] = _value;
		}
	}
	this._trim = function(_str) {
		return _str.replace(/(^\s+)|(\s*$)/g, '');
	}
	this._encode = function(_str) {
		return encodeURI(_str);
	}
	this._decode = function(_str) {
		return decodeURI(_str);
	}
}


