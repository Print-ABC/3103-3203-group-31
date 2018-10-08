<?php
// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../config/Database.php';
include_once '../model/CompanyCard.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate company card object
$user = new CompanyCard($db);

// Get user ID
$user->org_user_id = isset($_GET['id']) ? $_GET['id'] : die();

// Get post
$user->getCard();

// Create JSON array
$user_arr = array(
    'uid' => $user->org_user_id,
    'card-name' => $user->org_card_name,
    'organization' => $user->org_card_organization,
    'job-title' => $user->org_card_job_title,
    'contact' => $user->org_card_contact,
    'email' => $user->org_card_email
);

  // Make JSON
  print_r(json_encode($user_arr));
?>