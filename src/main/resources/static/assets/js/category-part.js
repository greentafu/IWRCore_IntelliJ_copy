// 변수
let showPartLCode=null;
let showPartMCode=null;
let showPartSCode=null;
let showPartLCode2=null;
let showPartMCode2=null;
let showPartSCode2=null;

let showPartRange=0; // 소속회사 안보기

document.addEventListener("DOMContentLoaded", function () {
    const noNoName=document.getElementById('noName'); // 미정, 소속회사 안보기
    if(noNoName) showPartRange=1;
    const yesCompany=document.getElementById('yesCompany'); // 모두 보기
    if(yesCompany) showPartRange=2;
    const selectedPno=document.getElementById('pno'); // 모두 보기
    if(selectedPno) {
        if(selectedPno.value===1 || selectedPno.value==='1') showPartRange=2;
    }

    initPart1();
    initPart2();

    const inputPartL=document.getElementById('inputPartL');
    const inputPartM=document.getElementById('inputPartM');
    if(inputPartL) inputPartL.addEventListener('input', () => refreshPartL());
    if(inputPartM) inputPartM.addEventListener('input', () => refreshPartM());

    const inputPartL2=document.getElementById('inputPartL2');
    const inputPartM2=document.getElementById('inputPartM2');
    if(inputPartL2) inputPartL2.addEventListener('input', () => refreshPartL2());
    if(inputPartM2) inputPartM2.addEventListener('input', () => refreshPartM2());
});

// 초기값1
function initPart1(){
    $.ajax({
        url:'/select/getPart',
        method:'GET',
        data:{ lcode:showPartLCode, mcode:showPartMCode, scode:showPartSCode, type:showPartRange },
        success:function(data){
            const selectPartL = document.getElementById('selectPartL');
            const selectPartM = document.getElementById('selectPartM');
            const selectPartS = document.getElementById('selectPartS');

            const inputPartL=document.getElementById('inputPartL');
            const inputPartM=document.getElementById('inputPartM');
            const inputPartS=document.getElementById('inputPartS');

            if(selectPartL){
                selectPartL.innerHTML = '';
                const allTd = document.createElement('option');
                allTd.value = '';
                allTd.textContent = '전체보기';
                selectPartL.appendChild(allTd);
                data.partLDTOs.forEach(function(partL) {
                    const option = document.createElement('option');
                    option.value = partL.partLcode;
                    option.textContent = partL.lname;
                    selectPartL.appendChild(option);
                    if (partL.partLcode == data.l) {
                        option.selected = true;
                        if(inputPartL) inputPartL.value = partL.lname;
                    }
                });
            }
            if(selectPartM){
                selectPartM.innerHTML = '';
                const allTd = document.createElement('option');
                allTd.value = '';
                allTd.textContent = '전체보기';
                selectPartM.appendChild(allTd);
                data.partMDTOs.forEach(function(partM) {
                    const option = document.createElement('option');
                    option.value = partM.partMcode;
                    option.textContent = partM.mname;
                    selectPartM.appendChild(option);
                    if (partM.partMcode == data.m) {
                        option.selected = true;
                        if(inputPartM) inputPartM.value = partM.mname;
                    }
                });
            }
            if(selectPartS){
                selectPartS.innerHTML = '';
                const allTd = document.createElement('option');
                allTd.value = '';
                allTd.textContent = '전체보기';
                selectPartS.appendChild(allTd);
                data.partSDTOs.forEach(function(partS) {
                    const option = document.createElement('option');
                    option.value = partS.partScode;
                    option.textContent = partS.sname;
                    selectPartS.appendChild(option);
                    if (partS.partScode == data.s) {
                        option.selected = true;
                        if(inputPartS) inputPartS.value = partS.sname;
                    }
                });
            }
        }
    });
}
// 초기값2
function initPart2(){
    $.ajax({
        url:'/select/getPart',
        method:'GET',
        data:{lcode:showPartLCode2, mcode:showPartMCode2, scode:showPartSCode2, type:showPartRange},
        success:function(data){
            const selectPartL = document.getElementById('selectPartL2');
            const selectPartM = document.getElementById('selectPartM2');
            const selectPartS = document.getElementById('selectPartS2');

            const inputPartL=document.getElementById('inputPartL2');
            const inputPartM=document.getElementById('inputPartM2');
            const inputPartS=document.getElementById('inputPartS2');

            if(selectPartL){
                selectPartL.innerHTML = '';
                const allTd = document.createElement('option');
                allTd.value = '';
                allTd.textContent = '전체보기';
                selectPartL.appendChild(allTd);
                data.partLDTOs.forEach(function(partL) {
                    const option = document.createElement('option');
                    option.value = partL.partLcode;
                    option.textContent = partL.lname;
                    selectPartL.appendChild(option);
                    if (partL.partLcode == data.l) {
                        option.selected = true;
                        if(inputPartL) inputPartL.value = partL.lname;
                    }
                });
            }
            if(selectPartM){
                selectPartM.innerHTML = '';
                const allTd = document.createElement('option');
                allTd.value = '';
                allTd.textContent = '전체보기';
                selectPartM.appendChild(allTd);
                data.partMDTOs.forEach(function(partM) {
                    const option = document.createElement('option');
                    option.value = partM.partMcode;
                    option.textContent = partM.mname;
                    selectPartM.appendChild(option);
                    if (partM.partMcode == data.m) {
                        option.selected = true;
                        if(inputPartM) inputPartM.value = partM.mname;
                    }
                });
            }
            if(selectPartS){
                selectPartS.innerHTML = '';
                const allTd = document.createElement('option');
                allTd.value = '';
                allTd.textContent = '전체보기';
                selectPartS.appendChild(allTd);
                data.partSDTOs.forEach(function(partS) {
                    const option = document.createElement('option');
                    option.value = partS.partScode;
                    option.textContent = partS.sname;
                    selectPartS.appendChild(option);
                    if (partS.partScode == data.s) {
                        option.selected = true;
                        if(inputPartS) inputPartS.value = partS.sname;
                    }
                });
            }
        }
    });
}

