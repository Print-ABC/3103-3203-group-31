<?php
// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../config/Database.php';
include_once '../model/StudentCard.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate CompanyCard object
$own = new StudentCard($db);

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
            'uid' => $stu_user_id,
            'card-name' => $stu_card_name,
            'contact' => $stu_card_contact,
            'email' => $stu_card_email,
            'course' => $stu_card_course
        );
        
        // Push to array
        $owned_arr['student-cards'][$i] = $item;
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