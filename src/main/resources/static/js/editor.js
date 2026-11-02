var errorLineNumber = 0;
var editor, compareEditor, yamlMode, aceRange;
var baseMarkerIds = [];
var compareMarkerIds = [];

var SAMPLES = {
  yaml: `server:
  port: 8090
  servlet.context-path: /enterprise-validator
logging:
  level.root: info
  level.io.exnihilo: debug
spring.pid.fail-on-write-error: true
spring.pid.file: /packages/config/enterprise-validator/enterprise-validator.pid`,
  json: `{
    "server": {
        "port": 8090
    },
    "logging": {
        "level": "info"
    }
}`,
  xml: `<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <server>
        <port>8090</port>
    </server>
    <logging level="info"/>
</configuration>`,
  text: 'Enterprise Validator'
};

function updateResult(message, className) {
  var block = document.getElementById('validationResultBlock');
  block.textContent = message;
  block.classList.remove('validation-message-success', 'validation-message-failure', 'validation-message-error');
  block.classList.add(className);
  block.focus();
}

function clearDiffMarkers() {
  baseMarkerIds.forEach(function(id) {
    editor.session.removeMarker(id);
  });
  baseMarkerIds = [];
  if (compareEditor) {
    compareMarkerIds.forEach(function(id) {
      compareEditor.session.removeMarker(id);
    });
    compareMarkerIds = [];
  }
}

/* Every action starts from a clean slate: no stale error gutter, no leftover diff
   highlights, no previous result banner. */
function resetVisualState() {
  if (errorLineNumber > 0) {
    editor.session.removeGutterDecoration(errorLineNumber - 1, 'failedGutter');
    errorLineNumber = 0;
  }
  clearDiffMarkers();
  var block = document.getElementById('validationResultBlock');
  block.textContent = '';
  block.classList.remove('validation-message-success', 'validation-message-failure', 'validation-message-error');
}

function ensureSample(kind) {
  if (!editor.getValue().trim()) {
    editor.setValue(SAMPLES[kind], 0);
    editor.clearSelection();
  }
}

/* Line-level LCS diff: returns the base lines only present in the base document and the
   compare lines only present in the comparison document. Used purely for visual
   highlighting; the semantic key diff comes from the server. */
function lineDiff(baseLines, compareLines) {
  var rows = baseLines.length;
  var cols = compareLines.length;
  var table = [];
  for (var r = 0; r <= rows; r++) {
    table.push(new Array(cols + 1).fill(0));
  }
  for (var r = rows - 1; r >= 0; r--) {
    for (var c = cols - 1; c >= 0; c--) {
      table[r][c] = baseLines[r] === compareLines[c] ? table[r + 1][c + 1] + 1
          : Math.max(table[r + 1][c], table[r][c + 1]);
    }
  }
  var removed = [];
  var added = [];
  var r = 0;
  var c = 0;
  while (r < rows && c < cols) {
    if (baseLines[r] === compareLines[c]) {
      r++;
      c++;
    } else if (table[r + 1][c] >= table[r][c + 1]) {
      removed.push(r);
      r++;
    } else {
      added.push(c);
      c++;
    }
  }
  while (r < rows) {
    removed.push(r);
    r++;
  }
  while (c < cols) {
    added.push(c);
    c++;
  }
  return { removed: removed, added: added };
}

function highlightLineDifferences() {
  if (!compareEditor) {
    return;
  }
  var baseLines = editor.getValue().split('\n');
  var compareLines = compareEditor.getValue().split('\n');
  if (baseLines.length * compareLines.length > 250000) {
    return;
  }
  var result = lineDiff(baseLines, compareLines);
  result.removed.forEach(function(row) {
    baseMarkerIds.push(editor.session.addMarker(new aceRange(row, 0, row, 1), 'diff-highlight-removed', 'fullLine'));
  });
  result.added.forEach(function(row) {
    compareMarkerIds.push(compareEditor.session.addMarker(new aceRange(row, 0, row, 1), 'diff-highlight-added', 'fullLine'));
  });
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
    highlightLineDifferences();
  }
  block.focus();
}

document.addEventListener('DOMContentLoaded', function() {
  editor = ace.edit('editor');
  editor.setTheme('ace/theme/idle_fingers');
  yamlMode = ace.require('ace/mode/yaml').Mode;
  aceRange = ace.require('ace/range').Range;
  editor.session.setMode(new yamlMode());
  editor.setValue(SAMPLES.yaml, 0);
  editor.clearSelection();

  function actionButton(id, kind, url) {
    document.getElementById(id).addEventListener('click', function() {
      resetVisualState();
      ensureSample(kind);
      if (url) {
        ajaxCall(url);
      }
    });
  }

  actionButton('encodeData', 'text', 'base64Encode');
  actionButton('decodeData', 'text', 'base64Decode');
  actionButton('validateYamlData', 'yaml', 'yaml');
  actionButton('validateJsonData', 'json', 'json');
  actionButton('formatJsonData', 'json', 'formatJson');
  actionButton('formatXmlData', 'xml', 'formatXml');

  document.getElementById('copyData').addEventListener('click', function() {
    resetVisualState();
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
  var compareEditorElement = document.getElementById('compareEditor');
  var diffBar = document.getElementById('diffBar');

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
    resetVisualState();
    diffMode = mode;
    ensureSample(mode === 'diffJson' ? 'json' : 'yaml');
    ensureCompareEditor();
    document.getElementById('compareTitle').textContent =
        mode === 'diffJson' ? 'Base document (left) vs comparison document (right) - JSON key diff'
            : 'Base document (left) vs comparison document (right) - YAML key diff';
    if (!compareEditor.getValue().trim()) {
      compareEditor.setValue(editor.getValue(), 0);
      compareEditor.clearSelection();
    }
    compareEditorElement.classList.remove('d-none');
    diffBar.classList.remove('d-none');
    compareEditor.focus();
  }

  function closeComparePane() {
    resetVisualState();
    diffMode = null;
    compareEditorElement.classList.add('d-none');
    diffBar.classList.add('d-none');
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
    resetVisualState();
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
        editor.session.addGutterDecoration((errorLineNumber - 1), 'failedGutter');
        editor.gotoLine(data.lineNumber, data.columnNumber, true);
      }
    }
    editor.clearSelection();
  }).catch(function() {
    updateResult('Technical failure possibly due to poor internet connectivity.', 'validation-message-error');
  });
}
