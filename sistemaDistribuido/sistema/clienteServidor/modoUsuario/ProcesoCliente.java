package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;

/** Fabian Garcia Erick Alfonso
 * Practica 5
 * Modificacion en Nucleo.send usando una cadena como primer parametro que es el nombre del servidor
 */

public class ProcesoCliente extends Proceso{
    private String cod_op, data;
    private static final int CREATE = 1;
    private static final int DELETE = 2;
    private static final int READ = 3;
    private static final int WRITE = 4;
    /**
     * 
     */
    public ProcesoCliente(Escribano esc){
        super(esc);
        start();
    }

    /**
     * 
     */
    public void run(){
        imprimeln("Proceso cliente en ejecucion.");
        imprimeln("Esperando datos para continuar.");
        Nucleo.suspenderProceso();
        byte[] solCliente = new byte[1024];
        byte[] respCliente=new byte[1024];
        imprimeln("Generando mensaje a ser enviado, llenando los campos necesarios");
        pack_request(solCliente);
        imprimeln("Señalamiento al nucleo para envio de mensaje");
        String server_name = "Server Erick";
        Nucleo.send(server_name,solCliente);
        imprimeln("Invocando Receive");
        Nucleo.receive(dameID(),respCliente);
        imprimeln("Procesando peticion recibida del servidor.");
        imprimeln("El mensaje de respuesta del servidor es:"+unpack_string(respCliente));
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
        solCliente[10] = (byte) (data.length());
        byte[] aux = data.getBytes();
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
        switch(cod_op)
        {
            case "Crear": 
                value = CREATE;
                break;
            case "Eliminar":
                value = DELETE;
                break;
            case "Leer":
                value = READ;
                break;
            case "Escribir":
                value = WRITE;
                break;
        }
        return value;
    }
    
    public void get_data(String cod_op, String data){
        this.cod_op = cod_op;
        this.data = data;
    }
}
