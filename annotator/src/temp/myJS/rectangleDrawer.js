//document.body.parentElement.scrollLeft
//document.body.parentElement.scrollTop

// <div class="background" style="background-image: url(background.jpg); width: x; height: y;">
// <canvas id="myCanvas" width=100% height=100%>
// </canvas>
// </div>

var inDrawing = false;

//TODO
var btnSwitchDrawID = "switchEdit";
var btnSDOriginalText = $("#" + btnSwitchDrawID).text();
var btnSDInDrawingText = "取消添加";

//TODO
function setBtnSwitchDrawOfRect(btnID, originalTxt, inDrawingTxt) {
    $("#" + btnID).click(() => {
        if (!inDrawing) {
            $("#" + btnID).text(inDrawingTxt);
        } else {
            $("#" + btnID).text(originalTxt);
        }
        inDrawing = !inDrawing;
    });
}

//TODO
function btnSwitchToNormal() {
    $("#" + btnSwitchDrawID).text(btnSDOriginalText);
    inDrawing = false;
}




class NoteRectangle {
    constructor(author, left, top, width, height, mark, id) {
        this.author = author;
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
        this.mark = mark;
        this.id = id;
    }
}

function rect_mouseOverFunc(layer) {
        $(this).animateLayer(layer, {
            fillStyle: 'rgba(255, 0, 0, 0.5)',
        }, 100);
        $(this).drawText({
            fillStyle: '#000',
            x: layer.x + layer.width / 2, y: layer.y + layer.height / 2,
            fontSize: 15,
            fontFamily: 'Arial',
            text: layer.data.mark,
            layer: true,
            name: 'toDelete',
        });
}

function rect_mouseOutFunc(layer) {
    $(this).animateLayer(layer, {
        fillStyle: 'transparent',
    }, 0);
    $(this).removeLayer('toDelete');
}

