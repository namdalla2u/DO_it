/*jshint eqeqeq:false */
/*global jQuery, define */
(function (factory) {
    "use strict";
    if (typeof define === "function" && define.amd) {
        // AMD. Register as an anonymous module.
        define([
            "jquery",
            "./grid.base"
        ], factory);
    } else {
        // Browser globals
        factory(jQuery);
    }
}(function ($) {
    /**
     * all events and options here are aded anonynous and not in the base grid
     * since the array is to big. Here is the order of execution.
     * From this point we use jQuery isFunction
     * formatCell
     * beforeEditCell,
     * onCellSelect (used only for noneditable cels)
     * afterEditCell,
     * beforeSaveCell, (called before validation of values if any)
     * beforeSubmitCell (if cellsubmit remote (ajax))
     * onSubmitCell
     * afterSubmitCell(if cellsubmit remote (ajax)),
     * afterSaveCell,
     * errorCell,
     * validationCell
     * serializeCellData - new
     * Options
     * cellsubmit (remote,clientArray) (added in grid options)
     * cellurl
     * ajaxCellOptions
     * restoreCellonFail
     * */
    "use strict";
//module begin
    $.jgrid.extend({
        editCell: function (iRow, iCol, ed, event) {
            return this.each(function () {
                var $t = this, nm, tmp, cc, cm,
                    highlight = $(this).jqGrid('getStyleUI', $t.p.styleUI + '.common', 'highlight', true),

                    hover = $(this).jqGrid('getStyleUI', $t.p.styleUI + '.common', 'hover', true),
                    inpclass = $(this).jqGrid('getStyleUI', $t.p.styleUI + ".celledit", 'inputClass', true);

                if (!$t.grid || $t.p.cellEdit !== true) {
                    return;
                }
                iCol = parseInt(iCol, 10);
                // select the row that can be used for other methods
                $t.p.selrow = $t.rows[iRow].id;
                if (!$t.p.knv) {
                    $($t).jqGrid("GridNav");
                }
                // check to see if we have already edited cell
                if ($t.p.savedRow.length > 0) {
                    // prevent second click on that field and enable selects
                    if (ed === true) {
                        if (iRow == $t.p.iRow && iCol == $t.p.iCol) {
                            return;
                        }
                    }
                    // save the cell
                    $($t).jqGrid("saveCell", $t.p.savedRow[0].id, $t.p.savedRow[0].ic);
                } else {
                    window.setTimeout(function () {
                        $("#" + $.jgrid.jqID($t.p.knv)).attr("tabindex", "-1").focus();
                    }, 1);
                }
                cm = $t.p.colModel[iCol];
                nm = cm.name;
                if (nm === 'subgrid' || nm === 'cb' || nm === 'rn') {
                    return;
                }
                try {
                    cc = $($t.rows[iRow].cells[iCol]);
                } catch (e) {
                    cc = $("td:eq(" + iCol + ")", $t.rows[iRow]);
                }
                if (parseInt($t.p.iCol, 10) >= 0 && parseInt($t.p.iRow, 10) >= 0 && $t.p.iRowId !== undefined) {
                    var therow = $($t).jqGrid('getGridRowById', $t.p.iRowId);
                    //$("td:eq("+$t.p.iCol+")",$t.rows[$t.p.iRow]).removeClass("edit-cell " + highlight);
                    $(therow).removeClass("selected-row " + hover).find("td:eq(" + $t.p.iCol + ")").removeClass("edit-cell " + highlight);
                }
                cc.addClass("edit-cell " + highlight);
                $($t.rows[iRow]).addClass("selected-row " + hover);
                if (cm.editable === true && ed === true && !cc.hasClass("not-editable-cell") && (!$.isFunction($t.p.isCellEditable) || $t.p.isCellEditable.call($t, nm, iRow, iCol))) {
                    try {
                        tmp = $.unformat.call($t, cc, {rowId: $t.rows[iRow].id, colModel: cm}, iCol);
                    } catch (_) {
                        tmp = (cm.edittype && cm.edittype === 'textarea') ? cc.text() : cc.html();
                    }
                    if ($t.p.autoencode) {
                        tmp = $.jgrid.htmlDecode(tmp);
                    }
                    if (!cm.edittype) {
                        cm.edittype = "text";
                    }
                    $t.p.savedRow.push({id: iRow, ic: iCol, name: nm, v: tmp, rowId: $t.rows[iRow].id});
                    if (tmp === "&nbsp;" || tmp === "&#160;" || (tmp.length === 1 && tmp.charCodeAt(0) === 160)) {
                        tmp = '';
                    }
                    if ($.isFunction($t.p.formatCell)) {
                        var tmp2 = $t.p.formatCell.call($t, $t.rows[iRow].id, nm, tmp, iRow, iCol);
                        if (tmp2 !== undefined) {
                            tmp = tmp2;
                        }
                    }
                    $($t).triggerHandler("jqGridBeforeEditCell", [$t.rows[iRow].id, nm, tmp, iRow, iCol]);
                    if ($.isFunction($t.p.beforeEditCell)) {
                        $t.p.beforeEditCell.call($t, $t.rows[iRow].id, nm, tmp, iRow, iCol);
                    }
                    var opt = $.extend({}, cm.editoptions || {}, {
                        id: iRow + "_" + nm,
                        name: nm,
                        rowId: $t.rows[iRow].id,
                        oper: 'edit'
                    });
                    var elc = $.jgrid.createEl.call($t, cm.edittype, opt, tmp, true, $.extend({}, $.jgrid.ajaxOptions, $t.p.ajaxSelectOptions || {}));
                    if ($.inArray(cm.edittype, ['text', 'textarea', 'password', 'select']) > -1) {
                        $(elc).addClass(inpclass);
                    }

                    cc.html("").append(elc).attr("tabindex", "0");
                    $.jgrid.bindEv.call($t, elc, opt);
                    window.setTimeout(function () {
                        $(elc).focus();
                    }, 1);
                    $("input, select, textarea", cc).on("keydown", function (e) {
                        if (e.keyCode === 27) {
                            if ($("input.hasDatepicker", cc).length > 0) {
                                if ($(".ui-datepicker").is(":hidden")) {
                                    $($t).jqGrid("restoreCell", iRow, iCol);
                                } else {
                                    $("input.hasDatepicker", cc).datepicker('hide');
                                }
                            } else {
                                $($t).jqGrid("restoreCell", iRow, iCol);
                            }
                        } //ESC
                        if (e.keyCode === 13 && !e.shiftKey) {
                            $($t).jqGrid("saveCell", iRow, iCol);
                            // Prevent default action
                            return false;
                        } //Enter
                        if (e.keyCode === 9) {
                            if (!$t.grid.hDiv.loading) {
                                if (e.shiftKey) {
                                    $($t).jqGrid("prevCell", iRow, iCol, e);
                                } //Shift TAb
                                else {
                                    $($t).jqGrid("nextCell", iRow, iCol, e);
                                } //Tab
                            } else {
                                return false;
                            }
                        }
                        e.stopPropagation();
                    });
                    $($t).triggerHandler("jqGridAfterEditCell", [$t.rows[iRow].id, nm, tmp, iRow, iCol]);
                    if ($.isFunction($t.p.afterEditCell)) {
                        $t.p.afterEditCell.call($t, $t.rows[iRow].id, nm, tmp, iRow, iCol);
                    }
                } else {
                    tmp = cc.html().replace(/\&#160\;/ig, '');
                    $($t).triggerHandler("jqGridCellSelect", [$t.rows[iRow].id, iCol, tmp, event]);
                    if ($.isFunction($t.p.onCellSelect)) {
                        $t.p.onCellSelect.call($t, $t.rows[iRow].id, iCol, tmp, event);
                    }
                }
                $t.p.iCol = iCol;
                $t.p.iRow = iRow;
                $t.p.iRowId = $t.rows[iRow].id;
            });
        },
        saveCell: function (iRow, iCol) {
            return this.each(function () {
                var $t = this, fr = $t.p.savedRow.length >= 1 ? 0 : null,
                    errors = $.jgrid.getRegional(this, 'errors'),
                    edit = $.jgrid.getRegional(this, 'edit');
                if (!$t.grid || $t.p.cellEdit !== true) {
                    return;
                }
                if (fr !== null) {
                    var trow = $($t).jqGrid("getGridRowById", $t.p.savedRow[0].rowId),
                        cc = $('td:eq(' + iCol + ')', trow),
                        cm = $t.p.colModel[iCol], nm = cm.name, nmjq = $.jgrid.jqID(nm), v, v2,
                        p = $(cc).offset();

                    switch (cm.edittype) {
                        case "select":
                            if (!cm.editoptions.multiple) {
                                v = $("#" + iRow + "_" + nmjq + " option:selected", trow).val();
                                v2 = $("#" + iRow + "_" + nmjq + " option:selected", trow).text();
                            } else {
                                var sel = $("#" + iRow + "_" + nmjq, trow), selectedText = [];
                                v = $(sel).val();
                                if (v) {
                                    v.join(",");
                                } else {
                                    v = "";
                                }
                                $("option:selected", sel).each(
                                    function (i, selected) {
                                        selectedText[i] = $(selected).text();
                                    }
                                );
                                v2 = selectedText.join(",");
                            }
                            if (cm.formatter) {
                                v2 = v;
                            }
                            break;
                        case "checkbox":
                            var cbv = ["Yes", "No"];
                            if (cm.editoptions && cm.editoptions.value) {
                                cbv = cm.editoptions.value.split(":");
                            }
                            v = $("#" + iRow + "_" + nmjq, trow).is(":checked") ? cbv[0] : cbv[1];
                            v2 = v;
                            break;
                        case "password":
                        case "text":
                        case "textarea":
                        case "button" :
                            v = $("#" + iRow + "_" + nmjq, trow).val();
                            v2 = v;
                            break;
                        case 'custom' :
                            try {
                                if (cm.editoptions && $.isFunction(cm.editoptions.custom_value)) {
                                    v = cm.editoptions.custom_value.call($t, $(".customelement", cc), 'get');
                                    if (v === undefined) {
                                        throw "e2";
                                    } else {
                                        v2 = v;
                                    }
                                } else {
                                    throw "e1";
                                }
                            } catch (e) {
                                if (e === "e1") {
                                    $.jgrid.info_dialog(errors.errcap, "function 'custom_value' " + edit.msg.nodefined, edit.bClose, {styleUI: $t.p.styleUI});
                                } else if (e === "e2") {
                                    $.jgrid.info_dialog(errors.errcap, "function 'custom_value' " + edit.msg.novalue, edit.bClose, {styleUI: $t.p.styleUI});
                                } else {
                                    $.jgrid.info_dialog(errors.errcap, e.message, edit.bClose, {styleUI: $t.p.styleUI});
                                }
                            }
                            break;
                    }
                    // The common approach is if nothing changed do not do anything
                    if (v2 !== $t.p.savedRow[fr].v) {
                        var vvv = $($t).triggerHandler("jqGridBeforeSaveCell", [$t.p.savedRow[fr].rowId, nm, v, iRow, iCol]);
                        if (vvv) {
                            v = vvv;
                            v2 = vvv;
                        }
                        if ($.isFunction($t.p.beforeSaveCell)) {
                            var vv = $t.p.beforeSaveCell.call($t, $t.p.savedRow[fr].rowId, nm, v, iRow, iCol);
                            if (vv) {
                                v = vv;
                                v2 = vv;
                            }
                        }
                        var cv = $.jgrid.checkValues.call($t, v, iCol), nuem = false;
                        if (cv[0] === true) {
                            var addpost = $($t).triggerHandler("jqGridBeforeSubmitCell", [$t.p.savedRow[fr].rowId, nm, v, iRow, iCol]) || {};
                            if ($.isFunction($t.p.beforeSubmitCell)) {
                                addpost = $t.p.beforeSubmitCell.call($t, $t.p.savedRow[fr].rowId, nm, v, iRow, iCol);
                                if (!addpost) {
                                    addpost = {};
                                }
                            }
                            var retsub = $($t).triggerHandler("jqGridOnSubmitCell", [$t.p.savedRow[fr].rowId, nm, v, iRow, iCol]);
                            if (retsub === undefined) {
                                retsub = true;
                            }
                            if ($.isFunction($t.p.onSubmitCell)) {
                                retsub = $t.p.onSubmitCell($t.p.savedRow[fr].rowId, nm, v, iRow, iCol);
                                if (retsub === undefined) {
                                    retsub = true;
                                }
                            }
                            if (retsub === false) {
                                return;
                            }
                            if ($("input.hasDatepicker", cc).length > 0) {
                                $("input.hasDatepicker", cc).datepicker('hide');
                            }
                            if ($t.p.cellsubmit === 'remote') {
                                if ($t.p.cellurl) {
                                    var postdata = {};
                                    if ($t.p.autoencode) {
                                        v = $.jgrid.htmlEncode(v);
                                    }
                                    if (cm.editoptions && cm.editoptions.NullIfEmpty && v === "") {
                                        v = 'null';
                                        nuem = true;
                                    }
                                    postdata[nm] = v;
                                    var opers = $t.p.prmNames,
                                        idname = opers.id,
                                        oper = opers.oper;

                                    postdata[idname] = $.jgrid.stripPref($t.p.idPrefix, $t.p.savedRow[fr].rowId);
                                    postdata[oper] = opers.editoper;
                                    postdata = $.extend(addpost, postdata);
                                    $($t).jqGrid("progressBar", {
                                        method: "show",
                                        loadtype: $t.p.loadui,
                                        htmlcontent: $.jgrid.getRegional($t, 'defaults.savetext')
                                    });
                                    $t.grid.hDiv.loading = true;
                                    $.ajax($.extend({
                                        url: $t.p.cellurl,
                                        data: $.isFunction($t.p.serializeCellData) ? $t.p.serializeCellData.call($t, postdata, nm) : postdata,
                                        type: "POST",
                                        complete: function (result, stat) {
                                            $($t).jqGrid("progressBar", {method: "hide", loadtype: $t.p.loadui});
                                            $t.grid.hDiv.loading = false;
                                            if (stat === 'success') {
                                                var ret = $($t).triggerHandler("jqGridAfterSubmitCell", [$t, result, postdata[idname], nm, v, iRow, iCol]) || [true, ''];
                                                if (ret[0] === true && $.isFunction($t.p.afterSubmitCell)) {
                                                    ret = $t.p.afterSubmitCell.call($t, result, postdata[idname], nm, v, iRow, iCol);
                                                }
                                                if (ret[0] === true) {
                                                    if (nuem) {
                                                        v = "";
                                                    }
                                                    $(cc).empty();
                                                    $($t).jqGrid("setCell", $t.p.savedRow[fr].rowId, iCol, v2, false, false, true);
                                                    $(cc).addClass("dirty-cell");
                                                    $(trow).addClass("edited");
                                                    $($t).triggerHandler("jqGridAfterSaveCell", [$t.p.savedRow[fr].rowId, nm, v, iRow, iCol]);
                                                    if ($.isFunction($t.p.afterSaveCell)) {
                                                        $t.p.afterSaveCell.call($t, $t.p.savedRow[fr].rowId, nm, v, iRow, iCol);
                                                    }
                                                    $t.p.savedRow.splice(0, 1);
                                                } else {
                                                    $($t).triggerHandler("jqGridErrorCell", [result, stat]);
                                                    if ($.isFunction($t.p.errorCell)) {
                                                        $t.p.errorCell.call($t, result, stat);
                                                    } else {
                                                        $.jgrid.info_dialog(errors.errcap, ret[1], edit.bClose, {
                                                            styleUI: $t.p.styleUI,
                                                            top: p.top + 30,
                                                            left: p.left,
                                                            onClose: function () {
                                                                if (!$t.p.restoreCellonFail) {
                                                                    $("#" + iRow + "_" + nmjq, trow).focus();
                                                                }
                                                            }
                                                        });
                                                    }
                                                    if ($t.p.restoreCellonFail) {
                                                        $($t).jqGrid("restoreCell", iRow, iCol);
                                                    }
                                                }
                                            }
                                        },
                                        error: function (res, stat, err) {
                                            $("#lui_" + $.jgrid.jqID($t.p.id)).hide();
                                            $t.grid.hDiv.loading = false;
                                            $($t).triggerHandler("jqGridErrorCell", [res, stat, err]);
                                            if ($.isFunction($t.p.errorCell)) {
                                                $t.p.errorCell.call($t, res, stat, err);
                                            } else {
                                                $.jgrid.info_dialog(errors.errcap, res.status + " : " + res.statusText + "<br/>" + stat, edit.bClose, {
                                                    styleUI: $t.p.styleUI,
                                                    top: p.top + 30,
                                                    left: p.left,
                                                    onClose: function () {
                                                        if (!$t.p.restoreCellonFail) {
                                                            $("#" + iRow + "_" + nmjq, trow).focus();
                                                        }
                                                    }
                                                });
                                            }
                                            if ($t.p.restoreCellonFail) {
                                                $($t).jqGrid("restoreCell", iRow, iCol);
                                            }
                                        }
                                    }, $.jgrid.ajaxOptions, $t.p.ajaxCellOptions || {}));
                                } else {
                                    try {
                                        $.jgrid.info_dialog(errors.errcap, errors.nourl, edit.bClose, {styleUI: $t.p.styleUI});
                                        if ($t.p.restoreCellonFail) {
                                            $($t).jqGrid("restoreCell", iRow, iCol);
                                        }
                                    } catch (e) {
                                    }
                                }
                            }
                            if ($t.p.cellsubmit === 'clientArray') {
                                $(cc).empty();
                                $($t).jqGrid("setCell", $t.p.savedRow[fr].rowId, iCol, v2, false, false, true);
                                $(cc).addClass("dirty-cell");
                                $(trow).addClass("edited");
                                $($t).triggerHandler("jqGridAfterSaveCell", [$t.p.savedRow[fr].rowId, nm, v, iRow, iCol]);
                                if ($.isFunction($t.p.afterSaveCell)) {
                                    $t.p.afterSaveCell.call($t, $t.p.savedRow[fr].rowId, nm, v, iRow, iCol);
                                }
                                $t.p.savedRow.splice(0, 1);
                            }
                        } else {
                            try {
                                if ($.isFunction($t.p.validationCell)) {
                                    $t.p.validationCell.call($t, $("#" + iRow + "_" + nmjq, trow), cv[1], iRow, iCol);
                                } else {
                                    window.setTimeout(function () {
                                        $.jgrid.info_dialog(errors.errcap, v + " " + cv[1], edit.bClose, {
                                            styleUI: $t.p.styleUI,
                                            top: p.top + 30,
                                            left: p.left,
                                            onClose: function () {
                                                if (!$t.p.restoreCellonFail) {
                                                    $("#" + iRow + "_" + nmjq, trow).focus();
                                                }
                                            }
                                        });
                                    }, 50);
                                    if ($t.p.restoreCellonFail) {
                                        $($t).jqGrid("restoreCell", iRow, iCol);
                                    }
                                }
                            } catch (e) {
                                alert(cv[1]);
                            }
                        }
                    } else {
                        $($t).jqGrid("restoreCell", iRow, iCol);
                    }
                }
                window.setTimeout(function () {
                    $("#" + $.jgrid.jqID($t.p.knv)).attr("tabindex", "-1").focus();
                }, 0);
            });
        },
        restoreCell: function (iRow, iCol) {
            return this.each(function () {
                var $t = this, fr = $t.p.savedRow.length >= 1 ? 0 : null;
                if (!$t.grid || $t.p.cellEdit !== true) {
                    return;
                }
                if (fr !== null) {
                    var trow = $($t).jqGrid("getGridRowById", $t.p.savedRow[fr].rowId),
                        cc = $('td:eq(' + iCol + ')', trow);
                    // datepicker fix
                    if ($.isFunction($.fn.datepicker)) {
                        try {
                            $("input.hasDatepicker", cc).datepicker('hide');
                        } catch (e) {
                        }
                    }
                    $(cc).empty().attr("tabindex", "-1");
                    $($t).jqGrid("setCell", $t.p.savedRow[0].rowId, iCol, $t.p.savedRow[fr].v, false, false, true);
                    $($t).triggerHandler("jqGridAfterRestoreCell", [$t.p.savedRow[fr].rowId, $t.p.savedRow[fr].v, iRow, iCol]);
                    if ($.isFunction($t.p.afterRestoreCell)) {
                        $t.p.afterRestoreCell.call($t, $t.p.savedRow[fr].rowId, $t.p.savedRow[fr].v, iRow, iCol);
                    }
                    $t.p.savedRow.splice(0, 1);
                }
                window.setTimeout(function () {
                    $("#" + $t.p.knv).attr("tabindex", "-1").focus();
                }, 0);
            });
        },
        nextCell: function (iRow, iCol, event) {
            return this.each(function () {
                var $t = this, nCol = false, i;
                if (!$t.grid || $t.p.cellEdit !== true) {
                    return;
                }
                // try to find next editable cell
                for (i = iCol + 1; i < $t.p.colModel.length; i++) {
                    if ($t.p.colModel[i].editable === true && (!$.isFunction($t.p.isCellEditable) || $t.p.isCellEditable.call($t, $t.p.colModel[i].name, iRow, i))) {
                        nCol = i;
                        break;
                    }
                }
                if (nCol !== false) {
                    $($t).jqGrid("editCell", iRow, nCol, true, event);
                } else {
                    if ($t.p.savedRow.length > 0) {
                        $($t).jqGrid("saveCell", iRow, iCol);
                    }
                }
            });
        },
        prevCell: function (iRow, iCol, event) {
            return this.each(function () {
                var $t = this, nCol = false, i;
                if (!$t.grid || $t.p.cellEdit !== true) {
                    return;
                }
                // try to find next editable cell
                for (i = iCol - 1; i >= 0; i--) {
                    if ($t.p.colModel[i].editable === true && (!$.isFunction($t.p.isCellEditable) || $t.p.isCellEditable.call($t, $t.p.colModel[i].name, iRow, i))) {
                        nCol = i;
                        break;
                    }
                }
                if (nCol !== false) {
                    $($t).jqGrid("editCell", iRow, nCol, true, event);
                } else {
                    if ($t.p.savedRow.length > 0) {
                        $($t).jqGrid("saveCell", iRow, iCol);
                    }
                }
            });
        },
        GridNav: function () {
            return this.each(function () {
                var $t = this;
                if (!$t.grid || $t.p.cellEdit !== true) {
                    return;
                }
                // trick to process keydown on non input elements
                $t.p.knv = $t.p.id + "_kn";
                var selection = $("<div style='position:fixed;top:0px;width:1px;height:1px;' tabindex='0'><div tabindex='-1' style='width:1px;height:1px;' id='" + $t.p.knv + "'></div></div>"),
                    i, kdir;

                function scrollGrid(iR, iC, tp) {
                    if (tp.substr(0, 1) === 'v') {
                        var ch = $($t.grid.bDiv)[0].clientHeight,
                            st = $($t.grid.bDiv)[0].scrollTop,
                            nROT = $t.rows[iR].offsetTop + $t.rows[iR].clientHeight,
                            pROT = $t.rows[iR].offsetTop;
                        if (tp === 'vd') {
                            if (nROT >= ch) {
                                $($t.grid.bDiv)[0].scrollTop = $($t.grid.bDiv)[0].scrollTop + $t.rows[iR].clientHeight;
                            }
                        }
                        if (tp === 'vu') {
                            if (pROT < st) {
                                $($t.grid.bDiv)[0].scrollTop = $($t.grid.bDiv)[0].scrollTop - $t.rows[iR].clientHeight;
                            }
                        }
                    }
                    if (tp === 'h') {
                        var cw = $($t.grid.bDiv)[0].clientWidth,
                            sl = $($t.grid.bDiv)[0].scrollLeft,
                            nCOL = $t.rows[iR].cells[iC].offsetLeft + $t.rows[iR].cells[iC].clientWidth,
                            pCOL = $t.rows[iR].cells[iC].offsetLeft;
                        if (nCOL >= cw + parseInt(sl, 10)) {
                            $($t.grid.bDiv)[0].scrollLeft = $($t.grid.bDiv)[0].scrollLeft + $t.rows[iR].cells[iC].clientWidth;
                        } else if (pCOL < sl) {
                            $($t.grid.bDiv)[0].scrollLeft = $($t.grid.bDiv)[0].scrollLeft - $t.rows[iR].cells[iC].clientWidth;
                        }
                    }
                }

                function findNextVisible(iC, act) {
                    var ind, i;
                    if (act === 'lft') {
                        ind = iC + 1;
                        for (i = iC; i >= 0; i--) {
                            if ($t.p.colModel[i].hidden !== true) {
                                ind = i;
                                break;
                            }
                        }
                    }
                    if (act === 'rgt') {
                        ind = iC - 1;
                        for (i = iC; i < $t.p.colModel.length; i++) {
                            if ($t.p.colModel[i].hidden !== true) {
                                ind = i;
                                break;
                            }
                        }
                    }
                    return ind;
                }

                $(selection).insertBefore($t.grid.cDiv);
                $("#" + $t.p.knv)
                    .focus()
                    .keydown(function (e) {
                        kdir = e.keyCode;
                        if ($t.p.direction === "rtl") {
                            if (kdir === 37) {
                                kdir = 39;
                            } else if (kdir === 39) {
                                kdir = 37;
                            }
                        }
                        switch (kdir) {
                            case 38:
                                if ($t.p.iRow - 1 > 0) {
                                    scrollGrid($t.p.iRow - 1, $t.p.iCol, 'vu');
                                    $($t).jqGrid("editCell", $t.p.iRow - 1, $t.p.iCol, false, e);
                                }
                                break;
                            case 40 :
                                if ($t.p.iRow + 1 <= $t.rows.length - 1) {
                                    scrollGrid($t.p.iRow + 1, $t.p.iCol, 'vd');
                                    $($t).jqGrid("editCell", $t.p.iRow + 1, $t.p.iCol, false, e);
                                }
                                break;
                            case 37 :
                                if ($t.p.iCol - 1 >= 0) {
                                    i = findNextVisible($t.p.iCol - 1, 'lft');
                                    scrollGrid($t.p.iRow, i, 'h');
                                    $($t).jqGrid("editCell", $t.p.iRow, i, false, e);
                                }
                                break;
                            case 39 :
                                if ($t.p.iCol + 1 <= $t.p.colModel.length - 1) {
                                    i = findNextVisible($t.p.iCol + 1, 'rgt');
                                    scrollGrid($t.p.iRow, i, 'h');
                                    $($t).jqGrid("editCell", $t.p.iRow, i, false, e);
                                }
                                break;
                            case 13:
                                if (parseInt($t.p.iCol, 10) >= 0 && parseInt($t.p.iRow, 10) >= 0) {
                                    $($t).jqGrid("editCell", $t.p.iRow, $t.p.iCol, true, e);
                                }
                                break;
                            default :
                                return true;
                        }
                        return false;
                    });
            });
        },
        getChangedCells: function (mthd) {
            var ret = [];
            if (!mthd) {
                mthd = 'all';
            }
            this.each(function () {
                var $t = this, nm;
                if (!$t.grid || $t.p.cellEdit !== true) {
                    return;
                }
                $($t.rows).each(function (j) {
                    var res = {};
                    if ($(this).hasClass("edited")) {
                        $('td', this).each(function (i) {
                            nm = $t.p.colModel[i].name;
                            if (nm !== 'cb' && nm !== 'subgrid') {
                                if (mthd === 'dirty') {
                                    if ($(this).hasClass('dirty-cell')) {
                                        try {
                                            res[nm] = $.unformat.call($t, this, {
                                                rowId: $t.rows[j].id,
                                                colModel: $t.p.colModel[i]
                                            }, i);
                                        } catch (e) {
                                            res[nm] = $.jgrid.htmlDecode($(this).html());
                                        }
                                    }
                                } else {
                                    try {
                                        res[nm] = $.unformat.call($t, this, {
                                            rowId: $t.rows[j].id,
                                            colModel: $t.p.colModel[i]
                                        }, i);
                                    } catch (e) {
                                        res[nm] = $.jgrid.htmlDecode($(this).html());
                                    }
                                }
                            }
                        });
                        res.id = this.id;
                        ret.push(res);
                    }
                });
            });
            return ret;
        }
/// end  cell editing
    });
//module end
}));
