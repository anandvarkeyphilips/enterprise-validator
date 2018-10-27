var initialData = `server:
    port: 8090
    servlet.context-path: /validator
logging:
  level.root: info
  file: /packages/logs/yaml-validator/yaml-validator.log
spring.pid.fail-on-write-error: true
spring.pid.file: /packages/config/yaml-validator/yaml-validator.pid`;

$(document).ready(function() {
    var errorLineNumber = 0;
    $('#validationResult').hide();
    var editor = ace.edit("editor");
    editor.setTheme("ace/theme/idle_fingers");
    var yamlMode = ace.require("ace/mode/yaml").Mode;
    editor.session.setMode(new yamlMode());
    editor.setValue(initialData, 0);
    editor.clearSelection;
    editor.focus();

    $("#encodeData").click(function() {
        editor.setValue(btoa(editor.getValue()));
    });
    $("#decodeData").click(function() {
        editor.setValue(atob(editor.getValue()));
    });
    $("#validateData").click(function() {
        $.ajax({
            url: 'yaml',
            type: "POST",
            data: JSON.stringify({
                yamlData: editor.getValue()
            }),
            contentType: "application/json",
            xhrFields: {
                withCredentials: false
            },
            success: function(data) {
                console.log("Success Operation validateData");
                console.log(data);
                if (data.validationMessage == "Valid YAML!!!") {
                    var newEditSession = editor.getSession();
                    newEditSession.removeGutterDecoration((errorLineNumber - 1), "failedGutter");
                    editor.setSession(newEditSession);
                    editor.focus();
                    $('#validationResult').show();
                    $('#validationResult').text('Valid YAML!!!');
                    $('#validationResult').css({
                        backgroundColor: 'green'
                    });
                    errorLineNumber = 0;
                } else {
                    $('#validationResult').show();
                    $('#validationResult').text(data.validationMessage);
                    $('#validationResult').css({
                        backgroundColor: 'red'
                    });
                    if (data.lineNumber > 0) {
                        errorLineNumber = data.lineNumber;
                        var newEditSession = editor.getSession();
                        newEditSession.addGutterDecoration((errorLineNumber - 1), "failedGutter");
                        editor.setSession(newEditSession);
                        editor.focus();
                        editor.gotoLine(data.lineNumber, data.columnNumber, true);
                    }
                }
            },
            error: function(data) {
                console.log("Failed Operation validateData");
                console.log(data);
            }
        });
    });
    $("#shareByEmail").click(function() {
        if (editor.getValue().length > 2000) {
            alert("The maximum Data Size for sharing by email is 2000 chars for the time being!")
            return false;
        }
        var link = "mailto:" +
            "?cc=anandvarkeyphilips@gmail.com" +
            "&subject=" + escape("YAML Data used for validation") +
            "&body=" + escape(editor.getValue());
        window.location.href = link;
    });
});