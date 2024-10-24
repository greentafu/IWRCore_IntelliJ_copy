$(document).ready(function(){
    let Lcode=$('#selectProL').val();
    let Mcode=$('#selectProM').val();
    let Scode=$('#selectProS').val();

    Lcode=(Lcode==="")?null:Lcode;
    Mcode=(Mcode==="")?null:Mcode;
    Scode=(Scode==="")?null:Scode;

    if(Lcode===null && Mcode===null && Scode===null) initPro1();
    else searchProCode(Lcode, Mcode, Scode);

    initPro2();
});

// 초기값1
function initPro1(){
    $.ajax({
        url:'/select/getPro',
        method:'GET',
        success:function(data){
            $('#selectProL').empty().append("<option value=''>전체보기</option>");
            $('#selectProM').empty().append('<option value="">전체보기</option>');
            $('#selectProS').empty().append('<option value="">전체보기</option>');

            data.proLDTOs.forEach(function(proL) {
                $('#selectProL').append(
                    $('<option></option>')
                        .attr('value', proL.proLcode)
                        .text(proL.lname)
                        .prop('selected', proL.proLcode == data.l)
                );
            });
            data.proMDTOs.forEach(function(proM) {
                $('#selectProM').append(
                    $('<option></option>')
                        .attr('value', proM.proMcode)
                        .text(proM.mname)
                        .prop('selected', proM.proMcode == data.m)
                );
            });
            data.proSDTOs.forEach(function(proS) {
                $('#selectProS').append(
                    $('<option></option>')
                        .attr('value', proS.proScode)
                        .text(proS.sname)
                        .prop('selected', proS.proScode == data.s)
                );
            });
            const tf=document.getElementById('inputProL');
            if(tf) addSelectChangeListenersP();
        }
    });
}
// 초기값2
function initPro2(){
    $.ajax({
        url:'/select/getPro',
        method:'GET',
        success:function(data){
            $('#selectProL2').empty().append("<option value=''>전체보기</option>");
            $('#selectProM2').empty().append('<option value="">전체보기</option>');
            $('#selectProS2').empty().append('<option value="">전체보기</option>');

            data.proLDTOs.forEach(function(proL) {
                $('#selectProL2').append(
                    $('<option></option>')
                        .attr('value', proL.proLcode)
                        .text(proL.lname)
                        .prop('selected', proL.proLcode == data.l)
                );
            });
            data.proMDTOs.forEach(function(proM) {
                $('#selectProM2').append(
                    $('<option></option>')
                        .attr('value', proM.proMcode)
                        .text(proM.mname)
                        .prop('selected', proM.proMcode == data.m)
                );
            });
            data.proSDTOs.forEach(function(proS) {
                $('#selectProS2').append(
                    $('<option></option>')
                        .attr('value', proS.proScode)
                        .text(proS.sname)
                        .prop('selected', proS.proScode == data.s)
                );
            });
        }
    });
}

