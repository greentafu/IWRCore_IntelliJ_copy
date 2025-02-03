// 변수
let showMaterLCode=null;
let showMaterMCode=null;
let showMaterSCode=null;
let showMaterLCode2=null;
let showMaterMCode2=null;
let showMaterSCode2=null;

let showMaterRange=0;

document.addEventListener("DOMContentLoaded", function () {
    const noNoName=document.getElementById('noName'); // 미정 안보기
    if(noNoName) showMaterRange=1;
    const yesCompany=document.getElementById('yesCompany');
    if(yesCompany) showMaterRange=2;

    initMater1();
    initMater2();

    const inputMaterL=document.getElementById('inputMaterL');
    const inputMaterM=document.getElementById('inputMaterM');
    if(inputMaterL) inputMaterL.addEventListener('input', () => refreshMaterL());
    if(inputMaterM) inputMaterM.addEventListener('input', () => refreshMaterM());

    const inputMaterL2=document.getElementById('inputMaterL2');
    const inputMaterM2=document.getElementById('inputMaterM2');
    if(inputMaterL2) inputMaterL2.addEventListener('input', () => refreshMaterL2());
    if(inputMaterM2) inputMaterM2.addEventListener('input', () => refreshMaterM2());
});

// 초기값1
function initMater1(){
    $.ajax({
        url:'/select/getMater',
        method:'GET',
        data:{ lcode:showMaterLCode, mcode:showMaterMCode, scode:showMaterSCode, type:showMaterRange },
        success:function(data){
            const selectMaterL = document.getElementById('selectMaterL');
            const selectMaterM = document.getElementById('selectMaterM');
            const selectMaterS = document.getElementById('selectMaterS');

            const inputMaterL=document.getElementById('inputMaterL');
            const inputMaterM=document.getElementById('inputMaterM');
            const inputMaterS=document.getElementById('inputMaterS');

            if(selectMaterL){
                selectMaterL.innerHTML = '';
                const allTd = document.createElement('option');
                allTd.value = '';
                allTd.textContent = '전체보기';
                selectMaterL.appendChild(allTd);
                data.materLDTOs.forEach(function(materL) {
                    const option = document.createElement('option');
                    option.value = materL.materLcode;
                    option.textContent = materL.lname;
                    selectMaterL.appendChild(option);
                    if (materL.materLcode == data.l) {
                        option.selected = true;
                        if(inputMaterL) inputMaterL.value = materL.lname;
                    }
                });
            }
            if(selectMaterM){
                selectMaterM.innerHTML = '';
                const allTd = document.createElement('option');
                allTd.value = '';
                allTd.textContent = '전체보기';
                selectMaterM.appendChild(allTd);
                data.materMDTOs.forEach(function(materM) {
                    const option = document.createElement('option');
                    option.value = materM.materMcode;
                    option.textContent = materM.mname;
                    selectMaterM.appendChild(option);
                    if (materM.materMcode == data.m) {
                        option.selected = true;
                        if(inputMaterM) inputMaterM.value = materM.mname;
                    }
                });
            }
            if(selectMaterS){
                selectMaterS.innerHTML = '';
                const allTd = document.createElement('option');
                allTd.value = '';
                allTd.textContent = '전체보기';
                selectMaterS.appendChild(allTd);
                data.materSDTOs.forEach(function(materS) {
                    const option = document.createElement('option');
                    option.value = materS.materScode;
                    option.textContent = materS.sname;
                    selectMaterS.appendChild(option);
                    if (materS.materScode == data.s) {
                        option.selected = true;
                        if(inputMaterS) inputMaterS.value = materS.sname;
                    }
                });
            }
        }
    });
}
// 초기값2
function initMater2(){
    $.ajax({
        url:'/select/getMater',
        method:'GET',
        data:{lcode:showMaterLCode2, mcode:showMaterMCode2, scode:showMaterSCode2, type:showMaterRange},
        success:function(data){
            const selectMaterL = document.getElementById('selectMaterL2');
            const selectMaterM = document.getElementById('selectMaterM2');
            const selectMaterS = document.getElementById('selectMaterS2');

            const inputMaterL=document.getElementById('inputMaterL2');
            const inputMaterM=document.getElementById('inputMaterM2');
            const inputMaterS=document.getElementById('inputMaterS2');

            if(selectMaterL){
                selectMaterL.innerHTML = '';
                const allTd = document.createElement('option');
                allTd.value = '';
                allTd.textContent = '전체보기';
                selectMaterL.appendChild(allTd);
                data.materLDTOs.forEach(function(materL) {
                    const option = document.createElement('option');
                    option.value = materL.materLcode;
                    option.textContent = materL.lname;
                    selectMaterL.appendChild(option);
                    if (materL.materLcode == data.l) {
                        option.selected = true;
                        if(inputMaterL) inputMaterL.value = materL.lname;
                    }
                });
            }
            if(selectMaterM){
                selectMaterM.innerHTML = '';
                const allTd = document.createElement('option');
                allTd.value = '';
                allTd.textContent = '전체보기';
                selectMaterM.appendChild(allTd);
                data.materMDTOs.forEach(function(materM) {
                    const option = document.createElement('option');
                    option.value = materM.materMcode;
                    option.textContent = materM.mname;
                    selectMaterM.appendChild(option);
                    if (materM.materMcode == data.m) {
                        option.selected = true;
                        if(inputMaterM) inputMaterM.value = materM.mname;
                    }
                });
            }
            if(selectMaterS){
                selectMaterS.innerHTML = '';
                const allTd = document.createElement('option');
                allTd.value = '';
                allTd.textContent = '전체보기';
                selectMaterS.appendChild(allTd);
                data.materSDTOs.forEach(function(materS) {
                    const option = document.createElement('option');
                    option.value = materS.materScode;
                    option.textContent = materS.sname;
                    selectMaterS.appendChild(option);
                    if (materS.materScode == data.s) {
                        option.selected = true;
                        if(inputMaterS) inputMaterS.value = materS.sname;
                    }
                });
            }
        }
    });
}

