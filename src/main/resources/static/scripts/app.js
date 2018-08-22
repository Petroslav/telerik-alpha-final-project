$(document).ready(function () {

    $('.home-icon').on('click', function(){
        $('html').animate({ scrollTop: 0 }, 'slow'); return true;
    });

    var totalH = $('#stickyNav').offset().top;
    $(window).bind('scroll', function () {
        var vPos = $(window).scrollTop();

        if (totalH < vPos) {
            $('#stickyNav').css({
                'position': 'fixed',
                'top': 0,
                'bottom' : 'auto'
            })
        } else {
            $('#stickyNav').css({
                'position': 'absolute',
                'top': '',

            })
        }
    });
});

