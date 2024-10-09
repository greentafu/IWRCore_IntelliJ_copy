// 변수
let page = 1;
let finPage=false;

let selectMaterL = null;
let selectMaterM = null;
let selectMaterS = null;
let materialSearch = null;

let formData1 = new FormData();

// 자재 목록 검색
function materialSearchBtn(){
    selectMaterL = (document.getElementById("selectMaterL2").value!=null)?document.getElementById("selectMaterL2").value:null;
    selectMaterM = (document.getElementById("selectMaterM2").value!=null)?document.getElementById("selectMaterM2").value:null;
    selectMaterS = (document.getElementById("selectMaterS2").value!=null)?document.getElementById("selectMaterS2").value:null;
    materialSearch = (document.getElementById("materialSearch2").value!=null)?document.getElementById("materialSearch2").value:null;
    renewTable();
}

// 자재 목록 로드
function loadItems() {
    const content1 = document.getElementById("content1");
    const firstTbody = document.getElementById("firstTbody");

    var allQuantity=document.querySelectorAll('[id^="quantity"]');
    var longList=[];

    // 선택된 자재번호 목록
    allQuantity.forEach(x=>{
        var num=x.id.substring(8);
        longList.push(num);
    });

    console.log('aaa', page, '/', longList, '/', selectMaterL, '/', selectMaterM, '/', selectMaterS, '/', materialSearch);

    $.ajax({
        url:'/select/materialList',
        method:'GET',
        data: {page:page, longList:longList, selectMaterL:selectMaterL, selectMaterM:selectMaterM, selectMaterS:selectMaterS, materialSearch:materialSearch},
        success:function(data){
            if(data.totalPage<page) finPage=true;

            data.dtoList.forEach(x=>{
                var materCode=x.materCode;
                var name=x.name;
                var Lname=x.materSDTO.materMDTO.materLDTO.lname;
                var Mname=x.materSDTO.materMDTO.mname;
                var Sname=x.materSDTO.sname;
                var standard=x.standard;
                var unit=x.unit;
                var color=x.color;
                var boxName=x.boxDTO.boxname;
                var file=x.file;

                var newRow = document.createElement('tr');

                var inputTd = document.createElement('td');
                var input = document.createElement('input');
                input.type = 'checkbox';
                input.id = 'checkbox'+materCode;
                input.class='form-check-input';
                inputTd.appendChild(input);
                newRow.appendChild(inputTd);

                [materCode, name].forEach(function(text) {
                    var td = document.createElement('td');
                    td.innerText = text;
                    newRow.appendChild(td);
                });
                [Lname, Mname, Sname].forEach(function(text) {
                    var td = document.createElement('td');
                    td.innerText = text;
                    td.style.display = 'none';
                    newRow.appendChild(td);
                });
                [standard, unit, color].forEach(function(text) {
                    var td = document.createElement('td');
                    td.innerText = text;
                    newRow.appendChild(td);
                });
                [boxName, file].forEach(function(text) {
                    var td = document.createElement('td');
                    td.innerText = text;
                    td.style.display = 'none';
                    newRow.appendChild(td);
                });

                firstTbody.appendChild(newRow);
            });
          }
        });

        page++;
    }

// 하단에 자재목록 추가
function addToLowerTable(){
    const materialRows=document.querySelectorAll('#materialList tbody tr');
    const materialSelTable=document.querySelector('#materialSel');

    materialRows.forEach(x=>{
        const checkbox=x.querySelector('input[type="checkbox"]');
        if(checkbox&&checkbox.checked){
            const cells=x.querySelectorAll('td');

            const code=cells[1].innerText;
            const name=cells[2].innerText;
            const matL=cells[3].innerText;
            const matM=cells[4].innerText;
            const matS=cells[5].innerText;
            const standard=cells[6].innerText;
            const unit=cells[7].innerText;
            const color=cells[8].innerText;
            const box=cells[9].innerText;
            const file=cells[10].innerText;

            const newRow = document.createElement('tr');

            [code, name].forEach(text => {
                const td = document.createElement('td');
                td.innerText = text;
                newRow.appendChild(td);
            });

            const inputTd = document.createElement('td');
            const input = document.createElement('input');
            input.type = 'number';
            input.min='0';
            input.style.border = '0px';
            input.style.width = '80px';
            input.id = `quantity${code}`;
            inputTd.appendChild(input);
            newRow.appendChild(inputTd);

            [matL, matM, matS, standard, unit, color, box, file].forEach(text => {
                const td = document.createElement('td');
                td.innerText = text;
                newRow.appendChild(td);
            });

            const deleteTd = document.createElement('td');
            const button = document.createElement('button');
            button.type = 'button';
            button.className = 'btn btn-sm btn-secondary';
            button.innerText = '삭제';
            button.onclick = function() {
                newRow.remove(); // Remove the row when the button is clicked
                renewTable();
            };
            deleteTd.appendChild(button);
            newRow.appendChild(deleteTd);

            const snoTd = document.createElement('td');
            snoTd.innerText = '';
            snoTd.style.display = 'none';
            snoTd.id = `sno${code}`;
            newRow.appendChild(snoTd);

            materialSelTable.appendChild(newRow);
        }
    });

    renewTable();
}

