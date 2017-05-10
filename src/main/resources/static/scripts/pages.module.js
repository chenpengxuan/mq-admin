/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
  'use strict';
    var array = [
        'ui.router',
        'BlurAdmin.pages.message',
        'BlurAdmin.pages.dispatch_detail',
        'BlurAdmin.pages.appconfig',
        'BlurAdmin.pages.callback'
    ];
  angular.module('BlurAdmin.pages',array).config(routeConfig).run(function($rootScope, $state, $stateParams) {
        $rootScope.$state = $state;
        $rootScope.$stateParams = $stateParams;
        $rootScope.$on("$stateChangeSuccess",  function(event, toState, toParams, fromState, fromParams) {
          // to be used for back button //won't work when page is reloaded.
          $rootScope.previousState_name = fromState.name;
          $rootScope.previousState_params = fromParams;
        });
        //back button function called from back button's ng-click="back()"
        $rootScope.back = function() {
          $state.go($rootScope.previousState_name,$rootScope.previousState_params);
        };
      });

  /** @ngInject */
  function routeConfig($urlRouterProvider, baSidebarServiceProvider) {
    $urlRouterProvider.otherwise('/message');

  }

})();
