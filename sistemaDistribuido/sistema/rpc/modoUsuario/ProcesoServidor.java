package sistemaDistribuido.sistema.rpc.modoUsuario;

import sistemaDistribuido.sistema.rpc.modoUsuario.Datos;
import java.net.InetAddress;
import java.net.UnknownHostException;
import sistemaDistribuido.sistema.rpc.modoMonitor.RPC;   //para pr�ctica 4
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;

/** Fabian Garcia Erick Alfonso
*  Practica 4
*  Implementacion de registro de servidor en programa conector asi como su deregistro
*/
public class ProcesoServidor extends Proceso{
	private LibreriaServidor ls;   //para pr�ctica 3

	/**
	 * 
	 */
	public ProcesoServidor(Escribano esc){
            super(esc);
            ls=new LibreriaServidor(esc);   //para pr�ctica 3
            start();
	}

	/**
	 * Resguardo del servidor
	 */
	public void run(){
            imprimeln("Proceso servidor en ejecucion.");
            Datos asa;
            int idUnico;
            imprimeln("El servidor se esta registrando en Programa Conector");
            
            try 
            {
                asa = new Datos(Nucleo.dameIdProceso(), InetAddress.getLocalHost().getHostAddress());
                
            } catch (UnknownHostException ex) {
                asa = null;
                imprimeln("No se puede obtener ip local");
                Logger.getLogger(ProcesoServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            idUnico = RPC.exportarInterfaz("Server", "1.0", asa);  //para pr�ctica 4
            
            
            byte [] solServidor = new byte[1024];
            byte [] respServidor = null;
            
            while(continuar())
            {
                imprimeln("Invocando a Receive en Proceso Servidor");
                Nucleo.receive(dameID(),solServidor);
                imprimeln("Procesando peticion recivida del cliente");
                short codop = get_cod_op_from_request(solServidor);
                switch (codop)
                {
                    case 0 :
                        respServidor = new byte[12];
                        int numero = merge_bytes_int(solServidor, 10);
                        imprimeln("Desordenando Parametros");
                        imprimeln("Se ha solicitado el cuadrado de un numero");
                        imprimeln("Se realiza la llamada al servidor");
                        int resCuadrado = ls.cuadrado(numero);
                        imprimeln("El servidor ha hecho la operacion");
                        imprimeln("Preparando Mensaje de Respuesta");
                        pack_response(respServidor,resCuadrado);
                        break;
                    case 1 :
                        respServidor = new byte[12];
                        int factorial = merge_bytes_int(solServidor, 10);
                        imprimeln("Desordenando Parametros");
                        imprimeln("Se ha solicitado el factorial de un numero");
                        imprimeln("Se realiza la llamada al servidor");
                        int res_factorial = ls.factorial(factorial);
                        imprimeln("El servidor ha hecho la operacion");
                        imprimeln("Preparando Mensaje de Respuesta");
                        pack_response(respServidor,res_factorial);
                        break;
                    case 2:
                        respServidor = new byte[12];
                        byte[] moda = unpack_parameters(solServidor,10);
                        int[] moda_array = unpack_values_from_message(moda);
                        imprimeln("Desordenando Parametros");
                        imprimeln("Se ha solicitado la moda de un arreglo de numeros");
                        imprimeln("Se realiza la llamada al servidor");
                        int res_moda = ls.moda(moda_array);
                        imprimeln("El servidor ha hecho la operacion");
                        imprimeln("Preparando Mensaje de Respuesta");
                        pack_response(respServidor,res_moda);
                        break;
                    case 3:
                        respServidor = new byte[12];
                        byte[] sum = unpack_parameters(solServidor,10);
                        int[] sum_array = unpack_values_from_message(sum);
                        imprimeln("Desordenando Parametros");
                        imprimeln("Se ha solicitado la sumatoria de un arreglo de numeros");
                        imprimeln("Se realiza la llamada al servidor");
                        int res_sum = ls.sumatoria(sum_array);
                        imprimeln("El servidor ha hecho la operacion");
                        imprimeln("Preparando Mensaje de Respuesta");
                        pack_response(respServidor,res_sum);
                        break;
                    default:
                       respServidor = new byte[1024];
                }
                imprimeln(" Señalamiento al nucleo para envio de respuesta de Servidor");
                Nucleo.send(solServidor[3], respServidor);
            }

            RPC.deregistrarInterfaz("Server", "1.0", idUnico);
	}
        
        public short get_cod_op_from_request(byte[] solServidor){
            return merge_bytes_short(solServidor[8], solServidor[9]);
        }
        
        public short merge_bytes_short(byte b1, byte b2) {
            return (short) ((b1 << 8) | (b2 & 0xFF));
        }
        
         public int merge_bytes_int(byte[] bytes, int start){
             int aux = 0;
             for (int i=start; i<start + 4 && i<bytes.length; i++) {
                aux <<= 8;
                aux |= (int)bytes[i] & 0xFF;
            }
            return aux;
        }
         
        public void pack_response(byte[] respServidor, int resultado){
            respServidor[8]  = (byte) (resultado >>>24);
            respServidor[9]  = (byte) (resultado >>>16);
            respServidor[10] = (byte) (resultado >>>8);
            respServidor[11] = (byte) (resultado);
        }
        
        public void int_to_byte_array(byte[] moda, int resultado){
            moda[0]  = (byte) (resultado >>>24);
            moda[1]  = (byte) (resultado >>>16);
            moda[2] = (byte) (resultado >>>8);
            moda[3] = (byte) (resultado);
        }

    private byte[] unpack_parameters(byte[] solServidor, int start) {
        byte[] data = new byte[1014];
        System.arraycopy(solServidor, 10, data, 0, solServidor.length - 10);
        
        return data;
    }

    private int[] unpack_values_from_message(byte[] moda) {
        int counter = 0;
        boolean invalid_data = true;
        String numbers = "";
        while(invalid_data){
            int number = merge_bytes_int(moda,counter);
            if (number != 0){
                numbers+= number + " ";
            }
            else{
                invalid_data = false;
            }
            counter += 4;
        }
        
        return extract_values_from_string(numbers);
    }
    
    private int[] extract_values_from_string(String numbers){
        String[] moda_parts = numbers.split(" ");
        int [] numeros = new int[moda_parts.length];
        for(int i = 0;i < moda_parts.length;i++)
        {
           numeros[i] = Integer.parseInt(moda_parts[i]);
        }
        return numeros;
    }
    
}
