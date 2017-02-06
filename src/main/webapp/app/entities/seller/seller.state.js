(function() {
    'use strict';

    angular
        .module('myappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('seller', {
            parent: 'entity',
            url: '/seller',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'myappApp.seller.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/seller/sellers.html',
                    controller: 'SellerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('seller');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('seller-detail', {
            parent: 'entity',
            url: '/seller/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'myappApp.seller.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/seller/seller-detail.html',
                    controller: 'SellerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('seller');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Seller', function($stateParams, Seller) {
                    return Seller.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'seller',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('seller-detail.edit', {
            parent: 'seller-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/seller/seller-dialog.html',
                    controller: 'SellerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Seller', function(Seller) {
                            return Seller.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('seller.new', {
            parent: 'seller',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/seller/seller-dialog.html',
                    controller: 'SellerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                account: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('seller', null, { reload: 'seller' });
                }, function() {
                    $state.go('seller');
                });
            }]
        })
        .state('seller.edit', {
            parent: 'seller',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/seller/seller-dialog.html',
                    controller: 'SellerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Seller', function(Seller) {
                            return Seller.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('seller', null, { reload: 'seller' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('seller.delete', {
            parent: 'seller',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/seller/seller-delete-dialog.html',
                    controller: 'SellerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Seller', function(Seller) {
                            return Seller.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('seller', null, { reload: 'seller' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
