/*
 *  
 *  (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *  
 */

'use strict';

var app = angular.module('BlurAdmin', [
  'ngAnimate',
  'ui.bootstrap',
  'ui.sortable',
  'ui.router',
  'ngTouch',
  'toastr',
  'smart-table',
  "xeditable",
  'ui.slimscroll',

  'BlurAdmin.theme',
  'BlurAdmin.pages'
]);

app.filter('limit', function () {
  return function(value,maxLength) {
    //还可以这样取
    //var args = Array.prototype.slice.call(arguments);
    //console.log(args);
    if(!angular.isString(value)) {
      return value;
    }
    if(value.length > maxLength){
      return value.substr(0,maxLength)+"...";
    }
    return value.replace(/^\s+|\s+$/g, '');
  };
});



addInterceptor(app);

function logout () {
  $.ajax({
    url: "/logout",
    success: function(data){
      // if(data.success){
        window.location = "login.html";
      // }
    }
  });
}

$.ajaxSetup({
  error: function (XMLHttpRequest, textStatus, errorThrown) {
    if(errorThrown == "Unauthorized"){
      location.href = "login.html";
    }
  }
});


var COOKIE_UserVo = 'userVo';
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
};

cookie.getCookie = function (name) {
  var strCookie = document.cookie;
  var arrCookie = strCookie.split("; ");
  for (var i = 0; i < arrCookie.length; i++) {
    var arr = arrCookie[i].split("=");
    if (arr[0] == name) return arr[1];
  }
  return "";
};

cookie.delCookie = function (name) {
  var exp = new Date();
  exp.setTime(exp.getTime() - 1);
  var cval = cookie.getCookie(name);
  if (cval != null){
    document.cookie = name + "=" + cval + "; path=/;expires=" + exp.toGMTString();
  }
};

function getCurrentUser() {
  var userVo = {};

  if(cookie.getCookie(COOKIE_UserVo)){
    var userVoStr = "";
    try{
      userVoStr = decodeURIComponent(cookie.getCookie(COOKIE_UserVo));
    }catch(e){
      userVoStr = unescape(cookie.getCookie(COOKIE_UserVo));
    }
    userVo = JSON.parse(userVoStr);
  }
  return userVo;
}

window.isAdmin = false;
$.ajax({
  async:false,
  url: "/getCurrentUser",
  success: function(data){
    if(data.success){
      cookie.addCookie(COOKIE_UserVo,JSON.stringify(data.content));
      if(data.content.role == 9 ){
        window.isAdmin = true;
        var array =  [
          'ui.router',

          // 'BlurAdmin.pages.dashboard',
          'BlurAdmin.pages.message',
          'BlurAdmin.pages.dispatch_detail',
          'BlurAdmin.pages.appconfig',
          'BlurAdmin.pages.callback',
          'BlurAdmin.pages.permission',
          // 'BlurAdmin.pages.logger'
        ];
        angular.module('BlurAdmin.pages',array)
      }
    }else{
      location.href = "login.html";
    }
  }
});


function addInterceptor(app) {
  // 定义一个 Service ，稍等将会把它作为 Interceptors 的处理函数
  app.factory('HttpInterceptor', ['$q', HttpInterceptor]);

  function HttpInterceptor($q) {
    return {
      requestError: function(err){
        return $q.reject(err);
      },
      response: function(res){
        return res;
      },
      responseError: function(err){
        var status = err.status;
        if(status == 401){
          location.href = "login.html";
        }
        return $q.reject(err);
      }
    };
  }

// 添加对应的 Interceptors
  app.config(['$httpProvider', function($httpProvider){
    $httpProvider.interceptors.push(HttpInterceptor);
  }]);
}

function isEmpty(val) {
  var isEmpty = val === null || val === undefined || val === "" || (angular.isArray(val) && val.length === 0);
  return isEmpty;
}

function popupAlert(msg){
  layer.alert(msg,{closeBtn: 0},function (index) {
    layer.close(index)
  });
}

$(document).on('click',"[info='true']",function () {
  var that = this;
  var str = $(this).attr('value');

  layer.tips(str, that,{
    tips: [2, '#3595CC'],
    time: 3000,
    maxWidth: 600,
  });
});