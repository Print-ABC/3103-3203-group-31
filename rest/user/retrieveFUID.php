<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../config/Database.php';
include_once '../model/User.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate student card object
$user = new User($db);

// Get user ID
$user->user_friend_id = isset($_GET['id']) ? $_GET['id'] : die();

// Get post
$user->getUserByFID();

// Create JSON array
$user_arr = array(
    'user_username' => $user->user_username,
    'user_name' => $user->user_name,
    'user_password' => $user->user_password,
    'user_contact' => $user->user_contact,
    'user_email'=>$user->user_email,
    'user_role' => $user->user_role,
    'user_friend_id' => $user->user_friend_id,
    'user_uid' => $user->user_id
);

// Make JSON
print_r(json_encode($user_arr));
?>