$(function () {
    $(".changePassword").click(function () {
       var username = $(this).parents('tr').attr('username');
       $('#changePasswordModal span[name=userNameSpan]').html(username);
   })

    $(".activateUser").click(function () {
        var username = $(this).parents('tr').attr('username');
        $('#activateUserModal #activateUserUserNameSpan').html(username);
    })

});