
/*
var Model1  = (function() {
    var channels;
    var posts;
    var users;

    var  instance;

    class Model1 {
        constructor() {
            this.channels = new Array();
            this.posts = new Array();
            this.users = new Array(10, 20, 30);
        }

        getUserSize() {
            return users.length;
        }

    };

   
return {
    getInstance:  function() {
        if (instance == null) {
            instance = new Model1();
            instance.constructor = null;
        }
        return instance;
    }
};
        

})();

var test = Model.getInstance();
console.log(test.getUserSize());



class Model {
    static instance;
    constructor() {
        if(instance) {
            this._users= [];
            Model.instance = this;
        }
      return Model.instance;
    }

    addItem(item) {
        this._users.push(item);
    }

    getUsersSize() {
        return this._users.length;
    }

}

const instance = new Model();
instance.addItem(30);
instance.addItem(20);
console.log(instance.getUsersSize());*/

class Post {
    constructor(uid, name, pversion, pid, type, hv) {
        this._uid = uid;
        this._name = name;
        this._pversion = pversion;
        this._pid = pid;
        this._type = type;
        this._hv = hv;
    }
}

class PostText extends Post {
    constructor(uid, name, pversion, pid, type, hv ,content) {
        super(uid, name, pversion, pid, type, hv);
        this._content = content;
    }
}
class PostImage extends Post {
    constructor(uid, name, pversion, pid, type, hv ,content) {
        super(uid, name, pversion, pid, type, hv);
        this._content = content;
    }

    getImageContent() {
        return this._content;
    }
}
class PostLocation extends Post {
    constructor(uid, name, pversion, pid, type, hv ,lat, lon) {
        super(uid, name, pversion, pid, type, hv);
        this._lat = lat;
        this._lon = lon;
    }
}



class Model {
    static _instance = new Model();
    static getInstance() {
        return this._instance;
    }

    constructor() {
        this._channels = new Array();
        this._posts = Post.prototype.posts;
        
    }

    addChannel(channel) {
        this._channels.push(channel);
    }

    addPost(post) {
        this._posts.push(post);
    }

    clearChannel() {
        this._posts = [];
    }

    printAllPosts() {
        console.log(this._posts.toString());
    }

}

