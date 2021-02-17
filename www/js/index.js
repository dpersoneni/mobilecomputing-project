/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

// Wait for the deviceready event before using any of Cordova's device APIs.
// See https://cordova.apache.org/docs/en/latest/cordova/events/events.html#deviceready
document.addEventListener('deviceready', onDeviceReady, false);

function onDeviceReady() {
    // Cordova is now initialized. Have fun!

    console.log('Running cordova-' + cordova.platformId + '@' + cordova.version);

    // var scritta = "scritta";
    // sessionStorage.setItem("scritta", scritta);

    registerUser();

    db = window.sqlitePlugin.openDatabase({
        name: 'my.db',
        location: 'default',
    });

    let queries = [
        'CREATE TABLE IF NOT EXISTS ImageContent (pid TEXT PRIMARY KEY, content TEXT)',
        'CREATE TABLE IF NOT EXISTS UserPicture (uid TEXT PRIMARY KEY, pversion TEXT , content TEXT)'

    ];
    db.sqlBatch(queries, creationSuccess, creationError);

}

function creationSuccess() {
    console.log("database created");
};
var creationError = function (error) {
    console.log('database creation ERROR: ' + error.message);
}

$(document).ready(function () {
    //OPEN CHANNEL PAGE
    $('.floatbutton').unbind("click").click(function () {
        $("#wallpage").hide();
        $("#createchannelpage").show();
        manageEventsCreateChannelPage();
    });

    //OPEN PROFILE PAGE
    $('#openuserimage').unbind("click").click(function () {
        console.log("UEE");
        $("#wallpage").hide();
        $("#usersettingspage").show();
        getProfileVar = getProfile();
        getProfileVar.done(manageEventsProfilePage).fail(logError);

    });
    /*
    $('#opensponsorpage').unbind("click").click(function () {
        console.log("UEE1");
        $("#wallpage").hide();
        $("#sponsorpage").show();
        sponsorsVar = sponsors();
        sponsorsVar.done(createSponsorPage).fail(logError);

    });
    
    */
    $('#opencartacredito').unbind("click").click(function () {
        console.log("UEE1");
        $("#wallpage").hide();
        $("#creditopage").show();
    });
});


var nomeglobale;

getProfileVar = getProfile();
getProfileVar.done(function (response) {
    nomeglobale = response.name;
    console.log(nomeglobale);
}).fail(logError);

$('.backbutton2').unbind("click").click(function () {
    console.log("torno da credito a wall");
    $("#creditopage").hide();
    $("#wallpage").show();


});

$('#buttonaggiungicarta').unbind("click").click(function () {
    console.log("UEE1");
    console.log($('#nome').val());
    console.log($('#cognome').val());
    console.log($('#numerocarta').val());
    console.log($('#mese').val());
    console.log($('#anno').val());
    console.log($('#codice').val());

    var nome1 = $('#nome').val();
    var cognome1 = $('#cognome').val();
    var numero1 = $('#numerocarta').val();
    var mese1 = parseInt($('#mese').val());
    var anno1 = parseInt($('#anno').val());
    var codice1 = parseInt($('#codice').val());

    console.log(typeof(anno1));


    var carta = {
        nome: nome1,
        cognome: cognome1,
        numero: numero1,
        mese: mese1,
        anno: anno1,
        codice: codice1
    }
    console.log(carta);
    localStorage.setItem("carta", JSON.stringify(carta));
    $("#creditopage").hide();
    $("#wallpage").show();
});

//////////////////////////////////////////////////////// 
// SPONSOR PAGE
/////////////////////////////////////////////////////
var textToSend;
var createSponsorPage = function (response) {
    $('#sponsors').empty();

    console.log(response);
    response.sponsors.forEach(element => {
        sponsor = createSponsorElement(element);


        $('#sponsors').append(sponsor);

    })

    $('.sponsorholder').unbind("click").click(function () {
        var text = $(this).attr('id');
        textToSend = text;

        openSponsorPage(textToSend);
    });

    // Model.getInstance().printAllPosts();
    $('.backbutton2').unbind("click").click(function () {
        console.log("torno da spo a wall");
        $("#sponsorpage").hide();
        $("#wallpage").show();
        $('#sponsors').empty();


    });
}


