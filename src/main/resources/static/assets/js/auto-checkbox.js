let jCheck=0;
let bCheck=0;
let gCheck=0;

const jBox=document.getElementById('autoJodalCheck');
const bBox=document.getElementById('autoBaljuCheck');
const gBox=document.getElementById('autoGumsuCheck');

document.addEventListener("DOMContentLoaded", function () {
    if(jBox) {
        checkJBox();
        jBox.addEventListener('change', function () { checkJBox(); changeAuto('j'); });
    }
    if(bBox) {
        checkBBox();
        bBox.addEventListener('change', function () { checkBBox(); changeAuto('b'); });
    }
    if(gBox) {
        checkGBox();
        gBox.addEventListener('change', function () { checkGBox(); changeAuto('g'); });
    }
});

function checkJBox(){ if(jBox.checked) jCheck=1; else jCheck=0;}
function checkBBox(){ if(bBox.checked) bCheck=1; else bCheck=0;}
function checkGBox(){ if(gBox.checked) gCheck=1; else gCheck=0; }

function changeAuto(type){
    $.ajax({
        url:'/input/changeAuto',
        method:'POST',
        data:{jCheck:jCheck, bCheck:bCheck, gCheck:gCheck},
        error: function(xhr, status, error) {
            if(type==='j') {
                if(jBox.checked) jBox.checked=false;
                else jBox.checked=true;
                checkJBox();
            }
            if(type==='b'){
                if(bBox.checked) bBox.checked=false;
                else bBox.checked=true;
                checkBBox();
            }
            if(type==='g'){
                if(gBox.checked) gBox.checked=false;
                else gBox.checked=true;
                checkGBox();
            }
        }
    });
}
