'use strict';

angular.module('mobilemanagerApp').controller('ClientDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Client', 'Repair',
        function($scope, $stateParams, $modalInstance, entity, Client, Repair) {

        $scope.client = entity;
        $scope.repairs = Repair.query();
        $scope.load = function(id) {
            Client.get({id : id}, function(result) {
                $scope.client = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('mobilemanagerApp:clientUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.client.id != null) {
                Client.update($scope.client, onSaveFinished);
            } else {
                Client.save($scope.client, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
