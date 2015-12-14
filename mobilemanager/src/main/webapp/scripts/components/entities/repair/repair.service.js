'use strict';

angular.module('mobilemanagerApp')
    .factory('Repair', function ($resource, DateUtils) {
        return $resource('api/repairs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.entryDate = DateUtils.convertDateTimeFromServer(data.entryDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
