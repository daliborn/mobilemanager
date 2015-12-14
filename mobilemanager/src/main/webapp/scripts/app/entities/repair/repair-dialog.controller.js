'use strict';

angular.module('mobilemanagerApp').controller('RepairDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Repair', 'Client',
        function($scope, $stateParams, $modalInstance, entity, Repair, Client) {

        $scope.repair = entity;
        $scope.clients = Client.query();
        $scope.load = function(id) {
            Repair.get({id : id}, function(result) {
                $scope.repair = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('mobilemanagerApp:repairUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.repair.id != null) {
                Repair.update($scope.repair, onSaveFinished);
            } else {
                Repair.save($scope.repair, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
