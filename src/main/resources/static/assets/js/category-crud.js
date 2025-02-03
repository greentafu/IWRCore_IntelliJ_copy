// 변수
let page = 1;
let finPage=false;
let page2 = 1;
let finPage2=false;
let page3 = 1;
let finPage3=false;

function loadCategory(type){
    if(type==='part') readPartCategory();
    if(type==='pro') readProCategory();
    if(type==='mater') readMaterCategory();
}

// 회사 카테고리 보기
function readPartCategory(){
    const radios = document.getElementsByName('showPart');
    let selectedBtn = '';
    for (let i = 0; i < radios.length; i++) {
        if (radios[i].checked) {
            selectedBtn = radios[i].value;
            break;
        }
    }
    showPart(selectedBtn);
}
function showPart(selectedBtn){
    const content1 = document.getElementById("content1");
    const firstTbody = document.getElementById("firstTbody");

    const noCategoryBtn=document.getElementById('noCategoryBtn');

    $.ajax({
        url:'/list/getPagePart',
        method:'GET',
        data:{page:page, code:selectedBtn},
        success:function(data){
            if(data.totalPage<page) finPage=true;

            let num=page*10+0;
            data.dtoList.forEach(x=>{
                const LDTO=x.partLDTO;
                const MDTO=x.partMDTO;
                const SDTO=x.partSDTO;

                let codeL=null;
                let nameL=null;
                let codeM=null;
                let nameM=null;
                let codeS=null;
                let nameS=null;

                if(LDTO!=null){
                    codeL=LDTO.partLcode;
                    nameL=LDTO.lname;
                }
                if(MDTO!=null){
                    codeM=MDTO.partMcode;
                    nameM=MDTO.mname;
                }
                if(SDTO!=null){
                    codeS=SDTO.partScode;
                    nameS=SDTO.sname;
                }

                const newRow = document.createElement("tr");

                const partLTd = document.createElement("td");
                partLTd.textContent = nameL;
                partLTd.id = 'partLName'+num;
                partLTd.style.width = "22%";
                newRow.appendChild(partLTd);

                const partMTd = document.createElement("td");
                partMTd.textContent = nameM;
                partMTd.id = 'partMName'+num;
                partMTd.style.width = "22%";
                newRow.appendChild(partMTd);

                const partSTd = document.createElement("td");
                partSTd.textContent = nameS;
                partSTd.id = 'partSName'+num;
                partSTd.style.width = "22%";
                newRow.appendChild(partSTd);

                if(!noCategoryBtn){
                    const btnTd = document.createElement("td");
                    btnTd.innerHTML=`
                        <button class="btn btn-outline-primary btn-sm" data-bs-target="#modalToggle2"
                            data-bs-toggle="modal" type="button"onclick="setModifyCategory(0, ${num}, ${codeL}, ${codeM}, ${codeS})">
                            수정
                        </button>
                        <button class="btn btn-outline-primary btn-sm" data-bs-target="#modalToggle"
                            data-bs-toggle="modal" type="button" onclick="setDeleteCategory(0, ${num}, ${codeL}, ${codeM}, ${codeS})">
                            삭제
                        </button>
                    `;
                    btnTd.style.width = "34%";
                    newRow.appendChild(btnTd);
                }

                firstTbody.appendChild(newRow);
                num++;
            });
        }
    });
    page++;
}
function refreshPartCategory(){
    document.getElementById("firstTbody").innerHTML='';
    page = 1;
    finPage=false;
    loadCategory('part');
}

