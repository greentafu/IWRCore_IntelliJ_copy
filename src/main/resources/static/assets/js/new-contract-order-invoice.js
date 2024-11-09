// 변수
let page = 1;
let finPage=false;
let selectPartL = null;
let selectPartM = null;
let selectPartS = null;
let partnerSearch = null;
let selectProL = null;
let selectProM = null;
let selectProS = null;
let productSearch = null;
let selectMaterL = null;
let selectMaterM = null;
let selectMaterS = null;
let materialSearch = null;

let page2 = 1;
let finPage2=false;
let selectPartL2 = null;
let selectPartM2 = null;
let selectPartS2 = null;
let partnerSearch2 = null;
let selectProL2 = null;
let selectProM2 = null;
let selectProS2 = null;
let productSearch2 = null;
let selectMaterL2 = null;
let selectMaterM2 = null;
let selectMaterS2 = null;
let materialSearch2 = null;

let checkValue = 0;

// 협력회사 검색
function partnerSearchBtn(type){
    page = 1;
    finPage=false;
    document.getElementById("firstTbody").innerText='';
    selectPartL = (document.getElementById("selectPartL").value!=null)?document.getElementById("selectPartL").value:null;
    selectPartM = (document.getElementById("selectPartM").value!=null)?document.getElementById("selectPartM").value:null;
    selectPartS = (document.getElementById("selectPartS").value!=null)?document.getElementById("selectPartS").value:null;
    partnerSearch = (document.getElementById("partnerSearch").value!=null)?document.getElementById("partnerSearch").value:null;
    loadItems(type);
}
function partnerSearchBtn2(type){
    page2 = 1;
    finPage2=false;
    document.getElementById("secondTbody").innerText='';
    selectPartL2 = (document.getElementById("selectPartL2").value!=null)?document.getElementById("selectPartL2").value:null;
    selectPartM2 = (document.getElementById("selectPartM2").value!=null)?document.getElementById("selectPartM2").value:null;
    selectPartS2 = (document.getElementById("selectPartS2").value!=null)?document.getElementById("selectPartS2").value:null;
    partnerSearch2 = (document.getElementById("partnerSearch2").value!=null)?document.getElementById("partnerSearch2").value:null;
    loadItems2(type);
}
// 제품 검색
function productSearchBtn(type){
    page = 1;
    finPage=false;
    document.getElementById("firstTbody").innerText='';
    selectProL = (document.getElementById("selectProL").value!=null)?document.getElementById("selectProL").value:null;
    selectProM = (document.getElementById("selectProM").value!=null)?document.getElementById("selectProM").value:null;
    selectProS = (document.getElementById("selectProS").value!=null)?document.getElementById("selectProS").value:null;
    productSearch = (document.getElementById("productSearch").value!=null)?document.getElementById("productSearch").value:null;
    loadItems(type);
}
function productSearchBtn2(type){
    page2 = 1;
    finPage2=false;
    document.getElementById("secondTbody").innerText='';
    selectProL2 = (document.getElementById("selectProL2").value!=null)?document.getElementById("selectProL2").value:null;
    selectProM2 = (document.getElementById("selectProM2").value!=null)?document.getElementById("selectProM2").value:null;
    selectProS2 = (document.getElementById("selectProS2").value!=null)?document.getElementById("selectProS2").value:null;
    productSearch2 = (document.getElementById("productSearch2").value!=null)?document.getElementById("productSearch2").value:null;
    loadItems2(type);
}
// 자제 검색
function materialSearchBtn(type){
    page = 1;
    finPage=false;
    document.getElementById("firstTbody").innerText='';
    selectMaterL = (document.getElementById("selectMaterL").value!=null)?document.getElementById("selectMaterL").value:null;
    selectMaterM = (document.getElementById("selectMaterM").value!=null)?document.getElementById("selectMaterM").value:null;
    selectMaterS = (document.getElementById("selectMaterS").value!=null)?document.getElementById("selectMaterS").value:null;
    materialSearch = (document.getElementById("materialSearch").value!=null)?document.getElementById("materialSearch").value:null;
    loadItems(type);
}
function materialSearchBtn2(type){
    page2 = 1;
    finPage2=false;
    document.getElementById("secondTbody").innerText='';
    selectMaterL2 = (document.getElementById("selectMaterL2").value!=null)?document.getElementById("selectMaterL2").value:null;
    selectMaterM2 = (document.getElementById("selectMaterM2").value!=null)?document.getElementById("selectMaterM2").value:null;
    selectMaterS2 = (document.getElementById("selectMaterS2").value!=null)?document.getElementById("selectMaterS2").value:null;
    materialSearch2 = (document.getElementById("materialSearch2").value!=null)?document.getElementById("materialSearch2").value:null;
    loadItems2(type);
}

