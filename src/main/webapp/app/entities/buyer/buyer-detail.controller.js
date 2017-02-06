(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('BuyerDetailController', BuyerDetailController);

    BuyerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Buyer', 'Item', 'Seller'];

    function BuyerDetailController($scope, $rootScope, $stateParams, previousState, entity, Buyer, Item, Seller) {
        var vm = this;

        vm.buyer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myappApp:buyerUpdate', function(event, result) {
            vm.buyer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
