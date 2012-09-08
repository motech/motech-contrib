var app = angular.module('paginator', []);

function FilterCtrl($scope, $http, $rootScope) {

    $scope.applyFilter = function () {
        $rootScope.$broadcast("filterUpdated");
    }

}