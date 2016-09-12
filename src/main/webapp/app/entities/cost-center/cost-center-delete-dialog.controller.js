(function() {
    'use strict';

    angular
        .module('planningApp')
        .controller('CostCenterDeleteController',CostCenterDeleteController);

    CostCenterDeleteController.$inject = ['$uibModalInstance', 'entity', 'CostCenter'];

    function CostCenterDeleteController($uibModalInstance, entity, CostCenter) {
        var vm = this;

        vm.costCenter = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CostCenter.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
