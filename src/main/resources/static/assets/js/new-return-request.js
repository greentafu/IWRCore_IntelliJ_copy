// 입고/반품> 수령확정
function saveReceiveShipment(whatButton){
    const sel=whatButton;
    const receiveNo=document.getElementById('receiveNo').value;
    $.ajax({
        url:'/saveReceiveShipment',
        method:'POST',
        data: {shipNo:receiveNo},
        success: function(response) {
            if(sel===1) window.location.href = '/goodshandling/list_received';
            if(sel===2) window.location.href = '/goodshandling/view_received?shipNO='+receiveNo;
            if(sel===3) window.location.href = '/main';
        }
    });
}
// 입고/반품> 입고확정
function saveCheckShipment(whatButton){
    const sel=whatButton;
    const receiveConfirmNo=document.getElementById('receiveConfirmNo').value;
    $.ajax({
        url:'/saveGetShipment',
        method:'POST',
        data: {shipNo:receiveConfirmNo},
        success: function(response) {
            if(sel===1) window.location.href = '/goodshandling/list_received';
            if(sel===2) window.location.href = '/goodshandling/view_received?shipNO='+receiveConfirmNo;
        }
    });
}
// 입고/반품> 수령 및 입고 모달
function receivebutton(button){
    var shipNum=button.getAttribute('shipNum');
    var shipNo=button.getAttribute('shipNo');
    document.getElementById('receiveText').innerHTML="협력회사에서 보낸 수량은 "+shipNum+"개 입니다.<br/>수령확정시 '수령확인'버튼을 누르세요.";
    document.getElementById('receiveNo').value=shipNo;
}
function receiveConfirm(button){
    var shipNum=button.getAttribute('shipNum');
    var shipNo=button.getAttribute('shipNo');
    document.getElementById('receiveConfirmText').innerHTML="협력회사에서 보낸 수량 "+shipNum+"개를 입고확정하시겠습니까?<br/>입고확정시 '입고확정'버튼을 누르세요.";
    document.getElementById('receiveConfirmNo').value=shipNo;
}
// 입고/반품> 반품 확정
function saveReturnShipment(){
    const shipNo=document.getElementById('shipNo').value;
    const email=document.getElementById('email').value;
    const returnType=document.getElementById('returnType').value;
    const returnReason=document.getElementById('returnReason').value;
    const returnText=document.getElementById('returnText').value;

    formData1.append('shipNo', shipNo);
    formData1.append('email', email);
    formData1.append('returnType', returnType);
    formData1.append('returnReason', returnReason);
    formData1.append('returnText', returnText);
    formData1.append('deleteFile', deleteFile1);

    $.ajax({
        url:'/saveReturnShipment',
        method:'POST',
        data: formData1,
            processData: false, // jQuery가 데이터를 처리하지 않도록 설정
            contentType: false, // jQuery가 Content-Type을 설정하지 않도록 설정
        success: function(response) {
            window.location.href = '/goodshandling/list_received';
        }
    });
}