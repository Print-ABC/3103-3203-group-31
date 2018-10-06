<?php
/* 
 * Author   : Tan Suan Khai
 * Created  : 06 OCT 2018
 */

class CompanyCard {
    // DB stuff
    private $conn;
    private $table = 'organization_card';
    
    // Card Properties
    public $org_user_id;
    public $org_card_name;
    public $org_card_organization;
    public $org_card_job_title;
    public $org_card_contact;
    public $org_card_email;
    
    // Constructor with DB
    public function __construct($db) {
        $this->conn = $db;
    }
    
    // Return all company cards from DB
    public function getAll() {
        // Create query        
        $query = 'SELECT HEX(org_user_id) as org_user_id, org_card_name, org_card_organization, org_card_job_title, org_card_contact, org_card_email FROM ' . $this->table;
        
        // Prepare statement
        $stmt = $this->conn->prepare($query);
        // Execute query
        $stmt->execute();
        return $stmt;
    }
    
    // Get one card
    public function getCard() {
        // Create query
        $query = 'SELECT HEX(org_user_id) as org_user_id, org_card_name, org_card_organization, org_card_job_title, org_card_contact, org_card_email FROM ' . $this->table
                . ' WHERE HEX(org_user_id) = ? ';
        
        // Prepare statement
        $stmt = $this->conn->prepare($query);
        
        // Bind ID
        $stmt->bindParam(1, $this->org_user_id);
        
        // Execute query
        $stmt->execute();
        
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        
        $this->org_card_name = $row['org_card_name'];
        $this->org_card_organization = $row['org_card_organization'];
        $this->org_card_job_title = $row['org_card_job_title'];
        $this->org_card_contact = $row['org_card_contact'];
        $this->org_card_email = $row['org_card_email'];
    }
}
?>