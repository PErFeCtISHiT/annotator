(function (imgURL, workerName, sponsorName, taskID) {

  let getAbsolute = function (reference, target) {
    //因为我们会将目标元素的边框纳入递归公式中，这里先减去对应的值
    let result = {
      left: -target.clientLeft,
      top: -target.clientTop
    };
    let node = target;
    while(node !== reference && node !== document){
      result.left = result.left + node.offsetLeft + node.clientLeft;
      result.top = result.top + node.offsetTop + node.clientTop;
      node = node.parentNode;
    }
    if(isNaN(reference.scrollLeft)){
      result.right = document.documentElement.scrollWidth - result.left;
      result.bottom = document.documentElement.scrollHeight - result.top;
    }else {
      result.right = reference.scrollWidth - result.left;
      result.bottom = reference.scrollHeight - result.top;
    }
    return result;
  };


  console.log("sponsorName:" + window.myVueStore.state.workerTask.currentSponsor);
  console.log("sponsorName:" + sponsorName);

  safelyQuit("canvas");

  let backgroundMaxWidth = 500;
  let globalRate = 1;
  // let backgroundMaxHeight = 500;

  let totalHolderID = "totalMsg";
  let totalInputID = "totalInput";

  let isModified = false;  //就是!isMark
  // let drawingType = "";       //TODO 设置图画类型用的
  let global_imgURL = "";
  let global_taskID = 0;
  let global_user = "";
  let labelSaverID = "labelSaver";
  let global_sponsor = "";

  let global_canvas = $('#canvas')[0];         //后面这个其实不好这么写 要用全局的

  let cacheOfMouseDown = null;
  let cacheOfMouseUp = null;
  let cacheOfMouseMove = null;
  let cacheOfMouseClick = null;


  function pushMouseEvent() {
    cacheOfMouseDown = global_canvas.onmousedown;
    cacheOfMouseUp = global_canvas.onmouseup;
    cacheOfMouseMove = global_canvas.onmousemove;
    cacheOfMouseClick = global_canvas.onclick;
  }

  function popMouseEvent() {
    global_canvas.onmousedown = cacheOfMouseDown;
    global_canvas.onmouseup = cacheOfMouseUp;
    global_canvas.onmousemove = cacheOfMouseMove;
    global_canvas.onclick = cacheOfMouseClick;
  }

  function setGlobalSponsor(input) {
    global_sponsor = input;
  }

  function setIsModified(boolean) {
    isModified = boolean;
  }

  //TODO 设置图画类型用的
  // function setdrawingType(inputDrawingType) {
  //   drawingType = inputDrawingType;
  // }

  function setImgURL(input) {
    global_imgURL = input;
  }

  function setGlobalTaskID(input) {
    global_taskID = input;
  }

  function setGlobalUser(input) {
    global_user = input;
  }


  function getClearJson() {
    return {
      isModified: false,
      imgURL: "",
      workerName: "workerName",
      sponsorName: "sponsor",
      taskID: 0,
      noteRectangle: [],
      notePolygon: [],
      noteTotal: [{mark: '', id: ''}],
    };
  }

  let globalImgMsg = getClearJson();


//TODO 这个函数用于回调callback函数，将带有img宽高的信息传回
//   function LoadImage(imgSrc, callback) {
//     let image = new Image();
//     image.src = imgSrc;
//     if (image.complete) {
//       callback(image);
//       image.onload = function () {
//       };
//     } else {
//       image.onload = function () {
//         callback(image);
//         // clear onLoad, IE behaves erratically with animated gifs otherwise
//         image.onload = function () {
//         };
//       };
//       image.onerror = function () {
//         alert("Could not load image.");
//       }
//     }
//   }

  //TODO 等比调整大小用的
  // function AdjustBackgroundSize(image) {
  //   if ((image.width / image.height) > (backgroundMaxWidth / backgroundMaxHeight)) {
  //     backgroundMaxHeight = backgroundMaxWidth * image.height / image.width;
  //   } else {
  //     backgroundMaxWidth = backgroundMaxHeight * image.width / image.height;
  //   }
  // }

// TODO 实际上点的坐标都要乘以一个比例系数了，所以这个方法先不调
//   function ActualAdjustBackSize() {
//     LoadImage("trafalgar-square-annotated.jpg", AdjustBackgroundSize);
//   }

  function getIDByTime() {
    return new Date().getTime();
  }


  function getLocX(e, left) {
    return e.clientX - left;
  }

  function getLocY(e, top) {
    // console.log(e.clientY + document.body.parentElement.scrollTop - top);
    // console.log(e.clientY - top);
    return e.clientY - top;
  }

  function safelyQuit() {
    for (let i = 0; i < arguments.length; i++) {
      let id = arguments[i];
      let domJQ = $("#" + id);
      domJQ.unbind();
      let dom = domJQ.get(0);
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

  // function showAllLayers(canvasId) {
  //   $("#" + canvasId).setLayers({visible: true}).drawLayers();
  // }
  //
  // function hideAllLayers(canvasId) {
  //   $("#" + canvasId).setLayers({visible: false}).drawLayers();
  // }

  function polygonTempArea(p0, p1, p2) {
    let area = p0.x * p1.y + p1.x * p2.y + p2.x * p0.y - p1.x * p0.y - p2.x * p1.y - p0.x * p2.y;
    return area / 2;
  }

//line 249 计算polygon的质心
  function getPolygonAreaCenter(points) {
    console.log(points);

    let sum_x = 0;
    let sum_y = 0;
    let sum_area = 0;
    let p1 = points[1];
    for (let i = 2; i < points.length; i++) {

      console.log(points[i]);

      let p2 = points[i];
      let area = polygonTempArea(points[0], p1, p2);
      sum_area += area;
      sum_x += (points[0].x + p1.x + p2.x) * area;
      sum_y += (points[0].y + p1.y + p2.y) * area;
      p1 = p2;
    }
    let xx = sum_x / sum_area / 3;
    let yy = sum_y / sum_area / 3;
    return {x: xx, y: yy};
  }

  function drawHintTextOnLayer(pointer, layer, inputX, inputY) {
    console.log(inputX);
    console.log(inputY);

    console.log('多边形的数据');
    console.log(layer.data.mark);


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
    let labelSaver = $("#" + labelSaverID);
    labelSaver.empty();
    $("#" + canvasID).getLayers(function (layer) {
      let htmlMsg = $("<span></span>").text(layer.data.mark).attr("class", "label label-default").css('margin', '0 0 5% 0');
      labelSaver.append(htmlMsg);
      let space = $("<span></span>").text(" ");
      labelSaver.append(space);
      return false;
    });
  }

  // function writeGlobalImgMsg(data) {
  //   if (data.isMarked === "true" || data.isMarked === true) {
  //     globalImgMsg = data;
  //     // globalImgMsg = JSON.parse(data);
  //   }
  // }

  // function setCanvasSizeAndSwitchOn(imgSrc, inputCanvasID, inputDivID, callBack) {
  //   let image = new Image();
  //   image.src = imgSrc;
  //
  //   // alert(image.src);
  //
  //   function handle(image) {
  //     let width = image.width;
  //     let height = image.height;
  //     let div = $("#" + inputDivID);
  //
  //     div.empty();
  //
  //     //这里都是attr！不要写错写成height后面却接attr的内容
  //     let txt = $("<canvas></canvas>").attr('width', width).attr('height', height).attr('id', inputCanvasID).css('border', "1px solid #000");
  //
  //     div.css('background-image', 'url("' + imgSrc + '")');
  //     div.css('width', width);
  //     div.css('height', height);
  //     div.append(txt);
  //     $().ready(callBack);
  //
  //   }
  //
  //
  //   if (image.complete) {
  //     handle(image);
  //     image.onload = function () {
  //     };
  //   } else {
  //     image.onload = function () {
  //       handle(image);
  //       // clear onLoad, IE behaves erratically with animated gifs otherwise
  //       image.onload = function () {
  //       };
  //     };
  //     image.onerror = function () {
  //       alert("Could not load image.");
  //     }
  //   }
  // }

  //TODO 调整整体图像用的
  // function setImgSizeAndSwitchTotalOn(imgSrc, inputDivID, callBack) {
  //   let div = $("#" + inputDivID);
  //   div.empty();
  //   let txt = $("<img>").attr("src", imgSrc);
  //   txt.css("width", "96%");
  //   div.append(txt);
  //   $().ready(callBack);
  // }


  // TODO 路径传参，现在用不到
  // function getKey(key) {
  //   let u = window.location.search.substr(1);
  //   let arr = u.split('&');
  //   let keys = [];
  //   let values = [];
  //   for (let i = 0; i < arr.length; i++) {
  //     let temp = arr[i].split('=');
  //     keys.push(temp[0]);
  //     values.push(temp[1]);
  //   }
  //   return values[keys.indexOf(key)];
  // }

  function updateGlobalJSON() {
    // globalImgMsg.type = drawingType;
    // globalImgMsg.isMarked = !isModified;
    globalImgMsg.imgURL = global_imgURL;
    globalImgMsg.taskID = global_taskID;
    globalImgMsg.workerName = global_user;
    globalImgMsg.sponsorName = global_sponsor;
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

    setIsModified(false);
    // setdrawingType("Rectangle");
    setImgURL(imgURL);
    setGlobalSponsor(sponsorName);
    setGlobalTaskID(taskID);
    setGlobalUser(workerName);
    updateGlobalJSON();
  }


// export {ActualAdjustBackSize,getIDByTime};
  //TODO
// 以上myGlobal

  //document.body.parentElement.scrollLeft
//document.body.parentElement.scrollTop

// <div class="background" style="background-image: url(background.jpg); width: x; height: y;">
// <canvas id="myCanvas" width=100% height=100%>
// </canvas>
// </div>

  let inDrawing = false;

//TODO
  let btnSwitchDrawID = "switchEdit";
  let btnRect = $("#" + btnSwitchDrawID);
  let btnSDOriginalText = btnRect.text();
  let btnSDInDrawingText = "停止添加";

  //以下poly
  let poly_isDrawingPolygon = false;

  let poly_startBtnID = "polyEdit";
  let btnPoly = $("#" + poly_startBtnID);
  let poly_originalTxt = "区域标注";
  let poly_inDrawingTxt = "停止添加";

  let poly_inputMarkID = "markInput";
  let poly_updateMark = "updateMark";
  let poly_deleteNote = "deleteNote";
  let poly_showAll = "showAll";
  let poly_hideAll = "hideAll";
  // let poly_submit = "commit";

  let poly_hoverFillStyle = 'rgba(255, 0, 0, 0.5)';

  let poly_canvasID = "canvas";
  let poly_penColor = "rgb(246,232,4)";
  let poly_penWidth = 1;

  let poly_refresh_up_and_down = function () {
  };

  let rect_refresh_up_and_down = function () {
  };

  function setJQObjDisabled(obj, boolean) {
    obj.attr("disabled", boolean);
  }

  function setBtnDisabled(boolean) {
    btnRect.attr("disabled", boolean);
    btnPoly.attr("disabled", boolean);
  }


//TODO
  function setBtnSwitchDrawOfRect(btnID, originalTxt, inDrawingTxt) {
    $("#" + btnID).click(() => {
      if (!inDrawing) {
        pushMouseEvent();
        rect_refresh_up_and_down();
        $("#" + btnID).text(inDrawingTxt);
        setJQObjDisabled(btnPoly, true);
      } else {
        popMouseEvent();
        $("#" + btnID).text(originalTxt);
        setJQObjDisabled(btnPoly, false);
      }
      inDrawing = !inDrawing;
    });
  }

//TODO
//   function btnSwitchToNormal() {
//     $("#" + btnSwitchDrawID).text(btnSDOriginalText);
//     inDrawing = false;
//   }


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
    if (!poly_isDrawingPolygon && !inDrawing) {
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
  }

  function rect_mouseOutFunc(layer) {
    if (!poly_isDrawingPolygon && !inDrawing) {
      $(this).animateLayer(layer, {
        fillStyle: 'transparent',
      }, 0);
      $(this).removeLayer('toDelete');
    }
  }

  let CanvasExt = {
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
      // console.log(noteRectangle);
      let canvasJQ = $("#" + CanvasExt.canvasId);

      for (let i = 0; i < noteRectangle.length; i++) {
        let note = noteRectangle[i];
        console.log(note);


        canvasJQ.addLayer({
          type: 'rectangle',
          strokeStyle: CanvasExt.penColor,
          strokeWidth: CanvasExt.strokeWidth,
          name: "rectangle" + note.id,
          fromCenter: false,
          x: note.left / globalRate, y: note.top / globalRate,      //缩放比例
          width: note.width / globalRate,
          height: note.height / globalRate,
          data: note,
          mouseover: rect_mouseOverFunc,
          mouseout: rect_mouseOutFunc,
          click: function (layer) {
            //TODO
            if (!inDrawing&&!poly_isDrawingPolygon) {
              CanvasExt.showNote(CanvasExt.canvasId, CanvasExt.inputID, layer);
              CanvasExt.setUpdateNote(CanvasExt.inputID, CanvasExt.updateBtnID, layer);
              CanvasExt.setDeleteNote(CanvasExt.canvasId, CanvasExt.inputID, CanvasExt.deleteBtnID, layer);
            }
          },
        });
      }
      canvasJQ.drawLayers();

      refreshLabels(CanvasExt.canvasId, labelSaverID);
    },

    showAllLayers: function (canvasId) {
      $("#" + canvasId).setLayers({visible: true}).drawLayers();
    },

    hideAllLayers: function (canvasId) {
      $("#" + canvasId).setLayers({visible: false}).drawLayers();
    },

    hideOtherLayers: function (canvasId, layerName) {
      let tempJQ = $("#" + canvasId);
      tempJQ.getLayers(function (layer) {
        if (!(layer.name === layerName)) {
          layer.visible = false;
        }
        return false; // do not generate the array
      });
      tempJQ.drawLayers();
    },

    makeAllLayersNormal: function (canvasId) {
      $("#" + canvasId).setLayers({strokeStyle: CanvasExt.penColor}).drawLayers();
    },

    makeTheLayerThick: function (canvasId, layerName) {
      let tempJQ = $("#" + canvasId);
      tempJQ.getLayers(function (layer) {
        if (layer.name === layerName) {
          layer.strokeStyle = '#000';
        } else {
          layer.strokeStyle = CanvasExt.penColor;
        }
        return false; // do not generate the array
      });
      tempJQ.drawLayers();
    },


    setShowAllBtn: function (canvasId, showAllBtnId) {
      let tempJQ = $("#" + showAllBtnId);
      tempJQ.unbind();
      tempJQ.click(() => {
        CanvasExt.showAllLayers(canvasId);
      });
    },

    setHideAllBtn: function (canvasId, hideAllBtnId) {
      let tempJQ = $("#" + hideAllBtnId);
      tempJQ.unbind();
      tempJQ.click(() => {
        CanvasExt.hideAllLayers(canvasId);
      });
    },

    showNote: function (canvasId, inputID, layer) {
      $("#" + inputID).val(layer.data.mark);
      CanvasExt.makeTheLayerThick(canvasId, layer.name);

    },

    setUpdateNote: function (inputID, updateBtnID, layer) {
      let tempJQ1 = $("#" + updateBtnID);
      tempJQ1.unbind();
      tempJQ1.click(() => {
        let tempJQ = $("#" + inputID);
        layer.data.mark = tempJQ.val();
        tempJQ.val("");
        refreshLabels(CanvasExt.canvasId, labelSaverID);
        window.myMessage({
          message:'更新单个矩形标签成功',
          type: 'success',
          duration: 1200
        });
      });
    },

    setDeleteNote: function (canvasId, inputID, deleteBtnID, layer) {
      let tempJQ = $("#" + deleteBtnID);
      tempJQ.unbind();
      tempJQ.click(() => {
        $("#" + inputID).val("");
        $("#" + canvasId).removeLayer(layer.name);
        refreshLabels(CanvasExt.canvasId, labelSaverID);
        alert("删除成功");
      });
    },

    drawRect: function (canvasId, penColor, strokeWidth, author, inputID, updateBtnID, deleteBtnID, showAllBtnID, hideAllBtnID) {

      this.penColor = penColor;
      this.penWidth = strokeWidth;
      this.author = author;

      let canvas = $("#" + canvasId).get(0);
      //canvas 的矩形框
      let canvasRect = canvas.getBoundingClientRect();
      //矩形框的左上角坐标

      // while(canvasRect.left===0||canvasRect.top===0||canvasRect.width===0||canvasRect.height===0){
      //   console.log(canvasRect.top);
      // }                              //单线程 这种写法会引起阻塞

      let canvasLeft = canvasRect.left;
      let canvasTop = canvasRect.top;
      let canvasWidth = canvasRect.width;
      let canvasHeight = canvasRect.height;


      let refreshCanvasLoc = function () {
        canvasRect = canvas.getBoundingClientRect();
        canvasLeft = canvasRect.left;
        canvasTop = canvasRect.top;
        canvasWidth = canvasRect.width;
        canvasHeight = canvasRect.height;
      };

      // console.log(canvasLeft);
      // console.log(canvasTop);


      let id;
      let layerName;
      let x = 0;
      let y = 0;

      let preventOutOfBorderSetted = false;

      canvas.onmousedown = null;
      canvas.onmouseup = null;
      //鼠标点击按下事件，画图准备
      canvas.onmousedown = (e) => {
        //回复flag状态

        refreshCanvasLoc();

        preventOutOfBorderSetted = false;

        //TODO
        if (inDrawing) {
          setBtnDisabled(true);
          //设置画笔颜色和宽度
          let color = this.penColor;
          let penWidth = this.penWidth;

          id = getIDByTime();
          layerName = "rectangle" + id;
          x = e.clientX - canvasLeft;
          y = e.clientY - canvasTop;

          // console.log(canvasLeft);
          // console.log(canvasHeight);
          //
          // console.log("x:"+(e.clientX + document.body.parentElement.scrollLeft- canvasLeft));
          // console.log("y:"+(e.clientY + document.body.parentElement.scrollTop- canvasTop));

          let tempJQCA = $("#" + canvasId);
          tempJQCA.addLayer({
            type: 'rectangle',
            strokeStyle: color,
            strokeWidth: penWidth,
            name: layerName,
            fromCenter: false,
            x: x, y: y,
            width: 1,
            height: 1
          });

          tempJQCA.drawLayers();
          tempJQCA.saveCanvas();
          //鼠标移动事件，画图
          canvas.onmousemove = (e) => {
            refreshCanvasLoc();

            let width = e.clientX - canvasLeft - x;
            let height = e.clientY - canvasTop - y;


            tempJQCA.removeLayer(layerName);

            tempJQCA.addLayer({
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

            if (x + width >= canvasWidth - 10 || y + height >= canvasHeight - 10) {
              handleUp(e);
              preventOutOfBorderSetted = true;
            }
          }
        }
      };

      canvas.onmouseup = (e) => {
        handleUp(e);
      };

      let cacheDown = canvas.onmousedown;
      let cacheUp = canvas.onmouseup;

      let that = this;

      rect_refresh_up_and_down = function () {
        canvas.onmousedown = cacheDown;
        canvas.onmouseup = cacheUp;
      };


      function handleUp(e) {
        //原句：if (isDrawing&&!preventOutOfBorderSetted)
        refreshCanvasLoc();

        if (inDrawing && !preventOutOfBorderSetted) {

          setJQObjDisabled(btnRect, false);

          let color = that.penColor;
          let penWidth = that.penWidth;

          canvas.onmousemove = null;

          let width = e.clientX - canvasLeft - x;
          let height = e.clientY - canvasTop - y;

          let tempJQCA = $("#" + canvasId);
          tempJQCA.removeLayer(layerName);

          let dataObj = new NoteRectangle(that.author, x, y, width, height, "", id);

          function smallHandle(layer) {
            CanvasExt.showNote(canvasId, inputID, layer);
            CanvasExt.setUpdateNote(inputID, updateBtnID, layer);
            CanvasExt.setDeleteNote(canvasId, inputID, deleteBtnID, layer);
          }

          function handleClick(layer) {
            //TODO
            if (!inDrawing && !poly_isDrawingPolygon) {
              smallHandle(layer)
            }
          }


          tempJQCA.addLayer({
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

          smallHandle(tempJQCA.getLayer(layerName));

          tempJQCA.drawLayers();
          tempJQCA.saveCanvas();
          CanvasExt.setShowAllBtn(canvasId, showAllBtnID);
          CanvasExt.setHideAllBtn(canvasId, hideAllBtnID);

          //TODO
          // btnSwitchToNormal();

        }
      }
    },

    getRefreshedJson: function () {
      let noteRectangle = [];
      $("#" + CanvasExt.canvasId).getLayers(function (layer) {
        if (layer.type === 'rectangle') {
          noteRectangle.push(layer.data);
          // console.log('push_rectangle');
        }
        return false;
      });
      globalImgMsg.noteRectangle = noteRectangle;

      //TODO 这里可以做完成度判断？
      // if(noteRectangle.length===0){
      //   globalImgMsg.isMarked = false;
      // }else{
      //   globalImgMsg.isMarked = true;
      // }

      return globalImgMsg;
    },
  };


  //TODO 这个拿数据要合并
  // function getJsonFromServerAndLoadRect(imgURL, provider) {
  //   if(!isModified) {
  //     $.getJSON("/Worker/Check", {imgURL: imgURL, provider: provider}, function (data) {
  //       writeGlobalImgMsg(data);
  //       CanvasExt.loadMyRect(globalImgMsg.noteRectangle);
  //     });
  //   }
  // }


  //TODO
  // function rect_sentJsonToServer(){
  //   // console.log(JSON.stringify(CanvasExt.getRefreshedJson()));
  //
  //   let keywordPostRect = isModified? "postMark" : "Modify";
  //
  //   $.ajax({
  //     type:'POST',
  //     url:"/Worker/"+keywordPostRect,
  //     data:JSON.stringify(CanvasExt.getRefreshedJson()),
  //     success:function(result){
  //       console.log(result);
  //     },
  //     contentType:'application/json',
  //     dataType:'json'
  //   });
  //   alert("提交成功");
  // }

  //TODO
  // function setBtnSubmit(btnSubmitID) {
  //   $("#"+btnSubmitID).click(()=>{
  //     rect_sentJsonToServer();
  //   });
  // }


  function getRectStarted() {
    let color = "rgb(246,232,4)";
    let width = 1;
    //TODO
    setBtnSwitchDrawOfRect(btnSwitchDrawID, btnSDOriginalText, btnSDInDrawingText);
    CanvasExt.writeInit("canvas", color, width, global_user, "markInput", "updateMark", "deleteNote", "showAll", "hideAll");
    // getJsonFromServerAndLoadRect(global_imgURL,"provider");
    // setBtnSubmit("commit");
    CanvasExt.switchRectOn();
  }

  // function rect_actualSwitchOn() {
  //   setCanvasSizeAndSwitchOn(global_imgURL,"canvas","canvasSaver",getRectStarted);
  // }

  //TODO
  //以上rectangleDrawer
  //请先导入global
//getClearJson,getLocX,getLocY在global那里


//千万注意这传的是note的信息
  function poly_loadPolygon(notePolygons) {
    for (let i = 0; i < notePolygons.length; i++) {
      let note = notePolygons[i];

      console.log("多边形信息：");
      console.log(note);

      let points = [];

      for (let g = 0; g < note.points.length; g++) {
        // console.log(note.points[i].x);
        // console.log(note.points[i].y);
        points.push({x: note.points[g].x/globalRate, y: note.points[g].y/globalRate});
      }

      console.log(points);

      let obj = {
        strokeStyle: poly_penColor,
        strokeWidth: poly_penWidth,
        rounded: true,
        layer: true,
        name: "polygon" + note.id,
        data: note,
        mouseover: function (layer) {
          if (!poly_isDrawingPolygon && !inDrawing) {
            $(this).animateLayer(layer, {
              fillStyle: poly_hoverFillStyle,
            }, 100);
            let pointCenter = getPolygonAreaCenter(points);
            drawHintTextOnLayer(this, layer, pointCenter.x, pointCenter.y);
          }
        },
        mouseout: poly_mouseOut,
        click: poly_handleLayerClicked,
        closed: true,
      };

      for (let p = 0; p < points.length; p += 1) {
        obj['x' + (p + 1)] = points[p].x;
        obj['y' + (p + 1)] = points[p].y;
      }

      console.log(obj);

      let tempJQ = $("#" + poly_canvasID);
      tempJQ.drawLine(obj);
      tempJQ.getLayer("polygon" + note.id).type = 'line';
      refreshLabels(poly_canvasID, labelSaverID);
    }
  }

  function NotePolygon(author, points, mark, id) {
    this.author = author;
    this.points = points;
    this.mark = mark;
    this.id = id;
  }

  function poly_mouseOut(layer) {
    if (!poly_isDrawingPolygon) {
      $(this).animateLayer(layer, {
        fillStyle: 'transparent',
      }, 0);
      removeHintTextOnLayer(this);
    }
  }

  function poly_changeFocus(inputLayer) {
    let tempJQ = $("#" + poly_canvasID);
    tempJQ.getLayers(function (layer) {
      if (layer.name === inputLayer.name) {
        layer.strokeStyle = '#000';
      } else {
        layer.strokeStyle = poly_penColor;
      }
      return false; // do not generate the array
    });
    tempJQ.drawLayers();
  }

  function poly_setShowNote(layer) {
    $("#" + poly_inputMarkID).val(layer.data.mark);
    poly_changeFocus(layer);
  }

  function poly_setUpdateNote(layer) {
    let tempJQ = $("#" + poly_updateMark);
    let tempJQInput = $("#" + poly_inputMarkID);
    tempJQ.unbind();
    tempJQ.click(() => {
      layer.data.mark = tempJQInput.val();

      // console.log("输入框的信息");
      // console.log(tempJQInput.val());
      //
      // console.log("多边形标注信息");
      // console.log(layer.data.mark);

      tempJQ.val("");
      refreshLabels(poly_canvasID, labelSaverID);
      window.myMessage({
        message:'更新单个区域划分标签成功',
        type: 'success',
        duration: 1200
      });
    });
  }

  function poly_setDeleteNote(layer) {
    let tempJQ = $("#" + poly_deleteNote);
    tempJQ.unbind();
    tempJQ.click(() => {
      $("#" + poly_inputMarkID).val("");
      $("#" + poly_canvasID).removeLayer(layer.name);
      refreshLabels(poly_canvasID, labelSaverID);
      alert("删除成功");
    });
  }

  function poly_layerMsgDivide(layer) {
    poly_setShowNote(layer);
    poly_setUpdateNote(layer);
    poly_setDeleteNote(layer);
  }

  function poly_handleLayerClicked(layer) {
    if (!poly_isDrawingPolygon) {
      poly_layerMsgDivide(layer);
    }
  }

  function poly_setStartDrawingBtn(startBtnID, originalTxt, inDrawingTxt) {
    $("#" + startBtnID).click(() => {
      if (!poly_isDrawingPolygon) {
        pushMouseEvent();
        poly_refresh_up_and_down();
        setJQObjDisabled(btnRect, true);
        $("#" + startBtnID).text(inDrawingTxt);
        // poly_startDrawing(poly_canvasID, poly_penColor, poly_penWidth, author);
      } else {
        popMouseEvent();
        setJQObjDisabled(btnRect, false);
        $("#" + startBtnID).text(originalTxt);
      }
      poly_isDrawingPolygon = !poly_isDrawingPolygon;
    });
  }

  // function poly_btnStartToNormal(startBtnID, originalTxt) {
  //   $("#" + startBtnID).text(originalTxt);
  //   poly_isDrawingPolygon = false;
  // }

  // function poly_ActualBtnStartToNormal() {
  //   poly_btnStartToNormal(poly_startBtnID, poly_originalTxt);
  // }

  function Poly_Point(x, y) {
    this.x = x;
    this.y = y;
  }

  function poly_startDrawing(canvasID, penColor, penWidth, author) {

    let canvas = $("#" + canvasID).get(0);

    let canvasPoly = canvas.getBoundingClientRect();

    // while(canvasPoly.left===0||canvasPoly.top===0){console.log(canvasPoly.top)} 单线程 这种写法会引起阻塞

    let canvasLeft = canvasPoly.left;

    let canvasTop = canvasPoly.top;

    let firstPoint = true;

    let secondPoint = false;

    let id = "";

    let polyPrefix = "polygon";

    let layerName = polyPrefix + id;

    let inMoving = false;

    let bolderPenWidth = 8;


    let refreshCanvasLoc = function () {
      canvasPoly = canvas.getBoundingClientRect();

      canvasLeft = canvasPoly.left;

      canvasTop = canvasPoly.top;
    };

    let reInit = function () {
      firstPoint = true;
      secondPoint = false;
      offsetLeft = 0;
      offSetTop = 0;
      obj = {
        strokeStyle: penColor,
        strokeWidth: penWidth,
        rounded: true,
        layer: true,
        name: layerName,
        type: 'line'
      };
      refreshLayerMsg();
      points = [];
    };


    let obj = {
      strokeStyle: penColor,
      strokeWidth: penWidth,
      rounded: true,
      layer: true,
      name: layerName,
    };

    let points = [];

    let pointsCache = [];

    let refreshLayerMsg = function (inputId) {
      id = inputId;
      layerName = polyPrefix + id;
      obj.name = layerName;
    };


    let drawLines = function () {
      let jqTemp = $("#" + canvasID);
      jqTemp.removeLayer(layerName);
      for (let p = 0; p < points.length; p += 1) {
        obj['x' + (p + 1)] = points[p].x;
        obj['y' + (p + 1)] = points[p].y;
      }
      jqTemp.drawLine(obj);
    };

    let testIfCloseEnough = function (a, b, delta) {
      return (Math.abs(a - b) < delta);
    };

    canvas.onclick = null;
    let offSetTop = 0;           //实际上没有用
    let offsetLeft = 0;          //实际上没有用

    let poly_handleMouseEvent = function (e) {

      refreshCanvasLoc();

      if (poly_isDrawingPolygon) {
        if (firstPoint && !secondPoint) {
          obj.strokeWidth = bolderPenWidth;
          // offSetTop = document.body.parentElement.scrollTop;
          // offsetLeft = document.body.parentElement.scrollLeft;
          setBtnDisabled(true);

          points.push(new Poly_Point(getLocX(e, canvasLeft) - offsetLeft, getLocY(e, canvasTop) - offSetTop));
          // console.log('x:'+getLocX(e, canvasLeft)+'y:'+getLocY(e, canvasTop));
          points.push(new Poly_Point(getLocX(e, canvasLeft) - offsetLeft, getLocY(e, canvasTop) - offSetTop));

          id = getIDByTime();
          refreshLayerMsg(id);

          // draw a dot
          drawLines();

          firstPoint = false;
          secondPoint = true;
        } else if (!firstPoint && secondPoint) {
          obj.strokeWidth = penWidth;
          points[1] = new Poly_Point(getLocX(e, canvasLeft) - offsetLeft, getLocY(e, canvasTop) - offSetTop);
          // console.log('x:'+getLocX(e, canvasLeft)+'y:'+getLocY(e, canvasTop));
          // draw the line
          drawLines();
          secondPoint = false;
        } else {
          if (testIfCloseEnough(getLocX(e, canvasLeft) - offsetLeft, points[0].x, 14) && testIfCloseEnough(getLocY(e, canvasTop) - offSetTop, points[0].y, 14) && !inMoving) {
            // console.log('x:'+getLocX(e, canvasLeft)+'y:'+getLocY(e, canvasTop));
            //close the shape
            obj['closed'] = true;

            console.log('注入点集');
            console.log(points);

            pointsCache = points;

            obj['mouseover'] = function (layer) {
              if (!poly_isDrawingPolygon && !inDrawing) {
                $(this).animateLayer(layer, {
                  fillStyle: poly_hoverFillStyle,
                }, 100);

                console.log('注入点集');
                console.log(pointsCache);
                let pointCenter = getPolygonAreaCenter(pointsCache);
                drawHintTextOnLayer(this, layer, pointCenter.x, pointCenter.y);
              }
            };
            obj['mouseout'] = poly_mouseOut;
            obj['click'] = poly_handleLayerClicked;

            //把note注入图层的data中
            obj['data'] = new NotePolygon(author, points, "", id);

            drawLines();
            // poly_ActualBtnStartToNormal();

            let layerJQ = $("#" + poly_canvasID);

            layerJQ.getLayer(layerName).type = 'line';

            poly_layerMsgDivide(layerJQ.getLayer(layerName));

            //这步很重要，结束绘画动作
            // canvas.onclick = null;
            canvas.onmousemove = null;
            inMoving = false;
            setJQObjDisabled(btnPoly, false);
            reInit();
          } else {
            points.push(new Poly_Point(getLocX(e, canvasLeft) - offsetLeft, getLocY(e, canvasTop) - offSetTop));
            //redraw the lines
            drawLines();
          }
        }
      }
    };

    canvas.onclick = (e) => {
      poly_handleMouseEvent(e);
    };

    canvas.onmousedown = null;

    canvas.onmousedown = () => {
      canvas.onmousemove = null;

      inMoving = true;
      canvas.onmousemove = (e) => {
        poly_handleMouseEvent(e);

      }
    };

    canvas.onmouseup = () => {
      inMoving = false;
      canvas.onmousemove = null;
    };

    let cacheDown = canvas.onmousedown;
    let cacheUp = canvas.onmouseup;

    poly_refresh_up_and_down = function () {
      canvas.onmousedown = cacheDown;
      canvas.onmouseup = cacheUp;
    };

  }

  function poly_getRefreshedJson() {
    let notePolygon = [];
    $("#" + poly_canvasID).getLayers(function (layer) {
      if (layer.type === 'line' && layer.data && layer.data.points && layer.data.points.length > 0) {
        notePolygon.push(layer.data);
      }
      return false;
    });
    globalImgMsg.notePolygon = notePolygon;

    //TODO 检查完成度？
    // if(notePolygon.length===0){
    //   toReturn.isMarked = false;
    // }else{
    //   toReturn.isMarked = true;
    // }

    return globalImgMsg;
  }


  // function poly_getJsonFromServerAndLoadPoly(imgURL, provider) {
  //   if(!isModified) {
  //     $.getJSON("/Worker/Check", {imgURL: imgURL, provider: provider}, function (data) {
  //       writeGlobalImgMsg(data);
  //       poly_loadPolygon(globalImgMsg.notePolygon);
  //     });
  //   }
  // }

  // function poly_sentJsonToServer() {
  //   let keywordPostRect = isModified ? "postMark" : "Modify";
  //
  //   $.ajax({
  //     type: 'POST',
  //     url: "/Worker/" + keywordPostRect,
  //     data: JSON.stringify(poly_getRefreshedJson()),
  //     success: function (result) {
  //       console.log(result);
  //     },
  //     contentType: 'application/json',
  //     dataType: 'json'
  //   });
  //   alert("提交成功");
  // }

  // function poly_setBtnShowAll() {
  //   $("#" + poly_showAll).click(() => {
  //     showAllLayers(poly_canvasID);
  //   });
  // }
  //
  // function poly_setBtnHideAll() {
  //   $("#" + poly_hideAll).click(() => {
  //     hideAllLayers(poly_canvasID);
  //   });
  // }
  //
  // function poly_setButtonCommit() {
  //   $("#" + poly_submit).click(() => {
  //     poly_sentJsonToServer();
  //   });
  // }

  function poly_switchPolygonModuleStarted() {
    // poly_setBtnShowAll();
    // poly_setBtnHideAll();         //TODO rect那边设置过一次全展示了
    // poly_getJsonFromServerAndLoadPoly(global_imgURL,"provider");
    // poly_setButtonCommit();
    poly_startDrawing(poly_canvasID, poly_penColor, poly_penWidth, global_user);
    poly_setStartDrawingBtn(poly_startBtnID, poly_originalTxt, poly_inDrawingTxt);
  }

  // function poly_actualSwitchOn() {
  //   setCanvasSizeAndSwitchOn(global_imgURL, "canvas", "canvasSaver", poly_switchPolygonModuleStarted);
  // }


  //TODO 以上polygonDrawer
  // global -> 画法 -> 启动器

  function loadTotal() {
    let prefix = 'total';
    globalImgMsg.noteTotal[0].id = prefix + getIDByTime();
    let txt = globalImgMsg.noteTotal && globalImgMsg.noteTotal[0] ? globalImgMsg.noteTotal[0].mark : '';
    let totalInput = $("#" + totalInputID);
    let totalHolder = $("#" + totalHolderID);
    totalHolder.val(txt);
    totalInput.val(txt);
  }

  function setInputBind() {
    let totalInput = $("#" + totalInputID);
    let totalHolder = $("#" + totalHolderID);
    totalInput.get(0).oninput = () => {
      totalHolder.val(totalInput.val());
      globalImgMsg.noteTotal[0].mark = totalInput.val();
    }
  }


  function getJsonFromServerAndLoadAllMarks(taskID, user, imgName) {
    console.log({taskID: taskID, users: [{username: user}], imgName: imgName});
    $.ajax({
      type: 'POST',
      url: "mark/checkImage",
      data: JSON.stringify({taskID: taskID, users: [{username: user}], imgName: imgName}),
      // data:JSON.stringify({taskID:123,user:[{username:"a"},{username:"b"}],imgName:"c.png"}),
      success: function (result) {
        console.log(result);
        if (result.marks && result.marks[0]) {
          globalImgMsg = result.marks[0];
          isModified = true;
        }
        CanvasExt.loadMyRect(globalImgMsg.noteRectangle);
        poly_loadPolygon(globalImgMsg.notePolygon);
        loadTotal();
      },
      contentType: 'application/json',
      dataType: 'json'
    });
  }

  function checkIfHasMarks() {
    return globalImgMsg.notePolygon.length > 0 || globalImgMsg.noteTotal.length > 0 || globalImgMsg.noteTotal !== {};
  }

  function setGlobalBtnSubmit(btnSubmitID) {
    $("#" + btnSubmitID).click(() => {

      //先刷新数据

      multiplyRate();
      poly_getRefreshedJson();
      CanvasExt.getRefreshedJson();

      if (checkIfHasMarks()) {
        globalImgMsg.isModified = isModified;
        globalImgMsg.noteTotal[0].mark = $("#" + totalInputID).val();
        console.log(globalImgMsg);
        $.ajax({
          type: 'POST',
          url: "/mark/postMark",
          data: JSON.stringify(globalImgMsg),
          success: function (result) {
            console.log(result);
          },
          contentType: 'application/json',
          dataType: 'json'
        });
        //TODO 后面就可以去掉下面这句了
        window.myMessage(
          {
            message: '提交成功',
            type: 'success',
            duration: 1000
          }
        );
      } else {
        window.myAlert('您未进行任何标注', '错误提示', {
          confirmButtonText: '确定',
          callback: () => {
            window.myMessage({
              message: '提交失败',
              type: 'error',
              duration: 1000
            });
          }
        });
      }

    });
  }


  function setCanvasSizeAndSwitchOn2(imgSrc, inputCanvasID, inputDivID, callBack1, callBack2) {
    let image = new Image();
    image.src = imgSrc;

    // alert(image.src);

    function handle(image) {
      let width = image.width;
      let height = image.height;

      if (width > backgroundMaxWidth) {
        globalRate = width / backgroundMaxWidth;
        width = backgroundMaxWidth;
        height /= globalRate;
      }

      let div = $("#" + inputDivID);

      div.empty();

      //这里都是attr！不要写错写成height后面却接attr的内容
      let txt = $("<canvas></canvas>").attr('width', width).attr('height', height).attr('id', inputCanvasID).css('border', "1px solid #000");

      div.css('background-image', 'url("' + imgSrc + '")');
      div.css('width', width);
      div.css('height', height);
      div.append(txt);

      $().ready(() => {
        let temp = global_imgURL.split('/');
        let imgName = temp[temp.length - 1];
        getJsonFromServerAndLoadAllMarks(global_taskID, global_user, imgName);
        setGlobalBtnSubmit("commit");
        setInputBind();
        callBack1();
        callBack2();

        let canvas = $("#"+poly_canvasID)[0];
        let resultTop = getAbsolute(document,canvas).top;
        window.scrollTo(0,resultTop);
      });

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
      };
      image.onerror = function () {
        alert("Could not load image.");
      }
    }
  }


  function multiplyRate() {
    if (globalRate !== 1) {
      $("#" + poly_canvasID).getLayers(function (layer) {
        if (layer.type === 'line') {
          let points = layer.data.points;
          for (let i = 0; i < points.length; i++) {
            points[i].x *= globalRate;
            points[i].y *= globalRate;
          }
        }
        if (layer.type === 'rectangle') {
          let data = layer.data;
          data.top *= globalRate;
          data.left *= globalRate;
          data.width *= globalRate;
          data.height *= globalRate;
        }
        return false;
      });
    }
  }

  setGlobalParamAndUpdateJson();
  CanvasExt.setShowAllBtn(poly_canvasID, poly_showAll);
  CanvasExt.setHideAllBtn(poly_canvasID, poly_hideAll);
  // rect_actualSwitchOn();
  // poly_actualSwitchOn();
  setCanvasSizeAndSwitchOn2(global_imgURL, "canvas", "canvasSaver", poly_switchPolygonModuleStarted, getRectStarted);
// chooseSwitchOnBaseOnType();

  console.log(window.myVueStore.state.user.loginState);
//TODO 以上starter

})(window.myVueStore.state.workerTask.currentImageURL, window.myVueStore.state.workerTask.currentUsername, window.myVueStore.state.workerTask.currentSponsor, window.myVueStore.state.workerTask.currentTaskID);

//imgURL,workerName,sponsorName,taskID

//window.myVueStore.state.workerTask.currentUsername 第二个属性改掉
