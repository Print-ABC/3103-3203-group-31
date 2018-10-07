<?php
// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once '../config/Database.php';
include_once '../model/CompanyCard.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate CompanyCard object
$user = new CompanyCard($db);

// Get raw posted data
$data = json_decode(file_get_contents("php://input"));

$user->org_user_id = $data->org_user_id;
$user->org_card_name = $data->org_card_name;
$user->org_card_organization = $data->org_card_organization;
$user->org_card_job_title = $data->org_card_job_title;
$user->org_card_contact = $data->org_card_contact;
$user->org_card_email = $data->org_card_email;

// Insert into DB
if ($user->create()) {
    echo json_encode(
            array('message' => 'New Organization Card Created')
    );
} else {
    echo json_encode(
            array('message' => 'New Student Card Not Created')
    );
}
?>