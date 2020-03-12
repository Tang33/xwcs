(function() {
	window['aui'] = {}
	//title:标题     contents:内容  state:状态('success','warning','error')
	function alert(title, contents,state) {
		//alert(contents);
		if (contents == null || contents== '' || contents == undefined) {
			contents = title;
			title = '温馨提示：';
		}
		if (state == null || state== '' || state == undefined) {
			swal(title, contents);
		}else{
			swal(title, contents,state);
		}
	}
	function confirm(objs) {
		swal({
			title: objs.title,
			text: objs.text,
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: '#DD6B55',
			confirmButtonText: '确 认'
		},
		function(){
			objs.success();
		});
	}
	function error(title, contents) {
		if (contents == null || contents== '' || contents == undefined) {
			contents = title;
			title = '错误提示：';
		}
		swal(title, contents, "error");
			/*layer.open({
				type:1,
				title : title,
				content : contents,
				icon : 5
			});*/
	}
//	字符循环拼接
	function repeatStr (str, count) {
		var text = '';
		for (var i = 0; i < count; i++) {
			text += str;
		}
		return text;
	}
//	字符串替换(字符串,要替换的字符,替换成什么)
	function replaceAll(str,AFindText,ARepText){
		raRegExp = new RegExp(AFindText,"g");
		return str.replace(raRegExp,ARepText);
	}
//	检测字符串，包括了日常处理的一些手机，邮箱，字母大小写验证
	function checkType (str, type) {
		switch (type) {
		case 'email':
			return /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/.test(str);
		case 'phone':
			return /^1[3|4|5|7|8][0-9]{9}$/.test(str);
		case 'tel':
			return /^(0\d{2,3}-\d{7,8})(-\d{1,4})?$/.test(str);
		case 'number':
			return /^[0-9]$/.test(str);
		case 'english':
			return /^[a-zA-Z]+$/.test(str);
		case 'chinese':
			return /^[\u4E00-\u9FA5]+$/.test(str);
		case 'lower':
			return /^[a-z]+$/.test(str);
		case 'upper':
			return /^[A-Z]+$/.test(str);
		default :
			return true;
		}
	}
//	获取，设置url参数，url 参数就是其中 ? 后面的参数
	function getUrlPrmt(url) {
		url = url ? url : window.location.href;
		let _pa = url.substring(url.indexOf('?') + 1), _arrS = _pa.split('&'), _rs = {};
		for (let i = 0, _len = _arrS.length; i < _len; i++) {
			let pos = _arrS[i].indexOf('=');
			if (pos == -1) {
				continue;
			}
			let name = _arrS[i].substring(0, pos), value = window.decodeURIComponent(_arrS[i].substring(pos + 1));
			_rs[name] = value;
		}
		return _rs;
	}
//	这里是设置url参数的函数，如果对象中有null和undefined，则会自动过滤。
	function setUrlPrmt(obj) {
		let _rs = [];
		for (let p in obj) {
			if (obj[p] != null && obj[p] != '') {
				_rs.push(p + '=' + obj[p])
			}
		}
		return _rs.join('&');
	}
//	获取文件后缀名的方法
	function getSuffix(file_name) {
		var result = /[^\.]+$/.exec(file_name);
		return result;
	}
	//判断`obj`是否为空
	function isEmptyObject(obj) {
		if (!obj || typeof obj !== 'object' || Array.isArray(obj))
			return false
			return !Object.keys(obj).length
	}
	function digitUppercase(n) {
		var fraction = ['角', '分'];
		var digit = [
			'零', '壹', '贰', '叁', '肆',
			'伍', '陆', '柒', '捌', '玖'
			];
		var unit = [
			['元', '万', '亿'],
			['', '拾', '佰', '仟']
			];
		var head = n < 0 ? '欠' : '';
		n = Math.abs(n);
		var s = '';
		for (var i = 0; i < fraction.length; i++) {
			s += (digit[Math.floor(n * 10 * Math.pow(10, i)) % 10] + fraction[i]).replace(/零./, '');
		}
		s = s || '整';
		n = Math.floor(n);
		for (var i = 0; i < unit[0].length && n > 0; i++) {
			var p = '';
			for (var j = 0; j < unit[1].length && n > 0; j++) {
				p = digit[n % 10] + unit[1][j] + p;
				n = Math.floor(n / 10);
			}
			s = p.replace(/(零.)*零$/, '').replace(/^$/, '零') + unit[0][i] + s;
		}
		return head + s.replace(/(零.)*零元/, '元')
		.replace(/(零.)+/g, '零')
		.replace(/^整$/, '零元整');
	}
	//拼接&符号
	function evals(obj) {
		if (!obj) return '';
		var pairs = [];
		for (var key in obj) {
			var value = obj[key];
			if (value instanceof Array) {
				for (var i = 0; i < value.length; ++i) {
					pairs.push(encodeURIComponent(key + '[' + i + ']') + '=' + encodeURIComponent(value[i]));
				}
				continue;
			}
			pairs.push(encodeURIComponent(key) + '=' + encodeURIComponent(obj[key]));
		}
		return pairs.join('&');
	}
	function getKeyName(keycode) {
		var keyCodeMap = {
				8: 'Backspace',
				9: 'Tab',
				13: 'Enter',
				16: 'Shift',
				17: 'Ctrl',
				18: 'Alt',
				19: 'Pause',
				20: 'Caps Lock',
				27: 'Escape',
				32: 'Space',
				33: 'Page Up',
				34: 'Page Down',
				35: 'End',
				36: 'Home',
				37: 'Left',
				38: 'Up',
				39: 'Right',
				40: 'Down',
				42: 'Print Screen',
				45: 'Insert',
				46: 'Delete',
				48: '0',
				49: '1',
				50: '2',
				51: '3',
				52: '4',
				53: '5',
				54: '6',
				55: '7',
				56: '8',
				57: '9',
				65: 'A',
				66: 'B',
				67: 'C',
				68: 'D',
				69: 'E',
				70: 'F',
				71: 'G',
				72: 'H',
				73: 'I',
				74: 'J',
				75: 'K',
				76: 'L',
				77: 'M',
				78: 'N',
				79: 'O',
				80: 'P',
				81: 'Q',
				82: 'R',
				83: 'S',
				84: 'T',
				85: 'U',
				86: 'V',
				87: 'W',
				88: 'X',
				89: 'Y',
				90: 'Z',
				91: 'Windows',
				93: 'Right Click',
				96: 'Numpad 0',
				97: 'Numpad 1',
				98: 'Numpad 2',
				99: 'Numpad 3',
				100: 'Numpad 4',
				101: 'Numpad 5',
				102: 'Numpad 6',
				103: 'Numpad 7',
				104: 'Numpad 8',
				105: 'Numpad 9',
				106: 'Numpad *',
				107: 'Numpad +',
				109: 'Numpad -',
				110: 'Numpad .',
				111: 'Numpad /',
				112: 'F1',
				113: 'F2',
				114: 'F3',
				115: 'F4',
				116: 'F5',
				117: 'F6',
				118: 'F7',
				119: 'F8',
				120: 'F9',
				121: 'F10',
				122: 'F11',
				123: 'F12',
				144: 'Num Lock',
				145: 'Scroll Lock',
				182: 'My Computer',
				183: 'My Calculator',
				186: ';',
				187: '=',
				188: ',',
				189: '-',
				190: '.',
				191: '/',
				192: '`',
				219: '[',
				220: '\\',
				221: ']',
				222: '\''
		};
		if (keyCodeMap[keycode]) {
			return keyCodeMap[keycode];
		} else {
			console.log('Unknow Key(Key Code:' + keycode + ')');
			return '';
		}
	}

	window['aui']['alert'] = alert;
	window['aui']['confirm'] = confirm;
	window['aui']['error'] = error;
	window['aui']['repeatStr'] = repeatStr;
	window['aui']['replaceAll'] = replaceAll;
	window['aui']['checkType'] = checkType;
	window['aui']['getUrlPrmt'] = getUrlPrmt;
	window['aui']['setUrlPrmt'] = setUrlPrmt;
	window['aui']['getSuffix'] = getSuffix;
	window['aui']['isEmptyObject'] = isEmptyObject;
	window['aui']['digitUppercase'] = digitUppercase;
	window['aui']['evals'] = evals;
	window['aui']['getKeyName'] = getKeyName;
})();