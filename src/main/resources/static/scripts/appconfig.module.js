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

    angular.module('BlurAdmin.pages.appconfig', ['bw.paging'])
        .config(routeConfig);


    /** @ngInject */
    function routeConfig($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('appconfig', {
                url: '/appconfig',
                templateUrl: 'appconfig-list.html',
                controller: 'appConfiglistCtrl',
                title: '应用管理',
                sidebarMeta: {
                    icon: 'ion-android-settings',
                    order: 300,
                },
            })
        ;

        // $urlRouterProvider.when('/monitor','/monitor/monitor-list');
    }

})();
