var errorLineNumber = 0;
var editor, yamlMode;
var initialData = `server:
  port: 8090
  servlet.context-path: /enterprise-validator
logging:
  level.root: info
  level.io.exnihilo: debug
  file: /packages/logs/enterprise-validator/enterprise-validator.log
spring.pid.fail-on-write-error: true
spring.pid.file: /packages/config/enterprise-validator/enterprise-validator.pid

management:
  endpoints:
    web.exposure.include: "*"
    web.exposure.exclude: loggers
  endpoint:
    shutdown.enabled: true`;

$(document).ready(function() {
    editor = ace.edit("editor");
    editor.setTheme("ace/theme/idle_fingers");
    yamlMode = ace.require("ace/mode/yaml").Mode;
    editor.session.setMode(new yamlMode());
        editor.setValue(initialData, 0);
    editor.clearSelection();

    $("#encodeData").click(function() {
        ajaxCall("base64Encode");
    });
    $("#decodeData").click(function() {
        ajaxCall("base64Decode");
    });
    $("#validateYamlData").click(function() {
        ajaxCall("yaml");
    });
    $("#validateJsonData").click(function() {
        ajaxCall("json");
    });
    $("#formatJsonData").click(function() {
        ajaxCall("formatJson");
    });
    $("#formatXmlData").click(function() {
        ajaxCall("formatXml");
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

function ajaxCall(url) {
    var newEditSession = editor.getSession();
    newEditSession.removeGutterDecoration((errorLineNumber - 1), "failedGutter");
    editor.setSession(newEditSession);
    errorLineNumber = 0;
    $.ajax({
        url: url,
        type: "POST",
        data: JSON.stringify({
            inputMessage: editor.getValue()
        }),
        contentType: "application/json",
        success: function(data) {
            editor.setValue(data.inputMessage);
            if (data.valid) {
                $('#validationResultBlock').text(data.validationMessage);
                $('#validationResultBlock').removeClass("validation-message-failure validation-message-error");
                $('#validationResultBlock').addClass("validation-message-success");
                editor.clearSelection();
                $('#validationResultBlock').focus();
            } else {
                $('#validationResultBlock').text(data.validationMessage);
                $('#validationResultBlock').removeClass( "validation-message-success validation-message-error");
                $('#validationResultBlock').addClass( "validation-message-failure" );
                if (data.lineNumber > 0) {
                    errorLineNumber = data.lineNumber;
                    var newEditSession = editor.getSession();
                    newEditSession.addGutterDecoration((errorLineNumber - 1), "failedGutter");
                    editor.setSession(newEditSession);
                    editor.gotoLine(data.lineNumber, data.columnNumber, true);
                }
                editor.clearSelection();
                $('#validationResultBlock').focus();
            }
        },
        error: function(data) {
            $('#validationResultBlock').text("Technical failure possibly due to poor internet connectivity.");
            $('#validationResultBlock').removeClass("validation-message-success validation-message-failure");
            $('#validationResultBlock').addClass("validation-message-error");
        }
    });
}