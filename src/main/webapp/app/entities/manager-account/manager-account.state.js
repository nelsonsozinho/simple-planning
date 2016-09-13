(function() {
    'use strict';

    angular
        .module('planningApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('manager-account', {
            parent: 'entity',
            url: '/manager-account?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'planningApp.managerAccount.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/manager-account/manager-accounts.html',
                    controller: 'ManagerAccountController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('managerAccount');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('manager-account-detail', {
            parent: 'entity',
            url: '/manager-account/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'planningApp.managerAccount.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/manager-account/manager-account-detail.html',
                    controller: 'ManagerAccountDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('managerAccount');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ManagerAccount', function($stateParams, ManagerAccount) {
                    return ManagerAccount.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'manager-account',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('manager-account-detail.edit', {
            parent: 'manager-account-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/manager-account/manager-account-dialog.html',
                    controller: 'ManagerAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ManagerAccount', function(ManagerAccount) {
                            return ManagerAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('manager-account.new', {
            parent: 'manager-account',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/manager-account/manager-account-dialog.html',
                    controller: 'ManagerAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                value: null,
                                description: null,
                                maturity: null,
                                isPaid: null,
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('manager-account', null, { reload: 'manager-account' });
                }, function() {
                    $state.go('manager-account');
                });
            }]
        })
        .state('manager-account.edit', {
            parent: 'manager-account',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/manager-account/manager-account-dialog.html',
                    controller: 'ManagerAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ManagerAccount', function(ManagerAccount) {
                            return ManagerAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('manager-account', null, { reload: 'manager-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('manager-account.delete', {
            parent: 'manager-account',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/manager-account/manager-account-delete-dialog.html',
                    controller: 'ManagerAccountDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ManagerAccount', function(ManagerAccount) {
                            return ManagerAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('manager-account', null, { reload: 'manager-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
