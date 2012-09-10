<#macro paginate id entity contextRoot filterSectionId rowsPerPage="10" stylePath="" >

<link rel="stylesheet" type="text/css" href="<@spring.url '${stylePath}/motech-paginator-pagination.css'/>"/>

<div id="${id}"
     ng-init="entity='${entity}';contextRoot='${contextRoot}';rowsPerPage='${rowsPerPage}';id='${id}';filterSectionId='${filterSectionId}';">
    <div class="paginator" ng-controller="PaginationCtrl">
        <div class="pull-right">
            <@filterButtons/>
        </div>
        <div class="paginator-content">
            <#nested>
        </div>
        <div class="pull-right">
            <@filterButtons/>
        </div>
    </div>
</div>

</#macro>
<#macro filterButtons >
<a ng-show="currentPage > 1" class="page-link" ng-click="firstPage()">First</a>
<a ng-show="currentPage > 1" class="page-link" ng-click="prevPage()">Previous</a>
<div class="current-page-info">{{currentPage}} out of {{numberOfPages()}} pages</div>
    <a ng-show="currentPage <  numberOfPages()" class="page-link"
       ng-click="nextPage()">Next</a>
    <a ng-show="currentPage <  numberOfPages()" class="page-link"
       ng-click="lastPage()">Last</a>

    <div class="goto-page">
        Go to
        <input type="text" class="current-page" value="{{currentPage}}"/> page
    </div>
</#macro>

<#macro filter id >

    <form id="${id}" ng-submit="applyFilter()" ng-controller="FilterCtrl">
        <#nested>
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
