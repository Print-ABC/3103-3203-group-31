<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once '../config/Database.php';
include_once '../model/User.php';
include_once '../functions.php';

// Instantiate DB & connect
$database = new Database();
$conn = $database->connect();

// Instantiate User object
$user = new User($conn);

// Initialize response array
$response["success"] = false;

// Get raw posted data
$data = json_decode(file_get_contents("php://input"), true);

if (isset($data['user_username']) && isset($data['user_password']) && isset($data['user_name']) &&
        isset($data['user_contact']) && isset($data['user_email']) && isset($data['user_role'])) {
    $user->user_username = $data['user_username'];
    $user->user_name = $data['user_name'];
    $user->user_password = $data['user_password'];
    $user->user_contact = $data['user_contact'];
    $user->user_email = $data['user_email'];
    $user->user_role = $data['user_role'];
    error_log($user->user_username);
    error_log($user->user_email);

    if (!userExists($user->user_username, $user->user_email)) {
        error_log("Shouldnt go here");

        // Get a unique Salt
        $user->salt = getSalt();

        // Generate a unique password Hash
        $user->password_hash = password_hash(concatPasswordWithSalt($user->user_password, $user->salt), PASSWORD_DEFAULT);
        

        // Insert into DB
        if ($user->create()) {
            $response["message"] = "New user created";
            $response["success"] = true;
        } else {
            $response["message"] = "Failed to create user";
        }
    } else {
        $response["message"] = "Username/Email already exists";
    }
} else {
    $response["message"] = "Missing input fields";
}
echo json_encode($response);
?>