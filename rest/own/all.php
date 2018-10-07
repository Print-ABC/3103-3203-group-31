<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../config/Database.php';
include_once '../model/CardOwned.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate CompanyCard object
$user = new CardOwned($db);

// Query DB
$result = $user->getAll();

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
            'co_user_id_owner' => $co_user_id_owner,
            'co_user_id_owned' => $co_user_id_owned,
            'co_username' => $co_username,
            'co_date_added' => $co_date_added
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