(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('SellerController', SellerController);

    SellerController.$inject = ['$scope', '$state', 'Seller'];

    function SellerController ($scope, $state, Seller) {
        var vm = this;

        vm.sellers = [];

        loadAll();

        function loadAll() {
            Seller.query(function(result) {
                vm.sellers = result;
                vm.searchQuery = null;
            });
        }
    }
})();
