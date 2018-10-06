<?php
class Database {
    // DB Params
    private $host = 'localhost';
    private $db_name = 'ncshare';
    private $user = 'root';
    private $password = '';
    private $conn;
    
    // DB Connect
    public function connect(){
        $this->conn = null;
        
        try{
            $this->conn = new PDO('mysql:host=' . $this->host . ';dbname=' . $this->db_name, $this->user, $this->password);
            $this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        } catch (PDOException $ex) {
            echo 'Connection Error: ' . $ex->getMessage();
        }
        
        return $this->conn;
    }
}
?>