(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('SellerDialogController', SellerDialogController);

    SellerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Seller', 'Item'];

    function SellerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Seller, Item) {
        var vm = this;

        vm.seller = entity;
        vm.clear = clear;
        vm.save = save;
        vm.items = Item.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.seller.id !== null) {
                Seller.update(vm.seller, onSaveSuccess, onSaveError);
            } else {
                Seller.save(vm.seller, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myappApp:sellerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
