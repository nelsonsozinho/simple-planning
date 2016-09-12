(function() {
    'use strict';

    angular
        .module('planningApp')
        .controller('BudgetController', BudgetController);

    BudgetController.$inject = ['$scope', '$state', 'Budget'];

    function BudgetController ($scope, $state, Budget) {
        var vm = this;
        
        vm.budgets = [];

        loadAll();

        function loadAll() {
            Budget.query(function(result) {
                vm.budgets = result;
            });
        }
    }
})();
