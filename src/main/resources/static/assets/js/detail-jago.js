// 변수
let page = 1;
let finPage=false;

let selectedYear=null;

document.getElementById('selectedYear').addEventListener("change", function(){
    selectedYear=document.getElementById('selectedYear').value;
    page = 1;
    finPage=false;
    document.getElementById("firstTbody").innerText='';
    loadTable();
});

function loadTable(){
    const content1 = document.getElementById("content1");
    const firstTbody = document.getElementById("firstTbody");
    const materCode=document.getElementById('materCode').value;
    $.ajax({
        url:'/select/stockDetail',
        method:'POST',
        data:{page:page, materCode:materCode, selectedYear:selectedYear},
        success:function(data){
            if(data.totalPage<page) finPage=true;
            let tempNum=0;

            if(data){
                data.dtoList.forEach(x=>{
                    const date=x.contractDTO.conDate.split('T')[0];
                    const mcode=x.contractDTO.jodalPlanDTO.materialDTO.materCode;
                    const mname=x.contractDTO.jodalPlanDTO.materialDTO.name;
                    const standard=x.contractDTO.jodalPlanDTO.materialDTO.standard;
                    const unit=x.contractDTO.jodalPlanDTO.materialDTO.unit;
                    const cmoney=x.contractDTO.money;
                    const sumship=x.sumShipNum;
                    const sumreq=x.sumRequestNum;

                    const stock=sumship-sumreq;

                    const newRow = document.createElement("tr");

                    [date, mcode, mname, standard, unit].forEach(text => {
                        const item = document.createElement("td");
                        item.textContent = text;
                        newRow.appendChild(item);
                    });

                    const td0 = document.createElement('td');
                    td0.innerText = cmoney;
                    td0.id='cmoney'+tempNum;
                    newRow.appendChild(td0);

                    const td1 = document.createElement('td');
                    td1.innerText = '';
                    td1.id='pre'+tempNum;
                    newRow.appendChild(td1);

                    const td2 = document.createElement('td');
                    td2.innerText = '';
                    td2.id='minus'+tempNum;
                    newRow.appendChild(td2);

                    [stock, cmoney*stock].forEach(text => {
                        const item = document.createElement("td");
                        item.textContent = text;
                        newRow.appendChild(item);
                    });

                    if(tempNum!=0){
                        const tempC=document.getElementById('cmoney'+(tempNum-1)).innerText;
                        document.getElementById('pre'+(tempNum-1)).innerText=cmoney;
                        document.getElementById('minus'+(tempNum-1)).innerText=tempC-cmoney;
                    }

                    tempNum+=1;
                    firstTbody.appendChild(newRow);
                });
            }
        }
    });
    page++;
}