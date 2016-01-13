'use strict';

describe('Controller Tests', function() {

    describe('Client Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockClient, MockRepair;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockClient = jasmine.createSpy('MockClient');
            MockRepair = jasmine.createSpy('MockRepair');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Client': MockClient,
                'Repair': MockRepair
            };
            createController = function() {
                $injector.get('$controller')("ClientDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mobilemanagerApp:clientUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