// 자재목록 새로고침
function renewTable(){
    page = 1;
    finPage=false;
    document.getElementById("firstTbody").innerText='';
    loadItems();
}

// 제품 정보 저장
function saveToLowerTable(whatButton){
    const manuCode=$('#productCode').text();
    const person=$('#person').val();
    const productName=$('#productName').val();
    const selectProS=$('#selectProS').val();
    const proColor=$('#inputColor2').val();
    const proFile=$('#proFile').val();
    const proText=$('#proText').val();
    const sel=whatButton;

    const materialSelTable=document.querySelector('#materialSel');
    const materialRows=materialSelTable.querySelectorAll('tr');

    const materData=[];
    let quantityBlank=true;

    if(person.trim()===''){
        alert('제품 담당자의 실명을 입력해 주세요.');
    }else if(productName.trim()===''){
        alert('제품의 이름을 입력해 주세요.');
    }else if(selectProS.trim()===''){
        alert('제품의 소분류 카테고리를 선택해 주세요.');
    }else{
        materialRows.forEach(x=>{
            const cells=x.querySelectorAll('td');

            const code=cells[0].innerText;
            const inputId='quantity'+code;
            const snoId='sno'+code;

            const inputElement=document.querySelector('#'+inputId);
            const quantity=inputElement.value;
            if(quantity==null || quantity=='') quantityBlank=false;

            const sno=cells[12].innerText;

            materData.push({code:code, quantity:quantity, sno:sno});
        });

        if((sel==2 || sel==3) && materData.length==0){
            alert('자재를 한 개 이상 선택해 주세요.');
        }else{
            if(quantityBlank==false) {
                alert('자재의 수량을 입력해 주세요.');
            }else{
                $.ajax({
                    url:'/saveProduct',
                    method:'POST',
                    contentType:'application/json',
                    data: JSON.stringify({
                        manuCode:manuCode,
                        person:person,
                        productName:productName,
                        selectProS:parseFloat(selectProS),
                        proColor:proColor,
                        proFile:proFile,
                        proText:proText,
                        materQuantityDTOs:materData,
                        sel:sel
                    }),
                    success: function(response) {
                        if(sel==2) window.location.href = '/production/list_newProduct';
                        else if(sel==3) window.location.href = '/production/list_manufacture';
                        else window.location.href = '/development/list_dev';
                    },
                    error: function(xhr, status, error) {
                        if(sel==2) window.location.href = '/production/list_newProduct';
                        else if(sel==3) window.location.href = '/production/list_manufacture';
                        else window.location.href = '/development/list_dev';
                    }
                });
            }
        }
    }
}

// 제품 구성 자재 목록 가져오기
function initStructure(){
    const manuCode=$('#productCode').text();
    $.ajax({
        url:'/select/selectedController',
        method:'GET',
        data:{manuCode:manuCode},
        success:function(data){
            console.log(data);
            const materialSelTable=document.querySelector('#materialSel');

            data.forEach(x=>{
                const code=x.materialDTO.materCode;
                const name=x.materialDTO.name;
                const matL=x.materialDTO.materSDTO.materMDTO.materLDTO.lname;
                const matM=x.materialDTO.materSDTO.materMDTO.mname;
                const matS=x.materialDTO.materSDTO.sname;
                const standard=x.materialDTO.standard;
                const unit=x.materialDTO.unit;
                const color=x.materialDTO.color;
                const box=x.materialDTO.boxDTO.boxname;
                const file=x.materialDTO.file;
                const quantity=x.quantity;
                const sno=x.sno;

                const newRow = document.createElement('tr');

                [code, name].forEach(text => {
                    const td = document.createElement('td');
                    td.innerText = text;
                    newRow.appendChild(td);
                });

                const inputTd = document.createElement('td');
                const input = document.createElement('input');
                input.type = 'number';
                input.min='0';
                input.style.border = '0px';
                input.style.width = '80px';
                input.id = `quantity${code}`;
                input.value=quantity;
                inputTd.appendChild(input);
                newRow.appendChild(inputTd);

                [matL, matM, matS, standard, unit, color, box, file].forEach(text => {
                    const td = document.createElement('td');
                    td.innerText = text;
                    newRow.appendChild(td);
                });

                const deleteTd = document.createElement('td');
                const button = document.createElement('button');
                button.type = 'button';
                button.className = 'btn btn-sm btn-secondary';
                button.innerText = '삭제';
                button.onclick = function() {
                    newRow.remove(); // Remove the row when the button is clicked
                    renewTable();
                };
                deleteTd.appendChild(button);
                newRow.appendChild(deleteTd);

                const snoTd = document.createElement('td');
                snoTd.innerText = sno;
                snoTd.style.display = 'none';
                snoTd.id = `sno${code}`;
                newRow.appendChild(snoTd);

                materialSelTable.appendChild(newRow);
            });
        }
    });

}

