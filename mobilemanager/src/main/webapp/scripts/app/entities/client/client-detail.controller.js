'use strict';

angular.module('mobilemanagerApp')
    .controller('ClientDetailController', function ($scope, $rootScope, $stateParams, entity, Client, Repair) {
        $scope.client = entity;
        $scope.load = function (id) {
            Client.get({id: id}, function(result) {
                $scope.client = result;
            });
        };
        var unsubscribe = $rootScope.$on('mobilemanagerApp:clientUpdate', function(event, result) {
            $scope.client = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
