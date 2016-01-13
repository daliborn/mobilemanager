'use strict';

angular.module('mobilemanagerApp')
    .controller('RepairController', function ($scope, $state, Repair, RepairSearch, ParseLinks) {

        $scope.repairs = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            Repair.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.repairs.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.repairs = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            RepairSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.repairs = result;
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
            $scope.repair = {
                imei: null,
                serialno: null,
                brand: null,
                entryDate: null,
                closed: null,
                comment: null,
                price: null,
                id: null
            };
        };
    });