function createSponsorElement(element) {
    console.log(element)
    var text = element.text.replace(/\s+/g, '')

    txt = "<div  class='sponsorholder' id='" + text + "' >  ";
    txt += "<div id='sponsorholder' class=" + element.url + " >";
    src = "data:image/png;base64," + element.image;
    // console.log(src);
    txt += "<img src=" + src + " class='imagesponsor' id='userpicture' > <h3 id='name' class='nameinfo'>" + element.text + "</h3>";
    txt += "</div>";

    txt += "</div>"

    return txt;
}

$('.backbutton3').unbind("click").click(function () {
    console.log("torno da spo a wall");
    $("#sponsorpage").hide();
    $("#wallpage").show();


});

function openSponsorPage(text) {
    console.log(text);
    sponsorsVar = sponsors();
    sponsorsVar.done(function (response) {
        response.sponsors.forEach(element => {
            console.log(element);
            if (element.text.replace(/\s+/g, '') == text) {

                src = "data:image/png;base64," + element.image;

                var immagine = '<img id="imagetotalscreen1" src="' + src + '" alt="">';
                $('#fullsponsorpage').append(immagine);
                var testo = '<p id="sponsortext">' + element.text + '</p>';
                $('#fullsponsorpage').append(testo);

                var url = "'http://" + element.url + "'";
                var system = "'_system'";
                var bottone = '<button class="btn btn-primary" onclick="window.open(' + url + ',' + system + '); return false;"  ></button>'
                $('#fullsponsorpage').append(bottone);

            }

        })


    }).fail(logError);

    $("#sponsorpage").hide();
    $("#fullsponsorpage").show();
    //console.log(url);
    //console.log(image);
    //console.log(text);
}

function backbuttonfull() {
    $("#fullsponsorpage").empty();

    console.log("torno da fullspo a spo");
    $("#fullsponsorpage").hide();
    $("#sponsorpage").show();
}


/////////////////////////////////////////////////////
//     REGISTER USER
/////////////////////////////////////////////////////
function registerUser() {
    sid = localStorage.getItem('sid');
    if (sid === null) {
        register.done(saveSid).fail(logError);

    } else {
        console.log("Ho preso un sid :   " + sid);

        var showWall = getWall(sid);
        showWall.done(printWall).fail(logError);
    }
}
// CALLBACK 
var saveSid = function (response) {
    localStorage.setItem('sid', response.sid);
    var showWallAfterSid = getWall(response.sid);
    showWallAfterSid.done(printWall).fail(logError);
}

var logError = function (error) {
    console.log(error);
}


/////////////////////////////////////////////////////
//     VARIABILI GLOBALI
/////////////////////////////////////////////////////
var ctitleToSend;
var imageToSend;
var latitudeToSend;
var longitudeToSend;


/////////////////////////////////////////////////////
//     WALL
/////////////////////////////////////////////////////

var printWall = function (response) {
    // console.log(response)
    response.channels.forEach(element => {
        channel = createChannelElement(element);

        //console.log(channel);
        $('#channels').append(channel);
        Model.getInstance().addChannel(channel);

    });
    $('.channelholder').unbind("click").click(function () {
        var ctitle = $(this).children('#ctitle').text();
        ctitleToSend = ctitle;

        var openChannel = getChannel(ctitle);
        openChannel.done(printChannel).fail(logError);
    });
    $('.channelholdermine').unbind("click").click(function () {
        var ctitle = $(this).children('#ctitle').text();
        ctitleToSend = ctitle;

        var openChannel = getChannel(ctitle);

        openChannel.done(printChannel).fail(logError);
    });


};

