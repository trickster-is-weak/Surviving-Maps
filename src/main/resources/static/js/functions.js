$(document).ready(function () {
    $('#menubar').load('nav');
    $('#footer').load('footer');
});

function rowClicked(id, site) {
    console.log(id)
    console.log(site)

    let row = $("#" + id);
    let selected = row.hasClass("selected");
    console.log("selected = " + selected);

    $('#searchResultTable tbody > tr').removeClass('selected');

    let obj;
    if (selected) {
        obj = {'id': -1}
    } else {
        obj = {'id': JSON.parse(site).id}
        row.addClass('selected');
    }

    getDisplay(obj)
}

function getDisplay(obj) {

    if (obj == null) {
        $('#searchResultTable tbody > tr').removeClass('selected');
        obj = {'id': -1}
    }

    let display = $("#displayViewer");
    $.ajax({
        type: 'get',
        url: '/getDisplay',
        data: obj,
        success: function (msg) {
            console.log("success display")
            display.html(msg);
        },
        error: function () {
            console.log("failure")
        }
    });
}


function dataTableFormat() {
    $('#searchResultTable').dataTable({
        "searching": false,
        "info": false,
        "lengthChange": false,
        "pageLength": 10
    });
}

function resetMultiSelect(multiSelect) {
    let select = $('#' + multiSelect)
    $('#' + multiSelect + ' option:selected').each(function () {
        $(this).prop('selected', false);
    })
    change();
    select.multiselect('refresh');
}

function resetBreakthroughs() {
    $('#example-reset option:selected').each(function () {
        $(this).prop('selected', false);
    })
    change();
    $('#example-reset').multiselect('refresh');
}

$(document).ready(function () {
    console.log("doc ready")
    reloadMultiSelects();
    dataTableFormat();
    setFormTriggers();

    $(function () {
        $("#tablebody").html(loading);
        setTimeout(fetchData, 50);
    });

});

function checkEnable(res) {

    let op = '#' + res + 'Op';
    let select = '#' + res;

    console.log(res)
    if ($(op).val() === "NO_PREFERENCE") {
        console.log("No pref")
        $(select).prop('disabled', true)
    } else {
        console.log("Other")
        $(select).prop('disabled', false)
    }

    console.log($(op).val());
    console.log($(select).prop('disabled'));

    return false;
}

function setFormTriggers() {
    $('#gameVariant').on("change", reloadForm)
    $('#disasters').on("change", change)
    $('#resources').on("change", change)
    $('#example-reset').on("change", change)
    $('#mapDetails').on("change", change)
}

function fetchData() {

    $.ajax({
        type: "GET",
        url: "/getdata",
        success: function (msg) {
            successFn(msg)
        },
        error: function () {
            console.log("failure")
        }
    });
}

function successFn(msg) {
    $("#tableDiv").html(msg);
    dataTableFormat();
    return true
}

let loading = "<tr>" +
    "<td colspan='11'>" +
    "<div class=\"clearfix\">\n" +
    "  <div class=\"spinner-grow float-center loading-icon\" role=\"status\">\n" +
    "    <span class=\"sr-only\">Loading...</span>\n" +
    "  </div>\n" +
    "</div>" +
    "</td>" +
    "</tr>";

let change = function formChange() {
    console.log("change fn")
    let form = $("#main_form");
    $("#tablebody").html(loading);

    console.log(form);
    console.log(form.attr('action'));
    console.log(form.attr('method'));
    console.log(form.serialize());

    $.ajax({
        type: form.attr('method'),
        url: form.attr('action'),
        data: form.serialize(),
        success: function (msg) {
            successFn(msg)
        },
        error: function (xhr) {
            console.log(xhr.responseText);
        }
    });
    getDisplay();
    return false;
};


function checkEnabled() {
    checkEnable("water")
    checkEnable("concrete")
    checkEnable("metal")
    checkEnable("raremetal")
    checkEnable("meteor")
    checkEnable("coldwaves")
    checkEnable("dustStorm")
    checkEnable("dustDevil")
}

function reloadForm() {
    let form = $("#main_form");

    let complex = form.attr('action') === "/complex"
    let url = complex ? "/reloadComplexForm" : "/reloadForm";

    $.ajax({
        type: "GET",
        data: form.serialize(),
        url: url,
        success: function (msg) {
            console.log("success reloadForm")
            // console.log(msg.toString().slice(0, 100))
            $("#formDiv").html(msg);
            if (complex) checkEnabled()

            reloadMultiSelects()
            setFormTriggers()
            change();
        },
        error: function () {
            console.log("failure")
        }
    });
}

function reloadMultiSelects() {
    console.log("reload multiselect")
    let btr = $('#example-reset');
    let nla = $('#landingArea-reset');
    let top = $('#topography-reset');
    let mpn = $('#map-name-reset');

    reloadMultiSelect(btr);
    reloadMultiSelect(nla);
    reloadMultiSelect(top);
    reloadMultiSelect(mpn);

}

function reloadMultiSelect(element) {

    element.multiselect({
            buttonContainer: '<div class="btn-group w-100" />',
            buttonClass: 'multiselect dropdown-toggle custom-select text-left',
            maxHeight: 200,
            enableFiltering: true
        }
    );

    element.multiselect('refresh');
    element.on("change", change)
}