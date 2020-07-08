<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>Enterprise Validator</title>
    <link rel="icon" type="image/png" href="favicon.ico">
    <link rel="stylesheet" href="css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/bulma.css">
    <link rel="stylesheet" href="css/editor.css">
    <script type="text/javascript" src="js/jquery-3.3.1.min.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/editor.js"></script>
    <script type="text/javascript" src="js/ace-noconflict/ace.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/ace-noconflict/mode-yaml.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/ace-noconflict/theme-idle_fingers.js" charset="utf-8"></script>
</head>
<body>
<section class="hero is-light">
    <div class="hero-body">
        <div class="container columns">
            <figure class="image is-128x128">
                <img src="favicon-256.png">
            </figure>
            <div style="padding-left: 10px;">
                <p class="title">Enterprise Validator</p>
                <p class="subtitle">
                    Now you can validate JSON and YAML easily!!<br/>
                    A YAML validator better than the <a href="http://www.yamllint.com">YAML Lint</a>.<br/>
                    A JSON validator and formatter that follows the JSON spec at <a href="http://json.org/">JSON.org</a>.
                </p>
            </div>
        </div>
    </div>
</section>
<section class="section">
    <div class="container">
        <form class="form-inline" data-toggle="validator" role="form">
            <button type="button" id="encodeData" class="btn btn-primary">BASE64 Encode</button>
            <button type="button" id="decodeData" class="btn btn-warning">BASE64 Decode</button>
            <button type="button" id="validateYamlData" class="btn btn-success">Validate YAML</button>
            <button type="button" id="validateJsonData" class="btn btn-success">Validate JSON</button>
            <button type="button" id="formatJsonData" class="btn btn-success">Format JSON</button>
            <button type="button" id="formatXmlData" class="btn btn-success">Format XML</button>
            <button type="button" id="shareByEmail" class="btn btn-info">Share Data</button>
            <div class="form-group" style="min-width: 100%">
                    <pre id="editor" style="height:75vh;">
                    </pre>
            </div>
            <div id="validationResultBlock" tabindex="-1"/>
        </form>
    </div>
</section>
</body>
<footer class="footer">
    <div class="content has-text-centered">
        <p>
            Made with <span style="color: #e25555;">&#9829;</span> in India by
            <a href="https://about.me/anandvarkeyphilips">Anand Varkey Philips</a>. The source code is
            <a href="https://raw.githubusercontent.com/anandvarkeyphilips/enterprise-validator/master/LICENSE">MIT</a>
            licensed and is available in <a href="https://github.com/anandvarkeyphilips/enterprise-validator">
            <img src="images/GitHub-Mark-32px.png" align="middle" style="width: 1.2em; height: 1.2em;"/></a>.
        </p>
    </div>
</footer>
</html>