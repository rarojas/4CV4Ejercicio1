package com.escom.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClienteSql {

	public static void main(String[] args) {
		new ClienteSql();
	}

	private final int PUERTO = 6666;
	private String host = "localhost";
	private Socket socket;
	protected BufferedReader entrada;
	protected PrintStream salida;

	public ClienteSql() {
		try {
			this.socket = new Socket(this.host, this.PUERTO);
			this.entrada = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.salida = new PrintStream(this.socket.getOutputStream());

			while (true) {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Escriba una consulta sql a ejecutar");
				String consulta = bufferedReader.readLine();
				if (consulta == "")
					break;
				this.salida.println(consulta);
				String resultado = this.entrada.readLine();
				if (resultado != null)
					System.out.println(resultado);
			}
			this.entrada.close();
			this.salida.close();
			this.socket.close();
		} catch (UnknownHostException e) {
			System.out.println("Host desconocido");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error de conexion");
			e.printStackTrace();
		}
	}
}
