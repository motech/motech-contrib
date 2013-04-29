function resetPassword(elem){
    var username = $(elem).data('username')
    $('#resetPasswordModal span[name=userNameSpan]').html(username);
}

function activateUser(elem){
    var username = $(elem).data('username')
    $('#activateUserModal #activateUserUserNameSpan').html(username);
}

function removeUser(elem){
        var answer = confirm('Are you sure you want to remove user?');
        if (answer) {
            var url = $(elem).data('url');
            $.get(url, function (response) {
                    reloadPage()
                }
            );
        }
        return false;
}

function deactivateUser(elem){
    var answer = confirm('Are you sure you want to deactivate user?');
    if (answer) {
        var url = $(elem).data('url');
        $.get(url, function (response) {
                reloadPage()
            }
        );
    }
    return false;
}

function addUser(elem){
    reloadPage();
    return false;
}

function reloadPage(){
    angular.element($('#listing .paginator')).controller().loadPage();
}