var backgroundMaxWidth = 500;
var backgroundMaxHeight = 500;

var firstTimeEdit = false;  //就是!isMark
var drawingType = "";
var global_imgURL = "";
var labelSaverID = "labelSaver";


function setFirstTimeEdit(boolean) {
    firstTimeEdit = boolean;
}

function setdrawingType(inputDrawingType) {
    drawingType = inputDrawingType;
}

function setImgURL(input) {
    global_imgURL = input;
}

function getClearJson() {
    var toReturn = {
        type: "",
        isMarked: false,
        imgURL: "",
        provider: "provider",
        noteRectangle: [],
        notePolygon: [],
        noteTotal: [],
    }
    return toReturn;
}

var globalImgMsg = getClearJson();


//这个函数用于回调callback函数，将带有img宽高的信息传回
function LoadImage(imgSrc, callback) {
    var image = new Image();
    image.src = imgSrc;
    if (image.complete) {
        callback(image);
        image.onload = function () {
        };
    } else {
        image.onload = function () {
            callback(image);
            // clear onLoad, IE behaves erratically with animated gifs otherwise
            image.onload = function () {
            };
        }
        image.onerror = function () {
            alert("Could not load image.");
        }
    }
}

function AdjustBackgroundSize(image) {
    if ((image.width / image.height) > (backgroundMaxWidth / backgroundMaxHeight)) {
        backgroundMaxHeight = backgroundMaxWidth * image.height / image.width;
    } else {
        backgroundMaxWidth = backgroundMaxHeight * image.width / image.height;
    }
}

// 实际上点的坐标都要乘以一个比例系数了，所以这个方法先不调
function ActualAdjustBackSize() {
    LoadImage("trafalgar-square-annotated.jpg", AdjustBackgroundSize);
}

function getIDByTime() {
    return new Date().getTime();
}


function getLocX(e, left) {
    return e.clientX + document.body.parentElement.scrollLeft - left;
}

function getLocY(e, top) {
    return e.clientY + document.body.parentElement.scrollTop - top;
}

function safelyQuit() {
    for (var i = 0; i < arguments.length; i++) {
        var id = arguments[i];
        var domJQ = $("#" + id);
        domJQ.unbind();
        var dom = domJQ.get(0);
        dom.onclick = null;
        dom.onmousedown = null;
        dom.onmouseup = null;
        dom.onmousemove = null;
        dom.onmouseover = null;
        dom.onmouseleave = null;
        dom.onmouseout = null;
        dom.onmousewheel = null;
    }

}

function showAllLayers(canvasId) {
    $("#" + canvasId).setLayers({visible: true}).drawLayers();
}

function hideAllLayers(canvasId) {
    $("#" + canvasId).setLayers({visible: false}).drawLayers();
}

function polygonTempArea(p0, p1, p2) {
    var area = p0.x * p1.y + p1.x * p2.y + p2.x * p0.y - p1.x * p0.y - p2.x * p1.y - p0.x * p2.y;
    return area / 2;
}

//line 249 计算polygon的质心
function getPolygonAreaCenter(points) {
    var sum_x = 0;
    var sum_y = 0;
    var sum_area = 0;
    var p1 = points[1];
    for (var i = 2; i < points.length; i++) {
        var p2 = points[i];
        var area = polygonTempArea(points[0], p1, p2);
        sum_area += area;
        sum_x += (points[0].x + p1.x + p2.x) * area;
        sum_y += (points[0].y + p1.y + p2.y) * area;
        p1 = p2;
    }
    var xx = sum_x / sum_area / 3;
    var yy = sum_y / sum_area / 3;
    return {x: xx, y: yy};
}

function drawHintTextOnLayer(pointer, layer, inputX, inputY) {
    $(pointer).drawText({
        fillStyle: '#000',
        x: inputX, y: inputY,
        fontSize: 15,
        fontFamily: 'Arial',
        text: layer.data.mark,
        layer: true,
        name: 'toDelete',
    });
}

function removeHintTextOnLayer(pointer) {
    //layer字符串要和drawHintTextOnLayer的保持一致
    $(pointer).removeLayer('toDelete');
}

function refreshLabels(canvasID, labelSaverID) {
    var labelSaver = $("#" + labelSaverID);
    labelSaver.empty();
    $("#" + canvasID).getLayers(function (layer) {
        var htmlMsg = $("<span></span>").text(layer.data.mark).attr("class", "label label-default");
        labelSaver.append(htmlMsg);
        return false;
    });
}

function writeGlobalImgMsg(data) {
    if (data.isMarked === "true" || data.isMarked === true) {
        globalImgMsg = data;
        // globalImgMsg = JSON.parse(data);
    }
}

function setCanvasSizeAndSwitchOn(imgSrc, inputCanvasID, inputDivID, callBack) {
    var image = new Image();
    image.src = imgSrc;
    // alert(image.src);

    function handle(image) {
        var width = image.width;
        var height = image.height;
        var div = $("#" + inputDivID);

        div.empty();

        //这里都是attr！不要写错写成height后面却接attr的内容
        var txt = $("<canvas></canvas>").attr('width', width).attr('height', height).attr('id', inputCanvasID).css('border', "1px solid #000");

        div.css('background-image', 'url("' + imgSrc + '")');
        div.css('width', width);
        div.css('height', height);
        div.append(txt);
        $().ready(callBack);

    }

    if (image.complete) {
        handle(image);
        image.onload = function () {
        };
    } else {
        image.onload = function () {
            handle(image);
            // clear onLoad, IE behaves erratically with animated gifs otherwise
            image.onload = function () {
            };
        }
        image.onerror = function () {
            alert("Could not load image.");
        }
    }
}

function setImgSizeAndSwitchTotalOn(imgSrc, inputDivID, callBack) {
    var div = $("#" + inputDivID);
    div.empty();
    var txt = $("<img>").attr("src", imgSrc);
    txt.css("width", "96%")
    div.append(txt);
    $().ready(callBack);
}


function getKey(key) {
    var u = window.location.search.substr(1);
    var arr = u.split('&');
    var keys = [];
    var values = [];
    for (var i = 0; i < arr.length; i++) {
        var temp = arr[i].split('=');
        keys.push(temp[0]);
        values.push(temp[1]);
    }
    return values[keys.indexOf(key)];
}

function updateGlobalJSONTypeAndURL() {
    globalImgMsg.type = drawingType;
    globalImgMsg.isMarked = !firstTimeEdit;
    globalImgMsg.imgURL = global_imgURL;
}


function setGlobalParamAndUpdateJson() {

    // var flag = getKey("flag") === "UnMarked" ? true : false;
    // var type = getKey("type");
    // //用正则表达式，/#转义，/g替换所有
    // // var src = "/" + getKey("src").split('-').join('/');
    // var src = getKey("src").split('-').join('/');
    // // alert(flag);
    // // alert(type);
    // // alert(src);

    setFirstTimeEdit(true);
    // setdrawingType("Rectangle");
    setImgURL('../../../src/temp/smaller.jpg');
    updateGlobalJSONTypeAndURL();
}


// export {ActualAdjustBackSize,getIDByTime};
