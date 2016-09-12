(function() {
    'use strict';
    angular
        .module('planningApp')
        .factory('CostCenter', CostCenter);

    CostCenter.$inject = ['$resource'];

    function CostCenter ($resource) {
        var resourceUrl =  'api/cost-centers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
