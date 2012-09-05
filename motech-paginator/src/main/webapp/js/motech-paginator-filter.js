var app = angular.module('paginator', []);

function FilterCtrl($scope, $http, $rootScope) {
    $scope.currentPage = 1;

    $scope.applyFilter = function () {
        $rootScope.$broadcast("filterUpdated", $scope.getCriteria());
    }

    $scope.getCriteria = function () {
        return $("#" + $scope.paginationControl + "-search").serializeObject();
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
}