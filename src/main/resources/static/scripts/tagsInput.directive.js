/*
 *
 *  (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

/**
 * @author v.lugovksy
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.pages.message')
      .directive('tagInput', tagInput);

  /** @ngInject */
  function tagInput() {
    return {
      restrict: 'A',
      link: function( $scope, elem, attr) {
        $(elem).tagsinput({
          tagClass:  'label label-' + attr.tagInput
        });
      }
    };
  }
})();