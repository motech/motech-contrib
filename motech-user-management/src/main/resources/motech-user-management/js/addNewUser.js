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
            $.post(url, $form.serialize(),
                    function (data) {
                    if (data == '') {
                        $('#addNewUserModal').modal('hide');
                    }
                    else {
                        $('#addUserServerSideError').text(data);
                        $('#addUserServerSideError').show();
                    }
                },
                "json"
            );
        }
    });


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
            externalId: {
                required: true
            },
            role: {
                required: true
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
            role: {
                required: "Role is required"
            }
        },
        errorPlacement: function (error, element) {
            $('#addUserError').append(error);
        },
        errorLabelContainer: '#addUserError'
    });
});