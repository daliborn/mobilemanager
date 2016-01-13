'use strict';

angular.module('mobilemanagerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('repair', {
                parent: 'entity',
                url: '/repairs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'mobilemanagerApp.repair.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/repair/repairs.html',
                        controller: 'RepairController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('repair');
                        $translatePartialLoader.addPart('brand');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('repair.detail', {
                parent: 'entity',
                url: '/repair/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'mobilemanagerApp.repair.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/repair/repair-detail.html',
                        controller: 'RepairDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('repair');
                        $translatePartialLoader.addPart('brand');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Repair', function($stateParams, Repair) {
                        return Repair.get({id : $stateParams.id});
                    }]
                }
            })
            .state('repair.new', {
                parent: 'repair',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/repair/repair-dialog.html',
                        controller: 'RepairDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    imei: null,
                                    serialno: null,
                                    brand: null,
                                    entryDate: null,
                                    closed: null,
                                    comment: null,
                                    price: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('repair', null, { reload: true });
                    }, function() {
                        $state.go('repair');
                    })
                }]
            })
            .state('repair.edit', {
                parent: 'repair',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/repair/repair-dialog.html',
                        controller: 'RepairDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Repair', function(Repair) {
                                return Repair.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('repair', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('repair.delete', {
                parent: 'repair',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/repair/repair-delete-dialog.html',
                        controller: 'RepairDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Repair', function(Repair) {
                                return Repair.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('repair', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
