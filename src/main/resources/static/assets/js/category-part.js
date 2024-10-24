$(document).ready(function(){
    let Lcode = $('#selectPartL').val();
    let Mcode = $('#selectPartM').val();
    let Scode = $('#selectPartS').val();

    Lcode=(Lcode==="")?null:Lcode;
    Mcode=(Mcode==="")?null:Mcode;
    Scode=(Scode==="")?null:Scode;

    if(Lcode===null && Mcode===null && Scode===null) initPart1();
    else searchPartCode(Lcode, Mcode, Scode);

    initPart2();
});

// 초기값1
function initPart1(){
    $.ajax({
        url:'/select/getPart',
        method:'GET',
        success:function(data){
            $('#selectPartL').empty().append("<option value=''>전체보기</option>");
            $('#selectPartM').empty().append('<option value="">전체보기</option>');
            $('#selectPartS').empty().append('<option value="">전체보기</option>');

            data.partLDTOs.forEach(function(partL) {
                $('#selectPartL').append(
                    $('<option></option>')
                        .attr('value', partL.partLcode)
                        .text(partL.lname)
                        .prop('selected', partL.partLcode == data.l)
                );
            });
            data.partMDTOs.forEach(function(partM) {
                $('#selectPartM').append(
                    $('<option></option>')
                        .attr('value', partM.partMcode)
                        .text(partM.mname)
                        .prop('selected', partM.partMcode == data.m)
                );
            });
            data.partSDTOs.forEach(function(partS) {
                $('#selectPartS').append(
                    $('<option></option>')
                        .attr('value', partS.partScode)
                        .text(partS.sname)
                        .prop('selected', partS.partScode == data.s)
                );
            });
            const tf=document.getElementById('inputPartL');
            if(tf) addSelectChangeListeners();
        }
    });
}
// 초기값2
function initPart2(){
    $.ajax({
        url:'/select/getPart',
        method:'GET',
        success:function(data){
            $('#selectPartL2').empty().append("<option value=''>전체보기</option>");
            $('#selectPartM2').empty().append('<option value="">전체보기</option>');
            $('#selectPartS2').empty().append('<option value="">전체보기</option>');

            data.partLDTOs.forEach(function(partL) {
                $('#selectPartL2').append(
                    $('<option></option>')
                        .attr('value', partL.partLcode)
                        .text(partL.lname)
                        .prop('selected', partL.partLcode == data.l)
                );
            });
            data.partMDTOs.forEach(function(partM) {
                $('#selectPartM2').append(
                    $('<option></option>')
                        .attr('value', partM.partMcode)
                        .text(partM.mname)
                        .prop('selected', partM.partMcode == data.m)
                );
            });
            data.partSDTOs.forEach(function(partS) {
                $('#selectPartS2').append(
                    $('<option></option>')
                        .attr('value', partS.partScode)
                        .text(partS.sname)
                        .prop('selected', partS.partScode == data.s)
                );
            });
        }
    });
}

