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

/**
 * Same as updateResult, but appends a clickable mailto link and a hint for
 * browsers (usually embedded webviews) that cannot open the mail client.
 */
function updateResultWithMailLink(message, mailtoLink) {
  var block = document.getElementById('validationResultBlock');
  block.textContent = '';
  block.classList.remove('validation-message-success', 'validation-message-failure', 'validation-message-error');
  block.classList.add('validation-message-success');
  var text = document.createElement('span');
  text.textContent = message + ' ';
  var link = document.createElement('a');
  link.href = mailtoLink;
  link.textContent = 'Open the compose window';
  var hint = document.createElement('div');
  hint.style.fontSize = '0.9em';
  hint.style.opacity = '0.85';
  hint.textContent = 'Nothing opened? This browser cannot handle mail links (common in embedded browsers) - use Copy to clipboard instead.';
  block.appendChild(text);
  block.appendChild(link);
  block.appendChild(hint);
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
    if (navigator.clipboard?.writeText) {
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
    updateResultWithMailLink('Opening your mail client with the validation data - choose the recipients and press send.', link);
    window.location.href = link;
  });

  var diffMode = null;
  var diffModal = document.getElementById('diffModal');
  var compareInput = document.getElementById('compareInput');

  function openDiffModal(mode) {
    diffMode = mode;
    document.getElementById('diffTitle').textContent =
        mode === 'diffJson' ? 'Compare JSON documents' : 'Compare YAML documents';
    diffModal.classList.add('show');
    compareInput.focus();
  }

  function closeDiffModal() {
    diffModal.classList.remove('show');
    diffMode = null;
  }

  document.getElementById('diffJsonData').addEventListener('click', function() {
    openDiffModal('diffJson');
  });
  document.getElementById('diffYamlData').addEventListener('click', function() {
    openDiffModal('diffYaml');
  });
  document.getElementById('diffCancel').addEventListener('click', closeDiffModal);
  diffModal.addEventListener('click', function(event) {
    if (event.target === diffModal) {
      closeDiffModal();
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
        compareMessage: compareInput.value
      })
    }).then(function(response) {
      if (!response.ok) {
        throw new Error('HTTP ' + response.status);
      }
      return response.json();
    }).then(function(data) {
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
      closeDiffModal();
    }).catch(function() {
      updateResult('Technical failure possibly due to poor internet connectivity.', 'validation-message-error');
      closeDiffModal();
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
