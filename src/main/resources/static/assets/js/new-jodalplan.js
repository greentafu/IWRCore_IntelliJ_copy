// 모두 선택
function selectAll(whatButton){
    const sel=whatButton;

    var checkAllBtn = document.getElementById('checkAllBtn');
    var isChecked = checkAllBtn.value === '1';

    var allCheckBox=document.querySelectorAll('[id^="btn"]');
    allCheckBox.forEach(checkbox=> {
        checkbox.checked = !isChecked;
    });
    checkAllBtn.value = isChecked ? '0' : '1';
}

// 자동 버튼
function autoBtn(whatButton){
    const sel=whatButton;

    var allTable=document.querySelectorAll('[id^="autoTable"]');
    allTable.forEach(table=>{
        var tbody = table.querySelector('tbody');
        if (tbody) {
            var rows = tbody.querySelectorAll('tr');
            rows.forEach(row=>{
                var no=row.querySelector('[id^="check"]').id.substring(5);
                if(sel===1) autoFillNum(no);
                if(sel===2) autoFillDate(no);
            });
        }
    });
}
// 수량 자동 넣기
function autoFillNum(number){
    var no=number;
    var checkbox=document.getElementById('btn'+no);
    if(checkbox){
        if(checkbox.checked){
            var totalNum=document.getElementById('totalNum'+no).innerText;
            var stockNum=document.getElementById('stockNum'+no).innerText;
            var quantity=totalNum-stockNum;

            var eachQuantity=Math.floor(quantity/3);
            var remainder=quantity%3;
            var thirdQuantity=eachQuantity+remainder;

            document.getElementById('oneNum'+no).value=eachQuantity;
            document.getElementById('twoNum'+no).value=eachQuantity;
            document.getElementById('threeNum'+no).value=thirdQuantity;
        }
    }
}
// 날짜 자동 넣기
function autoFillDate(number){
    var no=number;
    var startDateInput=document.getElementById('baseDate').value;
    var checkbox=document.getElementById('btn'+no);
    if(checkbox){
        if(checkbox.checked){
            var interDate = parseInt(document.getElementById('days-input').value, 10);

            var realtoday = new Date(startDateInput);

            var today=new Date(realtoday);
            var year = today.getFullYear();
            var month = today.getMonth() + 1;
            var day = today.getDate();
            var formattedDate = year + '-' + (month < 10 ? '0' + month : month) + '-' + (day < 10 ? '0' + day : day);
            document.getElementById('oneDate' + no).value = formattedDate;

            var futureDate1 = new Date(today);
            futureDate1.setDate(today.getDate() + interDate);
            year = futureDate1.getFullYear();
            month = futureDate1.getMonth() + 1;
            day = futureDate1.getDate();
            formattedDate = year + '-' + (month < 10 ? '0' + month : month) + '-' + (day < 10 ? '0' + day : day);
            document.getElementById('twoDate' + no).value = formattedDate;

            var futureDate2 = new Date(today);
            futureDate2.setDate(today.getDate() + interDate * 2);
            year = futureDate2.getFullYear();
            month = futureDate2.getMonth() + 1;
            day = futureDate2.getDate();
            formattedDate = year + '-' + (month < 10 ? '0' + month : month) + '-' + (day < 10 ? '0' + day : day);
            document.getElementById('threeDate' + no).value = formattedDate;
        }
    }
}

// 조달차수 저장
function saveChasu(){
    const chasuRows=document.querySelectorAll('#contentTbody tr');
    chasuData=[];

    chasuRows.forEach(x=>{
        const checkbox=x.querySelector('input[type="checkbox"]');
        if(checkbox&&checkbox.checked){
            const cells=x.querySelectorAll('td');
            const id=cells[11].innerText;
            const oneNum=document.getElementById('oneNum'+id).value;
            const twoNum=document.getElementById('twoNum'+id).value;
            const threeNum=document.getElementById('threeNum'+id).value;
            const oneDate=document.getElementById('oneDate'+id).value;
            const twoDate=document.getElementById('twoDate'+id).value;
            const threeDate=document.getElementById('threeDate'+id).value;
            chasuData.push({id:id, oneNum:oneNum, twoNum:twoNum, threeNum:threeNum, oneDate:oneDate, twoDate:twoDate, threeDate:threeDate});
        }
    });
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