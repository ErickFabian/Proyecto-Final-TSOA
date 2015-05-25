/*Morales Guzman Jose Eduardo
 *Codigo: 207452411
 *Seccion D03
 *Practica 5
 */

package sistemaDistribuido.sistema.clienteServidor.modoMonitor;

import java.util.Hashtable;

public class AdminReenvios {
	
	private Hashtable<Integer,Mensaje> tablaMensajes = new Hashtable<Integer,Mensaje>();
	
	public void agregarPosibleReenvio(int idProceso,byte[] mensaje, ParMaquinaProceso pmp){
		byte[] mensajeGuardar = new byte[1024];
		System.arraycopy(mensaje, 0, mensajeGuardar, 0, mensaje.length);
		tablaMensajes.put(idProceso, new Mensaje(mensajeGuardar,pmp));
	}
	
	
	public Mensaje dameMensajeAReenviar (int idProceso){
		return tablaMensajes.remove(idProceso);
	}
}
