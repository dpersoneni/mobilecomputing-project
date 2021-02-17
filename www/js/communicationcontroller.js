// var sid = 'oSS2fvq4PD1LsV4F';
const url_base = "https://ewserver.di.unimi.it/mobicomp/accordo/";


var register = $.ajax({
    method: 'post',
    url : url_base +  "register.php",
    dataType : 'json'
      


});
  
var sid = localStorage.getItem('sid');
function getWall(sid) {
    getWallVar =  $.ajax({
        method: 'post',
        url: url_base + "getWall.php",
        data: JSON.stringify({ sid: sid }),
        dataType: 'json'
    });
    return getWallVar;
}


function getProfile() {
   getProfileVar =  $.ajax({
        method: 'post',
        url: url_base + "getProfile.php",
        data: JSON.stringify({ sid: sid }),
        dataType: 'json'
    });
    return getProfileVar;
}

function sponsors() {
    getSponsors =  $.ajax({
         method: 'post',
         url: url_base + "sponsors.php",
         data: JSON.stringify({ sid: sid }),
         dataType: 'json'
     });
     return getSponsors;
 }

 
function pay(pid1, carta) {
    if (carta.mese == 1 && carta.anno ==2021 && carta.codice == 111) {
        cc1 = {name:carta.nome,surname:carta.cognome,number:carta.numero,month:carta.mese,year:carta.anno, secret:carta.codice}
    }
    
    console.log(cc1);
    payCar = $.ajax({
        method: 'post',
        url: url_base + "pay.php",
        data: JSON.stringify({ sid: sid, pid:pid1, cc:cc1,amount:1 }),
        dataType: 'json',
        
    });
    return payVar;
}


function getChannel(ctitle) {
    getChannelVar = $.ajax({
        method: 'post',
        url: url_base + "getChannel2.php",
        data: JSON.stringify({ sid: sid, ctitle:ctitle }),
        dataType: 'json',
        
    });
    return getChannelVar;
}

function addChannel(ctitle) {
    addChannelVar = $.ajax({
        method: 'post',
        url: url_base + "addChannel.php",
        data: JSON.stringify({ sid: sid, ctitle:ctitle }),
        dataType: 'json',
        
    });
    return addChannelVar;
}

function setNameProfile(name) {
    setNameProfileVar = $.ajax({
        method: 'post',
        url: url_base + "setProfile.php",
        data: JSON.stringify({ sid: sid, name:name }),
        dataType: 'json',
        
    });
    return setNameProfileVar;
}

function setPictureProfile(picture) {
    setPictureProfileVar = $.ajax({
        method: 'post',
        url: url_base + "setProfile.php",
        data: JSON.stringify({ sid: sid, picture:picture }),
        dataType: 'json',
        
    });
    return setPictureProfileVar;
}

function getPostImage(pid) {
    getPostImageVar = $.ajax({
        method: 'post',
        url: url_base + "getPostImage.php",
        data: JSON.stringify({ sid: sid, pid:pid }),
        dataType: 'json',
        
    });
    return getPostImageVar;
}

function addTextPost(ctitle, content) {
    addTextPostVar = $.ajax({
        method: 'post',
        url: url_base + "addPost.php",
        data: JSON.stringify({ sid: sid, ctitle:ctitle, type:"t", content:content }),
        dataType: 'json',
        
    });
    return addTextPostVar;
}

function addImagePost(ctitle, content) {
    addImagePostVar = $.ajax({
        method: 'post',
        url: url_base + "addPost.php",
        data: JSON.stringify({ sid: sid, ctitle:ctitle, type:"i", content:content }),
        dataType: 'json',
        
    });
    return addImagePostVar;
}

function addLocationPost(ctitle, lat, lon) {
    addLocationPostVar = $.ajax({
        method: 'post',
        url: url_base + "addPost.php",
        data: JSON.stringify({ sid: sid, ctitle:ctitle, type:"l", lat:lat, lon:lon }),
        dataType: 'json',
        
    });
    return addLocationPostVar;
}

function getUserPicture(uid) {
    getUserPictureVar = $.ajax({
        method: 'post',
        url: url_base + "getUserPicture.php",
        data: JSON.stringify({ sid: sid, uid:uid }),
        dataType: 'json',
        
    });
    return getUserPictureVar;
}