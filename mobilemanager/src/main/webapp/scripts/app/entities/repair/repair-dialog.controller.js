'use strict';

angular.module('mobilemanagerApp').controller('RepairDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Repair', 'Client',
        function($scope, $stateParams, $uibModalInstance, entity, Repair, Client) {

        $scope.repair = entity;
        $scope.clients = Client.query();
        $scope.load = function(id) {
            Repair.get({id : id}, function(result) {
                $scope.repair = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mobilemanagerApp:repairUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.repair.id != null) {
                Repair.update($scope.repair, onSaveSuccess, onSaveError);
            } else {
                Repair.save($scope.repair, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForEntryDate = {};

        $scope.datePickerForEntryDate.status = {
            opened: false
        };

        $scope.datePickerForEntryDateOpen = function($event) {
            $scope.datePickerForEntryDate.status.opened = true;
        };
}]);