// 선택1
function updatePartCode(changedSelect){
    const selectPartL = document.getElementById('selectPartL');
    const selectPartM = document.getElementById('selectPartM');
    const selectPartS = document.getElementById('selectPartS');

    if(selectPartL) {
        const tempL=selectPartL.value;
        if(tempL==='') showPartLCode=null;
        else showPartLCode=tempL;
    }
    if(selectPartM) {
        const tempM=selectPartM.value;
        if(tempM==='') showPartMCode=null;
        else showPartMCode=tempM;
    }
    if(selectPartS) {
        const tempS=selectPartS.value;
        if(tempS==='') showPartSCode=null;
        else showPartSCode=tempS;
    }

    if (changedSelect === 'L') { showPartMCode=null; showPartSCode=null; }
    else if (changedSelect === 'M') { showPartSCode=null; }

    initPart1();
}
// 선택2
function updatePartCode2(changedSelect){
    const selectPartL = document.getElementById('selectPartL2');
    const selectPartM = document.getElementById('selectPartM2');
    const selectPartS = document.getElementById('selectPartS2');

    if(selectPartL) {
        const tempL=selectPartL.value;
        if(tempL==='') showPartLCode2=null;
        else showPartLCode2=tempL;
    }
    if(selectPartM) {
        const tempM=selectPartM.value;
        if(tempM==='') showPartMCode2=null;
        else showPartMCode2=tempM;
    }
    if(selectPartS) {
        const tempS=selectPartS.value;
        if(tempS==='') showPartSCode2=null;
        else showPartSCode2=tempS;
    }

    if (changedSelect === 'L') { showPartMCode2=null; showPartSCode2=null; }
    else if (changedSelect === 'M') { showPartSCode2=null; }

    initPart2();
}

// 입력값 변경1
function refreshPartL(){
    showPartLCode=null;
    showPartMCode=null;
    initPart1();
}
function refreshPartM(){
    showPartLCode=document.getElementById('selectPartL').value;
    showPartMCode=null;
    initPart1();
}
// 입력값 변경2
function refreshPartL2(){
    showPartLCode2=null;
    showPartMCode2=null;
    initPart2();
}
function refreshPartM2(){
    showPartLCode2=document.getElementById('selectPartL2').value;
    showPartMCode2=null;
    initPart2();
}