<#macro activateUser>
<form class="modal hide" id="activateUserModal" submitOnEnterKey="true" action="<@spring.url "/userManagement/activateUser"/>">
    <div class="modal-header">
        <button class="close" data-dismiss="modal">x</button>
        <h3>Activate Web User</h3>
    </div>
    <div class="modal-body">
        <div id="activateUserServerSideError" class="alert alert-error hide"></div>
        <div id="activateUserError" class="alert alert-error hide"></div>
        <div class="control-group">
            <label class="float-left">User Name :&nbsp;</label>
            <input class="input-xlarge" type="hidden" name="userName" id="activateUserUserName" value=""/>
            <span name="activateUserUserNameSpan" id="activateUserUserNameSpan"></span>
        </div>
        <div class="control-group">
            <label class="control-label" for="activateUserNewPassword">Password *</label>

            <div class="controls">
                <input class="input-xlarge" type="password" name='newPassword' id="activateUserNewPassword"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="activateUserConfirmNewPassword">Confirm Password *</label>

            <div class="controls">
                <input class="input-xlarge" type="password" name='activateUserConfirmNewPassword' id="activateUserConfirmNewPassword"/>
            </div>
        </div>
    </div>
    <div class="modal-footer">

        <button type="button" class="btn  btn-primary" id="activateUser">Save</button>
        <button class="btn " data-dismiss="modal" id="activateUserClose">Close</button>
    </div>
</form>
<script type="text/javascript" src="<@spring.url '/motech-user-management/js/activateUser.js'/>"></script>
</#macro>