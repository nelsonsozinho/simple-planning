(function() {
    'use strict';

    angular
        .module('planningApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('budget', {
            parent: 'entity',
            url: '/budget',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'planningApp.budget.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/budget/budgets.html',
                    controller: 'BudgetController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('budget');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('budget-detail', {
            parent: 'entity',
            url: '/budget/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'planningApp.budget.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/budget/budget-detail.html',
                    controller: 'BudgetDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('budget');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Budget', function($stateParams, Budget) {
                    return Budget.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'budget',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('budget-detail.edit', {
            parent: 'budget-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/budget/budget-dialog.html',
                    controller: 'BudgetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Budget', function(Budget) {
                            return Budget.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('budget.new', {
            parent: 'budget',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/budget/budget-dialog.html',
                    controller: 'BudgetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                year: null,
                                version: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('budget', null, { reload: 'budget' });
                }, function() {
                    $state.go('budget');
                });
            }]
        })
        .state('budget.edit', {
            parent: 'budget',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/budget/budget-dialog.html',
                    controller: 'BudgetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Budget', function(Budget) {
                            return Budget.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('budget', null, { reload: 'budget' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('budget.delete', {
            parent: 'budget',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/budget/budget-delete-dialog.html',
                    controller: 'BudgetDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Budget', function(Budget) {
                            return Budget.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('budget', null, { reload: 'budget' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
