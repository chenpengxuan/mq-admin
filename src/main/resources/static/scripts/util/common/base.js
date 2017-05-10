define(function(require, exports, module) {

    var $ = jQuery = require('jquery');
    var cookies = require('cookie');

    var base = {};

    base.SUCCESS = "SUCCESS";
    base.FAIL = "FAIL";

    // 网站根目录
    window.base_path = location.protocol + '//' + location.host;
    var COOKIE_MENUS = 'cookie_menus';
    var LOGOUT_URL = base_path + "/logout";
    var LOGIN_URL = base_path + "/login.html";

    // 全局ajax设置未jsonp请求，通用错误处理
    $.ajaxSetup({
        dataType: "jsonp",
        jsonp: 'jsonp_callback',
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            vn.log(textStatus);
            vn.log(errorThrown);
        }
    });

    $(document).on("click", "a[href='none']", function(e){
        e.preventDefault(); // 取消事件的默认操作
    })

    // 登录按钮点击事件
    $("#logout").click(function(e){

        e.preventDefault();

        $.ajax({
            url: LOGOUT_URL,
            success: function(data){

                cookies.delCookie(COOKIE_MENUS);

                if(data.status == 'SUCCESS'){
                    window.location = LOGIN_URL;
                }else if(data.status == 'ERROR'){
                    window.location = LOGIN_URL;
                }else{
                    message.text("退出遇未知错误！");
                }
            }
        });
    });


    function getQueryString(name, url) {
        var str = url || document.location.search || document.location.hash,
            result = null;

        if (!name || str === '') {
            return result;
        }

        result = str.match(
            new RegExp('(^|&|[\?#])' + name + '=([^&]*)(&|$)')
        );

        return result === null ? result : decodeURIComponent(result[2]);
    }

    base.getQueryString = getQueryString;
    module.exports = base;

});