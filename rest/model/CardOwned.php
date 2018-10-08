<?php
/* 
 * Author   : Tan Suan Khai
 * Created  : 06 OCT 2018
 */

class CardOwned {
    // DB stuff
    private $conn;
    private $table = 'card_owned';
    
    // Card Properties
    public $co_user_id_owner;
    public $co_user_id_owned;
    public $co_username;
    public $co_date_added;
    
    // Constructor with DB
    public function __construct($db) {
        $this->conn = $db;
    }
    
    // Return all cards from DB
    public function getAll() {
        // Create query
        $query = 'SELECT HEX(co_user_id_owner) as co_user_id_owner, HEX(co_user_id_owned) as co_user_id_owned, co_username, co_date_added FROM ' . $this->table;
        // Prepare statement
        $stmt = $this->conn->prepare($query);
        // Execute query
        $stmt->execute();
        return $stmt;
    }
    
    // Insert a new card ownership
    public function create() {
        // Create query
        $query = 'INSERT INTO ' . $this->table . ' SET co_user_id_owner = UNHEX(:co_user_id_owner), co_user_id_owned = UNHEX(:co_user_id_owned), co_username = :co_username, co_date_added = CURRENT_TIMESTAMP()';
        // Prepare statement
        $stmt = $this->conn->prepare($query);
        // Clean data
        $this->co_user_id_owner = htmlspecialchars(strip_tags($this->co_user_id_owner));
        $this->co_user_id_owned = htmlspecialchars(strip_tags($this->co_user_id_owned));
        $this->co_username = htmlspecialchars(strip_tags($this->co_username));
        // Bind data
        $stmt->bindParam(':co_user_id_owner', $this->co_user_id_owner);
        $stmt->bindParam(':co_user_id_owned', $this->co_user_id_owned);
        $stmt->bindParam(':co_username', $this->co_username);
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