/////////////////////////////////////////////////////
//     CHANNEL
/////////////////////////////////////////////////////
var printChannel = function (response) {
    Model.getInstance().clearChannel();

    $("#wallpage").hide();
    $("#channelpage").show();
    $('#texttosend').val("");
    $('#logo-center').text(ctitleToSend);

    let userToCheck = new Set();
    var userDict = new Object();

    response.posts.forEach(element => {
        userToCheck.add(element.uid);
        if (element.type == "t") {
            let postText = new PostText(element.uid, element.name, element.pversion, element.pid, element.type, element.hv, element.content);
            Model.getInstance().addPost(postText);
            //   console.log(postText);
        } else if (element.type == "i") {
            let postImage = new PostImage(element.uid, element.name, element.pversion, element.pid, element.type, element.hv);
            Model.getInstance().addPost(postImage);

        } else if (element.type == "l") {
            let postLocation = new PostLocation(element.uid, element.name, element.pversion, element.pid, element.type,element.hv, element.lat, element.lon);
            Model.getInstance().addPost(postLocation);
            //   console.log(postLocation);

        }
    });
    //  Model.getInstance().printAllPosts();

    Model.getInstance()._posts.forEach(element => {
        post = createPostElement(element);

        $('#posts').append(post);
        if (element._type == "i") {
            let query = 'SELECT * FROM ImageContent WHERE pid=?';
            db.executeSql(query, [element._pid], function (resultSet) {

                //         console.log(resultSet);
                if (resultSet.rows.length != 0) {
                    //     console.log("elemento già presente in db");
                    //   console.log(resultSet.rows.item(0));
                    src = "data:image/png;base64," + resultSet.rows.item(0).content;
                    $("#" + element._pid + " ").children('#imgcontent').attr("src", src);

                } else {

                    getPostImageVar = getPostImage(element._pid);
                    getPostImageVar.done(function (response1) {
                        //   console.log("getPostImage");
                        element._content = response1.content;
                        let query2 = 'INSERT INTO ImageContent (pid,content) VALUES (?,?)';

                        db.executeSql(query2, [element._pid, response1.content], function (tx, res) {
                            //             console.log("INSERTION SUCCESS");
                            //     console.log(tx);
                            //      console.log(res);
                        }, insertionError);

                        src = "data:image/png;base64," + response1.content;
                        $("#" + element._pid + " ").children('#imgcontent').attr("src", src);


                    }).fail(logError);

                }
            }, function (tx, error) {
                console.log(tx);
                console.log(error);
            });
        }

        let queryUser = 'SELECT * FROM UserPicture WHERE uid=?';
        db.executeSql(queryUser, [element._uid], function (resultSet) {
            //      console.log(resultSet.rows.item(0));
            //  console.log(resultSet.rows.length);

            if (resultSet.rows.length == 0 || resultSet.rows.item(0).pversion != element._pversion) {
                console.log("profile picture non in db");
                var getUserPictureVar = getUserPicture(element._uid);
                getUserPictureVar.done(function (response) {
                    userPicture = response.picture;
                    pVersion = response.pversion;


                    let queryIns = 'INSERT INTO UserPicture (uid, pversion, content) VALUES (?,?,?)';
                    //   let queryIns = 'DELETE FROM UserPicture WHERE uid=?';

                    db.executeSql(queryIns, [element._uid, pVersion, userPicture], function () {
                        console.log("INSERTION SUCCESS")
                    }, insertionError);
                    if (userPicture != null) {

                        src = "data:image/png;base64," + userPicture;
                        //   console.log(src)
                        $("." + element._uid + " ").children('#userpicture').attr("src", src);
                    }
                })
            } else {
                console.log("profile picture presente  in db");
                if (resultSet.rows.item(0).content != null) {
                    console.log("uoooo");
                    src = "data:image/png;base64," + resultSet.rows.item(0).content;
                    $("." + element._uid + " ").children('#userpicture').attr("src", src);
                }


            }
        }, function (tx, error) {
            console.log(tx);
            console.log(error);
        });


    });

    // Model.getInstance().printAllPosts();
    $('.backbutton1').unbind("click").click(function () {
        console.log("torno da channel a wall");
        $("#channelpage").hide();
        $("#wallpage").show();
        $('#posts').empty();

        $("#showaddimage").hide();
        $("#showaddposition").hide();

    });

    $('#buttonaddtextpost').unbind("click").click(function () {
        var addPostVar;
        $("#showaddimage").hide();
        $("#showaddposition").hide();
        if ($('#texttosend').val() != "") {

            addPostVar = addTextPost(ctitleToSend, $('#texttosend').val());
            addPostVar.done(function () {

                $('#posts').empty();
                var openChannel = getChannel(ctitleToSend);

                openChannel.done(printChannel).fail(logError);

            }).fail(logError);

        } else {
            alert("You have to write something!")
        }
        return false;
    });

    $('#buttonopengallery').unbind("click").click(function () {
        console.log("ho cliccato");

        $("#showaddposition").hide();
        $("#showaddimage").hide();
        $("#buttonaddimagepost").hide();



        navigator.camera.getPicture(function cameraSuccess(imageData) {
            console.log(imageData);
            if (imageData.length < 137000) {
                $("#showaddimage").show();

                src = "data:image/png;base64," + imageData;
                console.log(src);

                $("#imagetosend").attr('src', src);
                imageToSend = imageData;

                $('#showaddimage').append("<button class='btn btn-primary' id='buttonaddimagepost' onclick='addImagePostAfterClick()'><i class='fas fa-paper-plane'></i>  Add post</button>")
            }


        }, function cameraError(error) {
            console.debug("Unable to obtain picture: " + error, "app");

        }, {
            quality: 50,
            sourceType: Camera.PictureSourceType.PHOTOLIBRARY,
            destinationType: Camera.DestinationType.DATA_URL
        });


        return false;
    });



    $('#buttonsaveposition').unbind("click").click(function () {
        $("#showaddimage").hide();
        $("#showaddposition").hide();
        $("#buttonaddlocationpost").hide();

        console.log("ho cliccato open position");

        var options = { enableHighAccuracy: true };

        if (!navigator.geolocation) {
            console.log("Geo not supported");
        } else {
            console.log("geo supportata");
            navigator.geolocation.getCurrentPosition(success, fail, options);
        }

    });

    function success(position) {
        console.log("position: è: " + position.coords.latitude + ";" + position.coords.longitude);
        $("#showaddposition").show();
        latitudeToSend = position.coords.latitude;
        longitudeToSend = position.coords.longitude;
        $("#positiontosend").text("Position recorded!");

        $('#showaddposition').append("<button class='btn btn-primary' id='buttonaddlocationpost' onclick='addPositionPostAfterClick()'><i class='fas fa-paper-plane'></i>  Add post</button>")
    }

    function fail(error) {
        console.log("errore:" + error);
    }
};




