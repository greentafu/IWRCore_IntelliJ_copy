let formData1 = new FormData();
let existFile1 = [];
let deleteFile1 = [];

let formData2 = new FormData();
let existFile2 = [];
let deleteFile2 = [];

// 파일추가1
document.getElementById('uploadBtn1').addEventListener('click', function() {
    var files=document.getElementById('uploadFiles1').files;

    for(var i=0; i<files.length; i++){
        let file=files[i];
        let originalName = file.name;
        let newName = originalName;
        let counter = 1;

        while ((formData1.has('files') && Array.from(formData1.values()).some(f => f.name === newName)) || existFile1.some(item => item.fileName === newName)) {
            newName = originalName.replace(/(\.[^/.]+)$/, ` (${counter})$1`);
            counter++;
        }

        formData1.append('files', file, newName);
    }
    showFileList1();
    document.getElementById('uploadFiles1').value='';
});
function showFileList1(){
    var fileTable1=document.getElementById('fileTable1');
    fileTable1.innerText='';
    let index=0;

    for (var value of formData1.values()) {
        const newRow = document.createElement('tr');

        const fileNameTd = document.createElement('td');
        const file = value instanceof File ? value : null;
        fileNameTd.innerText = file ? file.name+'         ' : '파일 아님         ';

        const button = document.createElement('button');
        button.type = 'button';
        button.className = 'btn btn-sm btn-secondary';
        button.innerText = 'x';
        button.onclick = (function(fileIndex) {
            return function() {
                const filesArray = Array.from(formData1.entries());
                filesArray.splice(fileIndex, 1);

                formData1 = new FormData();
                filesArray.forEach(([key, file]) => {
                    formData1.append(key, file);
                });
                showFileList1();
            };
        })(index);
        fileNameTd.appendChild(button);
        newRow.appendChild(fileNameTd);

        fileTable1.appendChild(newRow);
        index++;
    }
}

// 파일추가2
document.getElementById('uploadBtn2').addEventListener('click', function() {
    var files=document.getElementById('uploadFiles2').files;

    for(var i=0; i<files.length; i++){
        let file=files[i];
        let originalName = file.name;
        let newName = originalName;
        let counter = 1;

        while ((formData2.has('files') && Array.from(formData2.values()).some(f => f.name === newName)) || existFile2.some(item => item.fileName === newName)) {
            newName = originalName.replace(/(\.[^/.]+)$/, ` (${counter})$1`);
            counter++;
        }

        formData2.append('files', file, newName);
    }
    showFileList2();
    document.getElementById('uploadFiles2').value='';

    console.log("파일추가2: ",formData2, ", 기존파일2: ", existFile2);
});
function showFileList2(){
    var fileTable2=document.getElementById('fileTable3');
    fileTable2.innerText='';
    let index=0;

    for (var value of formData2.values()) {
        const newRow = document.createElement('tr');

        const fileNameTd = document.createElement('td');
        const file = value instanceof File ? value : null;
        fileNameTd.innerText = file ? file.name+'         ' : '파일 아님         ';

        const button = document.createElement('button');
        button.type = 'button';
        button.className = 'btn btn-sm btn-secondary';
        button.innerText = 'x';
        button.onclick = (function(fileIndex) {
            return function() {
                const filesArray = Array.from(formData2.entries());
                filesArray.splice(fileIndex, 1);

                formData2 = new FormData();
                filesArray.forEach(([key, file]) => {
                    formData2.append(key, file);
                });
                showFileList2();
            };
        })(index);
        fileNameTd.appendChild(button);
        newRow.appendChild(fileNameTd);

        fileTable2.appendChild(newRow);
        index++;
    }
}