// 선택1
function updateMaterCode(changedSelect){
    const selectMaterL = document.getElementById('selectMaterL');
    const selectMaterM = document.getElementById('selectMaterM');
    const selectMaterS = document.getElementById('selectMaterS');

    if(selectMaterL) {
        const tempL=selectMaterL.value;
        if(tempL==='') showMaterLCode=null;
        else showMaterLCode=tempL;
    }
    if(selectMaterM) {
        const tempM=selectMaterM.value;
        if(tempM==='') showMaterMCode=null;
        else showMaterMCode=tempM;
    }
    if(selectMaterS) {
        const tempS=selectMaterS.value;
        if(tempS==='') showMaterSCode=null;
        else showMaterSCode=tempS;
    }

    if (changedSelect === 'L') { showMaterMCode=null; showMaterSCode=null; }
    else if (changedSelect === 'M') { showMaterSCode=null; }

    initMater1();
}
// 선택2
function updateMaterCode2(changedSelect){
    const selectMaterL = document.getElementById('selectMaterL2');
    const selectMaterM = document.getElementById('selectMaterM2');
    const selectMaterS = document.getElementById('selectMaterS2');

    if(selectMaterL) {
        const tempL=selectMaterL.value;
        if(tempL==='') showMaterLCode2=null;
        else showMaterLCode2=tempL;
    }
    if(selectMaterM) {
        const tempM=selectMaterM.value;
        if(tempM==='') showMaterMCode2=null;
        else showMaterMCode2=tempM;
    }
    if(selectMaterS) {
        const tempS=selectMaterS.value;
        if(tempS==='') showMaterSCode2=null;
        else showMaterSCode2=tempS;
    }

    if (changedSelect === 'L') { showMaterMCode2=null; showMaterSCode2=null; }
    else if (changedSelect === 'M') { showMaterSCode2=null; }

    initMater2();
}

// 입력값 변경1
function refreshMaterL(){
    showMaterLCode=null;
    showMaterMCode=null;
    initMater1();
}
function refreshMaterM(){
    showMaterLCode=document.getElementById('selectMaterL').value;
    showMaterMCode=null;
    initMater1();
}
// 입력값 변경2
function refreshMaterL2(){
    showMaterLCode2=null;
    showMaterMCode2=null;
    initMater2();
}
function refreshMaterM2(){
    showMaterLCode2=document.getElementById('selectMaterL2').value;
    showMaterMCode2=null;
    initMater2();
}