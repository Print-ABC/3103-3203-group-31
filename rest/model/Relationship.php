<?php

/*
 * Author   : Tan Suan Khai
 * Created  : 06 OCT 2018
 */

class Relationship {

    // DB stuff
    private $conn;
    private $table = 'relationship';
    // Relationship Properties
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

    // Insert a new relationship
    public function create() {
        // Create query
        $query = 'INSERT INTO ' . $this->table . ' SET friend_one_id = :friend_one_id, friend_two_id = :friend_two_id, friend_status = :friend_status, friend_action_user_id = :friend_action_user_id';
        // Prepare statement
        $stmt = $this->conn->prepare($query);
        // Clean data
        $this->friend_one_id = htmlspecialchars(strip_tags($this->friend_one_id));
        $this->friend_two_id = htmlspecialchars(strip_tags($this->friend_two_id));
        $this->friend_status = htmlspecialchars(strip_tags($this->friend_status));
        $this->friend_action_user_id = htmlspecialchars(strip_tags($this->friend_action_user_id));
        // Bind data
        $stmt->bindParam(':friend_one_id', $this->friend_one_id);
        $stmt->bindParam(':friend_two_id', $this->friend_two_id);
        $stmt->bindParam(':friend_status', $this->friend_status);
        $stmt->bindParam(':friend_action_user_id', $this->friend_action_user_id);
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