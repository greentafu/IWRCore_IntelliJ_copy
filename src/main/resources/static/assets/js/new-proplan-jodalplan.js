var exLine=[];

// 생산계획 생산라인 수량 가져오기
function initLine(){
    var manuCode=document.getElementById('manuCode').value;
    $.ajax({
        url:'/getLines',
        method:'GET',
        data:{manuCode:manuCode},
        success:function(data){
            if(data){
                data.forEach(x=>{
                    var quantity=x.quantity;
                    var lineId='line'+x.line;
                    var input=document.getElementById(lineId);
                    input.value=quantity;

                    exLine.push(quantity);
                });
            }
        }
    });
}
// 생산계획 생산라인 수량 저장
function saveLine(){
    var manuCode=document.getElementById('manuCode').value;
    var list=[];
    var lines = document.querySelectorAll('input[id^="line"]');

    var trueNum=false;
    var minNum=false;
    lines.forEach(x=>{
        var temp=x.value;
        if(temp===null || temp==='') temp=0;
        var number=Number(temp);
        list.push(number);
        if(!Number.isInteger(number)) trueNum=true;
        if(number<0) minNum=true;
    });

    if(trueNum || minNum){
        alert('생산라인의 일일생산량은 0이상의 정수만 입력해주세요.');
    }else{
        $.ajax({
            url:'/saveLine',
            method:'GET',
            data:{manuCode:manuCode, quantityList:list},
            success:function(data){
                exLine=list;
            }
        });
    }
}
// 생산계획 저장
function savePlan(){
    var proplanNo=document.getElementById('proplanNo').value;
    var manuCode=document.getElementById('manuCode').value;
    var pronum=document.getElementById('pronum').value;
    var startDate=document.getElementById('startDate').value;
    var endDate=document.getElementById('endDate').value;
    var details=document.getElementById('details').value;

    var list=[];
    var boxes = document.querySelectorAll('input[id^="ckeck"]');
    boxes.forEach(x=>{
        if(x.checked){
            list.push(x.value);
        }
    });

    var trueNum=false;
    var minNum=false;
    if(pronum.trim()!==''){
        var number=Number(pronum);
        if(!Number.isInteger(number)) trueNum=true;
        if(number<0) minNum=true;
    }

    if(exLine.length===0){
        alert('생산라인의 생산수량을 저장 후 진행해 주세요.');
    }else if(list.length===0){
         alert('생산라인을 한 개 이상 선택해 주세요.');
    }else if(pronum.trim()===''){
        alert('제품의 총 생산수량을 입력해 주세요.');
    }else if(trueNum || minNum){
        alert('제품의 총 생산수량은 0이상의 정수를 입력해 주세요.');
    }else if(startDate.trim()===''){
        alert('제품생산 시작일을 입력해 주세요.');
    }else if(endDate.trim()===''){
        alert('제품생산 마감일을 입력해 주세요.');
    }else if(startDate>=endDate){
        alert('제품생산 마감일은 시작일보다 나중이어야 합니다.');
    }else{
        formData1.append('proplanNo', proplanNo);
        formData1.append('manuCode', manuCode);
        formData1.append('pronum', pronum);
        formData1.append('startD', startDate);
        formData1.append('endD', endDate);
        formData1.append('details', details);
        formData1.append('lineList', list);
        formData1.append('deleteFile', deleteFile1);

        $.ajax({
            url:'/savePlan',
            method:'POST',
            data: formData1,
                processData: false, // jQuery가 데이터를 처리하지 않도록 설정
                contentType: false, // jQuery가 Content-Type을 설정하지 않도록 설정
            success: function(response) {
                window.location.href = '/proteam/list_pro';
            }
        });
    }
}
// 생산계획 수정 불러오기
function initModifyProPlan(){
    const startDay=document.getElementById('startDay').innerText;
    const endDay=document.getElementById('endDay').innerText;
    const start=startDay.split('T')[0];
    const end=endDay.split('T')[0];
    document.getElementById('startDate').value=start;
    document.getElementById('endDate').value=end;

    const lines=document.getElementById('lines').innerText;
    const line=lines.split(',');
    line.forEach(x=>{
        const lineId='ckeck'+x;
        const tempLine=document.getElementById(lineId);
        if(tempLine) tempLine.checked=true;
    });
}