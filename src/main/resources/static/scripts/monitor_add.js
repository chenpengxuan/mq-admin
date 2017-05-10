/*
 *
 *  (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.pages.message')
      .controller('monitorAddCtrl', monitorAddCtrl);

  /** @ngInject */
  function monitorAddCtrl($scope,$stateParams,$state,$http) {

    $scope.obj = {};
      
    $('#cronReadMe').on('click', function(){
      var that = this;
      var str =
           "Cron表达式的格式：秒 分 时 日 月 周 年(可选)。<br/>" +
           "字段名                 允许的值                        允许的特殊字符 <br/>" +
            "秒                         0-59                               , - * / <br/>"+
            "分                         0-59                               , - * / <br/>"+
            "小时                       0-23                               , - * /<br/>" +
            "日                         1-31                               , - * ? / L W C<br/>" +
            "月                         1-12 or JAN-DEC         , - * /<br/>" +
            "周几                     1-7 or SUN-SAT           , - * ? / L C #  <br/>" +
            "年 (可选字段)     empty, 1970-2099      , - * /<br/>"+
            "“?”字符：表示不确定的值<br/>"+
            "“,”字符：指定数个值<br/>"+
            "“-”字符：指定一个值的范围<br/>"+
            "“/”字符：指定一个值的增加幅度。n/m表示从n开始，每次增加m<br/>"+
            "“L”字符：用在日表示一个月中的最后一天，用在周表示该月最后一个星期X<br/>"+
            "“W”字符：指定离给定日期最近的工作日(周一到周五)<br/>"+
            "“#”字符：表示该月第几个周X。6#3表示该月第3个周五<br/>"+
            "每隔5秒执行一次：*/5 * * * * ?<br/>"+
            "每隔1分钟执行一次：0 */1 * * * ?<br/>"+
            "每天23点执行一次：0 0 23 * * ?<br/>"+
            "每天凌晨1点执行一次：0 0 1 * * ?<br/>"+
            "每月1号凌晨1点执行一次：0 0 1 1 * ?<br/>"+
            "每月最后一天23点执行一次：0 0 23 L * ?<br/>"+
            "每周星期天凌晨1点实行一次：0 0 1 ? * L<br/>"+
            "在26分、29分、33分执行一次：0 26,29,33 * * * ?<br/>"+
            "每天的0点、13点、18点、21点都执行一次：0 0 0,13,18,21 * * ?<br/>";

      layer.tips(str, that,{
          tips: [2, '#3595CC'],
          time: -1,
          maxWidth: 600,
          closeBtn:true
        });
    });

    $state.current.title = "创建监控";
    var id = $stateParams.id;
    var isUpdate = false;
    if (id != "") {
      $state.current.title = "修改监控";
      isUpdate = true;
      $http({
        url: "/monitor/get",
        method: 'GET',
        params: {id: id}
      }).success(function (data) {
        if (data.success) {
          $scope.obj = data.content;
        }
      });
    }

    //获取数据源
    $http({
      url: "/dbSource/getAll",
      method: 'GET'
    }).success(function (data) {
      if (data.success) {
        $scope.dbSources = data.content;
      }
    });


    $scope.submit = function(){
        var url = "/monitor/add";
        if (isUpdate) {
            url = "/monitor/modify";
        }
        $.ajax({
            type: "POST",
            url: url,
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify($scope.obj),
            success: function (data) {
                    if(data.success){
                        if(isUpdate){
                            layer.alert("保存成功",{closeBtn: 0},function (index) {
                                layer.close(index);
                                $state.reload();
                            });
                        }else{
                            $state.go("monitor.monitor-list");
                        }
                    }else{
                        layer.alert(data.message);
                    }
                }
        });

    };

    $scope.goBack = function(){
      $state.go('monitor.monitor-list')
    };

  }

})();
