<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../config/Database.php';
include_once '../model/Relationship.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

$relationship = new Relationship($db);

// Get user friend ID
$relationship->friend_one_id = isset($_GET['id']) ? $_GET['id'] : die();

// Get post
$relationship->getLink();

// Create JSON array
$relationship_arr = array(
    'friend_one_id' => $relationship->friend_one_id,
    'friend_two_id' => $relationship->friend_two_id,
    'friend_status' => $relationship->friend_status,
    'friend_action_user_id' => $relationship->friend_action_user_id
);

// Make JSON
print_r(json_encode($relationship_arr));
?>