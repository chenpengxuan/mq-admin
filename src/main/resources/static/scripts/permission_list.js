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

  angular.module('BlurAdmin.pages.permission')
      .controller('permissionListCtrl', permissionListCtrl);
  
  /** @ngInject */
  function permissionListCtrl($scope, $state,$http,$timeout) {

    $scope.isAdmin = isAdmin;

    $scope.search = function(){

      //构建查询需要的参数
      var param = {
        loginName:$scope.obj.loginName
      };

      var postData = $("#jqGrid").jqGrid("getGridParam", "postData");

      // 将查询参数融入postData选项对象
      $.extend(postData, param);

      $("#jqGrid").jqGrid("setGridParam", {
        search: true    // 将jqGrid的search选项设为true
      }).trigger("reloadGrid", [{page:1}]);   // 重新载入Grid

    };


    $("#jqGrid").jqGrid({
      url: 'permission/list',
      editurl:"permission/save",
      mtype: "POST",
      datatype: "json",
      // caption:"消息列表",
      colModel: [
        { label: 'ID', name: 'id',sortable:false,key:true,hidden:true},
        { label: '登录名', name: 'loginName',sortable:false,editable:true,editrules : { required: true}},
        {
          label: '权限', name: 'action', sortable:false,editable:true,edittype:'select',editrules : { required: true},
          formatter:function(cellvalue, options, rowObject){
            return cellvalue == '9'?"管理员":"普通用户";
          },
          editoptions:{
            value:"9:管理员;1:普通用户"
          }
        },
        { label: '是否启用', name: 'enable', sortable:false,editable:true,edittype:'select',editrules : { required: true},
          editoptions:{
            value:"1:Enable;0:Disabled"
          },
          formatter:function(cellvalue, options, rowObject){
            return cellvalue == '1'?"Enable":"Disabled";
          }
        },
        { label: '创建人', name: 'creator', sortable:false},
        { label: '创建时间', name: 'createTime', sortable:false},
        { label: '更新人', name: 'updator', sortable:false},
        { label: '更新时间', name: 'updateTime', sortable:false}
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
        rows:"size",
        id:"permissionId"
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
            $(e).find("[name='loginName']").attr("readonly", true);
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
          },
          beforeSubmit: function(postData, formid){// id=value1,value2,...
            var editData = {
              "permissionId": null
            };
            postData = $.extend(postData, editData);
            return[true,"success"];
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