/////////////////////////////////////////////////////
//     FUNZIONI INVIO POST
/////////////////////////////////////////////////////
function addImagePostAfterClick() {
    $("#showaddimage").hide();

    console.log("ho cliccato ADD POST");

    addImagePostVar = addImagePost(ctitleToSend, imageToSend);
    addImagePostVar.done(function () {
        $('#posts').empty();
        var openChannel = getChannel(ctitleToSend);

        openChannel.done(printChannel).fail(logError);
    }).fail(logError);
}
function addPositionPostAfterClick() {
    $("#showaddposition").hide();

    addLocationVar = addLocationPost(ctitleToSend, latitudeToSend, longitudeToSend);
    addLocationVar.done(function () {
        $('#posts').empty();
        var openChannel = getChannel(ctitleToSend);

        openChannel.done(printChannel).fail(logError);
    }).fail(logError);
}



/////////////////////////////////////////////////////
//     CHANNEL
/////////////////////////////////////////////////////
function manageEventsCreateChannelPage() {

    // channelName = $('#newchannelname').val();
    $('#buttoncreatechannel').unbind("click").click(function () {
        // alert($('#newchannelname').val());
        var createNewChannel = addChannel($('#newchannelname').val());

        createNewChannel.done(returnToWall).fail(logError);
    });

    $('.backbutton').unbind("click").click(function () {
        console.log("torno da createchannel a wall");

        $("#createchannelpage").hide();
        $("#wallpage").show();

    });

};