// 선택1
function updateProCode(changedSelect){
    let Lcode=$('#selectProL').val();
    let Mcode=$('#selectProM').val();
    let Scode=$('#selectProS').val();

    if (changedSelect === 'L') {
        Mcode=null;
        Scode=null;
    } else if (changedSelect === 'M') {
        Scode=null;
    }

    $.ajax({
        url:'/select/pro',
        method:'GET',
        data:{lcode:Lcode, mcode:Mcode, scode:Scode},
        success:function(data){
            $('#selectProL').empty().append("<option value=''>전체보기</option>");
            $('#selectProM').empty().append('<option value="">전체보기</option>');
            $('#selectProS').empty().append('<option value="">전체보기</option>');

            data.proLDTOs.forEach(function(proL) {
                $('#selectProL').append(
                    $('<option></option>')
                        .attr('value', proL.proLcode)
                        .text(proL.lname)
                        .prop('selected', proL.proLcode == data.l)
                );
            });
            data.proMDTOs.forEach(function(proM) {
                $('#selectProM').append(
                    $('<option></option>')
                        .attr('value', proM.proMcode)
                        .text(proM.mname)
                        .prop('selected', proM.proMcode == data.m)
                );
            });
            data.proSDTOs.forEach(function(proS) {
                $('#selectProS').append(
                    $('<option></option>')
                        .attr('value', proS.proScode)
                        .text(proS.sname)
                        .prop('selected', proS.proScode == data.s)
                );
            });
            const tf=document.getElementById('inputProL');
            if(tf) addSelectChangeListenersP();
        }
    });
}
// 선택2
function updateProCode2(changedSelect){
    let Lcode=$('#selectProL2').val();
    let Mcode=$('#selectProM2').val();
    let Scode=$('#selectProS2').val();

    if (changedSelect === 'L') {
        Mcode=null;
        Scode=null;
    } else if (changedSelect === 'M') {
        Scode=null;
    }

    $.ajax({
        url:'/select/pro',
        method:'GET',
        data:{lcode:Lcode, mcode:Mcode, scode:Scode},
        success:function(data){
            $('#selectProL2').empty().append("<option value=''>전체보기</option>");
            $('#selectProM2').empty().append('<option value="">전체보기</option>');
            $('#selectProS2').empty().append('<option value="">전체보기</option>');

            data.proLDTOs.forEach(function(proL) {
                $('#selectProL2').append(
                    $('<option></option>')
                        .attr('value', proL.proLcode)
                        .text(proL.lname)
                        .prop('selected', proL.proLcode == data.l)
                );
            });
            data.proMDTOs.forEach(function(proM) {
                $('#selectProM2').append(
                    $('<option></option>')
                        .attr('value', proM.proMcode)
                        .text(proM.mname)
                        .prop('selected', proM.proMcode == data.m)
                );
            });
            data.proSDTOs.forEach(function(proS) {
                $('#selectProS2').append(
                    $('<option></option>')
                        .attr('value', proS.proScode)
                        .text(proS.sname)
                        .prop('selected', proS.proScode == data.s)
                );
            });
        }
    });
}

// input
function addSelectChangeListenersP() {
    $('#selectProL').off('change').on('change', function() {
        const selectedOption = this.options[this.selectedIndex];
        const selectedValue = selectedOption.textContent;
        $('#inputProL').val(selectedValue);

        const selectMValue = '';
        $('#inputProM').val(selectMValue);
        const selectSValue = '';
        $('#inputProS').val(selectSValue);
    });

    $('#selectProM').off('change').on('change', function() {
        const selectedOption = this.options[this.selectedIndex];
        const selectedValue = selectedOption.textContent;
        $('#inputProM').val(selectedValue);

        const selectedL=document.getElementById('selectProL');
        const selectedOptionL = selectedL.options[selectedL.selectedIndex];
        const selectLText = selectedOptionL.textContent;
        $('#inputProL').val(selectLText);

        const selectSValue = '';
        $('#inputProS').val(selectSValue);
    });

    $('#selectProS').off('change').on('change', function() {
        const selectedOption = this.options[this.selectedIndex];
        const selectedValue = selectedOption.textContent;
        $('#inputProS').val(selectedValue);

        const selectedL=document.getElementById('selectProL');
        const selectedOptionL = selectedL.options[selectedL.selectedIndex];
        const selectLText = selectedOptionL.textContent;
        $('#inputProL').val(selectLText);

        const selectedM=document.getElementById('selectProM');
        const selectedOptionM = selectedM.options[selectedM.selectedIndex];
        const selectMText = selectedOptionM.textContent;
        $('#inputProM').val(selectMText);
    });
}

// 초기화면1(검색)
function searchProCode(proL1, proM1, proS1){
    let Lcode=proL1;
    let Mcode=proM1;
    let Scode=proS1;

    $.ajax({
        url:'/select/pro',
        method:'GET',
        data:{lcode:Lcode, mcode:Mcode, scode:Scode},
        success:function(data){
            $('#selectProL').empty().append("<option value=''>전체보기</option>");
            $('#selectProM').empty().append('<option value="">전체보기</option>');
            $('#selectProS').empty().append('<option value="">전체보기</option>');

            data.proLDTOs.forEach(function(proL) {
                $('#selectProL').append(
                    $('<option></option>')
                        .attr('value', proL.proLcode)
                        .text(proL.lname)
                        .prop('selected', proL.proLcode == data.l)
                );
            });
            data.proMDTOs.forEach(function(proM) {
                $('#selectProM').append(
                    $('<option></option>')
                        .attr('value', proM.proMcode)
                        .text(proM.mname)
                        .prop('selected', proM.proMcode == data.m)
                );
            });
            data.proSDTOs.forEach(function(proS) {
                $('#selectProS').append(
                    $('<option></option>')
                        .attr('value', proS.proScode)
                        .text(proS.sname)
                        .prop('selected', proS.proScode == data.s)
                );
            });
        }
    });
}