<?php
/* 
 * Author   : Tan Suan Khai
 * Created  : 06 OCT 2018
 */

class User {
    // DB stuff
    private $conn;
    private $table = 'user';
    
    // User Properties
    public $user_username;
    public $user_name;
    public $user_password;
    public $user_contact;
    public $user_role;
    public $user_friend_id;
    public $user_id;

    // Constructor with DB
    public function __construct($db) {
        $this->conn = $db;
    }
    
    // Return all users from DB
    public function getAll() {
        // Create query
        $query = 'SELECT user_username, user_name, user_password, user_contact, user_role, user_friend_id, HEX(user_id) as user_id FROM ' . $this->table;
        // Prepare statement
        $stmt = $this->conn->prepare($query);
        // Execute query
        $stmt->execute();
        return $stmt;
    }
    
    public function getUser() {
        // Create query
        $query = 'SELECT user_username, user_name, user_password, user_contact, user_role, user_friend_id, HEX(user_id) as user_id FROM ' . $this->table
                . ' WHERE HEX(user_id) = ? ';
        // Prepare statement
        $stmt = $this->conn->prepare($query);
        // Bind ID
        $stmt->bindParam(1, $this->user_id);
        // Execute query
        $stmt->execute();
        // Fetch data
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        $this->user_username = $row['user_username'];
        $this->user_name = $row['user_name'];
        $this->user_password = $row['user_password'];
        $this->user_contact = $row['user_contact'];
        $this->user_role = $row['user_role'];
        $this->user_friend_id = $row['user_friend_id'];
        $this->user_id = $row['user_id'];
    }
    
    // Insert a new student card
    public function create() {
        // Create query
        $query = 'INSERT INTO ' . $this->table . ' SET user_username = :user_username, user_name = :user_name, user_password = :user_password, user_contact = :user_contact, user_role = 0, user_friend_id = NULL';
        // Prepare statement
        $stmt = $this->conn->prepare($query);
        // Clean data
        $this->user_username = htmlspecialchars(strip_tags($this->user_username));
        $this->user_name = htmlspecialchars(strip_tags($this->user_name));
        $this->user_password = htmlspecialchars(strip_tags($this->user_password));
        $this->user_contact = htmlspecialchars(strip_tags($this->user_contact));
        // Bind data
        $stmt->bindParam(':user_username', $this->user_username);
        $stmt->bindParam(':user_name', $this->user_name);
        $stmt->bindParam(':user_password', $this->user_password);
        $stmt->bindParam(':user_contact', $this->user_contact);
        // Execute query
        if ($stmt->execute()) {
            return true;
        }
        // Print error if something goes wrong
        printf("Error: %s.\n", $stmt->error);
        return false;
    }
}
?>