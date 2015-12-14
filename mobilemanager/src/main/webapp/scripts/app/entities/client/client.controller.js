'use strict';

angular.module('mobilemanagerApp')
    .controller('ClientController', function ($scope, Client, ClientSearch, ParseLinks) {
        $scope.clients = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Client.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.clients.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.clients = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Client.get({id: id}, function(result) {
                $scope.client = result;
                $('#deleteClientConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Client.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteClientConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            ClientSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.clients = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.client = {name: null, contactPhone: null, id: null};
        };
    });
