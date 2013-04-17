$(function () {

    $('#addNewUserModal').on('shown', function () {
        $('#newUserUserName').focus();
        $('#addUserError').empty();
        $('#addNewUserModal').find("input").val("");
        $('#addUserError').hide();
        $('#addUserServerSideError').hide();
    });

    $('#addNewUserModal').submit(function (event) {

        if (!$('#addNewUserModal').valid()) {
            $('#addUserError').show();
        }
        else {
            event.preventDefault();
            var $form = $(this), url = $form.attr('action');

            var person = {
                userName: $('input[name="newUserUserName"]').val(),
                password: $('input[name="newUserPassword"]').val(),
                roles: $('select[name="roles"]').val(),
                externalId: $('input[name="externalId"]').val()
            };

            $.ajax({
                type: 'POST',
                url: url,
                data: JSON.stringify(person),
                success: function () {
                        $('#addNewUserModal').modal('hide');
                    },
                error: function (data) {
                        $('#addUserServerSideError').text(data);
                        $('#addUserServerSideError').show();
                },
                contentType: "application/json; charset=utf-8"
            })
        }
    });

    $('#addNewUserModal').on('hidden', function () {
        location.reload();
    })

    $('#addNewUserModal').validate({
        rules: {
            newUserPassword: {
                required: true,
                minlength: 4
            },
            newUserConfirmPassword: {
                required: true,
                equalTo: '#newUserPassword'
            },
            newUserUserName: {
                required: true
            },
            roles: {
                required: function(element) {
                    if($("#roles").val() != null && $("#roles").val() !=''){
                        return false;
                    }else{
                        return true;
                    }
                }
            }
        },
        messages: {
            newUserPassword: {
                required: "'Password' cannot be empty",
                minlength: "'Password' should be at least 4 characters long"
            },
            newUserConfirmPassword: {
                required: "'Confirm New Password' cannot be empty",
                equalTo: "'Confirm New Password' should match 'New Password'"
            },
            newUserUserName: {
                required: "Username is required"
            },
            externalId: {
                required: "External Id is required"
            },
            roles: {
                required: "Role is required"
            }
        },
        errorPlacement: function (error, element) {
            $('#addUserError').append(error);
        },
        errorLabelContainer: '#addUserError'
    });
});