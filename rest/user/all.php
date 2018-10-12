<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../config/Database.php';
include_once '../model/User.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate CompanyCard object
$user = new User($db);

// Query DB
$result = $user->getAll();

// Get row count
$num = $result->rowCount();

// Check if any posts
if ($num > 0) {
    // JSON array
    $users = array();
    $i = 0;
    while ($row = $result->fetch(PDO::FETCH_ASSOC)) {
        extract($row);

        $item = array(
            'user_name' => $user_username,
            'user_username' => $user_name,
            'user_password' => $user_password,
            'user_contact' => $user_contact,
            'user_email'=>$user_email,
            'user_role' => $user_role,
            'user_friend_id' => $user_friend_id,
            'user_id' => $user_id
        );

        // Push to array
        $users['users'][$i] = $item;
        $i++;
    }
    // Turn to JSON & output
    echo json_encode($users);
} else {
    // No cards found
    echo json_encode(
            array('message' => 'No Users Found'));
}
?>