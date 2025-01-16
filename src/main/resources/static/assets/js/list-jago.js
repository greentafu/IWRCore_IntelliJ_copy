// 변수
let page = 1;
let totalPage = 1;

let finPage=false;
let selectProL = null;
let selectProM = null;
let selectProS = null;
let productSearch = null;
let selectMaterL = null;
let selectMaterM = null;
let selectMaterS = null;
let materialSearch = null;

let stockStatus = null;
let orderStatus = null;
let selectedBox = null;

function productSearchBtn(type){
    page = 1;
    finPage=false;
    document.getElementById("firstTbody").innerText='';
    selectProL = (document.getElementById("selectProL").value!=null)?document.getElementById("selectProL").value:null;
    selectProM = (document.getElementById("selectProM").value!=null)?document.getElementById("selectProM").value:null;
     selectProS = (document.getElementById("selectProS").value!=null)?document.getElementById("selectProS").value:null;
    productSearch = (document.getElementById("productSearch").value!=null)?document.getElementById("productSearch").value:null;
    loadList(type);
}
function materialSearchBtn(type){
    page = 1;
    finPage=false;
    document.getElementById("firstTbody").innerText='';
    selectMaterL = (document.getElementById("selectMaterL").value!=null)?document.getElementById("selectMaterL").value:null;
    selectMaterM = (document.getElementById("selectMaterM").value!=null)?document.getElementById("selectMaterM").value:null;
    selectMaterS = (document.getElementById("selectMaterS").value!=null)?document.getElementById("selectMaterS").value:null;
    materialSearch = (document.getElementById("materialSearch").value!=null)?document.getElementById("materialSearch").value:null;
    loadList(type);
}

document.getElementById('stockStatus').addEventListener("change", function(){
    stockStatus=document.getElementById('stockStatus').value;
    page = 1;
    finPage=false;
    document.getElementById("firstTbody").innerText='';
    loadList('stock');
});
document.getElementById('orderStatus').addEventListener("change", function(){
    orderStatus=document.getElementById('orderStatus').value;
    page = 1;
    finPage=false;
    document.getElementById("firstTbody").innerText='';
    loadList('stock');
});
document.getElementById('selectedBox').addEventListener("change", function(){
    selectedBox=document.getElementById('selectedBox').value;
    page = 1;
    finPage=false;
    document.getElementById("firstTbody").innerText='';
    loadList('stock');
});

function loadList(type){
    if(type==='stock') stockList();
    if(type==='money') moneyList();
}

function stockList(){
    const content1 = document.getElementById("content1");
    const firstTbody = document.getElementById("firstTbody");
    $.ajax({
        url:'/list/getStockList',
        method:'POST',
        data:{page:page, stockStatus:stockStatus, orderStatus:orderStatus, selectedBox:selectedBox,
              selectProL:selectProL, selectProM:selectProM, selectProS:selectProS, productSearch:productSearch,
              selectMaterL:selectMaterL, selectMaterM:selectMaterM, selectMaterS:selectMaterS, materialSearch:materialSearch},
        success:function(data){
            totalPage=data.totalPage;
            if(data.totalPage<page) finPage=true;
            data.dtoList.forEach(x=>{
                const materCode=x.materialDTO.materCode;
                const materialName=x.materialDTO.name;
                const box=x.materialDTO.boxDTO.boxname;
                const materS=x.materialDTO.materSDTO.sname;
                const materM=x.materialDTO.materSDTO.materMDTO.mname;
                const materL=x.materialDTO.materSDTO.materMDTO.materLDTO.lname;
                const category=materL+">"+materM+">"+materS;
                const shipNum=x.shipNum;
                const reqNum=x.reqNum;
                const stockNum=shipNum-reqNum;
                const orderNum=x.orderNum;

                const newRow = document.createElement("tr");

                const codeTd = document.createElement("td");
                codeTd.innerHTML = `<i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${materCode}</strong>`;
                newRow.appendChild(codeTd);

                const nameTd = document.createElement("td");
                nameTd.innerHTML = `<a href="/jago/stock?materCode=${materCode}"> ${materialName}</a>`;
                newRow.appendChild(nameTd);

                [category, stockNum].forEach(text => {
                    const item = document.createElement("td");
                    item.textContent = text;
                    newRow.appendChild(item);
                });

                const orderTd = document.createElement("td");
                if(orderNum!==0) orderTd.textContent = '발주 진행';
                newRow.appendChild(orderTd);

                const boxTd = document.createElement("td");
                boxTd.textContent = box;
                newRow.appendChild(boxTd);

                firstTbody.appendChild(newRow);
            });
        }
    });
    page++;
}

function moneyList(){
const content1 = document.getElementById("content1");
    const firstTbody = document.getElementById("firstTbody");
    $.ajax({
        url:'/list/getStockList',
        method:'POST',
        data:{page:page, stockStatus:stockStatus, orderStatus:orderStatus, selectedBox:selectedBox,
              selectProL:selectProL, selectProM:selectProM, selectProS:selectProS, productSearch:productSearch,
              selectMaterL:selectMaterL, selectMaterM:selectMaterM, selectMaterS:selectMaterS, materialSearch:materialSearch},
        success:function(data){
            totalPage=data.totalPage;
            if(data.totalPage<page) finPage=true;
            data.dtoList.forEach(x=>{
                const materCode=x.materialDTO.materCode;
                const materialName=x.materialDTO.name;
                const materS=x.materialDTO.materSDTO.sname;
                const materM=x.materialDTO.materSDTO.materMDTO.mname;
                const materL=x.materialDTO.materSDTO.materMDTO.materLDTO.lname;
                const category=materL+">"+materM+">"+materS;
                let money=0;
                const contractDTO=x.contractDTO;
                if(contractDTO) money=contractDTO.money;
                const shipNum=x.shipNum;
                const reqNum=x.reqNum;
                const stockNum=shipNum-reqNum;
                const moneyNum=money*stockNum;

                const newRow = document.createElement("tr");

                const codeTd = document.createElement("td");
                codeTd.innerHTML = `<i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${materCode}</strong>`;
                newRow.appendChild(codeTd);

                const nameTd = document.createElement("td");
                nameTd.innerHTML = `<a href="/jago/stock?materCode=${materCode}"> ${materialName}</a>`;
                newRow.appendChild(nameTd);

                [category, money, stockNum, moneyNum].forEach(text => {
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