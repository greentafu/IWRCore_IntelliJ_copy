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
// 출하요청> 생산계획 선택
function getOneProplan(proplanNo){
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

            document.getElementById('productName').innerText=productName;
            document.getElementById('productNo').innerText=productCode;


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

                [materialName, structureNum].forEach(function(text){
                    const td = document.createElement('td');
                    td.innerText = text;
                    newRow.appendChild(td);
                });

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

                secondTbody.appendChild(newRow);
            });
        }
    });
}
// 출하요청> 자동입력
function autoBtn(){

}