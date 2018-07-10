package projeto;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

class Automatico extends TimerTask {
	ControlarArduino anemometro = new ControlarArduino();
	ComunicarServidor sendserver = new ComunicarServidor();
	public String resposta = "";
	int acao = 3;
	Integer id = null;

	public void run() {

		String armazenamento = anemometro.arduino.recebeDados();
		System.out.println(armazenamento);

		try {
			if (armazenamento != null && !(armazenamento.isEmpty())) {
				resposta = (sendserver.EnviarServidor(armazenamento));
				System.out.println(resposta);

				tratarAcao(resposta);
			}

			else
				System.out.println("vazio");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void tratarAcao(String info) {

		Scanner texto = new Scanner(info);
		// System.out.println("entrou no tratamento de acao");
		do {
			// System.out.println("entrou no DO");
			if (texto.nextLine().contains("LED")) {
				// System.out.println("entrou no if");
				acao = texto.nextInt();
				// System.out.println("Valor da acao:" +acao);

			}
		} while (texto.hasNextLine());
		texto.close();
		// System.out.println(acao);

		if (acao == 0) {
			acao = 2;// desliga led

			anemometro.arduino.enviaDados(acao);
		} else {

			anemometro.arduino.enviaDados(acao);
		}
	}

}

public class ControlarArduino {
	ComunicarPorta arduino;
	ComunicarServidor sendserver;

	public ControlarArduino() {
		int bauds = 115200;
		arduino = new ComunicarPorta(bauds);

	}

	public static void main(String[] args) throws UnsupportedEncodingException {

		Timer timer = new Timer();
		timer.schedule(new Automatico(), 0, 5000);
	}
}
