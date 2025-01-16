let btnNum=1;
let requests = [];
let allData = [];

const today = new Date();
const formattedDate = today.toISOString().split('T')[0];

const excelDownload1 = document.getElementById('download-btn1');
const excelDownload2 = document.getElementById('download-btn2');
const excelDownload3 = document.getElementById('download-btn3');

document.addEventListener('DOMContentLoaded', ()=>{
    if(excelDownload1){
        excelDownload1.addEventListener('click', function(){
            btnNum=1;
            requests=[];
            allData=[];
            exportExcel();
        });
    }
    if(excelDownload2){
        excelDownload2.addEventListener('click', function(){
            btnNum=2;
            requests=[];
            allData=[];
            exportExcel();
        });
    }
    if(excelDownload3){
        excelDownload3.addEventListener('click', function(){
            btnNum=3;
            requests=[];
            allData=[];
            exportExcel();
        });
    }
});

function exportExcel(){
    getMergedWorksheet()
        .then(newWorksheet => {
            const wb = XLSX.utils.book_new();
            XLSX.utils.book_append_sheet(wb, newWorksheet, excelHandler.getSheetName());
            const wbout = XLSX.write(wb, { bookType: 'xlsx', type: 'binary' });
            saveAs(new Blob([s2ab(wbout)], { type: "application/octet-stream" }), excelHandler.getExcelFileName());
        })
        .catch(error => {
            console.error('Excel export failed:', error);
        });
}

let excelHandler = {
    getExcelFileName : function(){
        if(btnNum===1) return formattedDate+'_자재수량.xlsx';
        else if(btnNum===2) return formattedDate+'_자재금액.xlsx';
        else return formattedDate+'_자재 월별 현황.xlsx';
    },
    getSheetName : function(){
        if(btnNum===1) return '자재수량';
        else if(btnNum===2) return '자재금액';
        else return '자재 월별 현황';
    },
    getColumName : function(){
        if(btnNum===1) return ['자재번호', '자재명', '카테고리', '재고수량', '발주현황', '창고'];
        else if(btnNum===2) return ['자재번호', '자재명', '카테고리', '단가', '재고수량', '재고금액'];
        else return ['날짜', '자재번호', '자재명', '규격', '단위', '당 계약가격', '전 계약가격', '전 계약대비', '재고수량', '재고금액'];
    }
}

function getMergedWorksheet(){
    allData.push(excelHandler.getColumName());
    if(btnNum===1 || btnNum===2) btnNum12();
    if(btnNum===3) btnNum3();
    return new Promise((resolve, reject) => {
        Promise.all(requests)
            .then(() => {
                const worksheet = XLSX.utils.aoa_to_sheet(allData);
                resolve(worksheet);
            })
            .catch(error => {
                reject(error);
            });
    });
}
function btnNum12(){
    for(let page=1; page<=totalPage; page++) {
        const request=$.ajax({
            url:'/list/getStockList',
            method:'POST',
            data:{page:page, stockStatus:stockStatus, orderStatus:orderStatus, selectedBox:selectedBox,
                  selectProL:selectProL, selectProM:selectProM, selectProS:selectProS, productSearch:productSearch,
                  selectMaterL:selectMaterL, selectMaterM:selectMaterM, selectMaterS:selectMaterS, materialSearch:materialSearch},
            success:function(data){
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
                    let money=0;
                    const contractDTO=x.contractDTO;
                    if(contractDTO) money=contractDTO.money;
                    const moneyNum=money*stockNum;

                    if(btnNum===1){
                        const order=(orderNum!==0)?'발주 진행':'';
                        allData.push([materCode, materialName, category, stockNum, order, box]);
                    }
                    if(btnNum===2){
                        allData.push([materCode, materialName, category, money, stockNum, moneyNum]);
                    }
                });
            }
        });
        requests.push(request);
    }
}
function btnNum3(){
    const table=document.getElementById('data-table');
    const rows = table.querySelectorAll('tbody tr');
    rows.forEach(row => {
        allData.push([
            row.children[0].textContent,
            row.children[1].textContent,
            row.children[2].textContent,
            row.children[3].textContent,
            row.children[4].textContent,
            row.children[5].textContent,
            row.children[6].textContent,
            row.children[7].textContent,
            row.children[8].textContent,
            row.children[9].textContent
        ]);
    });
}

function s2ab(s) {
    let buf = new ArrayBuffer(s.length);
    let view = new Uint8Array(buf);
    for (let i=0; i<s.length; i++) view[i] = s.charCodeAt(i) & 0xFF;
    return buf;
}