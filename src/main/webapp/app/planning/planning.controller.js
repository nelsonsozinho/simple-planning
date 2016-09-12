(function() {
    'use strict';

    angular
        .module('planningApp')
        .controller('PlanningController', PlanningController);

    PlanningController.$inject = ['$state', 'Principal', 'LoginService'];

    function PlanningController ($state, Principal, LoginService) {
        var vm = this;

        getCosts();

        function getCosts() {
            console.log("Open accounts");
        }

    }
})();
