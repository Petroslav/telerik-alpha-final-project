$(document).ready(function () {

    var url = $(location).attr('href');
    var parameter = url.substring(url.lastIndexOf('=') + 1);

    $('#sortOption').val(parameter);

    $('.home-icon').on('click', function () {
        $('html').animate({scrollTop: 0}, 'slow');
        return true;
    });
    $('.my-tag').on('click', function () {
        var tagText = $(this).children("span").html();
        console.log(tagText);

        $(location).attr('href', '/tag/' + tagText);


    });

    // var totalH = $('#stickyNav').offset().top;
    // $(window).bind('scroll', function () {
    //     var vPos = $(window).scrollTop();
    //
    //     if (totalH < vPos) {
    //         $('#stickyNav').css({
    //             'position': 'fixed',
    //             'top': 0,
    //             'bottom': 'auto'
    //         })
    //     } else {
    //         $('#stickyNav').css({
    //             'position': 'absolute',
    //             'top': ''
    //
    //         })
    //     }
    // });

    $('#searchButton').on('click', function () {
        var textfield = $('#searchField').val();

        if (textfield !== "") {
            var prefix = "";

            if ($('#searchOption').is(':checked')) {
                prefix = "user:"
            }

            $(location).attr('href', '/search?criteria=' + prefix + textfield);
        }
    });
    $('#searchField').keypress(function (e) {
        var textfield = $('#searchField').val();
        if (textfield === "") {
            return;
        }
        if (e.which === 13) {

            $(location).attr('href', '/search?criteria=' + textfield);
        }
    });
    $('#sortButtonNewest').on('click', function () {


        $(location).attr('href', '/newest?sort=' + $('#sortOption').val());

    });
    $('#sortButtonPopular').on('click', function () {


        $(location).attr('href', '/popular?sort=' + $('#sortOption').val());

    });
    $('#loginSubmit').on('click', function () {
        var snackbar = document.querySelector('.mdl-js-snackbar');
        var username = $('#loginUsername');
        var password = $('#loginPass');
        var data = {
            message: 'Invalid Username or Password!',
            timeout: 3000
        };
        if (isWrongLength(username) || isEmpty(username)) {
            data.message = 'Invalid Username!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        if (isWrongLength(password) || isEmpty(password)) {
            data.message = 'Invalid Password!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        $('#loginForm').submit();

    });

    var isEmpty = function (e) {
        return !e.val();

    };
    var isWrongLength = function (e) {
        var length = e.val().length;
        return length < 4 || length > 16;
    };
    $('#registerButton').on('click', function () {
        var snackbar = document.querySelector('.mdl-js-snackbar');
        var username = $('#registerUsername');
        var password = $('#registerPassword1');
        var password2 = $('#registerPassword2');
        var email = $('#registerEmail');
        var email_regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;

        var data = {
            message: 'Invalid Register info!',
            timeout: 3000
        };
        if (isEmpty(username) || isEmpty(email) || isEmpty(password)) {
            data.message = 'Please fill username, password and email to register!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        if (isWrongLength(username) || isEmpty(username)) {
            data.message = 'Invalid Username!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }

        if (isWrongLength(password) || isEmpty(password)) {
            data.message = 'Invalid Password!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        if (password.val() !== password2.val()) {
            data.message = 'Passwords dont match !!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        if (isWrongLength(email) || isEmpty(email) || email_regex.test($.trim(email.val()))) {
            data.message = 'Invalid Email!!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        $('#registerForm').submit();
    });

    $('#createButton').on('click', function () {
        var snackbar = document.querySelector('.mdl-js-snackbar');
        var name = $('#createName');
        var description = $('#createDescription');
        var repoUrl = $('#createRepositoryUrl');
        var version = $('#createVersion');
        var tags = $('#createTagString');
        var file = $('#createFile');
        var pic = $('#createPic');
        var data = {
            message: 'Invalid Register info!',
            timeout: 3000
        };
        if (isEmpty(name) || isEmpty(description) || isEmpty(repoUrl) || isEmpty(version) || isEmpty(tags)) {
            data.message = 'Please fill all of the fields !';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        if($.trim(name.val()).length < 201 || $.trim(name.val()).length > 1){
            data.message = 'Invalid extension name!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        if(description.val().length ===0 || description.val().length > 5000){
            data.message = 'Invalid description length!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        if(!isGitRepo($.trim(repoUrl.val()))){
            data.message = 'Invalid repository url!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        if(!file.val()){
            data.message = 'Upload a file!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        if(!pic.val()){
            data.message = 'Upload a picture!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        if(!checkExtension()){
            return;
        }
        $('#createForm').submit();
    });
});
function checkExtension() {
    var snackbar = document.querySelector('.mdl-js-snackbar');
    var data = {
        message: 'Invalid Register info!',
        timeout: 3000
    };
    var file = document.querySelector("#createPic");
    if ( /\.(jpe?g|png|gif)$/i.test(file.files[0].name) === false ) {
        data.message = 'Wrong picture format!';
        snackbar.MaterialSnackbar.showSnackbar(data);
        return false;
    }
    return true;
}
function isGitRepo(e){
    var githubPrefix = 'https://github.com/';
    if(!e.startsWith(githubPrefix)){
        return false;
    }
    e = e.substring(githubPrefix.length);
    var words = e.split('/');
    return words.length >= 2;
}
