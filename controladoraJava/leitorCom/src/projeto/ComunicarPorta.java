package projeto;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class ComunicarPorta {
	private OutputStream serialOut;
	private SerialPort port;
	private int taxa;
	private String portaCOM;
	public int numPorta = 0;
	public boolean conectado = false;
	int tentativas = 0;

	
	// public ControlePorta(String portaCOM, int taxa)
	public ComunicarPorta(int taxa) {
		if (System.getProperties().get("os.name").toString()
				.contains("Windows")) {
			this.portaCOM = "COM";// Windows
		} else {
			this.portaCOM = "/dev/ttyUSB";// Linux
		}
		this.taxa = taxa;
		this.initialize();
	}

	private void initialize() {
		do {
			abrirPorta();
		} while (!conectado);

		System.out.println("CONECTADO NA PORTA: " + portaCOM + numPorta
				+ " TAXA DE:" + taxa);
		close();
	}

	public boolean abrirPorta() {

		try {

			// Define uma variável portId do tipo CommPortIdentifier para
			// realizar a comunicação serial
			CommPortIdentifier portId = null;
			try {
				// Tenta verificar se a porta COM informada existe
				portId = CommPortIdentifier.getPortIdentifier(this.portaCOM
						+ numPorta);
			} catch (NoSuchPortException npe) {

				// Caso a porta COM não exista será exibido um erro
				// System.out.println("Porta COM não encontrada.");
				numPorta++;
			}

			// Abre a porta COM
			port = (SerialPort) portId.open("Comunicação serial", this.taxa);

			// Define parametros das portas
			port.setSerialPortParams(this.taxa, // taxa de transferência da
												// porta serial
					SerialPort.DATABITS_8, // taxa de 10 bits 8 (envio)
					SerialPort.STOPBITS_1, // taxa de 10 bits 1 (recebimento)
					SerialPort.PARITY_NONE); // receber e enviar dados

			serialOut = port.getOutputStream();
			conectado = true;
		} catch (Exception e) {
			// e.printStackTrace();

		}
		return conectado;
	}

	public void close() {
		try {
			serialOut.close();
		} catch (IOException e) {
			System.out.println("Não foi possível fechar porta COM.");
		}
	}

	public void enviaDados(int opcao) {
		// System.out.println("Enviando Solicitação");
		try {
			// abrirPorta();
			// System.out.println("Entrou no try");
			serialOut.write(opcao);// escreve o valor na porta serial para ser
									// enviado
		} catch (IOException ex) {
			System.out.println("Não foi possível enviar dados. ");
		}

	}

	public String recebeDados() {
		System.out.println("Por Favor, aguarde!");
		String armazenamento = "";
		BufferedReader input = null;
		if (tentativas < 20) {
			try {
				// abrirPorta();
				input = new BufferedReader(new InputStreamReader(
						this.port.getInputStream()));

				do {
					armazenamento += input.readLine();
				} while (armazenamento.contains("\n"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				// e1.printStackTrace();

			}
		} else
			System.out.println("problemas ao acessar...");
		try {
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		close();
		return armazenamento;
	}
}
