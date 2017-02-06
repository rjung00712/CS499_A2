(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('SellerDeleteController',SellerDeleteController);

    SellerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Seller'];

    function SellerDeleteController($uibModalInstance, entity, Seller) {
        var vm = this;

        vm.seller = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Seller.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
