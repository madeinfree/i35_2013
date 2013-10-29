var map,centerPoint,
    cityCircle,marker,
    z=0,
    z1=0;
var path,data;
var resultDiv;
var theLat=1,theLng=1;

//Result Array
var A_i35_1,
    A_i35_2 = [];
var canDoing = true;
var imgNum = 0;

function initialize() {
  centerPoint = new google.maps.LatLng(23.828677,120.80148);
  var mapOptions = {
    zoom: 13,
    mapTypeId: google.maps.MapTypeId.SATELLITE ,
    center: centerPoint
  };
  map = new google.maps.Map(document.getElementById('map_canvas'),mapOptions);

  google.maps.event.addListener(map,'click',function(event) {
  point = event.latLng;
  console.log('經度:' + point.lng() + ' 緯度:' + point.lat());

  if(event.latLng) {
    mark(point.lat(),point.lng());
    placeMarker(point.lat(),point.lng());
    //執行ajax
    AjaxPost(1,point.lat(),point.lng());
  }
 });
  resultDiv = document.getElementById("result");
}

function mark(lat, lng){ //標註座標函式
  if(z1 === 1){
    marker.setMap(null);
    z1=0;
  }
  var m = new google.maps.LatLng(lat, lng);
  marker = new google.maps.Marker({
    map:map,
    draggable:true,
    position: m
  });
  z1=1;
}

function centerAt(lat, lng) {
    var m = new google.maps.LatLng(lat,lng);
    map.panTo(m);
}

function placeMarker(lat, lng) {
  if(z === 1) {
    cityCircle.setMap(null);
    z=0;
  }
  var m = new google.maps.LatLng(lat, lng);
  map.setCenter(m);
  map.setZoom(14);

  var populationOptions = {
    strokeColor: "#ffff00",
    strokeOpacity: 0.8,
    strokeWeight: 2,
    fillColor: "#FF0000",
    fillOpacity: 0.35,
    map: map,
    center: m,
    radius: 2000
  };
  
  cityCircle = new google.maps.Circle(populationOptions);
  cityCircle.setMap(map);
  z=1;
}

function AjaxPost(theSpeciesID,LocationLat,LocationLng) {
  $.ajax({
    type:"GET",
    url:"http://bio.vexp.idv.tw/~rys/open/wetmap/s_adv.php?type="+theSpeciesID+"&x="+LocationLng+"&y="+LocationLat+"&m=5",
    cache:false,
    dataType:"text",
    success:function(data){
      console.log(data);
      if(data === '') return;
      A_i35_1 = data.split(";");
      for(var i = 0, len = A_i35_1.length; i < len-1 ; i++) {
        A_i35_2[i] = (A_i35_1[i].trim()).split(",");
      }
      resultDiv.innerHTML = "<img style=\"width:100%;\" src=\"http://www.i35.club.tw/tools/picture.php?pid=" + A_i35_2[0][1] + "\" />" + "<br />生物編號：" + A_i35_2[0][1] + "<br />中文學名：" + A_i35_2[0][2] + "<br />中文科名：" + A_i35_2[0][3] + "<br />簡易分類名稱：" + A_i35_2[0][0];
    },
    error:function(data){
      resultDiv.innerHTML = '讀取失敗';
    }
  });
}

window.addEventListener("deviceorientation", Change_Sensor_Deviceorientation, true);

function Change_Sensor_Deviceorientation(event) {
  var ax = "Acceleration X value- " + event.beta,
      ay = "Acceleration Y value- " + event.gamma,
      e = event || window.event;
  if(e.gamma <= -20 && canDoing && A_i35_1.length != 0) {
    imgNum--;
    if(imgNum <= 0) imgNum = 0;
      canDoing = false;
      resultDiv.innerHTML = "<img style=\"width:100%;\" src=\"http://www.i35.club.tw/tools/picture.php?pid=" + A_i35_2[imgNum][1] + "\" />" + "<br />生物編號：" + A_i35_2[imgNum][1] + "<br />中文學名：" + A_i35_2[imgNum][2] + "<br />中文科名：" + A_i35_2[imgNum][3] + "<br />簡易分類名稱：" + A_i35_2[imgNum][0];
        setTimeout(function() {
          canDoing = true;
        }, 1000);
  }
  if(e.gamma >= 20 && canDoing && A_i35_1.length != 0) { 
    imgNum++;
    if(imgNum >= 3) imgNum = 3;
      canDoing = false;
      resultDiv.innerHTML = "<img style=\"width:100%;\" src=\"http://www.i35.club.tw/tools/picture.php?pid=" + A_i35_2[imgNum][1] + "\" />" + "<br />生物編號：" + A_i35_2[imgNum][1] + "<br />中文學名：" + A_i35_2[imgNum][2] + "<br />中文科名：" + A_i35_2[imgNum][3] + "<br />簡易分類名稱：" + A_i35_2[imgNum][0];
        setTimeout(function() {
          canDoing = true;
        }, 1000);
  }
}

function GetLocationAndSelect() {
  theLng = window.inwcall.GetLocationLng();
  theLat = window.inwcall.GetLocationLat();
  
  if(theLng==0 || theLat==0){ //如果沒有取得經緯度
    resultDiv.innerHTML='<font color="red">尚未取得經緯度，請等待。</font>';
  } else { //有取得經緯度
    mark(theLat,theLng);
    placeMarker(theLat,theLng);
    AjaxPost(1,theLat,theLng);
  }
}

function ExitApp() {
  inwcall.ExitApp();
}