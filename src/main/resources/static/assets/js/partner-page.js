let page = 1;
let finPage=false;
let selectedBox=null;

function initPartnerMain(){
    const allCard=document.querySelectorAll('[id^="card"]');
    allCard.forEach(x=>{
        const no=x.id.substring(4);
        getPartnerCard(no, 'main');
    });
}

function initPartnerProduct(){
    const no=document.getElementById('baljuNo').value;
    getPartnerCard(no, 'product');
}

function getPartnerCard(no, type){
    $.ajax({
        url:'/select/getPartnerProduct',
        method:'POST',
        data:{baljuNo:no},
        success:function(data){
            const returns=data.returns;
            const emergency=data.emergency;
            const baljuList=data.baljuList;
            const materialName=data.baljuDTO.contractDTO.jodalPlanDTO.materialDTO.name;
            const gumsuList=data.gumsuList;
            const totalShipment=data.totalShipment;
            const totalReturn=data.totalReturn;

            materialSpaceFnc(no, materialName, gumsuList);
            btnSpaceFnc(no, returns, emergency);
            btnDateLineFnc(no, baljuList[0].dueDate.split('T')[0], baljuList[1].dueDate.split('T')[0], baljuList[2].dueDate.split('T')[0]);
            btnNumberLineFnc(no, baljuList[0].quantity, baljuList[1].quantity, baljuList[2].quantity);
            btnDataProgressFnc(no, baljuList[0].percent, baljuList[1].percent, baljuList[2].percent, baljuList.length);
            btnQuantityProgressFnc(no, baljuList[0].count, baljuList[1].count, baljuList[2].count, baljuList.length);

            if(type==='main' && gumsuList.length!==0) mainTableContent(no, gumsuList);
            if(type==='product') productTableContent(no, baljuList, gumsuList, totalShipment, totalReturn);
        }
    });
}
function materialSpaceFnc(no, materialName, gumsuList){
    const materialSpace=document.getElementById('materialSpace'+no);
    materialSpace.innerHTML='';
    if(gumsuList.length!==0)
        materialSpace.innerHTML=`<a href="/partner/view_product?baljuNo=${no}"><h5 class="card-header">
                                 <button type="button" class="btn btn-outline-primary btn-lg">${materialName}</button></h5></a>`;
    else materialSpace.innerHTML=`<h5 class="card-header"><button type="button" class="btn btn-outline-secondary btn-lg">
                                  ${materialName}</button></h5>`;
}
function btnSpaceFnc(no, returns, emergency){
    const btnSpace = document.getElementById('btnSpace'+no);
    btnSpace.innerHTML='';
    if(returns!==null)
        btnSpace.innerHTML+=`<div class="col-md-6"><a href="/partner/view_return?reNO=${returns}"><button type="button" class="btn btn-sm btn-primary">
                             <span class="tf-icons bx bx-redo"></span>&nbsp; 반품처리</button></a></div>`;
    else btnSpace.innerHTML+=`<div class="col-md-6"><button type="button" class="btn btn-sm btn-outline-primary">
                               <span class="tf-icons bx bx-redo"></span>&nbsp; 반품처리</button></div>`;

    if(emergency!==null)
        btnSpace.innerHTML+=`<div class="col-md-6"><a href="/partner/list_urgent"><button type="button" class="btn btn-sm btn-primary">
                             <span class="tf-icons bx bx-task"></span>&nbsp; 긴급납품</button></a></div>`;
    else btnSpace.innerHTML+=`<div class="col-md-6"><button type="button" class="btn btn-sm btn-outline-primary">
                               <span class="tf-icons bx bx-task"></span>&nbsp; 긴급납품</button></div>`;
}
function btnDateLineFnc(no, date1, date2, date3){
    const dateLine = document.getElementById('dateLine'+no);
    dateLine.innerHTML=`<span></span>`;
    dateLine.innerHTML+=`<b>${date1}</b>`;
    dateLine.innerHTML+=`<b>${date2}</b>`;
    dateLine.innerHTML+=`<b>${date3}</b>`;
}
function btnNumberLineFnc(no, quantity1, quantity2, quantity3){
    const numberLine = document.getElementById('numberLine'+no);
    numberLine.innerHTML=`<span></span>`;
    numberLine.innerHTML+=`<b>${quantity1}</b>`;
    numberLine.innerHTML+=`<b>${quantity2}</b>`;
    numberLine.innerHTML+=`<b>${quantity3}</b>`;
}
function btnDataProgressFnc(no, percent1, percent2, percent3, len){
    const dateProgress=document.getElementById('dateProgress'+no);
    dateProgress.innerHTML='';
    const tempSize=12/len;
    const size=parseInt(tempSize);

    dateProgress.innerHTML += `<div class="progress col-md-${size}">
                                  <div class="progress-bar bg-info" role="progressbar"
                                       aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" style="width: ${percent1}%"
                                  >${percent1}%</div>
                               </div>`;
    dateProgress.innerHTML += `<div class="progress col-md-${size}">
                                  <div class="progress-bar bg-info" role="progressbar"
                                       aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" style="width: ${percent2}%"
                                  >${percent2}%</div>
                               </div>`;
    dateProgress.innerHTML += `<div class="progress col-md-${size}">
                                  <div class="progress-bar bg-info" role="progressbar"
                                       aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" style="width: ${percent3}%"
                                  >${percent3}%</div>
                               </div>`;
}
function btnQuantityProgressFnc(no, count1, count2, count3, len){
    const numberProgress=document.getElementById('numberProgress'+no);
    numberProgress.innerHTML='';
    const tempSize=12/len;
    const size=parseInt(tempSize);

    numberProgress.innerHTML += `<div class="progress col-md-${size}">
                                     <div class="progress-bar" role="progressbar"
                                         aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" style="width: ${count1}%"
                                     >${count1}%</div>
                                 </div>`;
    numberProgress.innerHTML += `<div class="progress col-md-${size}">
                                     <div class="progress-bar" role="progressbar"
                                         aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" style="width: ${count2}%"
                                     >${count2}%</div>
                                 </div>`;
    numberProgress.innerHTML += `<div class="progress col-md-${size}">
                                     <div class="progress-bar" role="progressbar"
                                         aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" style="width: ${count3}%"
                                     >${count3}%</div>
                                 </div>`;
}
// 메인페이지 테이블 추가
function mainTableContent(no, gumsuList){
    const gumsuTable=document.getElementById('gumsuTable'+no);

    const newRow1 = document.createElement("tr");
    const newTd = document.createElement("th");
    newRow1.appendChild(newTd);
    gumsuList.forEach((item, index)=>{
        const newTd2 = document.createElement("th");
        newTd2.textContent = index+'차';
        newRow1.appendChild(newTd2);
    });
    gumsuTable.appendChild(newRow1);

    const newRow2 = document.createElement("tr");
    const newTd3 = document.createElement("th");
    newTd3.textContent='검수일자';
    newRow2.appendChild(newTd3);
    gumsuList.forEach((item, index)=>{
        const newTd4 = document.createElement("td");
        newTd4.textContent = item.dueDate.split('T')[0];
        newRow2.appendChild(newTd4);
    });
    gumsuTable.appendChild(newRow2);

    const newRow3 = document.createElement("tr");
    const newTd5 = document.createElement("th");
    newTd5.textContent='검수 수량';
    newRow3.appendChild(newTd5);
    gumsuList.forEach((item, index)=>{
        const newTd6 = document.createElement("td");
        newTd6.textContent = item.quantity;
        newRow3.appendChild(newTd6);
    });
    gumsuTable.appendChild(newRow3);
}
// 제품페이지 테이블 추가
function productTableContent(no, baljuList, gumsuList, totalShipment, totalReturn){
    const productTable=document.getElementById('productTable'+no);
    productTable.innerHTML='';

    const totalOrder=baljuList[0].totalOrder;
    const totalGet=gumsuList[0].totalOrder-totalShipment;

    [totalOrder, totalGet, totalShipment, totalReturn].forEach(text => {
        const td = document.createElement('td');
        td.innerText = text;
        productTable.appendChild(td);
    });

    document.getElementById('getNum').value=totalGet;
}

