// 변수
let page = 1;
let finPage=false;
let selectProL = null;
let selectProM = null;
let selectProS = null;
let productSearch = null;

let exLine=[];

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
    if(type==="proplan") getAllProduct();
}

// 전체 제품목록 가져오기
function getAllProduct(){
    const content1 = document.getElementById("content1");
    const firstTbody = document.getElementById("firstTbody");
    $.ajax({
        url:'/select/getAllProduct',
        method:'POST',
        data:{page:page, selectProL:selectProL, selectProM:selectProM, selectProS:selectProS, productSearch:productSearch},
        success:function(data){
            if(data.totalPage<page) finPage=true;
            data.dtoList.forEach(x=>{
                const manuCode=x.manuCode;
                const name=x.name;

                const newRow = document.createElement("tr");

                [manuCode, name].forEach(text => {
                    const item = document.createElement("td");
                    item.textContent = text;
                    newRow.appendChild(item);
                });

                const btnTd = document.createElement("td");
                btnTd.innerHTML=`<button class="btn btn-sm btn-outline-primary" onclick="getOneProduct(${manuCode}, '${name}')">선택</button>`;
                newRow.appendChild(btnTd);

                firstTbody.appendChild(newRow);
            });
        }
    });
    page++;
}
// 제품 하나 선택
function getOneProduct(manuCode, name){
    document.getElementById('manuCode').value=manuCode;
    document.getElementById('productName').value=name;
    initLine();

    $('#modalScrollable').modal('hide');
}

// 생산계획 생산라인 수량 가져오기
function initLine(){
    const manuCode=document.getElementById('manuCode').value;
    if(manuCode!==null && manuCode!==''){
        $.ajax({
            url:'/getLines',
            method:'GET',
            data:{manuCode:manuCode},
            success:function(data){
                if(data){
                    data.forEach(x=>{
                        const quantity=x.quantity;
                        const lineId='line'+x.line.lineCode;
                        const input=document.getElementById(lineId);
                        input.value=quantity;

                        exLine.push(quantity);
                    });
                }
            }
        });
    }
}
// 생산계획 생산라인 수량 저장
function saveLine(){
    const manuCode=document.getElementById('manuCode').value;
    const list=[];
    const lines = document.querySelectorAll('input[id^="line"]');

    let trueNum=false;
    let minNum=false;
    lines.forEach(x=>{
        let temp=x.value;
        if(temp===null || temp==='') temp=0;
        const number=Number(temp);
        list.push(number);
        if(!Number.isInteger(number)) trueNum=true;
        if(number<0) minNum=true;
    });

    if(manuCode===null || manuCode===''){
        alert('제품을 선택해주세요.');
    }else if(trueNum || minNum){
        alert('생산라인의 일일생산량은 0이상의 정수만 입력해주세요.');
    }else{
        $.ajax({
            url:'/saveLine',
            method:'GET',
            data:{manuCode:manuCode, quantityList:list},
            success:function(data){
                exLine=list;
            }
        });
    }
}
// 생산계획 저장
function savePlan(){
    const proplanNo=document.getElementById('proplanNo').value;
    const manuCode=document.getElementById('manuCode').value;
    const pronum=document.getElementById('pronum').value;
    const startDate=document.getElementById('startDate').value;
    const endDate=document.getElementById('endDate').value;
    const details=document.getElementById('details').value;

    const list=[];
    const boxes = document.querySelectorAll('input[id^="ckeck"]');
    boxes.forEach(x=>{
        if(x.checked){
            list.push(x.value);
        }
    });

    let trueNum=false;
    let minNum=false;
    if(pronum.trim()!==''){
        const number=Number(pronum);
        if(!Number.isInteger(number)) trueNum=true;
        if(number<0) minNum=true;
    }

    if(exLine.length===0){
        alert('생산라인의 생산수량을 저장 후 진행해 주세요.');
    }else if(list.length===0){
         alert('생산라인을 한 개 이상 선택해 주세요.');
    }else if(pronum.trim()===''){
        alert('제품의 총 생산수량을 입력해 주세요.');
    }else if(trueNum || minNum){
        alert('제품의 총 생산수량은 0이상의 정수를 입력해 주세요.');
    }else if(startDate.trim()===''){
        alert('제품생산 시작일을 입력해 주세요.');
    }else if(endDate.trim()===''){
        alert('제품생산 마감일을 입력해 주세요.');
    }else if(startDate>=endDate){
        alert('제품생산 마감일은 시작일보다 나중이어야 합니다.');
    }else{
        formData1.append('proplanNo', proplanNo);
        formData1.append('manuCode', manuCode);
        formData1.append('pronum', pronum);
        formData1.append('startD', startDate);
        formData1.append('endD', endDate);
        formData1.append('details', details);
        formData1.append('lineList', list);
        formData1.append('deleteFile', deleteFile1);

        $.ajax({
            url:'/savePlan',
            method:'POST',
            data: formData1,
                processData: false, // jQuery가 데이터를 처리하지 않도록 설정
                contentType: false, // jQuery가 Content-Type을 설정하지 않도록 설정
            success: function(response) {
                window.location.href = '/proteam/list_pro';
            }
        });
    }
}
// 생산계획 수정 불러오기
function initModifyProPlan(){
    const startDay=document.getElementById('startDay').innerText;
    const endDay=document.getElementById('endDay').innerText;
    const start=startDay.split('T')[0];
    const end=endDay.split('T')[0];
    document.getElementById('startDate').value=start;
    document.getElementById('endDate').value=end;



    const lines=document.getElementById('lines').innerText;
    const line=lines.split(',');
    line.forEach(x=>{
        const lineId='ckeck'+x;
        const tempLine=document.getElementById(lineId);
        if(tempLine) tempLine.checked=true;
    });
}