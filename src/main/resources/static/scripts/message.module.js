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

    angular.module('BlurAdmin.pages.message', ['bw.paging'])
        .config(routeConfig);


    /** @ngInject */
    function routeConfig($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('message', {
                url: '/message',
                templateUrl: 'message-list.html',
                controller: 'listCtrl',
                title: '消息查询',
                sidebarMeta: {
                    icon: 'ion-ios-bell',
                    order: 300,
                },
            })
        ;

        // $urlRouterProvider.when('/monitor','/monitor/monitor-list');
    }

})();
