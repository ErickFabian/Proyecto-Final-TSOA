package sistemaDistribuido.sistema.rpc.modoUsuario;

import sistemaDistribuido.sistema.rpc.modoMonitor.RPC;  //para pr�ctica 4
import java.util.Arrays;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.rpc.modoUsuario.Libreria;
import sistemaDistribuido.util.Escribano;

public class LibreriaCliente extends Libreria{

    /** Fabian Garcia Erick Alfonso
    *  Practica 4
    *  Obtencion del asa destino desde el programa conector con puenteo en RPC 
    */
    public LibreriaCliente(Escribano esc){
            super(esc);
    }

    @Override
    protected void cuadrado() {
        int asaDest = RPC.importarInterfaz("Server", "1.0");  //para pr�ctica 4
        imprimeln("Preparando Buffer de Mensajes ...");
        byte[] solCliente = new byte[14];
        byte[] respCliente = new byte[1024];
        Integer n = (Integer) pila.pop();
        short codop = 0;
        pack_request(solCliente, n.intValue(), codop);
        imprimeln("Señalamiento al Nucleo");
        Nucleo.send(asaDest,solCliente);
        Nucleo.receive(Nucleo.dameIdProceso(),respCliente);
        imprimeln("Respuesta Recibida");
        int result = unpack_result(respCliente, 8);
        imprimeln("Colocando resultado en la pila");
        pila.push(new Integer(result));
    }
    
    protected void factorial(){
        int asaDest = RPC.importarInterfaz("Server", "1.0");  //para pr�ctica 4
        imprimeln("Preparando Buffer de Mensajes ...");
        byte[] solCliente = new byte[14];
        byte[] respCliente = new byte[1024];
        Integer n = (Integer) pila.pop();
        short codop = 1;
        pack_request(solCliente, n.intValue(), codop);
        imprimeln("Señalamiento al Nucleo");
        Nucleo.send(asaDest,solCliente);
        Nucleo.receive(Nucleo.dameIdProceso(),respCliente);
        imprimeln("Respuesta Recibida");
        int result = unpack_result(respCliente, 8);
        imprimeln("Colocando resultado en la pila");
        pila.push(new Integer(result));
    }
     
    protected void moda(){
        int asaDest = RPC.importarInterfaz("Server", "1.0");  //para pr�ctica 4
        imprimeln("Preparando Buffer de Mensajes ...");
        Object[] moda_numbers = pila.toArray();     
        Integer[] integerArray = Arrays.copyOf(moda_numbers, moda_numbers.length, Integer[].class);
        byte[] solCliente = new byte[1024];
        byte[] respCliente = new byte[1024];
        byte[] data = int_array_to_byte_array(integerArray);
        short codop = 2;
        pack_request_n_parameters(solCliente, data, codop);
        imprimeln("Señalamiento al Nucleo");
        Nucleo.send(asaDest,solCliente);
        Nucleo.receive(Nucleo.dameIdProceso(),respCliente);
        imprimeln("Respuesta Recibida");
        int result = unpack_result(respCliente, 8);
        imprimeln("Colocando resultado en la pila");
        pila.push(new Integer(result));
    }
    
    protected void sumatoria(){
        int asaDest = RPC.importarInterfaz("Server", "1.0");  //para pr�ctica 4
        imprimeln("Preparando Buffer de Mensajes ...");
        Object[] moda_numbers = pila.toArray();     
        Integer[] integerArray = Arrays.copyOf(moda_numbers, moda_numbers.length, Integer[].class);
        byte[] solCliente = new byte[1024];
        byte[] respCliente = new byte[1024];
        byte[] data = int_array_to_byte_array(integerArray);
        short codop = 3;
        pack_request_n_parameters(solCliente, data, codop);
        imprimeln("Señalamiento al Nucleo");
        Nucleo.send(asaDest,solCliente);
        Nucleo.receive(Nucleo.dameIdProceso(),respCliente);
        imprimeln("Respuesta Recibida");
        int result = unpack_result(respCliente, 8);
        imprimeln("Colocando resultado en la pila");
        pila.push(new Integer(result));
    }
    
    public void pack_request_n_parameters(byte[] solCliente, byte[] data, short codop){
        pack_cod_op_into_request(solCliente, codop);
        pack_message_data_into_request_n_parameters(solCliente, data);
    }
    
    public void pack_message_data_into_request_n_parameters(byte[] solCliente, byte[] data){
        System.arraycopy(data, 0, solCliente, 10, data.length);
    }
    
    public void merge_into_sol(byte[] solCliente,  byte[] n1, byte[] n2){
        System.arraycopy(n1, 0, solCliente, 10, n1.length);
        System.arraycopy(n2, 0, solCliente, 14, n2.length);
    }
    
    public byte[] merge_data_into_byte_array(int value){ 
        return new byte[] {
            (byte)(value >>> 24),
            (byte)(value >>> 16),
            (byte)(value >>> 8),
            (byte)value};
    }
    public void pack_request(byte[] solCliente, int numero, short codop){
        pack_cod_op_into_request(solCliente, codop);
        pack_message_data_into_request(solCliente, numero);
    }
    
    public void pack_cod_op_into_request(byte[] solCliente, short codop){
        solCliente[8] = (byte) (codop >>>8);
        solCliente[9] = (byte) (codop);
    }
    
    public void pack_message_data_into_request(byte[] solCliente, int numero){
        solCliente[10] = (byte) (numero >>>24);
        solCliente[11] = (byte) (numero >>>16);
        solCliente[12] = (byte) (numero >>>8);
        solCliente[13] = (byte) (numero);
    }
    
    public int unpack_result(byte[] bytes, int start){
        int aux = 0;
        for (int i=start; i<start + 4  && i<bytes.length; i++) {
           aux <<= 8;
           aux |= (int)bytes[i] & 0xFF;
       }
       return aux;
   }

    private byte[] int_array_to_byte_array(Integer[] integerArray) {
        byte[] data = new byte[1012];
        int i = 0;
        for(Integer item : integerArray){
            System.arraycopy(merge_data_into_byte_array(item), 0, data, i, 4);
            i += 4;
        }
        return data;
        
    }
}