// 선택1
function updatePartCode(changedSelect){
    let Lcode=$('#selectPartL').val();
    let Mcode=$('#selectPartM').val();
    let Scode=$('#selectPartS').val();

    if (changedSelect === 'L') {
        Mcode=null;
        Scode=null;
    } else if (changedSelect === 'M') {
        Scode=null;
    }

    $.ajax({
        url:'/select/part',
        method:'GET',
        data:{lcode:Lcode, mcode:Mcode, scode:Scode},
        success:function(data){
            $('#selectPartL').empty().append("<option value=''>전체보기</option>");
            $('#selectPartM').empty().append('<option value="">전체보기</option>');
            $('#selectPartS').empty().append('<option value="">전체보기</option>');

            data.partLDTOs.forEach(function(partL) {
                $('#selectPartL').append(
                    $('<option></option>')
                        .attr('value', partL.partLcode)
                        .text(partL.lname)
                        .prop('selected', partL.partLcode == data.l)
                );
            });
            data.partMDTOs.forEach(function(partM) {
                $('#selectPartM').append(
                    $('<option></option>')
                        .attr('value', partM.partMcode)
                        .text(partM.mname)
                        .prop('selected', partM.partMcode == data.m)
                );
            });
            data.partSDTOs.forEach(function(partS) {
                $('#selectPartS').append(
                    $('<option></option>')
                        .attr('value', partS.partScode)
                        .text(partS.sname)
                        .prop('selected', partS.partScode == data.s)
                );
            });
            const tf=document.getElementById('inputPartL');
            if(tf) addSelectChangeListeners();
        }
    });
}
// 선택2
function updatePartCode2(changedSelect){
    let Lcode=$('#selectPartL2').val();
    let Mcode=$('#selectPartM2').val();
    let Scode=$('#selectPartS2').val();

    if (changedSelect === 'L') {
        Mcode=null;
        Scode=null;
    } else if (changedSelect === 'M') {
        Scode=null;
    }

    $.ajax({
        url:'/select/part',
        method:'GET',
        data:{lcode:Lcode, mcode:Mcode, scode:Scode},
        success:function(data){
            $('#selectPartL2').empty().append("<option value=''>전체보기</option>");
            $('#selectPartM2').empty().append('<option value="">전체보기</option>');
            $('#selectPartS2').empty().append('<option value="">전체보기</option>');

            data.partLDTOs.forEach(function(partL) {
                $('#selectPartL2').append(
                    $('<option></option>')
                        .attr('value', partL.partLcode)
                        .text(partL.lname)
                        .prop('selected', partL.partLcode == data.l)
                );
            });
            data.partMDTOs.forEach(function(partM) {
                $('#selectPartM2').append(
                    $('<option></option>')
                        .attr('value', partM.partMcode)
                        .text(partM.mname)
                        .prop('selected', partM.partMcode == data.m)
                );
            });
            data.partSDTOs.forEach(function(partS) {
                $('#selectPartS2').append(
                    $('<option></option>')
                        .attr('value', partS.partScode)
                        .text(partS.sname)
                        .prop('selected', partS.partScode == data.s)
                );
            });
        }
    });
}

// input
function addSelectChangeListeners() {
    $('#selectPartL').off('change').on('change', function() {
        const selectedOption = this.options[this.selectedIndex];
        const selectedValue = selectedOption.textContent;
        $('#inputPartL').val(selectedValue);

        const selectMValue = '';
        $('#inputPartM').val(selectMValue);
        const selectSValue = '';
        $('#inputPartS').val(selectSValue);
    });

    $('#selectPartM').off('change').on('change', function() {
        const selectedOption = this.options[this.selectedIndex];
        const selectedValue = selectedOption.textContent;
        $('#inputPartM').val(selectedValue);

        const selectedL=document.getElementById('selectPartL');
        const selectedOptionL = selectedL.options[selectedL.selectedIndex];
        const selectLText = selectedOptionL.textContent;
        $('#inputPartL').val(selectLText);

        const selectSValue = '';
        $('#inputPartS').val(selectSValue);
    });

    $('#selectPartS').off('change').on('change', function() {
        const selectedOption = this.options[this.selectedIndex];
        const selectedValue = selectedOption.textContent;
        $('#inputPartS').val(selectedValue);

        const selectedL=document.getElementById('selectPartL');
        const selectedOptionL = selectedL.options[selectedL.selectedIndex];
        const selectLText = selectedOptionL.textContent;
        $('#inputPartL').val(selectLText);

        const selectedM=document.getElementById('selectPartM');
        const selectedOptionM = selectedM.options[selectedM.selectedIndex];
        const selectMText = selectedOptionM.textContent;
        $('#inputPartM').val(selectMText);
    });
}

// 초기화면1(검색)
function searchPartCode(partL1, partM1, partS1){
    let Lcode=partL1;
    let Mcode=partM1;
    let Scode=partS1;

    $.ajax({
        url:'/select/part',
        method:'GET',
        data:{lcode:Lcode, mcode:Mcode, scode:Scode},
        success:function(data){
            $('#selectPartL').empty().append("<option value=''>전체보기</option>");
            $('#selectPartM').empty().append('<option value="">전체보기</option>');
            $('#selectPartS').empty().append('<option value="">전체보기</option>');

            data.partLDTOs.forEach(function(partL) {
                $('#selectPartL').append(
                    $('<option></option>')
                        .attr('value', partL.partLcode)
                        .text(partL.lname)
                        .prop('selected', partL.partLcode == data.l)
                );
            });
            data.partMDTOs.forEach(function(partM) {
                $('#selectPartM').append(
                    $('<option></option>')
                        .attr('value', partM.partMcode)
                        .text(partM.mname)
                        .prop('selected', partM.partMcode == data.m)
                );
            });
            data.partSDTOs.forEach(function(partS) {
                $('#selectPartS').append(
                    $('<option></option>')
                        .attr('value', partS.partScode)
                        .text(partS.sname)
                        .prop('selected', partS.partScode == data.s)
                );
            });
        }
    });
}