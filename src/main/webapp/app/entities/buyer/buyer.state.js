(function() {
    'use strict';

    angular
        .module('myappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('buyer', {
            parent: 'entity',
            url: '/buyer',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'myappApp.buyer.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/buyer/buyers.html',
                    controller: 'BuyerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('buyer');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('buyer-detail', {
            parent: 'entity',
            url: '/buyer/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'myappApp.buyer.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/buyer/buyer-detail.html',
                    controller: 'BuyerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('buyer');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Buyer', function($stateParams, Buyer) {
                    return Buyer.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'buyer',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('buyer-detail.edit', {
            parent: 'buyer-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/buyer/buyer-dialog.html',
                    controller: 'BuyerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Buyer', function(Buyer) {
                            return Buyer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('buyer.new', {
            parent: 'buyer',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/buyer/buyer-dialog.html',
                    controller: 'BuyerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                address: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('buyer', null, { reload: 'buyer' });
                }, function() {
                    $state.go('buyer');
                });
            }]
        })
        .state('buyer.edit', {
            parent: 'buyer',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/buyer/buyer-dialog.html',
                    controller: 'BuyerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Buyer', function(Buyer) {
                            return Buyer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('buyer', null, { reload: 'buyer' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('buyer.delete', {
            parent: 'buyer',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/buyer/buyer-delete-dialog.html',
                    controller: 'BuyerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Buyer', function(Buyer) {
                            return Buyer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('buyer', null, { reload: 'buyer' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
