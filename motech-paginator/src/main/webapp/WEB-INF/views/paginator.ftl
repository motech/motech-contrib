<#macro paginate id entity contextRoot  rowsPerPage="10" stylePath="">

    <link rel="stylesheet" type="text/css" href="<@spring.url '${stylePath}/motech-paginator-pagination.css'/>"/>

    <div id="${id}" ng-init="entity='${entity}';contextRoot='${contextRoot}';rowsPerPage='${rowsPerPage}'">
        <div class="paginator" ng-controller="PaginationCtrl">
            <div class="paginator-content">
                <#nested>
            </div>
            <button ng-disabled="currentPage == 1" ng-click="prevPage()">Previous</button>
            {{currentPage}}/{{numberOfPages()}}
            <button ng-disabled="currentPage ==  numberOfPages()" ng-click="nextPage()">Next</button>
        </div>
    </div>

</#macro>

<#macro filter paginationControl >

    <form id="${paginationControl}-search" ng-controller="FilterCtrl" ng-init="paginationControl='${paginationControl}';">
        <#nested>
        <input type="button" ng-click="applyFilter()" value="Search"/>
    </form>

</#macro>

<#macro paginationScripts jsPath="" loadJquery="true">
    <#if loadJquery == "true">
        <script type="text/javascript" src="<@spring.url '${jsPath}/motech-paginator-jquery.js'/>"></script>
    </#if>
    <script type="text/javascript" src="<@spring.url '${jsPath}/motech-paginator-angular.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '${jsPath}/motech-paginator-pagination.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '${jsPath}/motech-paginator-filter.js'/>"></script>
</#macro>
