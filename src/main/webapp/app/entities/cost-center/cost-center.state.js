(function() {
    'use strict';

    angular
        .module('planningApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cost-center', {
            parent: 'entity',
            url: '/cost-center',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'planningApp.costCenter.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cost-center/cost-centers.html',
                    controller: 'CostCenterController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('costCenter');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cost-center-detail', {
            parent: 'entity',
            url: '/cost-center/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'planningApp.costCenter.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cost-center/cost-center-detail.html',
                    controller: 'CostCenterDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('costCenter');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CostCenter', function($stateParams, CostCenter) {
                    return CostCenter.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cost-center',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cost-center-detail.edit', {
            parent: 'cost-center-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cost-center/cost-center-dialog.html',
                    controller: 'CostCenterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CostCenter', function(CostCenter) {
                            return CostCenter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cost-center.new', {
            parent: 'cost-center',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cost-center/cost-center-dialog.html',
                    controller: 'CostCenterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                isGenerateRevenue: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cost-center', null, { reload: 'cost-center' });
                }, function() {
                    $state.go('cost-center');
                });
            }]
        })
        .state('cost-center.edit', {
            parent: 'cost-center',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cost-center/cost-center-dialog.html',
                    controller: 'CostCenterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CostCenter', function(CostCenter) {
                            return CostCenter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cost-center', null, { reload: 'cost-center' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cost-center.delete', {
            parent: 'cost-center',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cost-center/cost-center-delete-dialog.html',
                    controller: 'CostCenterDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CostCenter', function(CostCenter) {
                            return CostCenter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cost-center', null, { reload: 'cost-center' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
