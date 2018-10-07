<?php
// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once '../config/Database.php';
include_once '../model/StudentCard.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate StudentCard object
$user = new StudentCard($db);

// Get raw posted data
$data = json_decode(file_get_contents("php://input"));

$user->stu_user_id = $data->stu_user_id;
$user->stu_card_name = $data->stu_card_name;
$user->stu_card_email = $data->stu_card_email;
$user->stu_card_contact = $data->stu_card_contact;
$user->stu_card_course = $data->stu_card_course;

// Insert into DB
if ($user->create()) {
    echo json_encode(
            array('message' => 'New Student Card Created')
    );
} else {
    echo json_encode(
            array('message' => 'New Student Card Not Created')
    );
}
?>