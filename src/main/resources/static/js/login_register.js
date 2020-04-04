var startFunction = function() {
	login();
}

var login = function() {
	/* 让所有元素的active状态失效 */
	$("#loginLi").removeClass("active");
	$("#registerLi").removeClass("active");
	/* 让某个元素的active状态生效 */
	$("#loginLi").addClass("active");
	/* 让某些元素隐藏，某些元素取消隐藏 */
	$("#login").show();
	$("#register").hide();
}
var register = function() {
	/* 让所有元素的active状态失效 */
	$("#loginLi").removeClass("active");
	$("#registerLi").removeClass("active");
	/* 让某个元素的active状态生效 */
	$("#registerLi").addClass("active");
	/* 让某些元素隐藏，某些元素取消隐藏 */
	$("#login").hide();
	$("#register").show();
}
//
//var websocket = null;
////浏览器是否支持
//if ('WebSocket' in window) {
//	// 上面我们给webSocket定位的路径
//	websocket = new WebSocket('ws://localhost:8080/webSocket');
//} else {
//	alert('该浏览器不支持websocket!');
//}
////建立连接
//websocket.onopen = function(event) {
//	console.log('客户端建立连接');
//}
////关闭连接
//websocket.onclose = function(event) {
//	console.log('客户端关闭连接');
//}
////消息来的时候的事件
//websocket.onmessage = function(event) {
//	// 这里event.data就是我们从后台推送过来的消息
//	console.log('客户端接收消息:' + event.data);
//	alert( event.data);
//}
//
////发生错误时
//websocket.onerror = function() {
//	alert('websocket通信发生错误！');
//}
////窗口关闭时，Websocket关闭
//window.onbeforeunload = function() {
//	console.log('客户端关闭连接');
//	websocket.close();
//}

/*var x = function() {
	var a = $("#a").val();
	var b = $("#b").val();
	console.log(a + "," + b);
	$.ajax({
		url : "/authentication/authenticate",
		type : "POST",
		data : {
			"username" : a,
			"password" : b,
		},
		dataType : "JSON",
		async : false,
		success : function(result) {
			console.log("成功信息如下：");
			console.log(result);
		},
		error : function(err) {
			console.log("错误信息如下");
			console.log(err);
		}
	});
}*/
