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
                codeTd.id = 'materialCode'+materialCode;
                codeTd.innerText = materialCode;
                newRow.appendChild(codeTd);

                const nameTd = document.createElement('td');
                nameTd.id = 'materialName'+materialCode;
                nameTd.innerText = materialName;
                newRow.appendChild(nameTd);

                const structureTd = document.createElement('td');
                structureTd.innerText = structureNum;
                newRow.appendChild(structureTd);

                const remainTd = document.createElement('td');
                remainTd.id = 'remain'+materialCode;
                remainTd.innerText = remainNum;
                newRow.appendChild(remainTd);

                const reqNumTd = document.createElement('td');
                reqNumTd.id = 'reqNum'+materialCode;
                reqNumTd.innerText = reqNum;
                newRow.appendChild(reqNumTd);

                const dateTd = document.createElement('td');
                dateTd.id = 'reqDate'+materialCode;
                dateTd.innerText = reqDate;
                newRow.appendChild(dateTd);

                const btn1Td = document.createElement("td");
                if(reqRelease!==null){
                    btn1Td.innerHTML='출하완료';
                    btn1Td.style.color = 'green';
                }else if(remainNum<reqNum){
                    btn1Td.innerHTML=`
                        <button type="button" class="btn btn-outline-primary btn-sm" data-bs-toggle="modal" data-bs-target="#exLargeModal"
                            onclick="refreshContract(${materialCode})">
                            납품 요청
                        </button>
                    `;
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
// 긴급납품> 회사정보 가져오기
function getContract(materialCode){
    const content1 = document.getElementById("content1");
    const firstTbody = document.getElementById("firstTbody");

    const reqRemain=document.getElementById('remain'+materialCode).textContent;
    const reqNum=document.getElementById('reqNum'+materialCode).textContent;
    const reqDate=document.getElementById('reqDate'+materialCode).textContent;
    document.getElementById('urgencyRequestNum').textContent='출하요청 수량: '+reqNum;
    document.getElementById('urgencyRequestDate').textContent='출하요청 일자: '+reqDate;
    document.getElementById('urgencyMaterialCode').value=materialCode;

    document.getElementById('urgencyQuantity').value=Number(reqNum)-Number(reqRemain);
    if (reqDate) {
        const date = new Date(reqDate);
        date.setDate(date.getDate() - 2);
        const newDate = date.toISOString().split('T')[0];
        document.getElementById('urgencyDate').value=newDate;
    }

    const proNo=document.getElementById('proNo').value;
    $.ajax({
        url:'/select/getUrgencyContract',
        method:'POST',
        data: {page:page, proNo:proNo, materCode:materialCode},
        success: function(data) {
            if(data.totalPage<page) finPage=true;

            const contract=data.contractDTO;
            if(contract!=null){
                const materialName=document.getElementById('materialName'+materialCode).textContent;

                const partner=contract.partnerDTO;
                const allMake=(data.allMakeNum!==null)?Number(data.allMakeNum):0;
                const allShipNum=(data.allShipNum!==null)?Number(data.allShipNum):0;
                const allReturnNum=(data.allReturnNum!==null)?Number(data.allReturnNum):0;
                const allSend=allShipNum-allReturnNum;
                const allGet=allMake-allShipNum;

                document.getElementById('urgencyContract').textContent='협력회사: '+partner.name;
                document.getElementById('urgencyMaterial').textContent='자재: '+materialName+'('+materialCode+')';
                document.getElementById('urgencyStock').textContent='(계약수량: '+contract.conNum+', 납품수량: '+allSend+', 보유수량: '+allGet+')';
            }

            data.dtoList.forEach(x=>{
                const regDate=x.regDate.split('T')[0];
                const urNum=x.emerNum;
                const urDate=x.emerDate.split('T')[0];

                const newRow = document.createElement("tr");

                [regDate, urNum, urDate].forEach(text => {
                    const item = document.createElement("td");
                    item.textContent = text;
                    newRow.appendChild(item);
                });
                firstTbody.appendChild(newRow);
            });
        }
    });
    page++;
}
// 긴급납품> 모달창 새로고침
function refreshContract(materialCode){
    page = 1;
    finPage=false;
    document.getElementById("firstTbody").innerText='';
    getContract(materialCode);
}
// 긴급납품> 저장
function saveUrgency(){
    const urgencyNum=Number(document.getElementById('urgencyQuantity').value);
    const urgencyDate=document.getElementById('urgencyDate').value;
    const proplanNo=Number(document.getElementById('proNo').value);
    const materialCode=Number(document.getElementById('urgencyMaterialCode').value);

    let trueNum=false;
    let trueDate=false;
    let trueForm=false;

    if(urgencyNum<0 || urgencyNum===null || urgencyNum=='') trueNum=true;
    if(!Number.isInteger(urgencyNum)) trueNum=true;
    if(urgencyDate===null) trueDate=true;
    if(proPlanNo===null || proPlanNo==='') trueForm=true;
    if(materialCode===null || materialCode==='') trueForm=true;

    if(trueNum){
        alert('수량에 0이상의 정수를 입력해 주세요.');
    }else if(trueDate){
        alert('알맞은 요청일을 입력해 주세요.')
    }else if(trueForm){
        alert('형식이 잘못되었습니다.');
    }else{
        $.ajax({
            url:'/saveUrgency',
            method:'POST',
            data:{urgencyNum:urgencyNum, urgencyDate:urgencyDate, proplanNo:proplanNo, materialCode:materialCode},
            success: function(data) {
                if(data===0 || data==='0') alert('현재 진행중인 발주목록이 존재하지 않습니다.');
                else refreshContract(materialCode);
            },
            error: function(xhr, status, error) {
                $('#exLargeModal').modal('hide');
            }
        });
    }
}