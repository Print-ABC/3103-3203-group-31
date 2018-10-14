<?php

$random_salt_length = 64;

/**
 * Queries the database and checks whether the user already exists
 * 
 * @param $username
 * 
 * @return
 */
function userExists($username, $email) {
    $query = "SELECT user_username FROM user WHERE user_username = :username OR user_email = :email";
    global $conn;
    $stmt = $conn->prepare($query);
    $stmt->bindParam(":username", $username);
    $stmt->bindParam(":email", $email);
    $stmt->execute();
    $row = $stmt->fetch(PDO::FETCH_ASSOC);
    if ($row > 0) {
        $conn = null;
        return true;
    } else {
        $conn = null;
        return false;
    }
}

/**
 * Creates a unique Salt for hashing the password
 * 
 * @return
 */
function getSalt() {
    global $random_salt_length;
    return bin2hex(openssl_random_pseudo_bytes($random_salt_length));
}

/**
 * Creates password hash using the Salt and the password
 * 
 * @param $password
 * @param $salt
 * 
 * @return
 */
function hash_password($password, $salt) {
    global $random_salt_length;
    return hash_pbkdf2("sha256", $password, $salt, 1000, $random_salt_length);
}
