package sistemaDistribuido.sistema.clienteServidor.Alex;
//Mart�nez Rej�n,Mois�s Alexander
//207435363
//Taller de sistemas operativos secci�n D03
//Modificado para pr�cica 1 y 2

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ConectorDNS;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.sistema.rpc.modoUsuario.Datos;
import sistemaDistribuido.util.Escribano;
import sistemaDistribuido.util.Pausador;

/**
 * 
 */
public class ProcesoServidor extends Proceso{

	/**
	 * 
	 */
	public ProcesoServidor(Escribano esc){
		super(esc);
		start();
	}

	/**
	 * 
	 */
	public void run(){
            int codop,tam_mensaje,i,contador=12;	
            String solicitud="";
            imprimeln("Proceso servidor en ejecucion.");
            byte[] solServidor=new byte[1024];
            byte[] respServidor = null;
            String datos="";
            imprimeln("Inicio de proceso");

            Datos asa;
            int idUnico;
            imprimeln("El servidor se esta registrando en Servidor de Nombres");
            
            try 
            {
                asa = new Datos(Nucleo.dameIdProceso(), InetAddress.getLocalHost().getHostAddress());
                
            } catch (UnknownHostException ex) {
                asa = null;
                imprimeln("No se puede obtener ip local");
                Logger.getLogger(sistemaDistribuido.sistema.clienteServidor.modoUsuario.ProcesoServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            idUnico = ConectorDNS.exportarInterfaz("Server Alex", "1.0", asa);
            
            Nucleo.solicitarBuzon(dameID());
            
            while(continuar()){
                imprimeln("Invocando a receive");
                Nucleo.receive(dameID(),solServidor);
                tam_mensaje=(int)solServidor[8];
                codop=(int)solServidor[10];
                imprimeln("Procesando petición recibida del cliente");
                for(i=0;i<tam_mensaje;i++,contador++)
                {
                 solicitud=solicitud+((char)solServidor[contador]);
                }
                respServidor=new byte[1024];
                datos=Archivos(codop,solServidor,respServidor,((int)solServidor[0]),((int)solServidor[4]));
                imprimeln("el cliente solicitó: "+datos);
                Pausador.pausa(1000);  //sin esta l�nea es posible que Servidor solicite send antes que Cliente solicite receive
                imprimeln("Generando mensaje a ser enviado");
                imprimeln("llenando los campos necesarios");
                imprimeln("Señalando al núcleo para ser envío de mensaje");
                imprimeln("enviando respuesta");
                Pausador.pausa(2000);
                Nucleo.send(merge_bytes_int(solServidor),respServidor);
            }
            
            ConectorDNS.deregistrarInterfaz("Server Alex", "1.0", idUnico);
	}
       
        public String Archivos(int codop, byte [] solServidor, byte [] respServidor,int origen,int destino)
        {
        
         pack_id_into_response(respServidor);
         pack_client_id_into_response(respServidor,solServidor);
         switch(codop)
         {
             case 0://crear
                 imprimeln("Crear archivo");
                 respServidor[8]=(byte)("Se ha creado el archivo").length();
                 System.arraycopy("Se ha creado el archivo".getBytes(),0,respServidor,10,("Se ha creado el archivo").length());
                 return "Crear";
             case 1://eliminar
                 imprimeln("Eliminar archivo");
                 respServidor[8]=(byte)("Se ha eliminado el archivo").length();
                 System.arraycopy("Se ha eliminado el archivo".getBytes(),0,respServidor,10,("Se ha eliminado el archivo").length());
                 return "Eliminar";
             case 2://leer
                 imprimeln("Leer");
                 respServidor[8]=(byte)("Se ha leído del archivo").length();
                 System.arraycopy("Se ha leído del archivo".getBytes(),0,respServidor,10,("Se ha leído del archivo").length());
                 return "Leer";
             case 3://escribir
                 imprimeln("Escribir");
                 respServidor[8]=(byte)("Se ha escrito en el archivo").length();
                 System.arraycopy("Se ha escrito en el archivo".getBytes(),0,respServidor,10,("Se ha escrito en el archivo").length());
                 return "Escribir";
         }
         return "Error";
        }
        
 
        
        public void pack_id_into_response(byte[] respServidor){
            respServidor[0] = (byte) (dameID()>>>24);
            respServidor[1] = (byte) (dameID()>>>16);
            respServidor[2] = (byte) (dameID()>>>8);
            respServidor[3] = (byte) (dameID());
        }
        
        public void pack_client_id_into_response(byte[] respServidor, byte[] solServidor){
            byte[] arr2 = Arrays.copyOfRange(solServidor, 0, 4);
            int client_id = merge_bytes_int(arr2);
            respServidor[4] = (byte) (client_id >>> 24);
            respServidor[5] = (byte) (client_id >>> 16);
            respServidor[6] = (byte) (client_id >>> 8);
            respServidor[7] = (byte) (client_id);
        }
        public int merge_bytes_int(byte[] bytes){
            int aux = 0;
            for (int i=0; i<4 && i<bytes.length; i++) {
               aux <<= 8;
               aux |= (int)bytes[i] & 0xFF;
           }
           return aux;
       }
}
