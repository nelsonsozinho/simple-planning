(function() {
    'use strict';

    angular
        .module('planningApp')
        .controller('BudgetDialogController', BudgetDialogController);

    BudgetDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Budget', 'CostCenter'];

    function BudgetDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Budget, CostCenter) {
        var vm = this;

        vm.budget = entity;
        vm.clear = clear;
        vm.save = save;
        vm.costcenters = CostCenter.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.budget.id !== null) {
                Budget.update(vm.budget, onSaveSuccess, onSaveError);
            } else {
                Budget.save(vm.budget, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('planningApp:budgetUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
