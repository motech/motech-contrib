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

$(function () {
    $('#listing').bind('pageLoadSuccess', function () {

        function showNoResultsMessage() {
            $('[type=no-results] td').html(noResultsMessage);
            $('[type=no-results]').show();
        }

        var noResultsMessage ="";

        if ($('#userList tbody tr').length == 1) {
            if ($('#userName').val() == "" && $('#role').val() == "" ) {
                noResultsMessage= "Please filter by User Name or Role";
            }
            else{
                noResultsMessage = "No records found for given filter";
            }
            showNoResultsMessage()
        }
        else {
            $('[type=no-results]').hide();
        }
    });
});