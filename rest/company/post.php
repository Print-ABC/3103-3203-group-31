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

// Instantiate response array
$response["success"] = false;

// Get raw posted data
$data = json_decode(file_get_contents("php://input"), true);

if (isset($data['org_user_id']) && isset($data['org_card_name']) && isset($data['org_card_organization']) &&
        isset($data['org_card_job_title']) && isset($data['org_card_contact']) && isset($data['org_card_email'])) {
    $user->org_user_id = $data['org_user_id'];
    $user->org_card_name = $data['org_card_name'];
    $user->org_card_organization = $data['org_card_organization'];
    $user->org_card_job_title = $data['org_card_job_title'];
    $user->org_card_contact = $data['org_card_contact'];
    $user->org_card_email = $data['org_card_email'];

    // Check if user already created a card
    if (!$user->cardExists()) {
        // Insert into DB
        if ($user->create()) {
            $response["message"] = "New card created";
            $response["success"] = true;
        } else {
            $response["message"] = "Failed to create card";
        }
    } else {
        $response["message"] = "Card already exists";
    }
} else {
    $response["message"] = "Missing input fields";
}
echo json_encode($response);
?>