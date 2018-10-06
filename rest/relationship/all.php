<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../config/Database.php';
include_once '../model/Relationship.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate CompanyCard object
$own = new Relationship($db);

// Query DB
$result = $own->getAll();

// Get row count
$num = $result->rowCount();

// Check if any posts
if ($num > 0) {
    // JSON array
    $owned_arr = array();
    $i = 0;
    while ($row = $result->fetch(PDO::FETCH_ASSOC)) {
        extract($row);

        $item = array(
            'friend-one-id' => $friend_one_id,
            'friend-two-id' => $friend_two_id,
            'friend-status' => $friend_status,
            'friend-action-uid' => $friend_action_user_id
        );

        // Push to array
        $owned_arr['relations'][$i] = $item;
        $i++;
    }
    // Turn to JSON & output
    echo json_encode($owned_arr);
} else {
    // No cards found
    echo json_encode(
            array('message' => 'No Cards Found'));
}
?>