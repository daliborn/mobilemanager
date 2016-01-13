'use strict';

angular.module('mobilemanagerApp')
	.controller('RepairDeleteController', function($scope, $uibModalInstance, entity, Repair) {

        $scope.repair = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Repair.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
