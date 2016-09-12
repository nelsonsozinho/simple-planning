(function() {
    'use strict';

    angular
        .module('planningApp')
        .controller('CostCenterController', CostCenterController);

    CostCenterController.$inject = ['$scope', '$state', 'CostCenter'];

    function CostCenterController ($scope, $state, CostCenter) {
        var vm = this;
        
        vm.costCenters = [];

        loadAll();

        function loadAll() {
            CostCenter.query(function(result) {
                vm.costCenters = result;
            });
        }
    }
})();
