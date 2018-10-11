$(document).ready( function() {

    $("#login_button").click(function () {
        // console.log("here");
        var username = $("#login_username").val().trim();
        var password = $("#login_password").val().trim();

        if( username !== "" && password !== "") {
            $.post({
                url: '/api/auth/login',
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    username: username,
                    password: password
                }),
                success: function (response, status) {
                    if(response) { // check response here
                        alert("Login successful")
                    } else {
                        alert("Login failed")
                    }
                    if (status) alert(status)
                },
                error: function (jq) {
                    alert(jq.responseText)
                }
            });
        } else {
            alert("One of the fields is empty")
        }
    });

    $("#reg_button").click(function () {
        var username = $("#reg_username").val().trim();
        var password = $("#reg_password").val().trim();
        var firstName = $("#reg_firstname").val().trim();
        var lastName = $("#reg_lastname").val().trim();

        if( username !== "" && password !== "") {
            $.post({
                url: '/api/auth/register',
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    firstName: firstName,
                    lastName: lastName,
                    username: username,
                    password: password
                }),
                success: function (response) {
                    if (response.success) { // check response here
                        alert(response.msg)
                    } else {
                        alert(response.msg)
                    }
                }
            })
        } else {
            alert("One of the fields is empty")
        }
    })
});
