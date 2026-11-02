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
<header class="bg-dark text-white py-4 border-bottom border-secondary-subtle">
    <div class="container d-flex align-items-center gap-3">
        <img src="favicon-256.png" alt="Enterprise Validator logo" width="72" height="72" class="rounded-3 flex-shrink-0">
        <div>
            <p class="title h3 fw-semibold text-white mb-1">Enterprise Validator</p>
            <p class="text-white-50 mb-0">
                Now you can validate JSON and YAML easily!!<br/>
                A YAML validator better than the <a href="http://www.yamllint.com" class="link-info">YAML Lint</a>.<br/>
                A JSON validator and formatter that follows the JSON spec at <a href="http://json.org/" class="link-info">JSON.org</a>.
            </p>
        </div>
    </div>
</header>
<main class="container py-4 flex-grow-1">
    <div class="d-flex flex-wrap gap-2 mb-3" role="toolbar" aria-label="Editor actions">
        <button type="button" id="encodeData" class="btn btn-primary">BASE64 Encode</button>
        <button type="button" id="decodeData" class="btn btn-warning">BASE64 Decode</button>
        <button type="button" id="validateYamlData" class="btn btn-success">Validate YAML</button>
        <button type="button" id="validateJsonData" class="btn btn-success">Validate JSON</button>
        <button type="button" id="formatJsonData" class="btn btn-success">Format JSON</button>
        <button type="button" id="formatXmlData" class="btn btn-success">Format XML</button>
        <button type="button" id="shareByEmail" class="btn btn-info">Share Data</button>
    </div>
    <pre id="editor" class="m-0"></pre>
    <div id="validationResultBlock" tabindex="-1" class="mt-3"></div>
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
