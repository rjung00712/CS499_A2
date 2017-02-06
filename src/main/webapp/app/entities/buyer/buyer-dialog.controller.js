(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('BuyerDialogController', BuyerDialogController);

    BuyerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Buyer', 'Item', 'Seller'];

    function BuyerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Buyer, Item, Seller) {
        var vm = this;

        vm.buyer = entity;
        vm.clear = clear;
        vm.save = save;
        vm.buys = Item.query({filter: 'buyer-is-null'});
        $q.all([vm.buyer.$promise, vm.buys.$promise]).then(function() {
            if (!vm.buyer.buys || !vm.buyer.buys.id) {
                return $q.reject();
            }
            return Item.get({id : vm.buyer.buys.id}).$promise;
        }).then(function(buys) {
            vm.buys.push(buys);
        });
        vm.sellers = Seller.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.buyer.id !== null) {
                Buyer.update(vm.buyer, onSaveSuccess, onSaveError);
            } else {
                Buyer.save(vm.buyer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myappApp:buyerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
