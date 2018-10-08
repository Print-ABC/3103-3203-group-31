<?php
// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once '../config/Database.php';
include_once '../model/User.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate User object
$user = new User($db);

// Get raw posted data
$data = json_decode(file_get_contents("php://input"));

$user->user_username = $data->user_username;
$user->user_name = $data->user_name;
$user->user_password = $data->user_password;
$user->user_contact = $data->user_contact;

// Insert into DB
if ($user->create()) {
    echo json_encode(
            array('message' => 'New User Created')
    );
} else {
    echo json_encode(
            array('message' => 'New User Not Created')
    );
}
?>