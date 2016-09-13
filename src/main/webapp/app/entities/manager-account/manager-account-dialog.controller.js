(function() {
    'use strict';

    angular
        .module('planningApp')
        .controller('ManagerAccountDialogController', ManagerAccountDialogController);

    ManagerAccountDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ManagerAccount'];

    function ManagerAccountDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ManagerAccount) {
        var vm = this;

        vm.managerAccount = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.managerAccount.id !== null) {
                ManagerAccount.update(vm.managerAccount, onSaveSuccess, onSaveError);
            } else {
                ManagerAccount.save(vm.managerAccount, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('planningApp:managerAccountUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.maturity = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
