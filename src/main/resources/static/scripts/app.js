$(document).ready(function () {

    var url = $(location).attr('href');
    var parameter = url.substring(url.lastIndexOf('=')+1);

    $('#sortOption').val(parameter);

    $('.home-icon').on('click', function () {
        $('html').animate({scrollTop: 0}, 'slow');
        return true;
    });
    $('.my-tag').on('click', function(){
        var tagText = $(this).children("span").html();
        console.log(tagText);

        $(location).attr('href', '/tag/'+ tagText);


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
    $('#searchField').keypress(function(e){
        var textfield = $('#searchField').val();
        if(textfield === ""){
            return;
        }
        if(e.which===13){

            $(location).attr('href', '/search?criteria='+ textfield);
        }
    });
    $('#sortButton').on('click', function () {


            $(location).attr('href', '/?sort=' + $('#sortOption').val());

    });
    $('#loginSubmit').on('click', function(){
        var snackbar = document.querySelector('.mdl-js-snackbar');
        var username =  $('#loginUsername');
        var password =  $('#loginPass');

        var data = {
            message: 'Invalid Username or Password!',
            timeout: 3000
        };
        if(isWrongLength(username) || isEmpty(username)){
            data.message = 'Invalid Username!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        if(isWrongLength(password) || isEmpty(password)){
            data.message = 'Invalid Password!';
            snackbar.MaterialSnackbar.showSnackbar(data);
            return;
        }
        $('#loginForm').submit();

    });

    var isEmpty = function(e){
        return $.trim(e.val())=='';
    };
    var isWrongLength = function(e){
        var length = e.val().length;
        return length < 6 || length > 16;
    };
});

