(function() {
    'use strict';

    angular
        .module('planningApp')
        .controller('ManagerAccountDetailController', ManagerAccountDetailController);

    ManagerAccountDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ManagerAccount'];

    function ManagerAccountDetailController($scope, $rootScope, $stateParams, previousState, entity, ManagerAccount) {
        var vm = this;

        vm.managerAccount = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('planningApp:managerAccountUpdate', function(event, result) {
            vm.managerAccount = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
