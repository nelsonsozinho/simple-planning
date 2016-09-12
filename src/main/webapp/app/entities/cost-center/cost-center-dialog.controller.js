(function() {
    'use strict';

    angular
        .module('planningApp')
        .controller('CostCenterDialogController', CostCenterDialogController);

    CostCenterDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CostCenter', 'Budget'];

    function CostCenterDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CostCenter, Budget) {
        var vm = this;

        vm.costCenter = entity;
        vm.clear = clear;
        vm.save = save;
        vm.budgets = Budget.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.costCenter.id !== null) {
                CostCenter.update(vm.costCenter, onSaveSuccess, onSaveError);
            } else {
                CostCenter.save(vm.costCenter, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('planningApp:costCenterUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
