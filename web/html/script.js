var jobban_select_option = document.getElementById("jobban_option");
var job_order_option = document.getElementById("jobban_order");
var standard_order_option = document.getElementById("standard_order");

function validateJobbanOption() {
    if (jobban_select_option.selected) {
        job_order_option.disabled = false;
    } else {
        if (job_order_option.selected) {
            job_order_option.selected = false;
            standard_order_option.selected = true;
        }

        job_order_option.disabled = true;
    }
}

var order_field = document.getElementById("order_type");
var standard_sorting_field = document.getElementById("standard_order");
var standard_order_field = document.getElementById("auto_order");

function validateOrder() {
    if (standard_sorting_field.selected) {
        order_field.disabled = true;
        standard_order_field.selected = true;
    } else {
        order_field.disabled = false;
    }
}

var ckeyField = document.getElementById("ckey_field");
var adminCkeyField = document.getElementById("a_ckey_field");

function validateSubmit() {
    if (ckeyField.value.trim().length === 0 && adminCkeyField.value.trim().length === 0) {
        alert("Нужно ввести хотя бы одно из полей admin ckey и ckey!");
        return false;
    }

    return true;
}