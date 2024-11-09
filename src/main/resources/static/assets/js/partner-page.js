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
