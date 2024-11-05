function saveLocation(type){
    const location=document.getElementById('addLocation').value;
    $.ajax({
        url:'/saveLocation',
        method:'POST',
        data: {location:location},
        success: function(response) {
            if(type==="balju") refreshLocation();
        }
    });
}