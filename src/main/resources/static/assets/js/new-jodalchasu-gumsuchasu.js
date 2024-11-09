// 모두 선택
function selectAll(whatButton){
    const sel=whatButton;

    const checkAllBtn = document.getElementById('checkAllBtn');
    const isChecked = checkAllBtn.value === '1';

    const allCheckBox=document.querySelectorAll('[id^="btn"]');
    allCheckBox.forEach(checkbox=> {
        checkbox.checked = !isChecked;
    });
    checkAllBtn.value = isChecked ? '0' : '1';
}

// 자동 버튼
function autoBtn(whatButton){
    const sel=whatButton;
    const startDateInput=document.getElementById('baseDate').value;

    if(sel===2 && startDateInput===''){
        alert('기준 날짜를 입력해 주세요.');
    }else{
        const allTable=document.querySelectorAll('[id^="autoTable"]');
        allTable.forEach(table=>{
            const tbody = table.querySelector('tbody');
            if (tbody) {
                const rows = tbody.querySelectorAll('tr');
                rows.forEach(row=>{
                    const checkElement = row.querySelector('[id^="check"]');
                    if(checkElement){
                        const no=checkElement.id.substring(5);
                        if(sel===1) autoFillNum(no);
                        if(sel===2) autoFillDate(no);
                        if(sel===3) fillLocation(no);
                        if(sel===4) autoFillBaljuNum(no);
                        if(sel===5) autoFillGumsuDate(no);
                    }
                });
            }
        });
    }
}
// 수량 자동 넣기
function autoFillNum(number){
    const no=number;
    const checkbox=document.getElementById('btn'+no);
    if(checkbox && checkbox.checked){
        const totalNum=document.getElementById('totalNum'+no).innerText;
        const stockNum=document.getElementById('stockNum'+no).innerText;
        const quantity=totalNum-stockNum;

        const eachQuantity=Math.floor(quantity/3);
        const remainder=quantity%3;
        const thirdQuantity=eachQuantity+remainder;

        document.getElementById('oneNum'+no).value=eachQuantity;
        document.getElementById('twoNum'+no).value=eachQuantity;
        document.getElementById('threeNum'+no).value=thirdQuantity;

        document.getElementById('oneNum'+no).style.color = 'black';
        document.getElementById('twoNum'+no).style.color = 'black';
        document.getElementById('threeNum'+no).style.color = 'black';

        document.getElementById('needNum'+no).innerText = 0;
    }
}
// 날짜 자동 넣기
function autoFillDate(number){
    const no=number;
    const startDateInput=document.getElementById('baseDate').value;
    const checkbox=document.getElementById('btn'+no);
    if(checkbox && checkbox.checked){
        const interDate = parseInt(document.getElementById('days-input').value, 10);

        const realtoday = new Date(startDateInput);

        const today=new Date(realtoday);
        let year = today.getFullYear();
        let month = today.getMonth() + 1;
        let day = today.getDate();
        let formattedDate = year + '-' + (month < 10 ? '0' + month : month) + '-' + (day < 10 ? '0' + day : day);
        document.getElementById('oneDate' + no).value = formattedDate;

        const futureDate1 = new Date(today);
        futureDate1.setDate(today.getDate() + interDate);
        year = futureDate1.getFullYear();
        month = futureDate1.getMonth() + 1;
        day = futureDate1.getDate();
        formattedDate = year + '-' + (month < 10 ? '0' + month : month) + '-' + (day < 10 ? '0' + day : day);
        document.getElementById('twoDate' + no).value = formattedDate;

        const futureDate2 = new Date(today);
        futureDate2.setDate(today.getDate() + interDate * 2);
        year = futureDate2.getFullYear();
        month = futureDate2.getMonth() + 1;
        day = futureDate2.getDate();
        formattedDate = year + '-' + (month < 10 ? '0' + month : month) + '-' + (day < 10 ? '0' + day : day);
        document.getElementById('threeDate' + no).value = formattedDate;
    }
}

