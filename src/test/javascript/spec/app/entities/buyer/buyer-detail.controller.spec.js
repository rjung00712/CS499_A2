'use strict';

describe('Controller Tests', function() {

    describe('Buyer Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBuyer, MockItem, MockSeller;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBuyer = jasmine.createSpy('MockBuyer');
            MockItem = jasmine.createSpy('MockItem');
            MockSeller = jasmine.createSpy('MockSeller');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Buyer': MockBuyer,
                'Item': MockItem,
                'Seller': MockSeller
            };
            createController = function() {
                $injector.get('$controller')("BuyerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'myappApp:buyerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
