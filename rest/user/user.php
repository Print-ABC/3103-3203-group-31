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
$user->user_id = isset($_GET['id']) ? $_GET['id'] : die();

// Get post
$user->getUser();

// Create JSON array
$user_arr = array(
    'name' => $user->user_username,
    'username' => $user->user_name,
    'password' => $user->user_password,
    'contact' => $user->user_contact,
    'user-email'=>$user->user_email,
    'user-role' => $user->user_role,
    'uid-friend' => $user->user_friend_id,
    'uid' => $user->user_id
);

// Make JSON
print_r(json_encode($user_arr));
?>