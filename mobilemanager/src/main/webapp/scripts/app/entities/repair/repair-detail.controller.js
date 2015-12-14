'use strict';

angular.module('mobilemanagerApp')
    .controller('RepairDetailController', function ($scope, $rootScope, $stateParams, entity, Repair, Client) {
        $scope.repair = entity;
        $scope.load = function (id) {
            Repair.get({id: id}, function(result) {
                $scope.repair = result;
            });
        };
        $rootScope.$on('mobilemanagerApp:repairUpdate', function(event, result) {
            $scope.repair = result;
        });
    });
