<?php
// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../config/Database.php';
include_once '../model/StudentCard.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate student card object
$user = new StudentCard($db);

// Get user ID
$user->stu_user_id = isset($_GET['id']) ? $_GET['id'] : die();

// Get post
$user->getCard();

// Create JSON array
$user_arr = array(
    'uid' => $user->stu_user_id,
    'card-name' => $user->stu_card_name,
    'contact' => $user->stu_card_contact,
    'email' => $user->stu_card_email,
    'course' => $user->stu_card_course
);

  // Make JSON
  print_r(json_encode($user_arr));
?>