document.body.onload = function() {
    function enter(username) {
        function setStartVars(resp) {
            usernameId = resp.currentUserId;
            messageToken = resp.messageToken;
            messageEditToken = resp.messageEditToken;
            messageDeleteToken = resp.messageDeleteToken;
            userToken = resp.userToken;
            userChangeToken = resp.userChangeToken;
            textarea.focus();

            firstUpdateRequest = true;
            startGettingMessages();
        }
        var params = '?type=BASE_REQUEST&username=' + username;
        ajax('GET', address + params, null, setStartVars);
    }

    var name = getCookie('nickname');
    enter(name);

    //showUsernameForm();
};

function startGettingMessages() {
    gettingMessages = true;
    doGet();
}

function stopGettingMessages() {
    gettingMessages = false;
}

function doGet() {
    var params = 	'?type=GET_UPDATE' +
        '&messageToken=' + messageToken +
        '&messageEditToken=' + messageEditToken +
        '&messageDeleteToken=' + messageDeleteToken +
        '&userToken=' + userToken +
        '&userChangeToken=' + userChangeToken;

    if(firstUpdateRequest){
        params += '&firstUpdateRequest=true';
        firstUpdateRequest = false;
    }
    else{
        params += '&firstUpdateRequest=false';
    }

    ajax('GET', address + params, null, updateMsgs);
}

function setMessageText(messageId, text) {
    get(messageId).getElementsByClassName('message-text')[0].innerHTML = text;
}

function drawMessage(message) {
    var messageNode = new MessageNode(message);

    messages.insertBefore(messageNode, emptyDiv);
    messageNode.scrollIntoView();
}

function clearMessageContainer() {
    while(messages.firstElementChild != emptyDiv) {
        messages.removeChild(messages.firstElementChild);
    }
}

function updateMsgs(resp) {
    if(resp.userToken) {
        userToken = resp.userToken;
        JSON.parse(resp.users).forEach(function(user) {
            users[user.userId] = {
                "username":user.username,
                "userImage":user.userImage
            };

            if(usernameId == user.userId) {
                if(user.username) {
                    setUsername(user.username);
                }
                if(user.userImage) {
                    get('img').style.backgroundImage = 'url(' + user.userImage + ')';
                }
            }
        });
    }

    if(resp.userChangeToken) {
        userChangeToken = resp.userChangeToken;
        JSON.parse(resp.changedUsers).forEach(function(user) {
            users[user.userId] = {
                "username":user.username,
                "userImage":user.userImage
            };
            if(usernameId == user.userId) {
                if(user.username) {
                    setUsername(user.username);
                }
                if(user.userImage) {
                    get('img').style.backgroundImage = 'url(' + user.userImage + ')';
                    var divs = document.querySelectorAll('[usernameId="' + user.userId + '"]');
                    divs.forEach = [].forEach;
                    divs.forEach(function(div) {
                        div.style.backgroundImage = 'url(' + user.userImage + ')';
                    });
                }
            }
        });
    }

    if(resp.messageToken) {
        messageToken = resp.messageToken;
        JSON.parse(resp.messages).forEach(function(message) {
            drawMessage(message);
        });
    }

    if(resp.messageEditToken) {
        messageEditToken = resp.messageEditToken;
        JSON.parse(resp.editedMessages).forEach(function(editing) {
            setMessageText(editing.messageId, editing.messageText);
        });
    }

    if(resp.messageDeleteToken) {
        messageDeleteToken = resp.messageDeleteToken;
        JSON.parse(resp.deletedMessagesIds).forEach(function(id) {
            makeMessageDeleted(id);
        });
    }

    if(resp.userChangeToken) {
        userChangeToken = resp.userChangeToken;
        JSON.parse(resp.changedUsers).forEach(function(user) {
            users[user.userId] = {
                "username":user.username,
                "userImage":user.userImage
            };
            if(usernameId == user.userId) {
                if(user.username) {
                    setUsername(user.username);
                }
                if(user.userImage) {

                }
            }
        });
    }

    if(gettingMessages){
        setTimeout(doGet, 0);
    }
}