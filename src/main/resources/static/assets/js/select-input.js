if(document.getElementById('selColor')){
    document.getElementById('selColor').addEventListener('change', function() {
        const selectedValue = this.value;
        const inputField = document.getElementById('inputColor');
        inputField.value = selectedValue;
    });
}
if(document.getElementById('selColor2')){
    document.getElementById('selColor2').addEventListener('change', function() {
        const selectedValue = this.value;
        const inputField = document.getElementById('inputColor2');
        inputField.value = selectedValue;
    });
}
if(document.getElementById('selUnit')){
    document.getElementById('selUnit').addEventListener('change', function() {
        const selectedValue = this.value;
        const inputField = document.getElementById('inputUnit');
        inputField.value = selectedValue;
    });
}