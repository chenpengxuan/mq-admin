define(function (require, exports, module) {

    var $ = require('jquery');
    var base = require('base');
    var vn = require('vn');
    var cookies = require('cookie');
    var COOKIE_MENUS = 'cookie_menus';
    cookies.delCookie(COOKIE_MENUS);
    var menus = require('menus');
    var _layer = require('layer');

    var USERINFO_URL = "/dev/userinfo";
    var APPCOUNT_URL = "/dev/appCount";

    // 个人信息
    $.ajax({
        url: USERINFO_URL,
        success: function(data){
            if(data.status == 'SUCCESS'){
                var content = data.content;
                $("#username").text(content.username);
                $("#accountType").text(content.accountType);
                $("#canRaiseAmount").text(content.canRaiseAmount);
            }else{
                alert(data.message);
            }
        }
    });

    // 应用概况
    $.ajax({
        url: APPCOUNT_URL,
        success: function(data){
            if(data.status == 'SUCCESS'){
                var content = data.content;
                $("#putOnNum").text(content.putOnNum);
                $("#waitAuditNum").text(content.waitAuditNum);
                $("#unPassNum").text(content.unPassNum);
                $("#endNum").text(content.endNum);
            }else{
                alert(data.message);
            }
        }
    });


});

