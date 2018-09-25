<?php

$response = array();

// include db connect class
require_once __DIR__ . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

$userid = "xjustus";
$password = "12345";
$email = "justus@sit.com";
$name = "justus chua";
$contact = 99999999;
$organization = "SIT";
$role = "student";

$result = mysql_query("select user_name from users where user_id like '$userid' and user_password like '$password';") or
        die(mysql_error);

if (mysql_num_rows($result) > 0) {

    while ($row = mysql_fetch_array($result)) {
        $retrieve = $row["user_name"];
    }
    $response["success"] = 1;
    echo json_encode($retrieve);
} else {
// no product found
    $response["success"] = 0;
    $response["message"] = "No name found";

// echo no users JSON
    echo json_encode($response);
}
?>