function loadItems(type){
    if(type==="contract") getAllPartner();
    if(type==="balju") getBaljuPartner();
    if(type==="gumsu") getGumsuPartner();
}

function loadItems2(type){
    if(type="contract") getAllNonContractJodalPlan();
}

// 협력회사 선택
function getOnePartner(pno, type){
    const word=type;
    $.ajax({
        url:'/select/getOnePartner',
        method:'POST',
        data:{pno:pno},
        success:function(data){
            document.getElementById('partnerName').value=data.name;
            document.getElementById('partnerNo').value=data.pno;

            document.getElementById('selectPartL').value='';
            document.getElementById('selectPartM').value='';
            document.getElementById('selectPartS').value='';
            document.getElementById('partnerSearch').value='';
            partnerSearchBtn(word);

            $('#modalScrollable').modal('hide');
        }
    });
}








// 계약서> 협력회사 목록 가져오기
function getAllPartner() {
    const content1 = document.getElementById("content1");
    const firstTbody = document.getElementById("firstTbody");
    $.ajax({
        url:'/select/getAllPartner',
        method:'POST',
        data:{page:page, selectPartL:selectPartL, selectPartM:selectPartM, selectPartS:selectPartS, partnerSearch:partnerSearch},
        success:function(data){
            if(data.totalPage<page) finPage=true;
            data.dtoList.forEach(x=>{
                const pno=x.pno;
                const name=x.name;
                const number=x.registrationNumber;

                const newRow = document.createElement("tr");

                [name, number].forEach(text => {
                    const item = document.createElement("td");
                    item.textContent = text;
                    newRow.appendChild(item);
                });

                const btnTd = document.createElement("td");
                btnTd.innerHTML=`<button class="btn btn-sm btn-outline-primary" onclick="getOnePartner(${pno}, 'contract')">선택</button>`;
                newRow.appendChild(btnTd);

                firstTbody.appendChild(newRow);
            });
        }
    });
    page++;
}
// 계약서> 계약하지 않은 조달계획 목록
function getAllNonContractJodalPlan() {
    const content2 = document.getElementById("content2");
    const secondTbody = document.getElementById("secondTbody");

    const allInputCash=document.querySelectorAll('[id^="inputCash"]');
    const longList=[];

    // 선택된 조달계획번호 목록
    allInputCash.forEach(x=>{
        const num=x.id.substring(9);
        longList.push(num);
    });

    $.ajax({
        url:'/select/getAllNonContractJodalPlan',
        method:'POST',
        data:{page2:page2, longList:longList, selectMaterL2:selectMaterL2, selectMaterM2:selectMaterM2, selectMaterS2:selectMaterS2, materialSearch2:materialSearch2},
        success:function(data){
            if(data.totalPage<page2) finPage2=true;

            data.dtoList.forEach(x=>{
                const joNo=x.jodalPlanDTO.joNo;
                const materCode=x.jodalPlanDTO.materialDTO.materCode;
                const materName=x.jodalPlanDTO.materialDTO.name;
                const allNum=x.allJodalChasuNum;

                const newRow = document.createElement('tr');

                const inputTd = document.createElement('td');
                const input = document.createElement('input');
                input.type = 'checkbox';
                input.id = 'checkbox'+joNo;
                input.class='form-check-input';
                inputTd.appendChild(input);
                newRow.appendChild(inputTd);

                [joNo, materCode, materName].forEach(function(text){
                    const td = document.createElement('td');
                    td.innerText = text;
                    newRow.appendChild(td);
                });

                const numTd = document.createElement('td');
                numTd.innerText = allNum;
                numTd.style.display = 'none';
                newRow.appendChild(numTd);

                secondTbody.appendChild(newRow);
            });
        }
    });
    page2++;
}
// 계약서> 하단에 조달계획 추가
function addJodalPlan(){
    const planRows=document.querySelectorAll('#secondTbody tr');
    const insertPoint=document.querySelector('#insertPoint');

    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    const formattedDate = `${year}-${month}-${day}`;

    planRows.forEach(x=>{
        const checkbox=x.querySelector('input[type="checkbox"]');
        if(checkbox&&checkbox.checked){
            const cells=x.querySelectorAll('td');
            const joNo=cells[1].innerText;
            const materNo=cells[2].innerText;
            const materName=cells[3].innerText;
            const allNum=cells[4].innerText;

            const newRow = document.createElement('tr');

            [joNo, materNo, materName].forEach(text => {
                const td = document.createElement('td');
                td.innerText = text;
                newRow.appendChild(td);
            });

            const inputCash = document.createElement('input');
            inputCash.type = 'number';
            inputCash.style.border = '0px';
            inputCash.style.width = '150px';
            inputCash.id = 'inputCash'+joNo;
            const cashTd = document.createElement('td');
            cashTd.appendChild(inputCash);
            newRow.appendChild(cashTd);

            const inputNum = document.createElement('input');
            inputNum.type = 'number';
            inputNum.style.border = '0px';
            inputNum.style.width = '150px';
            inputNum.id = 'inputNum'+joNo;
            inputNum.value=allNum;
            const numTd = document.createElement('td');
            numTd.appendChild(inputNum);
            newRow.appendChild(numTd);

            const inputDate = document.createElement('input');
            inputDate.type = 'date';
            inputDate.style.border = '0px';
            inputDate.style.width = '150px';
            inputDate.id = 'inputDate'+joNo;
            inputDate.value=formattedDate;
            const dateTd = document.createElement('td');
            dateTd.appendChild(inputDate);
            newRow.appendChild(dateTd);

            const deleteTd = document.createElement('td');
            const button = document.createElement('button');
            button.type = 'button';
            button.className = 'btn btn-sm btn-secondary';
            button.innerText = '삭제';
            button.onclick = function() {
                newRow.remove(); // Remove the row when the button is clicked
                renew_getAllNonContractJodalPlan();
            };
            deleteTd.appendChild(button);
            newRow.appendChild(deleteTd);

            insertPoint.appendChild(newRow);
        }
    });
    renew_getAllNonContractJodalPlan();
}
// 계약서> 페이지 로드시 하단에 조달계획 1추가
function initJodalPlan(){
    const joNo=document.getElementById('selectedJoNo').innerText;
    const planRows=document.querySelectorAll('#secondTbody tr');
    const insertPoint=document.querySelector('#insertPoint');

    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    const formattedDate = `${year}-${month}-${day}`;

    if(joNo!=='' && joNo!==null){
        $.ajax({
            url:'/select/getOneNonContractJodalPlan',
            method:'POST',
            data:{joNo:joNo},
            success:function(data){
                const joNo=data.jodalPlanDTO.joNo;
                const materNo=data.jodalPlanDTO.materialDTO.materCode;
                const materName=data.jodalPlanDTO.materialDTO.name;
                const allNum=data.allJodalChasuNum;

                const newRow = document.createElement('tr');

                [joNo, materNo, materName].forEach(text => {
                    const td = document.createElement('td');
                    td.innerText = text;
                    newRow.appendChild(td);
                });

                const inputCash = document.createElement('input');
                inputCash.type = 'number';
                inputCash.style.border = '0px';
                inputCash.style.width = '150px';
                inputCash.id = 'inputCash'+joNo;
                const cashTd = document.createElement('td');
                cashTd.appendChild(inputCash);
                newRow.appendChild(cashTd);

                const inputNum = document.createElement('input');
                inputNum.type = 'number';
                inputNum.style.border = '0px';
                inputNum.style.width = '150px';
                inputNum.id = 'inputNum'+joNo;
                inputNum.value=allNum;
                const numTd = document.createElement('td');
                numTd.appendChild(inputNum);
                newRow.appendChild(numTd);

                const inputDate = document.createElement('input');
                inputDate.type = 'date';
                inputDate.style.border = '0px';
                inputDate.style.width = '150px';
                inputDate.id = 'inputDate'+joNo;
                inputDate.value=formattedDate;
                const dateTd = document.createElement('td');
                dateTd.appendChild(inputDate);
                newRow.appendChild(dateTd);

                const deleteTd = document.createElement('td');
                const button = document.createElement('button');
                button.type = 'button';
                button.className = 'btn btn-sm btn-secondary';
                button.innerText = '삭제';
                button.onclick = function() {
                    newRow.remove(); // Remove the row when the button is clicked
                    renew_getAllNonContractJodalPlan();
                };
                deleteTd.appendChild(button);
                newRow.appendChild(deleteTd);

                insertPoint.appendChild(newRow);
            }
        });
    }
}
// 계약서> 조달계획 목록 새로고침
function renew_getAllNonContractJodalPlan(){
    page2 = 1;
    finPage2=false;
    document.getElementById("secondTbody").innerText='';
    getAllNonContractJodalPlan();
}
// 계약서> 계약하지 않은 조달계획 모두 선택
function selectAllNonContractJodalPlan(){
    const checkBox=document.querySelectorAll('input[type="checkbox"]');
    checkBox.forEach(x=>{
        if(checkValue==0) {x.checked=true;}
        if(checkValue==1) {x.checked=false;}
    });
    checkValue=(checkValue===0)?1:0;
    document.getElementById('checkValue').value = checkValue;
}
// 계약서> 계약 저장
function saveContract(){
    const conNo=document.getElementById('selectedconNo').innerText;
    if(conNo==='') conNo==null;
    const person=document.getElementById('person').value;
    const partnerNo=document.getElementById('partnerNo').value;
    const planData=[];

    const insertPoint=document.querySelectorAll('#insertPoint tr');
    insertPoint.forEach(x=>{
        const cells=x.querySelectorAll('td');
        const joNo=cells[0].innerText;

        const inputCash=document.getElementById('inputCash'+joNo).value;
        const inputNum=document.getElementById('inputNum'+joNo).value;
        const inputDate=document.getElementById('inputDate'+joNo).value;

        planData.push({long1:joNo, long2:inputCash, long3:inputNum, string:inputDate});
    });

    formData1.append('conNo', conNo);
    formData1.append('person', person);
    formData1.append('partnerNo', partnerNo);
    formData1.append('planData', JSON.stringify(planData));
    formData1.append('deleteFile', deleteFile1);

    $.ajax({
        url:'/saveContract',
        method:'POST',
        contentType:'application/json',
        data: formData1,
              processData: false, // jQuery가 데이터를 처리하지 않도록 설정
              contentType: false, // jQuery가 Content-Type을 설정하지 않도록 설정
        success: function(response) {
            window.location.href = '/contract/list_contract';
        },
        error: function(xhr, status, error) {
            window.location.href = '/contract/list_contract';
        }
    });
}









