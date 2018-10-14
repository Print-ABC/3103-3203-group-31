<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once '../config/Database.php';
include_once '../model/User.php';
include_once '../functions.php';

// Initialize response array
$response["success"] = false;

// Instantiate DB & connect
$database = new Database();
$conn = $database->connect();

// Instantiate User object
$user = new User($conn);

// Get raw posted data
$input = json_decode(file_get_contents("php://input"), true);

//Check for Mandatory parameters
if (isset($input['user_username']) && isset($input['user_password'])) {
    $user->user_username = $input['user_username'];
    $input_password = $input['user_password'];
    
    // Retrieve login credentials using username
    if($user->retrieveLoginCred()){
        if((hash_password($input_password, $user->salt) == $user->user_password)){
            $response["message"] = "Login successful";
            $response["user_name"] = $user->user_name;
            $response["user_role"] = $user->user_role;
            $response["success"] = true;
        } else {
            $response["message"] = "Invalid username and password combination";
        }
    } else {
        $response["message"] = "Invalid username and password combination";
    }
} else {
    $response["message"] = "Missing input fields";
}
echo json_encode($response);
