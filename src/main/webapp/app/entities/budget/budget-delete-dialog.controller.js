(function() {
    'use strict';

    angular
        .module('planningApp')
        .controller('BudgetDeleteController',BudgetDeleteController);

    BudgetDeleteController.$inject = ['$uibModalInstance', 'entity', 'Budget'];

    function BudgetDeleteController($uibModalInstance, entity, Budget) {
        var vm = this;

        vm.budget = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Budget.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
