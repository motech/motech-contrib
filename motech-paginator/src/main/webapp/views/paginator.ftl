<#macro paginate entity contextRoot stylePath="" jsPath="">

<link rel="stylesheet" type="text/css" href="<@spring.url '${stylePath}/motech-paginator-pagination.css'/>"/>

<div ng-init="entity='${entity}';contextRoot='${contextRoot}'">
    <div class="paginator"  ng-controller="PaginationCtrl" >
        <div class="paginator-content">
            <#nested>
        </div>
        <button ng-disabled="currentPage == 0" ng-click="prevPage()">Previous</button>
        {{currentPage+1}}/{{numberOfPages()}}
        <button ng-disabled="currentPage >= data.results.length/pageSize - 1" ng-click="nextPage()">Next</button>
    </div>
</div>

<script type="text/javascript" src="<@spring.url '${jsPath}/paginator/motech-paginator-angular.js'/>"></script>
<script type="text/javascript" src="<@spring.url '${jsPath}/paginator/motech-paginator-pagination.js'/>"></script>
</#macro>