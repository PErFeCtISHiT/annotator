(function () {
  let div = $("#saver");
  let txt = $("<canvas></canvas>").attr('width', 600).attr('height', 400).attr('id', 'canvas2').css('border', "1px solid #000");
  div.append(txt);
  console.log('called');
  let temp1 = 600 * Math.random();
  let temp2 = 400 * Math.random();
  let id = new Date().getTime();
  console.log($('#canvas2').length);
  $().ready(()=>{$('#canvas2').drawRect({
    click(){
      console.log('counted');
    },
    layer:true,
    draggable: true,
    fillStyle: '#000',
    x: 150, y: 100,
    width: 200,
    height: 100
  });});
})();