// 제품 카테고리 보기
function readProCategory(){
    const radios = document.getElementsByName('showPro');
    let selectedBtn = '';
    for (let i = 0; i < radios.length; i++) {
        if (radios[i].checked) {
            selectedBtn = radios[i].value;
            break;
        }
    }
    showPro(selectedBtn);
}
function showPro(selectedBtn){
    const content2 = document.getElementById("content2");
    const secondTbody = document.getElementById("secondTbody");

    const noCategoryBtn=document.getElementById('noCategoryBtn');

    $.ajax({
        url:'/list/getPagePro',
        method:'GET',
        data:{page:page2, code:selectedBtn},
        success:function(data){
            if(data.totalPage<page2) finPage2=true;

            let num=page2*10+0;
            data.dtoList.forEach(x=>{
                const LDTO=x.proLDTO;
                const MDTO=x.proMDTO;
                const SDTO=x.proSDTO;

                let codeL=null;
                let nameL=null;
                let codeM=null;
                let nameM=null;
                let codeS=null;
                let nameS=null;

                if(LDTO!=null){
                    codeL=LDTO.proLcode;
                    nameL=LDTO.lname;
                }
                if(MDTO!=null){
                    codeM=MDTO.proMcode;
                    nameM=MDTO.mname;
                }
                if(SDTO!=null){
                    codeS=SDTO.proScode;
                    nameS=SDTO.sname;
                }

                const newRow = document.createElement("tr");

                const proLTd = document.createElement("td");
                proLTd.textContent = nameL;
                proLTd.id = 'proLName'+num;
                proLTd.style.width = "22%";
                newRow.appendChild(proLTd);

                const proMTd = document.createElement("td");
                proMTd.textContent = nameM;
                proMTd.id = 'proMName'+num;
                proMTd.style.width = "22%";
                newRow.appendChild(proMTd);

                const proSTd = document.createElement("td");
                proSTd.textContent = nameS;
                proSTd.id = 'proSName'+num;
                proSTd.style.width = "22%";
                newRow.appendChild(proSTd);

                if(!noCategoryBtn){
                    const btnTd = document.createElement("td");
                    btnTd.innerHTML=`
                        <button class="btn btn-outline-primary btn-sm" data-bs-target="#modalToggle3"
                            data-bs-toggle="modal" type="button"onclick="setModifyCategory(1, ${num}, ${codeL}, ${codeM}, ${codeS})">
                            수정
                        </button>
                        <button class="btn btn-outline-primary btn-sm" data-bs-target="#modalToggle"
                            data-bs-toggle="modal" type="button" onclick="setDeleteCategory(1, ${num}, ${codeL}, ${codeM}, ${codeS})">
                            삭제
                        </button>
                    `;
                    btnTd.style.width = "34%";
                    newRow.appendChild(btnTd);
                }

                secondTbody.appendChild(newRow);
                num++;
            });
        }
    });
    page2++;
}
function refreshProCategory(){
    document.getElementById("secondTbody").innerHTML='';
    page2 = 1;
    finPage2=false;
    loadCategory('pro');
}

