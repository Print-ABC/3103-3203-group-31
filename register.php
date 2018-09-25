<?php

//$userid = "xjustus";
//$password = "12345";
//$email = "justus@sit.com";
//$name = "justus chua";
//$contact = 99999999;
//$organization = "SIT";
//$role = "student";

$response = array();

if ((isset($_POST['userId'])) && (isset($_POST['password'])) && (isset($_POST['email'])) &&
        (isset($_POST['name'])) && (isset($_POST['contact'])) && (isset($_POST['organization'])) &&
        (isset($_POST['role']))) {
    $userId = $_POST['userId'];
    $password = $_POST['password'];
    $email = $_POST['email'];
    $name = $_POST['name'];
    $contact = $_POST['contact'];
    $organization = $_POST['organization'];
    $role = $_POST["role"];

    // include db connect class
    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();

    $result = mysql_query("INSERT INTO users VALUES('$userId','$password','$email','$name','$contact','$organization', '$role');");

    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Account successfully created.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Account creation failed.";

        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>
