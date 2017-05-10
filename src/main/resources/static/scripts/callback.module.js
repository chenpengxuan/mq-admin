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

    angular.module('BlurAdmin.pages.callback', ['bw.paging'])
        .config(routeConfig);


    /** @ngInject */
    function routeConfig($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('callback', {
                url: '/callback',
                title: '订阅管理',
                templateUrl: 'callback-list.html',
                controller: 'callbackListCtrl',
                sidebarMeta: {
                    icon: 'ion-ios-settings',
                    order: 400,
                },
            });
    }

})();
