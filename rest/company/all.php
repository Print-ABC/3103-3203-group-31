<?php
// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../config/Database.php';
include_once '../model/CompanyCard.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate CompanyCard object
$c_card = new CompanyCard($db);

// Query DB
$result = $c_card->getAll();

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
            'uid' => $org_user_id,
            'card-name' => $org_card_name,
            'organization' => $org_card_organization,
            'job-title' => $org_card_job_title,
            'contact' => $org_card_contact,
            'email' => $org_card_email
        );
        
        // Push to array
        $owned_arr['organization-cards'][$i] = $item;
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