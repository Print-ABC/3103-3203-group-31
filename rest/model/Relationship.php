<?php
/* 
 * Author   : Tan Suan Khai
 * Created  : 06 OCT 2018
 */

class Relationship {
    // DB stuff
    private $conn;
    private $table = 'relationship';
    
    // Card Properties
    public $friend_one_id;
    public $friend_two_id;
    public $friend_status;
    public $friend_action_user_id;
    
    // Constructor with DB
    public function __construct($db) {
        $this->conn = $db;
    }
    
    // Return all relationships from DB
    public function getAll() {
        // Create query
        $query = 'SELECT * FROM ' . $this->table;
        // Prepare statement
        $stmt = $this->conn->prepare($query);
        // Execute query
        $stmt->execute();
        return $stmt;
    }
    
    // Return all relationships from DB
    public function getLink() {
        // Create query
        $query = 'SELECT friend_one_id, friend_two_id, friend_status, friend_action_user_id FROM ' . $this->table
                . ' WHERE friend_one_id = ?';
        // Prepare statement
        $stmt = $this->conn->prepare($query);
        // Execute query
        $stmt->execute();
    }
}

?>