<?php
// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once '../config/Database.php';
include_once '../model/Relationship.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate Relationship object
$user = new Relationship($db);

// Get raw posted data
$data = json_decode(file_get_contents("php://input"));

$user->friend_one_id = $data->friend_one_id;
$user->friend_two_id = $data->friend_two_id;
$user->friend_status = $data->friend_status;
$user->friend_action_user_id = $data->friend_action_user_id;

// Insert into DB
if ($user->create()) {
    echo json_encode(
            array('message' => 'New Relationship Created')
    );
} else {
    echo json_encode(
            array('message' => 'New Relationship Not Created')
    );
}
?>