// 자재 수정 첨부파일 목록 불러오기
function initMaterialFile(materCode){
    var fileTable0=document.getElementById('fileTable0');
    var code=materCode;
    $.ajax({
        url:'/file/materialFile',
        method:'GET',
        data:{code:code},
        success:function(data){
            data.forEach(x=>{
                var path=x.uploadPath.replaceAll("\\", "/");
                var uuid=x.uuid;
                var originalFileName=x.fileName;
                var fileName=originalFileName.substring(uuid.length+1);

                var temp={uuid: uuid, fileName: fileName};
                existFile1.push(temp);

                const newRow = document.createElement('tr');
                const fileNameTd = document.createElement('td');
                fileNameTd.innerText = fileName+'         ';

                const button = document.createElement('button');
                button.type = 'button';
                button.className = 'btn btn-sm btn-secondary';
                button.innerText = 'x';
                button.onclick = (function(fileIndex) {
                    newRow.remove();
                    existFile1=existFile1.filter(x => x.uuid !== uuid);
                    deleteFile1.push(uuid);
                });

                fileNameTd.appendChild(button);
                newRow.appendChild(fileNameTd);
                fileTable0.appendChild(newRow);
            });
        }
    });
}
// 자재 첨부파일 목록 불러오기
function materialFileList(materCode){
    var fileTable0=document.getElementById('fileTable0');
    var code=materCode;
    $.ajax({
        url:'/file/materialFile',
        method:'GET',
        data:{code:code},
        success:function(data){
            data.forEach(x=>{
                var path=x.uploadPath.replaceAll("\\", "/");
                var uuid=x.uuid;
                var originalFileName=x.fileName;
                var fileName=originalFileName.substring(uuid.length+1);

                const newRow = document.createElement('tr');
                const fileNameTd = document.createElement('td');
                fileNameTd.innerHTML = `<a href="/file/download?path=${path}&fileName=${originalFileName}&type=m&uuid=${uuid}">${fileName}</a>`;
                newRow.appendChild(fileNameTd);
                fileTable0.appendChild(newRow);
            });
        }
    });
}

// 제품 수정 첨부파일 목록 불러오기
function initProductFile(manuCode){
    var fileTable2=document.getElementById('fileTable2');
    var code=manuCode;
    $.ajax({
        url:'/file/productFile',
        method:'GET',
        data:{code:code},
        success:function(data){
            data.forEach(x=>{
                var path=x.uploadPath.replaceAll("\\", "/");
                var uuid=x.uuid;
                var originalFileName=x.fileName;
                var fileName=originalFileName.substring(uuid.length+1);

                var temp={uuid: uuid, fileName: fileName};
                existFile2.push(temp);

                const newRow = document.createElement('tr');
                const fileNameTd = document.createElement('td');
                fileNameTd.innerText = fileName+'         ';

                const button = document.createElement('button');
                button.type = 'button';
                button.className = 'btn btn-sm btn-secondary';
                button.innerText = 'x';
                button.onclick = (function(fileIndex) {
                    newRow.remove();
                    existFile2=existFile2.filter(x => x.uuid !== uuid);
                    console.log('deleteFile2: ', deleteFile2);
                    deleteFile2.push(uuid);
                    console.log('deleteFile2: ', deleteFile2);
                });

                fileNameTd.appendChild(button);
                newRow.appendChild(fileNameTd);
                fileTable2.appendChild(newRow);
            });
        }
    });
}
// 제품 첨부파일 목록 불러오기
function productFileList(manuCode){
    var fileTable2=document.getElementById('fileTable2');
    var code=manuCode;
    $.ajax({
        url:'/file/productFile',
        method:'GET',
        data:{code:code},
        success:function(data){
            data.forEach(x=>{
                var path=x.uploadPath.replaceAll("\\", "/");
                var uuid=x.uuid;
                var originalFileName=x.fileName;
                var fileName=originalFileName.substring(uuid.length+1);

                const newRow = document.createElement('tr');
                const fileNameTd = document.createElement('td');
                fileNameTd.innerHTML = `<a href="/file/download?path=${path}&fileName=${originalFileName}&type=p&uuid=${uuid}">${fileName}</a>`;
                newRow.appendChild(fileNameTd);
                fileTable2.appendChild(newRow);
            });
        }
    });
}