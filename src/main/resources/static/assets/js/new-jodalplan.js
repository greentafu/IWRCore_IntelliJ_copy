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