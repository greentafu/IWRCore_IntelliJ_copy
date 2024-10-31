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
    if(type="contract") getAllPartner();
}

function loadItems2(type){
    if(type="contract") getAllNonContractJodalPlan();
}

// 협력회사 선택
function getOnePartner(pno){
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
            partnerSearchBtn('contract');

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
                btnTd.innerHTML=`<button class="btn btn-sm btn-outline-primary" onclick="getOnePartner(${pno})">선택</button>`;
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







