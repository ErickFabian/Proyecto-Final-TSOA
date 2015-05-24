/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baseCSv2.sistemaDistribuido.sistema.clienteServidor.Eduardo;
import java.nio.charset.Charset;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;

/**
 * 
 */
public class ProcesoCliente extends Proceso{

	/**
	 * 
	 */
	private String codop, texto;
	private enum Opciones {
		Crear,
		Eliminar,
		Leer,
		Escribir
	}
	public ProcesoCliente(Escribano esc){
		super(esc);
		start();
	}

	/**
	 * 
	 */
	public void run(){
		imprimeln("Inicio de proceso cliente");
		Nucleo.suspenderProceso();
		String cadenaRespuesta;
		byte[] solCliente=new byte[1024];
		byte[] respCliente=new byte[1024];
		byte[] arregloRespuesta;
		int x, y;
		byte[] arregloAuxiliar = texto.getBytes(Charset.forName("UTF-8"));
		imprimeln("Generando mensaje a ser enviado");
		pack_request(solCliente);
		imprimeln("Señalamiento al núcleo para envío de mensaje");
                String server_name = "Server Eduardo";
                Nucleo.send(server_name, solCliente);
		imprimeln("Invocando a receive()");
		Nucleo.receive(dameID(),respCliente);
		imprimeln("Procesando respuesta recibida del servidor");
		switch(respCliente[1023]){
			case -1:
				imprimeln("Servidor no disponible, mensaje AU recibido");
				break;
			case -2:
				imprimeln("Servidor no disponible, mensaje TA recibido");
				break;
			default:
				imprimeln("Resultado de la operación");
				imprimeln (unpack_string(respCliente));
		}
	}
	
	public void pack_request(byte[] solCliente){
        pack_id_into_request(solCliente);
        pack_server_id_into_request(solCliente);
        pack_cod_op_into_request(solCliente);
        pack_message_data_into_request(solCliente);
    }
    
    public void pack_id_into_request(byte[] solCliente){
        solCliente[0] = (byte) (0 >>>24);
        solCliente[1] = (byte) (0 >>>16);
        solCliente[2] = (byte) (0 >>>8);
        solCliente[3] = (byte) (0);
    }
    
    public void pack_server_id_into_request(byte[] solCliente){
        solCliente[4] = (byte) (0 >>>24);
        solCliente[5] = (byte) (0 >>>16);
        solCliente[6] = (byte) (0 >>>8);
        solCliente[7] = (byte) (0);
    }
    
    public void pack_cod_op_into_request(byte[] solCliente){
        short s = simbolic_value_of_cod_op();
        solCliente[8] = (byte) (s>>>8);
        solCliente[9] = (byte) (s);
    }
    
    public void pack_message_data_into_request(byte[] solCliente){
        solCliente[10] = (byte) (texto.length());
        byte[] aux = texto.getBytes();
        System.arraycopy(aux, 0, solCliente, 11, aux.length);
    }
    
    public String unpack_string(byte[] solServidor){
        byte message_length = solServidor[8];
        byte[] aux = new byte[message_length];
        int i;
        int j = 0;
        for(i = 9; i < message_length + 9; i++){
            aux[j] = solServidor[i];
            j++;
        }
        return new String(aux);
    }
    
    public short simbolic_value_of_cod_op(){
        short value = 0;
        Opciones codigoOP = Opciones.valueOf(codop);
		switch (codigoOP) {
			case Crear: value = 1; break;
			case Eliminar: value = 2; break; 
			case Leer: value = 3; break;
			case Escribir: value = 4; break;
		}
        return value;
    }

	public void pasarDatos(String com, String text) {
		codop = com;
		texto = text;

	}
}
