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
        
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        
        $this->user_username = $row['user_username'];
        $this->user_name = $row['user_name'];
        $this->user_password = $row['user_password'];
        $this->user_contact = $row['user_contact'];
        $this->user_role = $row['user_role'];
        $this->user_friend_id = $row['user_friend_id'];
        $this->user_id = $row['user_id'];
    }
}
?>