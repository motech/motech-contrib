<#macro addUser>
<form class="modal hide form-horizontal" id="addNewUserModal" submitOnEnterKey="true" action="<@spring.url '/userManagement/addNewUser'/>">
    <div class="modal-header">
        <button class="close" data-dismiss="modal">x</button>
        <h3>Add User</h3>
    </div>
    <div class="modal-body">
        <div id="addUserServerSideError" class="alert alert-error hide"></div>
        <div id="addUserError" class="alert alert-error hide"></div>

        <div class="control-group">
            <label class="control-label" for="newUserUserName">User Name</label>
            <div class="controls">
                <input class="input-xlarge" type="text" name='newUserUserName' id="newUserUserName"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="newUserPassword">Password</label>

            <div class="controls">
                <input class="input-xlarge" type="password" name='newUserPassword' id="newUserPassword"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="newUserConfirmPassword">Confirm Password</label>

            <div class="controls">
                <input class="input-xlarge" type="password" name='newUserConfirmPassword' id="newUserConfirmPassword"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="externalId">External Id</label>

            <div class="controls">
                <input class="input-xlarge" type="text" name='externalId' id="externalId"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="role">Role</label>
            <select id="role" name="role">
                <option value=""></option>
                <#list roles as role>
                    <option value="${role}">${role}</option>
                </#list>
            </select>
        </div>
    <div class="modal-footer">

        <button type="submit" class="btn  btn-primary" id="addUser"><i class="icon-ok icon-white"></i> Save</button>
        <button class="btn " data-dismiss="modal"><i class="icon-remove"></i> Close</button>
    </div>
</form>
<script type="text/javascript" src="<@spring.url '/motech-user-management/js/addNewUser.js'/>"></script>
</#macro>