// 조달계획> 조달차수 저장
function saveChasu(){
    const chasuRows=document.querySelectorAll('#contentTbody tr');
    const chasuData=[];

    let trueNum=false;
    let trueDate=false;
    let trueSum=false;

    chasuRows.forEach(x=>{
        const checkbox=x.querySelector('input[type="checkbox"]');
        if(checkbox && checkbox.checked){
            const cells=x.querySelectorAll('td');
            const id=cells[12].innerText;

            const oneNumInput=document.getElementById('oneNum'+id);
            const twoNumInput=document.getElementById('twoNum'+id);
            const threeNumInput=document.getElementById('threeNum'+id);

            const oneNum=Number(oneNumInput.value);
            const twoNum=Number(twoNumInput.value);
            const threeNum=Number(threeNumInput.value);
            const oneDate=document.getElementById('oneDate'+id).value;
            const twoDate=document.getElementById('twoDate'+id).value;
            const threeDate=document.getElementById('threeDate'+id).value;

            const oneColor = getComputedStyle(oneNumInput).color;
            const twoColor = getComputedStyle(twoNumInput).color;
            const threeColor = getComputedStyle(threeNumInput).color;

            if(oneNum<0 || oneNum===null || oneNum=='') trueNum=true;
            if(twoNum<0 || twoNum===null || twoNum=='') trueNum=true;
            if(threeNum<0 || threeNum===null || threeNum=='') trueNum=true;

            if(!Number.isInteger(oneNum) || !Number.isInteger(twoNum) || !Number.isInteger(threeNum)) trueNum=true;

            if(oneDate===null || twoDate===null || threeDate===null) trueDate=true;
            if(oneDate>=twoDate || twoDate>=threeDate) trueDate=true;

            if(oneColor === 'rgb(255, 0, 0)' || oneColor === 'red') trueSum=true;
            if(twoColor === 'rgb(255, 0, 0)' || twoColor === 'red') trueSum=true;
            if(threeColor === 'rgb(255, 0, 0)' || threeColor === 'red') trueSum=true;

            chasuData.push({id:id, oneNum:oneNum, twoNum:twoNum, threeNum:threeNum, oneDate:oneDate, twoDate:twoDate, threeDate:threeDate});
        }
    });
    if(trueNum){
        alert('수량에 0이상의 정수를 입력해 주세요.');
    }else if(trueDate){
        alert('알맞은 예정일을 입력해 주세요.')
    }else if(trueSum){
        alert('조달예정 수량이 부족합니다.');
    }else{
        $.ajax({
            url:'/saveJodalChasu',
            method:'POST',
            contentType:'application/json',
            data: JSON.stringify(chasuData),
            success:function(response) {
                window.location.href = '/jodal/list_jodal';
            },
            error: function(xhr, status, error) {
                window.location.href = '/jodal/list_jodal';
            }
        });
    }
}
// 조달계획> 조달계획 수량 확인
function checkSumJodalNum(whatInput){
    const input=whatInput;
    let no="";
    if(input.id.startsWith('three')) no=input.id.substring(8);
    else no=input.id.substring(6);

    const totalNum=document.getElementById('totalNum'+no).innerText;
    const stockNum=document.getElementById('stockNum'+no).innerText;
    const quantity=totalNum-stockNum;

    const oneNumInput=document.getElementById('oneNum'+no);
    const twoNumInput=document.getElementById('twoNum'+no);
    const threeNumInput=document.getElementById('threeNum'+no);
    const oneNum=Number(oneNumInput.value);
    const twoNum=Number(twoNumInput.value);
    const threeNum=Number(threeNumInput.value);

    if((oneNum+twoNum+threeNum)<quantity) {
        oneNumInput.style.color = 'red';
        twoNumInput.style.color = 'red';
        threeNumInput.style.color = 'red';
    }else{
        oneNumInput.style.color = 'black';
        twoNumInput.style.color = 'black';
        threeNumInput.style.color = 'black';
    }

    const remainder=quantity-(oneNum+twoNum+threeNum);
    document.getElementById('needNum'+no).innerText = remainder;
};