// 생산량 추가
function addQuantity(){
    const baljuNo=document.getElementById('baljuNo').value;
    const quantity=document.getElementById('makeQuantity').value;
    $.ajax({
        url:'/saveMakeQuantity',
        method:'POST',
        data:{baljuNo:baljuNo, quantity:quantity},
        success:function(data){
            initPartnerProduct(baljuNo);
            document.getElementById('makeQuantity').value='';
        }
    });
}
// 출고
function release(){
    const baljuNo=document.getElementById('baljuNo').value;
    const shipNum=document.getElementById('shipNum').value;
    const shipText=document.getElementById('shipText').value;
    $.ajax({
        url:'/saveShipment',
        method:'POST',
        data:{baljuNo:baljuNo, shipNum:shipNum, shipText:shipText},
        success:function(data){
            initPartnerProduct(baljuNo);
            document.getElementById('shipNum').value='';
            document.getElementById('shipText').value='';
        }
    });
}
// 반품 확인
function returnCheck(){
    const reNo=document.getElementById('reNo').value;
    $.ajax({
        url:'/saveReturnCheck',
        method:'POST',
        data:{reNo:reNo},
        success:function(data){
            window.location.href = '/partner/view_return?reNO='+reNo;
        }
    });
}

// 계약서, 발주서, 거래명세서, 반품, 긴급납품 목록 보기
function loadItems(type) {
    const content1 = document.getElementById("content1");
    const firstTbody = document.getElementById("firstTbody");

    if(type==='contract') contractList();
    if(type==='order') orderList();
    if(type==='invoice') invoiceList();
    if(type==='returns') returnList();
    if(type==='urgency') urgencyList();

    page++;
}
// 계약서 목록
function contractList(){
    $.ajax({
        url:'/list/partnerContractList',
        method:'GET',
        data:{page:page},
        success:function(data){
            if(data.totalPage<page) finPage=true;

            data.dtoList.forEach(x=>{
                const conNo=x.conNo;
                const conName=x.jodalPlanDTO.materialDTO.name;
                const conNum=x.conNum;
                const conMoney=x.money;
                const conDate=x.conDate.split('T')[0];

                const newRow = document.createElement("tr");

                [conDate, conName, conNum, conMoney].forEach(text => {
                    const item = document.createElement("td");
                    item.textContent = text;
                    newRow.appendChild(item);
                });

                const btnTd = document.createElement("td");
                btnTd.innerHTML=`<a href="/partner/view_contract?conNo=${conNo}"><button class="btn btn-primary">내용 보기</button></a>`;
                newRow.appendChild(btnTd);

                firstTbody.appendChild(newRow);
            });
        }
    });
}
// 발주서 목록
function orderList(){
    $.ajax({
        url:'/list/partnerOrderList',
        method:'GET',
        data:{page:page},
        success:function(data){
            if(data.totalPage<page) finPage=true;

            data.dtoList.forEach(x=>{
                const baljuNo=x.baljuNo;
                const conName=x.contractDTO.jodalPlanDTO.materialDTO.name;
                const baljuNum=x.baljuNum;
                const baljuDate=x.baljuDate.split('T')[0];
                const baljuWhere=x.baljuWhere;

                const newRow = document.createElement("tr");

                [baljuDate, conName, baljuNum, baljuWhere].forEach(text => {
                    const item = document.createElement("td");
                    item.textContent = text;
                    newRow.appendChild(item);
                });

                const btnTd = document.createElement("td");
                btnTd.innerHTML=`<a href="/partner/view_order?baljuNo=${baljuNo}"><button class="btn btn-primary">내용 보기</button></a>`;
                newRow.appendChild(btnTd);

                firstTbody.appendChild(newRow);
            });
        }
    });
}
// 거래명세서 목록
function invoiceList(){
    $.ajax({
        url:'/list/partnerInvoiceList',
        method:'GET',
        data:{page:page},
        success:function(data){
            if(data.totalPage<page) finPage=true;

            console.log(data);

            data.dtoList.forEach(x=>{
                const tranNO=x.invoiceDTO.tranNO;
                const conName=x.contractDTO.jodalPlanDTO.materialDTO.name;
                const dateCreated=x.invoiceDTO.dateCreated.split('T')[0];
                const text=x.invoiceDTO.text;

                const cash=x.invoiceDTO.cash;
                const cheque=x.invoiceDTO.cheque;
                const promissory=x.invoiceDTO.promissory;
                const receivable=x.invoiceDTO.receivable;
                const sumMoney=cash+cheque+promissory+receivable;

                const newRow = document.createElement("tr");

                [dateCreated, conName, sumMoney, text].forEach(text => {
                    const item = document.createElement("td");
                    item.textContent = text;
                    newRow.appendChild(item);
                });

                const btnTd = document.createElement("td");
                btnTd.innerHTML=`<a href="/partner/view_invoice?tranNO=${tranNO}"><button class="btn btn-primary">내용 보기</button></a>`;
                newRow.appendChild(btnTd);

                firstTbody.appendChild(newRow);
            });
        }
    });
}
// 반품 목록
function returnList(){
    selectedBox = (document.getElementById("selectedBox").value!='')?document.getElementById("selectedBox").value:null;
    $.ajax({
        url:'/list/partnerReturnList',
        method:'GET',
        data:{page:page, selectedBox:selectedBox},
        success:function(data){
            if(data.totalPage<page) finPage=true;
            data.dtoList.forEach(x=>{
                const reNo=x.reNO;
                const shipDate=x.shipmentDTO.regDate.split('T')[0];
                const returnDate=x.regDate.split('T')[0];
                const conName=x.shipmentDTO.baljuDTO.contractDTO.jodalPlanDTO.materialDTO.name;
                const shipNum=x.shipmentDTO.shipNum;
                const returnText=x.whyRe;
                const checkNum=x.returnCheck;

                const newRow = document.createElement("tr");

                [shipDate, returnDate, conName, shipNum, returnText].forEach(text => {
                    const item = document.createElement("td");
                    item.textContent = text;
                    newRow.appendChild(item);
                });

                if(checkNum===0){
                    const btnTd = document.createElement("td");
                    btnTd.innerHTML=`<a href="/partner/view_return?reNO=${reNo}"><button class="btn btn-primary">확인 미완료</button></a>`;
                    newRow.appendChild(btnTd);
                }
                if(checkNum===1){
                    const btnTd = document.createElement("td");
                    btnTd.innerHTML=`<a href="/partner/view_return?reNO=${reNo}"><button class="btn btn-secondary">확인 완료</button></a>`;
                    newRow.appendChild(btnTd);
                }

                firstTbody.appendChild(newRow);
            });
        }
    });
}
// 긴급납품
function urgencyList(){
    selectedBox = (document.getElementById("selectedBox").value!='')?document.getElementById("selectedBox").value:null;
    $.ajax({
        url:'/list/partnerUrgencyList',
        method:'GET',
        data:{page:page, selectedBox:selectedBox},
        success:function(data){
            if(data.totalPage<page) finPage=true;
            data.dtoList.forEach(x=>{
                const emerNo=x.emerNo;
                const emerNum=x.emerNum;
                const emerDate=x.emerDate.split('T')[0];
                const regDate=x.regDate.split('T')[0];
                const materialName=x.baljuDTO.contractDTO.jodalPlanDTO.materialDTO.name;
                const emcheck=x.emcheck;

                const newRow = document.createElement("tr");

                const dateTd = document.createElement("td");
                dateTd.textContent = regDate;
                newRow.appendChild(dateTd);

                const emDateTd = document.createElement("td");
                emDateTd.id = 'emDate'+emerNo;
                emDateTd.textContent = emerDate;
                newRow.appendChild(emDateTd);

                const nameTd = document.createElement("td");
                nameTd.id = 'materialName'+emerNo;
                nameTd.textContent = materialName;
                newRow.appendChild(nameTd);

                const emNumTd = document.createElement("td");
                emNumTd.id = 'emNum'+emerNo;
                emNumTd.textContent = emerNum;
                newRow.appendChild(emNumTd);

                const btnTd = document.createElement("td");
                if(emcheck===0) btnTd.innerHTML=`
                    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#modalToggle"
                        onclick="showUrgency(${emerNo})">
                        확인 미완료
                    </button>`;
                if(emcheck===1) btnTd.innerHTML=`<button class="btn btn-secondary">확인 완료</button>`;
                newRow.appendChild(btnTd);

                firstTbody.appendChild(newRow);
            });
        }
    });
}
function showUrgency(emerNo){
    const emDate=document.getElementById('emDate'+emerNo).textContent
    const materialName=document.getElementById('materialName'+emerNo).textContent
    const emNum=document.getElementById('emNum'+emerNo).textContent

    document.getElementById('checkText').innerHTML=materialName+"(희망일: "+emDate+", 희망수량: "+emNum+")의 납품요청을 완료하시겠습니까?";
    document.getElementById('checkBtn').value=emerNo;
}
function checkUrgency(button){
    const emerNo=button.value;
    $.ajax({
        url:'/saveEmergencyCheck',
        method:'POST',
        data:{emerNo:emerNo},
        success:function(response){
            window.location.reload();
        }
    });
}