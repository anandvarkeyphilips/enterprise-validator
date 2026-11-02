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

function updateResult(message, className) {
  var block = document.getElementById('validationResultBlock');
  block.textContent = message;
  block.classList.remove('validation-message-success', 'validation-message-failure', 'validation-message-error');
  block.classList.add(className);
  block.focus();
}

document.addEventListener('DOMContentLoaded', function() {
  editor = ace.edit('editor');
  editor.setTheme('ace/theme/idle_fingers');
  yamlMode = ace.require('ace/mode/yaml').Mode;
  editor.session.setMode(new yamlMode());
  editor.setValue(initialData, 0);
  editor.clearSelection();

  document.getElementById('encodeData').addEventListener('click', function() {
    ajaxCall('base64Encode');
  });
  document.getElementById('decodeData').addEventListener('click', function() {
    ajaxCall('base64Decode');
  });
  document.getElementById('validateYamlData').addEventListener('click', function() {
    ajaxCall('yaml');
  });
  document.getElementById('validateJsonData').addEventListener('click', function() {
    ajaxCall('json');
  });
  document.getElementById('formatJsonData').addEventListener('click', function() {
    ajaxCall('formatJson');
  });
  document.getElementById('formatXmlData').addEventListener('click', function() {
    ajaxCall('formatXml');
  });
  var shareButton = document.getElementById('shareByEmail');
  var shareMenu = document.getElementById('shareMenu');

  function closeShareMenu() {
    shareMenu.classList.remove('show');
    shareButton.setAttribute('aria-expanded', 'false');
  }

  shareButton.addEventListener('click', function(event) {
    event.stopPropagation();
    var opening = !shareMenu.classList.contains('show');
    shareMenu.classList.toggle('show', opening);
    shareButton.setAttribute('aria-expanded', opening ? 'true' : 'false');
  });
  document.addEventListener('click', function(event) {
    if (!shareMenu.contains(event.target) && !shareButton.contains(event.target)) {
      closeShareMenu();
    }
  });
  document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape') {
      closeShareMenu();
    }
  });

  document.getElementById('shareCopy').addEventListener('click', function() {
    closeShareMenu();
    var data = editor.getValue();
    if (navigator.clipboard && navigator.clipboard.writeText) {
      navigator.clipboard.writeText(data).then(function() {
        updateResult('Validation data copied to the clipboard.', 'validation-message-success');
      }).catch(function() {
        updateResult('Copying to the clipboard was blocked by the browser. Use the email option instead.',
            'validation-message-error');
      });
    } else {
      updateResult('The clipboard is not available in this browser. Use the email option instead.',
          'validation-message-error');
    }
  });

  document.getElementById('shareEmail').addEventListener('click', function() {
    closeShareMenu();
    var data = editor.getValue();
    if (data.length > 2000) {
      alert('The maximum Data Size for sharing by email is 2000 chars for the time being!')
      return false;
    }
    var link = 'mailto:' +
      '?subject=' + encodeURIComponent('Data used for validation') +
      '&body=' + encodeURIComponent(data);
    updateResult('Opening your mail client with the validation data. Choose the recipients and press send.',
        'validation-message-success');
    window.location.href = link;
  });
});

function ajaxCall(url) {
  var newEditSession = editor.getSession();
  newEditSession.removeGutterDecoration((errorLineNumber - 1), 'failedGutter');
  editor.setSession(newEditSession);
  errorLineNumber = 0;
  fetch(url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ inputMessage: editor.getValue() })
  }).then(function(response) {
    if (!response.ok) {
      throw new Error('HTTP ' + response.status);
    }
    return response.json();
  }).then(function(data) {
    editor.setValue(data.inputMessage);
    if (data.valid) {
      updateResult(data.validationMessage, 'validation-message-success');
    } else {
      updateResult(data.validationMessage, 'validation-message-failure');
      if (data.lineNumber > 0) {
        errorLineNumber = data.lineNumber;
        var session = editor.getSession();
        session.addGutterDecoration((errorLineNumber - 1), 'failedGutter');
        editor.setSession(session);
        editor.gotoLine(data.lineNumber, data.columnNumber, true);
      }
    }
    editor.clearSelection();
  }).catch(function() {
    updateResult('Technical failure possibly due to poor internet connectivity.', 'validation-message-error');
  });
}
