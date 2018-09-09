$(document).ready(function () {


    if ($('.sorting-list').length) {
        var url = $(location).attr('href');
        var parameter = url.substring(url.lastIndexOf('=') + 1);

        var redirectOption = url.includes('newest') ? 'newest?' : 'popular?';
        if (url.includes('search')) {
            redirectOption = url.substring(url.lastIndexOf('/') +1 , url.lastIndexOf('&') + 1)
        }
        if (parameter.lastIndexOf('/') === parameter.length - 1) {
            parameter = parameter.substring(0, parameter.length - 1);
        }

        var option = $('#' + parameter).html();
        $('#sortOption').val(option);
        $('#byDownloads').on('click', function () {
            $(location).attr('href', '/' + redirectOption + 'sort=byDownloads')
        });
        $('#byLastCommit').on('click', function () {
            $(location).attr('href', '/' + redirectOption + 'sort=byLastCommit')
        });
        $('#byUploadDate').on('click', function () {
            $(location).attr('href', '/' + redirectOption + 'sort=byUploadDate')
        });
    }

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

            $(location).attr('href', '/search?criteria=' + textfield+'&sort=byDownloads');
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
        if (isWrongLength(email) || isEmpty(email) || !email_regex.test($.trim(email.val()))) {
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
        if ($.trim(name.val()).length > 201 || $.trim(name.val()).length < 1) {
            data.message = 'Invalid extension name!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        if (description.val().length === 0 || description.val().length > 5000) {
            data.message = 'Invalid description length!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        if (!isGitRepo($.trim(repoUrl.val()))) {
            data.message = 'Invalid repository url!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        if (!file.val()) {
            data.message = 'Upload a file!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        if (!pic.val()) {
            data.message = 'Upload a picture!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        if (!checkExtension()) {
            return;
        }
        $('#createForm').submit();
    });

    $('#userEditButton').on('click', function () {
        var snackbar = document.querySelector('.mdl-js-snackbar');
        var password = $('#editNewPass');
        var password2 = $('#editNewPass2');

        var data = {
            message: 'Invalid Edit info!',
            timeout: 3000
        };
        if (!isEmpty(password) && isWrongLength(password)) {
            data.message = 'Invalid Password!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        if (!isEmpty(password2) && isWrongLength(password2)) {
            data.message = 'Invalid Repeated Password!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        if (password.val() !== password2.val()) {
            data.message = 'Passwords dont match !!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        if ($('#userEditPicBtn').val() && !checkExtensionUserEdit()) {
            return;
        }
        $('#userEditForm').submit();
    });
    $('#extensionEditButton').on('click', function () {
        var snackbar = document.querySelector('.mdl-js-snackbar');
        var name = $('#extensionEditName');
        var description = $('#extensionEditDescription');
        var repoUrl = $('#extensionEditRepo');
        var version = $('#extensionEditVersion');
        var tags = $('#extensionEditTags');
        var file = $('#extensionEditFileBtn');
        var pic = $('#extensionEditPictureBtn');
        var data = {
            message: 'Invalid Register info!',
            timeout: 3000
        };
        if (isEmpty(name) || isEmpty(description) || isEmpty(repoUrl) || isEmpty(version) || isEmpty(tags)) {
            data.message = 'Please fill all of the fields !';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        if ($.trim(name.val()).length > 201 || $.trim(name.val()).length < 1) {
            data.message = 'Invalid extension name!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        if (description.val().length === 0 || description.val().length > 5000) {
            data.message = 'Invalid description length!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        if (!isGitRepo($.trim(repoUrl.val()))) {
            data.message = 'Invalid repository url!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        // if (!file.val()) {
        //     data.message = 'Upload a file!';
        //     snackbar.MaterialSnackbar.showSnackbar(data);
        //     return;
        // }
        // if (!pic.val()) {
        //     data.message = 'Upload a picture!';
        //     snackbar.MaterialSnackbar.showSnackbar(data);
        //     return;
        // }
        if (pic.val() && !checkExtensionForExtensionEdit()) {
            return;
        }
        $('#extensionEditForm').submit();
    });
    if ($('#propertiesForm').length) {
        'use strict';
        var dialogButton = document.querySelector('#dialog-button');
        var dialog = document.querySelector('#dialog');
        if (!dialog.showModal) {
            dialogPolyfill.registerDialog(dialog);
        }
        dialogButton.addEventListener('click', function () {
            dialog.showModal();
        });
        dialog.querySelector('button:not([disabled])')
            .addEventListener('click', function () {
                dialog.close();
            });
    }
});

function checkExtension() {
    var snackbar = document.querySelector('.mdl-js-snackbar');
    var data = {
        message: 'Invalid Register info!',
        timeout: 3000
    };
    var file = document.querySelector("#createPicBtn");
    if (/\.(jpe?g|png|gif)$/i.test(file.files[0].name) === false) {
        data.message = 'Wrong picture format!';
        snackbar.MaterialSnackbar.showSnackbar(data);
        return false;
    }
    return true;
}

function checkExtensionUserEdit() {
    var snackbar = document.querySelector('.mdl-js-snackbar');
    var data = {
        message: 'Invalid Register info!',
        timeout: 3000
    };
    var file = document.querySelector('#userEditPicBtn');
    if (/\.(jpe?g|png|gif)$/i.test(file.files[0].name) === false) {
        data.message = 'Wrong picture format!';
        snackbar.MaterialSnackbar.showSnackbar(data);
        return false;
    }
    return true;
}

function checkExtensionForExtensionEdit() {
    var snackbar = document.querySelector('.mdl-js-snackbar');
    var data = {
        message: 'Invalid Register info!',
        timeout: 3000
    };
    var file = document.querySelector('#extensionEditPictureBtn');
    if (/\.(jpe?g|png|gif)$/i.test(file.files[0].name) === false) {
        data.message = 'Wrong picture format!';
        snackbar.MaterialSnackbar.showSnackbar(data);
        return false;
    }
    return true;
}

function isGitRepo(e) {
    var githubPrefix = 'https://github.com/';
    if (!e.startsWith(githubPrefix)) {
        return false;
    }
    e = e.substring(githubPrefix.length);
    var words = e.split('/');
    return words.length >= 2;
}

function checkIfPasswordsMatchRegister() {
    var pass1jq = $('#registerPassword1');
    var pass2jq = $('#registerPassword2');

    var pass1 = $.trim(pass1jq.val());
    var pass2 = $.trim(pass2jq.val());

    if (pass1 === pass2) {
        pass1jq.css('border-color', 'green');
        pass2jq.css('border-color', 'green');
    }
    else {
        pass1jq.css('border-color', 'red');
        pass2jq.css('border-color', 'red');

    }
}

function checkIfPasswordsMatchEdit() {
    var pass1jq = $('#editNewPass');
    var pass2jq = $('#editNewPass2');

    var pass1 = $.trim(pass1jq.val());
    var pass2 = $.trim(pass2jq.val());

    if (pass1 === pass2) {
        pass1jq.css('border-color', 'green');
        pass2jq.css('border-color', 'green');
    }
    else {
        pass1jq.css('border-color', 'red');
        pass2jq.css('border-color', 'red');

    }
}

function showSyncInfo() {
    $($('#syncInfo')).css('display', 'block');
}

if ($("#createForm").length) {

    document.getElementById("createFileBtn").onchange = function () {
        document.getElementById("createFile").value = this.files[0].name;
    };

    document.getElementById("createPicBtn").onchange = function () {
        checkExtension();
        document.getElementById("createPic").value = this.files[0].name;
    };
}
if ($('#userEditForm').length) {
    document.getElementById("userEditPicBtn").onchange = function () {
        checkExtensionUserEdit();
        document.getElementById("userEditPic").value = this.files[0].name;
    }
}
if ($('#extensionEditForm').length) {
    document.getElementById("extensionEditFileBtn").onchange = function () {
        document.getElementById("extensionEditFile").value = this.files[0].name;
    };

    document.getElementById("extensionEditPictureBtn").onchange = function () {
        checkExtensionForExtensionEdit();
        document.getElementById("extensionEditPicture").value = this.files[0].name;
    }
}

