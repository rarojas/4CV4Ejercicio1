package com.escom.servidor.sql;

import java.net.ServerSocket;
import java.net.Socket;

public class Servidor extends Thread {
	public static final int PUERTO = 6666;

	public static void main(String[] arg) {
		new Servidor();
	}

	ServerSocket socketServidor;

	public Servidor() {
		try {
			this.socketServidor = new ServerSocket(PUERTO);
			System.out.println("Corriendo...");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.start();
	}

	@Override
	public void run() {
		try {
			while (true) {
				Socket cliente = this.socketServidor.accept();
				new SqlConexion(cliente);
				System.out.println("Nuevo cliente");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