/////////////////////////////////////////////////////
//     PROFILE PAGE
/////////////////////////////////////////////////////
var manageEventsProfilePage = function (response) {
    //  $('#provascritta').text(sessionStorage.getItem('scritta'));

    $('#newnameprofile').val(response.name);

    if (response.picture != null) {
        src = "data:image/png;base64," + response.picture;

        $("#pictureprofile").attr("src", src);

    }

    // channelName = $('#newchannelname').val();
    $('#buttonchangename').unbind("click").click(function () {

        var setNameVar = setNameProfile($('#newnameprofile').val());

        setNameVar.done(settingSuccess).fail(logError);
    });

    $('#buttonchangeimage').unbind("click").click(function () {
        navigator.camera.getPicture(function cameraSuccess(imageData) {
            console.log(imageData);

            if (imageData.length < 137000) {
                var setPictureVar = setPictureProfile(imageData);
                setPictureVar.done(settingSuccess).fail(function () {
                    alert("Try with another photo, this one is too big!");
                });
            }


        }, function cameraError(error) {
            console.debug("Unable to obtain picture: " + error, "app");
            alert("Try with another photo, this one is too big!");

        }, {
            quality: 50,
            sourceType: Camera.PictureSourceType.PHOTOLIBRARY,
            destinationType: Camera.DestinationType.DATA_URL
        });
    });


    $('.backbutton').unbind("click").click(function () {
        console.log("torno da settings a wall");

        $("#usersettingspage").hide();
        $("#wallpage").show();
    });
};



var settingSuccess = function () {
    alert("Informations changed successfully!");
    getProfileVar = getProfile();
    getProfileVar.done(manageEventsProfilePage).fail(logError);
}

var returnToWall = function () {
    $("#createchannelpage").hide();
    $("#wallpage").show();
}



/////////////////////////////////////////////////////
//    CHANNEL GUI HOLDER
/////////////////////////////////////////////////////
function createChannelElement(element) {
    if (element.mine == "t") {
        txt = "<div class='channelholdermine' id='" + element.ctitle + "'>";
        txt += "<h3 id='ctitle'>" + element.ctitle + "</h3>";
        txt += "</div>";

        return txt;
    }
    txt = "<div class='channelholder' id='" + element.ctitle + "'>";
    txt += "<h3 id='ctitle'>" + element.ctitle + "</h3>";
    txt += "</div>";

    return txt;
}


/////////////////////////////////////////////////////
//     POST GUI HOLDER
/////////////////////////////////////////////////////
var pidglobale;
function openInfoCarta(pid) {
    pidglobale = pid;
    console.log("ue");
    carta = JSON.parse(localStorage.getItem('carta'));
    if (carta == null) {
        window.plugins.toast.showShortTop('devi prima inserire i dati della tua carta di credito', function (a) { console.log('toast success: ' + a) }, function (b) { alert('toast error: ' + b) })


    } else {

        navigator.notification.confirm(
            "vuoipagare 1€ per aumentare la visibilità di questo post?",  // message
            confermaVisibilita,         // callback
            'vuoi pagare 1€ per aumentare la visibilità di questo post?',            // title
            ['Si', 'No']                  // buttonName
        );
    }
}


function confermaVisibilita(buttonIndex) {
    console.log(buttonIndex);
    carta = JSON.parse(localStorage.getItem('carta'));
    if (buttonIndex == 1) {
        pidglobale = ""+pidglobale+"";
        var payVar = pay(pidglobale,carta);
        payVar.done(function (response) {

         console.log(response)

        }).fail(logError);
    }
}

function createPostElement(element) {
    console.log(element);

    if (element._hv == "t" ) {
      
        txt = "<div  class='postholder' style='background-color:#c8a2c8 ;'>";
    } else {
        txt = "<div  class='postholder'>";

    }

    txt += "<div id='infoholder' class=" + element._uid + ">";

    if (element._hv == "t" ) {
      
        txt += "<img src='img/user.png' class='imageinfo' id='userpicture'  onclick='showprofilepicturediv(" + element._uid + ")'> <h3 id='name' style='font-style: italic;' class='nameinfo'>" + element._name + "</h3>";
    } else {
        txt += "<img src='img/user.png' class='imageinfo' id='userpicture'  onclick='showprofilepicturediv(" + element._uid + ")'> <h3 id='name' class='nameinfo'>" + element._name + "</h3>";

    }
    console.log(element.name);
    console.log(nomeglobale);
    if (element._name == nomeglobale) {
        console.log("nomiuguali");
        if (element._hv != "t" ) {
            txt += "<button id='infocarta' onclick='openInfoCarta(" + element._pid + ")'>HV</button>";

        } else {

    
        }

    }
    txt += "</div>";

    txt += "<div class='contentholder' id=" + element._pid + ">";
    if (element._type == "t") {
        //    console.log(element._content);
        txt += "<p id='textcontent'>" + element._content + "</p>";


    } else if (element._type == "i") {
        //   console.log(element._content);

        txt += "<img id='imgcontent' onclick='showimagediv(" + element._pid + ")' />";


    } else if (element._type == "l") {

        txt += "<button  class='btn btn-primary' id='openlocationdiv' onclick='openlocationdiv(" + element._lat + "," + element._lon + ")'>Open position</button>";
    }

    txt += "</div>"
    txt += "</div>"

    return txt;
}



