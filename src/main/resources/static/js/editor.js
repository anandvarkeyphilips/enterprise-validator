var initialData = `server:
    port: 9090
    servlet.context-path: /validator
logging:
  level.root: info
  file: /packages/logs/yaml-validator/yaml-validator.log
spring.pid.fail-on-write-error: true
spring.pid.file: /packages/config/yaml-validator/yaml-validator.pid`;
var errorLineNumber = 0;
var editor, yamlMode;

$(document).ready(function() {
    $('#validationResult').hide();
    editor = ace.edit("editor");
    editor.setTheme("ace/theme/idle_fingers");
    yamlMode = ace.require("ace/mode/yaml").Mode;
    editor.session.setMode(new yamlMode());
        editor.setValue(initialData, 0);
    editor.clearSelection();

    $("#encodeData").click(function() {
        editor.setValue(btoa(editor.getValue()));
        editor.clearSelection();
    });
    $("#decodeData").click(function() {
        editor.setValue(atob(editor.getValue()));
        editor.clearSelection();
    });
    $("#validateYamlData").click(function() {
        ajaxCall("yaml","Valid YAML!!!");
    });
    $("#validateJsonData").click(function() {
        ajaxCall("json","Valid JSON!!!");
    });
    $("#formatJsonData").click(function() {
        ajaxCall("formatJson","Valid JSON!!!");
    });
    $("#formatXmlData").click(function() {
        ajaxCall("formatXml","Formatted XML!!!");
    });
    $("#shareByEmail").click(function() {
        if (editor.getValue().length > 2000) {
            alert("The maximum Data Size for sharing by email is 2000 chars for the time being!")
            return false;
        }
        var link = "mailto:" +
            "?cc=anandvarkeyphilips@gmail.com" +
            "&subject=" + escape("Data used for validation") +
            "&body=" + escape(editor.getValue());
        window.location.href = link;
    });
});

function ajaxCall(url,validationMessage) {
    $.ajax({
        url: url,
        type: "POST",
        data: JSON.stringify({
            inputMessage: editor.getValue()
        }),
        contentType: "application/json",
        success: function(data) {
            editor.setValue(data.inputMessage);
            console.log("Success Operation validateData");
            console.log(data);
            if (data.valid) {
                var newEditSession = editor.getSession();
                newEditSession.removeGutterDecoration((errorLineNumber - 1), "failedGutter");
                editor.setSession(newEditSession);
                editor.clearSelection();
                editor.focus();
                $('#validationResult').show();
                $('#validationResult').text(data.validationMessage);
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
                    editor.clearSelection();
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
}