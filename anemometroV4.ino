
#include <TinyGPS++.h>
#include <SoftwareSerial.h>


SoftwareSerial ss(2,3 );

// variaveis modificaveis
int LED = 0;//DESLIGADO POR PADRÃO
int velVento;
String id ="8";
int area =5;


int dado;

void setup()
{
  Serial.begin(115200); // ConexÃ£o serial (USB/COM)
  ss.begin(9600);//ConexÃ£o com GPS
  pinMode(13, OUTPUT); 
}

void loop()
{
  
  if(Serial.available() > 0) //verifica se existe comunicação com a porta serial
  {
    dado = Serial.read();//lê os dados da porta serial
    if (dado != 0)
      comandos(dado);
  }
  velVento = anemometro();
  gps ();
  if (LED == 1)
    digitalWrite(13, HIGH);
  else
    digitalWrite(13, LOW);
  
 
}

void comandos(int dado)
{
  switch(dado)
  {
  case 1://liga GPS
    LED = 1;
    break;
    
  case 2://Desliga GPS
    LED = 0;
    break;
    
  case 3://Liga Anemometro

   break;
   
  case 4://desliga Anemometro;

   break;
  }
}

int anemometro ()
{
  int vel = analogRead(A2);
  return (vel);
}
void gps ()
{
  delay(500);
  TinyGPSPlus gps; // create gps object
//  while (ss.available() > 0)
//  {
    gps.encode(ss.read());
 // }  
  //"{\"latitude\":-22.8520036,\"longitude\":-47.1272838,\"id\":\"1\",\"area\":5,\"actuators\":{\"LED\":\"0\"},\"sensors\":{\"Vento\":\"222\"},\"type\":\"Anemometer\"}"

    //if (gps.location.isUpdated()|| gps.time.value() != 0)
    
    Serial.print("{");
    Serial.print("\\\"latitude\\\":"); Serial.print(gps.location.lat()-3, 6); 
    Serial.print(",\\\"longitude\\\":"); Serial.print(gps.location.lng()-3, 6);
    Serial.print(",\\\"id\\\":\\\"");Serial.print(id);Serial.print("\\\"");
    Serial.print(",\\\"area\\\":");Serial.print(area);

    //Atuadores
    Serial.print(",\\\"actuators\\\":");
    Serial.print("{");
    Serial.print("\\\"LED\\\":\\");Serial.print("\""); Serial.print(LED); Serial.print("\\\"");
    Serial.print("}");
    //Sensores
    Serial.print(",\\\"sensors\\\":");
    Serial.print("{");
    Serial.print("\\\"Vento\\\":\\");Serial.print("\"");Serial.print(velVento); Serial.print("\\\"");
    Serial.print("}");    
    Serial.print(",\\\"type\\\":\\\"Anemometer\\\"");
    Serial.print("}");Serial.print ("\n");
  }
