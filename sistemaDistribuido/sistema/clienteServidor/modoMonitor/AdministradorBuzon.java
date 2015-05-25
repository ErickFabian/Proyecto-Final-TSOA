/*Morales Guzman Jose Eduardo
 *Codigo: 207452411
 *Seccion D03
 *Practica 5
 */

package sistemaDistribuido.sistema.clienteServidor.modoMonitor;
import java.util.Hashtable;


public class AdministradorBuzon {

	private Hashtable<Integer,Buzon> buzones;
	
	public AdministradorBuzon(){
		buzones = new Hashtable<Integer,Buzon>();
	}
	
	public Buzon dameBuzon(int idProceso){
		return buzones.remove(idProceso);
	}
	
	public Buzon existeBuzon(int idProceso){
		return buzones.get(idProceso);
	}
	
	public void insertaBuzon(int idProceso,Buzon buzon){
		buzones.put(idProceso, buzon);
	}
	
	public void solicitarBuzon(int idProceso){
		buzones.put(idProceso, new Buzon());
	}
	
}

