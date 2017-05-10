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

  angular.module('BlurAdmin.pages.appconfig')
      .controller('appConfiglistCtrl', appConfiglistCtrl);

  /** @ngInject */
  function appConfiglistCtrl($scope, $state,$http,$filter,$timeout) {

    $scope.isAdmin = isAdmin;
    $scope.obj = {};

    //获取数据源
    $http({
      url: "config/getAllApp",
      method: 'GET'
    }).success(function (data) {
      if (data.success) {
        $scope.appIdList = data.content;
        $timeout(function(){
          $('#appId').selectpicker({
            liveSearch: true
          });
        }, 100);
      }
    });

    $scope.search = function(){

      //构建查询需要的参数
      var param = {
        appId:$scope.obj.appId,
        groupId:$scope.obj.groupId
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

    $("#jqGrid").jqGrid({
      url: 'appConfig/list',
      editurl:"appConfig/saveApp",
      mtype: "POST",
      datatype: "json",
      // caption:"消息列表",
      colModel: [
        { label: '应用编号', name: 'appId',key:true,width: 150,sortable:false,editable:true,editrules : { required: true}},
        { label: '应用描述', name: 'description', width: 200,sortable:false,editable:true,edittype:"textarea"},
        {
          label: '消息中间件类型', name: 'mqType', width: 150,sortable:false,editable:true,edittype:'select',editrules : { required: true},
          formatter:function(cellvalue, options, rowObject){
            return cellvalue == '1'?'Kafka':'RabbitMq';
          },
          editoptions:{
            value:"0:RabbitMq;1:Kafka"
          }
        },
        { label: '分发组', name: 'dispatchGroup', width: 100 ,sortable:false,editable:true},
        {
          label: '操作', name: 'appId', width: 75 ,sortable:false,hidden:!isAdmin,
          formatter:function(cellvalue, options, rowObject){
            return "<a href='javascript:void(0)' onclick='addQueue(\""+cellvalue+"\")'>添加业务</a>";
          }
        }
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
        rows:"size",
        id:"appId" //设置id为appid 删除数据时使用
      },
      jsonReader: {
        root: "content.content",
        total: "content.totalPages",      //总页数
        records: "content.totalElements" //总记录数
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
        postData.page--;  //由于后台分页从0开始
      },
      subGrid: true, // set the subGrid property to true to show expand buttons for each row
      subGridRowExpanded: showChildGrid ,// javascript function that will take care of showing the child grid
    });


    function showChildGrid(parentRowID, parentRowKey) {
      var childGridID = parentRowID + "_table";

      // add a table and pager HTML elements to the parent grid row - we will render the child grid here
      $('#' + parentRowID).append('<table id=' + childGridID + '></table>');

      $("#" + childGridID).jqGrid({
        url: "appConfig/detail/" + parentRowKey,
        mtype: "GET",
        datatype: "json",
        page: 0,
        rowNum: 100,
        colModel: [
          { label: '业务代码', name: 'code',key:true, width: 100 ,sortable:false},
          { label: '业务描述', name: 'description', width: 100 ,sortable:false},
          { label: '是否启用', name: 'enable', width: 75 ,sortable:false},
          { label: '是否开户日志', name: 'enableLog', width: 75 ,sortable:false},
          { label: '检测进入补单时间的间隔', name: 'checkCompensateDelay', width: 75 ,sortable:false},
          { label: '检测进入补单的时间跨度', name: 'checkCompensateTimeSpan', width: 75 ,sortable:false},
          {
            label: '操作', name: 'code', width: 75 ,sortable:false,hidden:!isAdmin,
            formatter:function(cellvalue, options, rowObject){
              return "<a href='javascript:void(0)' onclick='editQueue(\""+parentRowKey+"\",\""+cellvalue+"\")'>编辑</a>";
            }
          }

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

    
    $scope.saveQueue = function () {

      if($scope.queue.code == ''){
        popupAlert("业务代码不能为空!");
        return;
      }

      $http({
        url: "appConfig/queue/save/" + $scope.queue.appId,
        method: 'POST',
        params:$scope.queue
      }).success(function (data) {
        if (data.success) {
          layer.alert(data.message,{closeBtn: 0},function (index) {
            layer.close(index);
            layer.close($scope.index);
            $("#jqGrid_" + $scope.queue.appId +"_table").jqGrid("setGridParam", {}).trigger("reloadGrid");   // 重新载入Grid
          });

        }else{
          popupAlert(data.message);
        }
      });
    };

    $scope.queue = {};

    window.addQueue = function (appId) {
      $scope.queue.poolSize =  50;
      $scope.queue.checkCompensateDelay =  10;
      $scope.queue.checkCompensateTimeSpan =  48;
      $scope.queue.enable = 'true';
      $scope.queue.enableLog = 'true';
      $scope.queue.description = '';

      $scope.queue.appId = appId;
      $scope.queue.code = '';
      $scope.queue.codeReadonly = false;
      $scope.$apply();//需要手动刷新
      $scope.index = layer.open({
        type: 1,
        title: "添加业务",
        closeBtn: 1,
        area: '516px',
        skin: 'layui-layer-rim',
        shadeClose: true,
        content: $('#queueContent'),
        end:function () {}
      });
    };
    
    window.editQueue = function (appId,queueCode) {

      $scope.queue.appId = appId;
      $scope.queue.code = queueCode;
      $scope.queue.codeReadonly = true;
      $http({
        url: "appConfig/queue/" + appId + "/" + queueCode,
        method: 'GET'
      }).success(function (data) {
        if (data.success) {
          $scope.queue.enable = data.content.enable+'';
          $scope.queue.enableLog = data.content.enableLog+'';
          $scope.queue.poolSize = data.content.poolSize;
          $scope.queue.description = data.content.description;
          $scope.queue.checkCompensateDelay = data.content.checkCompensateDelay;
          $scope.queue.checkCompensateTimeSpan = data.content.checkCompensateTimeSpan;

          // $scope.$apply();//需要手动刷新

          $scope.index = layer.open({
            type: 1,
            title: "编辑业务",
            closeBtn: 1,
            area: '516px',
            skin: 'layui-layer-rim',
            shadeClose: true,
            content: $('#queueContent'),
            end:function () {}
          });
        }else{
          popupAlert(data.message);
        }
      });

    };

    $('#jqGrid').jqGrid('navGrid', "#jqGridPager", {
          search: false, // true show search button on the toolbar
          add: isAdmin,
          edit: isAdmin,
          del: isAdmin,
          refresh: true,
          cloneToTop:true
        }, {
          editCaption: "修改应用配置",
          recreateForm: true,
          checkOnUpdate: true,
          checkOnSubmit: true,
          closeAfterEdit: true,
          beforeShowForm: function (e) {
            $(e).find("[name='appId']").attr("readonly", true);
          },
          errorTextFormat: function (data) {
            return 'Error: ' + data.responseText
          }
        },
        // options for the Add Dialog
        {
          closeAfterAdd: true,
          recreateForm: true,
          errorTextFormat: function (data) {
            return 'Error: ' + data.responseText
          }
        },
        // options for the Delete Dailog
        {
          errorTextFormat: function (data) {
            return 'Error: ' + data.responseText
          }
        }
    );
    $("#jqGrid").jqGrid('bindKeys');

  }

})();