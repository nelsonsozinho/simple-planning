(function() {
    'use strict';

    angular
        .module('planningApp')
        .controller('BudgetDetailController', BudgetDetailController);

    BudgetDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Budget', 'CostCenter'];

    function BudgetDetailController($scope, $rootScope, $stateParams, previousState, entity, Budget, CostCenter) {
        var vm = this;

        vm.budget = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('planningApp:budgetUpdate', function(event, result) {
            vm.budget = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
