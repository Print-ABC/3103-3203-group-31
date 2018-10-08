<?php
// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once '../config/Database.php';
include_once '../model/CardOwned.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate CardOwned object
$user = new CardOwned($db);

// Get raw posted data
$data = json_decode(file_get_contents("php://input"));

$user->co_user_id_owner = $data->co_user_id_owner;
$user->co_user_id_owned = $data->co_user_id_owned;
$user->co_username = $data->co_username;

// Insert into DB
if ($user->create()) {
    echo json_encode(
            array('message' => 'New Ownership Created')
    );
} else {
    echo json_encode(
            array('message' => 'New Ownership Not Created')
    );
}
?>