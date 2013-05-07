$(function () {
    $(".removeUserLink").click(function () {
        var answer = confirm('Are you sure?');
        if (answer) {
            var url = $(this).attr('href');
            $.get(url, function (response) {
                    location.reload();
                }
            );
        }
        return false;
    })
});