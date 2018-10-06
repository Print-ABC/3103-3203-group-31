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
}

?>