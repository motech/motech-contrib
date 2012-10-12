$(document).ready(function(){
    $("#filter").click(function(){
        $.post("caselogs/filter",  $("#filter-case-logs-form").serialize(), function(data){
            $("#logs-content").html(data);
        });
    })
});