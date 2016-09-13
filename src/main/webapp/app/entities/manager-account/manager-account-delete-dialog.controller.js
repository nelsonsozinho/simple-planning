(function() {
    'use strict';

    angular
        .module('planningApp')
        .controller('ManagerAccountDeleteController',ManagerAccountDeleteController);

    ManagerAccountDeleteController.$inject = ['$uibModalInstance', 'entity', 'ManagerAccount'];

    function ManagerAccountDeleteController($uibModalInstance, entity, ManagerAccount) {
        var vm = this;

        vm.managerAccount = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ManagerAccount.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
