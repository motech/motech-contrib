var app = angular.module('paginator', []);

function PaginationCtrl($scope, $http, $rootScope) {

    $scope.currentPage = 1;

    $scope.loadPage = function () {
        $http.get($scope.buildURL($scope.searchCriteria)).success(function (data) {
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

    $scope.firstPage = function () {
        $scope.currentPage = 1;
        $scope.loadPage();
    }

    $scope.lastPage = function () {
        $scope.currentPage = $scope.numberOfPages();
        $scope.loadPage();
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
    function setSearchCriteria() {
        if ($scope.filterSectionId) {
            $scope.searchCriteria = $("#" + $scope.filterSectionId).serializeObject();
        }
        else
            $scope.searchCriteria = null;
    }

    $("#" + $scope.id + ' .current-page').keypress(function (e) {
        var keyCode = e.which;

        //is entered key a number or not printable character
        var isSpecialCharcterPressed = keyCode == 8 || e.ctrlKey || e.metaKey || e.altKey;
        if (keyCode < 127 && (keyCode > 47 && keyCode < 58 || isSpecialCharcterPressed || keyCode == 0)) {
            return true;
        }

        if (keyCode == 13) {
            if ($(this).val() > 0 && $scope.numberOfPages() >= $(this).val()) {
                $scope.currentPage = $(this).val();
                $scope.loadPage();
            }
            else {
                $("#" + $scope.id + ' .current-page').val('1');
                $scope.currentPage = 1;
                $scope.loadPage();
            }
            return true;
        }
        return false;
    });

    setSearchCriteria();

    $rootScope.$on('filterUpdated', function (evt, searchCriteria) {
        setSearchCriteria();
        $scope.currentPage = 1;
        $scope.loadPage();
    });

    $scope.loadPage();
}