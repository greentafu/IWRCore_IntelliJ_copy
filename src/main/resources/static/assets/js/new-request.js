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

function loadItems(type){
    if(type==="request") getPlanProduct();
}

// 출하요청> 생산계획 목록
function getPlanProduct(){
    const content1 = document.getElementById("content1");
    const firstTbody = document.getElementById("firstTbody");
    $.ajax({
        url:'/select/getPlanProduct',
        method:'POST',
        data:{page:page, selectProL:selectProL, selectProM:selectProM, selectProS:selectProS, productSearch:productSearch},
        success:function(data){
            if(data.totalPage<page) finPage=true;
            data.dtoList.forEach(x=>{
                const proplanNo=x.proplanNo;
                const productNo=x.productDTO.manuCode;
                const productName=x.productDTO.name;
                const startDate=x.startDate.split('T')[0];
                const endDate=x.endDate.split('T')[0];
                const dates=startDate+'~'+endDate;
                const proPlanNum=x.pronum;

                const newRow = document.createElement("tr");

                [productNo, productName, dates, proPlanNum].forEach(text => {
                    const item = document.createElement("td");
                    item.textContent = text;
                    newRow.appendChild(item);
                });

                const btnTd = document.createElement("td");
                btnTd.innerHTML=`<button class="btn btn-sm btn-outline-primary" onclick="getOneProplan(${proplanNo})">선택</button>`;
                newRow.appendChild(btnTd);

                firstTbody.appendChild(newRow);
            });
        }
    });
    page++;
}
// 출하요청> 검색창 새로고침
function refreshSearch(){
    document.getElementById('selectProL').value='';
    document.getElementById('selectProM').value='';
    document.getElementById('selectProS').value='';
    document.getElementById('productSearch').value='';
    productSearchBtn('request');

    $('#exLargeModal').modal('hide');
}
// 출하요청> 생산계획 선택
function getOneProplan(proplanNo){
    refreshSearch();
    const content2 = document.getElementById("content2");
    const secondTbody = document.getElementById("secondTbody");
    secondTbody.innerText='';

    $.ajax({
        url:'/select/getOneProPlan',
        method:'POST',
        data:{proplanNo:proplanNo},
        success:function(data){

            const proplanDTO=data[0].proplanDTO;
            const productName=proplanDTO.productDTO.name;
            const productCode=proplanDTO.productDTO.manuCode;

            document.getElementById('proPlanNo').value=proplanDTO.proplanNo;
            document.getElementById('productName').value=productName;
            document.getElementById('productNo').value=productCode;

            data.forEach(x=>{
                const materialCode=x.structureDTO.materialDTO.materCode;
                const materialName=x.structureDTO.materialDTO.name;
                const structureNum=x.structureDTO.quantity;
                const sumRequest=x.sumRequest;
                const sumShip=x.sumShip;
                const remainNum=sumShip-sumRequest;

                const newRow = document.createElement('tr');

                const codeTd = document.createElement('td');
                codeTd.innerText = materialCode;
                codeTd.id = 'code'+materialCode;
                newRow.appendChild(codeTd);

                const nameTd = document.createElement('td');
                nameTd.innerText = materialName;
                newRow.appendChild(nameTd);

                const structureTd = document.createElement('td');
                structureTd.innerText = structureNum;
                structureTd.id = 'structure'+materialCode;
                newRow.appendChild(structureTd);

                const remainTd = document.createElement('td');
                remainTd.innerText = remainNum;
                remainTd.id = 'remain'+materialCode;
                newRow.appendChild(remainTd);

                const inputNum = document.createElement('input');
                inputNum.type = 'number';
                inputNum.style.border = '0px';
                inputNum.style.width = '150px';
                inputNum.id = 'inputNum'+materialCode;
                const reqNumTd = document.createElement('td');
                reqNumTd.appendChild(inputNum);
                newRow.appendChild(reqNumTd);

                const inputDate = document.createElement('input');
                inputDate.type = 'date';
                inputDate.style.border = '0px';
                inputDate.style.width = '150px';
                inputDate.id = 'inputDate'+materialCode;
                const dateTd = document.createElement('td');
                dateTd.appendChild(inputDate);
                newRow.appendChild(dateTd);

                const reNoTd = document.createElement('td');
                reNoTd.style.display = 'none';
                reNoTd.innerText = '';
                reNoTd.id = 'requestCode'+materialCode;
                newRow.appendChild(reNoTd);

                secondTbody.appendChild(newRow);
            });
        }
    });

    $.ajax({
        url:'/select/getOneProPlanLine',
        method:'POST',
        data:{proplanNo:proplanNo},
        success:function(data){
            const lineBox = document.getElementById('selectLine');
            data.forEach(x=>{
                const option = document.createElement('option');
                option.value = x.lineDTO.lineCode;
                option.textContent = x.lineDTO.lineName;
                lineBox.appendChild(option);
            });
        }
    });
}
// 출하요청> 출하요청 수정
function initRequest(){
    const content2 = document.getElementById("content2");
    const secondTbody = document.getElementById("secondTbody");
    secondTbody.innerText='';

    const preCode=document.getElementById('preCode').value;
    $.ajax({
        url:'/select/getRequest',
        method:'POST',
        data:{preCode:preCode},
        success:function(data){
            const preRequestDTO=data[0].preRequestDTO;
            const proplanDTO=preRequestDTO.proplanDTO;
            const productName=proplanDTO.productDTO.name;
            const productCode=proplanDTO.productDTO.manuCode;

            document.getElementById('proPlanNo').value=proplanDTO.proplanNo;
            document.getElementById('productName').value=productName;
            document.getElementById('productNo').value=productCode;

            data.forEach(x=>{
                const materialCode=x.structureDTO.materialDTO.materCode;
                const materialName=x.structureDTO.materialDTO.name;
                const structureNum=x.structureDTO.quantity;
                const sumRequest=x.sumRequest;
                const sumShip=x.sumShip;
                const remainNum=sumShip-sumRequest;

                const reqCode=x.requestDTO.requestCode;
                const reqNum=x.requestDTO.requestNum;
                const reqDate=x.requestDTO.eventDate.split('T')[0];
                const reqRelease=x.requestDTO.releaseDate;

                const newRow = document.createElement('tr');

                const codeTd = document.createElement('td');
                codeTd.innerText = materialCode;
                if(reqRelease===null){ codeTd.id = 'code'+materialCode; }
                newRow.appendChild(codeTd);

                const nameTd = document.createElement('td');
                nameTd.innerText = materialName;
                newRow.appendChild(nameTd);

                const structureTd = document.createElement('td');
                structureTd.innerText = structureNum;
                if(reqRelease===null){structureTd.id = 'structure'+materialCode;}
                newRow.appendChild(structureTd);

                const remainTd = document.createElement('td');
                remainTd.innerText = remainNum;
                if(reqRelease===null){remainTd.id = 'remain'+materialCode;}
                newRow.appendChild(remainTd);


                const reqNumTd = document.createElement('td');
                if(reqRelease===null){
                    const inputNum = document.createElement('input');
                    inputNum.type = 'number';
                    inputNum.style.border = '0px';
                    inputNum.style.width = '150px';
                    inputNum.id = 'inputNum'+materialCode;
                    inputNum.value = reqNum;
                    reqNumTd.appendChild(inputNum);
                }else{
                    reqNumTd.innerText = reqNum;
                }
                newRow.appendChild(reqNumTd);

                const dateTd = document.createElement('td');
                if(reqRelease===null){
                    const inputDate = document.createElement('input');
                    inputDate.type = 'date';
                    inputDate.style.border = '0px';
                    inputDate.style.width = '150px';
                    inputDate.id = 'inputDate'+materialCode;
                    inputDate.value = reqDate;
                    dateTd.appendChild(inputDate);
                }else{
                    dateTd.innerText = reqDate;
                }
                newRow.appendChild(dateTd);

                const reNoTd = document.createElement('td');
                reNoTd.style.display = 'none';
                reNoTd.innerText = reqCode;
                if(reqRelease===null){reNoTd.id = 'requestCode'+materialCode;}
                newRow.appendChild(reNoTd);

                secondTbody.appendChild(newRow);
            });
        }
    });
}
// 출하요청> 출하요청 상세보기
function detailRequest(){
    const content2 = document.getElementById("content2");
    const secondTbody = document.getElementById("secondTbody");
    secondTbody.innerText='';

    const preCode=document.getElementById('preCode').value;
    $.ajax({
        url:'/select/getRequest',
        method:'POST',
        data:{preCode:preCode},
        success:function(data){
            const preRequestDTO=data[0].preRequestDTO;
            const proplanDTO=preRequestDTO.proplanDTO;
            const productName=proplanDTO.productDTO.name;
            const productCode=proplanDTO.productDTO.manuCode;

            document.getElementById('proPlanNo').value=proplanDTO.proplanNo;
            document.getElementById('productName').value=productName;
            document.getElementById('productNo').value=productCode;

            data.forEach(x=>{
                const materialCode=x.structureDTO.materialDTO.materCode;
                const materialName=x.structureDTO.materialDTO.name;
                const structureNum=x.structureDTO.quantity;
                const sumRequest=x.sumRequest;
                const sumShip=x.sumShip;
                const remainNum=sumShip-sumRequest;

                const reqCode=x.requestDTO.requestCode;
                const reqNum=x.requestDTO.requestNum;
                const reqDate=x.requestDTO.eventDate.split('T')[0];

                const newRow = document.createElement('tr');

                const codeTd = document.createElement('td');
                codeTd.innerText = materialCode;
                newRow.appendChild(codeTd);

                const nameTd = document.createElement('td');
                nameTd.innerText = materialName;
                newRow.appendChild(nameTd);

                const structureTd = document.createElement('td');
                structureTd.innerText = structureNum;
                newRow.appendChild(structureTd);

                const remainTd = document.createElement('td');
                remainTd.innerText = remainNum;
                newRow.appendChild(remainTd);

                const reqNumTd = document.createElement('td');
                reqNumTd.innerText = reqNum;
                newRow.appendChild(reqNumTd);

                const dateTd = document.createElement('td');
                dateTd.innerText = reqDate;
                newRow.appendChild(dateTd);

                secondTbody.appendChild(newRow);
            });
        }
    });
}
// 출하요청> 출하요청 확인
function viewRequest(){
    const content2 = document.getElementById("content2");
    const secondTbody = document.getElementById("secondTbody");
    secondTbody.innerText='';

    const preCode=document.getElementById('preCode').value;
    $.ajax({
        url:'/select/getRequest',
        method:'POST',
        data:{preCode:preCode},
        success:function(data){
            const preRequestDTO=data[0].preRequestDTO;
            const proplanDTO=preRequestDTO.proplanDTO;
            const productName=proplanDTO.productDTO.name;
            const productCode=proplanDTO.productDTO.manuCode;

            document.getElementById('proPlanNo').value=proplanDTO.proplanNo;
            document.getElementById('productName').value=productName;
            document.getElementById('productNo').value=productCode;

            data.forEach(x=>{
                const materialCode=x.structureDTO.materialDTO.materCode;
                const materialName=x.structureDTO.materialDTO.name;
                const structureNum=x.structureDTO.quantity;
                const sumRequest=x.sumRequest;
                const sumShip=x.sumShip;
                const remainNum=sumShip-sumRequest;

                const reqCode=x.requestDTO.requestCode;
                const reqNum=x.requestDTO.requestNum;
                const reqDate=x.requestDTO.eventDate.split('T')[0];
                const reqRelease=x.requestDTO.releaseDate;

                const newRow = document.createElement('tr');

                const codeTd = document.createElement('td');
                codeTd.innerText = materialCode;
                newRow.appendChild(codeTd);

                const nameTd = document.createElement('td');
                nameTd.innerText = materialName;
                newRow.appendChild(nameTd);

                const structureTd = document.createElement('td');
                structureTd.innerText = structureNum;
                newRow.appendChild(structureTd);

                const remainTd = document.createElement('td');
                remainTd.innerText = remainNum;
                newRow.appendChild(remainTd);

                const reqNumTd = document.createElement('td');
                reqNumTd.innerText = reqNum;
                newRow.appendChild(reqNumTd);

                const dateTd = document.createElement('td');
                dateTd.innerText = reqDate;
                newRow.appendChild(dateTd);

                const btn1Td = document.createElement("td");
                if(reqRelease!==null){
                    btn1Td.innerHTML='출하완료';
                    btn1Td.style.color = 'green';
                }else if(remainNum<reqNum){
                    btn1Td.innerHTML='수량부족';
                    btn1Td.style.color = 'red';
                }else{
                    btn1Td.innerHTML=`<button class="btn btn-primary btn-sm" onclick=checkRequest(${reqCode})>출하하기</button>`;
                }
                newRow.appendChild(btn1Td);

                secondTbody.appendChild(newRow);
            });
        }
    });
}
// 출하요청> 수량자동입력
function autoCount(){
    const quantity=document.getElementById('product-quantity').value;
    const allInputNum=document.querySelectorAll('[id^="inputNum"]');
    allInputNum.forEach(x=>{
        const num=x.id.substring(8);
        const strNum=document.getElementById('structure'+num).textContent;
        x.value=strNum*quantity;
    });
}
// 출하요청> 자동입력
function autoBtn(){
    const inputDate=document.getElementById('desired-date').value;
    const allInputDate=document.querySelectorAll('[id^="inputDate"]');
    allInputDate.forEach(x=>{
        x.value=inputDate;
    });
}
// 출하요청> 출하정보 저장
function saveRequest(){
    const preCode=document.getElementById('preCode').value;
    const proPlanNo=document.getElementById('proPlanNo').value;
    const line=document.getElementById('selectLine').value;
    const requestText=document.getElementById('requestText').value;
    const requestDataList=[];

    let trueNum=false;
    let trueDate=false;

    if(proPlanNo===null || proPlanNo===''){
        alert('생산계획을 선택해주세요.');
    }else{

        const allInputNum=document.querySelectorAll('[id^="inputNum"]');
        allInputNum.forEach(x=>{
            const num=x.id.substring(8);
            let requestCode=document.getElementById('requestCode'+num).innerText;
            if(requestCode==='') reNo=null;
            const inputNum=Number(x.value);
            const inputDate=document.getElementById('inputDate'+num).value;

            if(inputNum!==null && inputNum!=='') {
                if(inputNum<0) trueNum=true;
                if(!Number.isInteger(inputNum)) trueNum=true;
                if(inputDate===null || inputDate==='') trueDate=true;
                requestDataList.push({long1:preCode, long2:line, long3:proPlanNo, long4:requestCode, long5:num,
                                      long6:inputNum, string1:requestText, string2:inputDate});
            }
        });

        if(trueNum){
            alert('요청수량에 0이상의 정수를 입력해주세요.');
        }else if(trueDate){
            alert('요청일자를 선택해주세요.');
        }else{
            $.ajax({
                url:'/saveRequest',
                method:'POST',
                contentType:'application/json',
                data: JSON.stringify(requestDataList),
                success: function(response) {
                    window.location.href = '/proteam/list_request';
                },
                error: function(xhr, status, error) {
                    window.location.href = '/proteam/list_request';
                }
            });
        }
    }
}
// 출하요청> 출하 확인
function checkRequest(button){
    const reqCode=button;
    $.ajax({
        url:'/saveRequestCheck',
        method:'POST',
        data: {reqCode:reqCode},
        success: function(response) {
            viewRequest();
        },
        error: function(xhr, status, error) {
            viewRequest();
        }
    });
}