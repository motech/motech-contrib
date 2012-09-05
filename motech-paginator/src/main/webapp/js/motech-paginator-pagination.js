var app = angular.module('paginator', []);

function PaginationCtrl($scope, $http, $rootScope) {

    $scope.currentPage = 1;

    $scope.loadPage = function (searchCriteria) {
        $http.get($scope.buildURL(searchCriteria)).success(function (data) {
            $scope.data = data;
            $scope.numberOfPages = function () {
                return Math.ceil($scope.data.totalRows / $scope.rowsPerPage);
            }
        });
    }

    $scope.prevPage = function () {
        $scope.currentPage--;
        $scope.loadPage();
    }

    $scope.buildURL = function (searchCriteria) {
        var url = $scope.contextRoot + '/page/' + $scope.entity +
            '?pageNo=' + $scope.currentPage +
            '&rowsPerPage=' + $scope.rowsPerPage;
        if (searchCriteria) {
            url += '&searchCriteria=' + JSON.stringify(searchCriteria);
        }
        return url;
    }

    $scope.nextPage = function () {
        $scope.currentPage++;
        $scope.loadPage();
    }

    $rootScope.$on('filterUpdated', function (evt, searchCriteria) {
        $scope.loadPage(searchCriteria);
    });

    $scope.loadPage();
}