var CanvasExt = {
    canvasId: "",
    penColor: "",
    strokeWidth: 0,
    author: "",
    inputID: "",
    updateBtnID: "",
    deleteBtnID: "",
    showAllBtnID: "",
    hideAllBtnID: "",

    writeInit: function (canvasId, penColor, strokeWidth, author, inputID, updateBtnID, deleteBtnID, showAllBtnID, hideAllBtnID) {
        CanvasExt.canvasId = canvasId;
        CanvasExt.penColor = penColor;
        CanvasExt.strokeWidth = strokeWidth;
        CanvasExt.author = author;
        CanvasExt.inputID = inputID;
        CanvasExt.updateBtnID = updateBtnID;
        CanvasExt.deleteBtnID = deleteBtnID;
        CanvasExt.showAllBtnID = showAllBtnID;
        CanvasExt.hideAllBtnID = hideAllBtnID;
    },

    switchRectOn: function () {
        CanvasExt.drawRect(CanvasExt.canvasId, CanvasExt.penColor, CanvasExt.strokeWidth, CanvasExt.author, CanvasExt.inputID, CanvasExt.updateBtnID, CanvasExt.deleteBtnID, CanvasExt.showAllBtnID, CanvasExt.hideAllBtnID)
    },

    // 注意先调用writeInit
    loadMyRect: function (noteRectangle) {
        console.log(noteRectangle);

        for (let i = 0; i < noteRectangle.length; i++) {
            var note = noteRectangle[i];
            console.log(note);

            $("#" + CanvasExt.canvasId).addLayer({
                type: 'rectangle',
                strokeStyle: CanvasExt.penColor,
                strokeWidth: CanvasExt.strokeWidth,
                name: "rectangle" + note.id,
                fromCenter: false,
                x: note.left, y: note.top,
                width: note.width,
                height: note.height,
                data: note,
                mouseover: rect_mouseOverFunc,
                mouseout: rect_mouseOutFunc,
                click: function (layer) {
                    //TODO
                    if (!inDrawing) {
                        CanvasExt.showNote(CanvasExt.canvasId, CanvasExt.inputID, layer);
                        CanvasExt.setUpdateNote(CanvasExt.inputID, CanvasExt.updateBtnID, layer);
                        CanvasExt.setDeleteNote(CanvasExt.canvasId, CanvasExt.inputID, CanvasExt.deleteBtnID, layer);
                    }
                },
            });
        }
        $("#" + CanvasExt.canvasId).drawLayers();

        refreshLabels(CanvasExt.canvasId,labelSaverID);
    },

    showAllLayers: function (canvasId) {
        $("#" + canvasId).setLayers({visible: true}).drawLayers();
    },

    hideAllLayers: function (canvasId) {
        $("#" + canvasId).setLayers({visible: false}).drawLayers();
    },

    hideOtherLayers: function (canvasId, layerName) {
        $("#" + canvasId).getLayers(function (layer) {
            if (!(layer.name === layerName)) {
                layer.visible = false;
            }
            return false; // do not generate the array
        });
        $("#" + canvasId).drawLayers();
    },

    makeAllLayersNormal: function (canvasId) {
        $("#" + canvasId).setLayers({strokeStyle: CanvasExt.penColor}).drawLayers();
    },

    makeTheLayerThick: function (canvasId, layerName) {
        $("#" + canvasId).getLayers(function (layer) {
            if (layer.name === layerName) {
                layer.strokeStyle = '#000';
            } else {
                layer.strokeStyle = CanvasExt.penColor;
            }
            return false; // do not generate the array
        });
        $("#" + canvasId).drawLayers();
    },


    setShowAllBtn: function (canvasId, showAllBtnId) {
        $("#" + showAllBtnId).unbind();
        $("#" + showAllBtnId).click(() => {
            CanvasExt.showAllLayers(canvasId);
        });
    },

    setHideAllBtn: function (canvasId, hideAllBtnId) {
        $("#" + hideAllBtnId).unbind();
        $("#" + hideAllBtnId).click(() => {
            CanvasExt.hideAllLayers(canvasId);
        });
    },

    showNote: function (canvasId, inputID, layer) {
        $("#" + inputID).val(layer.data.mark);
        CanvasExt.makeTheLayerThick(canvasId, layer.name);

    },

    setUpdateNote: function (inputID, updateBtnID, layer) {
        $("#" + updateBtnID).unbind();
        $("#" + updateBtnID).click(() => {
            layer.data.mark = $("#" + inputID).val();
            $("#" + inputID).val("");
            refreshLabels(CanvasExt.canvasId,labelSaverID);
            alert("更新成功");
        });
    },

    setDeleteNote: function (canvasId, inputID, deleteBtnID, layer) {
        $("#" + deleteBtnID).unbind();
        $("#" + deleteBtnID).click(() => {
            $("#" + inputID).val("");
            $("#" + canvasId).removeLayer(layer.name);
            refreshLabels(CanvasExt.canvasId,labelSaverID);
            alert("删除成功");
        });
    },

    drawRect: function (canvasId, penColor, strokeWidth, author, inputID, updateBtnID, deleteBtnID, showAllBtnID, hideAllBtnID) {

        this.penColor = penColor;
        this.penWidth = strokeWidth;
        this.author = author;

        var canvas = $("#" + canvasId).get(0);
        //canvas 的矩形框
        var canvasRect = canvas.getBoundingClientRect();
        //矩形框的左上角坐标
        var canvasLeft = canvasRect.left;
        var canvasTop = canvasRect.top;
        var canvasWidth = canvasRect.width;
        var canvasHeight = canvasRect.height;

        var id;
        var layerName;
        var x = 0;
        var y = 0;

        var preventOutOfBorderSetted = false;

        //鼠标点击按下事件，画图准备
        canvas.onmousedown = (e) => {
            //回复flag状态
            preventOutOfBorderSetted = false;

            //TODO
             if (inDrawing) {
                //设置画笔颜色和宽度
                var color = this.penColor;
                var penWidth = this.penWidth;

                id = getIDByTime();
                layerName = "rectangle" + id;
                x = e.clientX + document.body.parentElement.scrollLeft - canvasLeft;
                y = e.clientY + document.body.parentElement.scrollTop - canvasTop;

                $("#" + canvasId).addLayer({
                    type: 'rectangle',
                    strokeStyle: color,
                    strokeWidth: penWidth,
                    name: layerName,
                    fromCenter: false,
                    x: x, y: y,
                    width: 1,
                    height: 1
                });

                $("#" + canvasId).drawLayers();
                $("#" + canvasId).saveCanvas();
                //鼠标移动事件，画图
                canvas.onmousemove = (e) => {
                    var width = e.clientX + document.body.parentElement.scrollLeft - canvasLeft - x;
                    var height = e.clientY + document.body.parentElement.scrollTop - canvasTop - y;

                    console.log(e.clientX + document.body.parentElement.scrollLeft);
                    console.log(e.clientY + document.body.parentElement.scrollTop);

                    $("#" + canvasId).removeLayer(layerName);

                    $("#" + canvasId).addLayer({
                        type: 'rectangle',
                        strokeStyle: color,
                        strokeWidth: penWidth,
                        name: layerName,
                        fromCenter: false,
                        x: x, y: y,
                        width: width,
                        height: height
                    });

                    $("#" + canvasId).drawLayers();

                    if (x + width >= canvasWidth-10 || y + height >= canvasHeight-10) {
                        handleUp(e);
                        preventOutOfBorderSetted = true;
                    }
                }
             }
        };

        canvas.onmouseup = (e) => {
            handleUp(e);
        }

        function handleUp(e) {
            //原句：if (isDrawing&&!preventOutOfBorderSetted)
            if (inDrawing&&!preventOutOfBorderSetted) {
                var color = this.penColor;
                var penWidth = this.penWidth;

                canvas.onmousemove = null;

                var width = e.clientX + document.body.parentElement.scrollLeft - canvasLeft - x;
                var height = e.clientY + document.body.parentElement.scrollTop - canvasTop - y;

                $("#" + canvasId).removeLayer(layerName);

                var dataObj = new NoteRectangle(this.author, x, y, width, height, "", id);

                function smallHandle(layer) {
                    CanvasExt.showNote(canvasId, inputID, layer);
                    CanvasExt.setUpdateNote(inputID, updateBtnID, layer);
                    CanvasExt.setDeleteNote(canvasId, inputID, deleteBtnID, layer);
                }

                function handleClick(layer) {
                    //TODO
                    if (!inDrawing) {
                        smallHandle(layer)
                    }
                }



                $("#" + canvasId).addLayer({
                    type: 'rectangle',
                    strokeStyle: color,
                    strokeWidth: penWidth,
                    name: layerName,
                    fromCenter: false,
                    x: x, y: y,
                    width: width,
                    height: height,
                    data: dataObj,
                    mouseover: rect_mouseOverFunc,
                    mouseout: rect_mouseOutFunc,
                    click: function (layer) {
                        handleClick(layer);
                        // if (!inDrawing) {
                        //     CanvasExt.showNote(canvasId, inputID, layer);
                        //     CanvasExt.setUpdateNote(inputID, updateBtnID, layer);
                        //     CanvasExt.setDeleteNote(canvasId, inputID, deleteBtnID, layer);
                        // }
                    },
                });

                smallHandle($("#" + canvasId).getLayer(layerName));

                $("#" + canvasId).drawLayers();
                $("#" + canvasId).saveCanvas();
                CanvasExt.setShowAllBtn(canvasId, showAllBtnID);
                CanvasExt.setHideAllBtn(canvasId, hideAllBtnID);

                //TODO
                // btnSwitchToNormal();

            }
        }
    },

    getRefreshedJson: function () {
        var toReturn = globalImgMsg;
        var noteRectangle = [];
        $("#" + CanvasExt.canvasId).getLayers(function (layer) {
            if (layer.type === 'rectangle') {
                noteRectangle.push(layer.data);
            }
            return false;
        });
        toReturn.noteRectangle = noteRectangle;

        if(noteRectangle.length===0){
            toReturn.isMarked = false;
        }else{
            toReturn.isMarked = true;
        }

        return toReturn;
    },
};

