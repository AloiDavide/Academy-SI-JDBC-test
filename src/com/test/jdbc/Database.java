package com.test.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.jdbc.MysqlDataSource;

public class Database {
	private Connection con;
	private String dbName;
	
	private String serverName = "localhost";
	private int portNumber = 3306;
	private String username = "root";
	private String password = "admin";
	
	
	
	

	//Richiede una connessione senza nome per creare il nuovo database se non esiste, e aggiorna dbName, 
	//poi chiude la connessione anonima.
	//Da chiamare ogni volta che ci si vuole connettere a un db nuovo.
	public void initializeDB(String name) throws SQLException {
		this.dbName = null;
		
		String sql = "CREATE DATABASE IF NOT EXISTS " + name;
		PreparedStatement ps = getConnection().prepareStatement(sql);
		ps.executeUpdate();
		
		this.dbName = name;
		
		ps.close();
		con.close();
		this.con = null;
		
	}
	
	
	
	//alternativamente gestibile passando il nome a paramentro
	public Connection getConnection() throws SQLException {
		//nel caso un nome per il db non sia definito, apri una connessione che permetta a initializeDB di funzionare.
		if(this.dbName == null) {
			MysqlDataSource dataSource = new MysqlDataSource();
			dataSource.setServerName(this.serverName); //127.0.0.1
			dataSource.setPortNumber(this.portNumber);
			dataSource.setUser(this.username);
			dataSource.setPassword(this.password);
			
			this.con = dataSource.getConnection();
			return con;
		}
		
		//se il db esiste e il suo nome è stato memorizzato possiamo restituire una connessione completa
		else {
			if (this.con == null) {
				MysqlDataSource dataSource = new MysqlDataSource();
				dataSource.setServerName(this.serverName); //127.0.0.1
				dataSource.setPortNumber(this.portNumber);
				dataSource.setUser(this.username);
				dataSource.setPassword(this.password);
				
				dataSource.setDatabaseName(this.dbName);
			
				this.con = dataSource.getConnection();
			}
			return this.con;
		}
	}
	

	public void createTable(String name) throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS "+name+" (id INT PRIMARY KEY,"
						+ "cognome VARCHAR(255),"
						+ "nome VARCHAR(255));";
		PreparedStatement ps = getConnection().prepareStatement(sql);

		ps.executeUpdate();
		
		System.out.println("Created table "+name );

		ps.close();
	}
	
	public void insertUser(int id, String cognome,String nome) throws SQLException {
		String sql = "INSERT INTO utente(id, cognome, nome) VALUES (?, ?,?)";
		PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		ps.setInt(1, id);
		ps.setString(2, nome);
		ps.setString(3, cognome);
		ps.executeUpdate();

		ps.close();

	}
	
	public ResultSet doQuery(String sql) throws SQLException {
		PreparedStatement ps = getConnection().prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		return rs;
		

	}

	public void closeConnection() throws SQLException {
		if (this.con != null) {
			this.con.close();
		}
	}
	
	
	
	
	//getter e setter
	
	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
