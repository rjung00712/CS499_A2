(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('BuyerDeleteController',BuyerDeleteController);

    BuyerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Buyer'];

    function BuyerDeleteController($uibModalInstance, entity, Buyer) {
        var vm = this;

        vm.buyer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Buyer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
