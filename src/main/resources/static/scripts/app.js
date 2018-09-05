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
});

