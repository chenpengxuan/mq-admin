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

  angular.module('BlurAdmin.pages.logger')
      .controller('loggerListCtrl', loggerListCtrl);
  
  /** @ngInject */
  function loggerListCtrl($scope, $state,$http) {
	  
    $scope.page = 1;
    $scope.pageSize = 20;
    $scope.total = 0;
    $scope.logger = {};

    $scope.pagingAction = function(page,pageSize) {
      var param = {};
      param.page = page;
      param.size = pageSize;

      param.monitorName = $scope.logger.monitorName;
      param.execUser = $scope.logger.execUser;
      if(typeof($("#startTime")[0]) != "undefined"){
    	  param.startTime = $("#startTime")[0].value;
      }
      if(typeof($("#endTime")[0]) != "undefined"){
    	  param.endTime = $("#endTime")[0].value;
      }
      param.sort = "e.exec_time,desc";

      doPaging($http,"/execLog/list",param,function (data) {
          $scope.total = data.content.totalElements;
          $scope.loggerList = data.content.content;
      });
    };
    $scope.pagingAction($scope.page,$scope.pageSize,$scope.total);

    $scope.search = function(){
    	$scope.page = 1;
        $scope.pagingAction($scope.page,$scope.pageSize);
    };

  }

})();
