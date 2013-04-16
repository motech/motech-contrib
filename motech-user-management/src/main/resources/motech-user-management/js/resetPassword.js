$(function () {

    $('#resetPasswordModal').on('shown', function () {
        $('#newPassword').focus();
        $('#resetPasswordError').empty();
        $('#resetPasswordModal').find("input").val("");
        $('#resetPasswordError').hide();
        $('#resetPasswordServerSideError').hide();
    });

    $('#resetPasswordModal').submit(function (event) {
        var username = $('#resetPasswordModal span[name=userNameSpan]').html();
        $('#resetPasswordModal input[name=userName]').val(username);

        if (!$('#resetPasswordModal').valid()) {
            $('#resetPasswordError').show();
        }
        else {
            event.preventDefault();
            var $form = $(this), url = $form.attr('action');
            $.post(url, $form.serialize(),
                function (data) {
                    if (data == '') {
                        $('#resetPasswordModal').modal('hide');
                    }
                    else {
                        $('#resetPasswordServerSideError').text(data);
                        $('#resetPasswordServerSideError').show();
                    }
                }
            );
        }
    });


    $('#resetPasswordModal').validate({
        rules: {
            newPassword: {
                required: true,
                minlength: 4,
            },
            confirmNewPassword: {
                required: true,
                equalTo: '#newPassword'
            }
        },
        messages: {
            newPassword: {
                required: "'New Password' cannot be empty",
                minlength: "'New Password' should be at least 4 characters long",
            },
            confirmNewPassword: {
                required: "'Confirm New Password' cannot be empty",
                equalTo: "'Confirm New Password' should match 'New Password'"
            }
        },
        errorPlacement: function (error, element) {
            $('#resetPasswordError').append(error);
        },
        errorLabelContainer: '#resetPasswordError'
    });
});