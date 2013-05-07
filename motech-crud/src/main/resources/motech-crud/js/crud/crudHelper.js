function trimmed(values){
    for(var a in values){
        if(typeof(values[a])=='object'){
            values[a] = trimmed(values[a]);
        }
        else{
            if(jQuery.type(values[a]) == "string" && values[a] != undefined)
                values[a] = values[a].trim();
        }
    }
    return values;
}