/*Morales Guzman Jose Eduardo
 *Codigo: 207452411
 *Seccion D03
 *Practica 5
 */

package sistemaDistribuido.sistema.clienteServidor.modoMonitor;

import java.util.LinkedList;
import java.util.Queue;


public class Buzon {

	private final int MAXIMA_CAPACIDAD = 3;
	
	private Queue<byte[]> buzon;                                                                                                                                                                                                                         
	private int almacenamiento = MAXIMA_CAPACIDAD;
	
	public Buzon(){
		buzon = new LinkedList<byte[]>();
	}
	
	public boolean estaVacio(){
		return buzon.isEmpty();
	}
	
	public int dameEspacioDisponible(){
		return almacenamiento;
	}
	
	public byte[] dameMensaje(){
		byte[] b = buzon.poll();
		almacenamiento += 1;
		return b;
	}
	
	public void insertaMensaje(byte[] mensaje){
		almacenamiento -= 1;
		buzon.offer(mensaje);
	}
	
}

