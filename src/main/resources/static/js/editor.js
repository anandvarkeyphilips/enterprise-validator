var errorLineNumber = 0;
var editor, compareEditor, yamlMode;
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

function renderDiffResult(data) {
  var block = document.getElementById('validationResultBlock');
  block.textContent = '';
  block.classList.remove('validation-message-success', 'validation-message-failure', 'validation-message-error');
  if (!data.valid) {
    block.classList.add('validation-message-failure');
    block.textContent = data.validationMessage;
  } else if (data.identical) {
    block.classList.add('validation-message-success');
    block.textContent = data.validationMessage;
  } else {
    block.classList.add('validation-message-failure');
    var summary = document.createElement('span');
    summary.className = 'diff-line';
    summary.textContent = data.validationMessage;
    block.appendChild(summary);
    (data.differences || []).forEach(function(line) {
      var item = document.createElement('span');
      item.className = 'diff-line';
      item.textContent = line;
      block.appendChild(item);
    });
  }
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

  document.getElementById('copyData').addEventListener('click', function() {
    var data = editor.getValue();
    if (navigator.clipboard?.writeText) {
      navigator.clipboard.writeText(data).then(function() {
        updateResult('Editor content copied to the clipboard.', 'validation-message-success');
      }).catch(function() {
        updateResult('Copying to the clipboard was blocked by the browser.', 'validation-message-error');
      });
    } else {
      updateResult('The clipboard is not available in this browser.', 'validation-message-error');
    }
  });

  var diffMode = null;
  var comparePane = document.getElementById('comparePane');

  function ensureCompareEditor() {
    if (compareEditor) {
      return;
    }
    compareEditor = ace.edit('compareEditor');
    compareEditor.setTheme('ace/theme/idle_fingers');
    compareEditor.session.setMode(new yamlMode());
    compareEditor.setValue('', 0);
    compareEditor.clearSelection();
  }

  function openComparePane(mode) {
    diffMode = mode;
    ensureCompareEditor();
    document.getElementById('compareTitle').textContent =
        mode === 'diffJson' ? 'Comparison document (JSON diff)' : 'Comparison document (YAML diff)';
    comparePane.classList.remove('d-none');
    comparePane.classList.add('d-flex');
    compareEditor.focus();
  }

  function closeComparePane() {
    diffMode = null;
    comparePane.classList.add('d-none');
    comparePane.classList.remove('d-flex');
  }

  document.getElementById('diffJsonData').addEventListener('click', function() {
    openComparePane('diffJson');
  });
  document.getElementById('diffYamlData').addEventListener('click', function() {
    openComparePane('diffYaml');
  });
  document.getElementById('diffClose').addEventListener('click', closeComparePane);
  document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape' && diffMode) {
      closeComparePane();
    }
  });

  document.getElementById('diffRun').addEventListener('click', function() {
    if (!diffMode) {
      return;
    }
    fetch(diffMode, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        inputMessage: editor.getValue(),
        compareMessage: compareEditor.getValue()
      })
    }).then(function(response) {
      if (!response.ok) {
        throw new Error('HTTP ' + response.status);
      }
      return response.json();
    }).then(renderDiffResult).catch(function() {
      updateResult('Technical failure possibly due to poor internet connectivity.', 'validation-message-error');
    });
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
