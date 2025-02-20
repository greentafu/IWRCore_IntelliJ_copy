// 변수
let page = 1;
let finPage=false;

let selectMaterL = null;
let selectMaterM = null;
let selectMaterS = null;
let materialSearch = null;

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

    const allQuantity=document.querySelectorAll('[id^="quantity"]');
    const longList=[];

    // 선택된 자재번호 목록
    allQuantity.forEach(x=>{
        const num=x.id.substring(8);
        longList.push(num);
    });

    $.ajax({
        url:'/select/getMaterialList',
        method:'GET',
        data: {page:page, longList:longList, selectMaterL:selectMaterL, selectMaterM:selectMaterM, selectMaterS:selectMaterS, materialSearch:materialSearch},
        success:function(data){
            if(data.totalPage<page) finPage=true;

            data.dtoList.forEach(x=>{
                const materCode=x.materCode;
                const name=x.name;
                const Lname=x.materSDTO.materMDTO.materLDTO.lname;
                const Mname=x.materSDTO.materMDTO.mname;
                const Sname=x.materSDTO.sname;
                const standard=x.standard;
                const unit=x.unit;
                const color=x.color;
                const boxName=x.boxDTO.boxname;

                const newRow = document.createElement('tr');

                const inputTd = document.createElement('td');
                const input = document.createElement('input');
                input.type = 'checkbox';
                input.id = 'checkbox'+materCode;
                input.class='form-check-input';
                inputTd.appendChild(input);
                newRow.appendChild(inputTd);

                [materCode, name].forEach(function(text) {
                    const td = document.createElement('td');
                    td.innerText = text;
                    newRow.appendChild(td);
                });
                [Lname, Mname, Sname].forEach(function(text) {
                    const td = document.createElement('td');
                    td.innerText = text;
                    td.style.display = 'none';
                    newRow.appendChild(td);
                });
                [standard, unit, color].forEach(function(text) {
                    const td = document.createElement('td');
                    td.innerText = text;
                    newRow.appendChild(td);
                });
                [boxName].forEach(function(text) {
                    const td = document.createElement('td');
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

            [matL, matM, matS, standard, unit, color, box].forEach(text => {
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

            const sno=cells[11].innerText;

            materData.push({code:code, quantity:quantity, sno:sno});
        });

        if((sel==2 || sel==3) && materData.length==0){
            alert('자재를 한 개 이상 선택해 주세요.');
        }else{
            if(quantityBlank==false) {
                alert('자재의 수량을 입력해 주세요.');
            }else{
                formData2.append('manuCode', manuCode);
                formData2.append('person', person);
                formData2.append('productName', productName);
                formData2.append('selectProS', parseFloat(selectProS));
                formData2.append('proColor', proColor);
                formData2.append('proText', proText);
                formData2.append('materQuantityDTOs', JSON.stringify(materData));
                formData2.append('sel', sel);
                formData2.append('deleteFile', deleteFile2);

                $.ajax({
                    url:'/saveProduct',
                    method:'POST',
                    data: formData2,
                          processData: false, // jQuery가 데이터를 처리하지 않도록 설정
                          contentType: false, // jQuery가 Content-Type을 설정하지 않도록 설정
                    success: function(response) {
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
        url:'/select/getProductMaterial',
        method:'GET',
        data:{manuCode:manuCode},
        success:function(data){
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

                [matL, matM, matS, standard, unit, color, box].forEach(text => {
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
        formData1.append('deleteFile', deleteFile1);

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
                    $('#exLargeModal').modal('hide');
                    document.getElementById('materialName').value='';
                    document.getElementById('uploadFiles1').value='';
                    showMaterLCode=null;
                    showMaterMCode=null;
                    showMaterSCode=null;
                    document.getElementById('standard').value='';
                    document.getElementById('selUnit').value='';
                    document.getElementById('selColor').value='';
                    document.getElementById('inputUnit').value='';
                    document.getElementById('inputColor').value='';
                    document.getElementById('fileTable1').innerText='';
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
                    document.getElementById('uploadFiles1').value='';
                    document.getElementById('selectMaterL').value='';
                    document.getElementById('selectMaterM').value='';
                    document.getElementById('selectMaterS').value='';
                    document.getElementById('standard').value='';
                    document.getElementById('selUnit').value='';
                    document.getElementById('selColor').value='';
                    document.getElementById('inputUnit').value='';
                    document.getElementById('inputColor').value='';
                    document.getElementById('fileTable1').innerText='';
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