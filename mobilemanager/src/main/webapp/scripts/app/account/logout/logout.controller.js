'use strict';

angular.module('mobilemanagerApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
