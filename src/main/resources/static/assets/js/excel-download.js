let tempNum=1;
const today = new Date();
const formattedDate = today.toISOString().split('T')[0];

const excelDownload1 = document.getElementById('download-btn1');
const excelDownload2 = document.getElementById('download-btn2');
const excelDownload3 = document.getElementById('download-btn3');

document.addEventListener('DOMContentLoaded', ()=>{
    if(excelDownload1){
        excelDownload1.addEventListener('click', function(){
            tempNum=1;
            exportExcel();
        });
    }
    if(excelDownload2){
        excelDownload2.addEventListener('click', function(){
            tempNum=2;
            exportExcel();
        });
    }
    if(excelDownload3){
        excelDownload3.addEventListener('click', function(){
            tempNum=3;
            exportExcel();
        });
    }
});

function exportExcel(){
    const wb = XLSX.utils.book_new();
    const newWorksheet = excelHandler.getWorksheet();
    XLSX.utils.book_append_sheet(wb, newWorksheet, excelHandler.getSheetName());
    const wbout = XLSX.write(wb, {bookType:'xlsx',  type: 'binary'});
    saveAs(new Blob([s2ab(wbout)],{type:"application/octet-stream"}), excelHandler.getExcelFileName());
}

let excelHandler = {
    getExcelFileName : function(){
        if(tempNum===1) return formattedDate+'_자재수량.xlsx';
        else if(tempNum===2) return formattedDate+'_자재금액.xlsx';
        else return formattedDate+'_자재 월별 현황.xlsx';
    },
    getSheetName : function(){
        if(tempNum===1) return '자재수량';
        else if(tempNum===2) return '자재금액';
        else return '자재 월별 현황';
    },
    getExcelData : function(){
        return document.getElementById('data-table');
    },
    getWorksheet : function(){
        const table = this.getExcelData();
        const headerRow = table.querySelector('thead tr');
        let columnNames = ['자재번호', '자재명', '카테고리', '재고', '발주현황', '창고'];
        if(tempNum===1){ columnNames = ['자재번호', '자재명', '카테고리', '재고수량', '발주현황', '창고']; }
        if(tempNum===2){ columnNames = ['자재번호', '자재명', '카테고리', '단가', '재고수량', '재고금액']; }
        if(tempNum===3){ columnNames = ['날짜', '자재번호', '자재명', '규격', '단위', '당 계약가격', '전 계약가격', '전 계약대비', '재고수량', '재고금액']; }
        const headers = headerRow.querySelectorAll('th');
        headers.forEach(function(header, index) {
            if (columnNames[index]) {
                header.textContent = columnNames[index];
            }
        });
        return XLSX.utils.table_to_sheet(table);
    }
}

function s2ab(s) {
    let buf = new ArrayBuffer(s.length);
    let view = new Uint8Array(buf);
    for (let i=0; i<s.length; i++) view[i] = s.charCodeAt(i) & 0xFF;
    return buf;
}