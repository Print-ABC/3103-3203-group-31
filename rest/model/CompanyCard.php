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
        // Fetch data
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        $this->org_card_name = $row['org_card_name'];
        $this->org_card_organization = $row['org_card_organization'];
        $this->org_card_job_title = $row['org_card_job_title'];
        $this->org_card_contact = $row['org_card_contact'];
        $this->org_card_email = $row['org_card_email'];
    }

    public function create() {
        // Create query
        $query = 'INSERT INTO ' . $this->table . ' SET org_user_id = UNHEX(:org_user_id), org_card_name = :org_card_name, org_card_organization = :org_card_organization, org_card_job_title = :org_card_job_title, org_card_contact = :org_card_contact, org_card_email = :org_card_email';
        error_log($this->org_user_id);
        error_log($this->org_card_name);
        error_log($this->org_card_organization);
        error_log($this->org_card_job_title);
        error_log($this->org_card_contact);
        error_log($this->org_card_email);
        // Prepare statement
        $stmt = $this->conn->prepare($query);
        // Clean data
        $this->org_user_id = htmlspecialchars(strip_tags($this->org_user_id));
        $this->org_card_name = htmlspecialchars(strip_tags($this->org_card_name));
        $this->org_card_organization = htmlspecialchars(strip_tags($this->org_card_organization));
        $this->org_card_job_title = htmlspecialchars(strip_tags($this->org_card_job_title));
        $this->org_card_contact = htmlspecialchars(strip_tags($this->org_card_contact));
        $this->org_card_email = htmlspecialchars(strip_tags($this->org_card_email));
        // Bind data
        $stmt->bindParam(':org_user_id', $this->org_user_id);
        $stmt->bindParam(':org_card_name', $this->org_card_name);
        $stmt->bindParam(':org_card_organization', $this->org_card_organization);
        $stmt->bindParam(':org_card_job_title', $this->org_card_job_title);
        $stmt->bindParam(':org_card_contact', $this->org_card_contact);
        $stmt->bindParam(':org_card_email', $this->org_card_email);
        // Execute query
        if ($stmt->execute()) {
            return true;
        }
        // Print error if something goes wrong
        printf("Error: %s.\n", $stmt->error);
        return false;
    }

    // Check if user has already created a card
    function cardExists() {
        $query = "SELECT HEX(org_user_id) as org_user_id FROM " . $this->table . " WHERE HEX(org_user_id) = :org_user_id";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":org_user_id", $this->org_user_id);
        $stmt->execute();
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        if ($row > 0) {
            return true;
        } else {
            return false;
        }
    }

}

?>