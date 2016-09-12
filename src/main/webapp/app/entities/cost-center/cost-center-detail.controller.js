(function() {
    'use strict';

    angular
        .module('planningApp')
        .controller('CostCenterDetailController', CostCenterDetailController);

    CostCenterDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CostCenter', 'Budget'];

    function CostCenterDetailController($scope, $rootScope, $stateParams, previousState, entity, CostCenter, Budget) {
        var vm = this;

        vm.costCenter = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('planningApp:costCenterUpdate', function(event, result) {
            vm.costCenter = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