/////////////////////////////////////////////////////
//     FULL SCREEN IMAGE
/////////////////////////////////////////////////////
function showimagediv(pidPost) {
    $("#channelpage").hide();
    $("#showimagediv").show();

    $("#showaddimage").hide();
    $("#showaddposition").hide();

    let query = 'SELECT * FROM ImageContent WHERE pid=?';
    db.executeSql(query, [pidPost], function (resultSet) {

        src = "data:image/png;base64," + resultSet.rows.item(0).content;
        $("#imagetotalscreen").attr("src", src);

    }, function (tx, error) {
        console.log(tx);
        console.log(error);
    });

    $('.backbutton').unbind("click").click(function () {
        console.log("torno da open image a channel");

        $('#imagetotalscreen').empty();

        $("#imagetotalscreen").attr("src", "");
        $("#showimagediv").hide();
        $("#channelpage").show();
    });

}

function showprofilepicturediv(uid) {
    $("#channelpage").hide();
    $("#showimagediv").show();

    $("#showaddimage").hide();
    $("#showaddposition").hide();

    let query = 'SELECT * FROM UserPicture WHERE uid=?';
    db.executeSql(query, [uid], function (resultSet) {

        src = "data:image/png;base64," + resultSet.rows.item(0).content;
        $("#imagetotalscreen").attr("src", src);

    }, function (tx, error) {
        console.log(tx);
        console.log(error);
    });

    $('.backbutton').unbind("click").click(function () {
        console.log("torno da open image a channel");

        $('#imagetotalscreen').empty();

        $("#imagetotalscreen").attr("src", "");
        $("#showimagediv").hide();
        $("#channelpage").show();
    });

}




/////////////////////////////////////////////////////
//     FULL SCREEN POSITION
/////////////////////////////////////////////////////
function openlocationdiv(latitudeToSend, longitudeToSend) {

    $("#channelpage").hide();
    $("#showlocationdiv").show();

    $("#showaddimage").hide();
    $("#showaddposition").hide();

    mapboxgl.accessToken = 'pk.eyJ1IjoiZHBlcnNvbmVuaSIsImEiOiJja2hha3dhb2owanQ0MzFwam44MWx3M2p1In0.j2kboFTenc7rda5RURJJWQ';
    var map = new mapboxgl.Map({
        container: 'map',
        style: 'mapbox://styles/mapbox/streets-v11',
        center: [parseFloat(longitudeToSend), parseFloat(latitudeToSend)],
        zoom: 9
    });

    var marker = new mapboxgl.Marker().setLngLat([parseFloat(longitudeToSend), parseFloat(latitudeToSend)]).addTo(map);



    $('.backbutton').unbind("click").click(function () {
        console.log("torno da open location a channel");

        $('#map').empty();

        $("#showlocationdiv").hide();
        $("#channelpage").show();


    });


}


//DATABASE 
function insertionError(error) {
    console.log("INSERT ERROR: " + error.message);
}
function insertionSuccess(tx, res) {
    console.log("INSERTION SUCCESS");
    console.log(tx);
    console.log(res);
}


// pk.eyJ1IjoiZHBlcnNvbmVuaSIsImEiOiJja2hha3dhb2owanQ0MzFwam44MWx3M2p1In0.j2kboFTenc7rda5RURJJWQ