function getJsonFromServerAndLoadRect(imgURL, provider) {
    if(!firstTimeEdit) {
        $.getJSON("/Worker/Check", {imgURL: imgURL, provider: provider}, function (data) {
            writeGlobalImgMsg(data);
            CanvasExt.loadMyRect(globalImgMsg.noteRectangle);
        });
    }
}


function rect_sentJsonToServer(){
    // console.log(JSON.stringify(CanvasExt.getRefreshedJson()));

    var keywordPostRect = firstTimeEdit? "postMark" : "Modify";

    $.ajax({
        type:'POST',
        url:"/Worker/"+keywordPostRect,
        data:JSON.stringify(CanvasExt.getRefreshedJson()),
        success:function(result){
            console.log(result);
        },
        contentType:'application/json',
        dataType:'json'
    });
    alert("提交成功");
}

function setBtnSubmit(btnSubmitID) {
    $("#"+btnSubmitID).click(()=>{
        rect_sentJsonToServer();
    });
}



function getRectStarted() {
    var color = "red";
    var width = 1;
    //TODO
    setBtnSwitchDrawOfRect(btnSwitchDrawID, btnSDOriginalText, btnSDInDrawingText);
    CanvasExt.writeInit("canvas", color, width, "user", "markInput", "updateMark", "deleteNote", "showAll", "hideAll");
    getJsonFromServerAndLoadRect(global_imgURL,"provider");
    setBtnSubmit("commit");
    CanvasExt.switchRectOn();
}

function rect_actualSwitchOn() {
    setCanvasSizeAndSwitchOn(global_imgURL,"canvas","canvasSaver",getRectStarted);
}
