var total_inputMarkID = "markInput";

var total_updateMarkBtnID = "updateMark";
var total_getMarkMsgBtnID = "getMark";
var total_commitBtn = "commit"

//假设只有一个author,后面其实要根据author先删除并取出
function total_updateTotalMsg(mark) {
    globalImgMsg.noteTotal = [{author: "user", mark: mark, id: getIDByTime()}];      //这里是因为一个author所以才能这么做的
}

function total_sentJsonToServer() {
    var keywordPostRect = firstTimeEdit? "postMark" : "Modify";

    if(globalImgMsg.noteTotal.length === 0){
        globalImgMsg.isMarked = false;
    }else{
        globalImgMsg.isMarked = true;
    }

    $.ajax({
        type:'POST',
        url:"/Worker/"+keywordPostRect,
        data:JSON.stringify(globalImgMsg),
        success:function(result){
            alert("提交成功");
        },
        contentType:'application/json',
        dataType:'json'
    });
    alert("提交成功");
}

function total_setUpdateMarkBtn() {
    $("#" + total_updateMarkBtnID).click(() => {
        var mark = $("#" + total_inputMarkID).val();
        total_updateTotalMsg(mark);
        alert("更新成功");
    });
}

function total_setGetMarkMsgBtn() {
    $("#" + total_getMarkMsgBtnID).click(() => {
        var noteTotal = globalImgMsg.noteTotal;

        // console.log(noteTotal);

        if (noteTotal != null && noteTotal.length > 0) {
            var mark = noteTotal[0].mark;
            $("#" + total_inputMarkID).val(mark);
        }
    });
}

function total_setCommitBtn() {
    $("#" + total_commitBtn).click(() => {
        total_sentJsonToServer();
    });
}

function total_getMsgFromJsonAndUpdateMark(imgURL, provider) {
    if (!firstTimeEdit) {
        $.getJSON("/Worker/Check", {imgURL: imgURL, provider: provider}, function (data) {
            writeGlobalImgMsg(data);
        });
    }
}

function switchTotalOn() {
    total_getMsgFromJsonAndUpdateMark(global_imgURL,"provider");
    total_setUpdateMarkBtn();
    total_setGetMarkMsgBtn();
    total_setCommitBtn();
}

function total_actualSwitchOn() {
    setImgSizeAndSwitchTotalOn(global_imgURL,"canvasSaver",switchTotalOn);
}