// 자재 카테고리 보기
function readMaterCategory(){
    const radios = document.getElementsByName('showMater');
    let selectedBtn = '';
    for (let i = 0; i < radios.length; i++) {
        if (radios[i].checked) {
            selectedBtn = radios[i].value;
            break;
        }
    }
    showMater(selectedBtn);
}
function showMater(selectedBtn){
    const content3 = document.getElementById("content3");
    const thirdTbody = document.getElementById("thirdTbody");

    const noCategoryBtn=document.getElementById('noCategoryBtn');

    $.ajax({
        url:'/list/getPageMater',
        method:'GET',
        data:{page:page3, code:selectedBtn},
        success:function(data){
            if(data.totalPage<page2) finPage2=true;

            let num=page3*10+0;
            data.dtoList.forEach(x=>{
                const LDTO=x.materLDTO;
                const MDTO=x.materMDTO;
                const SDTO=x.materSDTO;

                let codeL=null;
                let nameL=null;
                let codeM=null;
                let nameM=null;
                let codeS=null;
                let nameS=null;

                if(LDTO!=null){
                    codeL=LDTO.materLcode;
                    nameL=LDTO.lname;
                }
                if(MDTO!=null){
                    codeM=MDTO.materMcode;
                    nameM=MDTO.mname;
                }
                if(SDTO!=null){
                    codeS=SDTO.materScode;
                    nameS=SDTO.sname;
                }

                const newRow = document.createElement("tr");

                const materLTd = document.createElement("td");
                materLTd.textContent = nameL;
                materLTd.id = 'materLName'+num;
                materLTd.style.width = "22%";
                newRow.appendChild(materLTd);

                const materMTd = document.createElement("td");
                materMTd.textContent = nameM;
                materMTd.id = 'materMName'+num;
                materMTd.style.width = "22%";
                newRow.appendChild(materMTd);

                const materSTd = document.createElement("td");
                materSTd.textContent = nameS;
                materSTd.id = 'materSName'+num;
                materSTd.style.width = "22%";
                newRow.appendChild(materSTd);

                if(!noCategoryBtn){
                    const btnTd = document.createElement("td");
                    btnTd.innerHTML=`
                        <button class="btn btn-outline-primary btn-sm" data-bs-target="#modalToggle4"
                            data-bs-toggle="modal" type="button"onclick="setModifyCategory(2, ${num}, ${codeL}, ${codeM}, ${codeS})">
                            수정
                        </button>
                        <button class="btn btn-outline-primary btn-sm" data-bs-target="#modalToggle"
                            data-bs-toggle="modal" type="button" onclick="setDeleteCategory(2, ${num}, ${codeL}, ${codeM}, ${codeS})">
                            삭제
                        </button>
                    `;
                    btnTd.style.width = "34%";
                    newRow.appendChild(btnTd);
                }

                thirdTbody.appendChild(newRow);
                num++;
            });
        }
    });
    page3++;
}
function refreshMaterCategory(){
    document.getElementById("thirdTbody").innerHTML='';
    page3 = 1;
    finPage3=false;
    loadCategory('mater');
}


// 카테고리 저장
function saveCategory(type){
    let saveType=null;
    let lcode=null;
    let mcode=null;
    let lname=null;
    let mname=null;
    let sname=null;

    if(type==='part') {
        saveType=0;
        lcode=document.getElementById('selectPartL').value;
        mcode=document.getElementById('selectPartM').value;
        lname=document.getElementById('inputPartL').value;
        mname=document.getElementById('inputPartM').value;
        sname=document.getElementById('inputPartS').value;
    }
    if(type==='pro') {
        saveType=1;
        lcode=document.getElementById('selectProL').value;
        mcode=document.getElementById('selectProM').value;
        lname=document.getElementById('inputProL').value;
        mname=document.getElementById('inputProM').value;
        sname=document.getElementById('inputProS').value;
    }
    if(type==='mater') {
        saveType=2;
        lcode=document.getElementById('selectMaterL').value;
        mcode=document.getElementById('selectMaterM').value;
        lname=document.getElementById('inputMaterL').value;
        mname=document.getElementById('inputMaterM').value;
        sname=document.getElementById('inputMaterS').value;
    }

    $.ajax({
        url:'/saveCategory',
        method:'POST',
        data:{type:saveType, lcode:lcode, mcode:mcode, lname:lname, mname:mname, sname:sname},
        success:function(data){
            if(type==='part'){
                document.getElementById('inputPartL').value='';
                document.getElementById('inputPartM').value='';
                document.getElementById('inputPartS').value='';
                showPartLCode=null;
                showPartMCode=null;
                refreshPartCategory();
                initPart1();
            }
            if(type==='pro'){
                document.getElementById('inputProL').value='';
                document.getElementById('inputProM').value='';
                document.getElementById('inputProS').value='';
                showProLCode=null;
                showProMCode=null;
                refreshProCategory();
                initPro1();
            }
            if(type==='mater'){
                document.getElementById('inputMaterL').value='';
                document.getElementById('inputMaterM').value='';
                document.getElementById('inputMaterS').value='';
                showMaterLCode=null;
                showMaterMCode=null;
                refreshMaterCategory();
                initMater1();
            }
        }
    });
}


