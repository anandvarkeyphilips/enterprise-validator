<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>Enterprise Test Suite</title>
    <link rel="icon" type="image/png" href="favicon.ico">
    <link rel="stylesheet" href="css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/bulma.css">
    <link rel="stylesheet" href="css/forEditor.css">
    <script type="text/javascript" src="js/jquery-3.3.1.min.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/forEditor.js"></script>
    <script type="text/javascript" src="js/ace-noconflict/ace.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/ace-noconflict/mode-yaml.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/ace-noconflict/theme-idle_fingers.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/ace-noconflict/theme-idle_fingers.js" charset="utf-8"></script>
</head>
<body>
<section class="hero is-light">
    <div class="hero-body">
        <div class="container">
            <p class="title">YAML Validator</p>
            <p class="subtitle">
            </p>
        </div>
    </div>
</section>
<section class="section">
    <div class="container">
        <form class="form-inline" data-toggle="validator" role="form">
            <button type="button" id="encodeData" class="btn btn-primary">BASE64 Encode</button>
            <button type="button" id="decodeData" class="btn btn-warning">BASE64 Decode</button>
            <button type="button" id="validateData" class="btn btn-success">Validate YAML</button>
            <button type="button" id="shareByEmail" class="btn btn-info">Share YAML</button>
            <div class="form-group" style="min-width: 100%">
                    <pre id="editor" style="height:75vh;">
                    </pre>
            </div>
            <div id="validationResult" style="color:white;width:100%;padding:10px;">
            </div>
        </form>
    </div>
</section>
</body>
<footer class="footer">
    <div class="container">
        <div class="content has-text-centered">
            <p>
                Made with <span style="color: #e25555;">&#9829;</span> in India by <a href="https://about.me/anandvarkeyphilips">Anand Varkey
                Philips</a>. The source code is
                <a href="http://opensource.org/licenses/mit-license.php">MIT</a> licensed. The website content
                is licensed under <a href="http://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY NC SA 4.0</a>.
            </p>
        </div>
    </div>
</footer>
</html>