// 변수
let page = 1;
let finPage=false;

let department = null;
let role = null;
let memberSearch = null;

let selectPartL = null;
let selectPartM = null;
let selectPartS = null;
let partnerSearch = null;

let checkID=false;
let checkReg=false;

// 검색
function searchBtn(type){
    page = 1;
    finPage=false;
    document.getElementById("firstTbody").innerText='';
    if(type==='member'){
        department = (document.getElementById("department").value!=null)?document.getElementById("department").value:null;
        role = (document.getElementById("role").value!=null)?document.getElementById("role").value:null;
        memberSearch = (document.getElementById("memberSearch").value!=null)?document.getElementById("memberSearch").value:null;
    }else if(type==='partner'){
        selectPartL = (document.getElementById("selectPartL").value!=null)?document.getElementById("selectPartL").value:null;
        selectPartM = (document.getElementById("selectPartM").value!=null)?document.getElementById("selectPartM").value:null;
        selectPartS = (document.getElementById("selectPartS").value!=null)?document.getElementById("selectPartS").value:null;
        partnerSearch = (document.getElementById("partnerSearch").value!=null)?document.getElementById("partnerSearch").value:null;
    }
    loadItems(type);
}
// 목록
function loadItems(type){
    if(type==="member") getMemberList();
    if(type==="partner") getPartnerList();
    if(type==="adminPartner") getPartnerList2();
    if(type==="memberContent") getMemberContent();
}
function getMemberList(){
    $.ajax({
        url:'/list/getMemberPage',
        method:'GET',
        data:{page:page, department:department, role:role, memberSearch:memberSearch},
        success:function(data){
            if(data.totalPage<page) finPage=true;
            data.dtoList.forEach(x=>{
                const tempMno=x.mno;
                const tempName=x.name;
                const tempDepartment=x.department;
                const tempPhone=x.phonenumber;
                const tempRole=x.roleSet;

                const newRow = document.createElement("tr");

                const mnoTd = document.createElement("td");
                mnoTd.textContent = tempMno;
                mnoTd.id = 'mno'+tempMno;
                newRow.appendChild(mnoTd);

                const nameTd = document.createElement("td");
                nameTd.innerHTML=`<a href="/manager/member?mno=${tempMno}">${tempName}</a>`;
                nameTd.id = 'name'+tempMno;
                newRow.appendChild(nameTd);

                const depTd = document.createElement("td");
                depTd.textContent = tempDepartment;
                depTd.id = 'department'+tempMno;
                newRow.appendChild(depTd);

                [tempRole, tempPhone].forEach(text => {
                    const item = document.createElement("td");
                    item.textContent = text;
                    newRow.appendChild(item);
                });

                const btnTd = document.createElement("td");
                if(tempMno===1 || tempMno==='1'){
                    btnTd.innerHTML=`
                        <div class="dropdown">
                            <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown"><i class="bx bx-dots-vertical-rounded"></i></button>
                            <div class="dropdown-menu">
                                <a class="dropdown-item" href="/manager/modify_member?mno=${tempMno}"><i class="bx bx-edit-alt me-1"></i> 수정</a>
                            </div>
                        </div>
                    `;
                }else{
                    btnTd.innerHTML=`
                        <div class="dropdown">
                            <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown"><i class="bx bx-dots-vertical-rounded"></i></button>
                            <div class="dropdown-menu">
                                <a class="dropdown-item" href="/manager/modify_member?mno=${tempMno}"><i class="bx bx-edit-alt me-1"></i> 수정</a>
                                <a data-bs-toggle="modal" href="#modalToggle" class="dropdown-item" onclick="setDeleteMember(${tempMno})"><i class="bx bx-trash me-1" ></i> 삭제</a>
                            </div>
                        </div>
                    `;
                }
                newRow.appendChild(btnTd);

                firstTbody.appendChild(newRow);
            });
        }
    });
    page++;
}
function getPartnerList(){
    $.ajax({
        url:'/list/getPartnerPage',
        method:'GET',
        data:{page:page, selectPartL:selectPartL, selectPartM:selectPartM, selectPartS:selectPartS, partnerSearch:partnerSearch},
        success:function(data){
            if(data.totalPage<page) finPage=true;
            data.dtoList.forEach(x=>{
                const tempPno=x.pno;
                const tempName=x.name;
                const tempReg=x.registrationNumber;
                const tempSector=x.sector;
                const tempType=x.type;
                const st=tempSector+'/'+tempType;
                const tempTel=x.telNumber;

                const newRow = document.createElement("tr");

                const pnoTd = document.createElement("td");
                pnoTd.textContent = tempPno;
                pnoTd.id = 'pno'+tempPno;
                newRow.appendChild(pnoTd);

                const nameTd = document.createElement("td");
                nameTd.innerHTML=`<a href="/manager/partner?pno=${tempPno}">${tempName}</a>`;
                nameTd.id = 'name'+tempPno;
                newRow.appendChild(nameTd);

                const regTd = document.createElement("td");
                regTd.textContent = tempReg;
                regTd.id = 'registerNumber'+tempPno;
                newRow.appendChild(regTd);

                [st, tempTel].forEach(text => {
                    const item = document.createElement("td");
                    item.textContent = text;
                    newRow.appendChild(item);
                });

                const btnTd = document.createElement("td");
                if(tempPno===1 || tempPno==='1'){
                    btnTd.innerHTML=`
                        <div class="dropdown">
                            <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown"><i class="bx bx-dots-vertical-rounded"></i></button>
                            <div class="dropdown-menu">
                                <a class="dropdown-item" href="/manager/modify_partner?pno=${tempPno}"><i class="bx bx-edit-alt me-1"></i> 수정</a>
                            </div>
                        </div>
                    `;
                }else{
                    btnTd.innerHTML=`
                        <div class="dropdown">
                            <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown"><i class="bx bx-dots-vertical-rounded"></i></button>
                            <div class="dropdown-menu">
                                <a class="dropdown-item" href="/manager/modify_partner?pno=${tempPno}"><i class="bx bx-edit-alt me-1"></i> 수정</a>
                                <a data-bs-toggle="modal" href="#modalToggle" class="dropdown-item" onclick="setDeletePartner(${tempPno})"><i class="bx bx-trash me-1" ></i> 삭제</a>
                            </div>
                        </div>
                    `;
                }
                newRow.appendChild(btnTd);

                firstTbody.appendChild(newRow);
            });
        }
    });
    page++;
}
function getPartnerList2(){
    $.ajax({
        url:'/list/getPartnerPage',
        method:'GET',
        data:{page:page, selectPartL:selectPartL, selectPartM:selectPartM, selectPartS:selectPartS, partnerSearch:partnerSearch},
        success:function(data){
            if(data.totalPage<page) finPage=true;
            data.dtoList.forEach(x=>{
                const tempPno=x.pno;
                const tempName=x.name;
                const tempReg=x.registrationNumber;
                const tempSector=x.sector;
                const tempType=x.type;
                const st=tempSector+'/'+tempType;
                const tempTel=x.telNumber;

                const newRow = document.createElement("tr");

                const pnoTd = document.createElement("td");
                pnoTd.textContent = tempPno;
                pnoTd.id = 'pno'+tempPno;
                newRow.appendChild(pnoTd);

                const nameTd = document.createElement("td");
                nameTd.innerHTML=`<a href="/adminpartner/view_partner?pno=${tempPno}">${tempName}</a>`;
                nameTd.id = 'name'+tempPno;
                newRow.appendChild(nameTd);

                const regTd = document.createElement("td");
                regTd.textContent = tempReg;
                regTd.id = 'registerNumber'+tempPno;
                newRow.appendChild(regTd);

                [st, tempTel].forEach(text => {
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

// 저장
function setSaveMember(){
    const name=document.getElementById('name').value;
    const phonenumber=document.getElementById('phonenumber').value;
    if(checkID==true){
        alert('존재하는 아이디입니다. 아이디를 변경해 주세요.');
    }else if(name.trim()===''){
        alert('이름을 입력해 주세요.');
    }else if(phonenumber.trim()===''){
        alert('핸드폰 번호를 입력해 주세요.');
    }else if(phonenumber.trim()!=''){
        const parts=phonenumber.split('-');
        const size = parts.length;
        if(size!=3 || parts[0].length!=3 || parts[1].length!=4 || parts[2].length!=4){
            alert('핸드폰 번호를 지정된 형식으로 입력해 주세요.');
        }else if(isNaN(parts[0]) || isNaN(parts[1]) || isNaN(parts[2])){
            alert('핸드폰 번호에 숫자만 입력해 주세요.');
        }else{
            saveMember();
        }
    }
}
function saveMember(){
    const mno=document.getElementById('mno').value;
    const name=document.getElementById('name').value;
    const phonenumber=document.getElementById('phonenumber').value;
    const department=document.getElementById('department').value;
    const role=document.getElementById('role').value;
    const id=document.getElementById('id').value;
    const pw=document.getElementById('pw').value;

    $.ajax({
        url:'/saveMember',
        method:'POST',
        data:{mno:mno, name:name, phonenumber:phonenumber, department:department, role:role, id:id, pw:pw},
        success:function(data){
            window.location.href = '/manager/list_member';
        }
    });
}

function setSavePartner(){
    const name=document.getElementById('name').value;
    const registerNum=document.getElementById('registerNum').value;
    const ceo=document.getElementById('ceo').value;
    const location=document.getElementById('location').value;
    const type=document.getElementById('type').value;
    const sector=document.getElementById('sector').value;
    const phonenumber=document.getElementById('phonenumber').value;
    const faxnumber=document.getElementById('faxnumber').value;
    const email=document.getElementById('email').value;
    const selectPartS=document.getElementById('selectPartS').value;
    const cname=document.getElementById('cname').value;
    const cnumber=document.getElementById('cnumber').value;
    const cmail=document.getElementById('cmail').value;

    if(checkID==true){
        alert('존재하는 아이디입니다. 아이디를 변경해 주세요.');
    }else if(checkReg==true){
        alert('존재하는 사업자등록번호입니다. 사업자등록번호를 변경해 주세요.');
    }else if(name.trim()==='' || registerNum.trim()==='' || ceo.trim()==='' || location.trim()==='' || type.trim()==='' ||
        sector.trim()==='' || phonenumber.trim()==='' || faxnumber.trim()==='' || email.trim()===''){
        alert('협력업체 정보를 모두 입력해 주세요.');
    }else if(selectPartS.trim()===''){
        alert('소분류 카테고리를 선택해 주세요.');
    }else if(cname.trim()==='' || cnumber.trim()==='' || cmail.trim()===''){
        alert('담당자 정보를 모두 입력해 주세요.');
    }else if(phonenumber.trim()!=''){
        const parts=phonenumber.split('-');
        const size = parts.length;
        if(size!=3 || parts[0].length!=3 || parts[1].length!=3 || parts[2].length!=4){
            alert('전화번호를 지정된 형식으로 입력해 주세요.');
        }else if(isNaN(parts[0]) || isNaN(parts[1]) || isNaN(parts[2])){
            alert('전화번호에 숫자만 입력해 주세요.');
        }else{
            if(registerNum.trim()!=''){
                const parts=registerNum.split('-');
                const size = parts.length;
                if(size!=3 || parts[0].length!=3 || parts[1].length!=2 || parts[2].length!=5){
                    alert('사업자등록 번호를 지정된 형식으로 입력해 주세요.');
                }else if(isNaN(parts[0]) || isNaN(parts[1]) || isNaN(parts[2])){
                    alert('사업자등록 번호에 숫자만 입력해 주세요.');
                }else{
                    if(cnumber.trim()!=''){
                        const parts=cnumber.split('-');
                        const size = parts.length;
                        if(size!=3 || parts[0].length!=3 || parts[1].length!=4 || parts[2].length!=4){
                            alert('담당자 핸드폰 번호를 지정된 형식으로 입력해 주세요.');
                        }else if(isNaN(parts[0]) || isNaN(parts[1]) || isNaN(parts[2])){
                            alert('담당자 핸드폰 번호에 숫자만 입력해 주세요.');
                        }else{
                            savePartner();
                        }
                    }
                }
            }
        }
    }
}
function savePartner(){
    const pno=document.getElementById('pno').value;
    const name=document.getElementById('name').value;
    const registerNum=document.getElementById('registerNum').value;
    const ceo=document.getElementById('ceo').value;
    const location=document.getElementById('location').value;
    const type=document.getElementById('type').value;
    const sector=document.getElementById('sector').value;
    const phonenumber=document.getElementById('phonenumber').value;
    const faxnumber=document.getElementById('faxnumber').value;
    const email=document.getElementById('email').value;
    const selectPartS=document.getElementById('selectPartS').value;
    const cname=document.getElementById('cname').value;
    const cnumber=document.getElementById('cnumber').value;
    const cmail=document.getElementById('cmail').value;
    const id=document.getElementById('id').value;
    const pw=document.getElementById('pw').value;

    $.ajax({
        url:'/savePartner',
        method:'POST',
        data:{pno:pno, name:name, registerNum:registerNum, ceo:ceo, location:location, type:type, sector:sector,
            phonenumber:phonenumber, faxnumber:faxnumber, email:email, selectPartS:selectPartS, cname:cname, cnumber:cnumber,
            cmail:cmail, id:id, pw:pw},
        success:function(data){
            window.location.href = '/manager/list_partner';
        }
    });
}


// 삭제
function setDeleteMember(mno){
    let tempMno=null;
    let tempDep=null;
    let tempName=null;

    const ex=document.getElementById('mno');
    if(ex){
        tempMno=document.getElementById('mno').value;
        tempDep=document.getElementById('department').value;
        tempName=document.getElementById('name').value;
    }else{
        tempMno=mno;
        tempDep=document.getElementById('department'+mno).textContent;
        tempName=document.getElementById('name'+mno).textContent;
    }

    document.getElementById('modalDeleteText').innerHTML=tempDep+" "+tempName+"("+tempMno+")를 삭제합니다<br/>삭제시 복구가 불가능합니다";
    document.getElementById('modalDeleteBtn').value=tempMno;
}
function deleteMember(button){
    const m_mno=button.value;
    $.ajax({
        url:'/deleteMember',
        method:'POST',
        data:{mno:m_mno},
        success:function(){
            window.location.href = '/manager/list_member';
        }
    });
}

function setDeletePartner(pno){
    let tempPno=null;
    let tempReg=null;
    let tempName=null;

    const ex=document.getElementById('pno');
    if(ex){
        tempPno=document.getElementById('pno').value;
        tempReg=document.getElementById('registerNum').value;
        tempName=document.getElementById('name').value;
    }else{
        tempPno=pno;
        tempReg=document.getElementById('registerNumber'+pno).textContent;
        tempName=document.getElementById('name'+pno).textContent;
    }

    document.getElementById('modalDeleteText').innerHTML=tempReg+" "+tempName+"("+tempPno+")를 삭제합니다<br/>삭제시 복구가 불가능합니다";
    document.getElementById('modalDeleteBtn').value=tempPno;
}
function deletePartner(button){
    const p_pno=button.value;
    $.ajax({
        url:'/deletePartner',
        method:'POST',
        data:{pno:p_pno},
        success:function(){
            window.location.href = '/manager/list_partner';
        }
    });
}