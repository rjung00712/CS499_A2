(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('BuyerController', BuyerController);

    BuyerController.$inject = ['$scope', '$state', 'Buyer'];

    function BuyerController ($scope, $state, Buyer) {
        var vm = this;

        vm.buyers = [];

        loadAll();

        function loadAll() {
            Buyer.query(function(result) {
                vm.buyers = result;
                vm.searchQuery = null;
            });
        }
    }
})();
