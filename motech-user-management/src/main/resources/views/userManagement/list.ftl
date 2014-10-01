<#import "/spring.ftl" as spring />
<#include "resetPassword.ftl">
<#include "activateUser.ftl">
<#include "addUser.ftl">
<#import "paginator.ftl" as paginator>

<html ng-app="usermanagement">
<head>
    <title> User Management </title>
    <link rel="stylesheet" type="text/css" href="<@spring.url '/motech-user-management/css/bootstrap.min.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<@spring.url '/motech-user-management/css/jquery-ui-1.9.1.custom.min.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<@spring.url '/motech-user-management/css/motech-paginator-pagination.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<@spring.url '/motech-user-management/css/userManagement.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<@spring.url '/motech-crud/css/ng-cloak.css'/>"/>
     
    <script type="text/javascript"  src="<@spring.url '/motech-user-management/js/jquery/jquery-1.8.2.min.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/motech-user-management/js/jquery/jquery.validate.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/motech-user-management/js/jquery/jquery-ui-1.9.1.custom.min.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/motech-user-management/js/bootstrap/bootstrap.min.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/motech-user-management/js/removeUser.js'/>"></script>
</head>
</html>

<body class="main">
<h1>User Listing</h1>

<div class="row-fluid">
<@paginator.filter id = "filter"  pagination_id = "listing">
    <div class="well" id="search-section">
        <div id="search-pane">
            <fieldset class="filters">
                <div class="row-fluid sel-result">
                        <div class="control-group span2">
                            <label class="control-label">User Name</label>

                            <div class="controls">
                                <input type="text" name="userName" id="userName" value="{{searchCriteria.userName}}" ng-cloak class="ng-cloak"/>
                            </div>
                        </div>
                        <div class="control-group span2">
                            <label class="control-label">Role</label>

                            <div class="controls">
                                <select id="role" name="role" value="{{searchCriteria.role}}">
                                    <option value=""></option>
                                    <#list roles as role>
                                        <option value="${role}">${role}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                </div>
            </fieldset>
            <div class="control-group buttons-group row-fluid">
                <div class="controls pull-right">
                    <button id="clearFilter" ng-click="clearFormFieldsAndSearchCriteria()" type="reset"
                            class="btn "><i class="icon-remove"></i> Clear All
                    </button>
                    <button type="submit" id="searchButton" class="btn btn-primary">
                        Search <i class="icon-search icon-white"></i>
                    </button>
                </div>
            </div>
        </div>
    </div>
</@paginator.filter>
</div>
<div class="row-fluid">
    <div class="results">
        <div>
        <@paginator.paginate id = "listing" entity="UserManagement" filterSectionId="filter" contextRoot="/${contextRoot}" rowsPerPage="20">
            <table id="userList" class="table table-striped table-bordered table-condensed"
                   redirectOnRowClick="true">
                <thead>
                <tr>
                        <th>User Name</th>
                        <th>Roles</th>
                        <th>Status</th>
                        <th>Reset Password</th>
                        <th>Activate</th>
                        <th>Deactivate</th>
                        <th>Delete</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in data.results" username={{item.userName}}>
                    <td>{{item.userName}}</td>
                    <td>
                        <span ng-repeat="role in item.roles">{{role}}&nbsp;&nbsp;</span>
                    </td>
                    <td type="status">
                        <div ng-show="item.active">
                            Active
                        </div>
                        <div ng-hide="item.active">
                            Inactive
                        </div>
                    </td>
                    <td><a class="resetPassword" id="resetPasswordLink" data-toggle="modal" href="#resetPasswordModal" ng-hide="!item.active" onclick="resetPassword(this)" data-username="{{item.userName}}" >Reset password</a></td>
                    <td><a class="activateUser" id="activateUserLink" data-toggle="modal" href="#activateUserModal"  ng-hide="item.active"  onclick="activateUser(this)" data-username="{{item.userName}}">Activate</a></td>
                    <td><a class="deactivateUserLink" data-url="<@spring.url "/userManagement/deactivateUser?userName={{item.userName}}"/>" ng-hide="!item.active"  onclick="deactivateUser(this)">Deactivate</a></td>
                    <td><a class="removeUserLink" data-url="<@spring.url "/userManagement/removeUser?userName={{item.userName}}"/>" onclick="removeUser(this)">Delete</a></td>
                </tr>
                <tr type="no-results" class="hide">
                    <td class="warning text-center" colspan="17"></td>
                </tr>
                </tbody>
            </table>
        </@paginator.paginate>
            <button class="btn btn-primary addNewUser" id="addNewUser"data-toggle="modal" href="#addNewUserModal">Add New User</button>
        </div>
    </div>
</div>
<@resetPassword/>
<@activateUser/>
<@addUser/>
</body>
<script type="text/javascript" src="<@spring.url '/motech-user-management/js/paginator/motech-paginator-angular.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/motech-user-management/js/paginator/motech-paginator-pagination.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/motech-user-management/js/paginator/motech-paginator-filter.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/motech-user-management/js/list.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/motech-user-management/js/util.js'/>"></script>
