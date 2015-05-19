package sistemaDistribuido.sistema.rpc.modoUsuario;

import java.awt.TextField;
import java.util.Arrays;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;
import sistemaDistribuido.util.Pausador;

/** Fabian Garcia Erick Alfonso
 *  Practica 3
 *  Modificacion de metodo de obtencion de datos desde interfaz para realizacion de RPC
 *  Asi como tratamiento de datos desde interfaz y las llamadas a RPC correspondientes
 */
public class ProcesoCliente extends Proceso{
	private Libreria lib;
        int cuadrado = 0;
        int factorial = 0;
        int[] moda;
        int[] sum;
	/**
	 * 
	 */
	public ProcesoCliente(Escribano esc){
		super(esc);
		//lib=new LibreriaServidor(esc);  //primero debe funcionar con esta para subrutina servidor local
		lib=new LibreriaCliente(esc);  //luego con esta comentando la anterior, para subrutina servidor remota
		start();
	}

	/**
	 * Programa Cliente
	 */
        
	public void run(){

            imprimeln("Proceso cliente en ejecucion.");
            imprimeln("Esperando datos para continuar.");
            Nucleo.suspenderProceso();
            imprimeln("Salio de suspenderProceso");

            int resultado;
            
            resultado=lib.cuadrado(this.cuadrado);
            imprimeln("Cuadrado= "+resultado);
            Pausador.pausa(2000);
            
            resultado=lib.factorial(this.factorial);
            imprimeln("Factorial= "+resultado);
            Pausador.pausa(2000);
            
            resultado=lib.moda(this.moda);
            imprimeln("Moda= "+resultado);
            Pausador.pausa(2000);
            
            resultado=lib.sumatoria(this.sum);
            imprimeln("Sumatoria= "+resultado);

            imprimeln("Fin del cliente.");
	}
        
        public void get_data(String text, String text0, String text1, String text2) {
            this.cuadrado = Integer.parseInt(text);
            this.factorial = Integer.parseInt(text0);
            get_moda_values(text1);
            get_sum_values(text2);
        }
        
        public void get_moda_values(String text1){
            String[] moda_parts = text1.split(" ");
            this.moda = new int[moda_parts.length];
            for(int i = 0;i < moda_parts.length;i++)
            {
               moda[i] = Integer.parseInt(moda_parts[i]);
            }
        }
        
         public void get_sum_values(String text2){
            String[] sum_parts = text2.split(" ");
            this.sum = new int[sum_parts.length];
            for(int i = 0;i < sum_parts.length;i++)
            {
               sum[i] = Integer.parseInt(sum_parts[i]);
            }
        }
}
