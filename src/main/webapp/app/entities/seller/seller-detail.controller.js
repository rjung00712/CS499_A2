(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('SellerDetailController', SellerDetailController);

    SellerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Seller', 'Item'];

    function SellerDetailController($scope, $rootScope, $stateParams, previousState, entity, Seller, Item) {
        var vm = this;

        vm.seller = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myappApp:sellerUpdate', function(event, result) {
            vm.seller = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