// 검수차수> 협력회사 목록 가져오기
function getGumsuPartner() {
    const content1 = document.getElementById("content1");
    const firstTbody = document.getElementById("firstTbody");
    $.ajax({
        url:'/select/getGumsuPartner',
        method:'POST',
        data:{page:page, selectPartL:selectPartL, selectPartM:selectPartM, selectPartS:selectPartS, partnerSearch:partnerSearch},
        success:function(data){
            if(data.totalPage<page) finPage=true;
            data.dtoList.forEach(x=>{
                const pno=x.pno;
                const name=x.name;
                const number=x.registrationNumber;

                const newRow = document.createElement("tr");

                [name, number].forEach(text => {
                    const item = document.createElement("td");
                    item.textContent = text;
                    newRow.appendChild(item);
                });

                const btnTd = document.createElement("td");
                btnTd.innerHTML=`<button class="btn btn-sm btn-outline-primary" onclick="getOnePartner(${pno}, 'gumsu'); getNonGumsuChasuBalju(${pno});">선택</button>`;
                newRow.appendChild(btnTd);

                firstTbody.appendChild(newRow);
            });
        }
    });
    page++;
}
// 검수차수> 검수차수 입력할 목록 가져오기
function getNonGumsuChasuBalju(pno){
    $.ajax({
        url:'/select/getNonGumsuChasu',
        method:'POST',
        data:{pno:pno},
        success:function(data){
            const setTable=document.getElementById('setTable');
            setTable.innerHTML='';
            if(data!=null){
                data.forEach(x=>{
                    const baljuNo=x.baljuDTO.baljuNo;
                    const materialName=x.baljuDTO.contractDTO.jodalPlanDTO.materialDTO.name;
                    const chasuDTOs=x.baljuChasuDTOs;

                    const newTable = document.createElement("table");
                    newTable.classList.add('table', 'table-bordered', 'mb-4');
                    newTable.id = 'autoTable'+baljuNo;

                    const newTbody = document.createElement("tbody");

                    const newRowHead=document.createElement('tr');
                    ['', '자재', '발주수량', '입고예정일', '진척검수 수량', '진척검수 날짜'].forEach(function(text) {
                        const td = document.createElement('td');
                        td.innerText = text;
                        newRowHead.appendChild(td);
                    });
                    newTbody.appendChild(newRowHead);

                    const newRow1 = document.createElement('tr');

                    const checkTd = document.createElement('td');
                    checkTd.id = 'check'+baljuNo;
                    checkTd.rowSpan = 3;
                    checkTd.style.width = '10px';
                    checkTd.className = 'text-center';
                    const checkInput = document.createElement('input');
                    checkInput.type = 'checkbox';
                    checkInput.id = 'btn'+baljuNo;
                    checkInput.className = 'form-check-input';
                    checkTd.appendChild(checkInput);
                    newRow1.appendChild(checkTd);

                    const nameTd = document.createElement('td');
                    nameTd.rowSpan = 3;
                    nameTd.innerText = materialName;
                    newRow1.appendChild(nameTd);

                    const num1Td = document.createElement('td');
                    num1Td.innerText = chasuDTOs[0].balNum;
                    num1Td.id = 'baljuNum1'+baljuNo;
                    newRow1.appendChild(num1Td);

                    const date1Td = document.createElement('td');
                    date1Td.innerText = chasuDTOs[0].balDate.split('T')[0];
                    date1Td.id = 'baljuDate1'+baljuNo;
                    newRow1.appendChild(date1Td);

                    const oneNumTd = document.createElement('td');
                    const oneNumInput = document.createElement('input');
                    oneNumInput.type = 'number';
                    oneNumInput.id = 'oneNum'+baljuNo;
                    oneNumInput.style.border = '0px';
                    oneNumTd.appendChild(oneNumInput);
                    newRow1.appendChild(oneNumTd);

                    const oneDateTd = document.createElement('td');
                    const oneDateInput = document.createElement('input');
                    oneDateInput.type = 'date';
                    oneDateInput.id = 'oneDate'+baljuNo;
                    oneDateInput.style.border = '0px';
                    oneDateTd.appendChild(oneDateInput);
                    newRow1.appendChild(oneDateTd);

                    newTbody.appendChild(newRow1);

                    // 두번째 열
                    const newRow2 = document.createElement('tr');

                    const num2Td = document.createElement('td');
                    num2Td.innerText = chasuDTOs[1].balNum;
                    num2Td.id = 'baljuNum2'+baljuNo;
                    newRow2.appendChild(num2Td);

                    const date2Td = document.createElement('td');
                    date2Td.innerText = chasuDTOs[1].balDate.split('T')[0];
                    date2Td.id = 'baljuDate2'+baljuNo;
                    newRow2.appendChild(date2Td);

                    const twoNumTd = document.createElement('td');
                    const twoNumInput = document.createElement('input');
                    twoNumInput.type = 'number';
                    twoNumInput.id = 'twoNum'+baljuNo;
                    twoNumInput.style.border = '0px';
                    twoNumTd.appendChild(twoNumInput);
                    newRow2.appendChild(twoNumTd);

                    const twoDateTd = document.createElement('td');
                    const twoDateInput = document.createElement('input');
                    twoDateInput.type = 'date';
                    twoDateInput.id = 'twoDate'+baljuNo;
                    twoDateInput.style.border = '0px';
                    twoDateTd.appendChild(twoDateInput);
                    newRow2.appendChild(twoDateTd);

                    newTbody.appendChild(newRow2);

                    // 세번째 열
                    const newRow3 = document.createElement('tr');

                    const num3Td = document.createElement('td');
                    num3Td.innerText = chasuDTOs[2].balNum;
                    num3Td.id = 'baljuNum3'+baljuNo;
                    newRow3.appendChild(num3Td);

                    const date3Td = document.createElement('td');
                    date3Td.innerText = chasuDTOs[2].balDate.split('T')[0];
                    date3Td.id = 'baljuDate3'+baljuNo;
                    newRow3.appendChild(date3Td);

                    const threeNumTd = document.createElement('td');
                    const threeNumInput = document.createElement('input');
                    threeNumInput.type = 'number';
                    threeNumInput.id = 'threeNum'+baljuNo;
                    threeNumInput.style.border = '0px';
                    threeNumTd.appendChild(threeNumInput);
                    newRow3.appendChild(threeNumTd);

                    const threeDateTd = document.createElement('td');
                    const threeDateInput = document.createElement('input');
                    threeDateInput.type = 'date';
                    threeDateInput.id = 'threeDate'+baljuNo;
                    threeDateInput.style.border = '0px';
                    threeDateTd.appendChild(threeDateInput);
                    newRow3.appendChild(threeDateTd);

                    newTbody.appendChild(newRow3);

                    newTable.appendChild(newTbody);
                    setTable.appendChild(newTable);
                });
            }
        }
    });
}
// 검수차수> 입고예상일 기준 날짜 자동 넣기
function autoFillGumsuDate(number){
    const no=number;
    const checkbox=document.getElementById('btn'+no);
    if(checkbox && checkbox.checked){
        const interDate = parseInt(document.getElementById('days-input').value, 10);

        const baljuDate1=document.getElementById('baljuDate1'+no).innerText;
        const baljuDate2=document.getElementById('baljuDate2'+no).innerText;
        const baljuDate3=document.getElementById('baljuDate3'+no).innerText;

        const realtoday1 = new Date(baljuDate1);
        const realtoday2 = new Date(baljuDate2);
        const realtoday3 = new Date(baljuDate3);

        const today1=new Date(realtoday1);
        today1.setDate(today1.getDate() - interDate);
        let year = today1.getFullYear();
        let month = today1.getMonth() + 1;
        let day = today1.getDate();
        let formattedDate = year + '-' + (month < 10 ? '0' + month : month) + '-' + (day < 10 ? '0' + day : day);
        document.getElementById('oneDate' + no).value = formattedDate;

        const today2=new Date(realtoday2);
        today2.setDate(today2.getDate() - interDate);
        year = today2.getFullYear();
        month = today2.getMonth() + 1;
        day = today2.getDate();
        formattedDate = year + '-' + (month < 10 ? '0' + month : month) + '-' + (day < 10 ? '0' + day : day);
        document.getElementById('twoDate' + no).value = formattedDate;

        const today3=new Date(realtoday3);
        today3.setDate(today3.getDate() - interDate);
        year = today3.getFullYear();
        month = today3.getMonth() + 1;
        day = today3.getDate();
        formattedDate = year + '-' + (month < 10 ? '0' + month : month) + '-' + (day < 10 ? '0' + day : day);
        document.getElementById('threeDate' + no).value = formattedDate;
    }
}
// 검수차수> % 수량
function amountPer(percent){
    const allTable=document.querySelectorAll('[id^="autoTable"]');
    allTable.forEach(table=>{
        const tbody = table.querySelector('tbody');
        if (tbody) {
            const checkbox=tbody.querySelector('input[type="checkbox"]');
            const no=checkbox.id.substring(3);
            if(checkbox&&checkbox.checked){
                const baljuNum1=document.getElementById('baljuNum1'+no).innerText;
                const baljuNum2=document.getElementById('baljuNum2'+no).innerText;
                const baljuNum3=document.getElementById('baljuNum3'+no).innerText;

                const tempNum1=parseInt(baljuNum1, 10);
                const tempNum2=parseInt(baljuNum2, 10);
                const tempNum3=parseInt(baljuNum3, 10);
                const oneNum=Math.floor(tempNum1*percent/100);
                const twoNum=Math.floor(tempNum2*percent/100);
                const threeNum=Math.floor(tempNum3*percent/100);

                document.getElementById('oneNum'+no).value=oneNum;
                document.getElementById('twoNum'+no).value=twoNum;
                document.getElementById('threeNum'+no).value=threeNum;
            }
        }
    });
}
// 검수차수> 검수차수 저장
function saveGumsu(whatButton){
    const sel=whatButton;
    let gumsuNo=document.getElementById('gumsuNo').value;
    if(gumsuNo=='') gumsuNo=null;
    const person=document.getElementById('person').value;
    const partnerNo=document.getElementById('partnerNo').value;
    const gumsuDataList=[];

    const allTable=document.querySelectorAll('[id^="autoTable"]');
    allTable.forEach(table=>{
        const checkbox=table.querySelector('input[type="checkbox"]');
        if(checkbox&&checkbox.checked){
            const tableData={};
            const tdData=[];
            const no=checkbox.id.substring(3);

            const oneNum=document.getElementById('oneNum'+no).value;
            const twoNum=document.getElementById('twoNum'+no).value;
            const threeNum=document.getElementById('threeNum'+no).value;

            const oneDate=document.getElementById('oneDate'+no).value;
            const twoDate=document.getElementById('twoDate'+no).value;
            const threeDate=document.getElementById('threeDate'+no).value;

            gumsuDataList.push({gumsuNo:gumsuNo, baljuNo:no, person:person, partnerNo:partnerNo,
                oneNum:oneNum, oneDate:oneDate, twoNum:twoNum, twoDate:twoDate, threeNum:threeNum, threeDate:threeDate});
        }
    });
    $.ajax({
        url:'/saveGumsu',
        method:'POST',
        contentType: 'application/json',
        data: JSON.stringify(gumsuDataList),
        success:function(data){
            if(sel===1) window.location.href = '/progress/requiring_progress';
            if(sel===2) window.location.href = '/progress/list_progress';
        }
    });
}