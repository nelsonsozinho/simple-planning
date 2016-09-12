(function() {
    'use strict';

    angular
        .module('planningApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('planning', {
            parent: 'app',
            url: '/planning',

            data: {
                authorities: []
            },

            views: {
                'planning@' : {
                    templateUrl: 'app/planning/planning.html',
                    controller: 'PlanningController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('planning');
                    return $translate.refresh();
                }]
            }

        });
    }
})();
