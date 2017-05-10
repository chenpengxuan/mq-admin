/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.pages.callback')
      .controller('callbackListCtrl', callbackListCtrl);
  
  /** @ngInject */
  function callbackListCtrl($scope, $state,$http,$timeout) {

    $scope.isAdmin = isAdmin;

    //获取数据源
    $http({
      url: "config/getAllApp",
      method: 'GET'
    }).success(function (data) {
      if (data.success) {
        $scope.appIdList = data.content;
        $scope.callback.appId = $scope.appIdList[0];
        $scope.callbackSelectAppId();
        $timeout(function(){
          $('#appId').selectpicker({
            liveSearch: true
          });

          $('#callbackAppId').selectpicker({
            liveSearch: true
          });
        }, 100);
      }
    });

    $scope.selectAppId = function(){
      var appId = $scope.obj.appId;
      $http({
        url: "config/getAllQueueByAppId?appId="+appId,
        method: 'GET'
      }).success(function (data) {
        if (data.success) {
          $scope.queueCodeList = data.content;

          $timeout(function(){
            $('#queueCode').selectpicker({
              liveSearch: true

            });
            $('#queueCode').selectpicker('refresh');
          }, 100);
        }
      });
    };

    $scope.search = function(){

      //构建查询需要的参数
      var param = {
        appId:$scope.obj.appId,
        queueCode:$scope.obj.queueCode,
        callbackUrl:$scope.obj.callbackUrl
      };

      var postData = $("#jqGrid").jqGrid("getGridParam", "postData");

      // 将查询参数融入postData选项对象
      $.extend(postData, param);

      $("#jqGrid").jqGrid("setGridParam", {
        search: true    // 将jqGrid的search选项设为true
      }).trigger("reloadGrid", [{page:1}]);   // 重新载入Grid

    };


    $("#jqGrid").jqGrid({
      url: 'appConfig/callbackList',
      mtype: "POST",
      datatype: "json",
      // caption:"消息列表",
      colModel: [
        { label: '应用编号', name: 'appId',width: 150,sortable:false},
        { label: '业务代码', name: 'queueCode', width: 150,sortable:false},
        { label: '订阅者编号', name: 'callbackConfig.callbackKey', width: 180 ,sortable:false},
        { label: '回调地址', name: 'callbackConfig.url', width: 300 ,sortable:false},
        { label: '超时时间(ms)', name: 'callbackConfig.callbackTimeOut', width: 70 ,sortable:false},
        { label: '并发度', name: 'callbackConfig.parallelismNum', width: 50 ,sortable:false},
        { label: '订阅者AppId', name: 'callbackConfig.callbackAppId', width: 180,sortable:false},
        { label: '重试时间(分)', name: 'callbackConfig.retryTimeout', width: 75,sortable:false},
        { label: '秒级补单', name: 'callbackConfig.secondCompensateSpan', width: 50,sortable:false},
        {
          label: '是否重试', name: 'callbackConfig.isRetry', width: 50 ,sortable:false,
          formatter:function(cellvalue, options, rowObject){
            return cellvalue == '1';
          }
        },
        { label: '是否启用', name: 'callbackConfig.enable', width: 50 ,sortable:false},
        {
          label: '操作', name: 'callbackConfig.callbackKey', width: 40 ,sortable:false,hidden:!isAdmin,
          formatter:function(cellvalue, options, rowObject){
            return "<a href='javascript:void(0)' onclick='editCallback(\""+rowObject.appId +"\",\"" + rowObject.queueCode +"\",\"" +cellvalue+"\")'>编辑</a>";
          }
        }
      ],
      loadonce : false,
      autowidth: true,
      shrinkToFit: true,
      height: 'auto',
      rowNum: 20,
      pager: "#jqGridPager",
      sortable:false,
      viewrecords: true,
      scrollrows:true,
      toppager:true,
      prmNames: {
        page:"page",
        rows:"size"
      },
      jsonReader: {
        root: "content.content",
        total: "content.totalPages",      //总页数
        records: "content.totalElements"  //总记录数
      },
      loadComplete: function(data) {
        if(!data.success){
          layer.alert(data.message,{closeBtn: 0},function (index) {
            layer.close(index)
          });
          return;
        }
      },
      beforeRequest:function () {
        var postData = $("#jqGrid").jqGrid("getGridParam", "postData");
        postData.page--;
      }
    });

    $('#jqGrid').jqGrid('navGrid',"#jqGridPager", {
      search: false, // true show search button on the toolbar
      add: false,
      edit: false,
      del: false,
      refresh: true
    });

    $("#jqGrid").jqGrid('bindKeys');


    $scope.callbackSelectAppId = function(){
      var appId = $scope.callback.appId;
      $http({
        url: "config/getAllQueueByAppId?appId="+appId,
        method: 'GET'
      }).success(function (data) {
        if (data.success) {
          $scope.callbackQueueCodeList = data.content;
          $scope.callback.queueCode = $scope.callbackQueueCodeList[0];

          $timeout(function(){
            $('#callbackQueueCode').selectpicker({
              liveSearch: true

            });
            $('#callbackQueueCode').selectpicker('refresh');
          }, 100);
        }
      });
    };

    
    $scope.saveCallback = function () {
      if(isEmpty($scope.callback.callbackAppId)){
        popupAlert("订阅者AppId不能为空!");
        return;
      }
      if(isEmpty($scope.callback.url)){
        popupAlert("回调地址不能为空!");
        return;
      }

      if(isEmpty($scope.callback.callbackTimeOut)){
        popupAlert("超时时间不能为空!");
        return;
      }

      $http({
        url: "appConfig/callback/save/" + $scope.callback.appId + "/"+$scope.callback.queueCode,
        method: 'POST',
        params:$scope.callback
      }).success(function (data) {
        if (data.success) {
          layer.alert(data.message,{closeBtn: 0},function (index) {
            layer.close(index);
            layer.close($scope.index);
            $scope.search();
          });

        }else{
          popupAlert(data.message);
        }
      });
    };
    $scope.callback = {};
    $scope.callback.new = true;

    $scope.create = function(){

      $scope.callback.new = true;

      $scope.callback.callbackKey='';
      $scope.callback.callbackAppId =  '';
      $scope.callback.url =  '';
      $scope.callback.enable =  'true';
      $scope.callback.onlyStgEnable =  'false';
      $scope.callback.description =  '';

      $scope.callback.callbackTimeOut =  5000;
      $scope.callback.httpMethod =  'POST';
      $scope.callback.contentType =  'application/json';
      $scope.callback.parallelismNum =  2;
      $scope.callback.isRetry =  '1';
      $scope.callback.retryTimeout =  30;
      $scope.callback.retryPolicy =  '1m-5m-10m-30m-1h-4h-8h-15h-1d-2d';
      $scope.callback.secondCompensateSpan =  0;
      $scope.callback.abandonQueue =  'false';

      $scope.index = layer.open({
        type: 1,
        title: "添加订阅者",
        closeBtn: 1,
        area: '616px',
        skin: 'layui-layer-rim',
        shadeClose: true,
        scrollbar: false,
        content: $('#callbackContent'),
        end:function () {}
      });
    };

    window.editCallback = function (appId,queueCode,callbackKey) {

      $scope.callback.new = false;

      $http({
        url: "appConfig/callback/" + appId + "/" + queueCode + "/" + callbackKey,
        method: 'GET'
      }).success(function (data) {
        if (data.success) {
          $scope.callback = data.content;

          $scope.callback.appId = appId;
          $scope.callback.queueCode = queueCode;

          if(data.content.enable == null ){
            $scope.callback.enable = 'true';
          }else{
            $scope.callback.enable = data.content.enable + "";
          }
          if(data.content.onlyStgEnable == null ){
            $scope.callback.onlyStgEnable = 'false';
          }else{
            $scope.callback.onlyStgEnable = data.content.onlyStgEnable + "";
          }

          if(data.content.abandonQueue == null ){
            $scope.callback.abandonQueue = 'false';
          }else{
            $scope.callback.abandonQueue = data.content.abandonQueue + "";
          }

          $scope.callback.isRetry = data.content.isRetry + '';
          // $scope.$apply();//需要手动刷新

          $scope.index = layer.open({
            type: 1,
            title: "编辑订阅者",
            closeBtn: 1,
            area: '616px',
            scrollbar: false,
            skin: 'layui-layer-rim',
            shadeClose: true,
            content: $('#callbackContent'),
            end:function () {}
          });
        }else{
          popupAlert(data.message);
        }
      });

    };
  }
})();
