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

    angular.module('BlurAdmin.pages.logger', ['bw.paging'])
        .config(routeConfig);


    /** @ngInject */
    function routeConfig($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('logger', {
                url: '/logger',
                title: '执行日志',
                templateUrl: 'logger-list.html',
                controller: 'loggerListCtrl',
                sidebarMeta: {
                    icon: 'ion-grid',
                    order: 400,
                },
            });
    }

})();
