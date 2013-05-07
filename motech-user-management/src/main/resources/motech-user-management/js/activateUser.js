$('#activateUserModal').on('shown', function () {
    $('#activateUserNewPassword').focus();
    $('#activateUserError').empty();
    $('#activateUserModal').find("input").val("");
    $('#activateUserError').hide();
    $('#activateUserServerSideError').hide();
});

$('#activateUser').click(function() {
    $('#activateUserModal').submit();
});

$('#activateUserModal').submit(function (event) {
    var username = $('#activateUserModal #activateUserUserNameSpan').html();
    $('#activateUserModal #activateUserUserName').val(username);

    if (!$('#activateUserModal').valid()) {
        $('#activateUserError').show();
    }
    else {
        event.preventDefault();
        var $form = $(this), url = $form.attr('action');
        $.ajax({
            type: 'POST',
            url: url,
            data: $form.serialize(),
            success: function () {
                $('#activateUserModal').modal('hide');
            },
            error: function (response) {
                $('#activateUserServerSideError').text(response);
                $('#activateUserServerSideError').show();
            }
        })

    }
});

$('#activateUserModal').on('hidden', function () {
    location.reload();
})

$('#activateUser').change(function () {
    $('#activateUserServerSideError').hide();
});

$('#activateUserModal').validate({
    rules:{
        activateUserNewPassword:{
            required:true,
            minlength:4
        },
        activateUserConfirmNewPassword:{
            required:true,
            equalTo:'#activateUserNewPassword'
        }
    },
    messages:{
        activateUserNewPassword:{
            required:"'Password' cannot be empty",
            minlength:"'Password' should be at least 4 characters long"
        },
        activateUserConfirmNewPassword:{
            required:"'Confirm Password' cannot be empty",
            equalTo:"'Confirm Password' should match 'Password'"
        }
    },
    errorPlacement:function (error, element) {
        $('#activateUserError').append(error);
    },
    errorLabelContainer:'#activateUserError'
});
