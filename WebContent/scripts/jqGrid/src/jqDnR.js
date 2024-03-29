/*
 * jqDnR - Minimalistic Drag'n'Resize for jQuery.
 *
 * Copyright (c) 2007 Brice Burgess <bhb@iceburg.net>, http://www.iceburg.net
 * Licensed under the MIT License:
 * http://www.opensource.org/licenses/mit-license.php
 * 
 * $Version: 2007.08.19 +r2
 */
/*jshint eqeqeq:false */
/*global jQuery, define */
(function (factory) {
    "use strict";
    if (typeof define === "function" && define.amd) {
        // AMD. Register as an anonymous module.
        define([
            "jquery"
        ], factory);
    } else {
        // Browser globals
        factory(jQuery);
    }
}(function ($) {
    "use strict";
//module begin
    $.fn.jqDrag = function (h) {
        return i(this, h, 'd');
    };
    $.fn.jqResize = function (h, ar) {
        return i(this, h, 'r', ar);
    };
    $.jqDnR = {
        dnr: {},
        e: 0,
        drag: function (v) {
            if (M.k == 'd') {
                E.css({left: M.X + v.pageX - M.pX, top: M.Y + v.pageY - M.pY});
            } else {
                E.css({width: Math.max(v.pageX - M.pX + M.W, 0), height: Math.max(v.pageY - M.pY + M.H, 0)});
                if (M1) {
                    E1.css({width: Math.max(v.pageX - M1.pX + M1.W, 0), height: Math.max(v.pageY - M1.pY + M1.H, 0)});
                }
            }
            return false;
        },
        stop: function () {
            //E.css('opacity',M.o);
            $(document).off('mousemove', J.drag).off('mouseup', J.stop);
        }
    };
    var J = $.jqDnR, M = J.dnr, E = J.e, E1, M1,
        i = function (e, h, k, aR) {
            return e.each(function () {
                h = (h) ? $(h, e) : e;
                h.on('mousedown', {e: e, k: k}, function (v) {
                    var d = v.data, p = {};
                    E = d.e;
                    E1 = aR ? $(aR) : false;
                    // attempt utilization of dimensions plugin to fix IE issues
                    if (E.css('position') != 'relative') {
                        try {
                            E.position(p);
                        } catch (e) {
                        }
                    }
                    M = {
                        X: p.left || f('left') || 0,
                        Y: p.top || f('top') || 0,
                        W: f('width') || E[0].scrollWidth || 0,
                        H: f('height') || E[0].scrollHeight || 0,
                        pX: v.pageX,
                        pY: v.pageY,
                        k: d.k
                        //o:E.css('opacity')
                    };
                    // also resize
                    if (E1 && d.k != 'd') {
                        M1 = {
                            X: p.left || f1('left') || 0,
                            Y: p.top || f1('top') || 0,
                            W: E1[0].offsetWidth || f1('width') || 0,
                            H: E1[0].offsetHeight || f1('height') || 0,
                            pX: v.pageX,
                            pY: v.pageY,
                            k: d.k
                        };
                    } else {
                        M1 = false;
                    }
                    //E.css({opacity:0.8});
                    if ($("input.hasDatepicker", E[0])[0]) {
                        try {
                            $("input.hasDatepicker", E[0]).datepicker('hide');
                        } catch (dpe) {
                        }
                    }
                    $(document).mousemove($.jqDnR.drag).mouseup($.jqDnR.stop);
                    return false;
                });
            });
        },
        f = function (k) {
            return parseInt(E.css(k), 10) || false;
        },
        f1 = function (k) {
            return parseInt(E1.css(k), 10) || false;
        };
    /*
        jQuery tinyDraggable v1.0.2
        Copyright (c) 2014 Simon Steinberger / Pixabay
        GitHub: https://github.com/Pixabay/jQuery-tinyDraggable
        More info: https://pixabay.com/blog/posts/p-52/
        License: http://www.opensource.org/licenses/mit-license.php
    */
    $.fn.tinyDraggable = function (options) {
        var settings = $.extend({handle: 0, exclude: 0}, options);
        return this.each(function () {
            var dx, dy, el = $(this), handle = settings.handle ? $(settings.handle, el) : el;
            handle.on({
                mousedown: function (e) {
                    if (settings.exclude && ~$.inArray(e.target, $(settings.exclude, el))) {
                        return;
                    }
                    e.preventDefault();
                    var os = el.offset();
                    dx = e.pageX - os.left, dy = e.pageY - os.top;
                    $(document).on('mousemove.drag', function (e) {
                        el.offset({top: e.pageY - dy, left: e.pageX - dx});
                    });
                },
                mouseup: function (e) {
                    $(document).off('mousemove.drag');
                }
            });
        });
    };
//module end
}));
