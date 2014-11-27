var editor;

$(function() {
    $("#startDate").datepicker();
    $("#startDate").datepicker("option", "showOn", 'both');
    $("#startDate").datepicker("option", "dateFormat", "yy/mm/dd");
    $("#endDate").datepicker();
    $("#endDate").datepicker("option", "showOn", 'both');
    $("#endDate").datepicker("option", "dateFormat", "yy/mm/dd");
    $("#memo-2").html($("#memo").val());

    $("#tree").dynatree({
        clickFolderMode: 3,
        onActivate: function(node) {
            // A DynaTreeNode object is passed to the activation handler
            // Note: we also get this event, if persistence is on, and the page is reloaded.
            //alert("You activated " + node.data.title);
        }
    });

    $("#memoDialog").dialog({
        autoOpen: false,
        width: 700,
        height: 500,
        buttons: [
            {
                text: "Ok",
                click: function() {
                    if (!editor)
                        return;

                    $("#memo-2").html(editor.getData());
                    $("#memo").val(editor.getData());

                    editor.destroy();
                    editor = null;
                    $(this).dialog("close");
                }
            },
            {
                text: "Cancel",
                click: function() {
                    $(this).dialog("close");
                }
            }
        ]
    });

    $("#categoryDialog").dialog({
        autoOpen: false,
        width: 700,
        height: 500,
        buttons: [
            {
                text: "Ok",
                click: function() {
                    var node = $("#tree").dynatree("getTree").getActiveNode();
                    if (node) {
                        //alert("node.data.key=" + node.data.key);
                        $("#categoryNo").val(node.data.key);
                        $("#categoryName").text(node.data.title);
                    }
                    $(this).dialog("close");
                }
            },
            {
                text: "Cancel",
                click: function() {
                    $(this).dialog("close");
                }
            }
        ]
    });

    $("#memo-link").click(function(event) {
        $("#memoDialog").dialog("open");
        event.preventDefault();
        if (editor)
            return;

        editor = CKEDITOR.appendTo('editor', {width: 670, height: 280}, $("#memo-2").html());
    });

    $("#category-link").click(function(event) {
        var n = $("#categoryNo").val();
        if (n !== "" && n !== "0") {
            var node = $("#tree").dynatree("getTree").getNodeByKey(n);
            node.focus();
            node.activate();
        }
        $("#categoryDialog").dialog("open");
        event.preventDefault();
    });
});
