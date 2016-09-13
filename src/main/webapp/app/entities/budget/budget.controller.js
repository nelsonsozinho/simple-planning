(function() {
    'use strict';

    angular
        .module('planningApp')
        .controller('BudgetController', BudgetController);

    BudgetController.$inject = ['$scope', '$state', 'Budget'];

    function BudgetController ($scope, $state, Budget) {
        var vm = this;

        vm.budgets = [];
        vm.elements = [
            {
                "id": 1,
                "name": {
                    "first": "John",
                    "last": "Schmidt"
                },
                "address": "45024 France",
                "price": 760.41,
                "isActive": "Yes",
                "product": {
                    "description": "Fried Potatoes",
                    "options": [
                        {
                            "description": "Fried Potatoes",
                            "image": "//a248.e.akamai.net/assets.github.com/images/icons/emoji/fries.png"
                        },
                        {
                            "description": "Fried Onions",
                            "image": "//a248.e.akamai.net/assets.github.com/images/icons/emoji/fries.png"
                        }
                    ]
                }
            },
            {
                "id": 2,
                "name": {
                    "first": "Mark",
                    "last": "Johnson"
                },
                "address": "45024 Tress Street",
                "price": 760.21,
                "isActive": "Yes",
                "product": {
                    "description": "Near of BoA",
                    "options": [
                        {
                            "description": "Fried Potatoes",
                            "image": "//a248.e.akamai.net/assets.github.com/images/icons/emoji/fries.png"
                        },
                        {
                            "description": "Fried Onions",
                            "image": "//a248.e.akamai.net/assets.github.com/images/icons/emoji/fries.png"
                        }
                    ]
                }
            },
            {
                "id": 3,
                "name": {
                    "first": "Frog",
                    "last": "Johnson"
                },
                "address": "45024 Tress Street",
                "price": 760.21,
                "isActive": "Yes",
                "product": {
                    "description": "Near of BoA",
                    "options": [
                        {
                            "description": "Fried Potatoes",
                            "image": "//a248.e.akamai.net/assets.github.com/images/icons/emoji/fries.png"
                        },
                        {
                            "description": "Fried Onions",
                            "image": "//a248.e.akamai.net/assets.github.com/images/icons/emoji/fries.png"
                        }
                    ]
                }
            }
        ];

        loadAll();

        function loadAll() {
            Budget.query(function(result) {
                vm.budgets = result;
            });
        }


    }
})();
