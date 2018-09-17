var ws = new WebSocket("ws://localhost:9997/example/hello");
var text;
var btn;
var textarea;
var btnAgent;
var btnClient;
window.onload = function(){
    text = document.getElementById("text");
    btn = document.getElementById("btn");
    btnAgent = document.getElementById("agent");
    btnClient = document.getElementById("client");
    textarea = document.getElementById('messages');
    btnAgent.addEventListener("click",sendAgentRole);
    btnClient.addEventListener("click",sendClientRole)
    btn.addEventListener("click",sendMessage);
};

function sendMessage() {
    message = text.value;
    text.value = "";
    textarea.value = textarea.value +"\n"+ message;
    ws.send(message);
}

function sendAgentRole() {
    ws.send("/reg agent");
    hideRoleBtn();
}

function sendClientRole() {
    ws.send("/reg client");
    hideRoleBtn();
}


function hideRoleBtn(){
    btnClient.disabled = true;
    btnAgent.disabled = true;
}
ws.onmessage = function (ev) {
    textarea.value = textarea.value + "\n" + ev.data;
    console.log(ev.data);
};