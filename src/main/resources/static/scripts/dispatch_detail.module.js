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

    angular.module('BlurAdmin.pages.dispatch_detail', ['bw.paging'])
        .config(routeConfig);


    /** @ngInject */
    function routeConfig($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('dispatch_detail', {
                url: '/dispatch_detail',
                title: '消息分发查询',
                templateUrl: 'dispatch-detail-list.html',
                controller: 'dispatchDetailListCtrl',
                sidebarMeta: {
                    icon: 'ion-ios-search',
                    order: 300,
                },
            });
    }

})();
