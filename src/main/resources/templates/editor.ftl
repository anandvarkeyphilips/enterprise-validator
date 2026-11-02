<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Enterprise Validator</title>
    <link rel="icon" type="image/png" href="favicon.ico">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/editor.css">
    <script type="text/javascript" src="js/ace-noconflict/ace.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/ace-noconflict/mode-yaml.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/ace-noconflict/theme-idle_fingers.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/editor.js" defer></script>
</head>
<body class="d-flex flex-column min-vh-100">
<header class="bg-body-tertiary py-4 border-bottom">
    <div class="container d-flex align-items-center justify-content-center gap-3">
        <img src="favicon-256.png" alt="Enterprise Validator logo" width="72" height="72" class="flex-shrink-0">
        <div>
            <p class="title h3 fw-semibold text-body mb-1">Enterprise Validator</p>
            <p class="text-body-secondary mb-0">
                Now you can validate JSON and YAML easily!!<br/>
                A YAML validator better than the <a href="http://www.yamllint.com">YAML Lint</a>.<br/>
                A JSON validator and formatter that follows the JSON spec at <a href="http://json.org/">JSON.org</a>.
            </p>
        </div>
    </div>
</header>
<main class="container py-4 flex-grow-1 d-flex flex-column gap-3">
    <div class="d-flex flex-wrap gap-2 justify-content-center flex-shrink-0" role="toolbar" aria-label="Editor actions">
        <button type="button" id="encodeData" class="btn btn-primary">BASE64 Encode</button>
        <button type="button" id="decodeData" class="btn btn-warning">BASE64 Decode</button>
        <button type="button" id="validateYamlData" class="btn btn-success">Validate YAML</button>
        <button type="button" id="validateJsonData" class="btn btn-success">Validate JSON</button>
        <button type="button" id="formatJsonData" class="btn btn-info">Format JSON</button>
        <button type="button" id="formatXmlData" class="btn btn-info">Format XML</button>
        <button type="button" id="diffJsonData" class="btn btn-secondary">Diff JSON</button>
        <button type="button" id="diffYamlData" class="btn btn-secondary">Diff YAML</button>
        <button type="button" id="copyData" class="btn btn-outline-secondary">Copy</button>
    </div>
    <div id="editorRow" class="d-flex gap-3 flex-grow-1">
        <pre id="editor" class="m-0"></pre>
        <div id="comparePane" class="d-none flex-column gap-2 flex-grow-1">
            <div class="d-flex gap-2 align-items-center flex-shrink-0">
                <span id="compareTitle" class="small text-body-secondary flex-grow-1">Comparison document</span>
                <button type="button" id="diffRun" class="btn btn-sm btn-primary">Run Diff</button>
                <button type="button" id="diffClose" class="btn btn-sm btn-outline-secondary" aria-label="Close compare pane">&times;</button>
            </div>
            <pre id="compareEditor" class="m-0"></pre>
        </div>
    </div>
    <div id="validationResultBlock" tabindex="-1" class="flex-shrink-0"></div>
</main>
<footer class="py-3 text-center border-top">
    <p class="mb-0 text-body-secondary">
        Made with <span class="text-danger">&#9829;</span> in India by
        <a href="https://about.me/anandvarkeyphilips">Anand Varkey Philips</a>. The source code is
        <a href="https://raw.githubusercontent.com/anandvarkeyphilips/enterprise-validator/master/LICENSE">MIT</a>
        licensed and is available in <a href="https://github.com/anandvarkeyphilips/enterprise-validator">
        <img src="images/GitHub-Mark-32px.png" align="middle" style="width: 1.2em; height: 1.2em;" alt="GitHub"/></a>.
    </p>
</footer>
</body>
</html>
