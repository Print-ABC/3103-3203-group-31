<?php
/* 
 * Author   : Tan Suan Khai
 * Created  : 06 OCT 2018
 */

class StudentCard {
    // DB stuff
    private $conn;
    private $table = 'student_card';
    
    // Card Properties
    public $stu_user_id;
    public $stu_card_name;
    public $stu_card_email;
    public $stu_card_contact;
    public $stu_card_course;
    
    // Constructor with DB
    public function __construct($db) {
        $this->conn = $db;
    }
    
    // Return all student cards from DB
    public function getAll() {
        // Create query
        $query = 'SELECT HEX(stu_user_id) as stu_user_id, stu_card_name, stu_card_email, stu_card_contact, stu_card_course FROM ' . $this->table;
        // Prepare statement
        $stmt = $this->conn->prepare($query);
        // Execute query
        $stmt->execute();
        return $stmt;
    }
    
    // Get one card
    public function getCard() {
        // Create query
        $query = 'SELECT HEX(stu_user_id) as stu_user_id, stu_card_name, stu_card_email, stu_card_contact, stu_card_course FROM ' . $this->table
                . ' WHERE HEX(stu_user_id) = ? ';
        // Prepare statement
        $stmt = $this->conn->prepare($query);
        // Bind ID
        $stmt->bindParam(1, $this->stu_user_id);
        // Execute query
        $stmt->execute();
        // Fetch data
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        $this->stu_card_name = $row['stu_card_name'];
        $this->stu_card_contact = $row['stu_card_contact'];
        $this->stu_card_email = $row['stu_card_email'];
        $this->stu_card_course = $row['stu_card_course'];
    }
    
    // Insert a new student card
    public function create() {
        // Create query
        $query = 'INSERT INTO ' . $this->table . ' SET stu_user_id = UNHEX(:stu_user_id), stu_card_name = :stu_card_name, stu_card_email = :stu_card_email, stu_card_contact = :stu_card_contact, stu_card_course = :stu_card_course';
        // Prepare statement
        $stmt = $this->conn->prepare($query);
        // Clean data
        $this->stu_user_id = htmlspecialchars(strip_tags($this->stu_user_id));
        $this->stu_card_name = htmlspecialchars(strip_tags($this->stu_card_name));
        $this->stu_card_email = htmlspecialchars(strip_tags($this->stu_card_email));
        $this->stu_card_contact = htmlspecialchars(strip_tags($this->stu_card_contact));
        $this->stu_card_course = htmlspecialchars(strip_tags($this->stu_card_course));
        // Bind data
        $stmt->bindParam(':stu_user_id', $this->stu_user_id);
        $stmt->bindParam(':stu_card_name', $this->stu_card_name);
        $stmt->bindParam(':stu_card_email', $this->stu_card_email);
        $stmt->bindParam(':stu_card_contact', $this->stu_card_contact);
        $stmt->bindParam(':stu_card_course', $this->stu_card_course);
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