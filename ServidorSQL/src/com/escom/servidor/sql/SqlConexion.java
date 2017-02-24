package com.escom.servidor.sql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class SqlConexion extends Thread {
	protected Socket cliente;
	protected BufferedReader entrada;
	protected PrintStream salida;
	protected String consulta;

	public SqlConexion(Socket cliente) {
		try {
			this.cliente = cliente;
			this.entrada = new BufferedReader(new InputStreamReader(this.cliente.getInputStream()));
			this.salida = new PrintStream(this.cliente.getOutputStream());

		} catch (IOException e) {
			e.printStackTrace();
			try {
				this.cliente.close();
			} catch (IOException exception) {

			}
			return;
		}

		this.start();
	}

	private void ejecutaSQL() {
		Connection connection;
		Statement statement;
		ResultSet resultSet;
		ResultSetMetaData resultSetMetaData;
		boolean existenMasResultados;
		String driverClass = "com.mysql.jdbc.Driver";
		String urlConection = "jdbc:mysql://localhost/test";
		String usuario = "root", clave = "", registro;
		int numeroColumnas, i;
		try {
			Class.forName(driverClass);
			connection = DriverManager.getConnection(urlConection, usuario, clave);
			statement = connection.createStatement();
			resultSet = statement.executeQuery(this.consulta);
			existenMasResultados = resultSet.next();
			if (!existenMasResultados) {
				this.salida.println("No hay resultados");
				return;
			}
			resultSetMetaData = resultSet.getMetaData();
			numeroColumnas = resultSetMetaData.getColumnCount();
			while (existenMasResultados) {
				registro = "";
				for (i = 1; i <= numeroColumnas; i++) {
					registro = registro.concat(resultSet.getString(i)).concat(" ");
				}
				this.salida.println(registro);
				System.out.println(registro);
				existenMasResultados = resultSet.next();
			}
			resultSet.close();
			statement.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				this.consulta = this.entrada.readLine();
				System.out.println("Consulta a ejecutar: ".concat(this.consulta).concat(";"));
				this.ejecutaSQL();
			}
		} catch (IOException e) {

		} finally {
			try {
				this.cliente.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
