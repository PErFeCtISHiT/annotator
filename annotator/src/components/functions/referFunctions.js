function getOffset(obj)   //获取任意元素的offsetLeft/offsetTop值
{
  let offsetTop = 0;
  let offsetLeft = 0;
  while(obj!==window.document.body&&obj!==null)
  {
    offsetLeft+=obj.offsetLeft;
    offsetTop+=obj.offsetTop;
    obj=obj.offsetParent;
  }
  return {offsetTop,offsetLeft};
}

function getAbsolute(target, reference = document) {
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
}

function getRandomIntInclusive(min, max) {
  min = Math.ceil(min);
  max = Math.floor(max);
  return Math.floor(Math.random() * (max - min + 1)) + min; //The maximum is inclusive and the minimum is inclusive
}

function getIDByTime() {
  return new Date().getTime();
}

function testIfCloseEnough (a, b, delta) {
  return (Math.abs(a - b) < delta);
}

function polygonTempArea(p0, p1, p2) {
  let area = p0.x * p1.y + p1.x * p2.y + p2.x * p0.y - p1.x * p0.y - p2.x * p1.y - p0.x * p2.y;
  return area / 2;
}

function getPolygonAreaCenter(points) {
  let sum_x = 0;
  let sum_y = 0;
  let sum_area = 0;
  let p1 = points[1];
  for (let i = 2; i < points.length; i++) {
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

function getMinAndMaxXY(points) {
  let Xs = [];
  let Ys = [];
  for(let i = 0; i < points.length; i++){
    Xs.push(points[i].x);
    Ys.push(points[i].y);
  }
  let xMin = Math.min(...Xs);
  let yMin = Math.min(...Ys);
  let xMax = Math.max(...Xs);
  let yMax = Math.max(...Ys);
  return {xMin,yMin,xMax,yMax};
}

export {getOffset,getAbsolute,getIDByTime,getRandomIntInclusive,testIfCloseEnough,getPolygonAreaCenter,getMinAndMaxXY}
