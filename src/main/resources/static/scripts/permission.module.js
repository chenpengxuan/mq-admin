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

    angular.module('BlurAdmin.pages.permission', ['bw.paging'])
        .config(routeConfig);


    /** @ngInject */
    function routeConfig($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('permission', {
                url: '/permission',
                title: '权限管理',
                templateUrl: 'permission-list.html',
                controller: 'permissionListCtrl',
                sidebarMeta: {
                    icon: 'ion-settings',
                    order: 400,
                },
            });
    }

})();
