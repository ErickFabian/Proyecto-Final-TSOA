/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baseCSv2.sistemaDistribuido.sistema.clienteServidor.Eduardo;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ConectorDNS;
//import java.util.LinkedList;

//import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ListaBuzones;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.sistema.rpc.modoUsuario.Datos;
//import sistemaDistribuido.sistema.clienteServidor.modoMonitor.MicroNucleo;
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
		imprimeln("Inicio de proceso Servidor");
		byte[] solServidor=new byte[1024];
		byte[] respServidor, arregloArchivo, arregloRespuesta;
		String operacion, respuesta, cadenaArchivo;
		int x, y;
		imprimeln("Solicitando buzon con id "+dameID());
		
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

                idUnico = ConectorDNS.exportarInterfaz("Server Eduardo", "1.0", asa);
		
		Nucleo.solicitarBuzon(dameID());
		while(continuar()){
			imprimeln("Invocando a receive()");
			Nucleo.receive(dameID(),solServidor);
			arregloArchivo = Arrays.copyOfRange(solServidor, 11, 11 + solServidor[10]);
			imprimeln("Procesando petición recibida del cliente");
			switch (get_cod_op_from_request(solServidor)) {
				case 1: operacion = "Crear"; break;
				case 2: operacion = "Eliminar"; break;
				case 3: operacion = "Leer"; break;
				case 4: operacion = "Escribir"; break;
				default: operacion = ""; break;
			}
			cadenaArchivo = new String(arregloArchivo, Charset.forName("UTF-8"));
			imprimeln ("El cliente solicitó " + operacion +
					" el archivo " + cadenaArchivo);
			respServidor=new byte[1024];
			imprimeln("Generando mensaje a ser enviado");
			respuesta = operacion + " el archivo " + cadenaArchivo + 
					" fue realizado con éxito";
			pack_response(solServidor, respServidor);
			/*arregloRespuesta = respuesta.getBytes(Charset.forName("UTF-8"));
			respServidor[8] = (byte) arregloRespuesta.length;
			y = 0;
			x = 9;
			while (x <= respServidor[8] + 8) {
				respServidor[x] = arregloRespuesta[y];
				x++;
				y++;
			}*/
			Pausador.pausa(5000);  //sin esta línea es posible que Servidor solicite send antes que Cliente solicite receive
			imprimeln("Señalamiento al núcleo para envío de mensaje");
			/*respServidor[0] = solServidor[4];
			respServidor[4] = solServidor[0];
			Nucleo.send(solServidor[0],respServidor);*/
			Nucleo.send(merge_bytes_int(solServidor),respServidor);
		}
		ConectorDNS.deregistrarInterfaz("Server Eduardo", "1.0", idUnico);
	}
	public void pack_response(byte[] solServidor, byte[] respServidor){
		pack_id_into_response(respServidor);
		pack_client_id_into_response(respServidor,solServidor);
		pack_message_into_response(respServidor, solServidor);
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
	
	public void pack_message_into_response(byte[] respServidor, byte[] solServidor){
		String message = unpack_string(solServidor);
		message+= "CODOP:"+get_cod_op_from_request(solServidor);
		respServidor[8] = (byte) message.length();
		byte[] aux = message.getBytes();
		System.arraycopy(aux, 0, respServidor, 9, aux.length);
	}
	
	public short get_cod_op_from_request(byte[] solServidor){
		return merge_bytes_short(solServidor[8], solServidor[9]);
	}
	
	public String unpack_string(byte[] solServidor){
		byte message_length = solServidor[10];
		byte[] aux = new byte[message_length];
		int i;
		int j = 0;
		for(i = 11; i < message_length + 11; i++){
			aux[j] = solServidor[i];
			j++;
		}
		return new String(aux);
	}
	
	public short merge_bytes_short(byte b1, byte b2) {
		return (short) ((b1 << 8) | (b2 & 0xFF));
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