// 발주서> 협력회사 목록 가져오기
function getBaljuPartner() {
    const content1 = document.getElementById("content1");
    const firstTbody = document.getElementById("firstTbody");
    $.ajax({
        url:'/select/getBaljuPartner',
        method:'POST',
        data:{page:page, selectPartL:selectPartL, selectPartM:selectPartM, selectPartS:selectPartS, partnerSearch:partnerSearch},
        success:function(data){
            if(data.totalPage<page) finPage=true;
            data.dtoList.forEach(x=>{
                const pno=x.pno;
                const name=x.name;
                const number=x.registrationNumber;

                const newRow = document.createElement("tr");

                [name, number].forEach(text => {
                    const item = document.createElement("td");
                    item.textContent = text;
                    newRow.appendChild(item);
                });

                const btnTd = document.createElement("td");
                btnTd.innerHTML=`<button class="btn btn-sm btn-outline-primary" onclick="getOnePartner(${pno}, 'balju'); getNonBaljuContract(${pno});">선택</button>`;
                newRow.appendChild(btnTd);

                firstTbody.appendChild(newRow);
            });
        }
    });
    page++;
}
// 발주서> 회사에 알맞은 발주하지 않은 계약 목록
function getNonBaljuContract(pno){
    $.ajax({
        url:'/select/getNonBaljuContract',
        method:'POST',
        data:{pno:pno},
        success:function(data){
            const setTable=document.getElementById('setTable');
            setTable.innerHTML='';
            if(data!=null){
                data.forEach(x=>{
                    const conNo=x.contractDTO.conNo;
                    const conNum=x.contractDTO.conNum;
                    const materCode=x.contractDTO.jodalPlanDTO.materialDTO.materCode;
                    const materName=x.contractDTO.jodalPlanDTO.materialDTO.name;
                    const jcnum1=x.jodalChasuDTOs[0].jcnum;
                    const jcnum2=x.jodalChasuDTOs[1].jcnum;
                    const jcnum3=x.jodalChasuDTOs[2].jcnum;
                    const joNum1=x.jodalChasuDTOs[0].joNum;
                    const joNum2=x.jodalChasuDTOs[1].joNum;
                    const joNum3=x.jodalChasuDTOs[2].joNum;
                    const joDate1=x.jodalChasuDTOs[0].joDate.split('T')[0];
                    const joDate2=x.jodalChasuDTOs[1].joDate.split('T')[0];
                    const joDate3=x.jodalChasuDTOs[2].joDate.split('T')[0];
                    const rem=joNum1+joNum2+joNum3;
                    const remainder=rem-conNum;

                    const newTable = document.createElement("table");
                    newTable.classList.add('table', 'table-bordered', 'mb-4');
                    newTable.id = 'autoTable'+conNo;

                    const newTbody = document.createElement("tbody");

                    // tr1
                    const newRow1 = document.createElement("tr");
                    // tr1-th1
                    const checkTh = document.createElement('th');
                    checkTh.id = 'check'+conNo;
                    checkTh.rowSpan = 6;
                    checkTh.style.width = '10px';
                    checkTh.className = 'text-center';
                    const checkInput = document.createElement('input');
                    checkInput.type = 'checkbox';
                    checkInput.id = 'btn'+conNo;
                    checkInput.className = 'form-check-input';
                    checkTh.appendChild(checkInput);
                    const conNumInput = document.createElement('input');
                    conNumInput.type = 'number';
                    conNumInput.id = 'contractSum'+conNo;
                    conNumInput.style.display = 'none';
                    conNumInput.value = conNum;
                    checkTh.appendChild(conNumInput);
                    const baljuNoInput = document.createElement('input');
                    baljuNoInput.type = 'number';
                    baljuNoInput.id = 'baljuNo'+conNo;
                    baljuNoInput.style.display = 'none';
                    checkTh.appendChild(baljuNoInput);
                    newRow1.appendChild(checkTh);
                    // tr1-th2
                    const title1 = document.createElement('th');
                    title1.style.width = '200px';
                    title1.textContent = '자재번호';
                    newRow1.appendChild(title1);
                    // tr1-th3
                    const title2 = document.createElement('th');
                    title2.textContent = '자재명';
                    newRow1.appendChild(title2);
                    // tr1-th4
                    const title3 = document.createElement('th');
                    title3.style.width = '80px';
                    newRow1.appendChild(title3);
                    // tr1-th5
                    const title4 = document.createElement('th');
                    title4.style.width = '150px';
                    title4.textContent = '수량 ()';
                    if(remainder>0) title4.textContent = `수량 (+${remainder})`;
                    if(remainder<=0) title4.textContent = `수량 (${remainder})`;
                    title4.id = 'sumCount'+conNo;
                    newRow1.appendChild(title4);
                    // tr1-th6
                    const title5 = document.createElement('th');
                    title5.textContent = '요청일';
                    newRow1.appendChild(title5);

                    newTbody.appendChild(newRow1);


                    // tr2
                    const newRow2 = document.createElement("tr");
                    // tr2-th1
                    const materCodeTd = document.createElement('td');
                    materCodeTd.rowSpan = 3;
                    materCodeTd.textContent = materCode;
                    newRow2.appendChild(materCodeTd);
                    // tr2-th2
                    const materNameTd = document.createElement('td');
                    materNameTd.rowSpan = 3;
                    materNameTd.textContent = materName;
                    newRow2.appendChild(materNameTd);
                    // tr2-input1
                    const content11 = document.createElement('td');
                    content11.textContent = '1차';
                    newRow2.appendChild(content11);
                    // tr2-input2
                    const content12 = document.createElement('td');
                    const a12Input = document.createElement('input');
                    a12Input.type = 'number';
                    a12Input.id = 'oneNum'+conNo;
                    a12Input.value = joNum1;
                    a12Input.style.width = '100%';
                    a12Input.style.border = '0';
                    if(rem<conNum) a12Input.style.color = 'red';
                    content12.appendChild(a12Input);
                    newRow2.appendChild(content12);
                    // tr2-input3
                    const content13 = document.createElement('td');
                    const a13Input = document.createElement('input');
                    a13Input.type = 'date';
                    a13Input.id = 'oneDate'+conNo;
                    a13Input.value = joDate1;
                    a13Input.style.width = '100%';
                    a13Input.style.border = '0';
                    content13.appendChild(a13Input);
                    newRow2.appendChild(content13);

                    newTbody.appendChild(newRow2);



                    // tr3
                    const newRow3 = document.createElement("tr");
                    // tr3-input1
                    const content21 = document.createElement('td');
                    content21.textContent = '2차';
                    newRow3.appendChild(content21);
                    // tr3-input2
                    const content22 = document.createElement('td');
                    const a22Input = document.createElement('input');
                    a22Input.type = 'number';
                    a22Input.id = 'twoNum'+conNo;
                    a22Input.value = joNum2;
                    a22Input.style.width = '100%';
                    a22Input.style.border = '0';
                    if(rem<conNum) a22Input.style.color = 'red';
                    content22.appendChild(a22Input);
                    newRow3.appendChild(content22);
                    // tr3-input3
                    const content23 = document.createElement('td');
                    const a23Input = document.createElement('input');
                    a23Input.type = 'date';
                    a23Input.id = 'twoDate'+conNo;
                    a23Input.value = joDate2;
                    a23Input.style.width = '100%';
                    a23Input.style.border = '0';
                    content23.appendChild(a23Input);
                    newRow3.appendChild(content23);

                    newTbody.appendChild(newRow3);



                    // tr4
                    const newRow4 = document.createElement("tr");
                    // tr4-input1
                    const content31 = document.createElement('td');
                    content31.textContent = '3차';
                    newRow4.appendChild(content31);
                    // tr4-input2
                    const content32 = document.createElement('td');
                    const a32Input = document.createElement('input');
                    a32Input.type = 'number';
                    a32Input.id = 'threeNum'+conNo;
                    a32Input.value = joNum3;
                    a32Input.style.width = '100%';
                    a32Input.style.border = '0';
                    if(rem<conNum) a32Input.style.color = 'red';
                    content32.appendChild(a32Input);
                    newRow4.appendChild(content32);
                    // tr4-input3
                    const content33 = document.createElement('td');
                    const a33Input = document.createElement('input');
                    a33Input.type = 'date';
                    a33Input.id = 'threeDate'+conNo;
                    a33Input.value = joDate3;
                    a33Input.style.width = '100%';
                    a33Input.style.border = '0';
                    content33.appendChild(a33Input);
                    newRow4.appendChild(content33);

                    newTbody.appendChild(newRow4);



                    // tr5
                    const newRow5 = document.createElement("tr");
                    // tr5-location
                    const locationWord = document.createElement('th');
                    locationWord.textContent = '배송장소';
                    newRow5.appendChild(locationWord);
                    // tr5-locationInput
                    const location = document.createElement('td');
                    location.colSpan = 4;
                    const locationInput = document.createElement('input');
                    locationInput.type = 'text';
                    locationInput.id = 'location'+conNo;
                    locationInput.style.width = '100%';
                    locationInput.style.border = '0';
                    location.appendChild(locationInput);
                    newRow5.appendChild(location);

                    newTbody.appendChild(newRow5);

                    // tr6
                    const newRow6 = document.createElement("tr");
                    // tr6-location
                    const callWord = document.createElement('th');
                    callWord.textContent = '배송 요청 사항';
                    newRow6.appendChild(callWord);
                    // tr6-locationInput
                    const call = document.createElement('td');
                    call.colSpan = 4;
                    const callInput = document.createElement('textarea');
                    callInput.rows = 4;
                    callInput.id = 'text'+conNo;
                    callInput.style.width = '100%';
                    callInput.style.border = '0';
                    call.appendChild(callInput);
                    newRow6.appendChild(call);

                    newTbody.appendChild(newRow6);


                    newTable.appendChild(newTbody);
                    setTable.appendChild(newTable);
                });
            }
        }
    });
}
// 발주서> 배송장소 목록
function locationList(){
    const content2 = document.getElementById("content2");
    const secondTbody = document.getElementById("secondTbody");
    const word = document.getElementById('searchLocation').value;
    $.ajax({
        url:'/select/getLocation',
        method:'POST',
        data:{page2:page2, word:word},
        success:function(data){
            if(data.totalPage<page2) finPage2=true;
            data.dtoList.forEach(x=>{
                const lno=x.lno;
                const loc=x.location;

                const newRow = document.createElement("tr");

                const locTd = document.createElement("td");
                locTd.textContent = loc;
                newRow.appendChild(locTd);

                const btnTd = document.createElement("td");
                btnTd.innerHTML=`<button class="btn btn-sm btn-outline-primary" onclick="selectLocation('${loc}')">선택</button>`;
                newRow.appendChild(btnTd);

                secondTbody.appendChild(newRow);
            });
        }
    });
    page2++;
}
// 발주서> 배송목록 새로고침
function refreshLocation(){
    page2 = 1;
    finPage2=false;
    document.getElementById("secondTbody").innerText='';
    locationList();
}
// 발주서> 배송목록 선택
function selectLocation(loc){
    document.getElementById('loc-input').value=loc;
    document.getElementById('searchLocation').value='';
    $('#modalScrollable2').modal('hide');

    refreshLocation();
}
// 발주서> 배송장소 일괄 넣기
function fillLocation(number){
    const no=number;
    const location=document.getElementById('loc-input').value;
    const checkbox=document.getElementById('btn'+no);
    if(checkbox && checkbox.checked){
        document.getElementById('location'+no).value=location;
    }
}
// 발주서> 발주서 수량 확인
function checkSumBaljuNum(whatInput){
    const input=whatInput;
    let no="";
    if(input.id.startsWith('three')) no=input.id.substring(8);
    else no=input.id.substring(6);

    const quantity=document.getElementById('contractSum'+no).value;

    const oneNumInput=document.getElementById('oneNum'+no);
    const twoNumInput=document.getElementById('twoNum'+no);
    const threeNumInput=document.getElementById('threeNum'+no);
    const oneNum=Number(oneNumInput.value);
    const twoNum=Number(twoNumInput.value);
    const threeNum=Number(threeNumInput.value);

    if((oneNum+twoNum+threeNum)<quantity) {
        oneNumInput.style.color = 'red';
        twoNumInput.style.color = 'red';
        threeNumInput.style.color = 'red';
    }else{
        oneNumInput.style.color = 'black';
        twoNumInput.style.color = 'black';
        threeNumInput.style.color = 'black';
    }

    const remainder=(oneNum+twoNum+threeNum)-quantity;
    if(remainder>0) document.getElementById('sumCount'+no).innerText = `수량 (+${remainder})`;
    if(remainder<=0) document.getElementById('sumCount'+no).innerText = `수량 (${remainder})`;
};
// 발주서> 수량 자동 넣기
function autoFillBaljuNum(number){
    const no=number;
    const checkbox=document.getElementById('btn'+no);
    if(checkbox && checkbox.checked){
        const quantity=document.getElementById('contractSum'+no).value;

        const eachQuantity=Math.floor(quantity/3);
        const remainder=quantity%3;
        const thirdQuantity=eachQuantity+remainder;

        document.getElementById('oneNum'+no).value=eachQuantity;
        document.getElementById('twoNum'+no).value=eachQuantity;
        document.getElementById('threeNum'+no).value=thirdQuantity;

        document.getElementById('oneNum'+no).style.color = 'black';
        document.getElementById('twoNum'+no).style.color = 'black';
        document.getElementById('threeNum'+no).style.color = 'black';

        document.getElementById('sumCount'+no).innerText = '수량 (0)';
    }
}
// 발주서> 발주서 저장
function saveBalju(){
    const allTable=document.querySelectorAll('[id^="autoTable"]');
    const baljuDataList=[];

    let trueNum=false;
    let trueDate=false;
    let trueSum=false;
    let trueLocation=false;

    allTable.forEach(x=>{
        const tableId=x.id;
        const no=tableId.substring(9);
        const checkbox=document.getElementById('btn'+no);

        if(checkbox && checkbox.checked){
            let baljuNo=document.getElementById('baljuNo'+no).value;
            if(baljuNo==='') baljuNo=null;
            const baljuWhere=document.getElementById('location'+no).value;
            const baljuPlz=document.getElementById('text'+no).value;

            const oneNumInput=document.getElementById('oneNum'+no);
            const twoNumInput=document.getElementById('twoNum'+no);
            const threeNumInput=document.getElementById('threeNum'+no);

            const oneNum=Number(oneNumInput.value);
            const twoNum=Number(twoNumInput.value);
            const threeNum=Number(threeNumInput.value);
            const oneDate=document.getElementById('oneDate'+no).value;
            const twoDate=document.getElementById('twoDate'+no).value;
            const threeDate=document.getElementById('threeDate'+no).value;

            const oneColor = getComputedStyle(oneNumInput).color;
            const twoColor = getComputedStyle(twoNumInput).color;
            const threeColor = getComputedStyle(threeNumInput).color;

            if(baljuWhere=='' || baljuWhere==null) trueLocation=true;

            if(oneNum<0 || oneNum===null || oneNum=='') trueNum=true;
            if(twoNum<0 || twoNum===null || twoNum=='') trueNum=true;
            if(threeNum<0 || threeNum===null || threeNum=='') trueNum=true;

            if(!Number.isInteger(oneNum) || !Number.isInteger(twoNum) || !Number.isInteger(threeNum)) trueNum=true;

            if(oneDate===null || twoDate===null || threeDate===null) trueDate=true;
            if(oneDate>=twoDate || twoDate>=threeDate) trueDate=true;

            if(oneColor === 'rgb(255, 0, 0)' || oneColor === 'red') trueSum=true;
            if(twoColor === 'rgb(255, 0, 0)' || twoColor === 'red') trueSum=true;
            if(threeColor === 'rgb(255, 0, 0)' || threeColor === 'red') trueSum=true;

            baljuDataList.push({baljuNo:baljuNo, conNo:no, baljuWhere:baljuWhere, baljuPlz:baljuPlz,
                oneNum:oneNum, oneDate:oneDate, twoNum:twoNum, twoDate:twoDate, threeNum:threeNum, threeDate:threeDate});
        }
    });
    if(trueNum){
        alert('수량에 0이상의 정수를 입력해 주세요.');
    }else if(trueDate){
        alert('알맞은 발주일을 입력해 주세요.')
    }else if(trueSum){
        alert('발주량이 계약량 미만입니다.');
    }else if(trueLocation){
        alert('배송지를 입력해 주세요.');
    }else{
        $.ajax({
            url:'/saveBalju',
            method:'POST',
            contentType:'application/json',
            data: JSON.stringify(baljuDataList),
            success: function(response) {
                window.location.href = '/order/list_order';
            },
            error: function(xhr, status, error) {
                window.location.href = '/order/list_order';
            }
        });
    }
}