// 카테고리 수정
function setModifyCategory(type, num, codeL, codeM, codeS){
    let inputS2=null;
    let inputM2=null;
    let selectM2=null;
    let selectL2=null;

    if(type===0){
        inputS2=document.getElementById('inputPartS2');
        inputM2=document.getElementById('inputPartM2');
        selectM2=document.getElementById('selectPartM2');
        selectL2=document.getElementById('selectPartL2');

        if(codeL!=null && codeL!=='') showPartLCode2=codeL;
        if(codeM!=null && codeM!=='') showPartMCode2=codeM;

        if(codeS!=null && codeS!=='') document.getElementById('modifyPart').value='0-2-'+codeS;
        else if(codeM!=null && codeM!=='') document.getElementById('modifyPart').value='0-1-'+codeM;
        else document.getElementById('modifyPart').value='0-0-'+codeL;

        initPart2();
    }else if(type===1){
        inputS2=document.getElementById('inputProS2');
        inputM2=document.getElementById('inputProM2');
        selectM2=document.getElementById('selectProM2');
        selectL2=document.getElementById('selectProL2');

        if(codeL!=null && codeL!='') showProLCode2=codeL;
        if(codeM!=null && codeM!='') showProMCode2=codeM;

        if(codeS!=null && codeS!=='') document.getElementById('modifyPro').value='1-2-'+codeS;
        else if(codeM!=null && codeM!=='') document.getElementById('modifyPro').value='1-1-'+codeM;
        else document.getElementById('modifyPro').value='1-0-'+codeL;

        initPro2();
    }else{
        inputS2=document.getElementById('inputMaterS2');
        inputM2=document.getElementById('inputMaterM2');
        selectM2=document.getElementById('selectMaterM2');
        selectL2=document.getElementById('selectMaterL2');

        if(codeL!=null && codeL!=='') showMaterLCode2=codeL;
        if(codeM!=null && codeM!=='') showMaterMCode2=codeM;

        if(codeS!=null && codeS!=='') document.getElementById('modifyMater').value='2-2-'+codeS;
        else if(codeM!=null && codeM!=='') document.getElementById('modifyMater').value='2-1-'+codeM;
        else document.getElementById('modifyMater').value='2-0-'+codeL;

        initMater2();
    }

    if(codeS!=null && codeS!=='') {
        inputS2.style.display = '';
        inputM2.style.display = '';
        selectM2.style.display = '';
        selectL2.style.display = '';
        if(type===0) inputS2.value=document.getElementById('partSName'+num).innerText;
        else if(type===1) inputS2.value=document.getElementById('proSName'+num).innerText;
        else inputS2.value=document.getElementById('materSName'+num).innerText;
    }else if(codeM!=null && codeM!=='') {
        inputS2.style.display = 'none';
        inputM2.style.display = '';
        selectM2.style.display = 'none';
        selectL2.style.display = '';
    }else{
        inputS2.style.display = 'none';
        inputM2.style.display = 'none';
        selectM2.style.display = 'none';
        selectL2.style.display = 'none';
    }
}
function modifyCategory(button){
    const value=button.value.split('-');
    const category=value[0];
    const type=value[1];
    const code=value[2];

    let lname=null;
    let mname=null;
    let sname=null;
    let lcode=null;
    let mcode=null;

    if(category===0 || category==='0'){
        lname=document.getElementById('inputPartL2').value;
        mname=document.getElementById('inputPartM2').value;
        sname=document.getElementById('inputPartS2').value;

        lcode=document.getElementById('selectPartL2').value;
        mcode=document.getElementById('selectPartM2').value;
    }if(category===1 || category==='1'){
        lname=document.getElementById('inputProL2').value;
        mname=document.getElementById('inputProM2').value;
        sname=document.getElementById('inputProS2').value;

        lcode=document.getElementById('selectProL2').value;
        mcode=document.getElementById('selectProM2').value;
    }else if(category===2 || category==='2'){
        lname=document.getElementById('inputMaterL2').value;
        mname=document.getElementById('inputMaterM2').value;
        sname=document.getElementById('inputMaterS2').value;

        lcode=document.getElementById('selectMaterL2').value;
        mcode=document.getElementById('selectMaterM2').value;
    }

    $.ajax({
        url:'/modifyCategory',
        method:'POST',
        data:{category:category, type:type, code:code,
              lname:lname, mname:mname, sname:sname, lcode:lcode, mcode:mcode},
        success:function(response){
            $('#modalToggle').modal('hide');
            if(category===0 || category==='0') {
                refreshPartCategory();
                refreshPartL();
            }else if(category===1 || category==='1') {
                refreshProCategory();
                refreshProL();
            }else if(category===2 || category==='2') {
                refreshMaterCategory();
                refreshMaterL();
            }
        }
    });
}
// 카테고리 삭제
function setDeleteCategory(type, num, codeL, codeM, codeS){
    if(type===0){
        const Lname=document.getElementById('partLName'+num).innerText;
        const Mname=document.getElementById('partMName'+num).innerText;
        const Sname=document.getElementById('partSName'+num).innerText;

        document.getElementById('deleteText').innerHTML="협력회사 카테고리를 삭제합니다.<br/>["+Lname+"] - ["+Mname+"] - ["+Sname+"]<br/>삭제시 복구가 불가능합니다.";
        if(codeS!=null && codeS!=='') document.getElementById('deleteBtn').value='0-2-'+codeS;
        else if(codeM!=null && codeM!=='') document.getElementById('deleteBtn').value='0-1-'+codeM;
        else document.getElementById('deleteBtn').value='0-0-'+codeL;
    }else if(type===1){
        const Lname=document.getElementById('proLName'+num).innerText;
        const Mname=document.getElementById('proMName'+num).innerText;
        const Sname=document.getElementById('proSName'+num).innerText;

        document.getElementById('deleteText').innerHTML="제품 카테고리를 삭제합니다.<br/>["+Lname+"] - ["+Mname+"] - ["+Sname+"]<br/>삭제시 복구가 불가능합니다.";
        if(codeS!=null && codeS!=='') document.getElementById('deleteBtn').value='1-2-'+codeS;
        else if(codeM!=null && codeM!=='') document.getElementById('deleteBtn').value='1-1-'+codeM;
        else document.getElementById('deleteBtn').value='1-0-'+codeL;
    }else{
        const Lname=document.getElementById('materLName'+num).innerText;
        const Mname=document.getElementById('materMName'+num).innerText;
        const Sname=document.getElementById('materSName'+num).innerText;

        document.getElementById('deleteText').innerHTML="자재 카테고리를 삭제합니다.<br/>["+Lname+"] - ["+Mname+"] - ["+Sname+"]<br/>삭제시 복구가 불가능합니다.";
        if(codeS!=null && codeS!=='') document.getElementById('deleteBtn').value='2-2-'+codeS;
        else if(codeM!=null && codeM!=='') document.getElementById('deleteBtn').value='2-1-'+codeM;
        else document.getElementById('deleteBtn').value='2-0-'+codeL;
    }
}
function deleteCategory(button){
    const value=button.value.split('-');
    const category=value[0];
    const type=value[1];
    const code=value[2];
    $.ajax({
        url:'/deleteCategory',
        method:'POST',
        data:{category:category, type:type, code:code},
        success:function(response){
            $('#modalToggle').modal('hide');
            if(category===0 || category==='0') {
                refreshPartCategory();
                refreshPartL();
            }else if(category===1 || category==='1') {
                refreshProCategory();
                refreshProL();
            }else if(category===2 || category==='2') {
                refreshMaterCategory();
                refreshMaterL();
            }
        }
    });
}