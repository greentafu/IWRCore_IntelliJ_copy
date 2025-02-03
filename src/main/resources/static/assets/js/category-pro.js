// 변수
let showProLCode=null;
let showProMCode=null;
let showProSCode=null;
let showProLCode2=null;
let showProMCode2=null;
let showProSCode2=null;

let showProRange=0;

document.addEventListener("DOMContentLoaded", function () {
    const noNoName=document.getElementById('noName'); // 미정 안보기
    if(noNoName) showProRange=1;
    const yesCompany=document.getElementById('yesCompany');
    if(yesCompany) showProRange=2;

    initPro1();
    initPro2();

    const inputProL=document.getElementById('inputProL');
    const inputProM=document.getElementById('inputProM');
    if(inputProL) inputProL.addEventListener('input', () => refreshProL());
    if(inputProM) inputProM.addEventListener('input', () => refreshProM());

    const inputProL2=document.getElementById('inputProL2');
    const inputProM2=document.getElementById('inputProM2');
    if(inputProL2) inputProL2.addEventListener('input', () => refreshProL2());
    if(inputProM2) inputProM2.addEventListener('input', () => refreshProM2());
});

// 초기값1
function initPro1(){
    $.ajax({
        url:'/select/getPro',
        method:'GET',
        data:{ lcode:showProLCode, mcode:showProMCode, scode:showProSCode, type:showProRange },
        success:function(data){
            const selectProL = document.getElementById('selectProL');
            const selectProM = document.getElementById('selectProM');
            const selectProS = document.getElementById('selectProS');

            const inputProL=document.getElementById('inputProL');
            const inputProM=document.getElementById('inputProM');
            const inputProS=document.getElementById('inputProS');

            if(selectProL){
                selectProL.innerHTML = '';
                const allTd = document.createElement('option');
                allTd.value = '';
                allTd.textContent = '전체보기';
                selectProL.appendChild(allTd);
                data.proLDTOs.forEach(function(proL) {
                    const option = document.createElement('option');
                    option.value = proL.proLcode;
                    option.textContent = proL.lname;
                    selectProL.appendChild(option);
                    if (proL.proLcode == data.l) {
                        option.selected = true;
                        if(inputProL) inputProL.value = proL.lname;
                    }
                });
            }
            if(selectProM){
                selectProM.innerHTML = '';
                const allTd = document.createElement('option');
                allTd.value = '';
                allTd.textContent = '전체보기';
                selectProM.appendChild(allTd);
                data.proMDTOs.forEach(function(proM) {
                    const option = document.createElement('option');
                    option.value = proM.proMcode;
                    option.textContent = proM.mname;
                    selectProM.appendChild(option);
                    if (proM.proMcode == data.m) {
                        option.selected = true;
                        if(inputProM) inputProM.value = proM.mname;
                    }
                });
            }
            if(selectProS){
                selectProS.innerHTML = '';
                const allTd = document.createElement('option');
                allTd.value = '';
                allTd.textContent = '전체보기';
                selectProS.appendChild(allTd);
                data.proSDTOs.forEach(function(proS) {
                    const option = document.createElement('option');
                    option.value = proS.proScode;
                    option.textContent = proS.sname;
                    selectProS.appendChild(option);
                    if (proS.proScode == data.s) {
                        option.selected = true;
                        if(inputProS) inputProS.value = proS.sname;
                    }
                });
            }
        }
    });
}
// 초기값2
function initPro2(){
    $.ajax({
        url:'/select/getPro',
        method:'GET',
        data:{lcode:showProLCode2, mcode:showProMCode2, scode:showProSCode2, type:showProRange},
        success:function(data){
            const selectProL = document.getElementById('selectProL2');
            const selectProM = document.getElementById('selectProM2');
            const selectProS = document.getElementById('selectProS2');

            const inputProL=document.getElementById('inputProL2');
            const inputProM=document.getElementById('inputProM2');
            const inputProS=document.getElementById('inputProS2');

            if(selectProL){
                selectProL.innerHTML = '';
                const allTd = document.createElement('option');
                allTd.value = '';
                allTd.textContent = '전체보기';
                selectProL.appendChild(allTd);
                data.proLDTOs.forEach(function(proL) {
                    const option = document.createElement('option');
                    option.value = proL.proLcode;
                    option.textContent = proL.lname;
                    selectProL.appendChild(option);
                    if (proL.proLcode == data.l) {
                        option.selected = true;
                        if(inputProL) inputProL.value = proL.lname;
                    }
                });
            }
            if(selectProM){
                selectProM.innerHTML = '';
                const allTd = document.createElement('option');
                allTd.value = '';
                allTd.textContent = '전체보기';
                selectProM.appendChild(allTd);
                data.proMDTOs.forEach(function(proM) {
                    const option = document.createElement('option');
                    option.value = proM.proMcode;
                    option.textContent = proM.mname;
                    selectProM.appendChild(option);
                    if (proM.proMcode == data.m) {
                        option.selected = true;
                        if(inputProM) inputProM.value = proM.mname;
                    }
                });
            }
            if(selectProS){
                selectProS.innerHTML = '';
                const allTd = document.createElement('option');
                allTd.value = '';
                allTd.textContent = '전체보기';
                selectProS.appendChild(allTd);
                data.proSDTOs.forEach(function(proS) {
                    const option = document.createElement('option');
                    option.value = proS.proScode;
                    option.textContent = proS.sname;
                    selectProS.appendChild(option);
                    if (proS.proScode == data.s) {
                        option.selected = true;
                        if(inputProS) inputProS.value = proS.sname;
                    }
                });
            }
        }
    });
}

