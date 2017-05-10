define(function (require, exports, module) {

    var cookie = {};

    cookie.addCookie = function (name, value, expiresHours) {
        var cookieString = name + "=" + escape(value) + '; path=/';
        //判断是否设置过期时间
        if (expiresHours > 0) {
            var date = new Date();
            date.setTime(date.getTime() + expiresHours * 3600 * 1000);
            cookieString = cookieString + "; expires=" + date.toGMTString();
        }
        document.cookie = cookieString;
    }

    cookie.getCookie = function (name) {
        var strCookie = document.cookie;
        var arrCookie = strCookie.split("; ");
        for (var i = 0; i < arrCookie.length; i++) {
            var arr = arrCookie[i].split("=");
            if (arr[0] == name) return arr[1];
        }
        return "";
    }

    cookie.delCookie = function (name) {
        var exp = new Date();
        exp.setTime(exp.getTime() - 1);
        var cval = cookie.getCookie(name);
        if (cval != null){
            document.cookie = name + "=" + cval + "; path=/;expires=" + exp.toGMTString();
        }
    }

    module.exports = cookie;

});