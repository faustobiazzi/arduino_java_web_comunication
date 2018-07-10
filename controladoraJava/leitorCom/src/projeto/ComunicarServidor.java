package projeto;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;



import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
//import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
//import org.apache.http.protocol.HttpContext;




public class ComunicarServidor
{
	//IP Felipe = 172.16.0.61 - 10.198.3.37:9000/device/refresh" 
	String ip = "10.198.3.37";//"10.198.0.106";
	int porta = 9000;
	String folder = "/device/refresh";
	public String txtResposta;
	
	public String EnviarServidor(String _dados) throws UnsupportedEncodingException
	{
		
		String dados = _dados.replace("\\", "");
		System.out.println("recebi DADOS do anemometro: "+dados);
		
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost("http://"+ip+":"+porta+folder);
		//HttpPost post = new HttpPost("http://10.198.3.37:9000/device/refresh");
		post.addHeader("Content-Type","application/json; charset=UTF-8");
		StringEntity entity = new StringEntity (dados);
							   				//{\"latitude\":2.852036,\"longitude\":7.192838,\"id\":\"1\",\"area\":5,\"actuators\":{\"LED\":\"0\"},\"sensors\":{\"Vento\":\"2\"},\"type\":\"Anemometer\"}"); //json passado pelo Filipe
			 								//{\"latitude\":0.000000,\"longitude\":0.000000,\"id\":\"1\",\"area\":5,\"actuators\":{\"LED\":\"0\"},\"sensors\":{\"Vento\":\"0\"},\"type\":\"Anemometer\"}
	
		post.setEntity(entity);
		ResponseHandler<String> handler =new ResponseHandler<String>()
		{

			@Override
			public String handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException 
			{
				System.out.println("Dados retornaram, obtendo resposta");
				InputStream resposta = response.getEntity().getContent();
				System.out.println("Coteúdo obtido");
				txtResposta = parsear (resposta);
				
				return "";
				
			}
		};
		try 
		{
			System.out.println("Enviando dados para o servidor");
			System.out.println(client.execute(post,handler));
		} 
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			System.out.println("Não houve resposta do servidor");
		}
		
		try 
		{
			System.out.println("Fechando cliente");
			client.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return txtResposta;
	}  
	
	public String parsear (InputStream _resposta)
	{
		System.out.println("Começando a parsear");
		String json = "";
		
		Scanner texto = new Scanner (_resposta);
		while (texto.hasNext())
		{
			json += texto.next();
			
		};
		
		System.out.println(json);
		
		//System.out.println(json);
		texto.close();
		
	
		String textosemChIN = json.replace("{", "");
		String textosemChOut =textosemChIN.replace("}", "");
		String textosemBarra = textosemChOut.replace("\\", "");
		String textodividido1[] = textosemBarra.split(",");
		String textolinhalinha ="";
		for(String divisao : textodividido1) 
		{
			String textodividido2[] = divisao.split(":");
			for(String divisao2 : textodividido2) 
			{
				divisao2 = divisao2.replace("\"", "");
				textolinhalinha += divisao2+"\n";
			}
		}
		
		//System.out.println(textolinhalinha);		
		return (textolinhalinha);
	}
}	