// 선택1
function updateProCode(changedSelect){
    const selectProL = document.getElementById('selectProL');
    const selectProM = document.getElementById('selectProM');
    const selectProS = document.getElementById('selectProS');

    if(selectProL) {
        const tempL=selectProL.value;
        if(tempL==='') showProLCode=null;
        else showProLCode=tempL;
    }
    if(selectProM) {
        const tempM=selectProM.value;
        if(tempM==='') showProMCode=null;
        else showProMCode=tempM;
    }
    if(selectProS) {
        const tempS=selectProS.value;
        if(tempS==='') showProSCode=null;
        else showProSCode=tempS;
    }

    if (changedSelect === 'L') { showProMCode=null; showProSCode=null; }
    else if (changedSelect === 'M') { showProSCode=null; }

    initPro1();
}
// 선택2
function updateProCode2(changedSelect){
    const selectProL = document.getElementById('selectProL2');
    const selectProM = document.getElementById('selectProM2');
    const selectProS = document.getElementById('selectProS2');

    if(selectProL) {
        const tempL=selectProL.value;
        if(tempL==='') showProLCode2=null;
        else showProLCode2=tempL;
    }
    if(selectProM) {
        const tempM=selectProM.value;
        if(tempM==='') showProMCode2=null;
        else showProMCode2=tempM;
    }
    if(selectProS) {
        const tempS=selectProS.value;
        if(tempS==='') showProSCode2=null;
        else showProSCode2=tempS;
    }

    if (changedSelect === 'L') { showProMCode2=null; showProSCode2=null; }
    else if (changedSelect === 'M') { showProSCode2=null; }

    initPro2();
}

// 입력값 변경1
function refreshProL(){
    showProLCode=null;
    showProMCode=null;
    initPro1();
}
function refreshProM(){
    showProLCode=document.getElementById('selectProL').value;
    showProMCode=null;
    initPro1();
}
// 입력값 변경2
function refreshProL2(){
    showProLCode2=null;
    showProMCode2=null;
    initPro2();
}
function refreshProM2(){
    showProLCode2=document.getElementById('selectProL2').value;
    showProMCode2=null;
    initPro2();
}