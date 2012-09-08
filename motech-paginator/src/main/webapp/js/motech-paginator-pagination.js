var app = angular.module('paginator', []);

function PaginationCtrl($scope, $http, $rootScope) {

    $scope.currentPage = 1;

    $scope.loadPage = function () {
        var searchCriteria = null;
        if ($scope.filterSectionId) {
            searchCriteria = $scope.getCriteria();
        }

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

    $scope.getCriteria = function () {
        return $("#" + $scope.filterSectionId).serializeObject();
    }

    $.fn.serializeObject = function () {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function () {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };

    $rootScope.$on('filterUpdated', function (evt, searchCriteria) {
        $scope.currentPage = 1;
        $scope.loadPage(searchCriteria);
    });

    $scope.loadPage();
}