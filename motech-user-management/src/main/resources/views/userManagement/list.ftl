<#import "/spring.ftl" as spring />
<#include "changePassword.ftl">
<#include "activateUser.ftl">

<html>
<head>
    <title> User Management </title>

    <link rel="stylesheet" type="text/css" href="<@spring.url '/motech-user-management/css/bootstrap.min.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<@spring.url '/motech-user-management/css/jquery-ui-1.9.1.custom.min.css'/>"/>
    <script type="text/javascript"  src="<@spring.url '/motech-user-management/js/jquery/jquery-1.8.2.min.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/motech-user-management/js/jquery/jquery.validate.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/motech-user-management/js/list.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/motech-user-management/js/jquery/jquery-ui-1.9.1.custom.min.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/motech-user-management/js/bootstrap/bootstrap.min.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/motech-user-management/js/removeUser.js'/>"></script>
</head>
</html>
<body class="main">
<div class="container"
<div class="row-fluid">
    <div class="hero-unit">
        <h1>User Management List</h1>
    </div>
    <table id="userList" class="table table-bordered table-striped table-hover">
        <thead>
        <tr>
            <th>User Name</th>
            <th>External Id</th>
            <th>Is Active</th>
            <th>Change Password</th>
            <th>Activate User</th>
            <th>Remove User</th>
        </tr>
        </thead>
        <tbody>
        <#if users?size == 0>
        <tr>
            <td class="warning" style="text-align: center" colspan="6">
                No registered users to show
            </td>
        </tr>
        <#else>
            <#list users as user>
            <tr id="user_${user.externalId}" username=${user.userName}>
                <td class="userName">${user.userName}</td>
                <td class="externalId">${user.externalId}</td>
                <td class="isActive">${user.active?string}</td>
                <td><a class="changePassword" id="changePasswordLink" data-toggle="modal" href="#changePasswordModal">Change password</a></td>
                <td><a class="activateUser" id="activateUserLink" data-toggle="modal" href="#activateUserModal">Activate user</a></td>
                <td><a class="removeUserLink" href="<@spring.url "/userManagement/removeUser?userName=${user.userName}"/>">Remove user</a></td>
            </tr>
            </#list>
        </#if>
        </tbody>
    </table>
</div>
</div>
<@changePassword/>
<@activateUser/>
</body>