// 파일추가1
document.getElementById('uploadBtn1').addEventListener('click', function() {
    var files=document.getElementById('uploadFiles1').files;

    for(var i=0; i<files.length; i++){
        let file=files[i];
        let originalName = file.name;
        let newName = originalName;
        let counter = 1;

        while (formData1.has('files') && Array.from(formData1.values()).some(f => f.name === newName)) {
            newName = originalName.replace(/(\.[^/.]+)$/, ` (${counter})$1`);
            counter++;
        }

        formData1.append('files', file, newName);
    }
    showFileList1();
    document.getElementById('uploadFiles1').value='';

    for (var key of formData1.keys()) {
        console.log(key);
    }
    for (var value of formData1.values()) {
        console.log(value);
    }
});
function showFileList1(){
    var fileTable1=document.getElementById('fileTable1');
    fileTable1.innerText='';
    let index=0;

    for (var value of formData1.values()) {
        const newRow = document.createElement('tr');

        const fileNameTd = document.createElement('td');
        const file = value instanceof File ? value : null;
        fileNameTd.innerText = file ? file.name+'         ' : '파일 아님         ';

        const button = document.createElement('button');
        button.type = 'button';
        button.className = 'btn btn-sm btn-secondary';
        button.innerText = 'x';
        button.onclick = (function(fileIndex) {
            return function() {
                const filesArray = Array.from(formData1.entries());
                filesArray.splice(fileIndex, 1);

                formData1 = new FormData();
                filesArray.forEach(([key, file]) => {
                    formData1.append(key, file);
                });
                showFileList1();
            };
        })(index);
        fileNameTd.appendChild(button);
        newRow.appendChild(fileNameTd);

        fileTable1.appendChild(newRow);
        index++;
    }
}

// 신규 자재 추가
function saveMaterial(whatButton){
    const materCode=document.getElementById('materCode').value;
    const name=document.getElementById('materialName').value;
    const materS=document.getElementById('selectMaterS').value;
    const standard=document.getElementById('standard').value;
    const unit=document.getElementById('inputUnit').value;
    const color=document.getElementById('inputColor').value;
    const box=document.getElementById('box').value;
    const sel=whatButton;

    if(name.trim()===''){
        alert('제품명을 입력해 주세요.');
    }else if(materS.trim()===''){
        alert('카테고리를 선택해 주세요');
    }else {
        formData1.append('materCode', materCode);
        formData1.append('name', name);
        formData1.append('materS', materS);
        formData1.append('standard', standard);
        formData1.append('unit', unit);
        formData1.append('color', color);
        formData1.append('box', box);

        $.ajax({
            url:'/saveMaterial',
            method:'POST',
            data: formData1,
                    processData: false, // jQuery가 데이터를 처리하지 않도록 설정
                    contentType: false, // jQuery가 Content-Type을 설정하지 않도록 설정
            success:function(data){
                if(sel==1) window.location.href = '/material/list_material';
                if(sel==2) {
                    renewTable();
                    $('#exLargeModal').modal('hide')
                    document.getElementById('materialName').value='';
                    document.getElementById('uploadFiles').value='';
                    document.getElementById('selectMaterL').value='';
                    document.getElementById('selectMaterM').value='';
                    document.getElementById('selectMaterS').value='';
                    document.getElementById('standard').value='';
                    document.getElementById('selUnit').value='';
                    document.getElementById('selColor').value='';
                    document.getElementById('inputUnit').value='';
                    document.getElementById('inputColor').value='';
                    formData1 = new FormData();

                    initMater1();
                }
            },
            error: function(xhr, status, error) {
                if(sel==1) window.location.href = '/material/list_material';
                if(sel==2) {
                    renewTable();
                    $('#exLargeModal').modal('hide')
                    document.getElementById('materialName').value='';
                    document.getElementById('uploadFiles').value='';
                    document.getElementById('selectMaterL').value='';
                    document.getElementById('selectMaterM').value='';
                    document.getElementById('selectMaterS').value='';
                    document.getElementById('standard').value='';
                    document.getElementById('selUnit').value='';
                    document.getElementById('selColor').value='';
                    document.getElementById('inputUnit').value='';
                    document.getElementById('inputColor').value='';
                    formData1 = new FormData();

                    initMater1();
                }
            }
        });
    }
}

// 폼 제출 방지
$('form').on('submit', function(event) {
    event.preventDefault();
});