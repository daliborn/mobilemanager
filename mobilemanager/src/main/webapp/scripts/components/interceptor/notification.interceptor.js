 'use strict';

angular.module('mobilemanagerApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-mobilemanagerApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-mobilemanagerApp-params')});
                }
                return response;
            },
        };
    });