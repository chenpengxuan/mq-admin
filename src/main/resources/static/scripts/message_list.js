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
      .controller('listCtrl', listCtrl);

  /** @ngInject */
  function listCtrl($scope, $state,$http,$filter,$timeout) {

    $scope.isAdmin = isAdmin;
    $scope.obj = {};

    var firstTime = true;

    //获取数据源
    $http({
      url: "config/getAllApp",
      method: 'GET'
    }).success(function (data) {
      if (data.success) {
        $scope.appIdList = data.content;
        $scope.obj.appId = $scope.appIdList[0];
        $scope.selectAppId();
        $timeout(function(){
          $('#appId').selectpicker({
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
            $scope.obj.queueCode = $scope.queueCodeList[0];

            $timeout(function(){
              $('#queueCode').selectpicker({
                liveSearch: true

              });
              $('#queueCode').selectpicker('refresh');
            }, 100);
          }
        });
    };

    var dateSetting = {
      language:  'zh-CN',
      weekStart: 1,
      todayBtn:  1,
      todayHighlight: 0,
      autoclose: 1,
      minView:2,
      keyboardNavigation:false,
      startView: 2,
      forceParse: 0,
      showMeridian: 0,
      initialDate:new Date()
    };

    $('#startTime').datetimepicker(dateSetting).on('changeDate', function(e){

      $('#endTime').datetimepicker("setStartDate",new Date(e.date.getFullYear(), e.date.getMonth(), e.date.getDate() + 1));
      var nextMonthFirstDay = new Date(e.date.getFullYear(), e.date.getMonth()+1);
      var lastDay = new Date(nextMonthFirstDay - 86400000);
      $('#endTime').datetimepicker("setEndDate",lastDay);
    });

    $('#endTime').datetimepicker(dateSetting);

    function getMonthLastDay(month) {
      var now = new Date();
      var nextMonth = month ? new Date(month.substring(0, 4) * 1, month.substring(5, 7) * 1) : new Date(now.getFullYear(), now.getMonth() + 1);
      var date = new Date(nextMonth - 86400000);
      return date;
    }

    var pattern = "YYYY-MM-DD 00:00";
    $scope.obj.startTime = laydate.now(0,pattern);
    $scope.obj.endTime = laydate.now(1,pattern);

    $('#endTime').datetimepicker("setEndDate",getMonthLastDay($scope.obj.startTime));

    function validate(){

      if(isEmpty($scope.obj.appId)){
        popupAlert("应用编号必选!");
        return false;
      }
      if(isEmpty($scope.obj.queueCode)){
        popupAlert("业务代码必选!");
        return false;
      }

      if(isEmpty($scope.obj.startTime)){
        popupAlert("创建开始时间必填!");
        return false;
      }
      if(isEmpty($scope.obj.endTime)){
        popupAlert("创建结束时间必填!");
        return false;
      }
      return true;
    }
    $scope.search = function(){
      firstTime = false;

      //构建查询需要的参数
      var param = {
        appId:$scope.obj.appId,
        queueCode:$scope.obj.queueCode,
        startTime:$scope.obj.startTime,
        endTime:$scope.obj.endTime,
        bizId:$scope.obj.bizId
      };

      var postData = $("#jqGrid").jqGrid("getGridParam", "postData");

      // 将查询参数融入postData选项对象
      $.extend(postData, param);

      $("#jqGrid").jqGrid("setGridParam", {
        search: true    // 将jqGrid的search选项设为true
      }).trigger("reloadGrid", [{page:1}]);   // 重新载入Grid

    };

    $scope.create = function(){
      $state.go("monitor.monitor-add");
    };

    function genButton(clazz,data,msg) {
      return '<button title="'+msg+'" style="margin-left: 5px;" class="btn btn-sm '+ clazz +'">'+data+'</button>'
    }

    $("#jqGrid").jqGrid({
      url: 'message/list',
      mtype: "POST",
      datatype: "json",
      // caption:"消息列表",
      colModel: [
        { label: '消息id', name: 'id',hidden:true},
        { label: '应用编号', name: 'appId',width: 150,sortable:false},
        { label: '业务代码', name: 'queueCode', width: 150,sortable:false},
        { label: '消息UUID', name: 'id', width: 180 ,sortable:false},
        { label: '消息编号', name: 'bizId', width: 180 ,sortable:false},
        { label: '创建时间', name: 'createTime', width: 120 ,sortable:false},
        {
          label: '处理结果', name: 'result', width: 150 ,sortable:false,
          formatter:function(cellvalue, options, rowObject){
            return genButton("btn-info",rowObject.initCount,"初始化数")  +
            genButton("btn-success",rowObject.successCount,"成功数")  +
            genButton("btn-danger",rowObject.failCount,"失败数")  +
            genButton("btn-warning",rowObject.compensateCount,"补单数");
          }
        },
        { label: '客户端Ip', name: 'clientIp', width: 90 ,sortable:false},
        { label: '接收站Ip', name: 'recvIp', width: 90 ,sortable:false},
        { label: '消息体', name: 'body',width:300,sortable:false}
      ],
      loadonce : false,
      autowidth: true,
      shrinkToFit: true,
      autoScroll: true,
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
         if(firstTime || !validate()){
           return false;
         }
        var postData = $("#jqGrid").jqGrid("getGridParam", "postData");
        postData.page--;
      },
      subGrid: true, // set the subGrid property to true to show expand buttons for each row
      subGridRowExpanded: showChildGrid // javascript function that will take care of showing the child grid
    });

    function showChildGrid(parentRowID, parentRowKey) {
      var childGridID = parentRowID + "_table";

      // add a table and pager HTML elements to the parent grid row - we will render the child grid here
      $('#' + parentRowID).append('<table id=' + childGridID + '></table>');

      $("#" + childGridID).jqGrid({
        url: "/message/detail/" + $scope.obj.appId + "/" + $scope.obj.queueCode + "/" + parentRowKey,
        mtype: "GET",
        datatype: "json",
        page: 0,
        colModel: [
          { label: 'id', name: 'id', key: true,hidden:true },
          { label: '订阅者ID', name: 'consumerId', width: 100 ,sortable:false},
          { label: '订阅者Url', name: 'callbackUrl', width: 300 ,sortable:false},
          { label: '创建时间', name: 'createTime', width: 120 ,sortable:false},
          { label: '最后调用来源', name: 'lastFrom', width: 75 ,sortable:false
            ,
            formatter:function(cellvalue, options, rowObject){
              return cellvalue == 0?'未调用':cellvalue == 1?'分发站':'补单站';
            }
          },
          { label: '最后调用时间', name: 'lastTime', width: 120 ,sortable:false},
          { label: '调用次数', name: 'callNum', width: 75 ,sortable:false},
          { label: '状态', name: 'status', width: 75 ,sortable:false
            ,
            formatter: function (cellvalue, options, rowObject) {
              return cellvalue == 0 ? '初始化' : cellvalue == 1 ? '成功' : cellvalue == 2?'失败':'补单中';
            }
          },
          { label: '处理结果', name: 'lastResp', width: 200 ,sortable:false}
        ],
        loadonce : false,
        sortable:false,
        autowidth: true,
        shrinkToFit: true,
        height: '100%',
        jsonReader: {
          root: "content"
        }
      });

    }

    $('#jqGrid').jqGrid('navGrid',"#jqGridPager", {
      search: false, // true show search button on the toolbar
      add: false,
      edit: false,
      del: false,
      refresh: true
    });
    $("#jqGrid").jqGrid('bindKeys');

  }

})();
