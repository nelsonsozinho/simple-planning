(function() {
    'use strict';
    angular
        .module('planningApp')
        .factory('ManagerAccount', ManagerAccount);

    ManagerAccount.$inject = ['$resource', 'DateUtils'];

    function ManagerAccount ($resource, DateUtils) {
        var resourceUrl =  'api/manager-accounts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.maturity = DateUtils.convertLocalDateFromServer(data.maturity);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.maturity = DateUtils.convertLocalDateToServer(data.maturity);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.maturity = DateUtils.convertLocalDateToServer(data.maturity);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
