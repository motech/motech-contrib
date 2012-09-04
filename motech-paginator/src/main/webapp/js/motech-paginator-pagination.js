var app=angular.module('paginator', []);

function PaginationCtrl($scope, $http) {
    $scope.currentPage = 1;

    $scope.loadPage = function (){
        $http.get($scope.contextRoot + '/page/' + $scope.entity + '?pageNo=' + $scope.currentPage).success(function(data) {
            $scope.data = data;
            $scope.pageSize = data.rowsPerPage;
            $scope.numberOfPages=function(){
                return Math.ceil($scope.data.totalRows/$scope.pageSize);
            }
        });
    }

    $scope.loadPage();

    $scope.prevPage = function(){
        $scope.currentPage--;
        $scope.loadPage();
    }

    $scope.nextPage = function(){
        $scope.currentPage++;
        $scope.loadPage();
    }
}

//We already have a limitTo filter built-in to angular,
//let's make a startFrom filter
app.filter('startFrom', function() {
    return function(input, start) {
        start = +start; //parse to int
        return input.slice(start);
    }
});

