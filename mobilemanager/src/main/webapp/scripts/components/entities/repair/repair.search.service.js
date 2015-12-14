'use strict';

angular.module('mobilemanagerApp')
    .factory('RepairSearch', function ($resource) {
        return $resource('api/_search/repairs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
