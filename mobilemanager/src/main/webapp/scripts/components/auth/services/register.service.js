'use strict';

angular.module('mobilemanagerApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


